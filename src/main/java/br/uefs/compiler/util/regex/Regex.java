package br.uefs.compiler.util.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Representation of a Regular Expression.
 */
public class Regex {

    /**
     * Regular Expression as String
     */
    private final String expression;

    public Regex(String expression) {
        this.expression = expression;
    }

    /**
     * Get all escaped special characters (see {@link SpecialCharacter} class) in the expression
     * and expand them to their regex correspondents.
     *
     * e.g.:
     *      expression = \d*, return = (0|1|2|3|4|5|6|7|8|9)*
     *
     * @param expression the regex expression that is going to be expanded.
     * @return expanded expression
     * @throws Exception in case of trying to get an expression for a character that is not special
     */
    public String expandSpecialCharacters(String expression) throws Exception {
        if (expression.length() == 1) return expression;

        StringBuilder sb = new StringBuilder();

        sb.append(expression.charAt(0));

        for (int i = 1; i < expression.length(); i++) {
            char previous = expression.charAt(i - 1);
            char c = expression.charAt(i);

            if (previous == '\\' && SpecialCharacter.check(c)) {
                sb.deleteCharAt(sb.length() - 1);
                sb.append(SpecialCharacter.getExpression(c));
            } else
                sb.append(c);

        }
        return sb.toString();
    }

    /**
     * Add concat operator ('.') in the following situations:
     * a . b
     * a . (
     * ) . a
     * . a
     * . (
     * ) . (
     * ? . a
     * ? . (
     *
     * e.g.:
     *      expression = ab|c, return = a.b|c
     *      expression = (\\.abc), return = (\\..a.b.c)
     *
     * @param expression the regex expression that is going to be expanded.
     * @return expanded expression
     */
    public String expandConcat(String expression) {
        if (expression.length() == 1) return expression;
        StringBuilder sb = new StringBuilder();

        boolean escapedCharacter = false;
        for (int i = 0; i < expression.length() - 1; i++) {
            char c = expression.charAt(i);
            char next = expression.charAt(i + 1);

            if (c == '\\') {
                // special case for backslash character
                if (escapedCharacter) {
                    if (!Operator.check(next) || next == '(') {
                        sb.append(c);
                        sb.append('.');
                    }
                    escapedCharacter = false;
                } else {
                    sb.append(c);
                    escapedCharacter = true;
                }
            } else if ((escapedCharacter || !Operator.check(c)) && (!Operator.check(next) || next == '(')) {
                sb.append(c);
                sb.append('.');
                escapedCharacter = false;
            } else if ((c == ')' || c == '*' || c == '?') && (!Operator.check(next) || next == '(')) {
                sb.append(c);
                sb.append('.');
                escapedCharacter = false;
            } else {
                sb.append(c);
                escapedCharacter = false;
            }
        }
        sb.append(expression.charAt(expression.length() - 1));
        return sb.toString();
    }

    /**
     * Converts the expression in infix notation to postfix.
     * <p>
     * e.g.:
     * expression = (a|(b.c)*), return = [a, b, c, ., *, |]
     * expression = a|b, return = [a, b, |]
     *
     * @return List of Operands and Operators (RegexElement) ordered in postfix notation.
     * @throws Exception in case of malformed expression that can't be converted.
     */
    public List<RegexElement> toPostfix() throws Exception {
        final String expandedExpression = expandSpecialCharacters(expandConcat(this.expression));

        Stack<Operator> opstack = new Stack<>();

        List<RegexElement> output = new ArrayList<>();

        boolean escapedCharacter = false;

        for (char c : expandedExpression.toCharArray()) {
            if (c == '\\' && !escapedCharacter) {
                escapedCharacter = true;
                continue;
            }

            if (escapedCharacter) {
                output.add(new Operand(c));
                escapedCharacter = false;
            } else if (Operator.check(c)) {
                if (c == ')') {
                    while (opstack.peek().getValue() != '(') {
                        output.add(opstack.pop());
                    }
                    opstack.pop();
                } else {
                    Operator op = new Operator(c);
                    while (!opstack.isEmpty() && opstack.peek().hasPrecendenceOver(op)) {
                        output.add(opstack.pop());
                    }
                    opstack.push(op);
                }
            } else output.add(new Operand(c));
        }

        while (!opstack.isEmpty()) output.add(opstack.pop());

        return output;
    }
}
