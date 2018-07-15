package br.uefs.compiler.parser.semantic;

import br.uefs.compiler.parser.Symbol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableParam implements Parameter{

    private Symbol symbol;
    private boolean isInAux;
    private boolean isInStack;
    private int offset;
    private String attribute;

    private static Pattern STACK_PATTERN = Pattern.compile("(\\w+)\\[(.+)\\]");

    public VariableParam(String param) {

        Matcher m = STACK_PATTERN.matcher(param);

        if (m.find()) {

            if (m.group(1).equals("Aux")) {
                isInAux = true;
                isInStack = false;
            }
            else {
                isInAux = false;
                isInStack = true;
            }

            offset = Integer.parseInt(m.group(2));
        }
        String[] attrs = param.split("\\.");
        attribute = attrs[1];
    }

    public boolean isInAux() {
        return isInAux;
    }

    public boolean isInStack() {
        return isInStack;
    }

    public int getOffset() {
        return offset;
    }

    public void setSymbol(Symbol sy) {
        this.symbol = sy;
    }

    @Override
    public void setAttribute(String value) {
        symbol.addProperty(attribute, value);
    }

    @Override
    public String getAttribute() {
        return symbol.getProperty(attribute);
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String toString() {
        return "VariableParam{symbol=" + symbol + ",offset=" + offset + ",attribute=" + attribute + "}";
    }
}
