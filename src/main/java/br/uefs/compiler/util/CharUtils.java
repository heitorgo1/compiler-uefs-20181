package br.uefs.compiler.util;

import java.util.function.Function;

public class CharUtils {

    public static Function<Character, Boolean> digitFunction() {
        return c -> isDigit(c);
    }

    public static Function<Character, Boolean> letterFunction() {
        return c -> isLetter(c);
    }

    public static Function<Character, Boolean> symbolFunction() {
        return c -> isSymbol(c);
    }

    public static Function<Character, Boolean> periodFunction() {
        return c -> c == '.';
    }

    public static Function<Character, Boolean> doubleQuotesFunction() {
        return c -> c == '"';
    }

    public static Function<Character, Boolean> identity(Character expected) {
        return c -> c == expected;
    }

    private static Function<Character, Boolean> whitespaceFunction() {
        return c -> isWhitespace(c);
    }

    private static Function<Character, Boolean> exclamationFunction() {
        return c -> c == '?';
    }

    private static Function<Character, Boolean> pipeFunction() {
        return c -> c == '|';
    }

    private static Function<Character, Boolean> asteriscFunction() {
        return c -> c == '*';
    }

    private static Function<Character, Boolean> openParenFunction() {
        return c -> c == '(';
    }

    private static Function<Character, Boolean> closeParenFunction() {
        return c -> c == ')';
    }

    private static Function<Character, Boolean> backslashFunction() {
        return c -> c == '\\';
    }

    private static Function<Character,Boolean> newlineFunction() {
        return c -> c == '\n';
    }

    public static Function<Character, Boolean> getSpecialCharacterFunction(String s) {
        switch (s) {
            case "\\d":
                return digitFunction();
            case "\\l":
                return letterFunction();
            case "\\y":
                return symbolFunction();
            case "\\.":
                return periodFunction();
            case "\"":
                return doubleQuotesFunction();
            case "\\s":
                return whitespaceFunction();
            case "\\?":
                return exclamationFunction();
            case "\\|":
                return pipeFunction();
            case "\\*":
                return asteriscFunction();
            case "\\(":
                return openParenFunction();
            case "\\)":
                return closeParenFunction();
            case "\\\\":
                return backslashFunction();
            case "\\n":
                return newlineFunction();
        }
        return identity(s.charAt(0));
    }

    public static boolean isEscaped(String s) {
        if (s.length() < 2) return false;
        return s.charAt(0) == '\\' && s.charAt(1) != '"';
    }

    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public static boolean isLetterOrDigit(char c) {
        return (isDigit(c) || isLetter(c));
    }

    public static boolean isSymbol(char c) {
        int v = (int) c;

        return (v != 34) && (v >= 32 && v <= 126);
    }

    public static boolean isWhitespace(char c) {
        int v = (int) c;

        return v == 9 || v == 10 || v == 13 || v == 32;
    }
}
