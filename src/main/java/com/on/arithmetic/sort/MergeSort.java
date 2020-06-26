package com.on.arithmetic.sort;

import java.util.Arrays;

/**
 * 归并排序
 *
 * @author Nordlicht
 */
public class MergeSort {
    public void merge(int[] arr, int left, int right, int rightBound) {
        int mid = right - 1;
        int i = left;
        int j = right;
        int k = 0;
        int[] temp = new int[rightBound - left + 1];
        while (i <= mid && j <= rightBound) {
            if (arr[i] <= arr[j]) {
                temp[k] = arr[i];
                i++;
                k++;
            } else {
                temp[k] = arr[j];
                j++;
                k++;
            }
        }
        while (i <= mid) {
            temp[k++] = arr[i++];
        }
        while (j <= rightBound) {
            temp[k++] = arr[j++];
        }
        for (int m = 0; m < temp.length; m++) {
            arr[left + m] = temp[m];
        }
        System.out.println(Arrays.toString(arr));
    }
}
