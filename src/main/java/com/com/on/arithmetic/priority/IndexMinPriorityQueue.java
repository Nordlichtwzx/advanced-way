package com.com.on.arithmetic.priority;

public class IndexMinPriorityQueue<T extends Comparable<T>> {
    //存储堆中的元素
    private T[] items;
    //保存每个元素在items数组中的索引，heap数组需要堆有序
    private int[] heap;
    //保存heap的逆序，heap的值作为索引，heap的索引作为值
    private int[] index;
    //记录堆中元素的个数
    private int N;


    public IndexMinPriorityQueue(int capacity) {
        this.items = (T[]) new Comparable[capacity + 1];
        this.heap = new int[capacity + 1];
        this.index = new int[capacity + 1];
        N = 0;

        for (int i = 0; i < index.length; i++) {
            index[i] = -1;
        }
    }

    //获取队列中元素的个数
    public int size() {
        return N;
    }

    //判断队列是否为空
    public boolean isEmpty() {
        return N == 0;
    }

    //判断堆中索引i处的元素是否小于索引j处的元素
    //因为heap数组相当于真的优先队列，所以比较数据也是通过heap数组的索引找到items中对应的值
    private boolean less(int i, int j) {
        return items[heap[i]].compareTo(items[heap[j]]) < 0;
    }

    //交换堆中i索引和j索引处的值
    //其实就是交换heap中的位置
    private void exch(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        //交换完heap以后再交换index
        index[heap[i]] = i;
        index[heap[j]] = j;
    }

    //判断k对应的元素是否存在
    public T contains(int k) {
        //index数组就相当于items的简化版，所以只需要判断index中k索引是否有数据就可以
        //不等于-1说明已经存在
        if (index[k] == -1) {
            return null;
        } else {
            return items[k];
        }
    }

    //最小元素关联的索引
    public int minIndex() {
        return heap[1];
    }


    //往队列中插入一个元素,并关联索引i
    public void insert(int i, T t) {
        if (contains(i) != null) {
            return;
        }
        items[i] = t;
        N++;
        heap[N] = i;
        index[i] = N;
        swim(N);
    }

    //删除队列中最小的元素,并返回该元素关联的索引
    public int delMin() {
        int minIndex = heap[1];
        exch(1, N);
        index[heap[N]] = -1;
        items[heap[N]] = null;
        heap[N] = -1;
        N--;
        sink(1);
        return minIndex;
    }

    //删除索引i关联的元素
    public void delete(int i) {
        if (!(contains(i) != null)) {
            return;
        }
        int k = index[i];
        exch(k, N);
        index[heap[N]] = -1;
        items[heap[N]] = null;
        heap[N] = -1;
        N--;
        swim(k);
        sink(k);
    }

    //把与索引i关联的元素修改为为t
    public void changeItem(int i, T t) {
        items[i] = t;
        int k = index[i];
        swim(k);
        sink(k);
    }


    //使用上浮算法，使索引k处的元素能在堆中处于一个正确的位置
    private void swim(int k) {
        while (k > 1) {
            if (less(k, k / 2)) {
                exch(k, k / 2);
            }
            k = k / 2;
        }
    }


    //使用下沉算法，使索引k处的元素能在堆中处于一个正确的位置
    private void sink(int k) {
        while (2 * k <= N) {
            int min;
            if (2 * k + 1 <= N) {
                if (less(2 * k, 2 * k + 1)) {
                    min = 2 * k;
                } else {
                    min = 2 * k + 1;
                }
            } else {
                min = 2 * k;
            }
            if (less(k, min)) {
                break;
            }
            exch(k, min);
            k = min;
        }
    }

    public static void main(String[] args) {
        String[] arr = {"S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        IndexMinPriorityQueue<String> indexMinPQ = new IndexMinPriorityQueue<>(20);
        //插入
        for (int i = 0; i < arr.length; i++) {
            indexMinPQ.insert(i, arr[i]);
        }
        System.out.println(indexMinPQ.size());
        //获取最小值的索引
        System.out.println(indexMinPQ.minIndex());
        //测试修改
        indexMinPQ.changeItem(0, "Z");
        int minIndex = -1;
        while (!indexMinPQ.isEmpty()) {
            minIndex = indexMinPQ.delMin();
            System.out.print(arr[minIndex] + ",");
        }
        System.out.println();
        IndexMinPriorityQueue<String> stringIndexMinPriorityQueue = new IndexMinPriorityQueue<>(3);
        stringIndexMinPriorityQueue.insert(0, "A");
        stringIndexMinPriorityQueue.insert(1, "D");
        stringIndexMinPriorityQueue.insert(2, "F");

        stringIndexMinPriorityQueue.changeItem(1, "Z");

        while (!stringIndexMinPriorityQueue.isEmpty()) {
            System.out.println(stringIndexMinPriorityQueue.delMin());
        }
    }

}

