package br.uefs.compiler.util.parser;

public class Terminal implements GrammarSymbol{

    private String value;

    public Terminal(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "'"+value+"'";
    }
}
