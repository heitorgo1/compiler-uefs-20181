package br.uefs.compiler.parser;

import java.util.*;

public class Grammar extends Hashtable<Symbol, Rule.Array> {

    private Symbol startSymbol;
    private Map<Symbol, Symbol.Set> followMap;

    public Grammar() {
        super();

        startSymbol = null;
        followMap = new Hashtable<>();
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

 /*   public static List<Production> PRODUCTIONS = Arrays.asList(
            new Production("<E>", Arrays.asList(
                    Arrays.asList("<T>", "<E'>")
            )),
            new Production("<E'>", Arrays.asList(
                    Arrays.asList("'+'", "<T>", "<E'>"),
                    Arrays.asList("")
            )),
            new Production("<T>", Arrays.asList(
                    Arrays.asList("<F>", "<T'>")
            )),
            new Production("<T'>", Arrays.asList(
                    Arrays.asList("'*'", "<F>", "<T'>"),
                    Arrays.asList("")
            )),
            new Production("<F>", Arrays.asList(
                    Arrays.asList("'('", "<E>","')'"),
                    Arrays.asList("ID")
            ))
    );*/

    public Symbol.Set first(Symbol symbol) {
        return first(Symbol.Array.fromSingleSymbol(symbol));
    }

    public Symbol.Set first(Symbol.Array symbols) {

        Symbol.Set temp = new Symbol.Set();

        if (symbols.hasSingleSymbol()) {
            Symbol sy = symbols.getSingleSymbol();
            if (sy.isTerminal()) return Symbol.Set.fromSingleSymbol(sy);
            else {
                for (Rule rule : this.get(sy)) {
                    temp.addAll(first(rule.getSymbols()));
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

    public Map<Symbol, Symbol.Set> buildFollowMap() {

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

                        for (int i = 0; i < r.getSymbols().size(); i++) {
                            Symbol sy = r.getSymbols().get(i);

                            if (sy.equals(target)) {

                                if (i == r.getSymbols().size() - 1) {
                                    map.get(target).addAll(map.get(tmpEntry.getKey()));
                                } else {
                                    Symbol.Array nextSymbols = new Symbol.Array(r.getSymbols().subList(i + 1, r.getSymbols().size()));

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
        } while(changed);


        return map;
    }

    public void print() {
        for (Map.Entry<Symbol, Rule.Array> entry : this.entrySet()) {
            Rule.Array arr = entry.getValue();
            System.out.println(arr);
        }
    }

    public void printFirst() {
        for (Map.Entry<Symbol, Rule.Array> entry : this.entrySet()) {
            Symbol sy = entry.getKey();
            System.out.println(sy+" -> "+first(sy));
        }
    }

    public void printFollow() {
        for (Map.Entry<Symbol, Rule.Array> entry : this.entrySet()) {
            Symbol sy = entry.getKey();
            System.out.println(sy+" -> "+follow(sy));
        }
    }
}
