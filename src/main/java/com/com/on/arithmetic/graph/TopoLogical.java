package com.com.on.arithmetic.graph;

import java.util.Stack;

public class TopoLogical {

    private Stack order;

    public TopoLogical(DiGraph diGraph) {
        DirectedCircle directedCircle = new DirectedCircle(diGraph);
        DepthFirstOrder depthFirstOrder = new DepthFirstOrder(diGraph);
        if (!directedCircle.hasCircle()) {
            this.order = depthFirstOrder.getReversePoint();
        }
    }

    public boolean hasCycle() {
        //如果order==null说明当前图有环
        return order == null;
    }

    public Stack getOrder() {
        return order;
    }

    public static void main(String[] args) {

        //准备有向图
        DiGraph digraph = new DiGraph(6);
        digraph.addEdge(0, 2);
        digraph.addEdge(0, 3);
        digraph.addEdge(2, 4);
        digraph.addEdge(3, 4);
        digraph.addEdge(4, 5);
        digraph.addEdge(1, 3);

        //通过TopoLogical对象堆有向图中的顶点进行排序
        TopoLogical topoLogical = new TopoLogical(digraph);

        //获取顶点的线性序列进行打印

        Stack<Integer> order = topoLogical.getOrder();
        StringBuilder sb = new StringBuilder();
        while (!order.isEmpty()) {
            sb.append(order.pop());
        }
        String str = sb.toString();
        System.out.println(str);
    }
}
