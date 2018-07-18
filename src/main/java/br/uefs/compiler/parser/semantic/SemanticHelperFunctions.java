package br.uefs.compiler.parser.semantic;

import br.uefs.compiler.parser.Symbol;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SemanticHelperFunctions {

    private static Pattern ACTION_PATTERN = Pattern.compile("((\\w|\\d)+)\\((.*)\\)");

    public static List<String> parseSteps(Symbol symbol) {
        assert symbol.isAction();

        String actionStr = symbol.getName();

        actionStr = actionStr.replaceAll("(^\\{)|(\\}$)", "");

        String[] steps = actionStr.split(";");

        return Arrays.asList(steps);
    }

    public static Symbol createPopActionSymbol(Symbol.Array symbols) {
        int counter = 0;
        for (Symbol sy : symbols) {
            if (!sy.isEmptyString() && !sy.isAction()) counter++;
        }
        String actionName = String.format("{pop(%d)}", counter);
        return new Symbol(actionName);
    }

    public static Action extractAction(String step) {
        step = step.trim();

        Matcher m = ACTION_PATTERN.matcher(step);

        if (m.find()) {
            String funcName = m.group(1).trim();

            List<String> params;
            if (m.group(3).isEmpty()) params = Arrays.asList();
            else params = Arrays.asList(m.group(3).split(","))
                    .stream().map(param -> param.trim())
                    .collect(Collectors.toList());

            return new Action(funcName, params);
        }
        return new Action("nop", Arrays.asList());
    }

    public static boolean isVariableParam(String param) {
        return param.contains("[") && param.contains("]");
    }
}
