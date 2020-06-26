package com.on.arithmetic.tree;

import java.util.ArrayDeque;
import java.util.Queue;

public class zhezhi {

    class Node {
        String item;
        Node left;
        Node right;

        public Node(String item, Node left, Node right) {
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }

    public Node createTree(int n) {
        Node root = null;
        for (int i = 0; i < n; i++) {
            //如果当前树为空树
            if (i == 0) {
                root = new Node("down", null, null);
                continue;
            }
            /**
             * 这里利用了层序遍历的思想，利用一个中间队列，
             * 找到没有左子树也没有右子树的节点，然后为该节点添加左右节点
             */
            Queue<Node> queue = new ArrayDeque();
            queue.add(root);
            while (!queue.isEmpty()) {
                //取出队列中的节点
                Node poll = queue.poll();
                //如果当前节点的左子节点不为空
                if (poll.left != null) {
                    queue.add(poll.left);
                }
                //如果当前节点的右子节点不为空
                if (poll.right != null) {
                    queue.add(poll.right);
                }
                //如果当前节点的左右节点都为空
                if (poll.left == null && poll.right == null) {
                    poll.left = new Node("down", null, null);
                    poll.right = new Node("up", null, null);
                }
            }
        }
        return root;
    }

    /**
     * 利用中序遍历输出当前树
     *
     * @param x
     */
    public void middleIterator(Node x) {
        if (x == null) {
            return;
        }
        if (x.left != null) {
            middleIterator(x.left);
        }
        System.out.println(x.item);
        if (x.right != null) {
            middleIterator(x.right);
        }
    }

    public static void main(String[] args) {
        zhezhi zz = new zhezhi();
        Node tree = zz.createTree(2);
        zz.middleIterator(tree);
    }
}
