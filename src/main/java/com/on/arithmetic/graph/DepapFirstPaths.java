package com.on.arithmetic.graph;


import java.util.Stack;

public class DepapFirstPaths {
    //索引代表顶点，值表示当前顶点是否已经被搜索
    private final boolean[] marked;
    //起点
    private final int s;
    //索引代表顶点，值代表从起点s到当前顶点路径上的最后一个顶点
    private final int[] edgeTo;

    //构造深度优先搜索对象，使用深度优先搜索找出G图中起点为s的所有路径
    public DepapFirstPaths(Graph G, int s) {
        //初始化marked数组
        this.marked = new boolean[G.V()];
        //初始化起点
        this.s = s;
        //初始化edgeTo数组
        this.edgeTo = new int[G.V()];

        dfs(G, s);
    }

    //使用深度优先搜索找出G图中v顶点的所有相邻顶点
    private void dfs(Graph G, int v) {
        marked[v] = true;
        for (Integer w : G.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }
        }
    }

    //判断w顶点与s顶点是否存在路径
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    //找出从起点s到顶点v的路径(就是该路径经过的顶点)
    public Stack<Integer> pathTo(int v) {
        if (!hasPathTo(v)) {
            return null;
        }
        Stack stack = new Stack();
        stack.push(v);
        int x = edgeTo[v];
        while (x != s) {
            stack.push(x);
            x = edgeTo[x];
        }
        stack.push(s);
        return stack;
    }

    public static void main(String[] args) throws Exception {
        //根据第一行数据构建一副图Graph
        Graph G = new Graph(6);
        G.addEdge(0, 2);
        G.addEdge(0, 1);
        G.addEdge(2, 1);
        G.addEdge(2, 3);
        G.addEdge(2, 4);
        G.addEdge(3, 5);
        G.addEdge(3, 4);
        G.addEdge(0, 5);
        //构建路径查找对象，并设置起点为0
        DepapFirstPaths paths = new DepapFirstPaths(G, 0);
        //调用 pathTo(4)，找到从起点0到终点4的路径，返回Stack
        Stack<Integer> path = paths.pathTo(4);
        StringBuilder sb = new StringBuilder();
        //遍历栈对象
        while (!path.empty()) {
            sb.append(path.pop() + "-");
        }

        sb.deleteCharAt(sb.length() - 1);

        System.out.println(sb);
    }

}