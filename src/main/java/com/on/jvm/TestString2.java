package com.on.jvm;

/**
 * 在jdk1.6之前，这句话返回值是false,jdk7/8返回的是true
 * 因为在jdk6及之前，字符串常量池时存放在方法区中的，但是在jdk7以后，字符串常量池被放到了堆空间中
 *
 * @author Nordlicht
 */
public class TestString2 {

    public static void main(String[] args) {
        /*
         * 这一步会在堆空间中生成一个new String(“ab”)对象
         * */
        String str = "a" + "b";
        str.intern();
        /*
         * 在调用完intern方法后，字符串常量池中的“ab”就指向了堆空间中“ab”的地址，字符串常量池中的ab存储的就是堆空间中ab的引用，
         * 所以在后面生成str2时，就相当于str2直接指向了字符串常量池中的ab，从而间接的指向了堆空间中的ab，
         * 而str是直接指向堆空间中的ab，所以最后输出true
         *
         * */
        String str2 = "ab";
        System.out.println(str == str2);//jdk1.6:false jdk7/8:true
    }
}
