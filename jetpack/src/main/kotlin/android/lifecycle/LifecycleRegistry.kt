package android.lifecycle

import android.annotation.SuppressLint
import androidx.annotation.NonNull
import androidx.arch.core.internal.FastSafeIterableMap
import java.lang.ref.WeakReference


class LifecycleRegistry(provider: LifecycleOwner) : Lifecycle() {

    private val mParentStates = ArrayList<State>()

    private val mHandlingEvent: Boolean = false
    private val mAddingObserverCounter: Int = 0

    private var mState: State
    private val mLifecycleOwner: WeakReference<LifecycleOwner>

    init {
        mLifecycleOwner = WeakReference<LifecycleOwner>(provider)
        mState = State.INITTALIZED
    }

    companion object {
        const val LOG_TAG: String = "LifecycleRegistry"
    }

    @SuppressLint("RestrictedApi")
    private val mObserverMap = FastSafeIterableMap<LifecycleObserver, ObserverWithState>()


    @SuppressLint("RestrictedApi")
    override fun addObserver(@NonNull observer: LifecycleObserver) {
        val initialState: State = if (mState == State.DESROYED) State.DESROYED else State.INITTALIZED
        val statefulObserver = ObserverWithState(observer, initialState)
        //不存在时，放入 返回null
        val previous: ObserverWithState = mObserverMap.putIfAbsent(observer, statefulObserver)
        if (previous != null) {
            return
        }
        val lifecycleOwner: LifecycleOwner = mLifecycleOwner.get()
            ?: return

        val isReentrance = mAddingObserverCounter != 0 || mHandlingEvent
        //目标状态
        val targetState: State? = calculateTargetState(observer)
    }

    override fun removeObserver(observer: LifecycleObserver) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 计算目标状态
     */
    @SuppressLint("RestrictedApi")
    private fun calculateTargetState(observer: LifecycleObserver): State? {
        //返回在与给定键关联的条目之前添加的条目
        val previous = mObserverMap.ceil(observer)
        val siblingState: State? = previous?.value?.mState
        val parentState: State? = if (mParentStates.isNotEmpty()) mParentStates[mParentStates.size - 1] else null
//        return min(min(mState,siblingState),parentState)
        return parentState
    }

    class ObserverWithState(observer: LifecycleObserver, initialState: State) {
        val mState: State = initialState
        val mLifecycleObserver = observer
    }
}