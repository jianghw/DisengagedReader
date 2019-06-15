package com.jianghw.lib.pool;

public final class LinkedPools {
    static final int FLAG_IN_USR = 1 << 0;
    static final int FLAG_ASYNCHRONOUS = 1 << 1;
    private static final int MAX_POOL_SIZE = 5;


    private static LinkedPools sCurrentPools;
    private LinkedPools next;
    private static int sPoolSize;
    private static Object mLock = new Object();

    public int what;
    public int arg1;
    public int arg2;
    public Object obj;

    /**
     * 获得
     * 这个 sPool 单链表和我们平时使用的单链表略有不同，不同之处在于 sPool 是在链表的头部插入和删除链表节点的
     */
    public static LinkedPools obtain() {
        synchronized (mLock) {
            if (sCurrentPools != null) {
                LinkedPools linkedPools = sCurrentPools;
                sCurrentPools = linkedPools.next;
                linkedPools.next = null;
                sPoolSize--;
                return linkedPools;
            }
        }
        return new LinkedPools();
    }

    /**
     * 释放
     */
    public boolean release() {
        synchronized (mLock) {
            if (sPoolSize < MAX_POOL_SIZE) {
//                what = 0;
//                arg1 = 0;
//                arg2 = 0;
//                obj = null;
                next = sCurrentPools;
                sCurrentPools = this;
                sPoolSize++;
                return true;
            }
        }
        return false;
    }
}
