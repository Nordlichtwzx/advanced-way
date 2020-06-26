package com.on.jvm;

public class TestString {

    public static void main(String[] args) {
        /*
         * 这一行代码创建了几个对象？两个
         * 1.在堆中创建string对象
         * 2.在字符串常量池中创建了abc
         * */
        String string = "abc";

        /*
         * 这一行代码创建了几个对象？
         * 1.因为有+号操作，所以先创建一个StringBuilder对象
         * 2.在堆中创建一个new String()对象
         * 3.在字符串常量池中创建一个“a”对象
         * 4.在堆中创建一个new String()对象
         * 5.在字符串常量池中创建一个“b”对象
         * 6.StringBuilder在最后调用了一个toString方法，
         *      toString方法中new了一个String对象，但是这个对象只存储在堆中，字符串常量池中没有
         * */
        String testString = "a" + "b";
    }

    public void add() {
        int a = 0;
        int b = 0;
        int c = a + b;
        System.out.println(c);
    }
}
