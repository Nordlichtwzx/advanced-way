package com.on.jvm;

import java.util.concurrent.TimeUnit;

/**
 * -Xms1G -Xmx1G -XX:+DoEscapeAnalysis -XX:+PrintGC
 * 执行时间：5ms
 *
 * @author Nordlicht
 */
public class StackAllocation {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            alloc();
        }
        System.out.println(System.currentTimeMillis() - start + "ms");
        try {
            TimeUnit.SECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void alloc() {
        User user = new User();
    }
}

class User {

}
