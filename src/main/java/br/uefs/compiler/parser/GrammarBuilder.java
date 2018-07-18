package br.uefs.compiler.parser;

/**
 * Encapsulates grammar definition
 */
public class GrammarBuilder {

    public static Grammar build() {
        Grammar grammar = new Grammar();
        grammar.addRule(new Rule("<S>", new Symbol.Array("{setScope(0)}", "<Global Declaration>", "<S 1>", "{hasOneStart()}")));

        grammar.addRule(new Rule("<S 1>", new Symbol.Array("<Global Declaration>", "<S 1>")));
        grammar.addRule(new Rule("<S 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Global Declaration>", new Symbol.Array("{incStart()}", "<Start Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new Symbol.Array("<Var Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new Symbol.Array("<Const Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new Symbol.Array("<Struct Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new Symbol.Array("<Function Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new Symbol.Array("<Procedure Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new Symbol.Array("<Typedef Def>")));

        grammar.addRule(new Rule("<Function Def>", new Symbol.Array("'function'", "<Type>", "{assign(Stack[-1].type, Aux[0].type); concat(Stack[-1].type, func)}", "<Declarator>", "'('", "{assign(Stack[-1].lex, Aux[-1].lex); incScope()}", "<Function Def lf>", "{decScope()}")));
        grammar.addRule(new Rule("<Function Def lf>", new Symbol.Array("{assign(Stack[-1].type, param)}", "<Parameter List>", "{getIdAndInsertParams(Aux[-1].lex, Aux[0].params)}", "')'", "<Compound Stmt>")));
        grammar.addRule(new Rule("<Function Def lf>", new Symbol.Array("')'", "<Compound Stmt>")));

        grammar.addRule(new Rule("<Procedure Def>", new Symbol.Array("'procedure'", "IDENTIFICADOR", "{insertSymbolType(Aux[0].lex, proc)}", "'('", "{assign(Stack[-1].lex, Aux[-1].lex); incScope()}", "<Procedure Def lf>", "{decScope()}")));
        grammar.addRule(new Rule("<Procedure Def lf>", new Symbol.Array("{assign(Stack[-1].type, param)}", "<Parameter List>", "{getIdAndInsertParams(Aux[-1].lex, Aux[0].params)}", "')'", "<Compound Stmt>")));
        grammar.addRule(new Rule("<Procedure Def lf>", new Symbol.Array("')'", "<Compound Stmt>")));

        grammar.addRule(new Rule("<Typedef Def>", new Symbol.Array("'typedef'", "<Typedef Def lf>")));
        grammar.addRule(new Rule("<Typedef Def lf>", new Symbol.Array("<Type>", "IDENTIFICADOR", "';'")));
        grammar.addRule(new Rule("<Typedef Def lf>", new Symbol.Array("<Struct Def>", "IDENTIFICADOR", "';'")));

        grammar.addRule(new Rule("<Var Def>", new Symbol.Array("'var'", "'{'", "{assign(Stack[-1].type, var)}", "<Declaration List>", "'}'")));

        grammar.addRule(new Rule("<Const Def>", new Symbol.Array("'const'", "'{'", "{assign(Stack[-1].type, const)}", "<Declaration List>", "'}'")));

        grammar.addRule(new Rule("<Struct Def>", new Symbol.Array("'struct'", "IDENTIFICADOR", "{incScope()}", "<Struct Def lf>", "{decScope()}")));
        grammar.addRule(new Rule("<Struct Def lf>", new Symbol.Array("'{'", "{assign(Stack[-1].type, struct)}", "<Declaration List>", "'}'")));
        grammar.addRule(new Rule("<Struct Def lf>", new Symbol.Array("'extends'", "IDENTIFICADOR", "'{'", "{assign(Stack[-1].type, struct)}", "<Declaration List>", "'}'")));

        grammar.addRule(new Rule("<Parameter List>", new Symbol.Array("{assign(Stack[-1].type, Aux[0].type)}", "<Parameter Declaration>", "{concat(Stack[-1].params, Aux[0].lex); assign(Stack[-1].type, Aux[0].type)}", "<Parameter List 1>", "{assign(Aux[-2].params, Aux[0].params)}")));
        grammar.addRule(new Rule("<Parameter List 1>", new Symbol.Array("','", "{assign(Stack[-1].type, Aux[-1].type)}", "<Parameter Declaration>", "{assign(Stack[-1].params, Aux[-2].params); concat(Stack[-1].params, Aux[0].lex); assign(Stack[-1].type, Aux[0].type)}", "<Parameter List 1>", "{assign(Aux[-3].params, Aux[0].params)}")));
        grammar.addRule(new Rule("<Parameter List 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Parameter Declaration>", new Symbol.Array("<Type>", "{assign(Stack[-1].type, Aux[0].type); concat(Stack[-1].type, Aux[-1].type)}", "<Declarator>", "{assign(Aux[-2].lex, Aux[0].lex)}")));

        grammar.addRule(new Rule("<Declaration List>", new Symbol.Array("{assign(Stack[-1].type, Aux[0].type)}", "<Declaration>", "{assign(Stack[-1].type, Aux[0].type)}", "<Declaration List 1>")));
        grammar.addRule(new Rule("<Declaration List 1>", new Symbol.Array("{assign(Stack[-1].type, Aux[0].type)}", "<Declaration>", "{assign(Stack[-1].type, Aux[0].type)}", "<Declaration List 1>")));
        grammar.addRule(new Rule("<Declaration List 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Declaration>", new Symbol.Array("<Type>", "{assign(Stack[-1].type, Aux[0].type); concat(Stack[-1].type, Aux[-1].type)}", "<Init Declarator List>", "';'")));

        grammar.addRule(new Rule("<Init Declarator List>", new Symbol.Array("{assign(Stack[-1].type, Aux[0].type); assign(Stack[-2].type, Aux[0].type)}", "<Init Declarator>", "<Init Declarator List 1>")));
        grammar.addRule(new Rule("<Init Declarator List 1>", new Symbol.Array("','", "{assign(Stack[-1].type, Aux[-1].type)}", "<Init Declarator>", "{assign(Stack[-1].type, Aux[-2].type)}", "<Init Declarator List 1>")));
        grammar.addRule(new Rule("<Init Declarator List 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Init Declarator>", new Symbol.Array("{assign(Stack[-1].type, Aux[0].type)}", "<Declarator>", "<Init Declarator lf>")));
        grammar.addRule(new Rule("<Init Declarator lf>", new Symbol.Array("'='", "<Initializer>")));
        grammar.addRule(new Rule("<Init Declarator lf>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Initializer>", new Symbol.Array("<Assign Expr>")));
        grammar.addRule(new Rule("<Initializer>", new Symbol.Array("'{'", "<Initializer List>", "<Initializer lf>")));
        grammar.addRule(new Rule("<Initializer lf>", new Symbol.Array("'}'")));
        grammar.addRule(new Rule("<Initializer lf>", new Symbol.Array("','", "'}'")));

        grammar.addRule(new Rule("<Initializer List>", new Symbol.Array("<Initializer>", "<Initializer List 1>")));
        grammar.addRule(new Rule("<Initializer List 1>", new Symbol.Array("','", "<Initializer>", "<Initializer List 1>")));
        grammar.addRule(new Rule("<Initializer List 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Declarator>", new Symbol.Array("IDENTIFICADOR", "{assign(Stack[-1].type, Aux[-1].type)}", "<Declarator 1>", "{insertSymbolType(Aux[-1].lex, Aux[0].type); assign(Aux[-2].lex, Aux[-1].lex)}")));
        grammar.addRule(new Rule("<Declarator 1>", new Symbol.Array("'['", "{assign(Stack[-1].type, Aux[-1].type); concat(Stack[-1].type, array)}", "<Declarator 1 lf>", "{assign(Aux[-2].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Declarator 1>", new Symbol.Array("")));
        grammar.addRule(new Rule("<Declarator 1 lf>", new Symbol.Array("<Cond Expr>", "{typeMatch(int, Aux[0].type)}", "']'", "{assign(Stack[-1].type, Aux[-2].type)}", "<Declarator 1>", "{assign(Aux[-2].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Declarator 1 lf>", new Symbol.Array("']'", "{assign(Stack[-1].type, Aux[-1].type)}", "<Declarator 1>", "{assign(Aux[-2].type, Aux[0].type)}")));

        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<Iteration Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<Expr Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("{incScope()}", "<Compound Stmt>", "{decScope()}")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<Print Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<Scan Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<If Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<Return Stmt>")));

        grammar.addRule(new Rule("<Stmt Or Declaration List>", new Symbol.Array("<Stmt>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List>", new Symbol.Array("<Var Def>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List>", new Symbol.Array("<Const Def>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List 1>", new Symbol.Array("<Stmt>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List 1>", new Symbol.Array("<Var Def>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List 1>", new Symbol.Array("<Const Def>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Start Def>", new Symbol.Array("'start'", "'('", "')'", "{incScope()}", "<Compound Stmt>", "{decScope()}")));

        grammar.addRule(new Rule("<Print Stmt>", new Symbol.Array("'print'", "'('", "<Argument List>", "')'", "';'")));

        grammar.addRule(new Rule("<Scan Stmt>", new Symbol.Array("'scan'", "'('", "<Argument List>", "')'", "';'")));

        grammar.addRule(new Rule("<Iteration Stmt>", new Symbol.Array("'while'", "'('", "<Expr>", "{typeMatch(bool, Aux[0].type)}", "')'", "<Stmt>")));

        grammar.addRule(new Rule("<If Stmt>", new Symbol.Array("'if'", "<Expr>", "{typeMatch(bool, Aux[0].type)}", "'then'", "<Stmt>", "<If Stmt lf>")));
        grammar.addRule(new Rule("<If Stmt lf>", new Symbol.Array("'else'", "<Stmt>")));
        grammar.addRule(new Rule("<If Stmt lf>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Return Stmt>", new Symbol.Array("'return'", "<Expr>", "';'")));

        grammar.addRule(new Rule("<Compound Stmt>", new Symbol.Array("'{'", "<Compound Stmt lf>")));
        grammar.addRule(new Rule("<Compound Stmt lf>", new Symbol.Array("'}'")));
        grammar.addRule(new Rule("<Compound Stmt lf>", new Symbol.Array("<Stmt Or Declaration List>", "'}'")));

        grammar.addRule(new Rule("<Expr Stmt>", new Symbol.Array("<Expr>", "';'")));
        grammar.addRule(new Rule("<Expr Stmt>", new Symbol.Array("';'")));

        grammar.addRule(new Rule("<Expr>", new Symbol.Array("<Assign Expr>", "{assign(Stack[-1].type, Aux[0].type)}", "<Expr 1>", "{assign(Aux[-2].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Expr 1>", new Symbol.Array("','", "<Assign Expr>", "{assign(Stack[-1].type, Aux[0].type)}", "<Expr 1>", "{assign(Aux[-3].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Assign Expr>", new Symbol.Array("<Cond Expr>", "{assign(Stack[-1].type, Aux[0].type)}", "<Assign Expr 1>", "{assign(Aux[-2].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Assign Expr 1>", new Symbol.Array("'='", "<Cond Expr>", "{typeMatch(Aux[-2].type, Aux[0].type); assign(Stack[-1].type, Aux[0].type)}", "<Assign Expr 1>", "{assign(Aux[-3].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Assign Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Cond Expr>", new Symbol.Array("<Logical Or Expr>", "{assign(Aux[-1].type, Aux[0].type)}")));

        grammar.addRule(new Rule("<Logical Or Expr>", new Symbol.Array("<Logical And Expr>", "{assign(Stack[-1].type, Aux[0].type)}", "<Logical Or Expr 1>", "{assign(Aux[-2].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Logical Or Expr 1>", new Symbol.Array("'||'", "<Logical And Expr>", "{typeMatch(Aux[-2].type, Aux[0].type); typeMatch(bool, Aux[0].type); assign(Stack[-1].type, bool)}", "<Logical Or Expr 1>", "{assign(Aux[-3].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Logical Or Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Logical And Expr>", new Symbol.Array("<Equal Expr>", "{assign(Stack[-1].type, Aux[0].type)}", "<Logical And Expr 1>", "{assign(Aux[-2].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Logical And Expr 1>", new Symbol.Array("'&&'", "<Equal Expr>", "{typeMatch(Aux[-2].type, Aux[0].type); typeMatch(bool, Aux[0].type); assign(Stack[-1].type, bool)}", "<Logical And Expr 1>", "{assign(Aux[-3].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Logical And Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Equal Expr>", new Symbol.Array("<Relational Expr>", "{assign(Stack[-1].type, Aux[0].type)}", "<Equal Expr 1>", "{assign(Aux[-2].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Equal Expr 1>", new Symbol.Array("{assign(Stack[-1].type, Aux[0].type)}", "<Equal Op>", "<Relational Expr>", "{typeMatch(Aux[-2].type, Aux[0].type); typeMatch(Aux[-1].musttypes, Aux[0].type); assign(Stack[-1].type, Aux[-1].returntype)}", "<Equal Expr 1>", "{assign(Aux[-3].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Equal Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Relational Expr>", new Symbol.Array("<Additive Expr>", "{assign(Stack[-1].type, Aux[0].type)}", "<Relational Expr 1>", "{assign(Aux[-2].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Relational Expr 1>", new Symbol.Array("{assign(Stack[-1].type, Aux[0].type)}", "<Relational Op>", "<Additive Expr>", "{typeMatch(Aux[-2].type, Aux[0].type); typeMatch(Aux[-1].musttypes, Aux[0].type); assign(Stack[-1].type, Aux[-1].returntype)}", "<Relational Expr 1>", "{assign(Aux[-3].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Relational Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Additive Expr>", new Symbol.Array("<Mult Expr>", "{assign(Stack[-1].type, Aux[0].type)}", "<Additive Expr 1>", "{assign(Aux[-2].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Additive Expr 1>", new Symbol.Array("{assign(Stack[-1].type, Aux[0].type)}", "<Additive Op>", "<Mult Expr>", "{typeMatch(Aux[-2].type, Aux[0].type); typeMatch(Aux[-1].musttypes, Aux[0].type); assign(Stack[-1].type, Aux[-1].returntype)}", "<Additive Expr 1>", "{assign(Aux[-3].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Additive Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Mult Expr>", new Symbol.Array("<Unary Expr>", "{assign(Stack[-1].type, Aux[0].type)}", "<Mult Expr 1>", "{assign(Aux[-2].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Mult Expr 1>", new Symbol.Array("{assign(Stack[-1].type, Aux[0].type)}", "<Mult Op>", "<Unary Expr>", "{typeMatch(Aux[-2].type, Aux[0].type); typeMatch(Aux[-1].musttypes, Aux[0].type); assign(Stack[-1].type, Aux[-1].returntype)}", "<Mult Expr 1>", "{assign(Aux[-3].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Mult Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Unary Expr>", new Symbol.Array("<Postfix Expr>", "{assign(Aux[-1].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Unary Expr>", new Symbol.Array("<Unary Op>", "<Unary Expr>", "{typeMatch(Aux[-1].musttypes, Aux[0].type); assign(Aux[-2].type, Aux[0].type)}")));

        grammar.addRule(new Rule("<Postfix Expr>", new Symbol.Array("<Primary Expr>", "{assign(Stack[-1].type, Aux[0].type)}", "<Postfix Expr 1>", "{assign(Aux[-2].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Postfix Expr 1>", new Symbol.Array("{assign(Stack[-1].type, Aux[0].type)}", "<Postfix Op>", "{typeMatch(Aux[0].musttypes, Aux[-1].type); assign(Stack[-1].type, Aux[0].returntype)}", "<Postfix Expr 1>", "{assign(Aux[-2].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Postfix Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("IDENTIFICADOR", "{getIdType(Aux[-1].type, Aux[0].lex)}")));
        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("NUMERO", "{getNumType(Aux[-1].type, Aux[0].lex)}")));
        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("CADEIACARACTERES", "{assign(Aux[-1].type, string)}")));
        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("'true'", "{assign(Aux[-1].type, bool)}")));
        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("'false'", "{assign(Aux[-1].type, bool)}")));
        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("'('", "<Expr>", "{assign(Aux[-2].type, Aux[0].type)}", "')'")));

        grammar.addRule(new Rule("<Equal Op>", new Symbol.Array("'=='", "{assign(Aux[-1].returntype, bool); concat(Aux[-1].musttypes, int); concat(Aux[-1].musttypes, float); concat(Aux[-1].musttypes, string); concat(Aux[-1].musttypes, bool)}")));
        grammar.addRule(new Rule("<Equal Op>", new Symbol.Array("'!='", "{assign(Aux[-1].returntype, bool); concat(Aux[-1].musttypes, int); concat(Aux[-1].musttypes, float); concat(Aux[-1].musttypes, string); concat(Aux[-1].musttypes, bool)}")));

        grammar.addRule(new Rule("<Relational Op>", new Symbol.Array("'<'", "{assign(Aux[-1].returntype, bool); concat(Aux[-1].musttypes, int); concat(Aux[-1].musttypes, float); concat(Aux[-1].musttypes, string)}")));
        grammar.addRule(new Rule("<Relational Op>", new Symbol.Array("'>'", "{assign(Aux[-1].returntype, bool); concat(Aux[-1].musttypes, int); concat(Aux[-1].musttypes, float); concat(Aux[-1].musttypes, string)}")));
        grammar.addRule(new Rule("<Relational Op>", new Symbol.Array("'>='", "{assign(Aux[-1].returntype, bool); concat(Aux[-1].musttypes, int); concat(Aux[-1].musttypes, float); concat(Aux[-1].musttypes, string)}")));
        grammar.addRule(new Rule("<Relational Op>", new Symbol.Array("'<='", "{assign(Aux[-1].returntype, bool); concat(Aux[-1].musttypes, int); concat(Aux[-1].musttypes, float); concat(Aux[-1].musttypes, string)}")));
        grammar.addRule(new Rule("<Relational Op>", new Symbol.Array("'<='", "{assign(Aux[-1].returntype, bool); concat(Aux[-1].musttypes, int); concat(Aux[-1].musttypes, float); concat(Aux[-1].musttypes, string)}")));

        grammar.addRule(new Rule("<Additive Op>", new Symbol.Array("'+'", "{assign(Aux[-1].returntype, Aux[-1].type); concat(Aux[-1].musttypes, int); concat(Aux[-1].musttypes, float)}")));
        grammar.addRule(new Rule("<Additive Op>", new Symbol.Array("'-'", "{assign(Aux[-1].returntype, Aux[-1].type); concat(Aux[-1].musttypes, int); concat(Aux[-1].musttypes, float)}")));

        grammar.addRule(new Rule("<Mult Op>", new Symbol.Array("'*'", "{assign(Aux[-1].returntype, Aux[-1].type); concat(Aux[-1].musttypes, int); concat(Aux[-1].musttypes, float)}")));
        grammar.addRule(new Rule("<Mult Op>", new Symbol.Array("'/'", "{assign(Aux[-1].returntype, Aux[-1].type); concat(Aux[-1].musttypes, int); concat(Aux[-1].musttypes, float)}")));

        grammar.addRule(new Rule("<Unary Op>", new Symbol.Array("'++'", "{concat(Aux[-1].musttypes, int); concat(Aux[-1].musttypes, float)}")));
        grammar.addRule(new Rule("<Unary Op>", new Symbol.Array("'--'", "{concat(Aux[-1].musttypes, int); concat(Aux[-1].musttypes, float)}")));
        grammar.addRule(new Rule("<Unary Op>", new Symbol.Array("'!'", "{concat(Aux[-1].musttypes, bool)}")));

        grammar.addRule(new Rule("<Postfix Op>", new Symbol.Array("'++'", "{assign(Aux[-1].returntype, Aux[-1].type); concat(Aux[-1].type, [int, float])}")));
        grammar.addRule(new Rule("<Postfix Op>", new Symbol.Array("'--'", "{assign(Aux[-1].returntype, Aux[-1].type); concat(Aux[-1].type, [int, float])}")));
        grammar.addRule(new Rule("<Postfix Op>", new Symbol.Array("'['", "<Expr>", "']'", "{assign(Aux[-3].returntype, Aux[-3].type);  concat(Aux[-3].musttypes, array)}")));
        grammar.addRule(new Rule("<Postfix Op>", new Symbol.Array("'('", "<Postfix Op lf>", "{assign(Aux[-2].returntype, Aux[-2].type); concat(Aux[-2].musttypes, [func, proc])}")));
        grammar.addRule(new Rule("<Postfix Op>", new Symbol.Array("'.'", "IDENTIFICADOR", "{getIdType(Aux[0].type, Aux[0].lex); assign(Aux[-2].returntype, Aux[0].type); concat(Aux[-2].musttypes, struct)}")));
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
