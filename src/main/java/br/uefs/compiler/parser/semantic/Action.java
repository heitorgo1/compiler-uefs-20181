package br.uefs.compiler.parser.semantic;

import java.util.ArrayList;
import java.util.List;

public class Action {

    private String funcName;
    private List<String> parametersStr;

    public Action(String funcName, List<String> params) {
        this.funcName = funcName;
        parametersStr = params;
    }

    public boolean isNop() {
        return funcName.equals("nop");
    }

    public boolean isPop() {
        assert parametersStr.size() == 1;

        return funcName.equals("pop");
    }

    public String getFuncName() {
        return funcName;
    }

    public List<String> getParamsStr() {
        return parametersStr;
    }


    @Override
    public String toString() {
        return String.format("%s(%s)", funcName, parametersStr);
    }
}
