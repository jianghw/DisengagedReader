package com.hongwei.lib.thread

class ThreadGroupDemo : Runnable {
    override fun run() {
        val message = "${Thread.currentThread().threadGroup.name}::${Thread.currentThread().name}"
        while (true) {
            try {
                Thread.sleep(3000)
                println(message)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}

fun main() {
    val threadGroup = ThreadGroup("thread_group")
    val thread_1 = Thread(threadGroup, ThreadGroupDemo(), "t_1")
    val thread_2 = Thread(threadGroup, ThreadGroupDemo(), "t_2")
    thread_1.start()
    thread_2.start()
    println("${threadGroup.activeCount()}")
    threadGroup.list()
}