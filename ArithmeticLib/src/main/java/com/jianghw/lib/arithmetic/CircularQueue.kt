package com.jianghw.lib.arithmetic

import java.util.*

/**
 * 循环队列  【涉及指针的运用】
 *
 * @author hongwei.jiang
 */
class CircularQueue<T> {

    private val size: Int

    /**
     * 第一个数据位置or空格后面数据的位置
     */
    private var head: Int = 0
    /**
     * 最后一个数据位置or满栈后再加数据的位置
     */
    private var tail: Int = 0

    private lateinit var queue:kotlin.Array<Any?>

    val isEmpty: Boolean
        get() = head == -1

    val isFull: Boolean
        get() = (tail + 1) % size == head

    @JvmOverloads
    constructor(size: Int = 8) {
        this.size = size
        this.queue = arrayOfNulls(size)
        head = -1
        tail = -1
    }

    constructor(clz: Class<T>, size: Int) {
        this.size = size
        //this.queue = kotlin.Array.newInstance(clz, size)
        head = -1
        tail = -1
    }

    /**
     * 加第一个值 head=0,tail=0
     * 加第二个值 head=0,tail=1
     * 加满 head=0,tail=size-1
     */
    fun ad(value: T): Boolean {
        if (isFull) {
            return false
        }
        if (isEmpty) {
            head = 0
        }
        tail = (tail + 1) % size
        queue[tail] = value
        return true
    }

    /**
     * 每删除一个头数据，指示器向后移一位
     */
    fun de(): Boolean {
        if (isEmpty) {
            return false
        }
        if (head == tail) {
            head = -1
            tail = -1
            return true
        }
        head = (head + 1) % size
        return true
    }

    fun front(): Any? {
        return if (isEmpty) {
            null
        } else queue[head]
    }

    fun rear(): Any? {
        return if (isEmpty) {
            null
        } else queue[tail]
    }

    fun print():kotlin.Array<Any?> {
        return queue
    }

    companion object {

        @JvmStatic
        fun main(args: kotlin.Array<String>) {
//            val queue = CircularQueue(Float::class.java, 2)
//            println(queue.ad(4f))
//            println(queue.ad(5f))
//            println(queue.ad(6f))
//            println(queue.de())
//            println(queue.front()!!.toString())
//            val floats = queue.print()
//            println(Arrays.toString(floats))


            val queueClass = CircularQueue<Float>(4)
            println(queueClass.ad(4f))
            println(queueClass.ad(5f))
            println(queueClass.ad(6f))
            println(queueClass.ad(7f))
            println(queueClass.ad(8f))
            println(queueClass.de())
            println(queueClass.front()!!.toString())
            //it is error
            val integers = queueClass.print()
            println(Arrays.toString(integers))
        }
    }
}
