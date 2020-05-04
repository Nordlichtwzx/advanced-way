# MySQL

## 1. 索引

### 1.1 索引概述

​		MySQL官方对索引的定义为：索引（index）是帮助MySQL高效获取数据的数据结构（有序）。在数据之外，数据库系统还维护着满足特定查找算法的数据结构，这些数据结构以某种方式引用（指向）数据，这样就可以在这些数据结构上实现高级查找算法，这种数据结构就是索引

![](images\索引.png)

### 1.2 索引优劣

**优势**

1. 类似于书籍的目录索引，提高数据检索效率的同时，降低数据库的IO成本
2. 通过索引对列数据进行排序，降低数据排序的成本，降低CPU的消耗

**劣势**

1. 索引会占据额外的磁盘空间
2. 索引会影响更新表的速度，因为在更新表数据的同时也需要更新索引。

### 1.3 索引结构

索引是在MySQL的存储引擎层中实现的，而不是在服务器层，所以每种存储引擎支持的索引都不完全一样，也不是所有的存储引擎都支持所有的索引类型

![](images\引擎索引.png)

### 1.4 BTREE结构

BTree又叫多路平衡搜索树，一颗m叉的BTree特性如下

* 树中每个节点最多包含m个孩子
* 除根节点与叶子结点外，每个节点至少是有[ceil(m/2)]（向上取整）个孩子
* 若根节点不是叶子节点，则至少有两个还在孩子
* 所有的叶子结点都在同一层
* 每个非叶子节点由n个key与n+1个指针组成，其中[ceil(m/2)-1]<=n<=m-1

### 1.5 B+树

* n叉B+树最多有n个key
* B+ 树的叶子结点保存key所在节点的信息，按照key大小排列
* 所有的非叶子结点都可以看作是key的索引部分

#### MySQL中的B+树

MySQL的B+树对经典的B+树进行了优化，在原有B+树的基础上，**增加了一个指向相邻叶子结点的链表指针**，就形成了带有顺序指正的B+树，这样可以提高区间访问的性能。

![](images\mysqlb+.png)

### 1.6 索引分类

1. 单值索引：即一个索引只包含单个列，一个表可以有多个单列索引
2. 唯一索引：索引列的值必须唯一，但允许有空值
3. 符合索引，即一个索引包含多个列

### 1.7 索引设计原则

* 对查询频次高，且数据量大的表建立索引
* 索引字段的选择，最佳候选列应当从where子句的条件中提取，如果where子句中的组合较多，那么应该挑选最常用、过滤效果最好的列的组合
* 使用唯一索引，区分度越高，使用索引的效率越高
* 索引不是多多益善，索引越多需要额外维护的成本就越高。**索引过多**会引入相当高的维护代价，降低DML操作的效率。另外索引过多的话，MySQL也会犯选择困难症，提高了选择的代价
* 使用段索引，因为索引也会占据额外的磁盘空间，段所有可以有效提升MySQL访问索引的I/O效率
* 利用最左前缀，组合索引。

## 2. 存储引擎

### 2.1 存储引擎概述

​		和大多数数据库不同，MySQL中有一个存储引擎的概念，针对不同的存储需求可以选择最优的存储引擎。存储引擎就是存储数据，建立索引，更新查询数据等等技术的实现方式，**存储引擎是基与表的，而不是基于库的**，所以存储引擎也可被称为表类型



MySQL支持多种存储引擎，可以使用下面的命令来查看

```mysql
show engines;
```

![](images\存储引擎.png)

也可以使用下面的命令来查看默认的存储引擎

```mysql
show variables like '%storage_enagine%';
```

![](images\默认存储引擎.png)

### 2.2 各种存储引擎

![](images\多种存储引擎.png)

#### 2.2.1 InnoDB

**支持**

1. 事务控制：MySQL中默认支持的事务隔离级别为可重复读，所以在操作数据库时如果使用了事务，但是没有提交事务，再用其他的客户端查看时是查询不到更新后的事务的。

2. 外键约束：支持外键约束

   删除主表数据时，如果有关联记录，则不删除

   ```mysq
   ON DELEET RESTRICET 
   ```

   更新主表时，如果子表有关联记录，则更新子表

   ```mysql
   ON UODATE CASCADE
   ```

3. 存储方式

   InnoDB存储表和索引有一下两种方式

   1. 使用共享表空间存储，这种方式创建的表结构保存在**.frm**文件中，数据和索引保存在innodb_data_home_dir和innodb_data_file_path定义的表空间中，可以是多个文件
   2. 使用多表空间存储，这种方式创建的表的表结构依然存储在**.frm**文件中，但是每个表的数据和索引单独保存在.**ibd**中

   ![](images\保存位置.png)

#### 2.2.2 MyISAM

MyISAM不支持事务，也不支持外键，其优势是访问的速度快，对事务的完整性没有要求或者以select、insert为主的应用基本带上都可以使用这个引擎

**不支持**

  1.   不支持事务：没有事务控制

  2.   存储方式：

       1. .frm：存储表定义
       2. .MYD：（MYData，存储数据）
       3. .MYI：（MYIndex，存储索引）

       ![](images\myisam存储文件.png)

#### 2.2.3 Memory

Memory存储引擎将表的数据存放在内存中，每个Memory表实际对应一个磁盘文件，**.frm**该文件中只存储表的结构。因为Memory引擎中的数据都存放在内存中，所以读取速度很快，并且默认使用HASH索引。但是服务一旦关闭，数据就会丢失

#### 2.2.4 MERGE

MERGE存储引擎是一组MyISAM表的组合，这些表结构必须完全相同，MERGE本身不存储数据，对MERGE表进行的增删改操作内部都是对MyISAM表的操作![](images\merge.png)

## 3. 优化SQL步骤

### 3.1 查看SQL执行频率

MySQL客户端连接成功后，通过show [session|global]命令可以提供服务状态信息。show[session|global] status可以根据需要加上参数“session”或者“global”来显示session级（当前连接）的结果和global级（自数据库上次启动至今）的统计结果，如果不写，默认使用参数是“session”

```mysql
show status like 'Com_______'(7个)
```

![](images\123.png)

```mysql 
show status like 'Innodb_rows_%';
```

![](images\Innodb查询.png)

### 3.2 定位低效率执行SQL

* 慢查询日志：通过慢查询日志定位那些执行效率较低的SQL语句，用--log-slow-queries[=file_name]选项启动时，mysql写一个包含所有执行时间超过long_query_time秒的SQL语句的日志文件

* show processlist：慢查询日志是在查询结束后才记录，所以在应用反映执行效率出现问题的时候慢查询日志并不能定位问题，可以使用show processlist命令查看档期那MySQL正在执行的线程，包括线程的状态，是否锁表等，可以实时的查看SQL的执行情况，同时进行一些优化

  ![](images\preocesslis.png)

### 3.3 explain分析执行计划

#### 3.3.1 explain之id

id字段是select查询的序列号，是一组数字，表示的是查询中执行select子句或者是操作表的顺序，id的情况有三种

1. id相同表示加载表的顺序是从上到下

   ![](images\id相同.png)

2. id不同id值越大，优先级越高，越先被执行

   ![](images\id不同.png)

3. id有相同，也有不同，同时存在，id相同的可以认为是一组，从上往下顺序执行；在所有的组中，id的值越大，优先级越高，越先执行

   ![](images\id同不同.png)

#### 3.3.2 explain之select_type



**从上往下执行效率越来越低**



| select_type  | 含义                                                         | 图解                                |
| ------------ | ------------------------------------------------------------ | ----------------------------------- |
| SIMPLE       | 简单的select查询，查询中不包含子查询或者union                | <img src="images\simple.png"  />  |
| PRIMARY      | 查询中若包含任何复杂的子查询，最外层查询标记为该标识         | ![](images\primary——subquery.png) |
| SUBQUERY     | 在SELECT或where列表中包含了子查询                            | ![](images\primary——subquery.png) |
| DERIVED      | 在FROM列表中包含的子查询，被标记为DERIVED（衍生）MySQL会递归执行这些资讯查询，把结果放在临时表中 | ![](images\derived.png)           |
| UNION        | 若第二个select出现在union之后，则标记为union；若union包含在from子句的子查询中，外层select将被标记为：DERIVED | ![](images\union.png)             |
| UNION RESULT | 从UNION表获取结果的SELECT                                    | ![](images\union.png)             |
|              |                                                              |                                     |

#### 3.3.3 explain之table

展示这一行的数据是关于哪一张表的

#### 3.3.4explain之type



**从上往下执行效率越低，一般来说，我们只需要保证查询至少达到染个级别，最好达到ref**

| type   | 含义                                                         |
| ------ | ------------------------------------------------------------ |
| NULL   | MySQL不访问任何表，索引，直接返回数据，比如返回当前数据select now() |
| system | 表只有一行记录(等于系统表)，这是const类型的特例，一般不会出现 |
| const  | 表示通过索引一次就找到了，const用于比较primary key或者unique索引。因为只匹配一行数据，所以很快。如将主键置于社热列表中，MySQL就能将该查询转换为一个常量，const会将主键或唯一索引的所有部分与常量值进行比较 |
| eq_ref | 类似ref，区别在于使用的是唯一索引，使用主键的关联查询，关联查询出的记录只有一条，常见于主键或唯一索引扫描 |
| ref    | 非唯一索引扫描，返回匹配某个单独值得所有行，本质上也是一种索引访问，返回所有匹配某个单独值得所有行（多个） |
| range  | 只检索给定返回的行，使用一个索引来选择行，where之后出现between，<,>,in等操作，用于范围查询 |
| index  | index与ALL的区别为index类型只是遍历了索引树，通常比ALL快，ALL是遍历数据文件。比如说select id from table，这里的id是主键索引，这样就会遍历索引树查找出所有的id。 |
| all    | 将遍历全表以找打匹配的行                                     |
|        |                                                              |

#### 3.3.5 explain之key

1. possible_keys：显示可能应用在这张表上的索引，一个或者多个
2. key：实际使用的索引，如果为NULL，则没有使用到索引
3. key_len：表示索引中使用的字节数，该值为索引字段最大可能长度，并非实际使用长度，在不损失精确性的前提下，长度越短越好

#### 3.3.6explain之rows、

扫描的行数

#### 3.3.7explain之extra



| extra          | 含义                                                         |
| -------------- | ------------------------------------------------------------ |
| using filesort | 说明MySQL会对数据使用一个外部的索引排序，而不是按照表内的索引顺序进行读取，称为：文件排序，效率低 |
| using tempary  | 使用了临时表保存中间结果，MySQL在对查询结果排序是使用临时表，常见于order by和group by；效率低 |
| using index    | 表示相应的select操作使用了覆盖索引，避免访问表的数据行，效率不错（索引命中） |
|                |                                                              |

### 3.4 show profile分析SQL

**MySQL从5.0.37版本开始增加了对show profiles和show profile 的支持，帮助我们知道SQL时间都耗费在了哪里**

1. 设置profile

   1. 查看是否有profile功能

      ```mysql
      show @@have_profiling;
      ```

   2. 查看当前profile是否开启

      ```mysql
      select @@profiling;
      ```

   3. 打开profile

      ```mysql
      set @@profiling = 1;
      ```

   ![](images\profiling.png)

2. 执行show profiles指令，来查看SQL语句执行的耗时

   ![](images\showprfiles.png)

3. 查看单个耗时较多的SQL语句具体的耗时

   ![](images\单独profiles.png)

### 3.5 trace

![](images\trace.png)

![](images\trace一部分.png)



## 4.索引的使用

### 4.1 避免索引失效

1. 全值匹配：对索引中所有列都指定具体指。索引生效，执行效率高。

2. 最左前缀法则：如果索引是多列，要遵守最左前缀法则，值得是查询从索引的最左前列开始，并且不跳过索引中的列

3. **范围查询右边的列，索引失效**

4. **不要在索引列上进行运算操作，索引失效**

5. **字符串不加单引号，造成索引失效。如果MySQL发现varchar类型的字段没有加单引号，会对该字符串进行隐式类型转换，这样就会导致该索引类使用了运算操作，就会失效。**

6. 尽量使用覆盖索引，避免select *，避免回表查询。

   **MySQL5.6引入的<u>索引下推优化</u>，可以在索引遍历过程中，对索引包含的字段先做判断，直接过滤掉不满足条件的记录，减少回表次数。**

   > using index：使用覆盖索引的时候就会出现
   >
   > using where：在查找使用索引的情况下，需要回表去查询所需的数据
   >
   > using index condition：查找使用了索引，但是需要回表查询的数据
   >
   > using index；using where：查找使用了索引，但是需要的数据都在索引列中能找到，所以不需要回表查询。

7. **用or分割开的条件，如果or前的条件中的列有索引，而后面的列中没有索引，那么涉及的索引搜不会被用到，索引失效**

8. **以%开头的like模糊查询，索引失效。可以通过覆盖索引来解决。**

9. 如果MYSQL评估使用索引比权标更慢，则不使用索引。比如说某一列的数据中某一个值占的比例特别大，那么就不会走索引，就会直接走全表扫描。

10. is NULL，is NOT NULL有时索引失效，这主要取决于该列数据中NULL值得多少，如果NULL值占比特别大，那么is NOT NULL就会走索引，相反is NULL走索引。**这主要取决于一个值在所在列所占的数据量大小，这是MySQL后台优化后的结果，和第9点一样，都是优化后决定是否使用索引，取决于该值在所在列中的占比**

11. in 走索引，not in不走索引

12. 单列索引和复合索引的选择

    尽量使用复合索引，而少用单列索引，创建复合索引

    ![](images\复合.png)

    创建单列索引

    ![](images\单列.png)

    数据库会选择一个最优的索引（辨识度最高的索引）来使用，并不会使用全部索引



### 4.2 查看索引使用的情况

![](images\查看索引.png)

## 5. SQL优化

### 5.1 大批量插入数据

**可以使用load命令导入数据**

在使用的过程中，对于使用InnoDB存储引擎的表，使用下面几种方式可以提高导入的效率

1. 主键顺序插入

   因为InnoDB类型的表是按照主键的顺序保存的，所以将导入的数据主键的顺序排列，可以有效地提高导入数据的效率，如果InnoDB表没有主见，那么系统会自动默认创建一个内部列作为主键，所以如果可以给表创建一个主键，将可以利用这点，来提高导入数据的效率。

2. 关闭唯一性校验

   在导入数据前执行

   ```mysql
   SET UNIQUE_CHECKS=0;
   ```

   关闭唯一性校验，在导入后执行

   ```mysq
   SET UNIQUE_CHECKS=1;
   ```

   恢复唯一性校验，可以提高导入的效率

3. 手动提交事务

   如果应用使用自动提交的方式，建议在导入之前执行

   ```mysql 
   SET AUTOCOMMIT=0;
   ```

   关闭自动提交，导入结束后再执行

   ```mysql
   SET AUTOCOMMIT=1;
   ```

   打开自动提交，这样也可以提高导入的效率。

### 5.2 优化insert语句

1. 如果需要同时对一张表插入很多行数据时，应该尽量使用多个值表insert语句，这种方式将大大缩减客户端与数据库之间的连接，关闭等消耗。使得效率比分开执行的单个insert语句快

   示例，原始方式为

   ```mysql
   insert into tb_test values(1,'Tom');insert into tb_test values(2,'Cat');insert into tb_test values(3,'Jerry');
   ```

   优化后

   ```mysql
   insert into tb_test values(1,'Tom'),(2,'Cat')，(3,'Jerry');
   ```

2. 在事务中进行数据插入，如果数据量大的话，可以分段的提交事务

   ```mysql
   start transaction;
   insert into tb_test values(1,'Tom');
   insert into tb_test values(2,'Cat');
   insert into tb_test values(3,'Jerry');
   commit;
   ```

3. 数据有序插入，优化MySQL建立索引的时间

   ```mysql
   insert into tb_test values(1,'Tom');
   insert into tb_test values(2,'Cat');
   insert into tb_test values(3,'Jerry');
   insert into tb_test values(4,'Tim');
   insert into tb_test values(5,'Rose');
   ```

### 5.3 优化order by语句

#### 5.3.1 两种排序方式：

1. filesort：所有不是通过索引返回排序结果的都叫filesort
2. index：通过有序索引顺序扫描直接返回有序数据，这种情况极为using index，不需要额外排序，操作效率高。
   1. order by后面使用单字段排序的时候，前面需要查询的数据必须是索引中的列，不然不会使用到索引
   2. order by后面使用多字段排序的时候，
      * 字段的顺序需要和索引的顺序保持一致
      * 字段在使用升序和降序时必须保持统一，否则会出现filesort

#### 5.3.2 FileSort的优化

FileSort的两种排序算法：

1. 两次扫描算法：在MYSQL4.1之前，使用该方法排序，首先根据条件去除排序字段和行指针信息，然后在排序区sort buffer中排序，如果sort buffer不够，则在临时表tempory table中存储排序的结果，完成排序之后，再根据指针回表读取记录，该操作可能会导致大量随机I/O操作

2. 一次扫描算法：一次性取出满足条件的所有字段，然后在sort buffer中排序后直接输出结果集，排序时内存开销较大，但是排序效率要比两次扫描算法高。

3. MySQL中通过比较系统变量max_length_for_sort_data的大小和Query语句查询出的字段总大小来决定使用哪种排序算法，如果

   max_length_for_sort_data更大，则使用一次扫描算法，反之使用两次扫描算法。

   可以提高sort_buffer_size和max_length_for_sort_data系统变量，来怎大排序区的大小，提高排序的效率

   ![](images\sortbuffer.png)

### 5.4 优化group by语句

由于group by实际上也同样会进行排序操作，而且与order by相比，group by主要只是多了排序之后的分组操作，所以，与order by一样，group by也可以利用索引

如果查询包含group by但是想要避免排序结果的消耗，可以提执行order by null禁止排序

![](images\1556339573979.png)

也可以使用索引来优化group by

![](images\1556339688158.png)

### 5.5 优化嵌套查询

Mysql4.1版本之后，开始支持SQL的子查询。这个技术可以使用SELECT语句来创建一个单列的查询结果，然后把这个结果作为过滤条件用在另一个查询中。使用子查询可以一次性的完成很多逻辑上需要多个步骤才能完成的SQL操作，同时也可以避免事务或者表锁死，并且写起来也很容易。**但是，有些情况下，子查询是可以被更高效的连接（JOIN）替代。**

示例 ，查找有角色的所有的用户信息 : 

```sql
 explain select * from t_user where id in (select user_id from user_role );
```

![](images\1556359399199.png)

优化后：连接(Join)查询之所以更有效率一些 ，是因为MySQL不需要在内存中创建临时表来完成这个逻辑上需要两个步骤的查询工作。

![](images\1556359482142.png)

### 5.6 优化OR条件

对于包含or的查询语句，如果要利用索引，则or之间的每个条件列都必须用到索引，而且不能使用到复合索引。

**可以使用union替换or来达到优化or的效果**

![](images\1556355027728.png)

UNION 语句的 type 值为 ref，OR 语句的 type 值为 range，可以看到这是一个很明显的差距

UNION 语句的 ref 值为 const，OR 语句的 type 值为 null，const 表示是常量值引用，非常快

这两项的差距就说明了 UNION 要优于 OR 。

### 5.7 优化分页查询

1. 优化思路一：

   在索引上完成排序分页操作，最后根据主键关联回原表查询所需要的其他列内容。![](images\1556416102800.png)

2.  优化思路二

    该方案适用于主键自增的表，可以把Limit 查询转换成某个位置的查询 。![](images\1556363928151.png)

### 5.8 使用SQL提示

SQL提示，是优化数据库的一个重要手段，简单来说，就是在SQL语句中加入一些人为的提示来达到优化操作的目的。

#### 5.8.1 USE INDEX

在查询语句中表名的后面，添加 use index 来提供希望MySQL去参考的索引列表，就可以让MySQL不再考虑其他可用的索引。

```
create index idx_seller_name on tb_seller(name);
```

![1556370971576](images\1556370971576.png) 

#### 5.8.2 IGNORE INDEX

如果用户只是单纯的想让MySQL忽略一个或者多个索引，则可以使用 ignore index 作为 hint 。

```
 explain select * from tb_seller ignore index(idx_seller_name) where name = '小米科技';
```

![1556371004594](images\1556371004594.png) 

#### 5.8.3 FORCE INDEX

为强制MySQL使用一个特定的索引，可在查询中使用 force index 作为hint 。 

``` SQL
create index idx_seller_address on tb_seller(address);
```

![1556371355788](images\1556371355788.png) 



## 6.应用优化

### 6.1 使用连接池

对于访问数据库来说，建立连接的代价是昂贵的，因为我们频繁的创建关闭连接，是比较消耗资源的，我们有必要建立数据库连接池，以提高访问的性能

### 6.2 减少对MySQL的访问

#### 6.2.1 避免对数据进行重复检索

在编写应用代码时，需要能够理清对数据库的访问逻辑。能够一次连接就获取到结果的，就不用两次连接，这样可以大大减少对数据库无用的重复请求。

### 6.2.2 增加cache层

在应用中，我们可以通过增加缓存来达到减轻数据库负担的目的。可以将部分数据从数据库中抽取出来放到应用端以文本形式存储，或者适用框架（mybatis/hibernate）提供的一级缓存/二级缓存，或者使用redis等nosql数据库来缓存

### 6.3 负载均衡

负载均衡是应用中使用非常普遍的一种优化方法，它的机制就是利用某种均衡算法，将固定的负载量分布到不同的服务器上， 以此来降低单台服务器的负载，达到优化的效果。

1. 主从复制

   通过MySQL的主从复制，实现读写分离，使增删改操作走主节点，查询操作走从节点，从而可以降低单台服务器的读写压力。

   ![](images\1.jpg)

2. 分布式架构

   分布式数据库架构适合大数据量、负载高的情况，它有良好的拓展性和高可用性。通过在多台服务器之间分布数据，可以实现在多台服务器之间的负载均衡，提高访问效率。

## 7.查询缓存

### 7.1概述

开启MySQL的查询缓存，当执行**完全相同**的SQL语句的时候，服务器就会直接从缓存中读取出结果，当数据被修改，之前该表的缓存就会失效，所以不适用于修改比较频繁的表

### 7.2操作流程

![](images\20180919131632347.png)

1. 客户端发送一条query语句到服务端
2. 服务端先检查查询缓存，如果命中缓存，直接返回。否则进入下一个阶段
3. 服务器端进行SQL解析，预处理，再有优化器生成对应的执行计划
4. MySQL根据优化器生成的执行计划，调用存储引擎的API来执行查询
5. 将结果返回给客户端，同时更新查询缓存中的内容

### 7.3查询缓存配置

1. 查看当前的MySQL数据库是否支持查询缓存

   ```mysql
   SHOW VARIABLES LIKE 'have_query_cache';	
   ```

   ![](images\1555249929012.png)

2. 查看当前MySQL是否开启了查询缓存

   ```mysql
   SHOW VARIABLES LIKE 'query_cache_type';
   ```

   ![](images\1555250015377.png)

3. 查看查询缓存的占用大小

   ```mysql
   SHOW VARIABLES LIKE 'query_cache_size';
   ```

   ![](images\1555250142451.png)

4. 查看查询缓存的状态变量

   ```mysql
   SHOW STATUS LIKE 'Qcache%';
   ```

   ![](images\1555250443958.png)

   各个变量的含义如下：

   | 参数                    | 含义                                                         |
   | ----------------------- | ------------------------------------------------------------ |
   | Qcache_free_blocks      | 查询缓存中的可用内存块数                                     |
   | Qcache_free_memory      | 查询缓存的可用内存量                                         |
   | Qcache_hits             | 查询缓存命中数                                               |
   | Qcache_inserts          | 添加到查询缓存的查询数                                       |
   | Qcache_lowmen_prunes    | 由于内存不足而从查询缓存中删除的查询数                       |
   | Qcache_not_cached       | 非缓存查询的数量（由于 query_cache_type 设置而无法缓存或未缓存） |
   | Qcache_queries_in_cache | 查询缓存中注册的查询数                                       |
   | Qcache_total_blocks     | 查询缓存中的块总数                                           |

### 7.4开启查询缓存

MySQL的查询缓存默认是关闭的，需要手动设置参数query_cache_type，来开启查询缓存，query_cache_type该参数的可取值有三个

| 值          | 含义                                                         |
| ----------- | ------------------------------------------------------------ |
| OFF 或 0    | 查询缓存功能关闭                                             |
| ON 或 1     | 查询缓存功能打开，SELECT的结果符合缓存条件即会缓存，否则，不予缓存，显式指定 SQL_NO_CACHE，不予缓存 |
| DEMAND 或 2 | 查询缓存功能按需进行，显式指定 SQL_CACHE 的SELECT语句才会缓存；其它均不予缓存 |

​	在MySQL的配置文件中，增加以下配置：

![](images\1555251383805.png)

配置完成后，**重启生效**

### 7.5查询缓存select选项

可以再select语句中指定两个与查询缓存相关的选项

* SQL_CACHE：如果查询结果是可以缓存的，并且query_cache_type系统变量的值为ON或者DEMAND，则缓存查询结果。

* SQL_NO_CACHE：服务器不使用查询缓存，它既不检查查询缓存，也不检查结果是否已缓存，也不缓存查询结果。

  ```mysq
  SELECT SQL_CACHE id, name FROM customer;
  SELECT SQL_NO_CACHE id, name FROM customer;
  ```

### 7.6 查询缓存失效的情况

1. SQL语句不一致的情况，要想命中查询缓存，查询的SQL语句必须**完全一致**

   ```mysq
   SQL1 : select count(*) from tb_item;
   SQL2 : Select count(*) from tb_item;
   ```

2. 当查询语句中有一些不确定的函数时，则不会缓存，如： now() , current_date() , curdate() , curtime() , rand() , uuid() , user() , database() 。

   ```mysql
   SQL1 : select * from tb_item where updatetime < now() limit 1;
   SQL2 : select user();
   SQL3 : select database();
   ```

3. 不使用任何表查询

   ```mysq
   select 'A';
   ```

4. 查询mysql，information_schema或者performance_schema数据库中的表时，不会走查询缓存

   ```mysql
   select * from information_schema.engines;
   ```

5. 在存储的函数，触发器或事件的主体内执行的查询。

6. 如果表更改，则使用该表的所有高速缓存查询都将变为无效并从高速缓存中删除。这包括使用`MERGE`映射到已更改表的表的查询。一个表可以被许多类型的语句，如被改变 INSERT， UPDATE， DELETE， TRUNCATE TABLE， ALTER TABLE， DROP TABLE，或 DROP DATABASE 。

## 8.内存优化

### 8.1 内存优化原则

1. 将尽量多的内存分配给MySQL做缓存，但要给操作系统和其他程序预留足够的内存
2. MyISAM存储引擎的数据文件读取依赖于操作系统自身的IO操作，因此，如果有MyISAM表，就要预留更多地内存给操作系统做IO缓存。
3. 排序区，链接区等缓存是分配给每个数据库会话（session）专用的，其默认的设置要根据最大连接数合理分配，如果设置太大，不但浪费资源，而且在并发连接较高时会导致物理内存耗尽。

### 8.2 MyISAM 内存优化

myisam存储引擎使用 key_buffer 缓存索引块，加速myisam索引的读写速度。对于myisam表的数据块，mysql没有特别的缓存机制，完全依赖于操作系统的IO缓存。

##### key_buffer_size

key_buffer_size决定MyISAM索引块缓存区的大小，直接影响到MyISAM表的存取效率。可以在MySQL参数文件中设置key_buffer_size的值，对于一般MyISAM数据库，建议至少将1/4可用内存分配给key_buffer_size。

在/usr/my.cnf 中做如下配置：

```
key_buffer_size=512M
```

##### read_buffer_size

如果需要经常顺序扫描myisam表，可以通过增大read_buffer_size的值来改善性能。但需要注意的是read_buffer_size是每个session独占的，如果默认值设置太大，就会造成内存浪费。

##### read_rnd_buffer_size

对于需要做排序的myisam表的查询，如带有order by子句的sql，适当增加 read_rnd_buffer_size 的值，可以改善此类的sql性能。但需要注意的是 read_rnd_buffer_size 是每个session独占的，如果默认值设置太大，就会造成内存浪费。

### 8.3 InnoDB 内存优化

innodb用一块内存区做IO缓存池，该缓存池不仅用来缓存innodb的索引块，而且也用来缓存innodb的数据块。

##### innodb_buffer_pool_size

该变量决定了 innodb 存储引擎表数据和索引数据的最大缓存区大小。在保证操作系统及其他程序有足够内存可用的情况下，innodb_buffer_pool_size 的值越大，缓存命中率越高，访问InnoDB表需要的磁盘I/O 就越少，性能也就越高。

```
innodb_buffer_pool_size=512M
```

##### innodb_log_buffer_size

决定了innodb重做日志缓存的大小，对于可能产生大量更新记录的大事务，增加innodb_log_buffer_size的大小，可以避免innodb在事务提交前就执行不必要的日志写入磁盘操作。

```
innodb_log_buffer_size=10M
```

## 9.MySQL并发参数调整

从实现上来说，MySQL Server 是多线程结构，包括后台线程和客户服务线程。多线程可以有效利用服务器资源，提高数据库的并发性能。在Mysql中，控制并发连接和线程的主要参数包括 max_connections、back_log、thread_cache_size、table_open_cahce。

### 9.1 max_connections

采用max_connections 控制允许连接到MySQL数据库的最大数量，默认值是 151。如果状态变量 connection_errors_max_connections 不为零，并且一直增长，则说明不断有连接请求因数据库连接数已达到允许最大值而失败，这是可以考虑增大max_connections 的值。

Mysql 最大可支持的连接数，取决于很多因素，包括给定操作系统平台的线程库的质量、内存大小、每个连接的负荷、CPU的处理速度，期望的响应时间等。在Linux 平台下，性能好的服务器，支持 500-1000 个连接不是难事，需要根据服务器性能进行评估设定。

### 9.2 back_log

back_log 参数控制MySQL监听TCP端口时设置的积压请求栈大小。如果MySql的连接数达到max_connections时，新来的请求将会被存在堆栈中，以等待某一连接释放资源，该堆栈的数量即back_log，如果等待连接的数量超过back_log，将不被授予连接资源，将会报错。5.6.6 版本之前默认值为 50 ， 之后的版本默认为 50 + （max_connections / 5）， 但最大不超过900。

如果需要数据库在较短的时间内处理大量连接请求， 可以考虑适当增大back_log 的值。



### 9.3 table_open_cache

该参数用来控制所有SQL语句执行线程可打开表缓存的数量， 而在执行SQL语句时，每一个SQL执行线程至少要打开 1 个表缓存。该参数的值应该根据设置的最大连接数 max_connections 以及每个连接执行关联查询中涉及的表的最大数量来设定 ：

​	max_connections x N ；



### 9.4 thread_cache_size

为了加快连接数据库的速度，MySQL 会缓存一定数量的客户服务线程以备重用，通过参数 thread_cache_size 可控制 MySQL 缓存客户服务线程的数量。



### 9.5 innodb_lock_wait_timeout

该参数是用来设置InnoDB 事务等待行锁的时间，默认值是50ms ， 可以根据需要进行动态设置。对于需要快速反馈的业务系统来说，可以将行锁的等待时间调小，以避免事务长时间挂起； 对于后台运行的批量处理程序来说， 可以将行锁的等待时间调大， 以避免发生大的回滚操作。

## 10. MySQL锁

### 10.1 锁的概述

锁是计算机协调多个进程或者线程并发访问某一资源的机制。在数据库中，除传统的计算资源（如CPU、RAM、I/O等）的争用以外，数据也是一种供许多用户共享的资源，如何保证数据并发访问的一致性，有效性是所有所有数据库必须解决的问题，锁冲突也是影响数据库并发访问性能的一个重要因素，从这个角度来说，锁对数据库而言显得尤其重要，也更加复杂。

### 10.2锁分类

**从对数据操作的粒度分**

1. 表锁：操作时，会锁定整个表
2. 行锁：操作时，会锁定当前操作行

**从对数据操作类型分**

1. 读锁（共享锁）：针对同一份数据，多个操作可以同时进行而不会互相影响
2. 写锁（排它锁）：当前操作没有完成之前，它会阻断其他写锁和读锁。

### 10.3 MySQL锁

MySQL锁的特点是不同的存储引擎支持不同的锁机制

| 存储引擎 | 表级锁           | 行级锁           | 页面锁 |
| -------- | ---------------- | ---------------- | ------ |
| MyISAM   | **支持（默认）** | 不支持           | 不支持 |
| InnoDB   | 支持             | **支持（默认）** | 不支持 |
| MEMORY   | 支持             | 不支持           | 不支持 |
| BDB      | 支持             | 不支持           | 支持   |

三种锁的特性

| 锁类型 | 特点                                                         |
| ------ | ------------------------------------------------------------ |
| 表级锁 | 偏向MyISAM 存储引擎，开销小，加锁快；不会出现死锁；锁定粒度大，发生锁冲突的概率最高,并发度最低。 |
| 行级锁 | 偏向InnoDB 存储引擎，开销大，加锁慢；会出现死锁；锁定粒度最小，发生锁冲突的概率最低,并发度也最高。 |
| 页面锁 | 开销和加锁时间界于表锁和行锁之间；会出现死锁；锁定粒度界于表锁和行锁之间，并发度一般。 |

综上所述，不能笼统的说哪种锁更好，只能就具体应用的特点来说哪种锁更适合，仅从锁的角度来说：

* 表级锁：更适合于已查询位置，只有少量按索引条件更新数据的应用
* 行级锁：更适合于有大量按索引条件并发更新少量不同数据，同时又有并发查询的应用

### 10.4 MyISAM表锁

MyISAM存储引擎只支持表锁，这也是MySQL开始几个版本中唯一支持的锁类型

#### 10.4.1 如何加表锁

MyISAM在执行查询语句前，会自动给涉及到的所有表加读锁，在执行更新操作前，会自动给涉及到的表加写锁，这个过程不需要用户干预，因此，用户一般不需要中介用lock table命令给MyISAM显示的加锁

显示加锁语法

```sql
加读锁 ： lock table table_name read;

加写锁 ： lock table table_name write；
```

#### 10.4.2 结论

锁模式的相互兼容性：

* 如果当前客户端拿到了**读锁**，那么其他客户端可以进行**读操作**，但是不能进行**写操作**
* 如果当前客户端拿到了**写锁**，那么其他客户端不能进行**读操作**也不能进行**写操作**

结论

1. 对MyISAM表的读操作，不会阻塞其他用户对同一表的读请求，但会阻塞对同一表的写请求
2. 对MyISAM表的写操作，则会阻塞其他用户对同一表的读写操作

**简而言之，就是读锁会阻塞写，但是不会阻塞读。而写锁，既会阻塞读，又会阻塞写。**

**此外，MyISAM的读写锁调度是写优先，这也是MyISAM不适合做写为主的表的存储引擎的原因，因为拿到写锁后，其他线程不能做任何操作，大量的更新会使查询很难得到锁，从而造成永远阻塞**

#### 10.4.3 查看锁的争用情况

```mysq
shoe open tabes;
```

![](images\showopentables.png)

In_use：表当前被查询使用的次数，如果该数为零，则表是打开的，但是当前没有被使用

Name_locked：表名称是否被锁定，**名称锁定用于取消表或对表进行重命名等操作**

```sql
show sattus like 'Table_locks%';
```

![](images\showtableslike.png)

Table_locks_immediate：能够立即获得表加锁的次数，每立即获取锁，值加一

Table_locks_waited：值得是不能立即获取表级锁而需要等待的次数，每等待一次，该值加一，**此值高说明存在着较为严重的表级锁争用情况**、

### 10.5 InnoDB行锁

#### 10.5.1 行锁的介绍

特点：偏向InnoDB存储引擎，开销大，加锁慢，会出现死锁；锁粒度小，发生锁冲突的概率最低，并发度也最高

**InnoDB与MyISAM最大不同：一是支持事务，二是采用行级锁**

#### 10.5.2 背景知识

**事务及其ACID属性**

事务是由一组SQL语句组成的逻辑处理单元。要么全部成功要么全部失败。

事务具有以下4个特性，简称为事务ACID属性。

| ACID属性             | 含义                                                         |
| -------------------- | ------------------------------------------------------------ |
| 原子性（Atomicity）  | 事务是一个原子操作单元，其对数据的修改，要么全部成功，要么全部失败。 |
| 一致性（Consistent） | 在事务开始和完成时，数据都必须保持一致状态。                 |
| 隔离性（Isolation）  | 数据库系统提供一定的隔离机制，保证事务在不受外部并发操作影响的 “独立” 环境下运行。 |
| 持久性（Durable）    | 事务完成之后，对于数据的修改是永久的。                       |



**并发事务处理带来的问题**

| 问题                               | 含义                                                         |
| ---------------------------------- | ------------------------------------------------------------ |
| 丢失更新（Lost Update）            | 当两个或多个事务选择同一行，最初的事务修改的值，会被后面的事务修改的值覆盖。 |
| 脏读（Dirty Reads）                | 当一个事务正在访问数据，并且对数据进行了修改，而这种修改还没有提交到数据库中，这时，另外一个事务也访问这个数据，然后使用了这个数据。 |
| 不可重复读（Non-Repeatable Reads） | 一个事务在读取某些数据后的某个时间，再次读取以前读过的数据，却发现和以前读出的数据不一致。 |
| 幻读（Phantom Reads）              | 一个事务按照相同的查询条件重新读取以前查询过的数据，却发现其他事务插入了满足其查询条件的新数据。 |



**事务隔离级别**

为了解决上述提到的事务并发问题，数据库提供一定的事务隔离机制来解决这个问题。数据库的事务隔离越严格，并发副作用越小，但付出的代价也就越大，因为事务隔离实质上就是使用事务在一定程度上“串行化” 进行，这显然与“并发” 是矛盾的。 

数据库的隔离级别有4个，由低到高依次为Read uncommitted、Read committed、Repeatable read、Serializable，这四个级别可以逐个解决脏写、脏读、不可重复读、幻读这几类问题。

| 隔离级别                | 丢失更新 | 脏读 | 不可重复读 | 幻读 |
| ----------------------- | -------- | ---- | ---------- | ---- |
| Read uncommitted        | ×        | √    | √          | √    |
| Read committed          | ×        | ×    | √          | √    |
| Repeatable read（默认） | ×        | ×    | ×          | √    |
| Serializable            | ×        | ×    | ×          | ×    |

备注 ： √  代表可能出现 ， × 代表不会出现 。

Mysql 的数据库的默认隔离级别为 Repeatable read ， 查看方式：

```
show variables like 'tx_isolation';
```

![1554331600009](images\1554331600009.png)  

#### 10.5.3 InnoDB的行锁模式

InnoDB实现了以下两种类型的行锁

* 共享锁（S）：又称为读锁，简称S锁，共享锁就是多个事务对于统一数据可以共享一把锁，都能访问到数据，但是只能读不能修改。
* 排它锁（X）：又称为写锁，简称X锁，排它锁就是不能与其他锁并存，如一个事务获取了一个数据行的排它锁，其他事物就不能再获取该行的其他锁，包括共享锁和排它锁，但是获取排它锁的事务可以对数据进行读取和修改

对于更新操作，InnoDB会自动给涉及到的数据添加排他锁

对于普通查询操作，InnoDB不会加任何锁

可以通过以下语句显示的给记录加锁

```sql
共享锁（S）：SELECT * FROM table_name WHERE ... LOCK IN SHARE MODE

排他锁（X) ：SELECT * FROM table_name WHERE ... FOR UPDATE
```

#### 10.5.4 行锁基本演示

| Session-1                                                    | Session-2                                                    |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![](images\1554354615030.png)关闭自动提交功能              | ![](images\1554354601867.png)关闭自动提交功能              |
| ![](images\1554354713628.png)可以正常的查询出全部的数据    | ![](images\1554354717336.png)可以正常的查询出全部的数据    |
| ![](images\1554354830589.png)查询id 为3的数据 ；           | ![](images\1554354832708.png)获取id为3的数据 ；            |
| ![](images\1554382789984.png) 更新id为3的数据，但是不提交； | ![](images\1554382905352.png)更新id为3 的数据， 出于等待状态 |
| ![](images\1554382977653.png)通过commit， 提交事务         | ![](images\1554383044542.png) 解除阻塞，更新正常进行       |
| 以上， 操作的都是同一行的数据，接下来，演示不同行的数据 ：   |                                                              |
| ![](images\1554385220580.png) 更新id为3数据，正常的获取到行锁 ， 执行更新 ； | ![](images\1554385236768.png) 由于与Session-1 操作不是同一行，获取当前行锁，执行更新； |

#### 10.5.5 无索引行锁升级为表锁

**如果不通过索引条件检索数据，或者索引失效，**那么InnoDB将对表中的所有记录加锁，实际效果跟表锁一样。

查看当前表的索引 ： show  index  from test_innodb_lock ;

![](images\1554385956215.png) 

| Session-1                                         | Session-2                                                    |
| ------------------------------------------------- | ------------------------------------------------------------ |
| 关闭事务的自动提交![](images\1554386287454.png) | 关闭事务的自动提交![](images\1554386312524.png)            |
| 执行更新语句 ：![](images\1554386654793.png)    | 执行更新语句， 但处于阻塞状态：![](images\1554386685610.png) |
| 提交事务：![](images\1554386721653.png)         | 解除阻塞，执行更新成功 ：![](images\1554386750004.png)     |
|                                                   | 执行提交操作 ：![](images\1554386804807.png)               |

由于 执行更新时 ， name字段本来为varchar类型， 我们是作为数组类型使用，存在类型转换，索引失效，最终行锁变为表锁 ；



#### 10.5.6 间隙锁危害

当我们用范围条件，而不是使用相等条件检索数据，并请求共享或排他锁时，InnoDB会给符合条件的已有数据进行加锁； 对于键值在条件范围内但并不存在的记录，叫做 "间隙（GAP）" ， InnoDB也会对这个 "间隙" 加锁，这种锁机制就是所谓的 间隙锁（Next-Key锁） 。

示例 ： 

| Session-1                                         | Session-2                                                    |
| ------------------------------------------------- | ------------------------------------------------------------ |
| 关闭事务自动提交 ![](images\1554387987130.png)  | 关闭事务自动提交![](images\1554387994533.png)              |
| 根据id范围更新数据![](images\1554388492478.png) |                                                              |
|                                                   | 插入id为2的记录， 出于阻塞状态![](images\1554388515936.png) |
| 提交事务 ；![](images\1554388149305.png)        |                                                              |
|                                                   | 解除阻塞 ， 执行插入操作 ：![](images\1554388548562.png)   |
|                                                   | 提交事务 ：commit                                            |

**锁住的范围包括一些原本不存在的行，但是在这些行被锁的时间内，又有其他事物对这些行进行了操作，这是就会造成间隙锁**



#### 10.5.7 InnoDB 行锁争用情况

```sql
show  status like 'innodb_row_lock%';
```

![1556455943670](images\1556455943670.png)

```
Innodb_row_lock_current_waits: 当前正在等待锁定的数量

Innodb_row_lock_time: 从系统启动到现在锁定总时间长度

Innodb_row_lock_time_avg:每次等待所花平均时长

Innodb_row_lock_time_max:从系统启动到现在等待最长的一次所花的时间

Innodb_row_lock_waits: 系统启动后到现在总共等待的次数


当等待的次数很高，而且每次等待的时长也不小的时候，我们就需要分析系统中为什么会有如此多的等待，然后根据分析结果着手制定优化计划。

```

#### 10.5.8 总结

InnoDB存储引擎由于实现了行级锁，虽然在锁定机制的实现方面带来的性能损耗可能会比表锁更高一些，但是在整体并发处理能力方面要远远优于MyISAM的表锁，当系统并发量高的时候，InnoDB和MyISAM的整体性能相比就会有很大的优势。

但是InnoDB的行级锁同样也有其脆弱的一面，当我们使用不当的时候，可能会让InnoDB的整体性能表现得不如MyISAM，或者更差

优化建议：

* 尽可能让所有数据检索都通过索引来完成，避免无索引行锁升级为表锁
* 合理设计索引，尽量缩小锁的范围
* 尽可能减少索引条件，及索引范围，避免间隙锁
* 尽量控制事务大小，减少锁定资源量和时间长度
* 尽可能使用低级别事务隔离（但是需要业务层面满足需求）



## 11.常用SQL技巧

#### 11.1 SQL执行顺序

编写顺序

```SQL
SELECT DISTINCT
	<select list>
FROM
	<left_table> <join_type>
JOIN
	<right_table> ON <join_condition>
WHERE
	<where_condition>
GROUP BY
	<group_by_list>
HAVING
	<having_condition>
ORDER BY
	<order_by_condition>
LIMIT
	<limit_params>
```

执行顺序

``` sql
FROM	<left_table>

ON 		<join_condition>

<join_type>		JOIN	<right_table>

WHERE		<where_condition>

GROUP BY 	<group_by_list>

HAVING		<having_condition>

SELECT DISTINCT		<select list>

ORDER BY	<order_by_condition>

LIMIT		<limit_params>
```



#### 11.2 正则表达式使用

正则表达式（Regular Expression）是指一个用来描述或者匹配一系列符合某个句法规则的字符串的单个字符串。

| 符号   | 含义                          |
| ------ | ----------------------------- |
| ^      | 在字符串开始处进行匹配        |
| $      | 在字符串末尾处进行匹配        |
| .      | 匹配任意单个字符, 包括换行符  |
| [...]  | 匹配出括号内的任意字符        |
| [^...] | 匹配不出括号内的任意字符      |
| a*     | 匹配零个或者多个a(包括空串)   |
| a+     | 匹配一个或者多个a(不包括空串) |
| a?     | 匹配零个或者一个a             |
| a1\|a2 | 匹配a1或a2                    |
| a(m)   | 匹配m个a                      |
| a(m,)  | 至少匹配m个a                  |
| a(m,n) | 匹配m个a 到 n个a              |
| a(,n)  | 匹配0到n个a                   |
| (...)  | 将模式元素组成单一元素        |

```
select * from emp where name regexp '^T';

select * from emp where name regexp '2$';

select * from emp where name regexp '[uvw]';
```



#### 11.3 MySQL 常用函数

数字函数

| 函数名称        | 作 用                                                      |
| --------------- | ---------------------------------------------------------- |
| ABS             | 求绝对值                                                   |
| SQRT            | 求二次方根                                                 |
| MOD             | 求余数                                                     |
| CEIL 和 CEILING | 两个函数功能相同，都是返回不小于参数的最小整数，即向上取整 |
| FLOOR           | 向下取整，返回值转化为一个BIGINT                           |
| RAND            | 生成一个0~1之间的随机数，传入整数参数是，用来产生重复序列  |
| ROUND           | 对所传参数进行四舍五入                                     |
| SIGN            | 返回参数的符号                                             |
| POW 和 POWER    | 两个函数的功能相同，都是所传参数的次方的结果值             |
| SIN             | 求正弦值                                                   |
| ASIN            | 求反正弦值，与函数 SIN 互为反函数                          |
| COS             | 求余弦值                                                   |
| ACOS            | 求反余弦值，与函数 COS 互为反函数                          |
| TAN             | 求正切值                                                   |
| ATAN            | 求反正切值，与函数 TAN 互为反函数                          |
| COT             | 求余切值                                                   |

字符串函数

| 函数名称  | 作 用                                                        |
| --------- | ------------------------------------------------------------ |
| LENGTH    | 计算字符串长度函数，返回字符串的字节长度                     |
| CONCAT    | 合并字符串函数，返回结果为连接参数产生的字符串，参数可以使一个或多个 |
| INSERT    | 替换字符串函数                                               |
| LOWER     | 将字符串中的字母转换为小写                                   |
| UPPER     | 将字符串中的字母转换为大写                                   |
| LEFT      | 从左侧字截取符串，返回字符串左边的若干个字符                 |
| RIGHT     | 从右侧字截取符串，返回字符串右边的若干个字符                 |
| TRIM      | 删除字符串左右两侧的空格                                     |
| REPLACE   | 字符串替换函数，返回替换后的新字符串                         |
| SUBSTRING | 截取字符串，返回从指定位置开始的指定长度的字符换             |
| REVERSE   | 字符串反转（逆序）函数，返回与原始字符串顺序相反的字符串     |

日期函数

| 函数名称                | 作 用                                                        |
| ----------------------- | ------------------------------------------------------------ |
| CURDATE 和 CURRENT_DATE | 两个函数作用相同，返回当前系统的日期值                       |
| CURTIME 和 CURRENT_TIME | 两个函数作用相同，返回当前系统的时间值                       |
| NOW 和  SYSDATE         | 两个函数作用相同，返回当前系统的日期和时间值                 |
| MONTH                   | 获取指定日期中的月份                                         |
| MONTHNAME               | 获取指定日期中的月份英文名称                                 |
| DAYNAME                 | 获取指定曰期对应的星期几的英文名称                           |
| DAYOFWEEK               | 获取指定日期对应的一周的索引位置值                           |
| WEEK                    | 获取指定日期是一年中的第几周，返回值的范围是否为 0〜52 或 1〜53 |
| DAYOFYEAR               | 获取指定曰期是一年中的第几天，返回值范围是1~366              |
| DAYOFMONTH              | 获取指定日期是一个月中是第几天，返回值范围是1~31             |
| YEAR                    | 获取年份，返回值范围是 1970〜2069                            |
| TIME_TO_SEC             | 将时间参数转换为秒数                                         |
| SEC_TO_TIME             | 将秒数转换为时间，与TIME_TO_SEC 互为反函数                   |
| DATE_ADD 和 ADDDATE     | 两个函数功能相同，都是向日期添加指定的时间间隔               |
| DATE_SUB 和 SUBDATE     | 两个函数功能相同，都是向日期减去指定的时间间隔               |
| ADDTIME                 | 时间加法运算，在原始时间上添加指定的时间                     |
| SUBTIME                 | 时间减法运算，在原始时间上减去指定的时间                     |
| DATEDIFF                | 获取两个日期之间间隔，返回参数 1 减去参数 2 的值             |
| DATE_FORMAT             | 格式化指定的日期，根据参数返回指定格式的值                   |
| WEEKDAY                 | 获取指定日期在一周内的对应的工作日索引                       |

聚合函数

| 函数名称 | 作用                             |
| -------- | -------------------------------- |
| MAX      | 查询指定列的最大值               |
| MIN      | 查询指定列的最小值               |
| COUNT    | 统计查询结果的行数               |
| SUM      | 求和，返回指定列的总和           |
| AVG      | 求平均值，返回指定列数据的平均值 |



## 12.MySQL常用

#### 12.1 mysql

该mysql不是指mysql服务，而是指mysql的客户端工具。

语法 ：

```
mysql [options] [database]
```

##### 12.1.1 连接选项

```
参数 ： 
	-u, --user=name			指定用户名
	-p, --password[=name]	指定密码
	-h, --host=name			指定服务器IP或域名
	-P, --port=#			指定连接端口

示例 ：
	mysql -h 127.0.0.1 -P 3306 -u root -p
	
	mysql -h127.0.0.1 -P3306 -uroot -p2143
	
```

##### 12.1.2 执行选项

```
-e, --execute=name		执行SQL语句并退出
```

此选项可以在Mysql客户端执行SQL语句，而不用连接到MySQL数据库再执行，对于一些批处理脚本，这种方式尤其方便。

```
示例：
	mysql -uroot -p2143 db01 -e "select * from tb_book";
```

![1555325632715](images\1555325632715.png) 

#### 12.2 mysqladmin

mysqladmin 是一个执行管理操作的客户端程序。可以用它来检查服务器的配置和当前状态、创建并删除数据库等。

可以通过 ： mysqladmin --help  指令查看帮助文档

![1555326108697](images\1555326108697.png) 

```
示例 ：
	mysqladmin -uroot -p2143 create 'test01';  
	mysqladmin -uroot -p2143 drop 'test01';
	mysqladmin -uroot -p2143 version;
	
```

#### 12.3 mysqlbinlog

由于服务器生成的二进制日志文件以二进制格式保存，所以如果想要检查这些文本的文本格式，就会使用到mysqlbinlog 日志管理工具。

语法 ：

```
mysqlbinlog [options]  log-files1 log-files2 ...

选项：
	
	-d, --database=name : 指定数据库名称，只列出指定的数据库相关操作。
	
	-o, --offset=# : 忽略掉日志中的前n行命令。
	
	-r,--result-file=name : 将输出的文本格式日志输出到指定文件。
	
	-s, --short-form : 显示简单格式， 省略掉一些信息。
	
	--start-datatime=date1  --stop-datetime=date2 : 指定日期间隔内的所有日志。
	
	--start-position=pos1 --stop-position=pos2 : 指定位置间隔内的所有日志。
```

#### 12.4 mysqldump

mysqldump 客户端工具用来备份数据库或在不同数据库之间进行数据迁移。备份内容包含创建表，及插入表的SQL语句。

语法 ：

```
mysqldump [options] db_name [tables]

mysqldump [options] --database/-B db1 [db2 db3...]

mysqldump [options] --all-databases/-A
```

##### 12.4.1 连接选项

```
参数 ： 
	-u, --user=name			指定用户名
	-p, --password[=name]	指定密码
	-h, --host=name			指定服务器IP或域名
	-P, --port=#			指定连接端口
```

##### 12.4.2 输出内容选项

```
参数：
	--add-drop-database		在每个数据库创建语句前加上 Drop database 语句
	--add-drop-table		在每个表创建语句前加上 Drop table 语句 , 默认开启 ; 不开启 (--skip-add-drop-table)
	
	-n, --no-create-db		不包含数据库的创建语句
	-t, --no-create-info	不包含数据表的创建语句
	-d --no-data			不包含数据
	
	 -T, --tab=name			自动生成两个文件：一个.sql文件，创建表结构的语句；
	 						一个.txt文件，数据文件，相当于select into outfile  
```

```
示例 ： 
	mysqldump -uroot -p2143 db01 tb_book --add-drop-database --add-drop-table > a
	
	mysqldump -uroot -p2143 -T /tmp test city
```

#### 12.5 mysqlimport/source

mysqlimport 是客户端数据导入工具，用来导入mysqldump 加 -T 参数后导出的文本文件。

语法：

```
mysqlimport [options]  db_name  textfile1  [textfile2...]
```

示例：

```
mysqlimport -uroot -p2143 test /tmp/city.txt
```



如果需要导入sql文件,可以使用mysql中的source 指令 : 

```
source /root/tb_book.sql
```



#### 12.6 mysqlshow

mysqlshow 客户端对象查找工具，用来很快地查找存在哪些数据库、数据库中的表、表中的列或者索引。

语法：

```
mysqlshow [options] [db_name [table_name [col_name]]]
```

参数：

```
--count		显示数据库及表的统计信息（数据库，表 均可以不指定）

-i			显示指定数据库或者指定表的状态信息
```



示例：

```
#查询每个数据库的表的数量及表中记录的数量
mysqlshow -uroot -p2143 --count

#查询test库中每个表中的字段书，及行数
mysqlshow -uroot -p2143 test --count

#查询test库中book表的详细情况
mysqlshow -uroot -p2143 test book --count

```

![](images\132124.png) 

![](images\表.png)



## 13. MySQL日志

在任何一种数据库中，都会有各种各样的日志，记录着数据库工作的方方面面，以帮助数据库管理员追踪数据库曾经发生过得各种事件，MySQL也不例外，在MySQL中，有四种不同的日志，分别是错误日志，二进制日志（binlog），查询日志和慢查询日志，这些日志记录着数据库在不同方面的踪迹

### 13.1错误日志

错误日志是MySQL中最重要的日志之一，他记录了当mysqld启动和停止时，以及服务器在运行过程中发生热河严重错误时的相关信息。当数据库出现任何故障导致无法正常使用时，可以首先查看此日志

该日志默认是开启的，默认存放目录为 mysql 的数据目录（var/lib/mysql）, 默认的日志文件名为  hostname.err（hostname是主机名）。

查看日志位置指令

```sql
show variables like '%log_error%';
```

![](images\错误日志.png)

查看日志内容

```sql
tail -f /var/lib/mysql/hostname.err
```

![](images\1553993537874.png)

### 13.2二进制日志（BINLOG）

#### 13.2.1 概述

二进制日志（BINLOG）记录了所有的DDL（数据定义语言）语句和DML（数据操纵语言）语句，但是不包括数据查询语句。**此日志对于灾难时的数据恢复骑着极其重要的作用，MySQL的主从复制就是通过BINLOG实现的**

默认情况下是没有开启的，需要到MySQL的配置文件中开启，并配置MySQL日志格式

配置文件：my.cnf

日志存放位置：配置时，给定了文件名，但是没有指定路径，日志默认写入MySQL的数据目录

```sql
#配置开启binlog日志， 日志的文件前缀为 mysqlbin -----> 生成的文件名如 : mysqlbin.000001
log_bin=mysqlbin

#配置二进制日志的格式
binlog_format=STATEMENT
```

#### 13.2.2 日志格式

**STATEMENT**

该日志格式在日志文件中记录的都是SQL语句（statement），每一条对数据进行修改的SQL都会记录在日志文件中，通过MySQL提供的mysqlbinlog工具，可以清晰的查看到每条语句的文本。主从复制的时候，从库（slave）会将日志解析为原文本，并在从库重新执行一次。

**ROW**

该日志格式在日志文件中记录的是每一行的数据变更，而不是记录SQL语句。比如：执行SQL语句：update table set id=1；，如果是STATEMENT日志格式，在日志文件中会记录一条SQL语句，如果是ROW，由于是对全表进行更新，也就是每一行记录都会发生变更，ROW格式的日志中会记录每一行的数据变更。**简单来说，ROW记录的是发生变化的每一行数据**

**MIXED**

这是目前MySQL默认的日志格式，即混合了STATEMENT 和 ROW两种格式。默认情况下采用STATEMENT，但是在一些特殊情况下采用ROW来进行记录。MIXED 格式能尽量利用两种模式的优点，而避开他们的缺点。

#### 13.2.3日志读取

由于日志以二进制方式存储，不能直接读取，需要使用mysqlbinlog工具来查看

```mysql
mysqlbinlog log-file
```

**查看STATEMENT格式的日志**

查看日志文件

![](images\1554079717375.png)

mysqlbin.index : 该文件是日志索引文件 ， 记录日志的文件名；

mysqlbing.000001 ：日志文件

查看到的内容

```sql
mysqlbinlog log-file
```

![](images\1554080016778.png)

**查看ROW格式日志**

配置

```mysql
#配置开启binlog日志， 日志的文件前缀为 mysqlbin -----> 生成的文件名如 : mysqlbin.000001,mysqlbin.000002
log_bin=mysqlbin

#配置二进制日志的格式
binlog_format=ROW
```

查看日志，需要在mysqlbinlog后面添加上参数-vv

```mysq
mysqlbinlog -vv mysqlbin.000002
```

![](images\1554095452022.png)

#### 13.2.4日志删除

**方式一：Reset Master**

通过Reset Master指令删除全部binlog日志，删除之后，日志编号将从xxxxx.000001重新开始

查询之前 ，先查询下日志文件 ： 

![1554118609489](images\1554118609489.png)   

执行删除日志指令： 

```
Reset Master
```

执行之后， 查看日志文件 ：

![1554118675264](images\1554118675264.png) 

**方式二：删除指定编号之前的**

```sql
purge master logs to 'mysqlbin.*****'
```

该命令将删除``` ******``` 编号之前的所有日志。 

**方式三：删除指定日期之前的**

```mysql
purge master logs before 'yyyy-mm-dd hh24:mi:ss'
```

该命令将删除日志为 "yyyy-mm-dd hh24:mi:ss" 之前产生的所有日志 。

**方式四：设置过期参数**

设置参数 --expire_logs_days=# ，此参数的含义是设置日志的过期天数， 过了指定的天数后日志将会被自动删除，这样将有利于减少DBA 管理日志的工作量。

![](images\1554125506938.png)



### 13.3 查询日志

查询日志中记录了客户端的所有操作语句，而二进制日志不包含查询数据的SQL语句。

默认情况下， 查询日志是未开启的。如果需要开启查询日志，可以设置以下配置 ：

```
#该选项用来开启查询日志 ， 可选值 ： 0 或者 1 ； 0 代表关闭， 1 代表开启 
general_log=1

#设置日志的文件名 ， 如果没有指定， 默认的文件名为 host_name.log 
general_log_file=file_name

```

在 mysql 的配置文件 /usr/my.cnf 中配置如下内容 ： 

![1554128184632](images\1554128184632.png) 

配置完毕之后，在数据库执行以下操作 ：

```
select * from tb_book;
select * from tb_book where id = 1;
update tb_book set name = 'lucene入门指南' where id = 5;
select * from tb_book where id < 8;

```



执行完毕之后， 再次来查询日志文件 ： 

![1554128089851](images\1554128089851.png) 



### 13.4 慢查询日志

慢查询日志记录了所有执行时间超过参数 long_query_time 设置值并且扫描记录数不小于 min_examined_row_limit 的所有的SQL语句的日志。long_query_time 默认为 10 秒，最小为 0， 精度可以到微秒。



##### 13.4.1 文件位置和格式

慢查询日志默认是关闭的 。可以通过两个参数来控制慢查询日志 ：

```sql
# 该参数用来控制慢查询日志是否开启， 可取值： 1 和 0 ， 1 代表开启， 0 代表关闭
slow_query_log=1 

# 该参数用来指定慢查询日志的文件名
slow_query_log_file=slow_query.log

# 该选项用来配置查询的时间限制， 超过这个时间将认为值慢查询， 将需要进行日志记录， 默认10s
long_query_time=10

```



##### 13.4.2 日志的读取

和错误日志、查询日志一样，慢查询日志记录的格式也是纯文本，可以被直接读取。

1） 查询long_query_time 的值。

![1554130333472](images\1554130333472.png) 



2） 执行查询操作

```sql
select id, title,price,num ,status from tb_item where id = 1;
```

![1554130448709](images\1554130448709.png)

由于该语句执行时间很短，为0s ， 所以不会记录在慢查询日志中。



```
select * from tb_item where title like '%阿尔卡特 (OT-927) 炭黑 联通3G手机 双卡双待165454%' ;

```

![1554130532577](images\1554130532577.png) 

该SQL语句 ， 执行时长为 26.77s ，超过10s ， 所以会记录在慢查询日志文件中。



3） 查看慢查询日志文件

直接通过cat 指令查询该日志文件 ： 

![1554130669360](images\1554130669360.png) 



如果慢查询日志内容很多， 直接查看文件，比较麻烦， 这个时候可以借助于mysql自带的 mysqldumpslow 工具， 来对慢查询日志进行分类汇总。 

![1554130856485](images\1554130856485.png)

## 14. Mysql复制

#### 14.1 复制概述

复制是指将主数据库的DDL 和 DML 操作通过二进制日志传到从库服务器中，然后在从库上对这些日志重新执行（也叫重做），从而使得从库和主库的数据保持同步。

MySQL支持一台主库同时向多台从库进行复制， 从库同时也可以作为其他从服务器的主库，实现链状复制。



#### 14.2 复制原理

MySQL 的主从复制原理如下。

![1554423698190](images\主从.jpg) 

从上层来看，复制分成三步：

- Master 主库在事务提交时，会把数据变更作为时间 Events 记录在二进制日志文件 Binlog 中。
- 主库推送二进制日志文件 Binlog 中的日志事件到从库的中继日志 Relay Log 。

- slave重做中继日志中的事件，将改变反映它自己的数据。



#### 14.3 复制优势

MySQL 复制的有点主要包含以下三个方面：

- 主库出现问题，可以快速切换到从库提供服务。

- 可以在从库上执行查询操作，从主库中更新，实现读写分离，降低主库的访问压力。

- 可以在从库中执行备份，以避免备份期间影响主库的服务。



#### 14.4 搭建步骤

##### 14.4.1 master

1） 在master 的配置文件（/usr/my.cnf）中，配置如下内容：

```properties
#mysql 服务ID,保证整个集群环境中唯一
server-id=1

#mysql binlog 日志的存储路径和文件名
log-bin=/var/lib/mysql/mysqlbin

#错误日志,默认已经开启
#log-err

#mysql的安装目录
#basedir

#mysql的临时目录
#tmpdir

#mysql的数据存放目录
#datadir

#是否只读,1 代表只读, 0 代表读写
read-only=0

#忽略的数据, 指不需要同步的数据库
binlog-ignore-db=mysql

#指定同步的数据库
#binlog-do-db=db01
```

2） 执行完毕之后，需要重启Mysql：

```sql
service mysql restart ；
```

3） 创建同步数据的账户，并且进行授权操作：

```sql
grant replication slave on *.* to 'itcast'@'192.168.192.131' identified by 'itcast';	

flush privileges;
```

4） 查看master状态：

```sql
show master status;
```

![1554477759735](images\1554477759735.png) 

字段含义：

```
File : 从哪个日志文件开始推送日志文件 
Position ： 从哪个位置开始推送日志
Binlog_Ignore_DB : 指定不需要同步的数据库
```



##### 14.4.2 slave

1） 在 slave 端配置文件中，配置如下内容：

```properties
#mysql服务端ID,唯一
server-id=2

#指定binlog日志
log-bin=/var/lib/mysql/mysqlbin
```

2）  执行完毕之后，需要重启Mysql：

```
service mysql restart；
```

3） 执行如下指令 ：

```sql
change master to master_host= '192.168.192.130', master_user='itcast', master_password='itcast', master_log_file='mysqlbin.000001', master_log_pos=413;
```

指定当前从库对应的主库的IP地址，用户名，密码，从哪个日志文件开始的那个位置开始同步推送日志。

4） 开启同步操作

```
start slave;

show slave status;
```

![1554479387365](images\1554479387365.png) 

5） 停止同步操作

```
stop slave;
```



##### 14.4.3 验证同步操作

1） 在主库中创建数据库，创建表，并插入数据 ：

```sql
create database db01;

user db01;

create table user(
	id int(11) not null auto_increment,
	name varchar(50) not null,
	sex varchar(1),
	primary key (id)
)engine=innodb default charset=utf8;

insert into user(id,name,sex) values(null,'Tom','1');
insert into user(id,name,sex) values(null,'Trigger','0');
insert into user(id,name,sex) values(null,'Dawn','1');
```

2） 在从库中查询数据，进行验证 ：

在从库中，可以查看到刚才创建的数据库：

![1554544658640](images\1554544658640.png) 

在该数据库中，查询user表中的数据：

![1554544679538](images\1554544679538.png) 