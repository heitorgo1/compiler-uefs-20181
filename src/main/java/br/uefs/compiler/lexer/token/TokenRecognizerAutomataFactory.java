package br.uefs.compiler.lexer.token;

import br.uefs.compiler.util.automata.DFA;
import br.uefs.compiler.util.automata.NFA;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for instantiating an Automata from token classes.
 */
public class TokenRecognizerAutomataFactory {

    public static DFA getDFAFromClassList(List<TokenClass> tclasses) throws Exception {
        NFA nfa = getNFAFromClassList(tclasses);
        return nfa.toDFA();
    }

    public static NFA getNFAFromClassList(List<TokenClass> tclasses) throws Exception {
        List<NFA> list = new ArrayList<>();

        for (TokenClass tokenClass : TokenClass.CLASSES) {
            NFA nfa = NFA.fromRegexExpression(tokenClass.getRegex(), tokenClass, TokenClass.INVALID_CLASS);
            list.add(nfa);
        }

        return NFA.merge(list, TokenClass.INVALID_CLASS);
    }
}
