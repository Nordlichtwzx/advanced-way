package com.on.jvm;

import java.lang.ref.SoftReference;

public class Soft {
    public static void main(String[] args) {
        SoftReference softReference = new SoftReference<>(new StringBuffer("Hello Soft"));

        //上面一行代码等价于下面三行代码

        //声明强引用
        StringBuilder stringBuilder = new StringBuilder("Hello Soft");
        SoftReference softReference1 = new SoftReference<>(stringBuilder);
        //销毁强引用
        stringBuilder = null;
    }
}
