package br.uefs.compiler.util.automata;

import java.util.*;

public class State {
    private int id;
    private boolean isFinal;

    private Map<String, List<State>> links;

    public State(int id) {
        this.id = id;
        this.isFinal = false;
        links = new HashMap<>();
    }

    public State(int id, boolean isFinal) {
        this.id = id;
        this.isFinal = isFinal;
        links = new HashMap<>();
    }

    public void addLink(String input, State other) {
        if (links.get(input) != null) {
            links.get(input).add(other);
        } else {
            List<State> list = new ArrayList<>();
            list.add(other);
            links.put(input, list);
        }
    }

    public Map<String, List<State>> getLinks() {
        return links;
    }

    public void setLinks(Map<String, List<State>> links) {
        this.links = links;
    }

    public int getId() {
        return id;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setNotFinal() {
        this.isFinal = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return id == state.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

}
