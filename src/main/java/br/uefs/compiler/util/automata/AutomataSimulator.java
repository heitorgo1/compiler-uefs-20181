package br.uefs.compiler.util.automata;

/**
 * Simulator capable of moving through an Automata and telling
 * when it is accepting or rejecting the input.
 */
public class AutomataSimulator {

    /**
     * The automata to be simulated. Only DFA support.
     */
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

    /**
     * Move back to the start State of the automata.
     */
    public void reset() {
        setCurrentState(automata.getStartState());
        this.lastVisitedFinalState = current.isFinal() ? current : State.REJECT_STATE;
    }

    /**
     * Retrieve the tag of the last accepting State the simulator visited.
     *
     * @param type Class of the tag to be retrieved
     * @param <T> Tag tha extends Comparable
     * @return the tag as class T
     */
    public <T extends Comparable> T getAcceptingTag(Class<T> type) {
        return lastVisitedFinalState.getTag(type);
    }

    /**
     * Informs if the automata has accepted the input at some moment.
     *
     * @return hasAcceptedInput
     */
    public boolean hasAcceptedInput() {
        return !lastVisitedFinalState.equals(State.REJECT_STATE);
    }

    /**
     * Informs if the automata is currently accepting the input.
     *
     * @return isAccepting
     */
    public boolean isAccepting() {
        return current.isFinal();
    }

    /**
     * Try to move to the next State given an input Character.
     *
     * @param input the input Character
     * @return false if reached rejecting State, true otherwise
     * @throws Exception
     */
    public boolean next(Character input) throws Exception {
        current = automata.move(current, input);
        if (current.isFinal()) lastVisitedFinalState = current;
        if (current.equals(State.REJECT_STATE)) return false;
        return true;
    }

}
