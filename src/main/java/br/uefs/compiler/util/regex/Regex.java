package br.uefs.compiler.util.regex;

import java.util.*;

public class Regex {


    private final String expression;

    public Regex(String expression) {
        this.expression = expression;
    }

    public String expandSpecialCharacters(String expression) throws Exception {
        if (expression.length() == 1) return expression;

        StringBuilder sb = new StringBuilder();

        sb.append(expression.charAt(0));

        for (int i = 1; i < expression.length(); i++) {
            char previous = expression.charAt(i - 1);
            char c = expression.charAt(i);

            if (previous == '\\' && SpecialCharacter.check(c)) {
                sb.deleteCharAt(sb.length()-1);
                sb.append(SpecialCharacter.getExpression(c));
            } else
                sb.append(c);

        }
        return sb.toString();
    }

    /*
    Add concat operator ('.') in the following situations:
     a . b
     a . (
     ) . a
     * . a
     * . (
     ) . (
     ? . a
     ? . (
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
            } else if ((escapedCharacter || !Operator.check(c)) && (!Operator.check(next) || next == '(' ) ) {
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
