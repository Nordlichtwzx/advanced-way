package com.com.on.juc;

import java.util.concurrent.*;

/**
 * @author Nordlicht
 * 线程池
 * 创建线程池的三种方式
 */
public class ThreadPoolDemo {
    public static void main(String[] args) {
        //创建固定线程数的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        //创建只有一个线程的线程池
        ExecutorService executorService1 = Executors.newSingleThreadExecutor();
        //自动扩容的创建线程池，线程池中线程的数量自动扩容
        ExecutorService executorService2 = Executors.newCachedThreadPool();

        //手写线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                3,
                5,
                2,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );


        for (int i = 0; i < 9; i++) {
            threadPoolExecutor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "执行了");
            });
        }
    }
}
