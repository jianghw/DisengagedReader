package android.lifecycle

import kotlin.reflect.KClass

/**
 * 处理生命周期转换等的内部类
 */
class Lifecycling {

    companion object Static {
        val sCallbackCache = HashMap<KClass<out Any>, Int>()

        fun getCallback(any: Any): GenericLifecycleObserver {
            if (any is FullLifecycleObserver) return FullLifecycleObserverAdapter(any as FullLifecycleObserver)
            if (any is GenericLifecycleObserver) return any as GenericLifecycleObserver

            val klass = any::class
            val type: Int? = getObserverConstructorType(klass)
        }

        private fun getObserverConstructorType(klass: KClass<out Any>): Int? {
            if (sCallbackCache.containsKey(klass)) {
                return sCallbackCache.get(klass)
            }
            val type: Int = resolveObserverCallbackType(klass)
        }

        private fun resolveObserverCallbackType(klass: KClass<out Any>): Int {
        }
    }
}