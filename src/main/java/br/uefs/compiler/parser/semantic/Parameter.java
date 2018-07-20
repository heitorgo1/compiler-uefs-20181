package br.uefs.compiler.parser.semantic;

import br.uefs.compiler.parser.Symbol;

import java.util.ArrayList;
import java.util.Map;

public class Parameter {

    public static class Array extends ArrayList<Parameter> {

        public Array() {
            super();
        }
    }

    private boolean writeEnabled;
    private String value;
    private Symbol reference;
    private String attribute;

    public Parameter(String value) {
        assert value != null;

        this.value = value;
        this.reference = null;
        this.attribute = null;
        writeEnabled = false;
    }

    public Parameter(Symbol reference, String attribute) {
        assert reference != null;
        assert attribute != null;

        this.value = null;
        this.reference = reference;
        this.attribute = attribute;
        this.writeEnabled = true;
    }

    public boolean isWritable() {
        return writeEnabled;
    }

    public boolean isReadable() {
        return attribute == null || !attribute.isEmpty();
    }

    public String read() {
        return value == null ? reference.getProperty(attribute) : value;
    }

    public String readFromAttribute(String attribute) {
        return reference.getProperty(attribute);
    }

    public void writeToAttribute(String attribute, String value) {
        reference.setProperty(attribute, value);
    }

    public void write(String value) throws Exception {
        if (!isWritable())
            throw new Exception("Parameter not writable");

        reference.setProperty(attribute, value);
    }

    public void assign(Parameter target) throws Exception {
        if (!isWritable())
            throw new Exception("Parameter not writable");

        if (!target.isReadable())
            reference.copyProperties(target.getSymbol().getProperties());
        else
            reference.setProperty(attribute, target.read());
    }

    public void concat(String value) throws Exception {
        if (!isWritable())
            throw new Exception("Parameter not writable");

        if (reference.getProperty(attribute) == null) {
            reference.setProperty(attribute, "");
        }
        String val = read();
        reference.setProperty(attribute, String.format("%s %s", val, value).trim());
    }

    public void concat(Parameter target) throws Exception {
        if (!isWritable())
            throw new Exception("Parameter not writable");

        if (!target.isReadable()) {
            for (Map.Entry<String, String> entry : target.getSymbol().getProperties().entrySet()) {
                if (reference.getProperty(entry.getKey()) == null) {
                    reference.setProperty(entry.getKey(), "");
                }
                String val = reference.getProperty(entry.getKey());
                reference.setProperty(entry.getKey(), String.format("%s %s",val, entry.getValue()).trim());
            }
        }
        else {
            if (reference.getProperty(attribute) == null) {
                reference.setProperty(attribute, "");
            }
            String val = read();
            reference.setProperty(attribute, String.format("%s %s", val, target.read()).trim());
        }

    }

    public Symbol getSymbol(){
        assert reference != null;
        return reference;
    }

    @Override
    public String toString() {
        return value != null ?
                String.format("Param{value=%s}", read())
                : !isReadable()  ?
                String.format("Param{reference=%s}",reference)
                : String.format("Param{reference=%s,attribute=%s,value=%s}", reference, attribute, read());
    }
}
