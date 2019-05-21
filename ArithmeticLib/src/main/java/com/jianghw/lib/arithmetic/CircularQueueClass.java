package com.jianghw.lib.arithmetic;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 循环队列  【涉及指针的运用】
 *
 * @author hongwei.jiang
 */
public class CircularQueueClass {

    private final int size;
    private final Object[] queue;
    /**
     * 第一个数据位置or空格后面数据的位置
     */
    private int head;
    /**
     * 最后一个数据位置or满栈后再加数据的位置
     */
    private int tail;

    public static void main(String[] args) {
        CircularQueueClass queue = new CircularQueueClass(Float.class, 2);
        System.out.println(queue.ad(4f));
        System.out.println(queue.ad(5f));
        System.out.println(queue.ad(6f));
        System.out.println(queue.de());
        System.out.println(queue.front().toString());
        Float[] floats = queue.print();
        System.out.println(Arrays.toString(floats));


        CircularQueueClass queueClass = new CircularQueueClass(4);
        System.out.println(queueClass.ad(4));
        System.out.println(queueClass.ad(5));
        System.out.println(queueClass.ad(6));
        System.out.println(queueClass.ad(7));
        System.out.println(queueClass.ad(8));
        System.out.println(queueClass.de());
        System.out.println(queueClass.front().toString());
        //it is error
        Integer[] integers = queueClass.print();
        System.out.println(Arrays.toString(integers));
    }

    public CircularQueueClass() {
        this(8);
    }

    public CircularQueueClass(int size) {
        this.size = size;
        this.queue = new Object[size];
        head = -1;
        tail = -1;
    }

    public <T> CircularQueueClass(Class<T> clz, int size) {
        this.size = size;
        this.queue = (T[]) Array.newInstance(clz, size);
        head = -1;
        tail = -1;
    }

    /**
     * 加第一个值 head=0,tail=0
     * 加第二个值 head=0,tail=1
     * 加满 head=0,tail=size-1
     */
    public <T> boolean ad(T value) {
        if (isFull()) {
            return false;
        }
        if (isEmpty()) {
            head = 0;
        }
        tail = (tail + 1) % size;
        queue[tail] = value;
        return true;
    }

    /**
     * 每删除一个头数据，指示器向后移一位
     */
    public boolean de() {
        if (isEmpty()) {
            return false;
        }
        if (head == tail) {
            head = -1;
            tail = -1;
            return true;
        }
        head = (head + 1) % size;
        return true;
    }

    public <T> T front() {
        if (isEmpty()) {
            return null;
        }
        return (T) queue[head];
    }

    public <T> T rear() {
        if (isEmpty()) {
            return null;
        }
        return (T) queue[tail];
    }

    public boolean isEmpty() {
        return head == -1;
    }

    public boolean isFull() {
        return ((tail + 1) % size) == head;
    }

    public <T> T[] print() {
        return (T[]) queue;
    }
}
