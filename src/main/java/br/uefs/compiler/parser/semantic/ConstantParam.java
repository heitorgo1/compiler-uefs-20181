package br.uefs.compiler.parser.semantic;

public class ConstantParam implements Parameter {

    private String value;

    public ConstantParam(String param) {
        this.value = param;
    }

    @Override
    public void setAttribute(String value) {}

    @Override
    public String getAttribute() {
        return null;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ConstantParam{value=" + value + "}";
    }

}
