package com.hongwei.jiang.lib.kotlin

class LooperThread : Thread() {
    init {
        name = "LooperThread"
    }
    private var mHandler: Handler? = null
    override fun run() {
        Looper.prepare()
        mHandler = Handler()
        Looper.loop()
    }
}