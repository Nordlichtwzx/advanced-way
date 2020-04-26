package com.com.on.arithmetic.tree;

import java.util.ArrayDeque;
import java.util.Queue;

public class BinaryTree<Key extends Comparable, Value> {
    //根节点
    private Node root;
    //树中节点的个数
    private int n;

    private class Node {
        private Key key;
        private Value value;
        private Node left;
        private Node right;

        public Node(Key key, Value value, Node left, Node right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * 树的节点个数
     *
     * @return
     */
    public int size() {
        return n;
    }

    public void put(Key key, Value value) {
        root = put(root, key, value);
    }

    /**
     * 往二叉树中插入元素
     *
     * @param x
     * @param key
     * @param value
     * @return
     */
    public Node put(Node x, Key key, Value value) {
        if (x == null) {
            n++;
            return new Node(key, value, null, null);
        }
        //如果x节点不为空
        int compare = key.compareTo(x.key);
        if (compare > 0) {
            //如果key大于x节点的key，则往右走
            x.right = put(x.right, key, value);
        } else if (compare < 0) {
            //如果key小于x节点的key，则往左走
            x.left = put(x.left, key, value);
        } else {
            //如果key等于x节点的key，则直接替换
            x.value = value;
        }
        return x;
    }

    public Value get(Key key) {
        return get(root, key);
    }

    /**
     * 在指定的树中通过指定的key获取元素
     *
     * @param x
     * @param key
     * @return
     */
    public Value get(Node x, Key key) {
        if (x == null) {
            return null;
        }
        //如果x节点不为空
        int compare = key.compareTo(x.key);
        if (compare > 0) {
            //如果key大于x节点的key，则往右走
            return get(x.right, key);
        } else if (compare < 0) {
            //如果key小于x节点的key，则往左走
            return get(x.left, key);
        } else {
            //如果key等于x节点的key，则直接返回
            return x.value;
        }
    }

    public void delete(Key key) {
        delete(root, key);
    }

    public Node delete(Node x, Key key) {
        if (x == null) {
            return null;
        }
        //如果x节点不为空
        int compare = key.compareTo(x.key);
        if (compare > 0) {
            //如果key大于x节点的key，则往右走
            x.right = delete(x.right, key);
        } else if (compare < 0) {
            //如果key小于x节点的key，则往左走
            x.left = delete(x.left, key);
        } else {
            //如果key等于x节点的key，则删除
            //只需要将相等的节点的右子树中最小的节点和该节点替换即可满足平衡
            n--;
            if (x.left == null) {
                return x.right;
            }
            if (x.right == null) {
                return x.left;
            }
            Node minNode = x.right;
            while (minNode.left != null) {
                minNode = minNode.left;
            }
            Node n = x.right;
            while (n.left != null) {
                if (n.left.left == null) {
                    n.left = null;
                }
            }

            minNode.left = x.left;
            minNode.right = x.right;
            x = minNode;
        }

        return x;
    }


    public Key minNode() {
        return minNode(root).key;
    }

    public Node minNode(Node x) {
        if (x.left != null) {
            return minNode(x.left);
        } else {
            return x;
        }
    }

    public Key maxNode() {
        return maxNode(root).key;
    }

    public Node maxNode(Node x) {
        if (x.right != null) {
            return maxNode(x.right);
        } else {
            return x;
        }
    }

    public Queue<Key> frontIterator() {
        Queue<Key> keys = new ArrayDeque<>();
        frontIterator(keys, root);
        return keys;
    }

    /**
     * 前序遍历
     * 中左右
     *
     * @param keys
     * @param x
     */
    public void frontIterator(Queue<Key> keys, Node x) {
        if (x == null) {
            return;
        }
        keys.add(x.key);
        if (x.left != null) {
            frontIterator(keys, x.left);
        }
        if (x.right != null) {
            frontIterator(keys, x.right);
        }
    }

    public Queue<Key> middleIterator() {
        Queue<Key> keys = new ArrayDeque<>();
        middleIterator(keys, root);
        return keys;
    }

    /**
     * 中序遍历
     * 左中右
     *
     * @param keys
     * @param x
     */
    public void middleIterator(Queue<Key> keys, Node x) {
        if (x == null) {
            return;
        }
        if (x.left != null) {
            middleIterator(keys, x.left);
        }
        keys.add(x.key);
        if (x.right != null) {
            middleIterator(keys, x.right);
        }
    }

    public Queue<Key> behindIterator() {
        Queue<Key> keys = new ArrayDeque<>();
        behindIterator(keys, root);
        return keys;
    }

    /**
     * 后序遍历
     * 左右中
     *
     * @param keys
     * @param x
     */
    public void behindIterator(Queue<Key> keys, Node x) {
        if (x == null) {
            return;
        }
        if (x.left != null) {
            behindIterator(keys, x.left);
        }
        if (x.right != null) {
            behindIterator(keys, x.right);
        }
        keys.add(x.key);
    }


    /**
     * 层序遍历
     * 主要用到了一个队列，将当前节点的左右节点放入到队列中，然后按顺序出队，再进行循环
     *
     * @return
     */
    public Queue<Key> cenxuIterator() {
        Queue<Key> keys = new ArrayDeque();
        Queue<Node> nodes = new ArrayDeque();
        nodes.add(root);
        while (!nodes.isEmpty()) {
            Node poll = nodes.poll();
            keys.add(poll.key);
            if (poll.left != null) {
                nodes.add(poll.left);
            }
            if (poll.right != null) {
                nodes.add(poll.right);
            }
        }
        return keys;
    }

    public int maxDeep() {
        return maxDeep(root);
    }

    /**
     * 获取最大深度，分别获取左右子树的最大深度，然后加 1 得到树的最大深度
     *
     * @param x
     * @return
     */
    public int maxDeep(Node x) {
        if (x == null) {
            return 0;
        }
        int max = 0;
        int maxL = 0;
        int maxR = 0;
        if (x.left != null) {
            maxL = maxDeep(x.left);
        }
        if (x.right != null) {
            maxR = maxDeep(x.right);
        }
        max = maxL > maxR ? maxL + 1 : maxR + 1;
        return max;
    }

}
