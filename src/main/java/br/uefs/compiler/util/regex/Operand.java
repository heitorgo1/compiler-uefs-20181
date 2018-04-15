package br.uefs.compiler.util.regex;

/**
 * Value in a Regular Expression that is not an operator.
 */
public class Operand implements RegexElement {

    public Character value;

    public Operand(Character value) {
        this.value = value;
    }

    @Override
    public Character getValue() {
        return this.value;
    }

    /**
     * If the character value of this operand is equal to some operator,
     * it's String representation comes with a backslash before the value.
     *
     * e.g.: value = *, return = "\*"
     * @return the character of this operand as String
     */
    public String toString() {
        if (Operator.check(value)) return "\\" + value;
        else return Character.toString(value);
    }
}
