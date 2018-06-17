package br.uefs.compiler.parser;

import java.util.List;

public class Production {

    private String name;
    private List<List<String>> productions;

    public Production(String name, List<List<String>> productions) {
        this.name = name;
        this.productions = productions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<List<String>> getProductions() {
        return productions;
    }

    public void setProductions(List<List<String>> productions) {
        this.productions = productions;
    }
}
