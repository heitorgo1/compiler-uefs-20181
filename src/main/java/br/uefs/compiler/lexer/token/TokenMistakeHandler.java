package br.uefs.compiler.lexer.token;

import java.util.Arrays;
import java.util.List;

public class TokenMistakeHandler {

    /*
    * Negative numbers should only be considered a NUMBER token if
    * the previous token is (, =, <=, >= +, -, / or *
    * */
    private static boolean isNumberMistake(Token previous, Token current) {
        return current.getTokenClass().getName().equals("NUMERO") &&
                current.getLexeme().contains("-") &&
                !previous.getLexeme().equals("(") &&
                !previous.getLexeme().equals("=") &&
                !previous.getLexeme().equals("<=") &&
                !previous.getLexeme().equals(">=") &&
                !previous.getLexeme().equals("+") &&
                !previous.getLexeme().equals("-") &&
                !previous.getLexeme().equals("/") &&
                !previous.getLexeme().equals("*");
    }

    public static boolean isMistake (Token previous, Token current) {
        return isNumberMistake(previous, current);
    }

    public static List<Token> solveMistake(Token previous, Token current) throws Exception {

        if (isNumberMistake(previous, current)) {
            Token minusToken = new Token(TokenClass.getClassByName("OPERADORARITMETICO"), "-", current.getLine());
            String number = current.getLexeme().replace("-", "");
            Token numToken = new Token(current.getTokenClass(), number, current.getLine());
            return Arrays.asList(minusToken, numToken);
        }

        throw new Exception("No mistake to be solved.");
    }
}
