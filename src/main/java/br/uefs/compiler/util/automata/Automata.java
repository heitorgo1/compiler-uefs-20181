package br.uefs.compiler.util.automata;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Automata abstraction for NFAs and DFAs
 */
public class Automata implements Serializable{

    /**
     * Start State of the Automata
     */
    protected State startState;

    public Automata() {
    }

    public Automata(State startState) {
        this.startState = startState;
    }

    public State getStartState() {
        return startState;
    }

    public StateSet getFinalStates() {
        StateSet ss = new StateSet();
        for (State st : getAllStates()) {
            if (st.isFinal()) ss.add(st);
        }
        return ss;
    }

    public StateSet getAllStates() {
        StateSet allStates = StateSet.of(startState);
        Stack<State> stack = new Stack<>();
        stack.push(startState);

        while (!stack.isEmpty()) {
            State u = stack.pop();

            for (State v : u.nextStates()) {
                if (!allStates.contains(v)) {
                    allStates.add(v);
                    stack.push(v);
                }
            }
        }
        return allStates;
    }

    public Set<Character> alphabet() {
        Set<Character> alphabet = new HashSet<>();
        Set<State> visited = new HashSet<>();
        Stack<State> stack = new Stack<>();
        stack.push(startState);
        visited.add(startState);

        while (!stack.isEmpty()) {
            State u = stack.pop();

            alphabet.addAll(u.possibleInputs());

            for (State v : u.nextStates()) {
                if (!visited.contains(v)) {
                    stack.push(v);
                    visited.add(v);
                }
            }
        }
        return alphabet;
    }

    /**
     * Build DFA directly from Regular Expression.
     *
     * @param expression
     * @return new DFA
     * @throws Exception
     */
    public static DFA buildDFAFromRegex (String expression) throws Exception {
        NFA nfa = NFA.fromRegexExpression(expression, o -> 0, o -> Integer.MAX_VALUE);
        return nfa.toDFA();
    }

}
