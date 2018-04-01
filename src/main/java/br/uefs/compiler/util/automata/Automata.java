package br.uefs.compiler.util.automata;

import br.uefs.compiler.util.RegexTree;

import java.util.ArrayList;
import java.util.List;
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
        return visitedStates.get(visitedStates.size() - 1);
    }

    public void reset() {
        visitedStates = new ArrayList<>();
        visitedStates.add(table.getStartId());
    }

    public boolean isAccepting() {
        int curId = getCurrentState();

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
            visitedStates.add(-1);
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
        NFA nfa = NFA.fromRegexTree(rxTree);
        System.out.println("NFA Complete.");
        TransitionTable transTable = nfa.asTransitionTable();
        System.out.println("NFA Conversion to Transition Table Complete.");
        TransitionTable dfaTable = transTable.subsetConstructionDFA();
        System.out.println("DFA Transition Table Complete.");
        System.out.println("Automata ready.");
        return new Automata(dfaTable);
    }
}
