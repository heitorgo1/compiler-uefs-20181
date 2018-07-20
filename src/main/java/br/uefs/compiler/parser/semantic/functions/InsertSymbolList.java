package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.lexer.token.ReservedWords;
import br.uefs.compiler.parser.semantic.Context;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.SemanticError;
import br.uefs.compiler.parser.semantic.SemanticHelperFunctions;

import java.util.function.BiConsumer;

public class InsertSymbolList implements BiConsumer<Context, Parameter.Array> {
    @Override
    public void accept(Context c, Parameter.Array params) {
        assert params.size() == 5;

        Parameter id = params.get(0); // identifier array
        Parameter line = params.get(1); // line array
        Parameter type = params.get(2); // type
        Parameter category = params.get(3); // category
        Parameter dimention = params.get(4); // dimentions array

        String[] ids = SemanticHelperFunctions.splitParam(id.read());
        String[] lines = SemanticHelperFunctions.splitParam(line.read());
        String[] dimentions = SemanticHelperFunctions.splitParam(dimention.read());

        for (int i = 0; i < ids.length; i++) {
            String curId = ids[i].trim();
            String curLine = lines[i].trim();
            String curDim = dimentions[i].trim();

            if (ReservedWords.isReserved(curId)) {
                String message = String.format("'%s' não pode ser usado como nome de variável", id.read());
                c.addError(new SemanticError(message, c.getCurrentToken().getLine()));
            } else if (c.isDeclaredInCurrentScope(curId)) {
                String message = String.format("Variável '%s' já foi declarada neste escopo.", id.read());
                c.addError(new SemanticError(message, c.getCurrentToken().getLine()));
            } else {
                c.getInCurrentScope(curId).put("line", curLine);
                c.getInCurrentScope(curId).put("type", type.read());
                c.getInCurrentScope(curId).put("cat", category.read());
                c.getInCurrentScope(curId).put("dim", curDim);

                if (category.read().equals("func") || category.read().equals("proc")) {
                    c.getInCurrentScope(curId).put("params", "");
                }
                System.out.println(c.getSymbolTableList());
            }
        }
    }
}
