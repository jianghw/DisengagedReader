package android.lifecycle

/**
 * 处理生命周期转换等的内部类
 */
class Lifecycling {

    companion object Callback {
        fun getCallback(any: Any): GenericLifecycleObserver {
            if (any is FullLifecycleObserver) return FullLifecycleObserverAdapter((FullLifecycleObserver) any)
        }
    }
}