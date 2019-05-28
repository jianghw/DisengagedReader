package com.jianghw.lib.arithmetic

fun main(args: Array<String>) {

}

class CircularQueueKotlin constructor(_size: Int) {

    var size: Int
    var head: Int
    var tail: Int
    var queue: Array<Any?>

    init {
        size = _size
        head = -1
        tail = -1
        queue = arrayOfNulls(size)
    }

    constructor() : this(8)

    fun <T : Any?> ad(value: T): Boolean {
        if (isFull()) {
            return false
        }
        if (isEmpty()) {
            head = 0
        }
        tail = (tail + 1) % size
        queue[tail] = value
        return true
    }

    fun re(): Boolean {
        if (isEmpty()) {
            return false
        }
        head = (head + 1) % head
        return true
    }

    fun isFull(): Boolean {

        return true
    }

    fun isEmpty(): Boolean {
        return true
    }

    fun front() {}

    fun rear() {

    }

    fun getQueue() {}

    fun toArray() {}
}

