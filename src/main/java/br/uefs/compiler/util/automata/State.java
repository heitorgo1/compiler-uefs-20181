package br.uefs.compiler.util.automata;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A state for Automata implementations.
 */
public class State implements Comparable, Serializable {
    /**
     * Global id counter to make each State unique
     */
    public static long RUNNING_ID = 0;
    /**
     * Special rejection State
     */
    public static final State REJECT_STATE = new State(-1);

    private long id;
    private boolean isFinal;
    /**
     * transitions possible by this state to (possibly) multiple states with a Character input
     */
    private Map<Character, StateSet> transitions;

    /**
     * Comparable tag to determine from which automata came the each State
     * when merging automatas.
     */
    private Comparable tag;

    public State() {
        this.id = RUNNING_ID++;
        this.isFinal = false;
        transitions = new Hashtable<>();
        tag = comp -> 0;
    }

    public State(Comparable tag) {
        this.id = RUNNING_ID++;
        this.isFinal = false;
        transitions = new Hashtable<>();
        this.tag = tag;
    }

    public State(Comparable tag, boolean isFinal) {
        this.id = RUNNING_ID++;
        this.isFinal = isFinal;
        transitions = new Hashtable<>();
        this.tag = tag;
    }

    public State(int id) {
        this.id = id;
        this.isFinal = false;
        transitions = new Hashtable<>();
        tag = comp -> 0;
    }

    public <T extends Comparable> T getTag(Class<T> type) {
        return type.cast(tag);
    }

    public Comparable getTag() {
        return tag;
    }

    public long getId() {
        return id;
    }

    public Map<Character, StateSet> getTransitions() {
        return transitions;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public State setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
        return this;
    }

    public Set<Character> possibleInputs() {
        return transitions.keySet();
    }

    public State setTag(Comparable tag) {
        this.tag = tag;
        return this;
    }

    public StateSet nextStates() {
        StateSet ss = new StateSet();
        for (Map.Entry<Character, StateSet> entry : transitions.entrySet()) {
            ss.addAll(entry.getValue());
        }
        return ss;
    }

    public State removeTransaction(Character input, State next) {
        if (transitions.containsKey(input)) {
            transitions.get(input).remove(next);
        }
        return this;
    }

    public State addTransaction(Character input, State next) {
        if (transitions.containsKey(input)) {
            transitions.get(input).add(next);
        } else {
            transitions.put(input, StateSet.of(next));
        }
        return this;
    }

    public State addAllTransactions(Map<Character, StateSet> transitions) {
        this.transitions.putAll(transitions);
        return this;
    }

    /**
     * Given an input, return to what States it can go.
     * Return rejection State in case there is not transaction for this input.
     *
     * @param input Character for transaction
     * @return set of states that can be reached by input
     */
    public StateSet move(Character input) {
        if (!transitions.containsKey(input)) return StateSet.of(REJECT_STATE);
        return transitions.get(input);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = State.class.cast(o);
        return id == state.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public int compareTo(Object o) {
        State other = (State) o;
        return tag.compareTo(other.tag);
    }

    @Override
    public String toString() {
        String trans = transitions.entrySet()
                .stream()
                .map(entry -> String.format("'%s': [%s]",
                        entry.getKey() == '\r' ? "\\r" : entry.getKey(),
                        entry.getValue()
                                .stream()
                                .map(state -> String.valueOf(state.getId()))
                                .collect(Collectors.joining(","))))
                .collect(Collectors.joining(", "));
        return String.format("S<%d, %s, %s> {%s}", id, isFinal, tag, trans);
    }
}
