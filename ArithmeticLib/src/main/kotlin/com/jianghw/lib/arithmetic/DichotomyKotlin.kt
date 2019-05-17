package com.jianghw.lib.arithmetic

class DichotomyKotlin {
    fun main(args: Array<String>) {
        val src: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
        val position: Int = binarySearch(src, 8)
        print(position)
    }

    private fun <T : Comparable<T>?> binarySearch(srcList: List<T>, item: T): Int {
        var low = 0
        var high: Int = srcList.size - 1
        while (low <= high) {
            val middle: Int = (low + high) / 2
            val temp: T = srcList[middle]
            if (temp == item) {
                return middle
            }
            if (temp!! > item) {
                high = middle - 1
            } else {
                low = middle + 1
            }
        }
        return -1
    }
}
