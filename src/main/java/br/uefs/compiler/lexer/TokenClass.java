package br.uefs.compiler.lexer;

import br.uefs.compiler.util.automata.StateTag;

public class TokenClass implements StateTag {

    private int id;
    private String regex;
    private String name;

    public TokenClass(int id, String regex, String name) {
        this.id = id;
        this.regex = regex;
        this.name = name;
    }

    public String getRegex() {
        return regex;
    }

    public String getName() {
        return name;
    }

    @Override
    public int get() {
        return id;
    }

    @Override
    public int compareTo(Object o) {
        TokenClass other = (TokenClass)o;
        if (other.get() == this.get()) return 0;
        return this.get() - other.get();
    }
}
