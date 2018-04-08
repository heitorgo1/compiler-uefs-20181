package br.uefs.compiler;

import br.uefs.compiler.lexer.Lexer;

import java.io.StringReader;

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
//        Lexer lexer = new Lexer();
//        lexer.scan();

//        Regex rx = new Regex("\\\\\"");
//        rx.toPostfix();
//
//        System.out.println(String.join("|", "a", "b", "c"));
//
//        for (int i : IntStream.rangeClosed('a', 'z').toArray()) {
//            System.out.println((char)i);
//        }
//
//        List<String> list = new ArrayList<>();
//        IntStream range1 = IntStream.rangeClosed('a','z');
//        IntStream range2 = IntStream.rangeClosed('A','Z');
//        for (int i : IntStream.concat(range1, range2).toArray()) {
//            list.add(Character.toString((char)i));
//        }
//
//        System.out.println(SpecialCharacter.getExpression('l'));
//        System.out.println(SpecialCharacter.getExpression('d'));
//        System.out.print(SpecialCharacter.getExpression('s'));
//
//        Character c = new Character((char)127);
//        System.out.println("("+Character.forDigit(9, 10)+")");
//
//        String expression = "(-)?\\s*\\d\\d*(\\.\\d(\\d)*)?";
//
//        Regex rx = new Regex(expression);
//        System.out.println(expression);
//        System.out.println(rx.expandConcat(expression));
//        System.out.println(rx.expandSpecialCharacters(expression));
//        NFA nfa = NFA.fromRegexExpression(expression, new Comparable() {
//            @Override
//            public int compareTo(Object o) {
//                return 0;
//            }
//
//            @Override
//            public String toString() {
//                return "";
//            }
//        });
//
//        System.out.print(nfa);
//        System.out.print(nfa.toDFA());
//        AutomataSimulator simulator = new AutomataSimulator(nfa.toDFA());
//
//        String input = "";
//
//        BufferedReader bf = new BufferedReader(new StringReader(input));
//
//        int i;
//        while ((i = bf.read()) != -1) {
//            Character c =(char)i;
//            simulator.next(c);
//        }
//        System.out.println(simulator.isAccepting());

        Lexer l = new Lexer(new StringReader("sdasd dae string;/*@#@%@@# @@dsakjaskj \t*/"));
        l.scan();
    }
}
