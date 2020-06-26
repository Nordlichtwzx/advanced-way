package com.on.jvm;

/**
 * @author Nordlicht
 * 1.设置堆空间大小的参数
 * -Xms:用来设置堆空间（年轻代+老年代）的初始内存大小
 * -X:是jvm的运行内存
 * ms:memory start
 * -Xmx:用来设置堆空间（年轻代—+老年代）的最大内存大小
 * 2.默认堆空间的大小
 * 初始内存大小：电脑物理内存的 1/64
 * 最大内存大小：电脑物理内存的 1/4
 * 3.手动设置 -Xms600m -Xmx600m
 * 开发中建议设置初始内存和最大内存为相同的值，因为在堆内存扩容或者见效的过程中也会有额外的消耗，
 * 同理线程池、连接池也可以按照这种思路
 * 4.查看设置的参数
 * 1.命令行
 * jps：查看进程
 * jstat -gc 进程id：查看该进程运行时内存
 * 2.-XX:+PrintGCDetails
 * 这个指令只会在程序执行完以后输出
 */
public class HeapSpace {
    public static void main(String[] args) {
        //返回Java虚拟机中的堆内存总量
        long totalMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024;
        //返回Java虚拟机中的试图使用的最大堆内存
        long maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024;

        System.out.println("-Xms" + totalMemory);
        System.out.println("-Xmx" + maxMemory);

    }
}
