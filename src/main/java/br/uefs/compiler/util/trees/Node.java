package br.uefs.compiler.util.trees;

public class Node<T> {

    private Node<T> left;
    private Node<T> right;
    private Node<T> parent;

    private T value;

    public Node(T value) {
        this.value = value;
        left = null;
        right = null;
        parent = null;
    }

    public Node<T> getLeft() {
        return left;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public Node<T> getRight() {
        return right;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }

    public Node<T> getParent() {
        return parent;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Node{%s, parent=%s, left=%s, right=%s}",
                value,
                parent.value,
                left.value,
                right.value);
    }
}
