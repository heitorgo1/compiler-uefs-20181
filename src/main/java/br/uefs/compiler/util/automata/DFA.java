package br.uefs.compiler.util.automata;

/**
 * Deterministic Finite Automata implementation
 */
public class DFA extends Automata {

    public DFA() {
        super();
    }

    public DFA(State startState) {
        super(startState);
    }

    public DFA withStartState(State start) {
        this.startState = start;
        return this;
    }

    /**
     * Given a State and an input, try to move to
     * the next State.
     *
     * @param state current State
     * @param input input Character of the transition
     * @return the next State
     * @throws Exception
     */
    public State move(State state, Character input) throws Exception {
        return state.move(input).getOnlyState();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DFA{\n");
        for (State st : getAllStates()) {
            if (st.equals(startState)) sb.append("*");
            sb.append(st.toString());
            sb.append("\n");
        }
        sb.append("}\n");
        return sb.toString();
    }
}
