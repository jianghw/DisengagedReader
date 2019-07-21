package android.lifecycle

/**
 * 内部类，可以接收任何生命周期更改并将其发送给接收方
 */
interface GenericLifecycleObserver : LifecycleObserver {

    fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event)
}
