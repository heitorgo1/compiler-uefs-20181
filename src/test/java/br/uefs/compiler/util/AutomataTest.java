package br.uefs.compiler.util;

import br.uefs.compiler.util.automata.Automata;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AutomataTest {

    private boolean automataCheck(Automata automata, String input) {
        automata.reset();
        for (char c : input.toCharArray()) {
            automata.forward(Character.toString(c));
        }
        return automata.isAccepting();
    }

    @Test
    public void reservedWordMatchTest() throws Exception {
        String ifRegex = "if";
        String returnRegex = "return";

        Automata ifAutomata = Automata.fromRegex(ifRegex);
        Automata returnAutomata = Automata.fromRegex(returnRegex);

        String rightIf = "if", wrongIf1 = "pif", wrongIf2 = "i";
        String rightReturn = "return", wrongReturn1 = "returne", wrongReturn2 = "eturn";

        Assert.assertTrue(automataCheck(ifAutomata, rightIf));
        Assert.assertFalse(automataCheck(ifAutomata, wrongIf1));
        Assert.assertFalse(automataCheck(ifAutomata, wrongIf2));

        Assert.assertTrue(automataCheck(returnAutomata, rightReturn));
        Assert.assertFalse(automataCheck(returnAutomata, wrongReturn1));
        Assert.assertFalse(automataCheck(returnAutomata, wrongReturn2));
    }

    @Test
    public void identifierMatchTest() throws Exception {
        String regex = "\\l(\\l|\\d|_)*";

        Automata automata = Automata.fromRegex(regex);

        String id1 = "a", id2 = "abc", id3 = "abd2", id4 = "abc_def",
                wid1 = "1sad", wid2 = "a_b?", wid3 = "a.&c", wid4 = " abcs";

        Assert.assertTrue(automataCheck(automata, id1));
        Assert.assertTrue(automataCheck(automata, id2));
        Assert.assertTrue(automataCheck(automata, id3));
        Assert.assertTrue(automataCheck(automata, id4));

        Assert.assertFalse(automataCheck(automata, wid1));
        Assert.assertFalse(automataCheck(automata, wid2));
        Assert.assertFalse(automataCheck(automata, wid3));
        Assert.assertFalse(automataCheck(automata, wid4));
    }

    @Test
    public void numberMatchTest() throws Exception {
        String regex = "(-)?\\s*\\d\\d*(\\.\\d(\\d)*)?";

        Automata automata = Automata.fromRegex(regex);

        List<Pair<String, Boolean>> tests = Arrays.asList(
                new Pair<>("-12312312", true), new Pair<>("-1", true),
                new Pair<>("1", true), new Pair<>("234", true),
                new Pair<>("123.1", true), new Pair<>("12312.521421", true),
                new Pair<>("-12321.12", true), new Pair<>("00123", true),
                new Pair<>("-   12312", true), new Pair<>("- 123.41", true),
                new Pair<>("abc", false), new Pair<>("-asbd", false),
                new Pair<>("12321a213", false), new Pair<>("123.41a", false),
                new Pair<>("123.b41", false), new Pair<>("312 123", false),
                new Pair<>("?2132", false), new Pair<>("12+23", false)
        );

        for (Pair<String, Boolean> pair : tests) {
            Assert.assertEquals(pair.getValue(), automataCheck(automata, pair.getKey()));
        }
    }

    @Test
    public void arithmeticMatchTest() throws Exception {
        String regex = "+|-|\\*|/|(++)|(--)";

        Automata automata = Automata.fromRegex(regex);

        List<Pair<String, Boolean>> tests = Arrays.asList(
                new Pair<>("+", true), new Pair<>("-", true),
                new Pair<>("/", true), new Pair<>("*", true),
                new Pair<>("++", true), new Pair<>("--", true),
                new Pair<>("abc", false), new Pair<>("**", false),
                new Pair<>("123", false), new Pair<>("+++", false),
                new Pair<>("//", false), new Pair<>("---", false),
                new Pair<>("1+1", false), new Pair<>("2/3", false)
        );

        for (Pair<String, Boolean> pair : tests) {
            Assert.assertEquals(pair.getKey(), pair.getValue(), automataCheck(automata, pair.getKey()));
        }
    }

    @Test
    public void relationalMatchTest() throws Exception {
        String regex = "(!=)|(==)|<|(<=)|>|(>=)|=";

        Automata automata = Automata.fromRegex(regex);

        List<Pair<String, Boolean>> tests = Arrays.asList(
                new Pair<>("!=", true), new Pair<>("==", true),
                new Pair<>("<", true), new Pair<>("<=", true),
                new Pair<>(">", true), new Pair<>(">=", true), new Pair<>("=", true),
                new Pair<>("abc", false), new Pair<>("**", false),
                new Pair<>("123", false), new Pair<>("+++", false),
                new Pair<>("//", false), new Pair<>("---", false),
                new Pair<>("1+1", false), new Pair<>("2/3", false)
        );

        for (Pair<String, Boolean> pair : tests) {
            Assert.assertEquals(pair.getKey(), pair.getValue(), automataCheck(automata, pair.getKey()));
        }
    }

    @Test
    public void logicMatchTest() throws Exception {
        String regex = "!|(&&)|(\\|\\|)";

        Automata automata = Automata.fromRegex(regex);

        List<Pair<String, Boolean>> tests = Arrays.asList(
                new Pair<>("!", true), new Pair<>("&&", true),
                new Pair<>("||", true),
                new Pair<>("abc", false), new Pair<>("**", false),
                new Pair<>("123", false), new Pair<>("+++", false),
                new Pair<>("//", false), new Pair<>("---", false),
                new Pair<>("1+1", false), new Pair<>("2/3", false)
        );

        for (Pair<String, Boolean> pair : tests) {
            Assert.assertEquals(pair.getKey(), pair.getValue(), automataCheck(automata, pair.getKey()));
        }
    }

    @Test
    public void lineCommentMatchTest() throws Exception {
        String regex = "//(\\y|\\s)*\n";

        Automata automata = Automata.fromRegex(regex);

        List<Pair<String, Boolean>> tests = Arrays.asList(
                new Pair<>("//!\n", true), new Pair<>("//\n", true),
                new Pair<>("//asdasdas\n", true), new Pair<>("// sjd sdjha hs\n", true),
                new Pair<>("/abc", false), new Pair<>("/**\n", false),
                new Pair<>("123", false), new Pair<>("+++", false),
                new Pair<>("//", false), new Pair<>("---", false),
                new Pair<>("1+1", false), new Pair<>("2/3", false)
        );

        for (Pair<String, Boolean> pair : tests) {
            Assert.assertEquals(pair.getKey(), pair.getValue(), automataCheck(automata, pair.getKey()));
        }
    }

    @Test
    public void blockCommentMatchTest() throws Exception {
        String regex = "/\\*(\\y|\\s|\"|\\*)*\\*/";

        Automata automata = Automata.fromRegex(regex);

        List<Pair<String, Boolean>> tests = Arrays.asList(
                new Pair<>("/* sad\n bsad\n */", true), new Pair<>("/*hello*/", true),
                new Pair<>("/******/", true), new Pair<>("/* // djash 2131 sa?!.\" // */", true),
                new Pair<>("/**\"***/", true), new Pair<>("/* // \"djash sa?!<>\".\" // */", true),
                new Pair<>("/abc", false), new Pair<>("/**\n", false),
                new Pair<>("123", false), new Pair<>("+++", false),
                new Pair<>("//", false), new Pair<>("---", false),
                new Pair<>("1+1", false), new Pair<>("2/3", false)
        );

        for (Pair<String, Boolean> pair : tests) {
            Assert.assertEquals(pair.getKey(), pair.getValue(), automataCheck(automata, pair.getKey()));
        }
    }

    @Test
    public void delimiterMatchTest() throws Exception {
        String regex = ";|,|\\(|\\)|[|]|{|}|\\.";

        Automata automata = Automata.fromRegex(regex);

        List<Pair<String, Boolean>> tests = Arrays.asList(
                new Pair<>(";", true), new Pair<>(",", true),
                new Pair<>("(", true), new Pair<>(")", true),
                new Pair<>("[", true), new Pair<>("]", true),
                new Pair<>("{", true), new Pair<>("}", true),
                new Pair<>(".", true),
                new Pair<>("/abc", false), new Pair<>("/**\n", false),
                new Pair<>("123", false), new Pair<>("+++", false),
                new Pair<>("//", false), new Pair<>("---", false),
                new Pair<>("1+1", false), new Pair<>("2/3", false)
        );

        for (Pair<String, Boolean> pair : tests) {
            Assert.assertEquals(pair.getKey(), pair.getValue(), automataCheck(automata, pair.getKey()));
        }
    }

    @Test
    public void stringMatchTest() throws Exception {
        String regex = "\"(\\l|\\d|\\y|(\\\\\"))*\"";

        Automata automata = Automata.fromRegex(regex);

        List<Pair<String, Boolean>> tests = Arrays.asList(
                new Pair<>("\"blalba\"", true), new Pair<>("\" sa sajd ks\"", true),
                new Pair<>("\" 213 231 23123\"", true), new Pair<>("\" \\\"\\\"\\\"\"", true),
                new Pair<>("\"?!@#@!$#@%(*#@(<>><>\"", true), new Pair<>("\"[]{}}},.!;\"", true),
                new Pair<>("\"{\"", true), new Pair<>("\"\"", true),
                new Pair<>("\".\"", true),
                new Pair<>("/abc", false), new Pair<>("/**\n", false),
                new Pair<>("123", false), new Pair<>("+++", false),
                new Pair<>("//", false), new Pair<>("---", false),
                new Pair<>("1+1", false), new Pair<>("2/3", false)
        );

        for (Pair<String, Boolean> pair : tests) {
            Assert.assertEquals(pair.getKey(), pair.getValue(), automataCheck(automata, pair.getKey()));
        }
    }
}
