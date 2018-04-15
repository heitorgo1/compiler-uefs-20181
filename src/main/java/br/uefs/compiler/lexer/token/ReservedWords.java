package br.uefs.compiler.lexer.token;

import java.util.Arrays;
import java.util.List;

public class ReservedWords {
    public static final List<String> WORDS = Arrays.asList(
            "const",
            "var",
            "struct",
            "typedef",
            "procedure",
            "function",
            "return",
            "start",
            "if",
            "then",
            "else",
            "while",
            "scan",
            "print",
            "int",
            "float",
            "bool",
            "string",
            "true",
            "false",
            "extends"
    );

    public static boolean isReserved(String word) {
        return WORDS.contains(word);
    }
}
