package br.uefs.compiler.lexer.token;

import java.util.Arrays;
import java.util.List;

/**
 * Class responsible for handling token creation mistakes that the automata
 * wasn't able to deal with for some reason.
 */
public class TokenMistakeHandler {

    /**
     * Negative numbers should only be considered a NUMBER token if
     * the previous token is (, =, ==, <, >, !=, <=, >= +, -, / or *
     * Otherwise they should be broken down into an OPERATOR and a NUMBER.
     *
     * @param previous token that came before the current token
     * @param current current token
     * @return is number mistake detected
     */
    private static boolean isNumberMistake(Token previous, Token current) {
        return current.getTokenClass().getName().equals("NUMERO") &&
                current.getLexeme().contains("-") &&
                !previous.getLexeme().equals("(") &&
                !previous.getLexeme().equals("=") &&
                !previous.getLexeme().equals("==") &&
                !previous.getLexeme().equals("<") &&
                !previous.getLexeme().equals(">") &&
                !previous.getLexeme().equals("<=") &&
                !previous.getLexeme().equals(">=") &&
                !previous.getLexeme().equals("!=") &&
                !previous.getLexeme().equals("+") &&
                !previous.getLexeme().equals("-") &&
                !previous.getLexeme().equals("/") &&
                !previous.getLexeme().equals("*");
    }

    public static boolean isMistake(Token previous, Token current) {
        return isNumberMistake(previous, current);
    }

    public static List<Token> solveMistake(Token previous, Token current) throws Exception {

        if (isNumberMistake(previous, current)) {
            // break current NUMBER token into - OPERATOR and NUMBER
            Token minusToken = new Token(TokenClass.getClassByName("OPERADORARITMETICO"), "-", current.getLine());
            String number = current.getLexeme().replace("-", "");
            Token numToken = new Token(current.getTokenClass(), number, current.getLine());
            return Arrays.asList(minusToken, numToken);
        }

        throw new Exception("No mistake to be solved.");
    }
}
