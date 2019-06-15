package com.jianghw.lib.pool;

import java.util.ArrayList;
import java.util.List;

public class LinkedPoolsTest {

    public static void main(String[] args) {

       /* LinkedPools linkedPools = LinkedPools.obtain();
        linkedPools.what = 0;
        LinkedPools linkedPools1 = LinkedPools.obtain();
        linkedPools1.what = 1;
        LinkedPools linkedPools2 = LinkedPools.obtain();
        linkedPools2.what = 2;
        LinkedPools linkedPools3 = LinkedPools.obtain();
        linkedPools3.what = 3;

        linkedPools.release();
        linkedPools1.release();
        linkedPools2.release();
        linkedPools3.release();

        LinkedPools linkedPools4 = LinkedPools.obtain();
        System.out.println(linkedPools4.what);
        LinkedPools linkedPools5 = LinkedPools.obtain();
        System.out.println(linkedPools5.what);*/

        List<LinkedPools> linkedPoolsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LinkedPools linkedPools = LinkedPools.obtain();
            linkedPools.what = i;
            linkedPoolsList.add(linkedPools);
        }

        for (LinkedPools linkedPools : linkedPoolsList) {
            System.out.println(linkedPools.release());
        }

        for (int i = 0; i < 10; i++) {
            LinkedPools linkedPools = LinkedPools.obtain();
            System.out.println(linkedPools.what);
            linkedPools.release();
        }
        System.out.println("=====================");
        for (int i = 0; i < 10; i++) {
            LinkedPools linkedPools = LinkedPools.obtain();
            System.out.println(linkedPools.what);
        }
    }
}
