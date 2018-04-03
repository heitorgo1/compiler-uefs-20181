package br.uefs.compiler.util.automata;

import br.uefs.compiler.util.Node;
import br.uefs.compiler.util.RegexTree;
import br.uefs.compiler.util.RegexUtils;

import java.util.*;
import java.util.concurrent.SynchronousQueue;

public class NFA implements Cloneable {
    public static final String EPS = "EPS";
    public static final int EMPTY_STATE = -1;
    private static int RUNNING_ID = 0;

    private State start;
    private List<State> acceptingStates;

    public NFA() {
        acceptingStates = new ArrayList<>();
    }

    public State getStart() {
        return this.start;
    }

    public List<State> getAccpetingStates() {
        return this.acceptingStates;
    }

    private static int getNewId() {
        return RUNNING_ID++;
    }

    public void setStart(State start) {
        this.start = start;
    }

    public void setAcceptingStates(List<State> acceptingStates) {
        this.acceptingStates = acceptingStates;
    }

    public void addAcceptingState(State st) {
        acceptingStates.add(st);
    }

    public void setStateTag(StateTag tag) {
        for (State st : acceptingStates) {
            st.setStateTag(tag);
        }
    }

    public static NFA ofSingleValue(String input) {
        State start = new State(getNewId());
        State end = new State(getNewId(), true);

        start.addLink(input, end);

        NFA nfa = new NFA();
        nfa.setStart(start);
        nfa.addAcceptingState(end);

        return nfa;
    }

    public static NFA ofStar(NFA target) {
        State start = new State(getNewId());
        State end = new State(getNewId(), true);

        NFA nfa = new NFA();

        start.addLink(EPS, target.getStart());
        start.addLink(EPS, end);
        nfa.setStart(start);
        nfa.addAcceptingState(end);
        target.getAccpetingStates().get(0).setNotFinal();
        target.getAccpetingStates().get(0).addLink(EPS, end);
        target.getAccpetingStates().get(0).addLink(EPS, target.getStart());

        return nfa;
    }

    public static NFA ofUnion(NFA target1, NFA target2) {
        State start = new State(getNewId());
        State end = new State(getNewId(), true);

        NFA nfa = new NFA();

        nfa.setStart(start);
        nfa.addAcceptingState(end);

        start.addLink(EPS, target1.getStart());
        start.addLink(EPS, target2.getStart());
        target1.getAccpetingStates().get(0).setNotFinal();
        target2.getAccpetingStates().get(0).setNotFinal();
        target1.getAccpetingStates().get(0).addLink(EPS, end);
        target2.getAccpetingStates().get(0).addLink(EPS, end);

        return nfa;
    }

    public static NFA ofConcat(NFA target1, NFA target2) {

        NFA nfa = new NFA();

        nfa.setStart(target1.getStart());
        nfa.addAcceptingState(target2.getAccpetingStates().get(0));

        target1.getAccpetingStates().get(0).setNotFinal();
        target1.getAccpetingStates().get(0).setLinks(target2.getStart().getLinks());

        return nfa;
    }

    public static NFA ofExclamation(NFA target) {
        State start = new State(getNewId());
        State end = new State(getNewId(), true);

        NFA nfa = new NFA();

        start.addLink(EPS, target.getStart());
        start.addLink(EPS, end);
        nfa.setStart(start);
        nfa.addAcceptingState(end);
        target.getAccpetingStates().get(0).setNotFinal();
        target.getAccpetingStates().get(0).addLink(EPS, end);

        return nfa;
    }

    public static NFA mergeNFAs(List<NFA> nfaList) {
        State start = new State(getNewId());

        NFA nfa = new NFA();
        nfa.setStart(start);

        for (NFA n : nfaList) {
            start.addLink(EPS, n.getStart());

            for (State acSt : n.getAccpetingStates()) {
                nfa.addAcceptingState(acSt);
            }
        }
        return nfa;
    }

    public static NFA fromRegexTree(RegexTree tree, StateTag tag) throws Exception {
        Iterator<Node> nodes = tree.iteratorPostorder();
        Stack<NFA> subexpressions = new Stack<>();

        System.out.format("Building NFA from Regex Tree...\n");

        while (nodes.hasNext()) {
            String value = nodes.next().getValue();

            if (!RegexUtils.isOperator(value)) {
                NFA nfa = NFA.ofSingleValue(value);
                subexpressions.push(nfa);
            } else {
                switch (value) {
                    case "*":
                        NFA targetStar = subexpressions.pop();
                        subexpressions.push(NFA.ofStar(targetStar));
                        break;
                    case "?":
                        NFA targetExp = subexpressions.pop();
                        subexpressions.push(NFA.ofExclamation(targetExp));
                        break;
                    case "|":
                        NFA targetUnion2 = subexpressions.pop();
                        NFA targetUnion1 = subexpressions.pop();
                        subexpressions.push(NFA.ofUnion(targetUnion1, targetUnion2));
                        break;
                    case ".":
                        NFA targetConcat2 = subexpressions.pop();
                        NFA targetConcat1 = subexpressions.pop();
                        subexpressions.push(NFA.ofConcat(targetConcat1, targetConcat2));
                        break;
                    default:
                        throw new Exception(String.format("Unknown operator '%s'", value));
                }
            }
        }

        NFA res = subexpressions.pop();
        res.setStateTag(tag);
        return res;
    }

    public TransitionTable asTransitionTable() {
        Map<Integer, Map<String, Set<Integer>>> map = new Hashtable<>();
        Map<Integer, StateTag> tags = new Hashtable<>();

        ArrayList<State> q = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        q.add(this.start);

        while (!q.isEmpty()) {
            State cur = q.remove(0);
            if (cur.isFinal())
                tags.put(cur.getId(), cur.getTag());
            visited.add(cur.getId());

            if (map.get(cur.getId()) == null) {
                map.put(cur.getId(), new Hashtable<>());
            }

            for (Map.Entry<String, List<State>> v : cur.getLinks().entrySet()) {

                if (map.get(cur.getId()).get(v.getKey()) == null) {
                    map.get(cur.getId()).put(v.getKey(), new HashSet<>());
                }

                for (State st : v.getValue()) {
                    map.get(cur.getId()).get(v.getKey()).add(st.getId());
                    if (!visited.contains(st.getId())) {
                        q.add(st);
                    }
                }
            }
        }

        Set<Integer> acceptIds = new HashSet<>();

        for (State st : acceptingStates) {
            acceptIds.add(st.getId());
        }


        return new TransitionTable(this.start.getId(), acceptIds, map, tags);
    }

    public void printNFA() {

        Set<Integer> visited = new HashSet<>();
        ArrayList<State> q = new ArrayList<>();

        q.add(this.start);

        System.out.println(this.start.getId() + " " + this.getAccpetingStates().get(0).getId());
        while (!q.isEmpty()) {
            State cur = q.remove(0);
            visited.add(cur.getId());

            System.out.print(String.format("%d(%s) ", cur.getId(), cur.isFinal()));
            for (Map.Entry<String, List<State>> v : cur.getLinks().entrySet()) {
                System.out.print(String.format("--[%s]-->", v.getKey()));

                for (State st : v.getValue()) {
                    System.out.print(st.getId() + " ");
                    if (!visited.contains(st.getId()))
                        q.add(st);
                }
                System.out.println();
            }

        }

    }
}
