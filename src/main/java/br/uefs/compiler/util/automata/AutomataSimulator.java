package br.uefs.compiler.util.automata;

import java.util.ArrayList;
import java.util.List;

public class AutomataSimulator {

    private DFA automata;
    private State current;
    private State lastVisitedFinalState;

    public AutomataSimulator(Automata automata) throws Exception {
        if (NFA.class.isInstance(automata)) throw new Exception("NFA Simulation not implemented.");
        this.automata = DFA.class.cast(automata);
        reset();
    }

    public State getCurreState() {
        return current;
    }

    public void setCurrentState(State state) {
        this.current = state;
    }

    public void reset() {
        setCurrentState(automata.getStartState());
        this.lastVisitedFinalState = current.isFinal() ? current : State.REJECT_STATE;
    }

    public boolean hasNext(Character input) throws Exception {
        return !automata.move(current, input).equals(State.REJECT_STATE);
    }

    public <T extends Comparable> T getTag(Class<T> type) {
        return current.getTag(type);

    }

    public <T extends Comparable> T getAcceptingTag(Class<T> type) {
        return lastVisitedFinalState.getTag(type);
    }

    public boolean hasAcceptedInput() {
        return !lastVisitedFinalState.equals(State.REJECT_STATE);
    }

    public boolean isAccepting() {
        return current.isFinal();
    }

    public boolean next(Character input) throws Exception {
        current = automata.move(current, input);
        if (current.isFinal()) lastVisitedFinalState = current;
        if (current.equals(State.REJECT_STATE)) return false;
        return true;
    }

}
