/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.lifecycle

import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.Lifecycle.State.CREATED
import androidx.lifecycle.Lifecycle.State.DESTROYED
import androidx.lifecycle.Lifecycle.State.INITIALIZED
import androidx.lifecycle.Lifecycle.State.RESUMED
import androidx.lifecycle.Lifecycle.State.STARTED

import android.annotation.SuppressLint
import android.util.Log

import androidx.annotation.MainThread
import androidx.arch.core.internal.FastSafeIterableMap

import java.lang.ref.WeakReference
import java.util.ArrayList
import kotlin.collections.Map.Entry

/**
 * An implementation of [Lifecycle] that can handle multiple observers.
 *
 *
 * It is used by Fragments and Support Library Activities. You can also directly use it if you have
 * a custom LifecycleOwner.
 */
class LifecycleRegistry
/**
 * Creates a new LifecycleRegistry for the given provider.
 *
 *
 * You should usually create this inside your LifecycleOwner class's constructor and hold
 * onto the same instance.
 *
 * @param provider The owner LifecycleOwner
 */
    (@NonNull provider: LifecycleOwner) : Lifecycle() {

    /**
     * Custom list that keeps observers and can handle removals / additions during traversal.
     *
     * Invariant: at any moment of time for observer1 & observer2:
     * if addition_order(observer1) < addition_order(observer2), then
     * state(observer1) >= state(observer2),
     */
    @SuppressLint("RestrictedApi")
    private val mObserverMap = FastSafeIterableMap()
    /**
     * Current state
     */
    @get:NonNull
    @get:Override
    var currentState: State? = null
        private set
    /**
     * The provider that owns this Lifecycle.
     * Only WeakReference on LifecycleOwner is kept, so if somebody leaks Lifecycle, they won't leak
     * the whole Fragment / Activity. However, to leak Lifecycle object isn't great idea neither,
     * because it keeps strong references on all other listeners, so you'll leak all of them as
     * well.
     */
    private val mLifecycleOwner: WeakReference<LifecycleOwner>

    private var mAddingObserverCounter = 0

    private var mHandlingEvent = false
    private var mNewEventOccurred = false

    // we have to keep it for cases:
    // void onStart() {
    //     mRegistry.removeObserver(this);
    //     mRegistry.add(newObserver);
    // }
    // newObserver should be brought only to CREATED state during the execution of
    // this onStart method. our invariant with mObserverMap doesn't help, because parent observer
    // is no longer in the map.
    private val mParentStates = ArrayList()

    private val isSynced: Boolean
        get() {
            if (mObserverMap.size() === 0) {
                return true
            }
            val eldestObserverState = mObserverMap.eldest().getValue().mState
            val newestObserverState = mObserverMap.newest().getValue().mState
            return eldestObserverState === newestObserverState && currentState === newestObserverState
        }

    /**
     * The number of observers.
     *
     * @return The number of observers.
     */
    val observerCount: Int
        @SuppressWarnings("WeakerAccess")
        get() = mObserverMap.size()

    init {
        mLifecycleOwner = WeakReference(provider)
        currentState = INITIALIZED
    }

    /**
     * Moves the Lifecycle to the given state and dispatches necessary events to the observers.
     *
     * @param state new state
     */
    @SuppressWarnings("WeakerAccess")
    @MainThread
    fun markState(@NonNull state: State) {
        moveToState(state)
    }

    /**
     * Sets the current state and notifies the observers.
     *
     *
     * Note that if the `currentState` is the same state as the last call to this method,
     * calling this method has no effect.
     *
     * @param event The event that was received
     */
    fun handleLifecycleEvent(@NonNull event: Lifecycle.Event) {
        val next = getStateAfter(event)
        moveToState(next)
    }

    private fun moveToState(next: State) {
        if (currentState === next) {
            return
        }
        currentState = next
        if (mHandlingEvent || mAddingObserverCounter != 0) {
            mNewEventOccurred = true
            // we will figure out what to do on upper level.
            return
        }
        mHandlingEvent = true
        sync()
        mHandlingEvent = false
    }

    private fun calculateTargetState(observer: LifecycleObserver): State {
        val previous = mObserverMap.ceil(observer)

        val siblingState = if (previous != null) previous!!.getValue().mState else null
        val parentState = if (!mParentStates.isEmpty())
            mParentStates.get(mParentStates.size() - 1)
        else
            null
        return min(min(currentState, siblingState), parentState)
    }

    @Override
    fun addObserver(@NonNull observer: LifecycleObserver) {
        val initialState = if (currentState === DESTROYED) DESTROYED else INITIALIZED
        val statefulObserver = ObserverWithState(observer, initialState)
        val previous = mObserverMap.putIfAbsent(observer, statefulObserver)

        if (previous != null) {
            return
        }
        val lifecycleOwner = mLifecycleOwner.get()
            ?: // it is null we should be destroyed. Fallback quickly
            return

        val isReentrance = mAddingObserverCounter != 0 || mHandlingEvent
        var targetState = calculateTargetState(observer)
        mAddingObserverCounter++
        while (statefulObserver.mState!!.compareTo(targetState) < 0 && mObserverMap.contains(observer)) {
            pushParentState(statefulObserver.mState)
            statefulObserver.dispatchEvent(lifecycleOwner, upEvent(statefulObserver.mState!!))
            popParentState()
            // mState / subling may have been changed recalculate
            targetState = calculateTargetState(observer)
        }

        if (!isReentrance) {
            // we do sync only on the top level.
            sync()
        }
        mAddingObserverCounter--
    }

    private fun popParentState() {
        mParentStates.remove(mParentStates.size() - 1)
    }

    private fun pushParentState(state: State?) {
        mParentStates.add(state)
    }

    @Override
    fun removeObserver(@NonNull observer: LifecycleObserver) {
        // we consciously decided not to send destruction events here in opposition to addObserver.
        // Our reasons for that:
        // 1. These events haven't yet happened at all. In contrast to events in addObservers, that
        // actually occurred but earlier.
        // 2. There are cases when removeObserver happens as a consequence of some kind of fatal
        // event. If removeObserver method sends destruction events, then a clean up routine becomes
        // more cumbersome. More specific example of that is: your LifecycleObserver listens for
        // a web connection, in the usual routine in OnStop method you report to a server that a
        // session has just ended and you close the connection. Now let's assume now that you
        // lost an internet and as a result you removed this observer. If you get destruction
        // events in removeObserver, you should have a special case in your onStop method that
        // checks if your web connection died and you shouldn't try to report anything to a server.
        mObserverMap.remove(observer)
    }

    private fun forwardPass(lifecycleOwner: LifecycleOwner) {
        val ascendingIterator = mObserverMap.iteratorWithAdditions()
        while (ascendingIterator.hasNext() && !mNewEventOccurred) {
            val entry = ascendingIterator.next()
            val observer = entry.getValue()
            while (observer.mState!!.compareTo(currentState) < 0 && !mNewEventOccurred
                && mObserverMap.contains(entry.getKey())
            ) {
                pushParentState(observer.mState)
                observer.dispatchEvent(lifecycleOwner, upEvent(observer.mState!!))
                popParentState()
            }
        }
    }

    private fun backwardPass(lifecycleOwner: LifecycleOwner) {
        val descendingIterator = mObserverMap.descendingIterator()
        while (descendingIterator.hasNext() && !mNewEventOccurred) {
            val entry = descendingIterator.next()
            val observer = entry.getValue()
            while (observer.mState!!.compareTo(currentState) > 0 && !mNewEventOccurred
                && mObserverMap.contains(entry.getKey())
            ) {
                val event = downEvent(observer.mState!!)
                pushParentState(getStateAfter(event))
                observer.dispatchEvent(lifecycleOwner, event)
                popParentState()
            }
        }
    }

    // happens only on the top of stack (never in reentrance),
    // so it doesn't have to take in account parents
    private fun sync() {
        val lifecycleOwner = mLifecycleOwner.get()
        if (lifecycleOwner == null) {
            Log.w(LOG_TAG, "LifecycleOwner is garbage collected, you shouldn't try dispatch " + "new events from it.")
            return
        }
        while (!isSynced) {
            mNewEventOccurred = false
            // no need to check eldest for nullability, because isSynced does it for us.
            if (currentState!!.compareTo(mObserverMap.eldest().getValue().mState) < 0) {
                backwardPass(lifecycleOwner)
            }
            val newest = mObserverMap.newest()
            if (!mNewEventOccurred && newest != null
                && currentState!!.compareTo(newest!!.getValue().mState) > 0
            ) {
                forwardPass(lifecycleOwner)
            }
        }
        mNewEventOccurred = false
    }

    internal class ObserverWithState(observer: LifecycleObserver, var mState: State?) {
        var mLifecycleObserver: GenericLifecycleObserver? = null

        init {
            mLifecycleObserver = Lifecycling.getCallback(observer)
        }

        fun dispatchEvent(owner: LifecycleOwner, event: Event) {
            val newState = getStateAfter(event)
            mState = min(mState, newState)
            mLifecycleObserver!!.onStateChanged(owner, event)
            mState = newState
        }
    }

    companion object {

        private val LOG_TAG = "LifecycleRegistry"

        internal fun getStateAfter(event: Event): State {
            when (event) {
                ON_CREATE, ON_STOP -> return CREATED
                ON_START, ON_PAUSE -> return STARTED
                ON_RESUME -> return RESUMED
                ON_DESTROY -> return DESTROYED
                ON_ANY -> {
                }
            }
            throw IllegalArgumentException("Unexpected event value $event")
        }

        private fun downEvent(state: State): Event {
            when (state) {
                INITIALIZED -> throw IllegalArgumentException()
                CREATED -> return ON_DESTROY
                STARTED -> return ON_STOP
                RESUMED -> return ON_PAUSE
                DESTROYED -> throw IllegalArgumentException()
            }
            throw IllegalArgumentException("Unexpected state value $state")
        }

        private fun upEvent(state: State): Event {
            when (state) {
                INITIALIZED, DESTROYED -> return ON_CREATE
                CREATED -> return ON_START
                STARTED -> return ON_RESUME
                RESUMED -> throw IllegalArgumentException()
            }
            throw IllegalArgumentException("Unexpected state value $state")
        }

        internal fun min(@NonNull state1: State, @Nullable state2: State?): State {
            return if (state2 != null && state2!!.compareTo(state1) < 0) state2 else state1
        }
    }
}
