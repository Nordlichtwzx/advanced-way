package com.on.arithmetic.sort;

import java.util.Arrays;

/**
 * 快速排序
 *
 * @author Nordlicht
 */
public class QuickSort {
    public void QuickSort(int[] arr, int left, int right) {
        if (left >= right) {
            return;
        }
        int l = left + 1;
        int r = right;
        int pivot = arr[left];
        while (l < r) {
            /**
             * 注意数组越界
             */
            while (arr[l] < pivot && l < r) {
                l++;
            }
            while (arr[r] >= pivot && l <= r) {
                r--;
            }
            if (l < r) {
                int temp = arr[l];
                arr[l] = arr[r];
                arr[r] = temp;
            }
        }
        arr[left] = arr[r];
        arr[r] = pivot;
        System.out.println(Arrays.toString(arr));
        QuickSort(arr, left, r - 1);
        QuickSort(arr, r + 1, right);
    }
}
