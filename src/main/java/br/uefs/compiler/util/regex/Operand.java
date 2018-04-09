package br.uefs.compiler.util.regex;

public class Operand implements RegexElement {

    public Character value;

    public Operand(Character value) {
        this.value = value;
    }

    @Override
    public Character getValue() {
        return this.value;
    }

    public String toString() {
        if (Operator.check(value)) return "\\" + value;
        else return Character.toString(value);
    }
}
