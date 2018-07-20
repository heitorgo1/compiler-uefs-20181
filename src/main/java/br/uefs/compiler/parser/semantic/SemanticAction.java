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
                put("insertSymbol", new InsertSymbol());
                put("insertSymbolList", new InsertSymbolList());
                put("setScope", new SetScope());
                put("incScope", new IncScope());
                put("decScope", new DecScope());
                put("incStart", new IncStart());
                put("hasOneStart", new HasOneStart());
                put("concat", new Concat());
                put("insertFuncParams", new InsertFuncParams());
                put("typedef", new Typedef());
                put("attachStructVars", new AttachStructVars());
                put("checkDeclaration", new CheckDeclaration());
                put("extendStruct", new ExtendStruct());
                put("incAssign", new IncAssign());
                put("assertEqual", new AssertEqual());
                put("assertInitConst", new AssertInitConst());
                put("invalidConst", new InvalidConst());
                put("getDefinedType", new GetDefinedType());
                put("getIdentifier", new GetIdentifier());
                put("arrayDimMatch", new ArrayDimMatch());
                put("structVarMatch", new StructVarMatch());
                put("getStructVarType", new GetStructVarType());
                put("assertNoParams", new AssertNoParams());
                put("assertNotConst", new AssertNotConst());
                put("assertParams", new AssertParams());
                put("assignedTypeMatch", new AssignedTypeMatch());
                put("getNumType", new GetNumType());
                put("typeMatch", new TypeMatch());
                put("getParams", new GetParams());
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
