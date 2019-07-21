package android.lifecycle

import java.lang.IllegalArgumentException

class FullLifecycleObserverAdapter(private val observer: FullLifecycleObserver) : GenericLifecycleObserver {

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> observer.onCreate(source)
            Lifecycle.Event.ON_START -> observer.onStart(source)
            Lifecycle.Event.ON_RESUME -> observer.onResume(source)
            Lifecycle.Event.ON_PAUSE -> observer.onPause(source)
            Lifecycle.Event.ON_STOP -> observer.onStop(source)
            Lifecycle.Event.ON_DESTROY -> observer.onDestroy(source)
            Lifecycle.Event.ON_ANY -> throw IllegalArgumentException("ON_ANY must not been send by anybody")

        }
    }

}
