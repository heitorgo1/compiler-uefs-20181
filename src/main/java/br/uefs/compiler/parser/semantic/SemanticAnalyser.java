package br.uefs.compiler.parser.semantic;

import br.uefs.compiler.lexer.token.Token;
import br.uefs.compiler.parser.Symbol;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SemanticAnalyser {

    public static int START_COUNTER = 0;
    public static long START_LINE = 0;

    public static Map<String, Map<String, Object>> SYMBOL_TABLE = new Hashtable<>();

    private static Pattern ACTION_PATTERN = Pattern.compile("((\\w|\\d)+)\\((.*)\\)");

    public static List<SemanticError> ERRORS = new ArrayList<>();

    private static Map<String, BiConsumer<List<Parameter>, Token>> FUNCTION_MAP =
            new HashMap<String, BiConsumer<List<Parameter>, Token>>() {{
                put("assign", SemanticFunctions::assign);
                put("insertSymbolType", SemanticFunctions::insertSymbolType);
                put("incStart", SemanticFunctions::incStart);
                put("hasOneStart", SemanticFunctions::hasOneStart);
                put("markAsArray", SemanticFunctions::markAsArray);
                put("insertCategory", SemanticFunctions::insertCategory);
                put("concat", SemanticFunctions::concat);
                put("insertParams", SemanticFunctions::insertParams);
            }};

    public static void reset() {
        SYMBOL_TABLE.clear();
        ERRORS = new ArrayList<>();
        START_COUNTER = 0;
        START_LINE = 0;
    }

    public static List<String> parseSteps(Symbol symbol) {
        assert symbol.isAction();

        String actionStr = symbol.getName();

        actionStr = actionStr.replaceAll("(^\\{)|(\\}$)", "");

        String[] steps = actionStr.split(";");

        return Arrays.asList(steps);
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

    public static void runAction(Action action, Token curToken) throws Exception {
        if (FUNCTION_MAP.containsKey(action.getFuncName())) {
            FUNCTION_MAP.get(action.getFuncName()).accept(action.getParams(), curToken);
        } else {
            throw new Exception("No such function: " + action.getFuncName());
        }
    }
}
