package br.uefs.compiler.util.automata;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class StateSet extends HashSet<State> {

    public StateSet() {
        super();
    }

    public StateSet(StateSet other) {
        super(other);
    }

    public State collapse() throws Exception {
        if (isEmpty()) throw new Exception("Can't collapse empty StateSet.");

        State state = new State()
                .setTag(getTag())
                .setIsFinal(isFinal());

        return state;
    }

    public static StateSet of(State state) {
        StateSet ss = new StateSet();
        ss.add(state);
        return ss;
    }

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

    public Comparable getTag() {
        TreeSet<Comparable> tags = new TreeSet<>();
        for (State st : this) {
            tags.add(st.getTag());
        }
        return tags.first();
    }

    public State getOnlyState() throws Exception {
        if (isEmpty()) throw new Exception("Empty StateSet");
        if (size() > 1) throw new Exception("StateSet contains more than one state: " + size());
        return iterator().next();
    }

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

    public boolean isRejecting() {
        return this.contains(State.REJECT_STATE);
    }

    public boolean isFinal() {
        if (isRejecting()) return false;
        for (State st : this) {
            if (st.isFinal()) return true;
        }
        return false;
    }
}
