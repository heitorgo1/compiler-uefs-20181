package br.uefs.compiler.lexer;

import br.uefs.compiler.lexer.token.Token;
import br.uefs.compiler.lexer.token.TokenClass;
import br.uefs.compiler.lexer.token.TokenMistakeHandler;
import br.uefs.compiler.util.automata.Automata;
import br.uefs.compiler.util.automata.AutomataSimulator;
import br.uefs.compiler.util.exceptions.InvalidCharacterException;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class responsible for generating tokens given an input Reader and
 * storing errors if they occur.
 */
public class Lexer {

    private List<String> uselessClasses = Arrays.asList(
            "ESPACO", "COMENTARIOLINHA", "COMENTARIOBLOCO"
    );

    private AutomataSimulator simulator;
    private InputReader reader;
    private List<Token> errors;

    public Lexer() {
        simulator = null;
        errors = new ArrayList<>();
        reader = null;
    }

    public Lexer(Reader in, Automata auto) throws Exception {
        simulator = new AutomataSimulator(auto);
        errors = new ArrayList<>();
        reader = new InputReader(in);
    }

    public void withReader(Reader in) throws IOException {
        reader = new InputReader(in);
    }

    public void withAutomata(Automata auto) throws Exception {
        simulator = new AutomataSimulator(auto);
    }

    public List<Token> getErrors() {
        return errors;
    }

    private boolean isTokenUseless(Token token) {
        return uselessClasses.contains(token.getTokenClass().getName());
    }

    private boolean isTokenError(Token token) {
        return token.getTokenClass().getName().contains("ERROR");
    }

    public List<Token> readAllTokens() throws Exception {
        List<Token> tokens = new ArrayList<>();
        for (Token token = nextToken(); token != null; token = nextToken()) {
            if (isTokenError(token)) {
                errors.add(token);
            } else if (!tokens.isEmpty() && TokenMistakeHandler.isMistake(tokens.get(tokens.size() - 1), token)) {
                tokens.addAll(TokenMistakeHandler.solveMistake(tokens.get(tokens.size() - 1), token));
            } else if (!isTokenUseless(token)) {
                tokens.add(token);
            }
        }
        return tokens;
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
                    token = new Token(TokenClass.INVALID_CHARACTER_CLASS,
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
