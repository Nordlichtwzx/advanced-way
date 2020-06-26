package com.on.arithmetic.graph;

public class DepthFirstSearch {
    //索引代表顶点，值表示当前顶点是否已经被搜索
    private final boolean[] marked;
    //记录有多少个顶点与s顶点相通
    private int count;

    //构造深度优先搜索对象，使用深度优先搜索找出G图中s顶点的所有相邻顶点
    public DepthFirstSearch(Graph G, int s) {
        //初始化marked数组，应该和给定图节点个数一样
        this.marked = new boolean[G.V()];
        //初始化节点连接个数为0
        this.count = 0;
        //深度优先遍历图
        dfs(G, s);
    }

    //使用深度优先搜索找出G图中v顶点的所有相通顶点
    private void dfs(Graph G, int v) {
        //将要遍历的节点标记为true
        marked[v] = true;
        for (Integer integer : G.adj(v)) {
            //如果当前当前要搜索的节点在marked数组中为false，则继续搜索，直到搜索到子节点为true
            if (!marked[integer]) {
                dfs(G, integer);
            }
        }
        //深度搜索完成后连接的数量加一
        count++;
    }

    //判断w顶点与s顶点是否相通，marked数组的值为true说明两者联通
    public boolean marked(int w) {
        return marked[w];
    }

    //获取与顶点s相通的所有顶点的总数
    public int count() {
        return count;
    }
}
