package com.on.arithmetic.graph;


import java.util.ArrayDeque;
import java.util.Queue;

public class Graph {
    //顶点数目
    private final int V;
    //边的数目
    private int E;
    //邻接表
    private final Queue<Integer>[] adj;

    public Graph(int V) {
        //初始化顶点数量
        this.V = V;
        //初始化边的数量
        this.E = 0;
        //初始化邻接表
        this.adj = new Queue[V];

        for (int i = 0; i < adj.length; i++) {
            adj[i] = new ArrayDeque<Integer>();
        }
    }


    //获取顶点数目
    public int V() {
        return V;
    }

    //获取边的数目
    public int E() {
        return E;
    }

    //向图中添加一条边 v-w
    public void addEdge(int v, int w) {
        //在无向图中，边是没有方向的，所以该边既可以说是从v到w的边，
        // 又可以说是从w到v的边，因此，需要让w出现在v的邻接表中，
        // 并且还要让v出现在w的邻接表中

        adj[v].add(w);
        adj[w].add(v);
        //边的数量+1
        E++;

    }

    //获取和顶点v相邻的所有顶点
    public Queue<Integer> adj(int v) {
        return adj[v];
    }

    public static void main(String[] args) {

        //准备Graph对象
        Graph G = new Graph(13);
        G.addEdge(0, 5);
        G.addEdge(0, 1);
        G.addEdge(0, 2);
        G.addEdge(0, 6);
        G.addEdge(5, 3);
        G.addEdge(5, 4);
        G.addEdge(3, 4);
        G.addEdge(4, 6);

        G.addEdge(7, 8);

        G.addEdge(9, 11);
        G.addEdge(9, 10);
        G.addEdge(9, 12);
        G.addEdge(11, 12);


        //准备深度优先搜索对象
        DepthFirstSearch search = new DepthFirstSearch(G, 0);
        System.out.println();
        //广度优先搜索
        BreadthFirstSearch breadthFirstSearch = new BreadthFirstSearch(G, 0);
        System.out.println();
        //测试与某个顶点相通的顶点数量
        int count = search.count();
        System.out.println("与起点0相通的顶点的数量为:" + count);


        //测试某个顶点与起点是否相同
        boolean marked1 = search.marked(5);
        System.out.println("顶点5和顶点0是否相通：" + marked1);


        boolean marked2 = search.marked(7);
        System.out.println("顶点7和顶点0是否相通：" + marked2);

    }

}