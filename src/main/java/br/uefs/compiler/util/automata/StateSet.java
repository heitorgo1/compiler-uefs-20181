package br.uefs.compiler.util.automata;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a set of distinct States.
 */
public class StateSet extends HashSet<State> {

    public StateSet() {
        super();
    }

    public StateSet(StateSet other) {
        super(other);
    }

    /**
     * Create a new State based on this set.
     * Set the tag as the smaller tag of this set, and
     * if there's a final State inside the set, make the collapsed
     * State final.
     *
     * @return new State representing this Set
     * @throws Exception in case the set is empty
     */
    public State collapse() throws Exception {
        if (isEmpty()) throw new Exception("Can't collapse empty StateSet.");

        State state = new State()
                .setTag(getTag())
                .setIsFinal(isFinal());

        return state;
    }

    /**
     * Create a new set out of a single State.
     *
     * @param state
     * @return new StateSet
     */
    public static StateSet of(State state) {
        StateSet ss = new StateSet();
        ss.add(state);
        return ss;
    }

    /**
     * Get the set of States reachable from this one
     * for a given input.
     *
     * @param input Character input
     * @return new StateSet
     */
    public StateSet move(Character input) {
        StateSet ss = new StateSet();
        for (State st : this) {
            StateSet next = st.move(input);
            if (!next.isRejecting())
                ss.addAll(next);
        }
        return ss;
    }

    public <T extends Comparable> T getTag(Class<T> type) {
        TreeSet<Comparable> tags = new TreeSet<>();
        for (State st : this) {
            tags.add(st.getTag(type));
        }
        return type.cast(tags.first());
    }

    /**
     * Get the smaller tag from all State
     * tags in this set.
     *
     * @return the smaller tag
     */
    public Comparable getTag() {
        TreeSet<Comparable> tags = new TreeSet<>();
        for (State st : this) {
            tags.add(st.getTag());
        }
        return tags.first();
    }

    /**
     * In cases where the set has only one State, retrieve only that State.
     *
     * @return the only State of the set.
     * @throws Exception empty set
     */
    public State getOnlyState() throws Exception {
        if (isEmpty()) throw new Exception("Empty StateSet");
        if (size() > 1) throw new Exception("StateSet contains more than one state: " + size());
        return iterator().next();
    }

    /**
     * All possible character inputs of this set that leads to
     * other States.
     *
     * @return input Characters
     */
    public Set<Character> possibleInputs() {
        Set<Character> inputs = new HashSet<>();
        for (State st : this) {
            inputs.addAll(st.possibleInputs());
        }
        return inputs;
    }

    public boolean containsOnly(State state) {
        return this.contains(state) && this.size() == 1;
    }

    /**
     * Check if the set contains a rejecting state.
     *
     * @return isRejecting
     */
    public boolean isRejecting() {
        return this.contains(State.REJECT_STATE);
    }

    /**
     * Check if the set is final. It is final if
     * there's at least one final State in it.
     *
     * @return isFinal
     */
    public boolean isFinal() {
        if (isRejecting()) return false;
        for (State st : this) {
            if (st.isFinal()) return true;
        }
        return false;
    }
}
