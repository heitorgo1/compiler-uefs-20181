package br.uefs.compiler.parser.semantic;

import java.util.ArrayList;
import java.util.List;

public class Action {

    private String funcName;
    private List<Parameter> parameters;

    public Action(String funcName, List<String> params) {
        this.funcName = funcName;
        parameters = new ArrayList<>();
        for (String param : params) {
            if (!SemanticAnalyser.isVariableParam(param)) {
                parameters.add(new ConstantParam(param));
            } else {
                parameters.add(new VariableParam(param));
            }
        }
    }

    public boolean isNop() {
        return funcName.equals("nop");
    }

    public boolean isPop() {
        return funcName.equals("pop");
    }

    public String getFuncName() {
        return funcName;
    }

    public List<Parameter> getParams() {
        return parameters;
    }


    @Override
    public String toString() {
        return String.format("%s(%s)", funcName, parameters);
    }
}
