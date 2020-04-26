package com.com.on.arithmetic.tree;

public class Heap<T extends Comparable<T>> {
    /**
     * 存储堆中的元素
     */
    private T[] value;
    /**
     * 记录堆中元素的个数
     */
    private int N;

    /**
     * 创建堆
     *
     * @param capacity
     */
    public Heap(int capacity) {
        value = (T[]) new Comparable[capacity + 1];
    }

    /**
     * 判断堆中i处的元素是否小于j处的元素
     *
     * @param i
     * @param j
     * @return
     */
    public boolean less(int i, int j) {
        return value[i].compareTo(value[j]) < 0;
    }

    /**
     * 交换堆中的元素
     *
     * @param i
     * @param j
     */
    public void exch(int i, int j) {
        T temp = value[i];
        value[i] = value[j];
        value[j] = temp;
    }

    /**
     * 往堆中插入一个元素
     *
     * @param t
     */
    public void insert(T t) {
        //第一步，先将元素插入到数组尾部
        value[++N] = t;
        swim(N);
    }

    /**
     * 使用上浮算法，使得索引k中的元素能处于一个正确的位置
     * 通过循环，，不断的比较当前节点和其父节点，如果当前节点大于其父节点，则交换。
     *
     * @param k
     */
    public void swim(int k) {
        while (k > 1) {
            if (less(k / 2, k)) {
                exch(k / 2, k);
            }
            k = k / 2;
        }
    }

    /**
     * 删除堆中的最大元素
     *
     * @return
     */
    public T deleteMax() {
        T max = value[1];
        //第一步，交换首末节点
        exch(1, N);
        //删除末尾节点
        value[N] = null;
        //元素个数-1
        N--;
        //通过下沉，让其有序
        sink(1);
        return max;
    }

    /**
     * 使用下沉算法，不断的比当前节点和其子节点中较大的元素，然后进行交换
     *
     * @param k
     */
    public void sink(int k) {
        while (2 * k <= N) {
            int max = 1;
            if (value[2 * k + 1] != null) {
                if (less(2 * k, 2 * k + 1)) {
                    max = 2 * k + 1;
                } else {
                    max = 2 * k;
                }
            } else {
                max = 2 * k;
            }
            if (less(max, k)) {
                //如果当前节点大于max节点，那就不用交化，说明已经到位了
                break;
            }
            exch(k, max);
            k = max;
        }
    }
}
