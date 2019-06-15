package com.jianghw.lib.pool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongwei.jiang
 */
public class ArrayPoolsTest {
    private int saveData;

    private void setTestData(int i) {
        this.saveData = i;
    }

    public int getTestData() {
        return this.saveData;
    }

    private static final ArrayPools.SynchronizedPool<ArrayPoolsTest> TEST_SYNCHRONIZED_POOL
            = new ArrayPools.SynchronizedPool<>(10);

    public static ArrayPoolsTest obtain() {
        ArrayPoolsTest acquire = TEST_SYNCHRONIZED_POOL.acquire();
        return acquire != null ? acquire : new ArrayPoolsTest();
    }

    public boolean release() {
        return TEST_SYNCHRONIZED_POOL.release(this);
    }

    public static void main(String[] args) {

        List<ArrayPoolsTest> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ArrayPoolsTest arrayPoolsTest = ArrayPoolsTest.obtain();
            arrayPoolsTest.setTestData(i);
            list.add(arrayPoolsTest);
        }

        for (ArrayPoolsTest arrayPoolsTest : list) {
            arrayPoolsTest.release();
        }

        for (int i = 0; i < 10; i++) {
            ArrayPoolsTest arrayPoolsTest = ArrayPoolsTest.obtain();
            System.out.println(arrayPoolsTest.getTestData());
            arrayPoolsTest.setTestData(i * 5);
        }

        for (int i = 0; i < 10; i++) {
            ArrayPoolsTest arrayPoolsTest = ArrayPoolsTest.obtain();
            System.out.println(arrayPoolsTest.getTestData());
        }
    }
}
