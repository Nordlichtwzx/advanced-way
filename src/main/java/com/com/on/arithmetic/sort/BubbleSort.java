package com.com.on.arithmetic.sort;


/**
 * 冒泡排序
 *
 * @author Nordlicht
 */
public class BubbleSort {
    public int[] MaoPao(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            boolean flag = false;
            for (int j = i; j < arr.length; j++) {
                if (arr[j] < arr[i]) {
                    flag = true;
                    int temp = arr[j];
                    arr[j] = arr[i];
                    arr[i] = temp;
                }
            }
            if (flag == false) {
                break;
            } else {
                flag = false;
            }
        }
        return arr;
    }
}
