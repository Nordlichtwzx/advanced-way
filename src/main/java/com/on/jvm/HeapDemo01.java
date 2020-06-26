package com.on.jvm;

import java.util.concurrent.TimeUnit;

public class HeapDemo01 {

    public static void main(String[] args) {
        try {
            TimeUnit.SECONDS.sleep(100);
            System.out.println("堆空间测试01");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
