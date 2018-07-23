package br.uefs.compiler.parser;

import java.util.*;

/**
 * A grammar symbol and it's data structures.
 * Can be non terminal (between < and >) or a terminal.
 */
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

    public static class Array extends ArrayList<Symbol> {

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

        public Symbol.Array getSymbolsWithoutActions() {
            Symbol.Array arr = new Symbol.Array();

            for (Symbol sy : this) {
                if (!sy.isAction()) arr.add(sy);
            }
            return arr;
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

    public static class Properties extends Hashtable<String, Object> {

        public Properties() {
            super();
        }
    }

    private String name;
    private Properties properties;

    public Symbol(String name) {
        this.name = name;
        properties = new Properties();
    }

    public void setProperty(String property, Object value) {
        properties.put(property, value);
    }

    public Object getProperty(String property) {
        return properties.get(property);
    }

    public Symbol cloneWithoutProperties() {
        return new Symbol(name);
    }

    public void copyProperties(Properties properties) {
        this.properties.putAll(properties);
    }

    public Properties getProperties() {
        return properties;
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
        return !isNonTerminal() && !isAction();
    }

    public boolean isAction() {
        return name.startsWith("{") && name.endsWith("}");
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
        return String.format("%s -> %s", name, properties);
    }
}
