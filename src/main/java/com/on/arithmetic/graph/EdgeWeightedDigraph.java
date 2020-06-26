package com.on.arithmetic.graph;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 加权有向图
 *
 * @author Nordlicht
 */
public class EdgeWeightedDigraph {
    //顶点总数
    private final int pointNum;
    //边的总数
    private int edgeNum;
    //邻接表
    private final Queue<DirectedEdge>[] adj;

    //创建一个含有V个顶点的空加权有向图
    public EdgeWeightedDigraph(int num) {
        this.pointNum = num;
        this.edgeNum = 0;
        adj = new Queue[num];
        for (int i = 0; i < adj.length; i++) {
            adj[i] = new ArrayDeque<>();
        }
    }

    //获取图中顶点的数量
    public int V() {
        return pointNum;
    }

    //获取图中边的数量
    public int E() {
        return edgeNum;
    }


    //向加权有向图中添加一条边e
    public void addEdge(DirectedEdge e) {
        adj[e.from()].add(e);
        edgeNum++;
    }

    //获取由顶点v指出的所有的边
    public Queue<DirectedEdge> adj(int v) {
        return adj[v];
    }

    //获取加权有向图的所有边
    public Queue<DirectedEdge> edges() {
        Queue queue = new ArrayDeque();
        for (int i = 0; i < pointNum; i++) {
            for (DirectedEdge directedEdge : adj[i]) {
                queue.add(directedEdge);
            }
        }
        return queue;
    }
}
