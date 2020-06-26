package com.on.arithmetic.tree;

import java.util.Queue;

public class main {

    public static void main(String[] args) {
        BinaryTree<String, String> binaryTree = new BinaryTree<>();
        binaryTree.put("E", "5");
        binaryTree.put("B", "2");
        binaryTree.put("D", "4");
        binaryTree.put("A", "1");
        binaryTree.put("G", "7");
        binaryTree.put("H", "8");
        binaryTree.put("F", "6");
        binaryTree.put("C", "3");

        Queue<String> queue = binaryTree.middleIterator();
        System.out.println(queue);
        System.out.println(binaryTree.maxDeep());


        Heap<String> heap = new Heap<>(10);
        heap.insert("A");
        heap.insert("B");
        heap.insert("C");
        heap.insert("D");
        heap.insert("E");
        heap.insert("F");
        heap.insert("G");
        heap.insert("H");


        String result;
        while ((result = heap.deleteMax()) != null) {
            System.out.println(result);
        }
    }
}
