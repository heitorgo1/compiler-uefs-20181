package br.uefs.compiler.lexer;

public class Token {

    private TokenClass tclass;
    private String lexeme;

    public Token(TokenClass tclass, String lexeme) {
        this.tclass = tclass;
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return String.format("<%s, '%s'>",
                this.tclass.getName(),
                this.lexeme);
    }
}
