package br.uefs.compiler.parser;

import java.util.ArrayList;
import java.util.Arrays;

public class SymbolArray extends ArrayList<Symbol> {

    public SymbolArray(Symbol... symbols) {
        super(Arrays.asList(symbols));
    }

    public SymbolArray(String... symbolNames) {
        super();

        for (String symbolName : symbolNames) {
            this.add(new Symbol(symbolName));
        }
    }

    public boolean hasSingleValue() {
        return this.size() == 1;
    }

    public Symbol getSingleValue() {
        assert hasSingleValue();

        return this.get(0);
    }
}
