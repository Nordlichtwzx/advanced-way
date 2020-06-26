package com.on.arithmetic.UF;

public class changtongdaolu {
    public static void main(String[] args) {
        UF_Tree_Weight weight = new UF_Tree_Weight(20);
        weight.union(0, 1);
        weight.union(6, 9);
        weight.union(3, 8);
        weight.union(5, 11);
        weight.union(2, 12);
        weight.union(6, 10);
        weight.union(4, 8);
        System.out.println(weight.count());
    }
}
