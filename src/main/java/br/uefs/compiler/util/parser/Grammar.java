package br.uefs.compiler.util.parser;

import java.util.*;

public class Grammar {

    public static List<Production> PRODUCTIONS = Arrays.asList(
            new Production("<S>", Arrays.asList(
                    Arrays.asList("<Global Declaration>", "<S 1>")
            )
            ),
            new Production("<S 1>", Arrays.asList(
                    Arrays.asList("<Global Declaration>", "<S 1>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Global Declaration>", Arrays.asList(
                    Arrays.asList("<Start Def>"),
                    Arrays.asList("<Var Def>"),
                    Arrays.asList("<Const Def>"),
                    Arrays.asList("<Struct Def>"),
                    Arrays.asList("<Function Def>"),
                    Arrays.asList("<Procedure Def>"),
                    Arrays.asList("<Typedef Def>")
            )
            ),
            new Production("<Function Def>", Arrays.asList(
                    Arrays.asList("'function'", "<Type>", "<Declarator>", "'('", "<Parameter List>", "')'", "'{'",
                            "<Stmt Or Declaration List>", "'}'")
            )
            ),
            new Production("<Procedure Def>", Arrays.asList(
                    Arrays.asList("'procedure'", "IDENTIFICADOR", "'('", "<Parameter List>", "')'", "'{'",
                            "<Stmt Or Declaration List>", "'}'")
            )
            ),
            new Production("<Typedef Def>", Arrays.asList(
                    Arrays.asList("'typedef'", "<Typedef Def lf>")
            )
            ),
            new Production("<Typedef Def lf>", Arrays.asList(
                    Arrays.asList("<Type>", "IDENTIFICADOR", "';'"),
                    Arrays.asList("<Struct Def>", "IDENTIFICADOR", "';'")
            )
            ),
            new Production("<Var Def>", Arrays.asList(
                    Arrays.asList("'var'", "'{'", "<Declaration List>", "'}'")
            )
            ),
            new Production("<Const Def>", Arrays.asList(
                    Arrays.asList("'const'", "'{'", "<Declaration List>", "'}'")
            )
            ),
            new Production("<Struct Def>", Arrays.asList(
                    Arrays.asList("'struct'", "IDENTIFICADOR", "<Struct Def lf>")
            )
            ),
            new Production("<Struct Def lf>", Arrays.asList(
                    Arrays.asList("'{'", "<Declaration List>", "'}'"),
                    Arrays.asList("'extends'", "IDENTIFICADOR", "'{'", "<Declaration List>", "'}'")
            )
            ),
            new Production("<Parameter List>", Arrays.asList(
                    Arrays.asList("<Parameter Declaration>", "<Parameter List 1>")
            )
            ),
            new Production("<Parameter List 1>", Arrays.asList(
                    Arrays.asList("','", "<Parameter Declaration>", "<Parameter List 1>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Parameter Declaration>", Arrays.asList(
                    Arrays.asList("<Type>", "<Declarator>")
            )
            ),
            new Production("<Declaration List>", Arrays.asList(
                    Arrays.asList("<Declaration>", "<Declaration List 1>")
            )
            ),
            new Production("<Declaration List 1>", Arrays.asList(
                    Arrays.asList("<Declaration>", "<Declaration List 1>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Declaration>", Arrays.asList(
                    Arrays.asList("<Type>", "<Init Declarator List>", "';'")
            )
            ),
            new Production("<Init Declarator List>", Arrays.asList(
                    Arrays.asList("<Init Declarator>", "<Init Declarator List 1>")
            )
            ),
            new Production("<Init Declarator List 1>", Arrays.asList(
                    Arrays.asList("','", "<Init Declarator>", "<Init Declarator List 1>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Init Declarator>", Arrays.asList(
                    Arrays.asList("<Declarator>", "<Init Declarator lf>")
            )
            ),
            new Production("<Init Declarator lf>", Arrays.asList(
                    Arrays.asList("'='", "<Initializer>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Initializer>", Arrays.asList(
                    Arrays.asList("<Assign Expr>"),
                    Arrays.asList("'{'", "<Initializer List>", "<Initializer lf>")
            )
            ),
            new Production("<Initializer lf>", Arrays.asList(
                    Arrays.asList("'}'"),
                    Arrays.asList("','", "'}'")
            )
            ),
            new Production("<Initializer List>", Arrays.asList(
                    Arrays.asList("<Initializer>", "<Initializer List 1>")
            )
            ),
            new Production("<Initializer List 1>", Arrays.asList(
                    Arrays.asList("','", "<Initializer>", "<Initializer List 1>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Declarator>", Arrays.asList(
                    Arrays.asList("IDENTIFICADOR", "<Declarator 1>")
            )
            ),
            new Production("<Declarator 1>", Arrays.asList(
                    Arrays.asList("'['", "<Declarator 1 lf>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Declarator 1 lf>", Arrays.asList(
                    Arrays.asList("<Cond Expr>", "']'", "<Declarator 1>"),
                    Arrays.asList("']'", "<Declarator 1>")
            )
            ),
            new Production("<Stmt>", Arrays.asList(
                    Arrays.asList("<Iteration Stmt>"),
                    Arrays.asList("<Expr Stmt>"),
                    Arrays.asList("<Compound Stmt>"),
                    Arrays.asList("<Print Stmt>"),
                    Arrays.asList("<Scan Stmt>"),
                    Arrays.asList("<If Stmt>"),
                    Arrays.asList("<Return Stmt>")
            )
            ),
            new Production("<Stmt Or Declaration List>", Arrays.asList(
                    Arrays.asList("<Stmt>", "<Stmt Or Declaration List 1>"),
                    Arrays.asList("<Var Def>", "<Stmt Or Declaration List 1>")
            )
            ),
            new Production("<Stmt Or Declaration List 1>", Arrays.asList(
                    Arrays.asList("<Stmt>", "<Stmt Or Declaration List 1>"),
                    Arrays.asList("<Var Def>", "<Stmt Or Declaration List 1>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Start Def>", Arrays.asList(
                    Arrays.asList("'start'", "'('", "')'", "'{'", "<Stmt Or Declaration List>", "'}'")
            )
            ),
            new Production("<Print Stmt>", Arrays.asList(
                    Arrays.asList("'print'", "'('", "<Argument List>", "')'", "';'")
            )
            ),
            new Production("<Scan Stmt>", Arrays.asList(
                    Arrays.asList("'scan'", "'('", "<Argument List>", "')'", "';'")
            )
            ),
            new Production("<Iteration Stmt>", Arrays.asList(
                    Arrays.asList("'while'", "'('", "<Expr>", "')'", "<Stmt>")
            )
            ),
            new Production("<If Stmt>", Arrays.asList(
                    Arrays.asList("'if'", "<Expr>", "'then'", "<Stmt>", "<If Stmt lf>")
            )
            ),
            new Production("<If Stmt lf>", Arrays.asList(
                    Arrays.asList("'else'", "<Stmt>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Return Stmt>", Arrays.asList(
                    Arrays.asList("'return'", "<Expr>", "';'")
            )
            ),
            new Production("<Compound Stmt>", Arrays.asList(
                    Arrays.asList("'{'", "<Compound Stmt lf>")
            )
            ),
            new Production("<Compound Stmt lf>", Arrays.asList(
                    Arrays.asList("'}'"),
                    Arrays.asList("<Stmt Or Declaration List>", "'}'")
            )
            ),
            new Production("<Expr Stmt>", Arrays.asList(
                    Arrays.asList("<Expr>", "';'"),
                    Arrays.asList("';'")
            )
            ),
            new Production("<Expr>", Arrays.asList(
                    Arrays.asList("<Assign Expr>", "<Expr 1>")
            )
            ),
            new Production("<Expr 1>", Arrays.asList(
                    Arrays.asList("','", "<Assign Expr>", "<Expr 1>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Assign Expr>", Arrays.asList(
                    Arrays.asList("<Cond Expr>"),
                    Arrays.asList("<Postfix Expr>", "'='", "<Assign Expr>")
            )
            ),
            new Production("<Cond Expr>", Arrays.asList(
                    Arrays.asList("<Logical Or Expr>")
            )
            ),
            new Production("<Logical Or Expr>", Arrays.asList(
                    Arrays.asList("<Logical And Expr>", "<Logical Or Expr 1>")
            )
            ),
            new Production("<Logical Or Expr 1>", Arrays.asList(
                    Arrays.asList("'||'", "<Logical And Expr>", "<Logical Or Expr 1>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Logical And Expr>", Arrays.asList(
                    Arrays.asList("<Equal Expr>", "<Logical And Expr 1>")
            )
            ),
            new Production("<Logical And Expr 1>", Arrays.asList(
                    Arrays.asList("'&&'", "<Equal Expr>", "<Logical And Expr 1>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Equal Expr>", Arrays.asList(
                    Arrays.asList("<Relational Expr>", "<Equal Expr 1>")
            )
            ),
            new Production("<Equal Expr 1>", Arrays.asList(
                    Arrays.asList("<Equal Op>", "<Relational Expr>", "<Equal Expr 1>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Relational Expr>", Arrays.asList(
                    Arrays.asList("<Additive Expr>", "<Relational Expr 1>")
            )
            ),
            new Production("<Relational Expr 1>", Arrays.asList(
                    Arrays.asList("<Relational Op>", "<Additive Expr>", "<Relational Expr 1>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Additive Expr>", Arrays.asList(
                    Arrays.asList("<Mult Expr>", "<Additive Expr 1>")
            )
            ),
            new Production("<Additive Expr 1>", Arrays.asList(
                    Arrays.asList("<Additive Op>", "<Mult Expr>", "<Additive Expr 1>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Mult Expr>", Arrays.asList(
                    Arrays.asList("<Unary Expr>", "<Mult Expr 1>")
            )
            ),
            new Production("<Mult Expr 1>", Arrays.asList(
                    Arrays.asList("<Mult Op>", "<Unary Expr>", "<Mult Expr 1>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Unary Expr>", Arrays.asList(
                    Arrays.asList("<Unary Op>", "<Unary Expr>"),
                    Arrays.asList("<Postfix Expr>")
            )
            ),
            new Production("<Postfix Expr>", Arrays.asList(
                    Arrays.asList("<Primary Expr>", "<Postfix Expr 1>")
            )
            ),
            new Production("<Postfix Expr 1>", Arrays.asList(
                    Arrays.asList("<Postfix Op>", "<Postfix Expr 1>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Primary Expr>", Arrays.asList(
                    Arrays.asList("IDENTIFICADOR"),
                    Arrays.asList("NUMERO"),
                    Arrays.asList("CADEIACARECTERES"),
                    Arrays.asList("'true'"),
                    Arrays.asList("'false'"),
                    Arrays.asList("'('", "<Expr>", "')'")
            )
            ),
            new Production("<Equal Op>", Arrays.asList(
                    Arrays.asList("'=='"),
                    Arrays.asList("'!='")
            )
            ),
            new Production("<Relational Op>", Arrays.asList(
                    Arrays.asList("'<'"),
                    Arrays.asList("'>'"),
                    Arrays.asList("'>='"),
                    Arrays.asList("'<='"),
                    Arrays.asList("'<='")
            )
            ),
            new Production("<Additive Op>", Arrays.asList(
                    Arrays.asList("'+'"),
                    Arrays.asList("'-'")
            )
            ),
            new Production("<Mult Op>", Arrays.asList(
                    Arrays.asList("'*'"),
                    Arrays.asList("'/'")
            )
            ),
            new Production("<Unary Op>", Arrays.asList(
                    Arrays.asList("'++'"),
                    Arrays.asList("'--'"),
                    Arrays.asList("'!'")
            )
            ),
            new Production("<Postfix Op>", Arrays.asList(
                    Arrays.asList("'++'"),
                    Arrays.asList("'--'"),
                    Arrays.asList("'['", "<Expr>", "']'"),
                    Arrays.asList("'('", "<Postfix Op lf>"),
                    Arrays.asList("'.'", "IDENTIFICADOR")
            )
            ),
            new Production("<Postfix Op lf>", Arrays.asList(
                    Arrays.asList("')'"),
                    Arrays.asList("<Argument List>", "')'")
            )
            ),
            new Production("<Argument List>", Arrays.asList(
                    Arrays.asList("<Assign Expr>", "<Argument List 1>")
            )
            ),
            new Production("<Argument List 1>", Arrays.asList(
                    Arrays.asList("','", "<Assign Expr>", "<Argument List 1>"),
                    Arrays.asList("")
            )
            ),
            new Production("<Type>", Arrays.asList(
                    Arrays.asList("'int'"),
                    Arrays.asList("'string'"),
                    Arrays.asList("'float'"),
                    Arrays.asList("'bool'"),
                    Arrays.asList("IDENTIFICADOR")
            )
            )
    );


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

    public static Production START_SYMBOL = PRODUCTIONS.get(0);

    public static boolean isNonTerminal(String s) {
        return s.startsWith("<") && s.endsWith(">");
    }

    public static boolean isTerminal(String s) {
        return !isNonTerminal(s);
    }

    public static Map<String, List<List<String>>> asMap() {
        Map<String, List<List<String>>> map = new Hashtable<>();

        for (Production p : PRODUCTIONS) {
            map.put(p.getName(), p.getProductions());
        }

        return map;
    }

    public static Map<String, Set<String>> firstMap() {
        Map<String, List<List<String>>> grammarMap = asMap();
        Map<String, Set<String>> map = new Hashtable<>();


        for (Production p : PRODUCTIONS) {
            map.put(p.getName(), first(p.getName(), grammarMap));

            for (List<String> prods : p.getProductions()) {
                for (String symbol : prods) {
                    if (isTerminal(symbol)) {
                        Set<String> set = new HashSet<>();
                        set.add(symbol);
                        map.put(symbol, set);
                    }
                }
            }
        }

        return map;
    }

    private static Set<String> first(String X, Map<String, List<List<String>>> grammarMap) {
        Set<String> set = new HashSet<>();

        if (isTerminal(X)) {
            set.add(X);
            return set;
        } else {
            for (List<String> prods : grammarMap.get(X)) {
                Set<String> tmpSet = new HashSet<>();
                for (int i = 0; i < prods.size(); i++) {
                    String symbol = prods.get(i);
                    Set<String> symbolFirst = first(symbol, grammarMap);
                    tmpSet.addAll(symbolFirst);
                    if (!symbolFirst.contains("")) {
                        break;
                    } else {
                        if (i != prods.size() - 1)
                            tmpSet.remove("");
                    }
                }
                set.addAll(tmpSet);
            }
        }
        return set;
    }

    public static Set<String> first(List<String> list, Map<String, Set<String>> firstMap) {
        Set<String> set = new HashSet<>();

        for (int i = 0; i < list.size(); i++) {
            String symbol = list.get(i);
            set.addAll(firstMap.get(symbol));

            set.remove("");
            if (!firstMap.get(symbol).contains("")) {
                break;
            } else if (firstMap.get(symbol).contains("") && i == list.size() - 1) {
                set.add("");
            }
        }

        return set;
    }

    public static Map<String, Set<String>> followMap() {
        Map<String, List<List<String>>> grammarMap = asMap();
        Map<String, Set<String>> map = new Hashtable<>();
        Map<String, Set<String>> first = firstMap();


        for (Production p : PRODUCTIONS) {
            Set<String> set = new HashSet<>();
            if (p.equals(START_SYMBOL)) set.add("$");
            map.put(p.getName(), set);
        }
        boolean change = false;

        while (true) {
            change = false;
            for (Production pp : PRODUCTIONS) {
                String X = pp.getName();
                int prevSize = map.get(X).size();
                for (Production p : PRODUCTIONS) {
                    for (List<String> symbols : p.getProductions()) {
                        for (int i = 0; i < symbols.size(); i++) {
                            String symbol = symbols.get(i);

                            if (symbol.equals(X)) {
                                if (i == symbols.size() - 1) {
                                    map.get(X).addAll(map.get(p.getName()));
                                } else {
                                    String next = symbols.get(i + 1);

                                    map.get(X).addAll(first.get(next));

                                    if (isNonTerminal(next) && first.get(next).contains(""))
                                        map.get(X).addAll(map.get(next));
                                }
                            }
                        }
                    }
                }
                map.get(X).remove("");
                if (map.get(X).size() > prevSize) change = true;
            }

            if (!change) break;
        }
        return map;
    }

    public static void print() {
        for (Production p : PRODUCTIONS) {
            for (List<String> symbols : p.getProductions()) {
                System.out.println();
                System.out.print(p.getName().replace(" ", "")+" -> ");
                for (String symbol : symbols) {
                    if (symbol.isEmpty()) System.out.print("'' ");
                    else System.out.print(symbol.replace(" ", "")+" ");
                }
            }
        }
    }
}
