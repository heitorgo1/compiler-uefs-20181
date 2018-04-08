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
    private int forward;
    private int currentLine;
    private int lexemeLine;

    public InputReader(Reader in) throws IOException {
        super(in);
        lexemeBegin = 0;
        forward = 0;
        currentLine = 1;
        lexemeLine = 1;
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

    public Character[] getBuffer(){
        return buffer;
    }

    public int getCurrentLine() {
        return currentLine;
    }

    public int getLexemeLine() {
        return lexemeLine;
    }

    public boolean isEof() {
        return forward >= buffer.length;
    }

    public Character readch() {
        char c = buffer[forward];
        return c;
    }

    public void forward() {
        forward++;
    }

    public boolean back() {
        if (forward == lexemeBegin) return false;
        forward--;
        return true;
    }

    public void failLexeme() {
        lexemeBegin++;
        while (forward != lexemeBegin) {
            if (forward == '\n') currentLine--;
            forward--;
        }
    }

    public void updateBegin() {
        lexemeBegin = forward;
    }

    public String getLexeme() {
        StringBuilder sb = new StringBuilder();
        for (int i = lexemeBegin; i < forward; i++) {
            sb.append(buffer[i]);
        }
        return sb.toString();
    }
}
