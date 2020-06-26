package com.on.arithmetic.sort;

import java.util.Arrays;

/**
 * 希尔排序
 *
 * @author Nordlicht
 */
public class HillSort {
    public void HillSort(int[] arr) {
        int num = (arr.length + 1) / 2;
        int count = 0;
        while (num > 0) {
            for (int i = 0; i < num; i++) {
                for (int j = i + num; j < arr.length; j += num) {
                    if (arr[i] > arr[j]) {
                        int temp = arr[i];
                        arr[i] = arr[j];
                        arr[j] = temp;
                    }
                }
            }
//           System.out.println(Arrays.toString(arr));
            count++;
            num = num / 2;
        }
        System.out.println("总共" + count + "次");
    }


    /**
     * 将每一次的排序都看作是一次插入排序，将大的值每次后移num
     *
     * @param arr
     */
    public void HillInsertSort(int[] arr) {
        int num = arr.length / 2;
        int count = 0;
        while (num > 0) {
            for (int i = num; i < arr.length; i++) {
                int index = i - num;
                int insertValue = arr[i];
                while (index >= 0 && arr[index] > insertValue) {
                    arr[index + num] = arr[index];
                    index -= num;
                }
                arr[index + num] = insertValue;
            }
            System.out.println(Arrays.toString(arr));
            count++;
            num = num / 2;
        }
        System.out.println("总共" + count + "次");
    }
}
