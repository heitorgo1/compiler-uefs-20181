package br.uefs.compiler.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class RegexTree implements Iterable<Node> {

    private Node root;

    public RegexTree() {
        this.root = null;
    }

    public RegexTree(Node root) {
        this.root = root;
    }

    public static RegexTree fromRegex(String rawRegex) throws Exception {
        String regex = RegexUtils.expandConcat(rawRegex);

        System.out.format("Building Regex Tree for %s\n",rawRegex);
        System.out.format("Formatted Regex: %s\n",regex);

        Stack<Character> operators = new Stack<>();
        Stack<Node> nodes = new Stack<>();

        boolean escapedLetter = false;

        for (int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i);

            if (c == '\\') {
                escapedLetter = true;
                continue;
            }

            if (escapedLetter) {
                nodes.push(new Node("\\" + c));
                escapedLetter = false;
            } else if (RegexUtils.isOperator(c)) {
                if (c == ')') {
                    while (operators.peek() != '(') {
                        applyOperation(operators, nodes);
                    }
                    operators.pop();
                } else {
                    while (!operators.isEmpty() && RegexUtils.hasPrecedence(operators.peek(), c)) {
                        applyOperation(operators, nodes);
                    }
                    operators.push(c);
                }
            } else {
                nodes.push(new Node(Character.toString(c)));
            }
        }

        while (!operators.isEmpty()) {
            applyOperation(operators, nodes);
        }

        return new RegexTree(nodes.pop());
    }

    private static void applyOperation(Stack<Character> operators, Stack<Node> nodes) throws Exception {

        if (!operators.isEmpty()) {
            char op = operators.pop();

            Node root = new Node(Character.toString(op));

            if (RegexUtils.isUnaryOperator(op)) {
                Node left = nodes.pop();
                root.setLeft(left);

                left.setParent(root);

                nodes.push(root);
            } else if (RegexUtils.isOperator(op)) {
                Node right = nodes.pop();
                Node left = nodes.pop();

                root.setLeft(left);
                root.setRight(right);

                left.setParent(root);
                right.setParent(root);

                nodes.push(root);
            } else {
                throw new Exception(String.format("unknown operator '%s'", op));
            }
        }
    }

    public void printInorder() {
        printInorder(root);
    }

    public void printInorder(Node node) {
        if (node == null) return;

        printInorder(node.getLeft());

        System.out.print(node.getValue() + " -> ");
        if (node.getParent() != null) System.out.println(node.getParent().getValue());
        else System.out.println("null");

        printInorder(node.getRight());
    }

    private List<Node> populateInorder() {
        List<Node> nodes = new ArrayList<>();

        populateInorder(root, nodes);
        return nodes;
    }

    private void populateInorder(Node root, List<Node> nodes) {
        if (root == null) return;

        populateInorder(root.getLeft(), nodes);

        nodes.add(root);

        populateInorder(root.getRight(), nodes);
    }

    private List<Node> populatePostorder() {
        List<Node> nodes = new ArrayList<>();

        populatePostorder(root, nodes);
        return nodes;
    }

    private void populatePostorder(Node root, List<Node> nodes) {
        if (root == null) return;

        populatePostorder(root.getLeft(), nodes);

        populatePostorder(root.getRight(), nodes);

        nodes.add(root);
    }

    @Override
    public Iterator<Node> iterator() {
        return populateInorder().iterator();
    }

    public Iterator<Node> iteratorPostorder() {
        return populatePostorder().iterator();
    }
}
