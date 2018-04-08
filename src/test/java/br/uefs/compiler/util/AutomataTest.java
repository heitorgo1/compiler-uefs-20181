package br.uefs.compiler.util;

import br.uefs.compiler.util.automata.Automata;
import br.uefs.compiler.util.automata.AutomataSimulator;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AutomataTest {

    private boolean automataCheck(AutomataSimulator simulator, String input) throws Exception {
        simulator.reset();
        for (Character c : input.toCharArray()) {
            simulator.next(c);
        }
        return simulator.isAccepting();
    }

    @Test
    public void reservedWordMatchTest() throws Exception {
        String ifRegex = "if";
        String returnRegex = "return";

        AutomataSimulator ifAutomata = new AutomataSimulator(Automata.buildDFAFromRegex(ifRegex));
        AutomataSimulator returnAutomata = new AutomataSimulator(Automata.buildDFAFromRegex(returnRegex));

        List<Pair<String, Boolean>> ifTests = Arrays.asList(
                new Pair<>("if", true), new Pair<>("pif", false), new Pair<>("i", false)
        );

        List<Pair<String, Boolean>> returnTests = Arrays.asList(
                new Pair<>("return", true), new Pair<>("returne", false), new Pair<>("eturn", false)
        );

        for (Pair<String, Boolean> pair : ifTests) {
            Assert.assertEquals(pair.getValue(), automataCheck(ifAutomata, pair.getKey()));
        }

        for (Pair<String, Boolean> pair : returnTests) {
            Assert.assertEquals(pair.getValue(), automataCheck(returnAutomata, pair.getKey()));
        }

    }

    @Test
    public void identifierMatchTest() throws Exception {
        String regex = "\\l(\\l|\\d|_)*";

        AutomataSimulator automata = new AutomataSimulator(Automata.buildDFAFromRegex(regex));

        List<Pair<String, Boolean>> tests = Arrays.asList(
                new Pair<>("a", true), new Pair<>("abc", true),
                new Pair<>("abd2", true), new Pair<>("abc_def", true),
                new Pair<>("1sad", false), new Pair<>("a_b?", false),
                new Pair<>("a.&c", false), new Pair<>(" abcs", false)
                );


        for (Pair<String, Boolean> pair : tests) {
            Assert.assertEquals(pair.getValue(), automataCheck(automata, pair.getKey()));
        }
    }

    @Test
    public void numberMatchTest() throws Exception {
        String regex = "(-)?\\s*\\d\\d*(\\.\\d(\\d)*)?";

        AutomataSimulator automata = new AutomataSimulator(Automata.buildDFAFromRegex(regex));

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

        AutomataSimulator automata = new AutomataSimulator(Automata.buildDFAFromRegex(regex));

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

        AutomataSimulator automata = new AutomataSimulator(Automata.buildDFAFromRegex(regex));

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

        AutomataSimulator automata = new AutomataSimulator(Automata.buildDFAFromRegex(regex));

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

        AutomataSimulator automata = new AutomataSimulator(Automata.buildDFAFromRegex(regex));

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

        AutomataSimulator automata = new AutomataSimulator(Automata.buildDFAFromRegex(regex));

        List<Pair<String, Boolean>> tests = Arrays.asList(
                new Pair<>("/* sad\n bsad\n */", true), new Pair<>("/*hello*/", true),
                new Pair<>("/******/", true), new Pair<>("/* // djash 2131 sa?!.\" // */", true),
                new Pair<>("/**\"***/", true), new Pair<>("/* // \"djash sa?!<>\".\" // */", true),
                new Pair<>("/abc", false), new Pair<>("/**\n", false),
                new Pair<>("123", false), new Pair<>("+++", false),
                new Pair<>("//", false), new Pair<>("---", false),
                new Pair<>("1+1", false), new Pair<>("2/3", false),
                new Pair<>("/*int abc = 2+2;\nstring */sa_sb != \"sdjasjdklsjad asj hsd\"\n", false)
        );

        for (Pair<String, Boolean> pair : tests) {
            Assert.assertEquals(pair.getKey(), pair.getValue(), automataCheck(automata, pair.getKey()));
        }
    }

    @Test
    public void delimiterMatchTest() throws Exception {
        String regex = ";|,|\\(|\\)|[|]|{|}|\\.";

        AutomataSimulator automata = new AutomataSimulator(Automata.buildDFAFromRegex(regex));

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

        AutomataSimulator automata = new AutomataSimulator(Automata.buildDFAFromRegex(regex));

        List<Pair<String, Boolean>> tests = Arrays.asList(
                new Pair<>("\"blalba\"", true), new Pair<>("\" sa sajd ks\"", true),
                new Pair<>("\" 213 231 23123\"", true), new Pair<>("\" \\\"\\\"\\\"\"", true),
                new Pair<>("\"?!@#@!$#@%(*#@(<>><>\"", true), new Pair<>("\"[]{}}},.!;\"", true),
                new Pair<>("\"{\"", true), new Pair<>("\"\"", true),
                new Pair<>("\".\"", true),
                new Pair<>("/abc", false), new Pair<>("/**\n", false),
                new Pair<>("123", false), new Pair<>("+++", false),
                new Pair<>("//", false), new Pair<>("---", false),
                new Pair<>("1+1", false), new Pair<>("2/3", false),
                new Pair<>("\"\n\n\n\"", false)
        );

        for (Pair<String, Boolean> pair : tests) {
            Assert.assertEquals(pair.getKey(), pair.getValue(), automataCheck(automata, pair.getKey()));
        }
    }
}
