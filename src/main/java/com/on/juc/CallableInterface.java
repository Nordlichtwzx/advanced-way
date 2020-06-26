package com.on.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author Nordlicht
 * 线程的三种实现方式
 * 1.实现Runable接口
 * 2.继承Thread类
 * 3.实现Callable接口
 * <p>
 * Callable接口和Runable接口的区别
 * 1.Callable接口调用call方法，Runnable接口调用run方法，方法名不同
 * 2.Callable接口有返回值，Runnable没有
 * 3.Callable接口会抛出异常，Runable没有
 * <p>
 * Callable实现的细节：
 * 1.如果需要开始一个新的线程，需要new Thread，但是，Thread的构造方法中不能直接放入Callable接口
 * 2.所以这个时候就需要引入一个中间类，就是FutureTask类，这个类实现了Runnable接口，
 * 并且它的构造方法中需要传入一个Callable接口，这样就可以将Runnable和Callable联系起来
 * 3.如果两个线程调用一个FutureTask，那么这个FutureTask只会执行一次。
 * 4.最好在最后调用FutureTask的get方法，这样不会造成主线程的阻塞。
 */

class MyCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("come in====");
        TimeUnit.SECONDS.sleep(4);
        return 1024;
    }
}

public class CallableInterface {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Callable callable = new MyCallable();
        FutureTask futureTask = new FutureTask<>(callable);
        new Thread(futureTask, "A").start();
        new Thread(futureTask, "B").start();

        System.out.println("执行完成");
        System.out.println(futureTask.get());
    }
}
