package br.uefs.compiler.lexer;

public class TokenClass implements Comparable {
    public static final TokenClass INVALID_CLASS = new TokenClass(Integer.MAX_VALUE, "", "CLASSEINVALIDA");

    private int id;
    private String regex;
    private String name;

    public TokenClass(int id, String regex, String name) {
        this.id = id;
        this.regex = regex;
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public String getRegex() {
        return regex;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Object o) {
        TokenClass other = (TokenClass) o;
        if (other.id == this.id) return 0;
        return this.id - other.id;
    }

    @Override
    public String toString() {
        return String.format("TokenClass{%d, %s, %s}",id, regex,name);
    }
}
