package com.on.jvm;

/**
 * 测试堆空间常用的参数
 * -XX:+PrintFlagsInitial：查看所有的参数的默认初始值
 * -XX:+PrintFlagsFinal：查看所有的参数的最终值（可能会存在修改，不再是默认）
 * 具体查看某个参数的指令：jps：查看当前运行的进程
 * jinfo -flag SurvivorRatio 进程id
 * -Xms：初始堆内存大小（物理内存的1/64）
 * -Xmx：最大堆内存大小（物理内存的1/4）
 * -Xmn：设置新生代大小（初始值及最大值）
 * -XX:NewRatio：设置新生代与老年代的比例
 * -XX:SurvivorRatio：设置新生代中Eden与S0/S1区的比例
 * 如果Eden区占比过大，那么就会使得Minor GC失去意义，因为Survivor区太小，执行完Minor GC后可能会存在对象无法复制到Survivor区，那就回直接放入到老年代
 * 如果Eden区占比过小，那么就会频繁的触发Minor GC，影响用户线程
 * -XX:MaxTenuringThreshold：设置新生代垃圾的最大年龄
 * -XX:+PrintGCDetails：输出详细的GC处理日志
 * 打印gc简要信息，
 * -XX:+PrintGC
 * -verbose:gc
 * -XX:HandlePromotionFailure：是否配置空间分配担保
 *
 * @author Nordlicht
 */
public class HeapArgsTest {
}
