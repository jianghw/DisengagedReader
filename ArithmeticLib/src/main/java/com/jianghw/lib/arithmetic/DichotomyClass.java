package com.jianghw.lib.arithmetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * dichotomy
 *
 * @author hongwei.jiang
 */
public class DichotomyClass {

    public static void main(String[] args) {
        List<Integer> src = new ArrayList<>();
        Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        Collections.addAll(src, array);
        int position = binarySearch(src, 10);
        System.out.println(position);
    }

    private static <T extends Number> int binarySearch(List<T> srcList, T item) {
        int size = srcList.size();
        int low = 0, high = size - 1;
        while (low <= high) {
            int middle = (high + low) / 2;
            T temp = srcList.get(middle);
            if (temp.equals(item)) {
                return middle;
            }
            if (temp.doubleValue() > item.doubleValue()) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        return -1;
    }
}
