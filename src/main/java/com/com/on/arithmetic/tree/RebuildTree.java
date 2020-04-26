package com.com.on.arithmetic.tree;

public class RebuildTree {

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    /**
     * 前序遍历序列{1,2,4,7,3,5,6,8}
     * 中序遍历序列{4,7,2,1,5,3,8,6}
     *
     * @param pre
     * @param in
     * @return
     */

    public TreeNode reConstructBinaryTree(int[] pre, int[] in) {
        if (pre == null || in == null) {
            return null;
        }
        TreeNode treeNode = rebuildTree(pre, 0, pre.length - 1, in, 0, in.length - 1);
        return treeNode;
    }

    /**
     * 前序遍历序列{1,2,4,7,3,5,6,8}
     * 中序遍历序列{4,7,2,1,5,3,8,6}
     *
     * @param pre
     * @param preStart
     * @param preEnd
     * @param in
     * @param inStart
     * @param inEnd
     * @return
     */
    public TreeNode rebuildTree(int[] pre, int preStart, int preEnd, int[] in, int inStart, int inEnd) {
        if (preStart > preEnd || inStart > inEnd) {
            return null;
        }
        //通过前序遍历可以确定pre中的第一个数是当前数的头结点
        TreeNode root = new TreeNode(pre[preStart]);
        //需要在中序遍历中找到头节点的位置，然后左边的就是左子树，右边的就是右子树
        for (int i = inStart; i <= inEnd; i++) {
            //说明在中序遍历的数组中找到了当前树的根节点
            //这里的节点需要计算，不能单纯靠 i
            if (pre[preStart] == in[i]) {
                root.left = rebuildTree(pre, preStart + 1, preStart + i - inStart, in, inStart, i);
                root.right = rebuildTree(pre, i - inStart + preStart + 1, preEnd, in, i + 1, inEnd);
            }
        }
        return root;
    }

    public static void main(String[] args) {
        int[] pre = {1, 2, 4, 7, 3, 5, 6, 8};
        int[] in = {4, 7, 2, 1, 5, 3, 8, 6};
        RebuildTree rebuildTree = new RebuildTree();
        TreeNode treeNode = rebuildTree.reConstructBinaryTree(pre, in);
        System.out.println(treeNode);
    }
}

