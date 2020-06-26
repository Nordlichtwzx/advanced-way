package com.on.arithmetic.graph;

import java.util.Stack;

public class DepthFirstOrder {

    private final boolean[] marked;

    private final Stack reversePoint;

    public DepthFirstOrder(DiGraph graph) {
        this.marked = new boolean[graph.getPointNum()];
        this.reversePoint = new Stack();
        //遍历所有节点，将节点放入到reversePoint栈中
        for (int i = 0; i < graph.getPointNum(); i++) {
            if (!marked[i]) {
                dfs(graph, i);
            }
        }
    }

    //基于深度优先搜索，创建一个拓扑排序栈
    public void dfs(DiGraph graph, int point) {
        marked[point] = true;
        for (Integer integer : graph.getAdj(point)) {
            if (!marked[integer]) {
                dfs(graph, integer);
            }
        }
        reversePoint.push(point);
    }

    public Stack getReversePoint() {
        return reversePoint;
    }
}
