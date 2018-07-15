package br.uefs.compiler.lexer;

import br.uefs.compiler.lexer.token.ReservedWords;
import br.uefs.compiler.lexer.token.Token;
import br.uefs.compiler.lexer.token.TokenClass;
import br.uefs.compiler.lexer.token.TokenRecognizerAutomataFactory;
import br.uefs.compiler.util.automata.DFA;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

public class LexerTest {

    static Lexer lexer;

    @BeforeClass
    public static void before() throws Exception {
        DFA dfa = TokenRecognizerAutomataFactory.getDFAFromClassList(TokenClass.CLASSES);
        lexer = new Lexer();
        lexer.withAutomata(dfa);
    }

    public void runTokenCheck(String clazz, String value) throws Exception {
        Token token = lexer.nextToken();
        Assert.assertTrue(String.format("Assert TokenClass for: %s", value),
                token.toString().contains(clazz));
        Assert.assertTrue(String.format("Assert Token Value for: %s", value),
                token.toString().contains(value.replace("\n","\\n")));
    }

    public void runTokenCheck(List<String> classes, List<String> values) throws Exception {
        for (int i = 0; i < values.size(); i++) {
            Token token = lexer.nextToken();

            Assert.assertTrue(String.format("Assert TokenClass for: %s\npos: %d", values.get(i), i),
                    token.toString().contains(classes.get(i)));
            Assert.assertTrue(String.format("Assert Token Value for: %s\npos: %d", values.get(i), i),
                    token.toString().contains(values.get(i).replace("\n","\\n")));
        }
    }

    @Test
    public void reservedWordsTest() throws Exception {
        for (String word : ReservedWords.RESERVED_WORDS) {
            lexer.withReader(new StringReader(word));
            runTokenCheck("PALAVRARESERVADA", word);
        }
    }

    @Test
    public void identifiersTest() throws Exception {
        List<String> ids = Arrays.asList("sdjka", "a", "sd_sad", "sddx_v_s__", "aaabbb");

        for (String id : ids) {
            lexer.withReader(new StringReader(id));
            runTokenCheck("IDENTIFICADOR", id);
        }
    }

    @Test
    public void numbersTest() throws Exception {
        List<String> nums = Arrays.asList("12312", "1", "-2", "- 2321", "-2.3", "1.52341341", "1.2", "   45563");

        for (String num : nums) {
            lexer.withReader(new StringReader(num));
            runTokenCheck("NUMERO", num);
        }
    }

    @Test
    public void arOperatorsTest() throws Exception {
        List<String> ops = Arrays.asList("+", "-", "++", "--", "/", "*");

        for (String op : ops) {
            lexer.withReader(new StringReader(op));
            runTokenCheck("OPERADORARITMETICO", op);
        }
    }

    @Test
    public void delimitersTest() throws Exception {
        List<String> dels = Arrays.asList(";", ",", "{", "}", "(", ")", "[", "]");

        for (String del : dels) {
            lexer.withReader(new StringReader(del));
            runTokenCheck("DELIMITADOR", del);
        }
    }

    @Test
    public void lineCommentTest() throws Exception {
        List<String> lines = Arrays.asList("//skadjaksldj", "//", "//21312", "//??<><>{~p", "//   ");

        for (String line : lines) {
            lexer.withReader(new StringReader(line));
            runTokenCheck("COMENTARIOLINHA", line);
        }
    }

    @Test
    public void blockCommentTest() throws Exception {
        List<String> blocks = Arrays.asList("/*skadjaksldj*/", "/**/", "/*  */", "/**\n*\n*\n*/", "/* 13 wjeq i \t\n */");

        for (String block : blocks) {
            lexer.withReader(new StringReader(block));
            runTokenCheck("COMENTARIOBLOCO", block);
        }
    }

    @Test
    public void codeBlock1Test() throws Exception {
        String code = "int ab = 1234;";
        lexer.withReader(new StringReader(code));

        List<String> classes = Arrays.asList(
                "PALAVRARESERVADA", "ESPACO", "IDENTIFICADOR", "ESPACO", "OPERADORRELACIONAL", "NUMERO",
                "DELIMITADOR"
        );
        List<String> values = Arrays.asList(
                "int", " ", "ab", " ", "=", " 1234", ";"
        );

        runTokenCheck(classes, values);
        Assert.assertNull(lexer.nextToken());
    }

    @Test
    public void codeBlock2Test() throws Exception {
        String code = "string s_s = \"hello World!\"; {}\n";
        lexer.withReader(new StringReader(code));

        List<String> classes = Arrays.asList(
                "PALAVRARESERVADA", "ESPACO", "IDENTIFICADOR", "ESPACO", "OPERADORRELACIONAL", "ESPACO",
                "CADEIACARECTERES", "DELIMITADOR", "ESPACO", "DELIMITADOR", "DELIMITADOR", "ESPACO"
        );
        List<String> values = Arrays.asList(
                "string", " ", "s_s", " ", "=", " ", "\"hello World!\"", ";", " ", "{", "}", "\n"
        );

        runTokenCheck(classes, values);

        Assert.assertNull(lexer.nextToken());
    }

    @Test
    public void codeBlock3Test() throws Exception {
        String code = "a-2";
        lexer.withReader(new StringReader(code));

        List<String> classes = Arrays.asList(
                "IDENTIFICADOR", "NUMERO"
        );
        List<String> values = Arrays.asList(
                "a", "-2"
        );

        runTokenCheck(classes, values);

        Assert.assertNull(lexer.nextToken());
    }

    @Test
    public void codeBlock4Test() throws Exception {
        String code = "/* my comment's\t*/ string ss";
        lexer.withReader(new StringReader(code));

        List<String> classes = Arrays.asList(
                "COMENTARIOBLOCO", "ESPACO", "PALAVRARESERVADA", "ESPACO", "IDENTIFICADOR"
        );
        List<String> values = Arrays.asList(
                "/* my comment's\t*/", " ", "string", " ", "ss"
        );

        runTokenCheck(classes, values);

        Assert.assertNull(lexer.nextToken());
    }

    @Test
    public void codeBlock5Test() throws Exception {
        String code = "11.11@+AB+5.0.10";
        lexer.withReader(new StringReader(code));

        List<String> classes = Arrays.asList(
                "NUMERO", "ERROR_VALORINESPERADO", "OPERADORARITMETICO", "IDENTIFICADOR", "OPERADORARITMETICO",
                "NUMERO", "DELIMITADOR", "NUMERO"
        );
        List<String> values = Arrays.asList(
                "11.11", "@", "+", "AB", "+", "5.0", ".", "10"
        );

        runTokenCheck(classes, values);

        Assert.assertNull(lexer.nextToken());
    }

    @Test
    public void codeBlock6Test() throws Exception {
        String code = "A-- - -2";
        lexer.withReader(new StringReader(code));

        List<String> classes = Arrays.asList(
                "IDENTIFICADOR", "OPERADORARITMETICO", "ESPACO","OPERADORARITMETICO", "ESPACO", "NUMERO"
        );
        List<String> values = Arrays.asList(
                "A", "--", " ", "-", " ", "-2"
        );

        runTokenCheck(classes, values);

        Assert.assertNull(lexer.nextToken());
    }

    @Test
    public void codeBlock7Test() throws Exception {
        String code = "(f<=-10)";
        lexer.withReader(new StringReader(code));

        List<String> classes = Arrays.asList(
                "DELIMITADOR", "IDENTIFICADOR", "OPERADORRELACIONAL","NUMERO", "DELIMITADOR"
        );
        List<String> values = Arrays.asList(
                "(", "f", "<=", "-10", ")"
        );

        runTokenCheck(classes, values);

        Assert.assertNull(lexer.nextToken());
    }
}
