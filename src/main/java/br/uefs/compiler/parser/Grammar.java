package br.uefs.compiler.parser;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Grammar abstraction as Map and it's related functions (FIRST and FOLLOW)
 * */
public class Grammar extends Hashtable<Symbol, Rule.Array> {

    private Symbol startSymbol;
    private Map<Symbol, Symbol.Set> followMap;
    private Map<Symbol, Symbol.Set> syncMap;

    public Grammar() {
        super();

        startSymbol = null;
        followMap = new Hashtable<>();
        syncMap = new Hashtable<>();
    }

    public void setStartSymbol(Symbol startSymbol) {
        this.startSymbol = startSymbol;
    }

    public Symbol getStartSymbol() {
        return this.startSymbol;
    }

    public Grammar addRule(Rule rule) {
        this.putIfAbsent(rule.getNonTerminal(), new Rule.Array());

        this.get(rule.getNonTerminal()).add(rule);
        return this;
    }

    public Symbol.Set getSynchronizingSet(Symbol s) {
        try {
            if (!syncMap.containsKey(s)) syncMap = buildSyncMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return syncMap.get(s);
    }

    public Symbol.Set getTerminalSet() {

        Symbol.Set set = new Symbol.Set();

        for (Map.Entry<Symbol, Rule.Array> entry : this.entrySet()) {
            for (Rule rule : entry.getValue()) {
                for (Symbol s : rule.getSymbols()) {
                    if (s.isTerminal()) {
                        set.add(s);
                    }
                }
            }
        }
        return set;
    }

    public Symbol.Set first(Symbol symbol) {
        return first(Symbol.Array.fromSingleSymbol(symbol));
    }

    public Symbol.Set first(Symbol.Array symbols) {
        symbols = symbols.getSymbolsWithoutActions();

        Symbol.Set temp = new Symbol.Set();

        if (symbols.hasSingleSymbol()) {
            Symbol sy = symbols.getSingleSymbol();
            if (sy.isTerminal()) return Symbol.Set.fromSingleSymbol(sy);
            else {
                for (Rule rule : this.get(sy)) {
                    temp.addAll(first(rule.getSymbols().getSymbolsWithoutActions()));
                }
            }
        } else {
            for (int i = 0; i < symbols.size(); i++) {
                Symbol sy = symbols.get(i);

                Symbol.Set cur = first(sy);

                if (cur.containsEmptyString()) {
                    if (i < symbols.size() - 1) {
                        cur.removeEmptyString();
                    }
                    temp.addAll(cur);
                } else {
                    temp.addAll(cur);
                    break;
                }
            }
        }

        return temp;
    }

    public Symbol.Set follow(Symbol target) {
        if (!followMap.containsKey(target)) {
            followMap = buildFollowMap();
        }
        return followMap.get(target);
    }

    private Map<Symbol, Symbol.Set> buildFollowMap() {

        Map<Symbol, Symbol.Set> map = new Hashtable<>();

        this.forEach((target, ruleArr) -> {
            Symbol.Set set = new Symbol.Set();
            if (target.equals(startSymbol)) set.add(Symbol.INPUT_END);
            map.putIfAbsent(target, set);
        });

        boolean changed;

        do {
            changed = false;
            for (Map.Entry<Symbol, Rule.Array> entry : this.entrySet()) {
                Symbol target = entry.getKey();

                int prevSize = map.get(target).size();

                for (Map.Entry<Symbol, Rule.Array> tmpEntry : this.entrySet()) {
                    Rule.Array ruleArr = tmpEntry.getValue();

                    for (Rule r : ruleArr) {
                        Symbol.Array symbols = r.getSymbols().getSymbolsWithoutActions();
                        for (int i = 0; i < symbols.size(); i++) {
                            Symbol sy = symbols.get(i);

                            if (sy.equals(target)) {

                                if (i == symbols.size() - 1) {
                                    map.get(target).addAll(map.get(tmpEntry.getKey()));
                                } else {
                                    Symbol.Array nextSymbols = new Symbol.Array(symbols.subList(i + 1, symbols.size()));

                                    map.get(target).addAll(first(nextSymbols));

                                    if (first(nextSymbols).containsEmptyString())
                                        map.get(target).addAll(map.get(tmpEntry.getKey()));
                                }
                            }

                        }
                    }
                }
                map.get(target).removeEmptyString();
                if (map.get(target).size() > prevSize) changed = true;
            }
        } while (changed);


        return map;
    }

    /**
     * For each non terminal, add the FIRST and synchronizing sets of
     * parent to it's own synchronizing set beside it's FOLLOW set.
     * */
    private Map<Symbol, Symbol.Set> buildSyncMap() throws InterruptedException {
        Map<Symbol, Symbol.Set> map = new Hashtable<>();
        Symbol start = getStartSymbol();
        LinkedBlockingQueue<Symbol> q = new LinkedBlockingQueue<>();
        map.putIfAbsent(start, new Symbol.Set());

        for (Symbol startChild : getChildNonTerminals(start)) {
            q.put(startChild);
            map.putIfAbsent(startChild, new Symbol.Set());
        }

        while (!q.isEmpty()) {
            Symbol cur = q.poll();

            for (Symbol child : getChildNonTerminals(cur)) {
                if (!map.containsKey(child)) {
                    map.putIfAbsent(child, first(cur));
                    map.get(child).addAll(follow(child));
                    map.get(child).addAll(map.get(cur));
                    // Delimiters and keywords
                    map.get(child).addAll(Arrays.asList(
                            new Symbol("'{'"), new Symbol("'if'"),
                            new Symbol("'}'"), new Symbol("'while'"),
                            new Symbol("'['"), new Symbol("'return'"),
                            new Symbol("']'"), new Symbol("'int'"),
                            new Symbol("'('"), new Symbol("'float'"),
                            new Symbol("')'"), new Symbol("'string'"),
                            new Symbol("';'"), new Symbol("'bool'"),
                            new Symbol("','"), new Symbol("IDENTIFICADOR")
                    ));
                    q.put(child);
                }
            }
        }

        return map;
    }

    public Symbol.Set getChildNonTerminals(Symbol s) {
        assert this.get(s) != null;

        Symbol.Set set = new Symbol.Set();

        for (Rule rule : this.get(s)) {
            for (Symbol child : rule.getSymbols()) {
                if (child.isNonTerminal()) {
                    set.add(child);
                }
            }
        }
        return set;
    }

    public void print() {
        for (Map.Entry<Symbol, Rule.Array> entry : this.entrySet()) {
            Rule.Array arr = entry.getValue();
            System.out.println(arr);
        }
    }

    public void printFirst() {
        System.out.println("FIRST TOKENS");
        for (Map.Entry<Symbol, Rule.Array> entry : this.entrySet()) {
            Symbol sy = entry.getKey();
            System.out.println(sy + " -> " + first(sy));
        }
    }

    public void printFollow() {
        System.out.println("FOLLOW TOKENS");
        for (Map.Entry<Symbol, Rule.Array> entry : this.entrySet()) {
            Symbol sy = entry.getKey();
            System.out.println(sy + " -> " + follow(sy));
        }
    }

    public void printSync() {
        System.out.println("SYNC TOKENS");
        for (Map.Entry<Symbol, Rule.Array> entry : this.entrySet()) {
            Symbol sy = entry.getKey();
            System.out.println(sy + " -> " + getSynchronizingSet(sy));
        }
    }

    public void printNonTerminals() {
        for (Map.Entry<Symbol, Rule.Array> entry : this.entrySet()) {
            Symbol sy = entry.getKey();
            System.out.println(sy);
        }
    }
}
