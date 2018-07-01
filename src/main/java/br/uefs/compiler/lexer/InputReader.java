package br.uefs.compiler.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper reader class that keeps pointers for lexeme creation
 * and automata iteration.
 */
public class InputReader extends BufferedReader {

    private Character[] buffer;
    private int lexemeBegin;
    private int lexemeEnd;
    /**
     * Pointer that should move with the automata
     */
    private int forward;
    private int currentLine;

    public InputReader(Reader in) throws IOException {
        super(in);
        lexemeBegin = 0;
        forward = 0;
        lexemeEnd = 0;
        currentLine = 1;
        buffer = readAllFile(in);
    }

    /**
     * Load reader content in Character buffer.
     *
     * @param in input reader
     * @return Array of Characters encountered in reader
     * @throws IOException
     */
    private Character[] readAllFile(Reader in) throws IOException {
        List<Character> list = new ArrayList<>();
        int c;
        while ((c = in.read()) != -1) {
            if (c <= 128)
                list.add((char) c);
        }
        close();
        return list.toArray(new Character[list.size()]);
    }

    public int getCurrentLine() {
        return currentLine;
    }

    public boolean isEof() {
        return lexemeBegin >= buffer.length;
    }

    public void mark() {
        lexemeEnd = forward;
    }

    /**
     * Read current char from input.
     * <p>
     * If forward can't move, return invalid character -1.
     *
     * @return the current char pointed by forward
     */
    public Character readch() {
        if (forward >= buffer.length) return new Character((char) -1);
        char c = buffer[forward++];
        return c;
    }

    /**
     * Restart reading from the next char after lexemeBegin.
     */
    public void startFromNextChar() {
        if (lexemeBegin == '\n') currentLine++;
        lexemeBegin++;
        lexemeEnd = lexemeBegin;
        forward = lexemeBegin;
    }

    public char getCurrentChar() {
        return buffer[lexemeBegin];
    }

    /**
     * Get lexeme between lexemeBegin and lexemeEnd
     *
     * @return a lexeme string
     */
    public String getLexeme() {
        StringBuilder sb = new StringBuilder();
        for (int i = lexemeBegin; i < lexemeEnd; i++) {
            sb.append(buffer[i]);
        }
        return sb.toString();
    }

    /**
     * Update all pointers and the currentLine counter.
     */
    public void updatePointers() {
        while (lexemeBegin != lexemeEnd) {
            if (buffer[lexemeBegin] == '\n') currentLine++;
            lexemeBegin++;
        }
        forward = lexemeEnd;
    }
}
