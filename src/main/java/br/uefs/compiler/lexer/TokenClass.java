package br.uefs.compiler.lexer;

public enum TokenClass {
    IDENTIFIER(256, "\\w(\\w|\\d|_)*"),
    NUMBER(257, "(-)?\\s*\\d\\d*(.\\d(\\d)*)?"),
    RELATIONAL_OP(258, "!=|==|<|<=|>|>=|="),
    ARITHMETIC_OP(259, "+|-|*|/|++|--"),
    LOGIC_OP(260, "!|&&|||"),
    STRING(261, "\"(\\w|\\d|\\a|\\\"\""),
    DELIMITER(262, ";|,|(|)|[|]|{|}|.");

    public final int id;
    public final String regx;

    TokenClass(int id, String regx) {
        this.id = id;
        this.regx = regx;
    }
}
