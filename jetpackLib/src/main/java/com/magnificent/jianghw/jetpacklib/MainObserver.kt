package com.magnificent.jianghw.jetpacklib

import androidx.lifecycle.common.Lifecycle
import androidx.lifecycle.common.LifecycleObserver
import androidx.lifecycle.common.OnLifecycleEvent

class MainObserver(var lifecycle: Lifecycle, var callback: MainObserverCallback) :
    LifecycleObserver {
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