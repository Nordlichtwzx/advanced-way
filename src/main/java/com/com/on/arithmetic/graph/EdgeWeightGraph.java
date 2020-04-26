package com.com.on.arithmetic.graph;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 加权无向图
 */
public class EdgeWeightGraph {

    private int pointNum;
    private int edgeNum;
    private Queue<Edge>[] edgeQueue;

    public EdgeWeightGraph(int num) {
        this.pointNum = num;
        this.edgeNum = num;
        this.edgeQueue = new Queue[num];
        for (int i = 0; i < edgeQueue.length; i++) {
            edgeQueue[i] = new ArrayDeque<>();
        }
    }

    public int getPointNum() {
        return pointNum;
    }

    public int getEdgeNum() {
        return edgeNum;
    }

    public void addEdge(Edge edge) {
        edgeQueue[edge.either()].add(edge);
        edgeQueue[edge.other(edge.either())].add(edge);
        edgeNum++;
    }

    /**
     * 获取点关联的所有边
     *
     * @param v
     * @return
     */
    public Queue<Edge> getEdge(int v) {
        return edgeQueue[v];
    }

    /**
     * 获取整个图中的所有边
     *
     * @return
     */
    public Queue<Edge> getAllEdges() {
        Queue queue = new ArrayDeque<>();
        for (int i = 0; i < pointNum; i++) {
            for (Edge edge : edgeQueue[i]) {
                // 因为是无向图，如果直接添加的话会导致出现重复的边，
                // 所以在添加的时候判断，如果领接表中的点大于当前点，就添加
                if (i < edge.other(i)) {
                    queue.add(edge);
                }
            }
        }
        return queue;
    }
}
