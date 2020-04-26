package com.com.on.arithmetic.graph;

import java.util.Stack;

public class DirectedCircle {
    //用来标记是否已被搜索过
    private boolean[] marked;
    //返回当前图时否有环
    private boolean hasCircle;
    //将通过深度优先遍历过的点放入到栈中，每一次放入的时候进行判断，是否已经在栈中，如果在，说明有环
    private Stack stack;

    public DirectedCircle(DiGraph G) {
        this.marked = new boolean[G.getPointNum()];
        this.hasCircle = false;
        this.stack = new Stack();

        //因为可能存在非联通图，所以需要对每个点都做深度优先遍历，判断是否有环
        for (int i = 0; i < G.getPointNum(); i++) {
            dfs(G, i);
        }
    }

    //深度优先遍历。判断是否有环
    public void dfs(DiGraph graph, int point) {
        marked[point] = true;
        stack.push(point);
        for (Integer integer : graph.getAdj(point)) {
            //如果当前节点的领接表中的元素没有被搜索过，则继续递归调用dfs
            if (!marked[integer]) {
                dfs(graph, integer);
            }
            if (stack.contains(integer)) {
                hasCircle = true;
                return;
            }
        }
        stack.pop();
    }

    //判断当前有向图是否有环
    public boolean hasCircle() {
        return hasCircle;
    }
}
