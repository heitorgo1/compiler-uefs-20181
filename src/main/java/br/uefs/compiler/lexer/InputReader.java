package br.uefs.compiler.lexer;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class InputReader extends BufferedReader {

    private Character[] buffer;
    private int lexemeBegin;
    private int lexemeEnd;
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

    private Character[] readAllFile(Reader in) throws IOException {
        List<Character> list = new ArrayList<>();
        int c;
        while ((c = in.read()) != -1) {
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

    public Character readch() {
        if (forward >= buffer.length) return new Character((char)-1);
        char c = buffer[forward++];
        return c;
    }

    public void startFromNextChar() {
        lexemeBegin++;
        lexemeEnd = lexemeBegin;
        forward = lexemeBegin;
    }

    public char getCurrentChar() {
        return buffer[lexemeBegin];
    }

    public String getLexeme() {
        StringBuilder sb = new StringBuilder();
        for (int i = lexemeBegin; i < lexemeEnd; i++) {
            sb.append(buffer[i]);
        }
        return sb.toString();
    }

    public void updatePointers() {
        while (lexemeBegin != lexemeEnd) {
            if (buffer[lexemeBegin] == '\n') currentLine++;
            lexemeBegin++;
        }
        forward = lexemeEnd;
    }
}
