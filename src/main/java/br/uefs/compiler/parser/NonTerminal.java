package br.uefs.compiler.parser;

import java.util.List;
import java.util.Objects;

public class NonTerminal implements GrammarSymbol {

    private String value;
    private List<Production> productions;

    public NonTerminal(String value, List<Production> productions) {
        this.value = value;
        this.productions = productions;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NonTerminal that = (NonTerminal) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.format("<%s>", value);
    }
}
