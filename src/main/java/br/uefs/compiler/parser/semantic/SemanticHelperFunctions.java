package br.uefs.compiler.parser.semantic;

import br.uefs.compiler.parser.Symbol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SemanticHelperFunctions {

    private static Pattern ACTION_PATTERN = Pattern.compile("((\\w|\\d)+)\\((.*)\\)");
    private static Pattern STACK_PATTERN = Pattern.compile("(\\w+)\\[(.+)\\]");

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
                    .stream()
                    .map(param -> param.trim())
                    .collect(Collectors.toList());

            return new Action(funcName, params);
        }
        return new Action("nop", Arrays.asList());
    }

    public static int extractOffset(String param) {
        assert isInAux(param) || isInStack(param);

        Matcher m = STACK_PATTERN.matcher(param);
        int offset = 0;
        if (m.find()) {
            offset = Integer.parseInt(m.group(2));
        }
        return offset;
    }

    public static List<String> extractAttributes(String param) {
        assert isInAux(param) || isInStack(param);

        String[] attr = param.split("\\.");
        if (attr.length == 1) return new ArrayList<>();
        return Arrays.asList(attr[1].split("!"));
    }

    public static String[] splitParam(String param) {
        param = param.replaceAll("(^\\[)|(\\]$)", "");

        return param.split("\\s+");
    }

    public static boolean isInAux(String param) {
        return param.contains("Aux");
    }

    public static boolean isInStack(String param) {
        return param.contains("Stack");
    }


}
