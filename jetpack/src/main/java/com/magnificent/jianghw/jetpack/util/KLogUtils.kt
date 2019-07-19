package com.magnificent.jianghw.jetpacklib.util


class Log {
    companion object {
        fun v(tag: String?, msg: String?, tr: Throwable): Int {
            return android.util.Log.v(tag, msg, tr)
        }

        fun d(tag: String?, msg: String?, tr: Throwable): Int {
            return android.util.Log.d(tag, msg, tr)
        }
    }

}