package com.com.on.arithmetic.graph;

import com.company.UF.UF_Tree_Weight;
import com.company.priority.MinPriorityQueue;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 核心想法：将所有的边都放在一个最小优先队列中，每次取出一条边，即最小权重的边，
 * 然后通过并查集判断这条边所关联的两个点是否在同一个树中，如果在同一个树中则不能连接，
 * 如果不在同一个树中，则连接，最后生成一个最小生成树
 * 用到的数据结构：队列，最小优先队列，并查集
 *
 * @author Nordlicht
 */
public class KrustalMst {
    //保存最小生成树的所有边
    private Queue<Edge> mst;
    //索引代表顶点，使用uf.connect(v,w)可以判断顶点v和顶点w是否在同一颗树中，使用uf.union(v,w)可以把顶点v所在的树和顶点w所在的树合并
    private UF_Tree_Weight uf;
    //存储图中所有的边，使用最小优先队列，对边按照权重进行排序
    private MinPriorityQueue<Edge> pq;

    //根据一副加权无向图，创建最小生成树计算对象
    public KrustalMst(EdgeWeightGraph G) {
        this.mst = new ArrayDeque<>(G.getPointNum());
        this.uf = new UF_Tree_Weight(G.getPointNum());
        //因为优先队列使用堆实现的，第0位为空，所以需要加1
        this.pq = new MinPriorityQueue<>(G.getEdgeNum() + 1);
        for (Edge edge : G.getAllEdges()) {
            pq.insert(edge);
        }
        while (!pq.isEmpty() && mst.size() < G.getPointNum()) {
            Edge edge = pq.delMin();
            //如果不在同一个树中，则union
            if (!uf.connected(edge.either(), edge.other(edge.either()))) {
                uf.union(edge.either(), edge.other(edge.either()));
            }
            mst.add(edge);
        }
    }

    //获取最小生成树的所有边
    public Queue<Edge> edges() {
        return mst;
    }

    public static void main(String[] args) throws Exception {


        EdgeWeightGraph G = new EdgeWeightGraph(8);
        G.addEdge(new Edge(4, 5, 0.35));
        G.addEdge(new Edge(4, 7, 0.37));
        G.addEdge(new Edge(5, 7, 0.28));
        G.addEdge(new Edge(0, 7, 0.16));
        G.addEdge(new Edge(1, 5, 0.32));
        G.addEdge(new Edge(0, 4, 0.38));
        G.addEdge(new Edge(2, 3, 0.17));
        G.addEdge(new Edge(1, 7, 0.19));
        G.addEdge(new Edge(0, 2, 0.26));
        G.addEdge(new Edge(1, 2, 0.36));
        G.addEdge(new Edge(1, 3, 0.29));
        G.addEdge(new Edge(2, 7, 0.34));
        G.addEdge(new Edge(6, 2, 0.40));
        G.addEdge(new Edge(3, 6, 0.52));
        G.addEdge(new Edge(6, 0, 0.58));
        G.addEdge(new Edge(6, 4, 0.93));

        //创建一个PrimMST对象，计算加权无向图中的最小生成树
        KrustalMst krustalMst = new KrustalMst(G);


        //获取最小生成树中的所有边
        Queue<Edge> edges = krustalMst.edges();

        //遍历打印所有的边
        for (Edge e : edges) {
            int v = e.either();
            int w = e.other(v);
            double weight = e.getWeight();
            System.out.println(v + "-" + w + " :: " + weight);

        }


    }
}
