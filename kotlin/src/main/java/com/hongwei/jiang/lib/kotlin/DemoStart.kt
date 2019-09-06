package com.hongwei.jiang.lib.kotlin


fun main() {
    println("current thread running :${Thread.currentThread().name}")
    Looper.prepare()
    LooperThread().start()
    Looper.loop()
    println("main is running end")
}