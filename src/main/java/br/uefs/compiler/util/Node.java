package br.uefs.compiler.util;

public class Node {

    private Node parent;
    private Node right;
    private Node left;

    private String value;

    public Node(String value) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    public Node setParent(Node parent) {
        this.parent = parent;
        return this;
    }

    public Node setRight(Node right) {
        this.right = right;
        return this;
    }

    public Node setLeft(Node left) {
        this.left = left;
        return this;
    }

    public Node setValue(String value) {
        this.value = value;
        return this;
    }

    public Node getParent() {
        return parent;
    }

    public Node getRight() {
        return right;
    }

    public Node getLeft() {
        return left;
    }

    public String getValue() {
        return value;
    }
}
