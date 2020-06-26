package com.on.arithmetic.graph;

/**
 * 加权无向图的边
 *
 * @author Nordlicht
 */
public class Edge implements Comparable<Edge> {
    private final int pointOne;
    private final int pointTwo;
    private final double weight;

    public Edge(int pointOne, int pointTwo, double weight) {
        this.pointOne = pointOne;
        this.pointTwo = pointTwo;
        this.weight = weight;
    }

    public int either() {
        return pointOne;
    }

    public int other(int point) {
        if (point == pointOne) {
            return pointTwo;
        } else {
            return pointOne;
        }
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Edge that) {
        int cmp;
        if (this.getWeight() > that.getWeight()) {
            //如果当前边的权重大于that边
            cmp = 1;
        } else if (this.getWeight() < that.getWeight()) {
            //如果当前边的权重小于that边
            cmp = -1;
        } else {
            //如果当前边的权重等于that边
            cmp = 0;
        }
        return cmp;
    }
}
