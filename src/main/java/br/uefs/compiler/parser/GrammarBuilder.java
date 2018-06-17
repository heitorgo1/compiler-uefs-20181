package br.uefs.compiler.parser;

public class GrammarBuilder {

    public Grammar build() {
        Grammar grammar = new Grammar();
        grammar.addRule(new Rule("<S>", new SymbolArray("<Global Declaration>", "<S 1>")));
        grammar.addRule(new Rule("<S 1>", new SymbolArray("<Global Declaration>", "<S 1>")));
        grammar.addRule(new Rule("<S 1>", new SymbolArray("")));
        grammar.addRule(new Rule("<Global Declaration>", new SymbolArray("<Start Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new SymbolArray("<Var Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new SymbolArray("<Const Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new SymbolArray("<Struct Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new SymbolArray("<Function Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new SymbolArray("<Procedure Def>")));
        grammar.addRule(new Rule("<Global Declaration>", new SymbolArray("<Typedef Def>")));
        grammar.addRule(new Rule("<Function Def>", new SymbolArray("'function'", "<Type>", "<Declarator>", "'('", "<Parameter List>", "')'", "'{'", "<Stmt Or Declaration List>", "'}'")));
        grammar.addRule(new Rule("<Procedure Def>", new SymbolArray("'procedure'", "IDENTIFICADOR", "'('", "<Parameter List>", "')'", "'{'", "<Stmt Or Declaration List>", "'}'")));
        grammar.addRule(new Rule("<Typedef Def>", new SymbolArray("'typedef'", "<Typedef Def lf>")));
        grammar.addRule(new Rule("<Typedef Def lf>", new SymbolArray("<Type>", "IDENTIFICADOR", "';'")));
        grammar.addRule(new Rule("<Typedef Def lf>", new SymbolArray("<Struct Def>", "IDENTIFICADOR", "';'")));
        grammar.addRule(new Rule("<Var Def>", new SymbolArray("'var'", "'{'", "<Declaration List>", "'}'")));
        grammar.addRule(new Rule("<Const Def>", new SymbolArray("'const'", "'{'", "<Declaration List>", "'}'")));
        grammar.addRule(new Rule("<Struct Def>", new SymbolArray("'struct'", "IDENTIFICADOR", "<Struct Def lf>")));
        grammar.addRule(new Rule("<Struct Def lf>", new SymbolArray("'{'", "<Declaration List>", "'}'")));
        grammar.addRule(new Rule("<Struct Def lf>", new SymbolArray("'extends'", "IDENTIFICADOR", "'{'", "<Declaration List>", "'}'")));
        grammar.addRule(new Rule("<Parameter List>", new SymbolArray("<Parameter Declaration>", "<Parameter List 1>")));
        grammar.addRule(new Rule("<Parameter List 1>", new SymbolArray("','", "<Parameter Declaration>", "<Parameter List 1>")));
        grammar.addRule(new Rule("<Parameter List 1>", new SymbolArray("")));
        grammar.addRule(new Rule("<Parameter Declaration>", new SymbolArray("<Type>", "<Declarator>")));
        grammar.addRule(new Rule("<Declaration List>", new SymbolArray("<Declaration>", "<Declaration List 1>")));
        grammar.addRule(new Rule("<Declaration List 1>", new SymbolArray("<Declaration>", "<Declaration List 1>")));
        grammar.addRule(new Rule("<Declaration List 1>", new SymbolArray("")));
        grammar.addRule(new Rule("<Declaration>", new SymbolArray("<Type>", "<Init Declarator List>", "';'")));
        grammar.addRule(new Rule("<Init Declarator List>", new SymbolArray("<Init Declarator>", "<Init Declarator List 1>")));
        grammar.addRule(new Rule("<Init Declarator List 1>", new SymbolArray("','", "<Init Declarator>", "<Init Declarator List 1>")));
        grammar.addRule(new Rule("<Init Declarator List 1>", new SymbolArray("")));
        grammar.addRule(new Rule("<Init Declarator>", new SymbolArray("<Declarator>", "<Init Declarator lf>")));
        grammar.addRule(new Rule("<Init Declarator lf>", new SymbolArray("'='", "<Initializer>")));
        grammar.addRule(new Rule("<Init Declarator lf>", new SymbolArray("")));
        grammar.addRule(new Rule("<Initializer>", new SymbolArray("<Assign Expr>")));
        grammar.addRule(new Rule("<Initializer>", new SymbolArray("'{'", "<Initializer List>", "<Initializer lf>")));
        grammar.addRule(new Rule("<Initializer lf>", new SymbolArray("'}'")));
        grammar.addRule(new Rule("<Initializer lf>", new SymbolArray("','", "'}'")));
        grammar.addRule(new Rule("<Initializer List>", new SymbolArray("<Initializer>", "<Initializer List 1>")));
        grammar.addRule(new Rule("<Initializer List 1>", new SymbolArray("','", "<Initializer>", "<Initializer List 1>")));
        grammar.addRule(new Rule("<Initializer List 1>", new SymbolArray("")));
        grammar.addRule(new Rule("<Declarator>", new SymbolArray("IDENTIFICADOR", "<Declarator 1>")));
        grammar.addRule(new Rule("<Declarator 1>", new SymbolArray("'['", "<Declarator 1 lf>")));
        grammar.addRule(new Rule("<Declarator 1>", new SymbolArray("")));
        grammar.addRule(new Rule("<Declarator 1 lf>", new SymbolArray("<Cond Expr>", "']'", "<Declarator 1>")));
        grammar.addRule(new Rule("<Declarator 1 lf>", new SymbolArray("']'", "<Declarator 1>")));
        grammar.addRule(new Rule("<Stmt>", new SymbolArray("<Iteration Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new SymbolArray("<Expr Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new SymbolArray("<Compound Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new SymbolArray("<Print Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new SymbolArray("<Scan Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new SymbolArray("<If Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new SymbolArray("<Return Stmt>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List>", new SymbolArray("<Stmt>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List>", new SymbolArray("<Var Def>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List 1>", new SymbolArray("<Stmt>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List 1>", new SymbolArray("<Var Def>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List 1>", new SymbolArray("")));
        grammar.addRule(new Rule("<Start Def>", new SymbolArray("'start'", "'('", "')'", "'{'", "<Stmt Or Declaration List>", "'}'")));
        grammar.addRule(new Rule("<Print Stmt>", new SymbolArray("'print'", "'('", "<Argument List>", "')'", "';'")));
        grammar.addRule(new Rule("<Scan Stmt>", new SymbolArray("'scan'", "'('", "<Argument List>", "')'", "';'")));
        grammar.addRule(new Rule("<Iteration Stmt>", new SymbolArray("'while'", "'('", "<Expr>", "')'", "<Stmt>")));
        grammar.addRule(new Rule("<If Stmt>", new SymbolArray("'if'", "<Expr>", "'then'", "<Stmt>", "<If Stmt lf>")));
        grammar.addRule(new Rule("<If Stmt lf>", new SymbolArray("'else'", "<Stmt>")));
        grammar.addRule(new Rule("<If Stmt lf>", new SymbolArray("")));
        grammar.addRule(new Rule("<Return Stmt>", new SymbolArray("'return'", "<Expr>", "';'")));
        grammar.addRule(new Rule("<Compound Stmt>", new SymbolArray("'{'", "<Compound Stmt lf>")));
        grammar.addRule(new Rule("<Compound Stmt lf>", new SymbolArray("'}'")));
        grammar.addRule(new Rule("<Compound Stmt lf>", new SymbolArray("<Stmt Or Declaration List>", "'}'")));
        grammar.addRule(new Rule("<Expr Stmt>", new SymbolArray("<Expr>", "';'")));
        grammar.addRule(new Rule("<Expr Stmt>", new SymbolArray("';'")));
        grammar.addRule(new Rule("<Expr>", new SymbolArray("<Assign Expr>", "<Expr 1>")));
        grammar.addRule(new Rule("<Expr 1>", new SymbolArray("','", "<Assign Expr>", "<Expr 1>")));
        grammar.addRule(new Rule("<Expr 1>", new SymbolArray("")));
        grammar.addRule(new Rule("<Assign Expr>", new SymbolArray("<Cond Expr>")));
        grammar.addRule(new Rule("<Assign Expr>", new SymbolArray("<Postfix Expr>", "'='", "<Assign Expr>")));
        grammar.addRule(new Rule("<Cond Expr>", new SymbolArray("<Logical Or Expr>")));
        grammar.addRule(new Rule("<Logical Or Expr>", new SymbolArray("<Logical And Expr>", "<Logical Or Expr 1>")));
        grammar.addRule(new Rule("<Logical Or Expr 1>", new SymbolArray("'||'", "<Logical And Expr>", "<Logical Or Expr 1>")));
        grammar.addRule(new Rule("<Logical Or Expr 1>", new SymbolArray("")));
        grammar.addRule(new Rule("<Logical And Expr>", new SymbolArray("<Equal Expr>", "<Logical And Expr 1>")));
        grammar.addRule(new Rule("<Logical And Expr 1>", new SymbolArray("'&&'", "<Equal Expr>", "<Logical And Expr 1>")));
        grammar.addRule(new Rule("<Logical And Expr 1>", new SymbolArray("")));
        grammar.addRule(new Rule("<Equal Expr>", new SymbolArray("<Relational Expr>", "<Equal Expr 1>")));
        grammar.addRule(new Rule("<Equal Expr 1>", new SymbolArray("<Equal Op>", "<Relational Expr>", "<Equal Expr 1>")));
        grammar.addRule(new Rule("<Equal Expr 1>", new SymbolArray("")));
        grammar.addRule(new Rule("<Relational Expr>", new SymbolArray("<Additive Expr>", "<Relational Expr 1>")));
        grammar.addRule(new Rule("<Relational Expr 1>", new SymbolArray("<Relational Op>", "<Additive Expr>", "<Relational Expr 1>")));
        grammar.addRule(new Rule("<Relational Expr 1>", new SymbolArray("")));
        grammar.addRule(new Rule("<Additive Expr>", new SymbolArray("<Mult Expr>", "<Additive Expr 1>")));
        grammar.addRule(new Rule("<Additive Expr 1>", new SymbolArray("<Additive Op>", "<Mult Expr>", "<Additive Expr 1>")));
        grammar.addRule(new Rule("<Additive Expr 1>", new SymbolArray("")));
        grammar.addRule(new Rule("<Mult Expr>", new SymbolArray("<Unary Expr>", "<Mult Expr 1>")));
        grammar.addRule(new Rule("<Mult Expr 1>", new SymbolArray("<Mult Op>", "<Unary Expr>", "<Mult Expr 1>")));
        grammar.addRule(new Rule("<Mult Expr 1>", new SymbolArray("")));
        grammar.addRule(new Rule("<Unary Expr>", new SymbolArray("<Unary Op>", "<Unary Expr>")));
        grammar.addRule(new Rule("<Unary Expr>", new SymbolArray("<Postfix Expr>")));
        grammar.addRule(new Rule("<Postfix Expr>", new SymbolArray("<Primary Expr>", "<Postfix Expr 1>")));
        grammar.addRule(new Rule("<Postfix Expr 1>", new SymbolArray("<Postfix Op>", "<Postfix Expr 1>")));
        grammar.addRule(new Rule("<Postfix Expr 1>", new SymbolArray("")));
        grammar.addRule(new Rule("<Primary Expr>", new SymbolArray("IDENTIFICADOR")));
        grammar.addRule(new Rule("<Primary Expr>", new SymbolArray("NUMERO")));
        grammar.addRule(new Rule("<Primary Expr>", new SymbolArray("CADEIACARECTERES")));
        grammar.addRule(new Rule("<Primary Expr>", new SymbolArray("'true'")));
        grammar.addRule(new Rule("<Primary Expr>", new SymbolArray("'false'")));
        grammar.addRule(new Rule("<Primary Expr>", new SymbolArray("'('", "<Expr>", "')'")));
        grammar.addRule(new Rule("<Equal Op>", new SymbolArray("'=='")));
        grammar.addRule(new Rule("<Equal Op>", new SymbolArray("'!='")));
        grammar.addRule(new Rule("<Relational Op>", new SymbolArray("'<'")));
        grammar.addRule(new Rule("<Relational Op>", new SymbolArray("'>'")));
        grammar.addRule(new Rule("<Relational Op>", new SymbolArray("'>='")));
        grammar.addRule(new Rule("<Relational Op>", new SymbolArray("'<='")));
        grammar.addRule(new Rule("<Relational Op>", new SymbolArray("'<='")));
        grammar.addRule(new Rule("<Additive Op>", new SymbolArray("'+'")));
        grammar.addRule(new Rule("<Additive Op>", new SymbolArray("'-'")));
        grammar.addRule(new Rule("<Mult Op>", new SymbolArray("'*'")));
        grammar.addRule(new Rule("<Mult Op>", new SymbolArray("'/'")));
        grammar.addRule(new Rule("<Unary Op>", new SymbolArray("'++'")));
        grammar.addRule(new Rule("<Unary Op>", new SymbolArray("'--'")));
        grammar.addRule(new Rule("<Unary Op>", new SymbolArray("'!'")));
        grammar.addRule(new Rule("<Postfix Op>", new SymbolArray("'++'")));
        grammar.addRule(new Rule("<Postfix Op>", new SymbolArray("'--'")));
        grammar.addRule(new Rule("<Postfix Op>", new SymbolArray("'['", "<Expr>", "']'")));
        grammar.addRule(new Rule("<Postfix Op>", new SymbolArray("'('", "<Postfix Op lf>")));
        grammar.addRule(new Rule("<Postfix Op>", new SymbolArray("'.'", "IDENTIFICADOR")));
        grammar.addRule(new Rule("<Postfix Op lf>", new SymbolArray("')'")));
        grammar.addRule(new Rule("<Postfix Op lf>", new SymbolArray("<Argument List>", "')'")));
        grammar.addRule(new Rule("<Argument List>", new SymbolArray("<Assign Expr>", "<Argument List 1>")));
        grammar.addRule(new Rule("<Argument List 1>", new SymbolArray("','", "<Assign Expr>", "<Argument List 1>")));
        grammar.addRule(new Rule("<Argument List 1>", new SymbolArray("")));
        grammar.addRule(new Rule("<Type>", new SymbolArray("'int'")));
        grammar.addRule(new Rule("<Type>", new SymbolArray("'string'")));
        grammar.addRule(new Rule("<Type>", new SymbolArray("'float'")));
        grammar.addRule(new Rule("<Type>", new SymbolArray("'bool'")));
        grammar.addRule(new Rule("<Type>", new SymbolArray("IDENTIFICADOR")));

        grammar.setStartSymbol(new Symbol("<S>"));
        return grammar;
    }
}
