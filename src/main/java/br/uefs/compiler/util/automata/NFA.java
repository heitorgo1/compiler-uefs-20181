package br.uefs.compiler.util.automata;

import br.uefs.compiler.lexer.token.TokenClass;
import br.uefs.compiler.util.regex.Operand;
import br.uefs.compiler.util.regex.Operator;
import br.uefs.compiler.util.regex.Regex;
import br.uefs.compiler.util.regex.RegexElement;

import java.util.*;

/**
 * Non-deterministic Finite Automata implementation
 */
public class NFA extends Automata {
    /**
     * Character that represents an epsilon input
     */
    public static final Character EPS = new Character((char) 127);

    public NFA() {
        super();
    }

    public NFA(State startState) {
        super(startState);
    }

    public NFA withStartState(State start) {
        this.startState = start;
        return this;
    }

    /**
     * Get all States reachable from a given State
     * using only epsilon (EPS) transitions.
     *
     * @param state input State
     * @return e-closure StateSet
     */
    public StateSet eclosure(State state) {
        return eclosure(StateSet.of(state));
    }

    /**
     * Get all States reachable from a given set of States
     * using only epsilon (EPS) transitions.
     *
     * @param states input StateSet
     * @return e-closure StateSet
     */
    public StateSet eclosure(StateSet states) {
        StateSet eclouseSet = new StateSet(states);
        Stack<State> stack = new Stack<>();

        for (State state : states) stack.push(state);

        while (!stack.isEmpty()) {
            State u = stack.pop();

            for (State v : u.move(EPS)) {
                if (!v.equals(State.REJECT_STATE) && !eclouseSet.contains(v)) {
                    eclouseSet.add(v);
                    stack.push(v);
                }
            }
        }
        return eclouseSet;
    }

    public StateSet move(StateSet states, Character input) {
        return states.move(input);
    }

    public StateSet move(State state, Character input) {
        return state.move(input);
    }

    /**
     * Converts this NFA to DFA using the Subset Construction Algorithm.
     *
     * @return DFA corresponding to the NFA
     * @throws Exception
     * @see <a href="http://www.cs.nuim.ie/~jpower/Courses/Previous/parsing/node9.html">Subset Construction Algorithm</a>
     */
    public DFA toDFA() throws Exception {
        Set<StateSet> Dstates = new HashSet<>();
        Map<StateSet, State> stateMap = new Hashtable<>();
        List<StateSet> unmarked = new ArrayList<>();
        StateSet start = eclosure(startState);

        stateMap.put(start, start.collapse());

        Dstates.add(start);
        unmarked.add(start);

        while (!unmarked.isEmpty()) {
            StateSet T = unmarked.remove(0);

            for (Character input : T.possibleInputs()) {
                if (input.equals(EPS)) continue;
                StateSet U = eclosure(T.move(input));

                if (!Dstates.contains(U)) {
                    Dstates.add(U);
                    unmarked.add(U);
                }

                stateMap.putIfAbsent(U, U.collapse());
                stateMap.get(T).addTransaction(input, stateMap.get(U));
            }
        }

        return new DFA(stateMap.get(start));
    }

    /**
     * Instantiates a NFA for a Regex Operand
     * <p>
     * q0 ----input----> q1*
     *
     * @param input Input that will make the transition from a start State to the Final State of this NFA
     * @param tag   Tag of the new final state
     * @return new NFA
     */
    public static NFA ofSingleInput(Character input, Comparable tag, Comparable defaultTag) {
        State end = new State(tag, true);
        State start = new State(defaultTag)
                .addTransaction(input, end);

        return new NFA(start);
    }

    /**
     * Instantiates a NFA for a Regex Start (*) Operator
     * ____________________________EPS_____________________
     * /                                                     \
     * ->q0 ----EPS----> (target.q0 ---> target.q1)----EPS------> q1*
     * \___________________EPS__________________/
     *
     * @param target     NFA corresponding to the * operator
     * @param tag        Tag of the new final state
     * @param defaultTag Tag that will replace the tag of old final states
     * @return new NFA
     */
    public static NFA ofStar(NFA target, Comparable tag, Comparable defaultTag) throws Exception {
        State end = new State(tag, true);
        State start = new State(defaultTag)
                .addTransaction(EPS, target.getStartState())
                .addTransaction(EPS, end);

        StateSet targetFinalStates = target.getFinalStates();
        // target must have only one final state
        State targetFinalState = targetFinalStates.getOnlyState();
        targetFinalState
                .setIsFinal(false)
                .setTag(defaultTag)
                .addTransaction(EPS, end)
                .addTransaction(EPS, start);

        return new NFA(start);
    }

    /**
     * Instantiates a NFA for a Regex Question (?) Operator
     * <p>
     * ____________________________EPS_____________________
     * /                                                    \
     * ->q0 ----EPS----> (target.q0 ---> target.q1)----EPS------> q1*
     *
     * @param target     NFA corresponding to the ? operator
     * @param tag        Tag of the new final state
     * @param defaultTag Tag that will replace the tag of old final states
     * @return new NFA
     */
    public static NFA ofQuestion(NFA target, Comparable tag, Comparable defaultTag) throws Exception {
        State end = new State(tag, true);
        State start = new State(defaultTag)
                .addTransaction(EPS, target.getStartState())
                .addTransaction(EPS, end);

        // target must have only one final state
        target
                .getFinalStates()
                .getOnlyState()
                .setTag(defaultTag)
                .setIsFinal(false)
                .addTransaction(EPS, end);

        return new NFA(start);
    }

    /**
     * Instantiates a NFA for a Regex Union (|) Operator
     * <p>
     * ______EPS____> (target1.q0 ---> target1.q1) ___EPS____
     * /                                                       \
     * q0                                                        --> q1*
     * \                                                        /
     * ------EPS------> (target2.q0 ---> target2.q1) ---EPS---
     *
     * @param target1    NFA to be united
     * @param target2    NFA to be united
     * @param tag        Tag of the new final state
     * @param defaultTag Tag that will replace the tag of old final states
     * @return new NFA
     */
    public static NFA ofUnion(NFA target1, NFA target2, Comparable tag, Comparable defaultTag) throws Exception {
        State end = new State(tag, true);
        State start = new State(defaultTag)
                .addTransaction(EPS, target1.getStartState())
                .addTransaction(EPS, target2.getStartState());

        // targets must have only one final state
        State target1FinalState = target1.getFinalStates().getOnlyState()
                .setIsFinal(false)
                .setTag(defaultTag);
        State target2FinalState = target2.getFinalStates().getOnlyState()
                .setIsFinal(false)
                .setTag(defaultTag);

        target1FinalState.addTransaction(EPS, end);
        target2FinalState.addTransaction(EPS, end);

        return new NFA(start);
    }

    /**
     * Instantiates a NFA for a Regex Concat (.) Operator
     * <p>
     * target1.q0 ---> (target1.q1 & target2.q0) ---> target2.q1*
     *
     * @param target1    NFA that comes first in the concatenation
     * @param target2    NFA that comes later in the concatenation
     * @param defaultTag Tag that will replace the tag of old final states
     * @return new NFA
     */
    public static NFA ofConcat(NFA target1, NFA target2, Comparable defaultTag) throws Exception {
        // targets must have only one final state
        target1.getFinalStates().getOnlyState()
                .setIsFinal(false)
                .setTag(defaultTag)
                .addAllTransactions(target2.getStartState().getTransitions());

        return new NFA(target1.getStartState());
    }

    /**
     * Group many NFAs into a single one by creating a new start State
     * and making an epsilon transition from it to every other start State.
     *
     * @param nfas       NFAs to be merged
     * @param defaultTag Tag of the new start State
     * @return new NFA
     */
    public static NFA merge(List<NFA> nfas, TokenClass defaultTag) {
        State start = new State(defaultTag);

        for (NFA nfa : nfas) {
            start.addTransaction(EPS, nfa.getStartState());
        }

        return new NFA(start);
    }

    /**
     * Converts a Regular Expression to an NFA and tag it.
     *
     * @param expression Regular Expression as String
     * @param tag        Tag of the new NFA final State
     * @param defaultTag Tag for other States that are not final
     * @return new NFA that recognizes the input expression
     * @throws Exception in case the expression can't be converted
     */
    public static NFA fromRegexExpression(String expression, Comparable tag, Comparable defaultTag) throws Exception {
        Regex regex = new Regex(expression);
        List<RegexElement> postfix = regex.toPostfix();

        Stack<NFA> subnfas = new Stack<>();

        for (RegexElement el : postfix) {
            if (Operand.class.isInstance(el)) {
                subnfas.push(NFA.ofSingleInput(el.getValue(), tag, defaultTag));
            } else if (Operator.class.isInstance(el)) {
                switch (el.getValue()) {
                    case '*':
                        NFA targetStar = subnfas.pop();
                        subnfas.push(NFA.ofStar(targetStar, tag, defaultTag));
                        break;
                    case '?':
                        NFA targetQuestion = subnfas.pop();
                        subnfas.push(NFA.ofQuestion(targetQuestion, tag, defaultTag));
                        break;
                    case '|':
                        NFA targetUnion2 = subnfas.pop();
                        NFA targetUnion1 = subnfas.pop();
                        subnfas.push(NFA.ofUnion(targetUnion1, targetUnion2, tag, defaultTag));
                        break;
                    case '.':
                        NFA targetConcat2 = subnfas.pop();
                        NFA targetConcat1 = subnfas.pop();
                        subnfas.push(NFA.ofConcat(targetConcat1, targetConcat2, defaultTag));
                        break;
                    default:
                        throw new Exception("Can't convert this operator to NFA: " + el.getValue());
                }
            } else throw new Exception("Unrecognized RegexElement: " + el.getValue());
        }

        return subnfas.pop();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NFA{\n");
        for (State st : getAllStates()) {
            if (st.equals(startState)) sb.append("*");
            sb.append(st.toString());
            sb.append("\n");
        }
        sb.append("}\n");
        return sb.toString();
    }
}
