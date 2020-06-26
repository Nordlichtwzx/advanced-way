package com.on.jvm;

public class Strong {

    public static void main(String[] args) {
        //这里的stringBuffer就是强引用
        StringBuffer stringBuffer = new StringBuffer("hello world");
    }
}
