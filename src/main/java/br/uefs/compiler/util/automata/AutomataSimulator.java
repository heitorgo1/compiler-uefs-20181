package br.uefs.compiler.util.automata;

import java.util.ArrayList;
import java.util.List;

public class AutomataSimulator {

    private DFA automata;
    private State current;

    private List<State> previousStates;

    public AutomataSimulator(Automata automata) throws Exception {
        if (NFA.class.isInstance(automata)) throw new Exception("NFA Simulation not implemented.");
        this.automata = DFA.class.cast(automata);
        this.previousStates = new ArrayList<>();
        this.current = automata.getStartState();
    }

    public State getCurreState() {
        return current;
    }

    public void setCurrentState(State state) {
        this.current = state;
    }

    public void reset() {
        setCurrentState(automata.getStartState());
        this.previousStates = new ArrayList<>();
    }

    public boolean hasNext(Character input) throws Exception {
        return !automata.move(current, input).equals(State.REJECT_STATE);
    }

    public <T extends Comparable> T getTag(Class<T> type) {
        return current.getTag(type);
    }

    public boolean isAccepting() {
        return current.isFinal();
    }

    public boolean next(Character input) throws Exception {
        previousStates.add(current);
        current = automata.move(current, input);
        if (current.equals(State.REJECT_STATE)) return false;
        return true;
    }

    public boolean back() {
        if (previousStates.isEmpty()) return false;
        current = previousStates.get(previousStates.size() - 1);
        previousStates.remove(previousStates.size() - 1);
        return true;
    }
}
