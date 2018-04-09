package br.uefs.compiler.util.regex;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Operator implements RegexElement {

    public static Set<Character> OPERATORS = new HashSet<>(Arrays.asList(
            '*', '?', '.', '|', '(', ')'
    ));

    private Character op;

    public Operator(Character op) throws Exception {
        if (!OPERATORS.contains(op))
            throw new Exception(String.format("'%s' is not a valid Regex operator.", op));
        this.op = op;
    }

    public static boolean check(Character input) {
        return OPERATORS.contains(input);
    }

    public boolean isUnary() {
        return op == '*' || op == '?';
    }

    public boolean hasPrecendenceOver(Operator other) {
        if (op == '(' || op == ')' || other.op == '(' || other.op == ')') return false;
        if (isUnary()) return true;
        if (op == other.op) return true;

        if (op == '.' && other.op == '|') return true;
        return false;
    }

    @Override
    public Character getValue() {
        return this.op;
    }

    public String toString() {
        return Character.toString(op);
    }
}