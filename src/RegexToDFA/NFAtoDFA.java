package RegexToDFA;

import java.util.*;

public class NFAtoDFA {

    private static final char EPSILON = 'ε';

    private NFAtoDFA() {
        // Utility class
    }

    public static DFA convert(NFA nfa) {
        DFA dfa = new DFA();
        Map<Set<States>, States> stateMapping = new HashMap<>();
        Queue<Set<States>> queue = new LinkedList<>();

        Set<States> initialSet = new HashSet<>();
        if (nfa.getStartState() != null) {
            initialSet.add(nfa.getStartState());
        } else {
            System.err.println("NFA has no start state. Cannot convert.");
            return dfa;
        }

        Set<States> startSet = epsilonClosure(initialSet, nfa);

        States dfaStartState = new States(startSet);
        if (containsAccepting(startSet)) {
            dfaStartState.setAccepting(true);
        }

        dfa.setStartState(dfaStartState);
        stateMapping.put(startSet, dfaStartState);
        queue.add(startSet);

        while (!queue.isEmpty()) {
            Set<States> currentSet = queue.poll();
            States currentDFAState = stateMapping.get(currentSet);

            Set<Character> alphabet = new HashSet<>(nfa.getAlphabet());
            alphabet.remove(EPSILON);

            for (char symbol : alphabet) {
                Set<States> nextSetRaw = new HashSet<>();
                for (States state : currentSet) {
                    nextSetRaw.addAll(nfa.getNextStates(state, symbol));
                }

                Set<States> nextSet = epsilonClosure(nextSetRaw, nfa);

                if (nextSet.isEmpty()) continue;

                States nextDFAState = stateMapping.get(nextSet);

                if (nextDFAState == null) {
                    nextDFAState = new States(nextSet);
                    if (containsAccepting(nextSet)) {
                        nextDFAState.setAccepting(true);
                    }
                    stateMapping.put(nextSet, nextDFAState);
                    queue.add(nextSet);
                }

                dfa.addDFATransition(currentDFAState, symbol, nextDFAState);
            }
        }

        for(States dfaState : stateMapping.values()){
            dfa.addState(dfaState);
        }

        return dfa;
    }

    public static DFA convertAndPrint(NFA nfa) {
        DFA dfa = convert(nfa);
        System.out.println("\n--- DFA Result ---");
        printDFA(dfa);
        return dfa;
    }

    public static void printDFA(DFA dfa) {
        if (dfa == null) {
            System.out.println("DFA is null.");
            return;
        }
        dfa.printDFA();
    }

    private static Set<States> epsilonClosure(Set<States> states, NFA nfa) {
        Set<States> closure = new HashSet<>(states);
        Stack<States> stack = new Stack<>();

        for (States state : states) {
            stack.push(state);
        }

        while (!stack.isEmpty()) {
            States currentState = stack.pop();
            Set<States> epsilonReachable = nfa.getNextStates(currentState, EPSILON);

            for (States nextState : epsilonReachable) {
                if (closure.add(nextState)) {
                    stack.push(nextState);
                }
            }
        }
        return closure;
    }

    private static boolean containsAccepting(Set<States> states) {
        for (States s : states) {
            if (s.Accepting()) {
                return true;
            }
        }
        return false;
    }

    private static String setToName(Set<States> states) {
        if (states == null || states.isEmpty()) {
            return "{}";
        }
        List<String> names = new ArrayList<>();
        for (States s : states) {
            names.add("q" + s.getId());
        }
        Collections.sort(names);
        return "{" + String.join(",", names) + "}";
    }
}
