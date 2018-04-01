package br.uefs.compiler.util;

import org.junit.Assert;
import org.junit.Test;

public class CharUtilsTest {

    @Test
    public void isLetterTest() {

        for (int i = 0; i < 256; i++) {
            char c = (char)i;
            if ( (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
                Assert.assertTrue(CharUtils.isLetter(c));
            else
                Assert.assertFalse(CharUtils.isLetter(c));
        }
    }

    @Test
    public void isDigitTest() {

        for (int i = 0; i < 256; i++) {
            char c = (char)i;
            if ( (c >= '0' && c <= '9'))
                Assert.assertTrue(CharUtils.isDigit(c));
            else
                Assert.assertFalse(CharUtils.isDigit(c));
        }
    }

    @Test
    public void isSymbolTest() {

        for (int i = 0; i < 256; i++) {
            char c = (char)i;
            if ( i != 34 && (i >= 32 && i <= 126) )
                Assert.assertTrue(CharUtils.isSymbol(c));
            else
                Assert.assertFalse(CharUtils.isSymbol(c));
        }
    }

    @Test
    public void isWhitespaceTest() {

        for (int i = 0; i < 256; i++) {
            char c = (char)i;
            if ( c == '\n' || c == '\t' || c == '\r' || c == ' ' )
                Assert.assertTrue(CharUtils.isWhitespace(c));
            else
                Assert.assertFalse(CharUtils.isWhitespace(c));
        }
    }
}
