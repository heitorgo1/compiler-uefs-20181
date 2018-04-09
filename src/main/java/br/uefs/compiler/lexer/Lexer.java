package br.uefs.compiler.lexer;

import br.uefs.compiler.util.automata.AutomataSimulator;
import br.uefs.compiler.util.automata.DFA;
import br.uefs.compiler.util.automata.NFA;
import br.uefs.compiler.util.exceptions.InvalidCharacterException;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {

    private static final TokenClass invalidCharacterClass =
            new TokenClass(-1, "", "ERROR_CARACTEREINVALIDO");

    private static final List<TokenClass> tokenClasses = Arrays.asList(
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
            new TokenClass(292, "/\\*(\\b|\\s|\")*", "ERROR_COMENTARIOBLOCOABERTO")
    );

    private List<String> uselessClasses = Arrays.asList(
            "ESPACO", "COMENTARIOLINHA", "COMENTARIOBLOCO"
    );

    private AutomataSimulator simulator;
    private InputReader reader;
    private List<Token> errors;

    public Lexer() throws Exception {
        DFA dfa = generateDFA();
        simulator = new AutomataSimulator(dfa);
        errors = new ArrayList<>();
        reader = null;
    }

    public Lexer(Reader in) throws Exception {
        DFA dfa = generateDFA();
        simulator = new AutomataSimulator(dfa);
        errors = new ArrayList<>();
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

    public TokenClass getClassById(int id) {
        for (TokenClass tclass : tokenClasses) {
            if (tclass.getId() == id) return tclass;
        }
        return TokenClass.INVALID_CLASS;
    }

    public void withReader(Reader in) throws IOException {
        reader = new InputReader(in);
    }

    private boolean isTokenUseless(Token token) {
        return uselessClasses.contains(token.getTokenClass().getName());
    }

    private boolean isTokenError(Token token) {
        return token.getTokenClass().getName().contains("ERROR");
    }

    private boolean hasProblem(Token previousToken, Token currentToken) {
        return currentToken.getTokenClass().getName().equals("NUMERO") &&
                currentToken.getLexeme().contains("-") &&
                !previousToken.getLexeme().equals("(") &&
                !previousToken.getLexeme().equals("=") &&
                !"+-/*".contains(previousToken.getLexeme());
    }

    public List<Token> handleProblem(Token token) {
        Token minusToken = new Token(getClassById(278), "-", reader.getCurrentLine());
        String number = token.getLexeme().replace("-", "");
        Token numToken = new Token(token.getTokenClass(), number, token.getLine());
        return Arrays.asList(minusToken, numToken);
    }

    public List<Token> readAllTokens() throws Exception {
        List<Token> tokens = new ArrayList<>();
        for (Token token = nextToken(); token != null; token = nextToken()) {
            if (isTokenError(token)) {
                errors.add(token);
            } else if (!tokens.isEmpty() && hasProblem(tokens.get(tokens.size() - 1), token)) {
                tokens.addAll(handleProblem(token));
            } else if (!isTokenUseless(token)) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    public List<Token> getErrors() {
        return errors;
    }

    public Token nextToken() throws Exception {
        Token token = null;
        while (!reader.isEof()) {
            Character c = reader.readch();
            boolean ok = simulator.next(c);

            if (simulator.isAccepting()) reader.mark();

            if (!ok) {
                try {
                    token = tryAcceptLexeme();
                    reader.updatePointers();
                } catch (InvalidCharacterException e) {
                    token = new Token(invalidCharacterClass,
                            Character.toString(e.getInvalidCharacter()),
                            reader.getCurrentLine());
                    reader.startFromNextChar();
                }
                break;
            }
        }

        simulator.reset();
        return token;
    }

    public Token tryAcceptLexeme() throws Exception {
        String lexeme = reader.getLexeme();

        if (simulator.hasAcceptedInput()) {
            TokenClass type = simulator.getAcceptingTag(TokenClass.class);
            return new Token(type, lexeme, reader.getCurrentLine());
        } else
            throw new InvalidCharacterException(reader.getCurrentChar());
    }

    public void printTokens() throws Exception {
        Token token;
        while ((token = nextToken()) != null) {
            System.out.println(token);
        }
    }
}
