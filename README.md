# Compiler for EXA869 UEFS 2018.1
Compiler Front-end for EXA869 MI Processadores de Linguagem de Programação, UEFS 2018.1.

## Problems 

Problems to be solved and their deadlines.

1. [ ] Lexical Analyzer - 22/04/2018
2. [ ] Context Free Grammar - unknown
3. [ ] Syntax Analyzer - unknown
4. [ ] Semantic Analyzer - unknown

### Lexical Analyzer

Lexemes description:

Problem 1 - Table 1

|    Lexeme Class      |  Description |
|:--------------------:|------------------------------------------------------------------------------------------------------------------------------------------------------|
| reserved words       | const, var, struct, typedef, procedure, function, return, start, if, then, else, while, scan,  print, int, float, bool, string, true, false, extends |
| identifier           |  letter(letter \| digit \| _ )*                                                                                                                        |
| number               | (-)? whitespace\* digit digit\*(.digit (digit)\*)?                                                                                                       |
| digit                | [0-9]                                                                                                                                                |
| letter               | [a-z] \| [A-Z]                                                                                                                                        |
| arithmetic operators | + \| - \| * \| / \| ++ \| --                                                                                                                              |
| relational operators | != \| == \| < \| <= \| > \| >= \| =                                                                                                                        |
| logic operators      | ! \| && \| \|\|                                                                                                                                          |
| comments             | // this is a line comment <br/> /\* <br/> this is a block comment <br/> \*/                                                                                           |
| delimiters           | ; \| , \| ( \| ) \| [ \| ] \| { \| } \| .                                                                                                                    |
| string               | "(letter \| digit \| symbol \| \\")\*"                                                                                                                    |
| symbol               | ASCII from 32 to 126 (except ASCII 34)                                                                                                               |
| whitespace           | ASCII 9 \| ASCII 10 \| ASCII 13 \| ASCII 32                                                                                                             |



