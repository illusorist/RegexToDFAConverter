package mainPackage;

import java.util.*;

public class NfaToDfa {

    public static DFA convert(NFA nfa) {
        DFA dfa = new DFA();
        Map<Set<States>, States> stateMapping = new HashMap<>();
        Queue<Set<States>> queue = new LinkedList<>();
        
        // Start state of DFA is the epsilon closure of NFA's start state
        Set<States> startSet = epsilonClosure(Set.of(nfa.getStartState()));
        States dfaStartState = new States(setToName(startSet));
        if (containsAccepting(startSet)) {
            dfaStartState.setAccepting(true);
        }
        dfa.setStartState(dfaStartState);
        stateMapping.put(startSet, dfaStartState);
        queue.add(startSet);

        while (!queue.isEmpty()) {
            Set<States> currentSet = queue.poll();
            States currentDFAState = stateMapping.get(currentSet);

            for (char symbol : nfa.getAlphabet()) {
                Set<States> nextSet = new HashSet<>();
                for (States state : currentSet) {
                    nextSet.addAll(nfa.getNextStates(state, symbol));
                }

                nextSet = epsilonClosure(nextSet);
                if (nextSet.isEmpty()) continue;

                States nextDFAState = stateMapping.get(nextSet);
                if (nextDFAState == null) {
                    nextDFAState = new States(setToName(nextSet));
                    if (containsAccepting(nextSet)) {
                        nextDFAState.setAccepting(true);
                    }
                    stateMapping.put(nextSet, nextDFAState);
                    queue.add(nextSet);
                }

                dfa.addDFATransition(currentDFAState, symbol, nextDFAState);
            }
        }

        return dfa;
    }

    private static Set<States> epsilonClosure(Set<States> states) {
        // Add support for ε-transitions if your NFA has them.
        return states;
    }

    private static boolean containsAccepting(Set<States> states) {
        for (States s : states) {
            if (s.Accepting()) return true;
        }
        return false;
    }

    private static String setToName(Set<States> states) {
        List<String> names = new ArrayList<>();
        for (States s : states) {
            names.add(s.getName());
        }
        Collections.sort(names);
        return String.join("_", names);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        NFA nfa = new NFA();

        System.out.print("Enter number of states: ");
        int numStates = scanner.nextInt();
        scanner.nextLine();

        Map<String, States> stateMap = new HashMap<>();

        for (int i = 0; i < numStates; i++) {
            System.out.print("Enter state name: ");
            String name = scanner.nextLine();
            States state = new States(name);
            stateMap.put(name, state);
            nfa.addState(state);
        }

        System.out.print("Enter start state: ");
        String startName = scanner.nextLine();
        nfa.setStartState(stateMap.get(startName));

        System.out.print("Enter number of accepting states: ");
        int acceptCount = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < acceptCount; i++) {
            System.out.print("Enter accepting state name: ");
            String acc = scanner.nextLine();
            stateMap.get(acc).setAccepting(true);
        }

        System.out.print("Enter number of transitions: ");
        int transCount = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < transCount; i++) {
            System.out.print("Enter transition (from symbol to): ");
            String[] parts = scanner.nextLine().split(" ");
            String from = parts[0];
            char symbol = parts[1].charAt(0);
            String to = parts[2];
            nfa.addTransition(stateMap.get(from), symbol, stateMap.get(to));
        }

        DFA dfa = convert(nfa);
        System.out.println("DFA result:");
        dfa.printDFA();
    }
}
