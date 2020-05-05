package com.com.on.arithmetic.graph;

import com.com.on.arithmetic.priority.IndexMinPriorityQueue;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * prim算法
 * 首先，默认将0节点放入到最小生成树中，此时最小生成树中只有一个点，然后开始找与最小生成树的横切边，
 * 找到最小的横切边，然后放入到edgeTo中与最小横切边关联的，那个的最小生成树以外的点，为索引的位置
 * 同理，将这条边的weight值放入到distTo中
 * 并且更新索引最小优先队列
 * 用到的数据结构：索引最下优先队列，
 *
 * @author Nordlicht
 */
public class PrimMST {
    //索引代表顶点，值表示当前顶点和最小生成树之间的最短边
    private final Edge[] edgeTo;
    //索引代表顶点，值表示当前顶点和最小生成树之间的最短边的权重
    private final double[] distTo;
    //索引代表顶点，如果当前顶点已经在树中，则值为true，否则为false
    private final boolean[] marked;
    //存放树中顶点与非树中顶点之间的有效横切边
    private final IndexMinPriorityQueue<Double> indexMinPriorityQueue;

    //根据一副加权无向图，创建最小生成树计算对象
    public PrimMST(EdgeWeightGraph G) {
        this.edgeTo = new Edge[G.getPointNum()];
        this.distTo = new double[G.getPointNum()];
        for (int i = 0; i < distTo.length; i++) {
            distTo[i] = Double.POSITIVE_INFINITY;
        }
        this.marked = new boolean[G.getPointNum()];
        indexMinPriorityQueue = new IndexMinPriorityQueue<>(G.getPointNum());
        distTo[0] = 0.0;
//        edgeTo[0] = new Edge(0,0,0.0);
        indexMinPriorityQueue.insert(0, 0.0);
        while (!indexMinPriorityQueue.isEmpty()) {
            visit(G, indexMinPriorityQueue.delMin());
        }
    }


    //将顶点v添加到最小生成树中，并且更新数据
    private void visit(EdgeWeightGraph G, int v) {
        marked[v] = true;
        for (Edge edge : G.getEdge(v)) {
            int w = edge.other(v);
            if (marked[w]) {
                continue;
            }
            if (edge.getWeight() < distTo[w]) {
                edgeTo[w] = edge;
                distTo[edge.other(w)] = edge.getWeight();
                if (indexMinPriorityQueue.contains(w) != null) {
                    double temp = indexMinPriorityQueue.contains(w);
                    if (edge.getWeight() < temp) {
                        indexMinPriorityQueue.changeItem(w, edge.getWeight());
                    }
                } else {
                    indexMinPriorityQueue.insert(w, edge.getWeight());
                }

            }

        }
    }

    //获取最小生成树的所有边
    public Queue<Edge> edges() {
        //创建队列对象
        Queue<Edge> allEdges = new ArrayDeque<>();
        for (int i = 0; i < edgeTo.length; i++) {
            if (edgeTo[i] != null) {
                allEdges.add(edgeTo[i]);
            }
        }
        return allEdges;
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
        PrimMST primMST = new PrimMST(G);


        //获取最小生成树中的所有边
        Queue<Edge> edges = primMST.edges();

        //遍历打印所有的边
        for (Edge e : edges) {
            int v = e.either();
            int w = e.other(v);
            double weight = e.getWeight();
            System.out.println(v + "-" + w + " :: " + weight);

        }


    }

}
