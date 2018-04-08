package br.uefs.compiler.lexer;

import br.uefs.compiler.util.automata.AutomataSimulator;
import br.uefs.compiler.util.automata.DFA;
import br.uefs.compiler.util.automata.NFA;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {

    private static List<TokenClass> tokenClasses = Arrays.asList(
            new TokenClass(256, "const", "PALAVRARESERVADA"),
            new TokenClass(257, "var", "PALAVRARESERVADA"),
            new TokenClass(258, "struct", "PALAVRARESERVADA"),
            new TokenClass(259, "typedef", "PALAVRARESERVADA"),
            new TokenClass(260, "procedure", "PALAVRARESERVADA"),
            new TokenClass(261, "function", "PALAVRARESERVADA"),
            new TokenClass(262, "return", "PALAVRARESERVADA"),
            new TokenClass(263, "start", "PALAVRARESERVADA"),
            new TokenClass(264, "if", "PALAVRARESERVADA"),
            new TokenClass(265, "then", "PALAVRARESERVADA"),
            new TokenClass(266, "else", "PALAVRARESERVADA"),
            new TokenClass(267, "while", "PALAVRARESERVADA"),
            new TokenClass(268, "scan", "PALAVRARESERVADA"),
            new TokenClass(269, "print", "PALAVRARESERVADA"),
            new TokenClass(270, "int", "PALAVRARESERVADA"),
            new TokenClass(271, "float", "PALAVRARESERVADA"),
            new TokenClass(272, "bool", "PALAVRARESERVADA"),
            new TokenClass(273, "string", "PALAVRARESERVADA"),
            new TokenClass(274, "true", "PALAVRARESERVADA"),
            new TokenClass(275, "false", "PALAVRARESERVADA"),
            new TokenClass(276, "extends", "PALAVRARESERVADA"),
            new TokenClass(277, "\\l(\\l|\\d|_)*", "IDENTIFICADOR"),
            new TokenClass(278, "+|-|\\*|/|(++)|(--)", "OPERADORARITMETICO"),
            new TokenClass(279, "\\s\\s*", "ESPACO"),
            new TokenClass(280, "(-)?\\s*\\d\\d*(\\.\\d(\\d)*)?", "NUMERO"),
            new TokenClass(281, "(!=)|(==)|<|(<=)|>|(>=)|=", "OPERADORRELACIONAL"),
            new TokenClass(282, "!|(&&)|(\\|\\|)", "OPERADORLOGICO"),
            new TokenClass(283, "//(\\y)*\n", "COMENTARIOLINHA"),
            new TokenClass(284, "/\\*(\\y|\\s|\")*\\*/", "COMENTARIOBLOCO"),
            new TokenClass(285, ";|,|\\(|\\)|[|]|{|}|\\.", "DELIMITADOR"),
            new TokenClass(286, "\"(\\y|(\\\\\")|(\\\\n))*\"", "CADEIACARECTERES"),
            // Errors
            new TokenClass(287, "\\l(\\l|\\d|\\N)*", "ERROR_IDENTIFICADORMALFORMADO"),
            new TokenClass(288, "\"(\\y|(\\\\\")|(\\\\n))*", "ERROR_CADEIAABERTA"),
            new TokenClass(289, "(-)?\\s*\\d\\d*\\.", "ERROR_NUMERODECIMALINCOMPLETO"),
            new TokenClass(290, "(-)?\\s*\\d\\d*\\N*\\d*(\\.\\d\\d*\\N*\\d*)?", "ERROR_NUMEROMALFORMADO"),
            new TokenClass(291, "\\N\\N*", "ERROR_VALORINESPERADO"),
            new TokenClass(292, "/\\*(\\l|\\d|\\N|\\s|\\a)*", "ERROR_COMENTARIOBLOCOABERTO")
            );

    private AutomataSimulator simulator;
    private InputReader reader;
    private List<Token> errors;

    public Lexer() throws Exception {
        DFA dfa = generateDFA();
        simulator = new AutomataSimulator(dfa);
        reader = null;
    }

    public Lexer(Reader in) throws Exception {
        DFA dfa = generateDFA();
        simulator = new AutomataSimulator(dfa);
        reader = new InputReader(in);
    }

    private DFA generateDFA() throws Exception {
        List<NFA> list = new ArrayList<>();

        for (TokenClass tokenClass : tokenClasses) {
            NFA nfa = NFA.fromRegexExpression(tokenClass.getRegex(), tokenClass, TokenClass.INVALID_CLASS);
            list.add(nfa);
        }

        return NFA.merge(list, TokenClass.INVALID_CLASS).toDFA();
    }

    public void withReader(Reader in) throws IOException {
        reader = new InputReader(in);
    }

    public Token nextToken() throws Exception {
        Token token = null;
        while (!reader.isEof()) {
            Character c = reader.readch();
            boolean ok = simulator.next(c);
            reader.forward();

            if (!ok) {
                token = tryAccept();
                break;
            }
        }

        if (token == null)
            token = tryAccept();

        simulator.reset();
        return token;
    }

    public Token tryAccept() {
        Token token = null;
        String failedLexeme = reader.getLexeme();

        while (!simulator.isAccepting()) {
            boolean ok = simulator.back();
            reader.back();
            if (!ok) break;
        }

        if (simulator.isAccepting()) {
            String lexeme = reader.getLexeme();
            TokenClass type = simulator.getTag(TokenClass.class);
            token = new Token(type, lexeme, 0);
            reader.updateBegin();
        } else {
            System.out.format("ERROR: %s\n", failedLexeme);
        }
        return token;
    }

    public void scan() throws Exception {
        Token token;
        while ((token = nextToken()) != null) {
            System.out.println(token);
        }
    }
}
