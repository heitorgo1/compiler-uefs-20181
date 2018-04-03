package br.uefs.compiler.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public class DoubleBufferReader extends BufferedReader {

    private static int BUFFER_SIZE = 4096;
    public static int EOF = -1;

    private char[] buffer;
    private int begin;
    private int forward;

    private int charsRead;

    public DoubleBufferReader(Reader in) throws IOException {
        super(in);

        this.forward = 0;
        this.begin = 0;
        this.charsRead = 0;
        this.buffer = new char[BUFFER_SIZE<<1];
    }

    public int currentBuffer() {
        return forward < BUFFER_SIZE ? 0 : 1;
    }

    public int currentBufferOffset() {
        return currentBuffer() == 0 ? 0 : BUFFER_SIZE;
    }

    public void goForward() {
        forward = (forward+1)%(BUFFER_SIZE<<1);
    }

    public void backForward() {
        if (forward == begin) return;
        forward = (forward-1);
        if (forward < 0) forward = (BUFFER_SIZE<<1)-1;
    }

    public void updateBegin() {
        begin = forward;
    }

    public String getString() {
        StringBuilder sb = new StringBuilder();
        for (int i = begin; i != forward; i = (i+1)%(BUFFER_SIZE<<1)) {
            sb.append(buffer[i]);
        }
        return sb.toString();
    }

    public int read() throws IOException {
        if (charsRead == 0)
            charsRead = read(buffer, currentBufferOffset(), BUFFER_SIZE);

        if (charsRead == EOF) return EOF;

        char c = buffer[forward];
        goForward();
        charsRead--;

        return (int)c;
    }

    public void back() {
        backForward();
        charsRead++;
    }
}
