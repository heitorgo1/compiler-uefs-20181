package br.uefs.compiler.parser.semantic;

import br.uefs.compiler.parser.semantic.functions.*;

import java.util.Hashtable;
import java.util.Map;
import java.util.function.BiConsumer;

public class SemanticAction {

    private String name;
    private Parameter.Array paramList;

    private static Map<String, BiConsumer<Context, Parameter.Array>> SEMANTIC_FUNCTIONS_MAP =
            new Hashtable<String, BiConsumer<Context, Parameter.Array>>() {{
                put("assign", new Assign());
                put("append", new Append());
                put("assertEqual", new AssertEqual());
                put("attachStructVars", new AttachStructVars());
                put("assertInitConst", new AssertInitConst());
                put("arrayDimMatch", new ArrayDimMatch());
                put("assertParams", new AssertParams());
                put("assertNoParams", new AssertNoParams());
                put("assertNotConst", new AssertNotConst());
                put("assignedTypeMatch", new AssignedTypeMatch());
                put("concat", new Concat());
                put("checkStructDefined", new CheckStructDefined());
                put("checkAttribution", new CheckAttribution());
                put("decScope", new DecScope());
                put("extendStruct", new ExtendStruct());
                put("getDefinedType", new GetDefinedType());
                put("getIdentifier", new GetIdentifier());
                put("getStructVarType", new GetStructVarType());
                put("getNumType", new GetNumType());
                put("getParams", new GetParams());
                put("hasOneStart", new HasOneStart());
                put("insertSymbol", new InsertSymbol());
                put("incScope", new IncScope());
                put("incStart", new IncStart());
                put("insertFuncParams", new InsertFuncParams());
                put("incAssign", new IncAssign());
                put("invalidConst", new InvalidConst());
                put("initializeParams", new InitializeParams());
                put("popArray", new PopArray());
                put("setScope", new SetScope());
                put("typedef", new Typedef());
                put("typeMatch", new TypeMatch());
            }};

    public SemanticAction(String name, Parameter.Array paramList) {
        this.name = name;
        this.paramList = paramList;
    }

    public void run(Context c) throws Exception {
        if (SEMANTIC_FUNCTIONS_MAP.containsKey(name)) {
            SEMANTIC_FUNCTIONS_MAP.get(name).accept(c, paramList);
        } else {
            throw new Exception("No such function: " + name);
        }
    }
}
