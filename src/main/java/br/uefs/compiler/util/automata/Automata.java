package br.uefs.compiler.util.automata;

import br.uefs.compiler.util.RegexTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Automata {

    private List<Integer> visitedStates;
    private TransitionTable table;

    public Automata(TransitionTable table) {
        this.table = table;
        this.visitedStates = new ArrayList<>();
        this.visitedStates.add(table.getStartId());
    }

    public int getCurrentState() {
        if (visitedStates.isEmpty()) return NFA.EMPTY_STATE;
        return visitedStates.get(visitedStates.size() - 1);
    }

    public void reset() {
        visitedStates = new ArrayList<>();
        visitedStates.add(table.getStartId());
    }


    public int getCurrentStateTagId() {
        return table.getTag(getCurrentState()).get();
    }

    public boolean isAccepting() {
        int curId = getCurrentState();
        if (curId == NFA.EMPTY_STATE) return false;

        return table.isAcceptingState(curId);
    }

    public boolean forward(String input) {
        int curId = getCurrentState();
        Set<Integer> nextStatesIds = table.move(curId, input);

        if (nextStatesIds.iterator().hasNext()) {
            // get only the first, works for DFA
            int nextStateId = nextStatesIds.iterator().next();
            visitedStates.add(nextStateId);
            return true;
        }
        else {
            visitedStates.add(NFA.EMPTY_STATE);
        }
        return false;
    }

    public boolean back() {
        if (visitedStates.isEmpty()) {
            return false;
        }
        visitedStates.remove(visitedStates.size() - 1);
        return true;
    }

    public static Automata fromRegex(String regex) throws Exception {
        RegexTree rxTree = RegexTree.fromRegex(regex);
        System.out.println("Regex Tree Complete.");
        NFA nfa = NFA.fromRegexTree(rxTree, new StateTag() {
            @Override
            public int get() {
                return 1;
            }

            @Override
            public int compareTo(Object o) {
                return 0;
            }
        });
        System.out.println("NFA Complete.");
        TransitionTable transTable = nfa.asTransitionTable();
        System.out.println("NFA Conversion to Transition Table Complete.");
        TransitionTable dfaTable = transTable.subsetConstructionDFA();
        for (Map.Entry<Integer, StateTag> entry : dfaTable.getTags().entrySet()) {
            System.out.format("%d %s\n",entry.getKey(), entry.getValue().get());
        }
        System.out.println("DFA Transition Table Complete.");
        System.out.println("Automata ready.");
        return new Automata(dfaTable);
    }

    public void print(){
        table.print();
    }
}
