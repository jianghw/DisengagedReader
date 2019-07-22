package android.lifecycle

import android.annotation.SuppressLint
import androidx.annotation.NonNull
import androidx.arch.core.internal.FastSafeIterableMap
import java.lang.ref.WeakReference


class LifecycleRegistry(provider: LifecycleOwner) : Lifecycle() {

    private val mParentStates = ArrayList<State>()

    private val mHandlingEvent: Boolean = false
    private var mAddingObserverCounter: Int = 0

    private var mState: State
    private val mLifecycleOwner: WeakReference<LifecycleOwner>

    init {
        mLifecycleOwner = WeakReference<LifecycleOwner>(provider)
        mState = State.INITIALIZED
    }

    companion object {
        const val LOG_TAG: String = "LifecycleRegistry"
    }

    @SuppressLint("RestrictedApi")
    private val mObserverMap = FastSafeIterableMap<LifecycleObserver, ObserverWithState>()


    @SuppressLint("RestrictedApi")
    override fun addObserver(@NonNull observer: LifecycleObserver) {
        val initialState: State = if (mState == State.DESTROYED) State.DESTROYED else State.INITIALIZED
        val statefulObserver = ObserverWithState(observer, initialState)
        //不存在时，放入 返回null
        val previous: ObserverWithState = mObserverMap.putIfAbsent(observer, statefulObserver)
        if (previous != null) {
            return
        }
        val lifecycleOwner: LifecycleOwner = mLifecycleOwner.get()
            ?: return
        //是否重入
        val isReentrance = mAddingObserverCounter != 0 || mHandlingEvent
        //目标状态
        val targetState: State = calculateTargetState(observer)
        mAddingObserverCounter++
        while ((statefulObserver.mState < targetState)
            && mObserverMap.contains(observer)
        ) {
            pushParentState(statefulObserver.mState)
            statefulObserver.dispatchEvent(lifecycleOwner, upEvent(statefulObserver.mState))
            popParentState()
        }
    }

    private fun upEvent(state: State): Event {
        return when (state) {
            State.INITIALIZED, State.DESTROYED -> Event.ON_CREATE
            State.CREATED -> Event.ON_START
            State.STARTED -> Event.ON_RESUME
            State.RESUMED -> throw IllegalArgumentException()
            else -> throw IllegalArgumentException("Unexpected state value $state")
        }
    }

    private fun pushParentState(state: State) {
        mParentStates.add(state)
    }

    private fun popParentState() {
        mParentStates.removeAt(mParentStates.size - 1)
    }

    override fun removeObserver(observer: LifecycleObserver) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 计算目标状态
     */
    @SuppressLint("RestrictedApi")
    private fun calculateTargetState(observer: LifecycleObserver): State {
        //返回在与给定键关联的条目之前添加的条目
        val previous = mObserverMap.ceil(observer)
        val siblingState: State? = previous?.value?.mState
        val parentState: State? = if (mParentStates.isNotEmpty()) mParentStates[mParentStates.size - 1] else null
        return min(min(mState, siblingState), parentState)
    }

    internal fun min(@NonNull state1: State, state2: State?): State {
        return if (state2 != null && state2!!.compareTo(state1) < 0) state2 else state1
    }

    class ObserverWithState(observer: LifecycleObserver, initialState: State) {
        val mState: State = initialState
        val mLifecycleObserver = observer

        fun dispatchEvent(lifecycleOwner: LifecycleOwner, event: Event) {
        }
    }
}