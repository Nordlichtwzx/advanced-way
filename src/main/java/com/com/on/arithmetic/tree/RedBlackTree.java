package com.com.on.arithmetic.tree;

public class RedBlackTree<Key extends Comparable<Key>, Value> {
    //根节点
    private Node root;
    //记录树中元素的个数
    private int N;
    //红色链接
    private static final boolean RED = true;
    //黑色链接
    private static final boolean BLACK = false;


    //结点类
    private class Node {
        //存储键
        public Key key;
        //存储值
        private Value value;
        //记录左子结点
        public Node left;
        //记录右子结点
        public Node right;
        //由其父结点指向它的链接的颜色
        public boolean color;

        public Node(Key key, Value value, Node left, Node right, boolean color) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
            this.color = color;
        }
    }


    //获取树中元素的个数
    public int size() {
        return N;
    }


    /**
     * 判断当前节点的父指向链接是否为红色
     *
     * @param x
     * @return
     */
    private boolean isRed(Node x) {
        if (x == null) {
            return false;
        }
        return x.color;
    }

    /**
     * 左旋转
     *
     * @param h
     * @return
     */
    private Node rotateLeft(Node h) {
        //声明h节点的由右子节点为x节点
        Node x = h.right;
        //h节点的右子节点为x节点的左子节点
        h.right = x.left;
        //h节点成为x节点的左子节点
        x.left = h;
        //x节点的color为h节点的color
        x.color = h.color;
        //h节点的color为红色
        h.color = RED;
        return x;
    }

    /**
     * 右旋
     *
     * @param h
     * @return
     */
    private Node rotateRight(Node h) {
        //声明h的左子节点为x节点
        Node x = h.left;
        //x节点的右子节点为h节点的左子节点
        h.left = x.right;
        //h节点为x节点的右子节点
        x.right = h;
        //x节点的color为h的color
        x.color = h.color;
        //h的color为red
        h.color = RED;
        return x;
    }

    /**
     * 颜色反转,相当于完成拆分4-节点
     *
     * @param h
     */
    private void flipColors(Node h) {
        //将当前节点color置位黑
        h.color = RED;
        //左子节点为黑色；
        h.left.color = BLACK;
        //右子节点为黑色
        h.right.color = BLACK;
    }

    /**
     * 在整个树上完成插入操作
     *
     * @param key
     * @param val
     */
    public void put(Key key, Value val) {
        root = put(root, key, val);
        root.color = BLACK;
    }

    /**
     * 在指定树中，完成插入操作,并返回添加元素后新的树
     *
     * @param h
     * @param key
     * @param val
     */
    private Node put(Node h, Key key, Value val) {
        //判断h这棵树是否为空，如果为空则添加一个颜色为红色的并且没有子节点的节点并返回
        if (h == null) {
            return new Node(key, val, null, null, RED);
        }
        //如果不为空
        if (key.compareTo(h.key) < 0) {
            //小于当前节点，向左走
            h.left = put(h.left, key, val);
        } else if (key.compareTo(h.key) > 0) {
            //大于当前节点，向右走
            h.right = put(h.right, key, val);
        } else {
            //等于，替换
            h.value = val;
        }
        //插入完成后
        //左旋
        if (isRed(h.right) && !isRed(h.left)) {
            h = rotateLeft(h);
        }
        //右旋
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }
        //颜色交换
        if (isRed(h.left) && isRed(h.right)) {
            flipColors(h);
        }
        return h;
    }

    //根据key，从树中找出对应的值
    public Value get(Key key) {
        return get(root, key);
    }

    //从指定的树x中，查找key对应的值
    public Value get(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int compare = key.compareTo(x.key);
        if (compare < 0) {
            return get(x.left, key);
        } else if (compare > 0) {
            return get(x.right, key);
        } else {
            return x.value;
        }
    }

    public static void main(String[] args) {
        //创建红黑树
        RedBlackTree<String, String> tree = new RedBlackTree<>();

        //往树中插入元素
        tree.put("1", "张三");
        tree.put("2", "李四");
        tree.put("3", "王五");
        //从树中获取元素
        String r1 = tree.get("1");
        System.out.println(r1);


        String r2 = tree.get("2");
        System.out.println(r2);

        String r3 = tree.get("3");
        System.out.println(r3);
    }

}

