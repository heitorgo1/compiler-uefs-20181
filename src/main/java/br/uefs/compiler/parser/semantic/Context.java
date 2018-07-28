package br.uefs.compiler.parser.semantic;

import br.uefs.compiler.lexer.token.Token;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Context {

    private int scope = 0;
    private int startCounter = 0;
    private long startLine = 0;
    private boolean supressErrors = false;
    private SymbolTable.Array symbolTableList;
    private Map<String, Map<String, Object>> typeMap;
    private Token currentToken;
    private List<SemanticError> errors;

    public Context(SymbolTable.Array tableList) {
        this.symbolTableList = tableList;
        errors = new ArrayList<>();
        typeMap = new Hashtable<>();
    }

    public void setCurrentToken(Token token) {
        this.currentToken = token;
    }

    public int getStartCounter() {
        return startCounter;
    }

    public long getStartLine() {
        return startLine;
    }

    public void incrementStartCounter() {
        startCounter++;
    }

    public Map<String, Map<String, Object>> getTypeMap() {
        return typeMap;
    }

    public void setCurrentScope(int i) {
        assert i >= 0;

        scope = i;

        while (symbolTableList.size() < scope + 1) {
            symbolTableList.add(new SymbolTable());
        }
    }

    public void incrementScope() {

        scope++;


        if (symbolTableList.size() < scope + 1) {
            symbolTableList.add(new SymbolTable());
        }
    }

    public void decrementScope() {
        if (symbolTableList.size() > 0) {
            symbolTableList.remove(scope);
        }

        scope--;
    }

    public int getScope() {
        return scope;
    }

    public SymbolTable getSymbolTable(int scope) {
        return symbolTableList.get(scope);
    }

    public SymbolTable getCurrentScopeSymbolTable() {
        return symbolTableList.get(scope);
    }

    public boolean isDeclaredInCurrentScope(String name) {
        return symbolTableList.get(scope).containsKey(name);
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public void setSupressErrors(boolean value) {
        this.supressErrors = value;
    }

    public boolean isSupressingErrors() {
        return supressErrors;
    }

    public void addError(SemanticError err) {
        if (!supressErrors)
            errors.add(err);
    }

    public List<SemanticError> getErrors() {
        return errors;
    }

    public Map<String, Object> getInCurrentScope(String input) {
        getCurrentScopeSymbolTable().putIfAbsent(input, new Hashtable<>());
        return getCurrentScopeSymbolTable().get(input);
    }

    public SymbolTable.Array getSymbolTableList() {
        return symbolTableList;
    }

    public void setStartLine(long line) {
        startLine = line;
    }
}
