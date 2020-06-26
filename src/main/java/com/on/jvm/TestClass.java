package com.on.jvm;

/**
 * 测试对象实例化的过程
 * 1.加载类元信息
 * 2.分配内存空间
 * 3.处理并发
 * 4.属性的默认初始化
 * 5.设置对象头信息
 * 6.①显示初始化、②代码块中初始化、③构造方法中初始化
 *
 * @author Nordlicht
 */
public class TestClass {

    int id = 1001;
    String name;
    Account account;

    {
        name = "测试";
    }

    public TestClass(Account account) {
        this.account = account;
    }
}

class Account {

}
