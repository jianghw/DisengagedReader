package android.lifecycle

interface LifecycleOwner {
    fun getLifecycle(): Lifecycle
}