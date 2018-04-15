package br.uefs.compiler.lexer.token;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class TokenClass implements Comparable, Serializable {
    public static final TokenClass INVALID_CLASS = new TokenClass(Integer.MAX_VALUE, "", "CLASSEINVALIDA");

    public static final TokenClass INVALID_CHARACTER_CLASS =
            new TokenClass(-1, "", "ERROR_CARACTEREINVALIDO");

    public static final List<TokenClass> CLASSES = Arrays.asList(
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
            new TokenClass(283, "//(\\y|\t|\r)*", "COMENTARIOLINHA"),
            new TokenClass(284, "/\\*(\\y|\\s|\")*\\*/", "COMENTARIOBLOCO"),
            new TokenClass(285, ";|,|\\(|\\)|[|]|{|}|\\.", "DELIMITADOR"),
            new TokenClass(286, "\"(\\y|(\\\\\")|(\\\\n))*\"", "CADEIACARECTERES"),
            // Errors
            new TokenClass(287, "\\l(\\l|\\d|\\N)*", "ERROR_IDENTIFICADORMALFORMADO"),
            new TokenClass(288, "\"(\\y|(\\\\\")|(\\\\n))*", "ERROR_CADEIAABERTA"),
            new TokenClass(289, "(-)?\\s*\\d\\d*\\.", "ERROR_NUMERODECIMALINCOMPLETO"),
            new TokenClass(290, "(-)?\\s*\\d\\d*(\\l|\\N)*\\d*(\\.\\d\\d*(\\l|\\N|\\.)*\\d*)?", "ERROR_NUMEROMALFORMADO"),
            new TokenClass(291, "\\N\\N*", "ERROR_VALORINESPERADO"),
            new TokenClass(292, "/\\*(\\b|\\s|\")*", "ERROR_COMENTARIOBLOCOABERTO")
    );

    private int id;
    private String regex;
    private String name;

    public TokenClass(int id, String regex, String name) {
        this.id = id;
        this.regex = regex;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getRegex() {
        return regex;
    }

    public String getName() {
        return name;
    }

    public static TokenClass getClassById(int id) {
        for (TokenClass tclass : CLASSES) {
            if (tclass.getId() == id) return tclass;
        }
        return INVALID_CLASS;
    }

    public static TokenClass getClassByName(String name) {
        for (TokenClass tclass : CLASSES) {
            if (tclass.getName().equals(name)) return tclass;
        }
        return INVALID_CLASS;
    }

    @Override
    public int compareTo(Object o) {
        TokenClass other = (TokenClass) o;
        if (other.id == this.id) return 0;
        return this.id - other.id;
    }

    @Override
    public String toString() {
        return String.format("TokenClass{%d, %s, %s}", id, regex, name);
    }
}
