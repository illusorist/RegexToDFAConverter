import java.util.*;

public class DFA {
    private States startState;
    private Set<States> allStates;

    public DFA(NFA nfa) {
        this.allStates = new HashSet<>();
        this.startState = convert(nfa);
    }

    private States convert(NFA nfa) {
        Map<Set<States>, States> stateMap = new HashMap<>();
        Queue<Set<States>> queue = new LinkedList<>();

        Set<States> startSet = epsilonClosure(Set.of(nfa.getStartState()));
        States dfaStart = new States(startSet);
        stateMap.put(startSet, dfaStart);
        allStates.add(dfaStart);
        queue.add(startSet);

        while (!queue.isEmpty()) {
            Set<States> currentSet = queue.poll();
            States currentDFAState = stateMap.get(currentSet);

            for (char symbol : nfa.getAlphabet()) {
                Set<States> moveResult = move(currentSet, symbol);
                Set<States> closure = epsilonClosure(moveResult);

                if (closure.isEmpty()) continue;

                States targetDFAState = stateMap.get(closure);
                if (targetDFAState == null) {
                    targetDFAState = new States(closure);
                    stateMap.put(closure, targetDFAState);
                    allStates.add(targetDFAState);
                    queue.add(closure);
                }

                currentDFAState.addDFATransition(symbol, targetDFAState);
            }
        }

        return dfaStart;
    }

    private Set<States> move(Set<States> states, char symbol) {
        Set<States> result = new HashSet<>();
        for (States s : states) {
            result.addAll(s.getTransitions().stream()
                    .filter(t -> t.getSymbol() == symbol)
                    .map(Transition::getTo)
                    .toList());
        }
        return result;
    }

    private Set<States> epsilonClosure(Set<States> states) {
        Stack<States> stack = new Stack<>();
        Set<States> result = new HashSet<>(states);
        stack.addAll(states);

        while (!stack.isEmpty()) {
            States state = stack.pop();
            for (Transition t : state.getTransitions()) {
                if (t.getSymbol() == '\0') {
                    States target = t.getTo();
                    if (!result.contains(target)) {
                        result.add(target);
                        stack.push(target);
                    }
                }
            }
        }
        return result;
    }

    public void printDFA() {
        System.out.println("Start state: " + startState);
        Set<States> acceptingStates = new HashSet<>();
        for (States state : allStates) {
            if (state.Accepting()) {
                acceptingStates.add(state);
            }
        }
        System.out.println("Accepting states: " + acceptingStates);
        System.out.println("All transitions:");
        for (States state : allStates) {
            for (Map.Entry<Character, States> entry : state.getDFATransitions().entrySet()) {
                System.out.println("  " + state + " --" + entry.getKey() + "--> " + entry.getValue());
            }
        }
    }
}
