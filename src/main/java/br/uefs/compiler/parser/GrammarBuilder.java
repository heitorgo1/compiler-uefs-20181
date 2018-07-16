package br.uefs.compiler.parser;

/**
 * Encapsulates grammar definition
 */
public class GrammarBuilder {

    public static Grammar build() {
        Grammar grammar = new Grammar();
        grammar.addRule(new Rule("<S>", new Symbol.Array("<Global Declaration>", "<S 1>", "{hasOneStart()}")));

        grammar.addRule(new Rule("<S 1>", new Symbol.Array("<Global Declaration>", "<S 1>")));
        grammar.addRule(new Rule("<S 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Global Declaration>", new Symbol.Array("{incStart()}", "<Start Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new Symbol.Array("<Var Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new Symbol.Array("<Const Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new Symbol.Array("<Struct Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new Symbol.Array("<Function Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new Symbol.Array("<Procedure Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new Symbol.Array("<Typedef Def>")));

        grammar.addRule(new Rule("<Function Def>", new Symbol.Array("'function'", "<Type>", "{assign(<Declarator>:Stack[-1].type, <Type>:Aux[0].type); assign(Stack[-1].cat, func)}", "<Declarator>", "'('", "{assign(Stack[-1].lex, Aux[-1].lex)}","<Function Def lf>")));
        grammar.addRule(new Rule("<Function Def lf>", new Symbol.Array("{assign(Stack[-1].cat, param)}", "<Parameter List>", "{insertParams(Aux[-1].lex, Aux[0].params)}", "')'", "'{'", "<Stmt Or Declaration List>", "'}'")));
        grammar.addRule(new Rule("<Function Def lf>", new Symbol.Array("')'", "'{'", "<Stmt Or Declaration List>", "'}'")));

        grammar.addRule(new Rule("<Procedure Def>", new Symbol.Array("'procedure'", "IDENTIFICADOR", "{insertCategory(Aux[0].lex, proc)}","'('", "{assign(Stack[-1].lex, Aux[-1].lex)}", "<Procedure Def lf>")));
        grammar.addRule(new Rule("<Procedure Def lf>", new Symbol.Array("{assign(Stack[-1].cat, param)}", "<Parameter List>", "{insertParams(Aux[-1].lex, Aux[0].params)}", "')'", "'{'", "<Stmt Or Declaration List>", "'}'")));
        grammar.addRule(new Rule("<Procedure Def lf>", new Symbol.Array("')'", "'{'", "<Stmt Or Declaration List>", "'}'")));

        grammar.addRule(new Rule("<Typedef Def>", new Symbol.Array("'typedef'", "<Typedef Def lf>")));
        grammar.addRule(new Rule("<Typedef Def lf>", new Symbol.Array("<Type>", "IDENTIFICADOR", "';'")));
        grammar.addRule(new Rule("<Typedef Def lf>", new Symbol.Array("<Struct Def>", "IDENTIFICADOR", "';'")));

        grammar.addRule(new Rule("<Var Def>", new Symbol.Array("'var'", "'{'", "{assign(Stack[-1].cat, var)}", "<Declaration List>", "'}'")));

        grammar.addRule(new Rule("<Const Def>", new Symbol.Array("'const'", "'{'", "{assign(Stack[-1].cat, const)}","<Declaration List>", "'}'")));

        grammar.addRule(new Rule("<Struct Def>", new Symbol.Array("'struct'", "IDENTIFICADOR", "<Struct Def lf>")));
        grammar.addRule(new Rule("<Struct Def lf>", new Symbol.Array("'{'", "{assign(Stack[-1].cat, struct)}", "<Declaration List>", "'}'")));
        grammar.addRule(new Rule("<Struct Def lf>", new Symbol.Array("'extends'", "IDENTIFICADOR", "'{'", "{assign(Stack[-1].cat, struct)}", "<Declaration List>", "'}'")));

        grammar.addRule(new Rule("<Parameter List>", new Symbol.Array("{assign(Stack[-1].cat, Aux[0].cat)}", "<Parameter Declaration>", "{concat(Stack[-1].params, Aux[0].lex); assign(Stack[-1].cat, Aux[0].cat)}", "<Parameter List 1>", "{assign(Aux[-2].params, Aux[0].params)}")));
        grammar.addRule(new Rule("<Parameter List 1>", new Symbol.Array("','", "{assign(Stack[-1].cat, Aux[-1].cat)}", "<Parameter Declaration>",  "{assign(Stack[-1].params, Aux[-2].params); concat(Stack[-1].params, Aux[0].lex); assign(Stack[-1].cat, Aux[0].cat)}", "<Parameter List 1>", "{assign(Aux[-3].params, Aux[0].params)}")));
        grammar.addRule(new Rule("<Parameter List 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Parameter Declaration>", new Symbol.Array("<Type>", "{assign(Stack[-1].type, Aux[0].type); assign(Stack[-1].cat, Aux[-1].cat)}", "<Declarator>", "{assign(Aux[-2].lex, Aux[0].lex)}")));

        grammar.addRule(new Rule("<Declaration List>", new Symbol.Array("{assign(Stack[-1].cat, Aux[0].cat)}", "<Declaration>", "{assign(Stack[-1].cat, Aux[0].cat)}","<Declaration List 1>")));
        grammar.addRule(new Rule("<Declaration List 1>", new Symbol.Array("{assign(Stack[-1].cat, Aux[0].cat)}", "<Declaration>", "{assign(Stack[-1].cat, Aux[0].cat)}", "<Declaration List 1>")));
        grammar.addRule(new Rule("<Declaration List 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Declaration>", new Symbol.Array("<Type>", "{assign(Stack[-1].type, Aux[0].type); assign(Stack[-1].cat, Aux[-1].cat)}", "<Init Declarator List>", "';'")));

        grammar.addRule(new Rule("<Init Declarator List>", new Symbol.Array("{assign(Stack[-1].type, Aux[0].type); assign(Stack[-2].type, Aux[0].type); assign(Stack[-1].cat, Aux[0].cat); assign(Stack[-2].cat, Aux[0].cat)}", "<Init Declarator>", "<Init Declarator List 1>")));
        grammar.addRule(new Rule("<Init Declarator List 1>", new Symbol.Array("','", "{assign(Stack[-1].type, Aux[-1].type); assign(Stack[-1].cat, Aux[-1].cat)}", "<Init Declarator>", "{assign(Stack[-1].type, Aux[-2].type); assign(Stack[-1].cat, Aux[0].cat)}","<Init Declarator List 1>")));
        grammar.addRule(new Rule("<Init Declarator List 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Init Declarator>", new Symbol.Array("{assign(Stack[-1].type, Aux[0].type); assign(Stack[-1].cat, Aux[0].cat)}","<Declarator>", "<Init Declarator lf>")));
        grammar.addRule(new Rule("<Init Declarator lf>", new Symbol.Array("'='", "<Initializer>")));
        grammar.addRule(new Rule("<Init Declarator lf>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Initializer>", new Symbol.Array("<Assign Expr>")));
        grammar.addRule(new Rule("<Initializer>", new Symbol.Array("'{'", "<Initializer List>", "<Initializer lf>")));
        grammar.addRule(new Rule("<Initializer lf>", new Symbol.Array("'}'")));
        grammar.addRule(new Rule("<Initializer lf>", new Symbol.Array("','", "'}'")));

        grammar.addRule(new Rule("<Initializer List>", new Symbol.Array("<Initializer>", "<Initializer List 1>")));
        grammar.addRule(new Rule("<Initializer List 1>", new Symbol.Array("','", "<Initializer>", "<Initializer List 1>")));
        grammar.addRule(new Rule("<Initializer List 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Declarator>", new Symbol.Array("IDENTIFICADOR", "<Declarator 1>", "{insertSymbolType(Aux[-1].lex, Aux[-2].type); insertCategory(Aux[-1].lex, Aux[-2].cat); assign(Aux[-2].lex, Aux[-1].lex)}")));
        grammar.addRule(new Rule("<Declarator 1>", new Symbol.Array("'['", "<Declarator 1 lf>")));
        grammar.addRule(new Rule("<Declarator 1>", new Symbol.Array("")));
        grammar.addRule(new Rule("<Declarator 1 lf>", new Symbol.Array("<Cond Expr>", "']'", "<Declarator 1>")));
        grammar.addRule(new Rule("<Declarator 1 lf>", new Symbol.Array("']'", "<Declarator 1>")));

        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<Iteration Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<Expr Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<Compound Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<Print Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<Scan Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<If Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<Return Stmt>")));

        grammar.addRule(new Rule("<Stmt Or Declaration List>", new Symbol.Array("<Stmt>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List>", new Symbol.Array("<Var Def>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List 1>", new Symbol.Array("<Stmt>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List 1>", new Symbol.Array("<Var Def>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Start Def>", new Symbol.Array("'start'", "'('", "')'", "'{'", "<Stmt Or Declaration List>", "'}'")));

        grammar.addRule(new Rule("<Print Stmt>", new Symbol.Array("'print'", "'('", "<Argument List>", "')'", "';'")));

        grammar.addRule(new Rule("<Scan Stmt>", new Symbol.Array("'scan'", "'('", "<Argument List>", "')'", "';'")));

        grammar.addRule(new Rule("<Iteration Stmt>", new Symbol.Array("'while'", "'('", "<Expr>", "')'", "<Stmt>")));

        grammar.addRule(new Rule("<If Stmt>", new Symbol.Array("'if'", "<Expr>", "'then'", "<Stmt>", "<If Stmt lf>")));
        grammar.addRule(new Rule("<If Stmt lf>", new Symbol.Array("'else'", "<Stmt>")));
        grammar.addRule(new Rule("<If Stmt lf>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Return Stmt>", new Symbol.Array("'return'", "<Expr>", "';'")));

        grammar.addRule(new Rule("<Compound Stmt>", new Symbol.Array("'{'", "<Compound Stmt lf>")));
        grammar.addRule(new Rule("<Compound Stmt lf>", new Symbol.Array("'}'")));
        grammar.addRule(new Rule("<Compound Stmt lf>", new Symbol.Array("<Stmt Or Declaration List>", "'}'")));

        grammar.addRule(new Rule("<Expr Stmt>", new Symbol.Array("<Expr>", "';'")));
        grammar.addRule(new Rule("<Expr Stmt>", new Symbol.Array("';'")));

        grammar.addRule(new Rule("<Expr>", new Symbol.Array("<Assign Expr>", "<Expr 1>")));
        grammar.addRule(new Rule("<Expr 1>", new Symbol.Array("','", "<Assign Expr>", "<Expr 1>")));
        grammar.addRule(new Rule("<Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Assign Expr>", new Symbol.Array("<Cond Expr>", "<Assign Expr 1>")));
        grammar.addRule(new Rule("<Assign Expr 1>", new Symbol.Array("'='", "<Cond Expr>", "<Assign Expr 1>")));
        grammar.addRule(new Rule("<Assign Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Cond Expr>", new Symbol.Array("<Logical Or Expr>")));

        grammar.addRule(new Rule("<Logical Or Expr>", new Symbol.Array("<Logical And Expr>", "<Logical Or Expr 1>")));
        grammar.addRule(new Rule("<Logical Or Expr 1>", new Symbol.Array("'||'", "<Logical And Expr>", "<Logical Or Expr 1>")));
        grammar.addRule(new Rule("<Logical Or Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Logical And Expr>", new Symbol.Array("<Equal Expr>", "<Logical And Expr 1>")));
        grammar.addRule(new Rule("<Logical And Expr 1>", new Symbol.Array("'&&'", "<Equal Expr>", "<Logical And Expr 1>")));
        grammar.addRule(new Rule("<Logical And Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Equal Expr>", new Symbol.Array("<Relational Expr>", "<Equal Expr 1>")));
        grammar.addRule(new Rule("<Equal Expr 1>", new Symbol.Array("<Equal Op>", "<Relational Expr>", "<Equal Expr 1>")));
        grammar.addRule(new Rule("<Equal Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Relational Expr>", new Symbol.Array("<Additive Expr>", "<Relational Expr 1>")));
        grammar.addRule(new Rule("<Relational Expr 1>", new Symbol.Array("<Relational Op>", "<Additive Expr>", "<Relational Expr 1>")));
        grammar.addRule(new Rule("<Relational Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Additive Expr>", new Symbol.Array("<Mult Expr>", "<Additive Expr 1>")));
        grammar.addRule(new Rule("<Additive Expr 1>", new Symbol.Array("<Additive Op>", "<Mult Expr>", "<Additive Expr 1>")));
        grammar.addRule(new Rule("<Additive Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Mult Expr>", new Symbol.Array("<Unary Expr>", "<Mult Expr 1>")));
        grammar.addRule(new Rule("<Mult Expr 1>", new Symbol.Array("<Mult Op>", "<Unary Expr>", "<Mult Expr 1>")));
        grammar.addRule(new Rule("<Mult Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Unary Expr>", new Symbol.Array("<Postfix Expr>")));
        grammar.addRule(new Rule("<Unary Expr>", new Symbol.Array("<Unary Op>", "<Unary Expr>")));

        grammar.addRule(new Rule("<Postfix Expr>", new Symbol.Array("<Primary Expr>", "<Postfix Expr 1>")));
        grammar.addRule(new Rule("<Postfix Expr 1>", new Symbol.Array("<Postfix Op>", "<Postfix Expr 1>")));
        grammar.addRule(new Rule("<Postfix Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("IDENTIFICADOR")));
        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("NUMERO")));
        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("CADEIACARACTERES")));
        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("'true'")));
        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("'false'")));
        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("'('", "<Expr>", "')'")));

        grammar.addRule(new Rule("<Equal Op>", new Symbol.Array("'=='")));
        grammar.addRule(new Rule("<Equal Op>", new Symbol.Array("'!='")));

        grammar.addRule(new Rule("<Relational Op>", new Symbol.Array("'<'")));
        grammar.addRule(new Rule("<Relational Op>", new Symbol.Array("'>'")));
        grammar.addRule(new Rule("<Relational Op>", new Symbol.Array("'>='")));
        grammar.addRule(new Rule("<Relational Op>", new Symbol.Array("'<='")));
        grammar.addRule(new Rule("<Relational Op>", new Symbol.Array("'<='")));

        grammar.addRule(new Rule("<Additive Op>", new Symbol.Array("'+'")));
        grammar.addRule(new Rule("<Additive Op>", new Symbol.Array("'-'")));

        grammar.addRule(new Rule("<Mult Op>", new Symbol.Array("'*'")));
        grammar.addRule(new Rule("<Mult Op>", new Symbol.Array("'/'")));

        grammar.addRule(new Rule("<Unary Op>", new Symbol.Array("'++'")));
        grammar.addRule(new Rule("<Unary Op>", new Symbol.Array("'--'")));
        grammar.addRule(new Rule("<Unary Op>", new Symbol.Array("'!'")));

        grammar.addRule(new Rule("<Postfix Op>", new Symbol.Array("'++'")));
        grammar.addRule(new Rule("<Postfix Op>", new Symbol.Array("'--'")));
        grammar.addRule(new Rule("<Postfix Op>", new Symbol.Array("'['", "<Expr>", "']'")));
        grammar.addRule(new Rule("<Postfix Op>", new Symbol.Array("'('", "<Postfix Op lf>")));
        grammar.addRule(new Rule("<Postfix Op>", new Symbol.Array("'.'", "IDENTIFICADOR")));
        grammar.addRule(new Rule("<Postfix Op lf>", new Symbol.Array("')'")));
        grammar.addRule(new Rule("<Postfix Op lf>", new Symbol.Array("<Argument List>", "')'")));

        grammar.addRule(new Rule("<Argument List>", new Symbol.Array("<Assign Expr>", "<Argument List 1>")));
        grammar.addRule(new Rule("<Argument List 1>", new Symbol.Array("','", "<Assign Expr>", "<Argument List 1>")));
        grammar.addRule(new Rule("<Argument List 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Type>", new Symbol.Array("'int'", "{assign(<Type>:Aux[-1].type, int)}")));
        grammar.addRule(new Rule("<Type>", new Symbol.Array("'string'", "{assign(<Type>:Aux[-1].type, string)}")));
        grammar.addRule(new Rule("<Type>", new Symbol.Array("'float'", "{assign(<Type>:Aux[-1].type, float)}")));
        grammar.addRule(new Rule("<Type>", new Symbol.Array("'bool'", "{assign(<Type>:Aux[-1].type, bool)}")));
        grammar.addRule(new Rule("<Type>", new Symbol.Array("IDENTIFICADOR", "{assign(<Type>:Aux[-1].type, id)}")));

        grammar.setStartSymbol(new Symbol("<S>"));

        return grammar;
    }
}
