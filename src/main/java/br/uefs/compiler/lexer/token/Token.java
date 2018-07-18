package br.uefs.compiler.lexer.token;

import br.uefs.compiler.parser.Symbol;

/**
 * Representation of Token. Every token has a class, a lexeme and
 * line number associated with it.
 */
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

    public Symbol toSymbol() {
        if (tclass.getName().equals("PALAVRARESERVADA")
                || tclass.getName().equals("DELIMITADOR")
                || tclass.getName().equals("OPERADORLOGICO")
                || tclass.getName().equals("OPERADORARITMETICO")
                || tclass.getName().equals("OPERADORRELACIONAL")) {
            return new Symbol(String.format("'%s'", lexeme));
        } else if (tclass.getName().equals("END"))
            return new Symbol(lexeme);
        else return new Symbol(tclass.getName());
    }

    @Override
    public String toString() {
        return String.format("<%s, %s, %d>",
                this.tclass.getName(),
                this.lexeme
                        .replace("\n", "\\n")
                        .replace("\r", "\\r"),
                this.line);
    }
}
