package br.uefs.compiler.parser.semantic;

import br.uefs.compiler.parser.Symbol;

import java.util.ArrayList;
import java.util.List;

public class Parameter {

    public static class Array extends ArrayList<Parameter> {

        public Array() {
            super();
        }
    }

    private boolean isConstant;
    private String value;
    private Symbol reference;
    private List<String> attributes;

    public Parameter(String value) {
        assert value != null;

        this.value = value;
        this.reference = null;
        this.attributes = null;
        isConstant = true;
    }

    public Parameter(Symbol reference, List<String> attributes) {
        assert reference != null;
        assert attributes != null;

        this.value = null;
        this.reference = reference;
        this.attributes = attributes;
        this.isConstant = false;
    }

    public boolean isConstant() {
        return isConstant;
    }

    public Object readFromAttribute(String attribute) {
        return reference.getProperty(attribute);
    }

    public void writeToAttribute(String attribute, Object value) {
        reference.setProperty(attribute, value);
    }

    public void concatToAttribute(String attribute, Object value) {
        if (readFromAttribute(attribute) == null) {
            writeToAttribute(attribute, "");
        }

        Object current = readFromAttribute(attribute);
        writeToAttribute(attribute, current.toString().trim() + value.toString().trim());
    }

    public void appendToAttribute(String attribute, Object value) {

        if (readFromAttribute(attribute) == null) {
            writeToAttribute(attribute, new ArrayList<>());
        }

        List<Object> currentList = List.class.cast(readFromAttribute(attribute));
        currentList.add(value);
    }

    public void extendListFromAttribute(String attribute, List<Object> values) {
        if (readFromAttribute(attribute) == null) {
            writeToAttribute(attribute, new ArrayList<>());
        }

        List<Object> currentList = List.class.cast(readFromAttribute(attribute));
        currentList.addAll(values);
    }


    public List<Object> getValuesAsList() {
        List<Object> values = new ArrayList<>();
        for (String attr : attributes) {
            values.add(readFromAttribute(attr));
        }
        return values;
    }

    public Object readOnlyAttribute() {
        assert attributes.size() == 1;

        return readFromAttribute(attributes.get(0));
    }

    public Object read() {
        return value == null ?
                attributes.size() == 1 ?
                        readOnlyAttribute()
                        : getValuesAsList()
                : value.equals("''") ?
                ""
                : value;
    }

    public void write(Object value) throws Exception {
        if (isConstant())
            throw new Exception("Parameter not writable");

        for (String attr : attributes) {
            writeToAttribute(attr, value);
        }
    }

    public void assign(Parameter target) throws Exception {
        if (isConstant())
            throw new Exception("Parameter not writable");

        if (target.isConstant()) {
            write(target.read());
        } else {
            List<Object> valueList = target.getValuesAsList();
            if (valueList.size() != attributes.size())
                throw new Exception(String.format("Attribute List don't match: expected=%d,got=%d",
                        attributes.size(), valueList.size()));

            for (int i = 0; i < attributes.size(); i++) {
                writeToAttribute(attributes.get(i), valueList.get(i));
            }
        }
    }

    public void concat(Object value) throws Exception {
        if (isConstant())
            throw new Exception("Parameter not writable");

        for (String attr : attributes) {
            concatToAttribute(attr, value);
        }
    }

    public void concat(Parameter target) throws Exception {
        if (isConstant())
            throw new Exception("Parameter not writable");

        if (target.isConstant()) {
            concat(target.read());
        } else {
            List<Object> valueList = target.getValuesAsList();
            if (valueList.size() != attributes.size())
                throw new Exception(String.format("Attribute List don't match: expected=%d,got=%d",
                        attributes.size(), valueList.size()));

            for (int i = 0; i < attributes.size(); i++) {
                concatToAttribute(attributes.get(i), valueList.get(i));
            }
        }
    }

    public void append(Parameter target) throws Exception {
        if (isConstant())
            throw new Exception("Parameter not writable");

        if (target.isConstant()) {
            concat(target.read());
        } else {
            List<Object> valueList = target.getValuesAsList();
            if (valueList.size() != attributes.size())
                throw new Exception(String.format("Attribute List don't match: expected=%d,got=%d",
                        attributes.size(), valueList.size()));

            for (int i = 0; i < attributes.size(); i++) {
                if (valueList.get(i) instanceof List) {
                    extendListFromAttribute(attributes.get(i), List.class.cast(valueList.get(i)));
                } else {
                    appendToAttribute(attributes.get(i), valueList.get(i));
                }
            }
        }
    }

    public Symbol getSymbol() {
        assert reference != null;
        return reference;
    }

    @Override
    public String toString() {
        return isConstant ?
                String.format("Param{value=%s}", read())
                : String.format("Param{reference=%s,attributes=%s,value=%s}", reference, attributes, getValuesAsList());
    }
}
