package com.on.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadOrderAccess {

    public static void main(String[] args) {
        ShareResource shareResource = new ShareResource();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareResource.print5();
            }
        }, "A").start();
        new Thread(() -> {
//            for (int i = 0; i <10 ; i++) {
            shareResource.print10();
//            }
        }, "B").start();
        new Thread(() -> {
//            for (int i = 0; i <10 ; i++) {
            shareResource.print15();
//            }
        }, "C").start();
    }
}


class ShareResource {
    Lock lock = new ReentrantLock();
    Condition condition1 = lock.newCondition();
    Condition condition2 = lock.newCondition();
    Condition condition3 = lock.newCondition();
    private int num = 1;

    public void print5() {
        lock.lock();
        try {
            while (num != 1) {
                condition1.await();
            }
            for (int i = 0; i < 5; i++) {
                System.out.println("a" + "\t" + i);
            }
            num = 2;
            condition2.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print10() {
        lock.lock();
        try {
            while (num != 2) {
                condition2.await();
            }
            for (int i = 0; i < 10; i++) {
                System.out.println("b" + "\t" + i);
            }
            num = 3;
            condition3.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print15() {
        lock.lock();
        try {
            while (num != 3) {
                condition3.await();
            }
            for (int i = 0; i < 15; i++) {
                System.out.println("c" + "\t" + i);
            }
            num = 1;
            condition1.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}