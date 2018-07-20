package br.uefs.compiler.parser.semantic;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class SymbolTable extends Hashtable<String, Map<String, Object>> {

    public static class Array extends ArrayList<SymbolTable> {

    }
}
