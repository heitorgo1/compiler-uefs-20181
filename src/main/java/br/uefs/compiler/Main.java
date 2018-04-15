package br.uefs.compiler;

import br.uefs.compiler.lexer.Lexer;
import br.uefs.compiler.lexer.token.Token;
import br.uefs.compiler.lexer.token.TokenClass;
import br.uefs.compiler.lexer.token.TokenRecognizerAutomataFactory;
import br.uefs.compiler.util.automata.DFA;
import br.uefs.compiler.util.cache.CacheHandler;
import br.uefs.compiler.util.regex.Regex;
import br.uefs.compiler.util.regex.SpecialCharacter;

import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        Reader in = new StringReader("11.11@+AB+5.0.10");
//        Reader in = new FileReader("entrada/entrada_exemplo_teste_lexico.txt");
        Lexer lexer;
        DFA dfa;
        String validationStr = String.join(" ",
                TokenClass.CLASSES.stream().map(c -> c.toString()).collect(Collectors.toList()));

        if (CacheHandler.isCacheValid(validationStr)) {
            System.out.println("Automata in cache still valid! Reading it...");
            dfa = DFA.class.cast(CacheHandler.readCache());
        }
        else {
            System.out.println("Invalid automata in cache. Generating new automata...");
            dfa = TokenRecognizerAutomataFactory.getDFAFromClassList(TokenClass.CLASSES);
            CacheHandler.updateCache(validationStr, dfa);
        }
        System.out.println("Done.");

        lexer = new Lexer(in, dfa);

        List<Token> tokens = lexer.readAllTokens();
        List<Token> errors = lexer.getErrors();

        System.out.println(tokens);
        System.out.println(errors);

        System.out.println(new Regex("(a|(bc)*)").toPostfix());

    }
}
