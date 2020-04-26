##### MySQL中文乱码怎么修改

1. 修改配置文件 my.cnf

   在最后加上中文字符集配置

   ```xml
   character_set_server=utf8
   ```

2. 重启mysql

3. 修改已经生成的库、表的字符集

   修改已生成数据库的字符集

   ```mysql
   alter database mydb character set 'utf8';
   ```

   修改以生成表的字符集

   ```mysql
   alter table mytable convert to character set 'utf8';
   ```

   

外键约束：

1. 插入一条数据，如果有外键约束的话需要扫描外键关联的表，确认关联的这条记录是否存在，如果关联的表数据量太大，那会浪费很多资源
2. 在初始化数据的时候需要按照演的顺序来插入数据，不然数据会导入失败
3. 在删除数据的时候如果有外键关联会导致数据无法删除



#### MyIASM和InnoDB的区别

| 对比项 | MyISAM                                                 | InnoDB                                                       |
| ------ | ------------------------------------------------------ | ------------------------------------------------------------ |
| 外键   | 不支持                                                 | 支持                                                         |
| 事务   | 不支持                                                 | 支持                                                         |
| 行表所 | 表锁，即使操作一条记录也会锁住整张表不适合高并发的操作 | 行锁，操作时只锁某一行，不对其他行有影响，适合高并发的操作**（可能会发生死锁？？？）** |
| 缓存   | 只缓存索引，不缓存数据                                 | 不仅缓存索引，还缓存所有查询结果，对内存要求较高，而且内存大小对性能有决定性的作用 |
|        | 节省资源，消耗少，简单业务                             | 并发写，事务，更大的资源                                     |
|        |                                                        |                                                              |

## 最左前缀法则

![](E:\mysql\最左前缀法则.png)



## 会导致索引失效的情况

1. **where后面的条件语句使用了函数**
2. **创建索引时把范围查询的字段放在了别的字段前面，后面的字段索引失效。切记：范围查询字段在建索引的时候放在最后边**
3. **使用不等于“<>”会导致索引失效**
4. **使用IS NOT NULL会导致索引失效**
5. **在使用like查询的时候如果前面也用了“%”会导致索引失效**
6. **where后面条件等号后面的值发生类型转换也会导致索引失效**



## 排序分组优化



**已建立的索引**

```mysql
 create index idx_age_deptid_name on emp (age,deptid,name)
```

**无过滤，不索引**

```mysql
用不到索引
explain  select SQL_NO_CACHE * from emp order by age,deptid; 
可以用到索引，limit也算过滤条件
explain  select SQL_NO_CACHE * from emp order by age,deptid limit 10; 
```

**顺序错，必排序**（using  filesort）

```mysql
可以用到索引
explain select * from emp where age=45 order by deptid;
可以用到索引
explain select * from emp where age=45 order by deptid,name; 
用不到索引
explain select * from emp where age=45 order by deptid,empno;
用不到索引
explain select * from emp where age=45 order by name,deptid;
用不到索引
explain select * from emp where deptid=45 order by age;
```

**方向反，必排序**（using  filesort）

```mysql
可以用到索引
explain select * from emp where age=45 order by  deptid desc, name desc ;
正序反序同时出现，用不到索引
explain select * from emp where age=45 order by  deptid asc, name desc ;
```

