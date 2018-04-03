package br.uefs.compiler;

import br.uefs.compiler.lexer.DoubleBufferReader;
import br.uefs.compiler.lexer.Lexer;
import br.uefs.compiler.util.RegexTree;
import br.uefs.compiler.util.automata.Automata;
import br.uefs.compiler.util.automata.NFA;
import br.uefs.compiler.util.automata.StateTag;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
//        BufferedReader br = new BufferedReader(new StringReader("Hello World"));
//        char[] buf = new char[256];
//        System.out.println(br.read(buf, 0, 5));
//        System.out.println(new String(buf));
//        System.out.println(br.read(buf, 0, 5));
//        System.out.println(new String(buf));
//        System.out.println(br.read(buf, 0, 5));
//        System.out.println(new String(buf));
//
//        RegexTree tree = RegexTree.fromRegex("\\l(\\l|\\d|_)*");
//        RegexTree tree = RegexTree.fromRegex("\"(\\l|\\d|\\y|\\\")*\"");
//        RegexTree tree = RegexTree.fromRegex("(a|b)*abb");
//        RegexTree tree = RegexTree.fromRegex("/\\*\\l*\\*/");
//        RegexTree tree = RegexTree.fromRegex("const");
//        RegexTree tree = RegexTree.fromRegex("(-?\\s*\\d\\d*)(\\.\\d\\d*)?");
//        RegexTree tree = RegexTree.fromRegex("(a|b)*a");
//        tree.printInorder();
//        for (Iterator<Node> it = tree.iteratorPostorder(); it.hasNext(); ) {
//            Node no = it.next();
//            System.out.print(no.getValue() + " ");
//        }
//
//        NFA nfa = NFA.fromRegexTree(tree);
//        nfa.printNFA();
//        System.out.println("-------------");
//        TransitionTable nfaTransTable = nfa.asTransitionTable();
//        nfaTransTable.print();
//        System.out.println("-------------");
//        TransitionTable dfaTransTable = nfaTransTable.subsetConstructionDFA();
//        dfaTransTable.print();
//
//        Automata automata = Automata.fromRegex("\\\\l(\\\\l|\\\\d|_)*");
//
//        String s = new String("consta");
//        System.out.print(automata.getCurrentState() + " ");
//        for (char c : s.toCharArray()) {
//            automata.forward(Character.toString(c));
//            System.out.print(automata.getCurrentState() + " ");
//
//        }
//        System.out.println();
//        System.out.println(automata.isAccepting());

//        DoubleBufferReader in = new DoubleBufferReader(new StringReader("12345678"));
//
//        System.out.println((char) in.read());
//        System.out.println((char) in.read());
//        System.out.println(in.getString());
//        System.out.println((char) in.read());
//        System.out.println((char) in.read());
//        System.out.println((char) in.read());
//        System.out.println((char) in.read());
//
//        System.out.println((char) in.read());
//        System.out.println((char) in.read());
//        System.out.println(in.read() == -1 ? "EOF" : "WHAT?");

//        RegexTree tree1 = RegexTree.fromRegex("(a|b)*abb");
//        RegexTree tree2 = RegexTree.fromRegex("/\\*\\l*\\*/");
//
//        NFA nfa1 = NFA.fromRegexTree(tree1, new StateTag() {
//            @Override
//            public int get() {
//                return 1;
//            }
//
//            @Override
//            public int compareTo(Object o) {
//                return 0;
//            }
//        });
//
//        NFA nfa2 = NFA.fromRegexTree(tree2, new StateTag() {
//            @Override
//            public int get() {
//                return 2;
//            }
//
//            @Override
//            public int compareTo(Object o) {
//                return 0;
//            }
//        });
//
//        NFA nfa3 = NFA.mergeNFAs(Arrays.asList(nfa1, nfa2));
//
//        for (Map.Entry<Integer, StateTag> entry : nfa3.asTransitionTable().subsetConstructionDFA().getTags().entrySet()) {
//            System.out.format("%d %s\n",entry.getKey(), entry.getValue().get());
//        }
//
        Lexer lexer = new Lexer();
        lexer.scan();
    }
}
