package android.lifecycle

import kotlin.reflect.KClass

/**
 * 处理生命周期转换等的内部类
 */
object Lifecycling {


    val sCallbackCache = HashMap<KClass<out Any>, Int>()

    fun getCallback(any: Any): GenericLifecycleObserver? {
        if (any is FullLifecycleObserver) return FullLifecycleObserverAdapter(any as FullLifecycleObserver)
        if (any is GenericLifecycleObserver) return any as GenericLifecycleObserver

        val klass = any::class
        val type: Int? = getObserverConstructorType(klass)
        return null
    }

    private fun getObserverConstructorType(klass: KClass<out Any>): Int? {
        if (sCallbackCache.containsKey(klass)) {
            return sCallbackCache.get(klass)
        }
        val type: Int = resolveObserverCallbackType(klass)
        return 0
    }

    private fun resolveObserverCallbackType(klass: KClass<out Any>): Int {
        return 0
    }

}