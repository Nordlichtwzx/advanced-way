package com.com.on.juc;

import java.util.concurrent.TimeUnit;

class Phone {
    public static synchronized void sendEmail() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Email");
    }

    public static synchronized void sendSMS() {
        System.out.println("SMS");
    }

    public void hello() {
        System.out.println("hello");
    }
}


/**
 * 1.标准访问，先打印邮件还是短信
 * 2.邮件方法暂停4秒钟，先答应邮件还是短信
 * <p>
 * 一个对象里面如果有多个synchronized方法，某一时刻，只要有一个线程去调用了其中的一个synchronized方法了
 * 其他的线程都只能等待，换句话说，某一时刻内，只能有唯一一个线程去访问这些synchronized方法
 * 所得是当前对象this，被锁定后，其他的线程都不能进入到当前对象的其他的synchronized方法
 * <p>
 * <p>
 * 3.新增一个普通方法hello，先打印邮件还是hello
 * 普通方法与同步锁无关所以不影响普通方法的执行
 * <p>
 * 4.两部手机，先打印邮件还是短信
 * 两部手机，相当于两个对象，不存在两者互相影响的现象
 * <p>
 * 5.两个静态同步方法，同一部手机，先打印邮件还是短信
 * 静态方法中加同步锁，锁的是class本省，所以跟第一种情况一样
 * <p>
 * 6.两个静态同步方法，2部手机，先打印邮件还是短信
 * 所有的静态同步方法用的锁都是class的一把锁，所以和情况一一样
 * <p>
 * 7.一个普通同步方法，一个静态同步方法，一部手机，先打印邮件还是短信
 * 一个用的是对象的锁，一个用的是class文件的锁，互相不影响
 * <p>
 * 8.一个普通同步方法，一个静态同步方法，两部手机，先打印邮件还是短信
 * 同理，普通方法不用同步锁，静态同步方法使用的class文件的锁，所以互不影响
 */
public class Lock8 {
    public static void main(String[] args) {
        Phone phone = new Phone();
        Phone phone1 = new Phone();
        new Thread(() -> {
            Phone.sendEmail();
        }, "A").start();
        new Thread(() -> {
//            phone.sendSMS();
//            phone1.sendSMS();
            phone1.hello();
        }, "B").start();
    }
}
