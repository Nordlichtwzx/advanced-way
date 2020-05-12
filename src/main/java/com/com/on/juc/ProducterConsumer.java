package com.com.on.juc;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Nordlicht
 * <p>
 * 在使用线程调度的时候必须要使用while循环，而不是if
 * 如果使用if会造成线程的虚假唤醒
 * 拿这个举例，有可能两个生产者都在wait，但这时一个消费者执行完成，会notifyall所有线程，
 * 如果用if不会再进行判断就会执行后面的操作，这样就会导致两个生产者同时执行，会对num进行两次加操作
 * <p>
 * 高聚合前提下，线程操作资源类
 * 判断、干活、通知
 * 多线程交互中，必须要防止多线程的虚假唤醒。也即（判断只是用while，不使用if）
 * 标志位
 */
public class ProducterConsumer {

    public static void main(String[] args) {
        Source source = new Source();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    source.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    source.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    source.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    source.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "D").start();
    }
}


class Source {
    /*
     * 用lock替换synchronized，就是将wait和notify替换为await和signalAll;
     * */
    Lock lock = new ReentrantLock();

    /*
     * 使用synchronized实现
     * */
    /*public synchronized void increment() throws InterruptedException {
        while (num != 0) {
            this.wait();
        }
        num++;
        System.out.println(Thread.currentThread().getName() + "\t" + num);
        this.notifyAll();
    }

    public synchronized void decrement() throws InterruptedException {
        while (num == 0) {
            this.wait();
        }
        num--;
        System.out.println(Thread.currentThread().getName() + "\t" + num);
        this.notifyAll();
    }*/
    Condition condition = lock.newCondition();
    private int num = 0;

    public void increment() throws InterruptedException {
        lock.lock();
        try {
            while (num != 0) {
                condition.await();
            }
            num++;
            System.out.println(Thread.currentThread().getName() + "\t" + num);
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void decrement() throws InterruptedException {
        lock.lock();
        try {
            while (num == 0) {
                condition.await();
            }
            num--;
            System.out.println(Thread.currentThread().getName() + "\t" + num);
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}