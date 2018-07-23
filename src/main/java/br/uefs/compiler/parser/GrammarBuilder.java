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

        grammar.addRule(new Rule("<Function Def>", new Symbol.Array("'function'", "<Type>", "<Declarator>", "{concat(Aux[-1].type, Aux[0].dim); insertSymbol(Aux[0].lex, Aux[0].line, Aux[-1].type, func)}", "'('", "{assign(Stack[-1].lex, Aux[-1].lex); incScope()}", "<Function Def lf>", "{decScope()}")));
        grammar.addRule(new Rule("<Function Def lf>", new Symbol.Array("<Parameter List>", "{insertFuncParams(Aux[-1].lex, Aux[0].paramtypes)}", "')'", "<Compound Stmt>")));
        grammar.addRule(new Rule("<Function Def lf>", new Symbol.Array("{initializeParams(Aux[0].lex)}", "')'", "<Compound Stmt>")));

        grammar.addRule(new Rule("<Procedure Def>", new Symbol.Array("'procedure'", "IDENTIFICADOR", "{insertSymbol(Aux[0].lex, Aux[0].line, null, proc)}", "'('", "{assign(Stack[-1].lex, Aux[-1].lex); incScope()}", "<Procedure Def lf>", "{decScope()}")));
        grammar.addRule(new Rule("<Procedure Def lf>", new Symbol.Array("<Parameter List>", "{insertFuncParams(Aux[-1].lex, Aux[0].paramtypes)}", "')'", "<Compound Stmt>")));
        grammar.addRule(new Rule("<Procedure Def lf>", new Symbol.Array("{initializeParams(Aux[0].lex)}", "')'", "<Compound Stmt>")));

        grammar.addRule(new Rule("<Typedef Def>", new Symbol.Array("'typedef'", "<Type>", "IDENTIFICADOR", "{typedef(Aux[0].lex, Aux[0].line, Aux[-1].type)}", "';'")));

        grammar.addRule(new Rule("<Var Def>", new Symbol.Array("'var'", "'{'", "{assign(Stack[-1].cat, var)}", "<Declaration List>", "'}'")));

        grammar.addRule(new Rule("<Const Def>", new Symbol.Array("'const'", "'{'", "{assign(Stack[-1].cat, const)}", "<Declaration List>", "'}'")));

        grammar.addRule(new Rule("<Struct Def>", new Symbol.Array("'struct'", "IDENTIFICADOR", "{typedef(Aux[0].lex, Aux[0].line, struct); assign(Stack[-1].lex, Aux[0].lex); incScope()}", "<Struct Def lf>", "{decScope()}")));
        grammar.addRule(new Rule("<Struct Def lf>", new Symbol.Array("'{'", "{assign(Stack[-1].cat, struct_var)}", "<Declaration List>", "{attachStructVars(Aux[-2].lex)}", "'}'")));
        grammar.addRule(new Rule("<Struct Def lf>", new Symbol.Array("'extends'", "IDENTIFICADOR", "{checkStructDefined(Aux[0].lex)}", "'{'", "{assign(Stack[-1].cat, struct_var)}", "<Declaration List>", "{attachStructVars(Aux[-4].lex); extendStruct(Aux[-4].lex, Aux[-2].lex)}", "'}'")));
        // sy: paramtypes
        grammar.addRule(new Rule("<Parameter List>", new Symbol.Array("<Parameter Declaration>", "{append(Stack[-1].paramtypes, Aux[0].type)}", "<Parameter List 1>", "{assign(Aux[-2].paramtypes, Aux[0].paramtypes)}")));
        grammar.addRule(new Rule("<Parameter List 1>", new Symbol.Array("','", "<Parameter Declaration>", "{assign(Stack[-1].paramtypes, Aux[-2].paramtypes); append(Stack[-1].paramtypes, Aux[0].type)}", "<Parameter List 1>", "{assign(Aux[-3].paramtypes, Aux[0].paramtypes)}")));
        grammar.addRule(new Rule("<Parameter List 1>", new Symbol.Array("")));
        // sy: type
        grammar.addRule(new Rule("<Parameter Declaration>", new Symbol.Array("<Type>", "<Declarator>", "{concat(Aux[-1].type, Aux[0].dim); insertSymbol(Aux[0].lex, Aux[0].line, Aux[-1].type, param); assign(Aux[-2].type, Aux[-1].type)}")));
        // sy: | inh: cat
        grammar.addRule(new Rule("<Declaration List>", new Symbol.Array("{assign(Stack[-1].cat, Aux[0].cat)}", "<Declaration>", "{assign(Stack[-1].cat, Aux[-1].cat)}", "<Declaration List 1>")));
        grammar.addRule(new Rule("<Declaration List 1>", new Symbol.Array("{assign(Stack[-1].cat, Aux[0].cat)}", "<Declaration>", "{assign(Stack[-1].cat, Aux[-1].cat)}", "<Declaration List 1>")));
        grammar.addRule(new Rule("<Declaration List 1>", new Symbol.Array("")));
        // sy: | inh: cat
        grammar.addRule(new Rule("<Declaration>", new Symbol.Array("<Type>", "{assign(Stack[-1].type, Aux[0].type); assign(Stack[-1].cat, Aux[-1].cat)}", "<Init Declarator List>", "';'")));
        // sy: | inh: type cat
        grammar.addRule(new Rule("<Init Declarator List>", new Symbol.Array("{assign(Stack[-1].type!cat, Aux[0].type!cat)}", "<Init Declarator>", "{assign(Stack[-1].type!cat, Aux[0].type!cat)}", "<Init Declarator List 1>"))); // Declaration type
        grammar.addRule(new Rule("<Init Declarator List 1>", new Symbol.Array("','", "{assign(Stack[-1].type!cat, Aux[-1].type!cat)}", "<Init Declarator>", "{assign(Stack[-1].type!cat, Aux[0].type!cat)}", "<Init Declarator List 1>")));
        grammar.addRule(new Rule("<Init Declarator List 1>", new Symbol.Array("")));
        // sy: lex line dim init |  inh: type cat
        grammar.addRule(new Rule("<Init Declarator>", new Symbol.Array("<Declarator>", "{concat(Aux[-1].type, Aux[0].dim); insertSymbol(Aux[0].lex, Aux[0].line, Aux[-1].type, Aux[-1].cat); assign(Stack[-1].ltype, Aux[-1].type); assign(Stack[-1].init, false)}", "<Init Declarator lf>", "{assertInitConst(Aux[0].init, Aux[-2].cat)}"))); // Init Declarator List or Init Declarator List 1 type
        grammar.addRule(new Rule("<Init Declarator lf>", new Symbol.Array("'='", "{assign(Aux[-1].init, true); assign(Stack[-1].ltype, Aux[-1].ltype); assign(Stack[-1].rdim, '')}", "<Initializer>"))); // Init Declarator type
        grammar.addRule(new Rule("<Init Declarator lf>", new Symbol.Array("")));
        // sy: | inh: rdim ltype
        grammar.addRule(new Rule("<Initializer>", new Symbol.Array("<Assign Expr>", "{concat(Aux[0].type, Aux[-1].rdim); typeMatch(Aux[-1].ltype, Aux[0].type)}"))); // Init Declarator lf ltype, dim, line, lex
        grammar.addRule(new Rule("<Initializer>", new Symbol.Array("'{'", "{assign(Stack[-1].ltype, Aux[-1].ltype); concat(Stack[-1].rdim, Aux[-1].rdim, [])}", "<Initializer List>", "'}'")));
        // sy: | inh: rdim ltype
        grammar.addRule(new Rule("<Initializer List>", new Symbol.Array("{assign(Stack[-1].rdim!ltype, Aux[0].rdim!ltype); assign(Stack[-2].rdim!ltype, Aux[0].rdim!ltype)}", "<Initializer>", "<Initializer List 1>")));
        grammar.addRule(new Rule("<Initializer List 1>", new Symbol.Array("','", "{assign(Stack[-1].rdim!ltype, Aux[-1].rdim!ltype); assign(Stack[-2].rdim!ltype, Aux[-1].rdim!ltype)}", "<Initializer>", "<Initializer List 1>")));
        grammar.addRule(new Rule("<Initializer List 1>", new Symbol.Array("")));
        // sy: lex line dim
        grammar.addRule(new Rule("<Declarator>", new Symbol.Array("IDENTIFICADOR", "{assign(Stack[-1].dim, '')}", "<Declarator 1>", "{assign(Aux[-2].dim, Aux[0].dim); assign(Aux[-2].lex!line, Aux[-1].lex!line)}")));
        grammar.addRule(new Rule("<Declarator 1>", new Symbol.Array("'['", "{concat(Stack[-1].dim, Aux[-1].dim, [])}", "<Declarator 1 lf>", "{assign(Aux[-2].dim, Aux[0].dim)}")));
        grammar.addRule(new Rule("<Declarator 1>", new Symbol.Array("")));
        grammar.addRule(new Rule("<Declarator 1 lf>", new Symbol.Array("<Cond Expr>", "{typeMatch(int, Aux[0].type)}", "']'", "{assign(Stack[-1].dim, Aux[-2].dim)}", "<Declarator 1>", "{assign(Aux[-3].dim, Aux[0].dim)}")));
        grammar.addRule(new Rule("<Declarator 1 lf>", new Symbol.Array("']'", "{assign(Stack[-1].dim, Aux[-1].dim)}", "<Declarator 1>", "{assign(Aux[-2].dim, Aux[0].dim)}")));

        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<Iteration Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<Expr Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("{incScope()}", "<Compound Stmt>", "{decScope()}")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<Print Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<Scan Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<If Stmt>")));
        grammar.addRule(new Rule("<Stmt>", new Symbol.Array("<Return Stmt>")));

        grammar.addRule(new Rule("<Stmt Or Declaration List>", new Symbol.Array("<Stmt>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List>", new Symbol.Array("<Var Def>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List>", new Symbol.Array("{invalidConst()}", "<Const Def>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List 1>", new Symbol.Array("<Stmt>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List 1>", new Symbol.Array("<Var Def>", "<Stmt Or Declaration List 1>")));
        grammar.addRule(new Rule("<Stmt Or Declaration List 1>", new Symbol.Array("{invalidConst()}", "<Const Def>", "<Stmt Or Declaration List 1>")));
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

        grammar.addRule(new Rule("<Assign Expr>", new Symbol.Array("<Cond Expr>", "{assign(Stack[-1].type!cat, Aux[0].type!cat)}", "<Assign Expr 1>", "{assign(Aux[-2].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Assign Expr 1>", new Symbol.Array("{checkAttribution(Aux[0].cat)}", "'='", "<Cond Expr>", "{assignedTypeMatch(Aux[-2].type, Aux[0].type); assign(Stack[-1].type!cat, Aux[0].type!cat)}", "<Assign Expr 1>", "{assign(Aux[-3].type, Aux[0].type)}")));
        grammar.addRule(new Rule("<Assign Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Cond Expr>", new Symbol.Array("<Logical Or Expr>", "{assign(Aux[-1].type!cat, Aux[0].type!cat)}")));

        grammar.addRule(new Rule("<Logical Or Expr>", new Symbol.Array("<Logical And Expr>", "{assign(Stack[-1].type!cat, Aux[0].type!cat)}", "<Logical Or Expr 1>", "{assign(Aux[-2].type!cat, Aux[0].type!cat)}")));
        grammar.addRule(new Rule("<Logical Or Expr 1>", new Symbol.Array("'||'", "<Logical And Expr>", "{typeMatch(Aux[-2].type, Aux[0].type); typeMatch(bool, Aux[0].type); assign(Stack[-1].type, bool);  assign(Stack[-1].cat, Aux[-2].cat)}", "<Logical Or Expr 1>", "{assign(Aux[-3].type!cat, Aux[0].type!cat)}")));
        grammar.addRule(new Rule("<Logical Or Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Logical And Expr>", new Symbol.Array("<Equal Expr>", "{assign(Stack[-1].type!cat, Aux[0].type!cat)}", "<Logical And Expr 1>", "{assign(Aux[-2].type!cat, Aux[0].type!cat)}")));
        grammar.addRule(new Rule("<Logical And Expr 1>", new Symbol.Array("'&&'", "<Equal Expr>", "{typeMatch(Aux[-2].type, Aux[0].type); typeMatch(bool, Aux[0].type); assign(Stack[-1].type, bool); assign(Stack[-1].cat, Aux[-2].cat)}", "<Logical And Expr 1>", "{assign(Aux[-3].type!cat, Aux[0].type!cat)}")));
        grammar.addRule(new Rule("<Logical And Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Equal Expr>", new Symbol.Array("<Relational Expr>", "{assign(Stack[-1].type!cat, Aux[0].type!cat)}", "<Equal Expr 1>", "{assign(Aux[-2].type!cat, Aux[0].type!cat)}")));
        grammar.addRule(new Rule("<Equal Expr 1>", new Symbol.Array("{assign(Stack[-1].type!cat, Aux[0].type!cat)}", "<Equal Op>", "<Relational Expr>", "{typeMatch(Aux[-2].type, Aux[0].type); typeMatch(Aux[-1].musttypes, Aux[0].type); assign(Stack[-1].type!cat, Aux[-1].returntype!cat)}", "<Equal Expr 1>", "{assign(Aux[-3].type!cat, Aux[0].type!cat)}")));
        grammar.addRule(new Rule("<Equal Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Relational Expr>", new Symbol.Array("<Additive Expr>", "{assign(Stack[-1].type!cat, Aux[0].type!cat)}", "<Relational Expr 1>", "{assign(Aux[-2].type!cat, Aux[0].type!cat)}")));
        grammar.addRule(new Rule("<Relational Expr 1>", new Symbol.Array("{assign(Stack[-1].type!cat, Aux[0].type!cat)}", "<Relational Op>", "<Additive Expr>", "{typeMatch(Aux[-2].type, Aux[0].type); typeMatch(Aux[-1].musttypes, Aux[0].type); assign(Stack[-1].type!cat, Aux[-1].returntype!cat)}", "<Relational Expr 1>", "{assign(Aux[-3].type!cat, Aux[0].type!cat)}")));
        grammar.addRule(new Rule("<Relational Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Additive Expr>", new Symbol.Array("<Mult Expr>", "{assign(Stack[-1].type!cat, Aux[0].type!cat)}", "<Additive Expr 1>", "{assign(Aux[-2].type!cat, Aux[0].type!cat)}")));
        grammar.addRule(new Rule("<Additive Expr 1>", new Symbol.Array("{assign(Stack[-1].type!cat, Aux[0].type!cat)}", "<Additive Op>", "<Mult Expr>", "{typeMatch(Aux[-2].type, Aux[0].type); typeMatch(Aux[-1].musttypes, Aux[0].type); assign(Stack[-1].type!cat, Aux[-1].returntype!cat)}", "<Additive Expr 1>", "{assign(Aux[-3].type!cat, Aux[0].type!cat)}")));
        grammar.addRule(new Rule("<Additive Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Mult Expr>", new Symbol.Array("<Unary Expr>", "{assign(Stack[-1].type!cat, Aux[0].type!cat)}", "<Mult Expr 1>", "{assign(Aux[-2].type!cat, Aux[0].type!cat)}")));
        grammar.addRule(new Rule("<Mult Expr 1>", new Symbol.Array("{assign(Stack[-1].type!cat, Aux[0].type!cat)}", "<Mult Op>", "<Unary Expr>", "{typeMatch(Aux[-2].type, Aux[0].type); typeMatch(Aux[-1].musttypes, Aux[0].type); assign(Stack[-1].type!cat, Aux[-1].returntype!cat)}", "<Mult Expr 1>", "{assign(Aux[-3].type!cat, Aux[0].type!cat)}")));
        grammar.addRule(new Rule("<Mult Expr 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Unary Expr>", new Symbol.Array("<Postfix Expr>", "{assign(Aux[-1].type!cat, Aux[0].type!cat)}")));
        grammar.addRule(new Rule("<Unary Expr>", new Symbol.Array("<Unary Op>", "<Unary Expr>", "{typeMatch(Aux[-1].musttypes, Aux[0].type); assign(Aux[-2].type!cat, Aux[0].type!cat)}")));

        grammar.addRule(new Rule("<Postfix Expr>", new Symbol.Array("<Primary Expr>", "{assign(Stack[-1].lex!type!cat, Aux[0].lex!type!cat)}", "<Postfix Expr 1>", "{assign(Aux[-2].type!cat, Aux[0].type!cat)}")));
        grammar.addRule(new Rule("<Postfix Expr 1>", new Symbol.Array("{assign(Stack[-1].lex!type!cat, Aux[0].lex!type!cat)}", "<Postfix Op>", "{assign(Stack[-1].lex!cat, Aux[0].lex!cat); assign(Stack[-1].type, Aux[0].returntype)}", "<Postfix Expr 1>", "{assign(Aux[-2].type!cat, Aux[0].type!cat)}")));
        grammar.addRule(new Rule("<Postfix Expr 1>", new Symbol.Array("")));

        // sy: lex line type cat
        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("IDENTIFICADOR", "{getIdentifier(Aux[-1], Aux[0].lex)}")));
        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("NUMERO", "{getNumType(Aux[-1].type, Aux[0].lex); assign(Aux[-1].cat, const); assign(Aux[-1].lex!line, Aux[0].lex!line)}")));
        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("CADEIACARACTERES", "{assign(Aux[-1].type, string); assign(Aux[-1].cat, const); assign(Aux[-1].lex!line, Aux[0].lex!line)}")));
        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("'true'", "{assign(Aux[-1].type, bool); assign(Aux[-1].cat, const); assign(Aux[-1].lex!line, Aux[0].lex!line)}")));
        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("'false'", "{assign(Aux[-1].type, bool); assign(Aux[-1].cat, const); assign(Aux[-1].lex!line, Aux[0].lex!line)}")));
        grammar.addRule(new Rule("<Primary Expr>", new Symbol.Array("'('", "<Expr>", "{assign(Aux[-2].type, Aux[0].type); assign(Aux[-2].cat, const); assign(Aux[-2].lex, null)}", "')'"))); // fix

        grammar.addRule(new Rule("<Equal Op>", new Symbol.Array("'=='", "{assign(Aux[-1].returntype, bool); append(Aux[-1].musttypes, [int float string bool])}")));
        grammar.addRule(new Rule("<Equal Op>", new Symbol.Array("'!='", "{assign(Aux[-1].returntype, bool); append(Aux[-1].musttypes, [int float string bool])}")));

        grammar.addRule(new Rule("<Relational Op>", new Symbol.Array("'<'", "{assign(Aux[-1].returntype, bool); append(Aux[-1].musttypes, [int float string])}")));
        grammar.addRule(new Rule("<Relational Op>", new Symbol.Array("'>'", "{assign(Aux[-1].returntype, bool); append(Aux[-1].musttypes, [int float string])}")));
        grammar.addRule(new Rule("<Relational Op>", new Symbol.Array("'>='", "{assign(Aux[-1].returntype, bool); append(Aux[-1].musttypes, [int float string])}")));
        grammar.addRule(new Rule("<Relational Op>", new Symbol.Array("'<='", "{assign(Aux[-1].returntype, bool); append(Aux[-1].musttypes, [int float string])}")));
        grammar.addRule(new Rule("<Relational Op>", new Symbol.Array("'<='", "{assign(Aux[-1].returntype, bool); append(Aux[-1].musttypes, [int float string])}")));

        grammar.addRule(new Rule("<Additive Op>", new Symbol.Array("'+'", "{assign(Aux[-1].returntype, Aux[-1].type); append(Aux[-1].musttypes, [int float string])}")));
        grammar.addRule(new Rule("<Additive Op>", new Symbol.Array("'-'", "{assign(Aux[-1].returntype, Aux[-1].type); append(Aux[-1].musttypes, [int float])}")));

        grammar.addRule(new Rule("<Mult Op>", new Symbol.Array("'*'", "{assign(Aux[-1].returntype, Aux[-1].type); append(Aux[-1].musttypes, [int float])}")));
        grammar.addRule(new Rule("<Mult Op>", new Symbol.Array("'/'", "{assign(Aux[-1].returntype, Aux[-1].type); append(Aux[-1].musttypes, [int float])}")));

        grammar.addRule(new Rule("<Unary Op>", new Symbol.Array("'++'", "{append(Aux[-1].musttypes, [int float])}")));
        grammar.addRule(new Rule("<Unary Op>", new Symbol.Array("'--'", "{append(Aux[-1].musttypes, [int float])}")));
        grammar.addRule(new Rule("<Unary Op>", new Symbol.Array("'!'", "{append(Aux[-1].musttypes, bool)}")));

        // inh: lex type cat
        grammar.addRule(new Rule("<Postfix Op>", new Symbol.Array("'++'", "{typeMatch([int float], Aux[-1].type); assertNotConst(Aux[-1].cat); assign(Aux[-1].returntype, Aux[-1].type)}")));
        grammar.addRule(new Rule("<Postfix Op>", new Symbol.Array("'--'", "{typeMatch([int float], Aux[-1].type); assertNotConst(Aux[-1].cat); assign(Aux[-1].returntype, Aux[-1].type)}")));
        grammar.addRule(new Rule("<Postfix Op>", new Symbol.Array("'['", "{popArray(Aux[-1].type)}", "<Expr>", "{typeMatch(int, Aux[0].type)}", "']'", "{assign(Aux[-3].returntype, Aux[-3].type)}"))); // fix array match
        grammar.addRule(new Rule("<Postfix Op>", new Symbol.Array("'('", "{getParams(Stack[-1].paramtypes, Aux[-1].lex)}", "<Postfix Op lf>", "{assign(Aux[-2].returntype, Aux[-2].type)}")));
        grammar.addRule(new Rule("<Postfix Op>", new Symbol.Array("'.'", "IDENTIFICADOR", "{getStructVarType(Aux[0].type, Aux[-2].lex, Aux[0].lex); assign(Aux[-2].returntype, Aux[0].type)}")));
        grammar.addRule(new Rule("<Postfix Op lf>", new Symbol.Array("')'", "{assertNoParams(Aux[-1].paramtypes)}")));
        grammar.addRule(new Rule("<Postfix Op lf>", new Symbol.Array("<Argument List>", "{assertParams(Aux[-1].paramtypes, Aux[0].paramtypes)}", "')'")));

        grammar.addRule(new Rule("<Argument List>", new Symbol.Array("<Assign Expr>", "{append(Stack[-1].paramtypes, Aux[0].type)}", "<Argument List 1>", "{assign(Aux[-2].paramtypes, Aux[0].paramtypes)}")));
        grammar.addRule(new Rule("<Argument List 1>", new Symbol.Array("','", "<Assign Expr>", "{append(Stack[-1].paramtypes, Aux[-2].paramtypes, Aux[0].type)}", "<Argument List 1>", "{assign(Aux[-3].paramtypes, Aux[0].paramtypes)}")));
        grammar.addRule(new Rule("<Argument List 1>", new Symbol.Array("")));

        grammar.addRule(new Rule("<Type>", new Symbol.Array("'int'", "{assign(Aux[-1].type, int)}")));
        grammar.addRule(new Rule("<Type>", new Symbol.Array("'string'", "{assign(Aux[-1].type, string)}")));
        grammar.addRule(new Rule("<Type>", new Symbol.Array("'float'", "{assign(Aux[-1].type, float)}")));
        grammar.addRule(new Rule("<Type>", new Symbol.Array("'bool'", "{assign(Aux[-1].type, bool)}")));
        grammar.addRule(new Rule("<Type>", new Symbol.Array("'struct'", "IDENTIFICADOR", "{checkStructDefined(Aux[0].lex); assign(Aux[-2].type, Aux[0].lex)}")));
        grammar.addRule(new Rule("<Type>", new Symbol.Array("IDENTIFICADOR", "{getDefinedType(Aux[-1].type, Aux[0].lex)}")));

        grammar.setStartSymbol(new Symbol("<S>"));

        return grammar;
    }
}
