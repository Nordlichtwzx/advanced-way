package com.com.on.juc;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Nordlicht
 * <p>
 * ArrayList 是线程不安全的,如果在高并发的情况下对一个ArrayList执行读写操作，会出现
 * java.util.ConcurrentModificationException异常
 * <p>
 * 解决方案：
 * 1.使用Vector
 * 2.使用Collections.synchronizedList(arrayList)
 * 3.使用CopyOnWriteArrayList
 */
public class ArrayListIsNotSafe {
    public static void main(String[] args) {
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        ArrayList arrayList = new ArrayList<String>();
        Vector vector = new Vector();
        List synchronizedList = Collections.synchronizedList(arrayList);

//        for (int i = 0; i < 30; i++) {
//            new Thread(() -> {
//                arrayList.add(UUID.randomUUID().toString().substring(0, 8));
//                System.out.println(arrayList);
//            }).start();
//        }

//        for (int i = 0; i < 30; i++) {
//            new Thread(() -> {
//                vector.add(UUID.randomUUID().toString().substring(0, 8));
//                System.out.println(vector);
//            }).start();
//        }

        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                synchronizedList.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(synchronizedList);
            }).start();
        }

    }
}
