package br.uefs.compiler.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * A grammar symbol and it's data structures.
 * Can be non terminal (between < and >) or a terminal.
 * */
public class Symbol {

    public static Symbol EMPTY_STRING = new Symbol("");
    public static Symbol INPUT_END = new Symbol("$");

    static class Set extends HashSet<Symbol> {

        public boolean hasOnlyTerminals() {
            for (Symbol sy : this) {
                if (!sy.isTerminal()) return false;
            }
            return true;
        }

        public static Set fromSingleSymbol(Symbol symbol) {
            Set set = new Set();
            set.add(symbol);
            return set;
        }

        public boolean containsEmptyString() {
            return this.contains(EMPTY_STRING);
        }

        public void removeEmptyString() {
            this.remove(EMPTY_STRING);
        }
    }

    static class Array extends ArrayList<Symbol> {

        public Array() {
            super();
        }

        public Array(List<Symbol> symbols) {
            super(symbols);
        }

        public Array(String... symbolNames) {
            super();

            for (String symbolName : symbolNames) {
                this.add(new Symbol(symbolName));
            }
        }

        public boolean hasSingleSymbol() {
            return this.size() == 1;
        }

        public Symbol getSingleSymbol() {
            assert hasSingleSymbol();

            return this.get(0);
        }

        public static Array fromSingleSymbol(Symbol symbol) {
            Array arr = new Array();
            arr.add(symbol);
            return arr;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Symbol s : this) sb.append(s + " ");
            return sb.toString();
        }
    }

    private String name;

    public Symbol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isEmptyString() {
        return name.equals("");
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
