package br.uefs.compiler.parser;

import java.util.Objects;

public class Symbol {

    private String name;

    public Symbol (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isNonTerminal() {
        return name.startsWith("<") && name.endsWith(">");
    }

    public boolean isTerminal() {
        return !isNonTerminal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol = (Symbol) o;
        return Objects.equals(name, symbol.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
