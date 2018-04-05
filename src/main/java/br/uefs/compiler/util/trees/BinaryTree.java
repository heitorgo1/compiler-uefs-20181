package br.uefs.compiler.util.trees;

import java.util.ArrayList;
import java.util.List;

public class BinaryTree<T> {

    private Node<T> root;

    public BinaryTree() {
        root = null;
    }

    public void setRoot(Node<T> root) {
        this.root = root;
    }

    private Iterable<T> topologicalTraversal(Node<T> node, List<T> list) {
        if (node == null) return null;

        topologicalTraversal(node.getLeft(), list);
        topologicalTraversal(node.getRight(), list);
        list.add(node.getValue());

        return list;
    }

    public Iterable<T> topologicalTraversal() {
        List<T> values = new ArrayList<>();
        return topologicalTraversal(root, values);
    }

    private Iterable<T> inOrderTraversal(Node<T> node, List<T> list) {
        if (node == null) return null;

        inOrderTraversal(node.getLeft(), list);
        list.add(node.getValue());
        inOrderTraversal(node.getRight(), list);

        return list;
    }

    public Iterable<T> inOrderTraversal() {
        List<T> values = new ArrayList<>();
        return topologicalTraversal(root, values);
    }

    private void print(Node<T> node) {
        if (node == null) return;

        print(node.getLeft());
        System.out.println(node);
        print(node.getRight());
    }

    public void print() {
        print(root);
    }
}
