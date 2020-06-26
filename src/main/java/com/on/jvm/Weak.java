package com.on.jvm;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class Weak {
    public static void main(String[] args) {
        WeakReference softReference = new WeakReference<>(new StringBuffer("Hello Weak"));

        //上面一行代码等价于下面三行代码

        //声明强引用
        StringBuilder stringBuilder = new StringBuilder("Hello Weak");
        WeakReference softReference1 = new WeakReference<>(stringBuilder);
        //销毁强引用
        stringBuilder = null;

        StringBuilder stringBuilder1 = new StringBuilder("hello phantom");
        ReferenceQueue referenceQueue = new ReferenceQueue();
        PhantomReference phantomReference = new PhantomReference(stringBuilder1, referenceQueue);
        stringBuilder1 = null;
    }
}
