package br.uefs.compiler;

import br.uefs.compiler.lexer.Lexer;
import br.uefs.compiler.lexer.Token;

import java.io.StringReader;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        Lexer l = new Lexer(new StringReader("i = a++ + - 2;\nint j = -3"));
        List<Token> tokens = l.readAllTokens();
        List<Token> errors = l.getErrors();

        System.out.println(tokens);
        System.out.println(errors);
    }
}
