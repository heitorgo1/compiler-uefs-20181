package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.lexer.token.Token;
import br.uefs.compiler.parser.semantic.ConstantParam;
import br.uefs.compiler.parser.semantic.Parameter;
import br.uefs.compiler.parser.semantic.VariableParam;

import java.util.List;
import java.util.function.BiConsumer;

public class Assign implements BiConsumer<List<Parameter>, Token> {
    @Override
    public void accept(List<Parameter> params, Token token) {
        assert params.size() == 2;

        Parameter first = params.get(0);
        Parameter second = params.get(1);

        VariableParam receiver = VariableParam.class.cast(first);

        if (second instanceof VariableParam) {
            receiver.setAttribute(second.getAttribute());
        } else if (second instanceof ConstantParam) {
            receiver.setAttribute(second.getValue());
        }
    }
}
