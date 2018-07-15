# Compiler for EXA869 UEFS 2018.1
Compiler Front-end for EXA869 MI Processadores de Linguagem de Programação, UEFS 2018.1.

## Problems 

Problems to be solved and their deadlines.

1. [x] Lexical Analyzer - 22/04/2018
2. [x] [Context Free Grammar](https://docs.google.com/document/d/1bHU1Tl3i42zqa6bDExDx2aRCMPcjzEm15IVa4rdEDZg/edit) - 20/05/2018
3. [x] Syntax Analyzer - 03/07/2018
4. [ ] Semantic Analyzer - 29/07/2018

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

### Context Free Grammar

See `grammar.txt`

### Testing

For linux, go to the command line and type these commands:
```
sudo chmod +x gradlew
./gradlew test
```

For windows, go to cmd and type this command:
```
gradlew.bat test
```

### Running

Before running, place your input files inside the folder `./entrada`.

For linux, go to the command line and type these commands:
```
sudo chmod +x gradlew
./gradlew run
```

For windows, go to cmd and type this command:
```
gradlew.bat run
```

The output for each input file can be found in folder `./saida`.