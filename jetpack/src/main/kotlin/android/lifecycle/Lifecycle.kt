package android.lifecycle

import androidx.annotation.MainThread
import androidx.annotation.NonNull

abstract class Lifecycle {

    @MainThread
    abstract fun addObserver(@NonNull observer: LifecycleObserver)

    @MainThread
    abstract fun removeObserver(@NonNull observer: LifecycleObserver)


    enum class Event {
        ON_CREATE,
        ON_START,
        ON_RESUME,
        ON_PAUSE,
        ON_STOP,
        ON_DESTROY,
        ON_ANY
    }

    enum class State {
        DESTROYED,
        INITIALIZED,
        CREATED,
        STARTED,
        RESUMED;

        fun isAtLeast(@NonNull state: State): Boolean {
            return compareTo(state) > 0
        }
    }
}