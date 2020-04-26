package com.com.on.arithmetic.graph;

import java.util.ArrayDeque;
import java.util.Queue;

public class BreadthFirstSearch {
    //索引代表顶点，值表示当前顶点是否已经被搜索
    private boolean[] marked;
    //记录有多少个顶点与s顶点相通
    private int count;
    //用来存储待搜索邻接表的点
    private Queue<Integer> waitSearch;

    //构造广度优先搜索对象，使用广度优先搜索找出G图中s顶点的所有相邻顶点
    public BreadthFirstSearch(Graph G, int s) {
        this.marked = new boolean[G.V()];
        this.count = 0;
        this.waitSearch = new ArrayDeque<>();

        bfs(G, s);
    }

    //使用广度优先搜索找出G图中v顶点的所有相邻顶点
    private void bfs(Graph G, int v) {
        marked[v] = true;
        waitSearch.add(v);
        while (!waitSearch.isEmpty()) {
            Integer poll = waitSearch.poll();
            for (Integer integer : G.adj(poll)) {
                if (!marked[integer]) {
                    bfs(G, integer);
                }
            }
        }
        count++;
    }

    //判断w顶点与s顶点是否相通
    public boolean marked(int w) {
        return marked[w];
    }

    //获取与顶点s相通的所有顶点的总数
    public int count() {
        return count;
    }
}
