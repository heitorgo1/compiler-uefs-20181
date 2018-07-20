package br.uefs.compiler.parser;

import br.uefs.compiler.lexer.Lexer;
import br.uefs.compiler.lexer.token.Token;
import br.uefs.compiler.lexer.token.TokenClass;
import br.uefs.compiler.lexer.token.TokenRecognizerAutomataFactory;
import br.uefs.compiler.util.automata.DFA;
import br.uefs.compiler.util.cache.CacheHandler;
import br.uefs.compiler.util.errors.CompilerError;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

public class SemanticTest {

    static Grammar grammar;
    static Lexer lexer;

    @BeforeClass
    public static void buildGrammar() throws Exception {
        String cacheValidationStr = String.join(" ",
                TokenClass.CLASSES.stream().map(c -> c.toString()).collect(Collectors.toList()));
        DFA dfa;

        if (CacheHandler.isCacheValid(cacheValidationStr)) {
            System.out.println("Automata in cache still valid! Reading it...");
            dfa = DFA.class.cast(CacheHandler.readCache());
        } else {
            System.out.println("Invalid automata in cache. Generating new automata...");
            dfa = TokenRecognizerAutomataFactory.getDFAFromClassList(TokenClass.CLASSES);
            CacheHandler.updateCache(cacheValidationStr, dfa);
        }
        lexer = new Lexer();
        lexer.withAutomata(dfa);

        grammar = new Grammar();

        grammar.addRule(new Rule("<D>", new Symbol.Array("{SetScope(0)}", "<T>", "{assign(<L>:Stack[-1].type, <T>:Aux[0].type)}", "<L>", "';'")));
        grammar.addRule(new Rule("<T>", new Symbol.Array("'int'", "{assign(<T>:Aux[-1].type, 'integer')}")));
        grammar.addRule(new Rule("<T>", new Symbol.Array("'float'", "{assign(<T>:Aux[-1].type, 'float')}")));
        grammar.addRule(new Rule("<L>", new Symbol.Array("IDENTIFICADOR", "{insertSymbolType(IDENTIFICADOR:Aux[0].lex, <L>:Aux[-1].type); assign(<R>:Stack[-1].type, <L>:Aux[-1].type)}", "<R>")));
        grammar.addRule(new Rule("<R>", new Symbol.Array("','", "{assign(<L>:Stack[-1].type, <R>:Aux[-1].type)}", "<L>")));
        grammar.addRule(new Rule("<R>", new Symbol.Array("")));
        grammar.setStartSymbol(new Symbol("<D>"));
    }

    @Test
    public void simpleTest() {
        //String action = "{insertType(IDENTIFICADOR, <L>.type); assign(<R>.type, <L>.type)}";
        String action = "{assign(<T>:Stack[-1].type, <L>:Aux[0].type)}";

        //SemanticAnalyser.parseAction(new Symbol(action));
    }

    @Test
    public void processTest() throws Exception {
        String code = "float a, a;";
        lexer.withReader(new StringReader(code));

        List<Token> tokens = lexer.readAllTokens();

        PredictiveParser parser = new PredictiveParser(ParsingTable.build(grammar));

        System.out.println(tokens);
        parser.parse(tokens);

        for (CompilerError error : parser.getErrors()) {
            System.out.println(error.getMessage());
        }
    }
}
