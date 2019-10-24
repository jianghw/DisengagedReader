package com.jianghw.lib.xjava;

public class SingleTest {

    private static volatile SingleTest mInstance = null;
    private int count = 10;

    public static SingleTest getInstance() {
        if (mInstance == null) {
            synchronized (SingleTest.class) {
                if (mInstance == null) {
                    mInstance = new SingleTest();
                }
            }
        }
        return mInstance;
    }

    public void setCount(int count) {
        this.count = count;

        System.out.println("setcount:" + SingleTest.getInstance().getCount());
    }

    public int getCount() {
        return count;
    }
}
