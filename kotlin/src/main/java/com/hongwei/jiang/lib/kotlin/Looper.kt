package com.hongwei.jiang.lib.kotlin

class Looper {

    var _array = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)

    companion object {
        val sThreadLocal = ThreadLocal<Looper>()

        fun prepare() {
            sThreadLocal.set(Looper())
        }

        fun loop() {
            val looper = myLooper()
            try {
                val iterator = looper?._array?.iterator()
                while (true) {
                    if (iterator?.hasNext()!!) {
                        val next = iterator.next()
                        Thread.sleep(1000)
                        println("${Thread.currentThread().name} thread next :${next}")
                        if (next == 8) {
                            break
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            println("${Thread.currentThread().name} thread end ")
        }

        private fun myLooper(): Looper? {
            return sThreadLocal.get()
        }
    }
}