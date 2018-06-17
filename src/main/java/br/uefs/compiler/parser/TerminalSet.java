package br.uefs.compiler.parser;

import java.util.Arrays;
import java.util.HashSet;

public class TerminalSet extends HashSet<String> {

    public TerminalSet() {
        super();
    }

    public static TerminalSet ofSingleTerminal(String terminal) {
        TerminalSet set = new TerminalSet();
        set.add(terminal);
        return set;
    }
}
