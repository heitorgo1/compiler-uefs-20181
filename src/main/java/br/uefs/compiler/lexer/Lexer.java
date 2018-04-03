package br.uefs.compiler.lexer;

import br.uefs.compiler.util.RegexTree;
import br.uefs.compiler.util.automata.Automata;
import br.uefs.compiler.util.automata.NFA;
import br.uefs.compiler.util.automata.TransitionTable;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {

    private static List<TokenClass> tokenClasses = Arrays.asList(
            new TokenClass(256, "const", "CONST"),
            new TokenClass(257, "var", "VAR"),
            new TokenClass(258, "struct", "STRUCT"),
            new TokenClass(259, "typedef", "TYPEDEF"),
            new TokenClass(260, "procedure", "PROCEDURE"),
            new TokenClass(261, "function", "FUNCTION"),
            new TokenClass(262, "return", "RETURN"),
            new TokenClass(263, "start", "START"),
            new TokenClass(264, "if", "IF"),
            new TokenClass(265, "then", "THEN"),
            new TokenClass(266, "else", "ELSE"),
            new TokenClass(267, "while", "WHILE"),
            new TokenClass(268, "scan", "SCAN"),
            new TokenClass(269, "print", "PRINT"),
            new TokenClass(270, "int", "INT"),
            new TokenClass(271, "float", "FLOAT"),
            new TokenClass(272, "bool", "BOOL"),
            new TokenClass(273, "string", "STRING"),
            new TokenClass(274, "true", "TRUE"),
            new TokenClass(275, "false", "FALSE"),
            new TokenClass(276, "extends", "EXTENDS"),
            new TokenClass(277, "\\l(\\l|\\d|_)*", "IDENTIFIER"),
            new TokenClass(278, "(-)?\\s*\\d\\d*(\\.\\d(\\d)*)?", "NUMBER"),
            new TokenClass(279, "+|-|\\*|/|(++)|(--)", "AROP"),
            new TokenClass(280, "(!=)|(==)|<|(<=)|>|(>=)|=", "RELOP"),
            new TokenClass(281, "!|(&&)|(\\|\\|)", "LOGOP"),
            new TokenClass(282, "//(\\y|\\s)*\n", "LINE_COMMENT"),
            new TokenClass(283, "/\\*(\\y|\\s|\")*\\*/", "BLOCK_COMMENT"),
            new TokenClass(284, ";|,|\\(|\\)|[|]|{|}|\\.", "DELIMITER"),
            new TokenClass(285, "\"(\\y|(\\\\\")|(\\\\n))*\"", "STRING_LITERAL"),
            new TokenClass(286, "\\s\\s*", "WHITESPACE")
    );

    private Automata automata;

    public Lexer() throws Exception {
        NFA nfa = this.genNFA();

        TransitionTable transTable = nfa.asTransitionTable().subsetConstructionDFA();

        automata = new Automata(transTable);
    }

    private NFA genNFA() throws Exception {
        List<NFA> list = new ArrayList<>();

        for (TokenClass tokenClass : tokenClasses) {
            NFA nfa = NFA.fromRegexTree(RegexTree.fromRegex(tokenClass.getRegex()), tokenClass);
            list.add(nfa);
        }

        return NFA.mergeNFAs(list);
    }

    private TokenClass getTokenClass(int id) {
        for (TokenClass clazz : tokenClasses) {
            if (clazz.get() == id) return clazz;
        }
        return null;
    }

    public void scan() throws Exception {
        String input = new String("using namespace std;\n" +
                "\n" +
                "int main () {\n" +
                "        int a,b,c;\n" +
                "        scanf (\"%d %d %d\",&a,&b,&c);\n" +
                "        printf (\"%d %d %d hello\\n\",a,b,c);\n" +
                "        return 0;\n" +
                "}\n");
        DoubleBufferReader reader = new DoubleBufferReader(new StringReader(input));
        List<Token> tokens = new ArrayList<>();
        int line = 1;
        int cnt = 0;

        int v;
        while ((v = reader.read()) != DoubleBufferReader.EOF) {
            String in = Character.toString((char)v);
            boolean ok = automata.forward(in);

            if (!ok) {
                while (!automata.isAccepting()) {
                    boolean end = automata.back();
                    if (end) break;
                }

                if (automata.isAccepting()) {
                    reader.back();
                    cnt++;
                    int tagId = automata.getCurrentStateTagId();
                    String lexeme = reader.getString();
                    TokenClass tokenClass = getTokenClass(tagId);
                    Token token = new Token(tokenClass, lexeme);
                    tokens.add(token);
                    System.out.format("%s %d line: %d\n",token, tagId, line);
                }
                else {
                    System.out.format("Character '%s' not valid on line %d\n", reader.getString(), line);
                }

                reader.updateBegin();
                automata.reset();
            }

            if (cnt == 0 && v == 10) line++;
            if (cnt > 0) cnt--;
        }

        while (!automata.isAccepting()) {
            boolean end = automata.back();
            if (end) break;
        }

        if (automata.isAccepting()) {
            int tagId = automata.getCurrentStateTagId();
            String lexeme = reader.getString();
            TokenClass tokenClass = getTokenClass(tagId);
            Token token = new Token(tokenClass, lexeme);
            tokens.add(token);
            System.out.format("%s %d line: %d\n",token, tagId, line);
        }

        System.out.println(tokens);

        reader.close();
    }
}
