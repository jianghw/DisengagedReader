package com.magnificent.jianghw.jetpacklib

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MainObserver(var lifecycle: Lifecycle, var callback: MainObserverCallback) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun doCreate() {
        callback.doPrintCreate()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun doStart() {
        callback.doPrintStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun doResume() {
        callback.doPrintResume()
    }
}