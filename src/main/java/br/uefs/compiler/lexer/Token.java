package br.uefs.compiler.lexer;

public class Token {

    private TokenClass tclass;
    private String value;

    public Token(TokenClass tclass, String value) {
        this.tclass = tclass;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("<%s, %s>",
                this.tclass.name(),
                this.value);
    }
}
