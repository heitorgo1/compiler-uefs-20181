package br.uefs.compiler;

import br.uefs.compiler.util.Node;
import br.uefs.compiler.util.RegexTree;
import br.uefs.compiler.util.automata.Automata;
import br.uefs.compiler.util.automata.NFA;
import br.uefs.compiler.util.automata.TransitionTable;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new StringReader("Hello World"));
        char[] buf = new char[256];
//        System.out.println(br.read(buf, 0, 5));
//        System.out.println(new String(buf));
//        System.out.println(br.read(buf, 0, 5));
//        System.out.println(new String(buf));
//        System.out.println(br.read(buf, 0, 5));
//        System.out.println(new String(buf));

//        RegexTree tree = RegexTree.fromRegex("\\l(\\l|\\d|_)*");
//        RegexTree tree = RegexTree.fromRegex("\"(\\l|\\d|\\y|\\\")*\"");
//        RegexTree tree = RegexTree.fromRegex("(a|b)*abb");
//        RegexTree tree = RegexTree.fromRegex("/\\*\\l*\\*/");
//        RegexTree tree = RegexTree.fromRegex("const");
        RegexTree tree = RegexTree.fromRegex("(-?\\s*\\d\\d*)(\\.\\d\\d*)?");
//        RegexTree tree = RegexTree.fromRegex("(a|b)*a");
        tree.printInorder();
        for (Iterator<Node> it = tree.iteratorPostorder(); it.hasNext(); ) {
            Node no = it.next();
            System.out.print(no.getValue() + " ");
        }

        NFA nfa = NFA.fromRegexTree(tree);
        nfa.printNFA();
        System.out.println("-------------");
        TransitionTable nfaTransTable = nfa.asTransitionTable();
        nfaTransTable.print();
        System.out.println("-------------");
        TransitionTable dfaTransTable = nfaTransTable.subsetConstructionDFA();
        dfaTransTable.print();

        Automata automata = Automata.fromRegex("(-?\\s*\\d\\d*)(\\.\\d\\d*)?");

        String s = new String("2341523.213");
        System.out.print(automata.getCurrentState() + " ");
        for (char c : s.toCharArray()) {
            automata.forward(Character.toString(c));
            System.out.print(automata.getCurrentState() + " ");

        }
        System.out.println();
        System.out.println(automata.isAccepting());
    }
}
