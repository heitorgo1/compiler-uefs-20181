package br.uefs.compiler.lexer;

public class Token {

    private TokenClass tclass;
    private String lexeme;
    private long line;

    public Token(TokenClass tclass, String lexeme, long line) {
        this.tclass = tclass;
        this.lexeme = lexeme;
        this.line = line;
    }

    @Override
    public String toString() {
        return String.format("<%s, %s, %d>",
                this.tclass.getName(),
                this.lexeme,
                this.line);
    }
}
