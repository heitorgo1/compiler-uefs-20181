package br.uefs.compiler.util.automata;

import br.uefs.compiler.util.CharUtils;

import java.util.*;

public class TransitionTable {

    private Map<Integer, Map<String, Set<Integer>>> table;
    private int startId;
    private Set<Integer> acceptIds;
    private Map<Integer, StateTag> tags;

    public TransitionTable(int startId, Set<Integer> acceptIds, Map<Integer, Map<String, Set<Integer>>> table, Map<Integer, StateTag> tags) {
        this.table = table;
        this.startId = startId;
        this.acceptIds = acceptIds;
        this.tags = tags;
    }

    public Set<Integer> eclosure(int stateId) {
        Set<Integer> set = new HashSet<>();
        set.add(stateId);
        return eclosure(set);
    }

    public Set<Integer> eclosure(Set<Integer> stateIds) {
        Set<Integer> eclosure = new HashSet<>(stateIds);
        Stack<Integer> stack = new Stack<>();
        for (int stateId : stateIds) stack.push(stateId);

        while (!stack.isEmpty()) {
            int from = stack.pop();

            Set<Integer> epsTrans = table.get(from).get(NFA.EPS) == null ?
                    new HashSet<>() :
                    table.get(from).get(NFA.EPS);

            for (int to : epsTrans) {
                if (!eclosure.contains(to)) {
                    eclosure.add(to);
                    stack.push(to);
                }
            }
        }

        return eclosure;
    }

    public StateTag getTag(int stateId) {
        return this.tags.get(stateId);
    }

    public int getStartId() {
        return startId;
    }

    public Set<Integer> move(int stateId, String input) {
        if (stateId == NFA.EMPTY_STATE) return new HashSet<>();
        ArrayList<String> keys = new ArrayList<>();
        keys.addAll(table.get(stateId).keySet());

        // non-special characters first
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int r = Integer.compare(o1.length(), o2.length());
                if (r == 0) return o1.compareTo(o2);
                return r;
            }
        });

        if (CharUtils.isEscaped(input)) {
            if (table.get(stateId).get(input) == null) return new HashSet<>();
            return table.get(stateId).get(input);
        } else {
            for (String key : keys) {
                char c = input.charAt(0);
                if (CharUtils.getSpecialCharacterFunction(key).apply(c)) {
                    return table.get(stateId).get(key);
                }
            }
        }

        return new HashSet<>();
    }

    public Set<Integer> move(Set<Integer> stateIds, String input) {
        Set<Integer> moveFromAllStates = new HashSet<>();
        for (int id : stateIds) {
            moveFromAllStates.addAll(move(id, input));
        }
        return moveFromAllStates;
    }

    public Set<String> allInputSymbols(Set<Integer> stateIds) {
        Set<String> inputSymbols = new HashSet<>();
        for (int id : stateIds) {
            for (Map.Entry<String, Set<Integer>> entry : table.get(id).entrySet()) {
                inputSymbols.add(entry.getKey());
            }
        }
        return inputSymbols;
    }

    public boolean isAcceptingState(int stateId) {
        return acceptIds.contains(stateId);
    }

    public Map<Integer, StateTag> getTags() {
        return this.tags;
    }

    public boolean containsAcceptingState(Set<Integer> stateIds) {

        for (int acceptId : acceptIds) {
            if (stateIds.contains(acceptId)) return true;
        }
        return false;
    }

    public StateTag getAcceptingStateTag(Set<Integer> stateIds) {
        List<StateTag> tags = new ArrayList<>();

        for (int id : stateIds) {
            if (this.tags.get(id) == null) continue;
            tags.add(this.tags.get(id));
        }

        Collections.sort(tags);

        return tags.get(0);
    }

    public void print() {

        Stack<Integer> stack = new Stack<>();
        Set<Integer> visited = new HashSet<>();

        stack.push(startId);
        visited.add(startId);

        while (!stack.isEmpty()) {
            int curId = stack.pop();

            int tag = getTag(curId) == null ? -1 : getTag(curId).get();
            System.out.print(String.format("%d(%s)(%s) ", curId, acceptIds.contains(curId),tag));
            for (Map.Entry<String, Set<Integer>> entry : table.get(curId).entrySet()) {
                System.out.print(String.format("--(%s)--> ", entry.getKey()));

                for (int id : entry.getValue()) {
                    System.out.print(id + " ");

                    if (!visited.contains(id)) {
                        visited.add(id);
                        stack.push(id);
                    }
                }
                System.out.println();
            }
        }
    }

    public TransitionTable subsetConstructionDFA() {
        Set<Set<Integer>> Dstates = new HashSet<>();
        Set<Integer> eclosure = eclosure(startId);

        System.out.format("Applying Subset Construct to generate a DFA from NFA Transition Table...\n");

        Dstates.add(eclosure);

        ArrayList<Set<Integer>> unmarkedSets = new ArrayList<>();
        unmarkedSets.add(eclosure);

        int running_id = 0;
        int startId = running_id;

        Map<Set<Integer>, Integer> idTable = new Hashtable<>();
        Map<Integer, StateTag> tags = new Hashtable<>();
        Set<Integer> acceptIds = new HashSet<>();

        Map<Integer, Map<String, Set<Integer>>> map = new Hashtable<>();

        while (!unmarkedSets.isEmpty()) {
            Set<Integer> T = unmarkedSets.remove(0);
            if (!idTable.containsKey(T)) {
                idTable.put(T, running_id++);
            }
            int curId = idTable.get(T);

            if (map.get(curId) == null)
                map.put(curId, new Hashtable<>());

            if (containsAcceptingState(T)) {
                StateTag finalTag = getAcceptingStateTag(T);
                acceptIds.add(curId);
                tags.put(curId, finalTag);
            }

            for (String input : allInputSymbols(T)) {
                if (input == NFA.EPS) continue;
                Set<Integer> U = eclosure(move(T, input));
                if (!idTable.containsKey(U)) {
                    idTable.put(U, running_id++);
                }
                int otherId = idTable.get(U);
                if (!Dstates.contains(U)) {
                    Dstates.add(U);
                    unmarkedSets.add(U);
                }
                map.get(curId).put(input, new HashSet<>(Arrays.asList(otherId)));
            }
        }

        return new TransitionTable(startId, acceptIds, map, tags);
    }
}
