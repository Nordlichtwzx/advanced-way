package com.com.on.arithmetic.graph;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author Nordlicht
 * 有向图
 */
public class DiGraph {
    //定点数目
    private int pointNum;
    //边的数目
    private int sideNum;
    //领接表
    private Queue<Integer>[] adj;

    public DiGraph(int num) {
        this.pointNum = num;
        this.sideNum = 0;
        adj = new Queue[num];
        for (int i = 0; i < num; i++) {
            adj[i] = new ArrayDeque<>();
        }
    }


    /**
     * 获取点的数量
     *
     * @return
     */
    public int getPointNum() {
        return pointNum;
    }

    /**
     * 获取边的数量
     *
     * @return
     */
    public int getSideNum() {
        return sideNum;
    }


    /**
     * 添加由v指向w的边
     *
     * @param v
     * @param w
     */
    public void addEdge(int v, int w) {
        adj[v].add(w);
        this.sideNum++;
    }

    /**
     * 获取v点指向的所有点
     *
     * @param v
     * @return
     */
    public Queue<Integer> getAdj(int v) {
        return adj[v];
    }


    /**
     * 返回该图的反向图
     *
     * @return
     */
    public DiGraph reverse() {
        DiGraph graph = new DiGraph(pointNum);
        for (int i = 0; i < pointNum; i++) {
            for (Integer integer : adj[i]) {
                graph.addEdge(integer, i);
            }
        }
        return graph;
    }
}
