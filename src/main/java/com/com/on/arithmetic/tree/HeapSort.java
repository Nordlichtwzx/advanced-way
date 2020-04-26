package com.com.on.arithmetic.tree;

import java.util.Arrays;

public class HeapSort {

    public boolean less(Comparable[] heap, int i, int j) {
        return heap[i].compareTo(heap[j]) < 0;
    }

    public void exch(Comparable[] heap, int i, int j) {
        Comparable temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }


    public void createHeap(Comparable[] source, Comparable[] heap) {
        //为了方便操作将元数组拷贝进heap中，当前的heap未排序，并且第一个元素为空
        System.arraycopy(source, 0, heap, 1, source.length);
        //根据堆的结构，在长度除以2得到的索引对应的值与最后一位索引之间的值都为叶子结点，没有下沉的必要
        for (int i = heap.length / 2; i > 0; i--) {
            sink(heap, i, heap.length - 1);
        }
    }


    public void sort(Comparable[] source) {
        Comparable[] heap = new Comparable[source.length + 1];
        createHeap(source, heap);
        //堆中元素的数量
        int n = heap.length - 1;
        //每次交换第一个元素和末尾的元素，以为第一个元素是最大值，交换后再将第一个元素下沉
        //这样循环每次下沉结束后第一个元素都是最大的元素，最终得到的heap就是从小到大排列
        while (n != 1) {
            exch(heap, 1, n);
            n--;
            sink(heap, 1, n);
        }
        System.arraycopy(heap, 1, source, 0, source.length);
    }

    public void sink(Comparable[] heap, int target, int range) {
        //下沉算法，根据给定的范围下沉
        while (2 * target + 1 <= range) {
            int max = target;
            if (less(heap, 2 * target, 2 * target + 1)) {
                max = 2 * target + 1;
            } else {
                max = 2 * target;
            }
            if (less(heap, max, target)) {
                break;
            }
            exch(heap, target, max);
            target = max;
        }
    }

    public static void main(String[] args) {
        Integer[] arr = {1, 2, 5, 4, 4, 6, 3, 4, 4, 3, 3, 4, 22, 1, 1, 2, 2, 3, 3, 4, 43, 34, 34, 34, 43, 3, 89, 32, 3543, 54325, 432, 1234, 5423, 5432, 312, 42315};
        HeapSort heapSort = new HeapSort();
        heapSort.sort(arr);
        System.out.println(Arrays.toString(arr));
    }

}
