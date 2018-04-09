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

    public TokenClass getTokenClass() {
        return tclass;
    }

    public String getLexeme() {
        return lexeme;
    }

    public long getLine() {
        return line;
    }

    @Override
    public String toString() {
        return String.format("<%s, %s, %d>",
                this.tclass.getName(),
                this.lexeme
                        .replace("\n","\\n")
                        .replace("\r", "\\r"),
                this.line);
    }
}
