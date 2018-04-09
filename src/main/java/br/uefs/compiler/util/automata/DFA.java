package br.uefs.compiler.util.automata;

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
