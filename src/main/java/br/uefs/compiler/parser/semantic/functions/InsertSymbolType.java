package br.uefs.compiler.parser.semantic.functions;

import br.uefs.compiler.lexer.token.Token;
import br.uefs.compiler.parser.semantic.Parameter;

import java.util.List;
import java.util.function.BiConsumer;

public class InsertSymbolType implements BiConsumer<List<Parameter>, Token> {
    @Override
    public void accept(List<Parameter> parameters, Token token) {

    }
}
