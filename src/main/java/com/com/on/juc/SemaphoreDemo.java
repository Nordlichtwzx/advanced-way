package com.com.on.juc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author Nordlicht
 * <p>
 * 信号量
 * acquire(获取)：当一个线程调用acquire操作时，他要么通过成功获取信号量，（信号量减一），要么一直等下去，直到有线程释放信号量或者超时
 * release(释放):实际上会将信号量的值加一，然后唤醒等待的线程
 * <p>
 * 信号量主要用于两个目的，一个是用于多个共享资源的互斥使用（那不就是synchronize吗），另一个用于并发控制线程数的控制
 * <p>
 * 当Semaphore的构造参数为1的时候，相当于模拟锁，并且可以控制每一个锁内部执行的时间
 */
public class SemaphoreDemo {

    public static void main(String[] args) {
        //假设有三个资源类，六个线程同时抢占
        Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                try {
                    //当前线程占有一个资源
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "抢占了资源");
                    //占有该资源四秒钟
                    TimeUnit.SECONDS.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                    System.out.println(Thread.currentThread().getName() + "释放了资源");
                }
            }, String.valueOf(i)).start();
        }
    }
}
