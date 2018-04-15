package br.uefs.compiler.util.regex;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Value in a Regular Expression that is an operator, as defined in
 * the OPERATORS set.
 *
 * Operators:
 *  * X* - Star - X, zero or more times
 *  * X|Y - Union - selects either X or Y
 *  * X.Y - Concat - X followed by Y
 *  * X?  - Question - X, once or not at all
 *  * (exp) - Parenthesized expression - are evaluated before the others, deepest first
 */
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

    /**
     * Check if an operator has precedence over some other operator.
     *
     * Parenthesis have no precedence over other operators.
     * Unary operators (* and ?) have precendence over Concat and Union.
     * If the current operator is equal to the other, it has precedence.
     * Concat operator has precence over Union.
     *
     * @param other The operator that is being compared with this one.
     * @return true if this operator has precedence over other, false otherwise
     */
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