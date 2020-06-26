package com.on.juc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Nordlicht
 * 读写锁案例
 * 将读锁和写锁区分开来
 * <p>
 * 如果不加锁，就会破坏数据的原子性，没有一次性完成所有操作，有可能在写的过程中有别的线程插入进来进行写的操作
 */
public class ReadWriteBlockDemo {

    public static void main(String[] args) {
        MyCache myCache = new MyCache();

        for (int i = 0; i < 5; i++) {
            final int temp = i;
            new Thread(() -> {
                myCache.put(String.valueOf(temp), String.valueOf(temp));
            }, String.valueOf(i)).start();
        }

        for (int i = 0; i < 5; i++) {
            final int temp = i;
            new Thread(() -> {
                myCache.get(String.valueOf(temp));
            }, String.valueOf(i)).start();
        }

    }
}

class MyCache {
    private final Map<String, String> map = new HashMap();

    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void put(String key, String value) {
        readWriteLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "开始写入数据");
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + "数据写入完成");
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public String get(String key) {
        readWriteLock.readLock().lock();
        String s = "";
        try {
            System.out.println(Thread.currentThread().getName() + "开始读取数据");
            s = map.get(key);
            System.out.println(Thread.currentThread().getName() + "数据读取完成");
        } finally {
            readWriteLock.readLock().unlock();
        }
        return s;
    }
}
