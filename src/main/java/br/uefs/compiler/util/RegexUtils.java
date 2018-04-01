package br.uefs.compiler.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RegexUtils {

    public static final Set<Character> OPERATORS = new HashSet<>(Arrays.asList(
            '*',
            '|',
            '.',
            '?',
            '(',
            ')'
    ));

    public static boolean isOperator(char c) {
        return OPERATORS.contains(c);
    }

    public static boolean isOperator(String s) {
        if (s.length() > 1 || s.length() == 0) return false;
        return OPERATORS.contains(s.charAt(0));
    }

    public static boolean isUnaryOperator(char c) {
        return c == '*' || c == '?';
    }

    public static boolean hasPrecedence(char targetOperator, char otherOperator) {
        if (targetOperator == '(' || targetOperator == ')' || otherOperator == '(' || otherOperator == ')')
            return false;

        if (targetOperator == otherOperator) return true;

        if (isUnaryOperator(targetOperator) && isUnaryOperator(otherOperator))
            return true;
        if (isUnaryOperator(targetOperator) && !isUnaryOperator(otherOperator))
            return true;
        if (!isUnaryOperator(targetOperator) && isUnaryOperator(otherOperator))
            return false;
        if (!isUnaryOperator(targetOperator) && !isUnaryOperator(otherOperator))
            return true;
        return true;
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
    public static String expandConcat(String s) {
        if (s.length() == 1) return s;
        StringBuilder sb = new StringBuilder();

        boolean escapedCharacter = false;
        for (int i = 0; i < s.length() - 1; i++) {
            char c = s.charAt(i);
            char next = s.charAt(i + 1);

            if (c == '\\') {
                if (escapedCharacter) {
                    if (!isOperator(next) || next == '(') {
                        sb.append(c);
                        sb.append('.');
                    }
                    escapedCharacter = false;
                }
                else {
                    sb.append(c);
                    escapedCharacter = true;
                }
            } else if ((escapedCharacter || !isOperator(c)) && !isOperator(next)) {
                sb.append(c);
                sb.append('.');
                escapedCharacter = false;
            } else if ((escapedCharacter || !isOperator(c)) && next == '(') {
                sb.append(c);
                sb.append('.');
                escapedCharacter = false;
            } else if ((c == ')' || c == '*' || c == '?') && !isOperator(next)) {
                sb.append(c);
                sb.append('.');
                escapedCharacter = false;
            } else if ((c == '*' || c == '?' || c == ')') && next == '(') {
                sb.append(c);
                sb.append('.');
                escapedCharacter = false;
            } else {
                sb.append(c);
                escapedCharacter = false;
            }
        }
        sb.append(s.charAt(s.length() - 1));
        return sb.toString();
    }
}
