import java.util.*;

//identify the NFA 5 Tubles

public class NFAtoDFA {

    Map<String, Map<Character, Set<String>>> nfa;
    Set<String> nfaStates;
    Set<Character> alphabet;
    String startState;
    Set<String> acceptStates;

    public NFAtoDFA(Set<String> states, Set<Character> alphabet,
                    Map<String, Map<Character, Set<String>>> transitions,
                    String startState, Set<String> acceptStates) {
        this.nfaStates = states;
        this.alphabet = alphabet;
        this.nfa = transitions;
        this.startState = startState;
        this.acceptStates = acceptStates;
    }

// identify the Conversion Process from NFA To DFA

    public void convert() {
        Queue<Set<String>> queue = new LinkedList<>();
        Map<Set<String>, String> dfaStateNames = new HashMap<>();
        Map<String, Map<Character, String>> dfaTransitions = new HashMap<>();
        Set<String> dfaAcceptStates = new HashSet<>();

        Set<String> startSet = new HashSet<>();
        startSet.add(startState);
        queue.add(startSet);
        dfaStateNames.put(startSet, "A");

        int stateId = 1;

        while (!queue.isEmpty()) {
            Set<String> currentSet = queue.poll();
            String currentName = dfaStateNames.get(currentSet);
            dfaTransitions.putIfAbsent(currentName, new HashMap<>());

            for (char symbol : alphabet) {
                Set<String> nextSet = new HashSet<>();

                for (String state : currentSet) {
                    nextSet.addAll(nfa.getOrDefault(state, new HashMap<>())
                            .getOrDefault(symbol, new HashSet<>()));
                }

                if (!dfaStateNames.containsKey(nextSet)) {
                    String name = "" + (char) ('A' + stateId++);
                    dfaStateNames.put(nextSet, name);
                    queue.add(nextSet);
                }

                dfaTransitions.get(currentName).put(symbol, dfaStateNames.get(nextSet));
            }
        }

        for (Set<String> stateSet : dfaStateNames.keySet()) {
            for (String s : stateSet) {
                if (acceptStates.contains(s)) {
                    dfaAcceptStates.add(dfaStateNames.get(stateSet));
                }
            }
        }

        // Implement the Conversion 
        System.out.println("\nConverted DFA:");
        for (String state : dfaTransitions.keySet()) {
            for (char symbol : dfaTransitions.get(state).keySet()) {
                System.out.println(state + " --" + symbol + "--> " + dfaTransitions.get(state).get(symbol));
            }
        }

        System.out.println("Start State: A");
        System.out.println("Accept States: " + dfaAcceptStates);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose example:");
        System.out.println("1. Even number of 0s (1*01*01*)*");
        System.out.println("2. Ends with 'ab' but no 'ba'");
        System.out.print("Choice: ");
        int choice = scanner.nextInt();

        if (choice == 1) {
            runEvenZerosExample();
        } else if (choice == 2) {
            runEndsWithABNoBAExample();
        } else {
            System.out.println("Invalid choice.");
        }
    }

    // Example.(1)
    public static void runEvenZerosExample() {
        Set<String> states = Set.of("q0", "q1");
        Set<Character> alphabet = Set.of('0', '1');
        Map<String, Map<Character, Set<String>>> transitions = new HashMap<>();

        transitions.put("q0", Map.of(
            '0', Set.of("q1"),
            '1', Set.of("q0")
        ));

        transitions.put("q1", Map.of(
            '0', Set.of("q0"),
            '1', Set.of("q1")
        ));

        String start = "q0";
        Set<String> accept = Set.of("q0");

        NFAtoDFA converter = new NFAtoDFA(states, alphabet, transitions, start, accept);
        converter.convert();
    }

    // Example.(2)
    public static void runEndsWithABNoBAExample() {
        Set<String> states = Set.of("q0", "q1", "q2", "q3", "qDead");
        Set<Character> alphabet = Set.of('a', 'b');
        Map<String, Map<Character, Set<String>>> transitions = new HashMap<>();

        // q0: start
        transitions.put("q0", Map.of(
            'a', Set.of("q1"),
            'b', Set.of("qDead")
        ));

        // q1: saw 'a'
        transitions.put("q1", Map.of(
            'a', Set.of("q1"),
            'b', Set.of("q2")
        ));

        // q2: saw "ab"
        transitions.put("q2", Map.of(
            'a', Set.of("q1"),
            'b', Set.of("qDead")
        ));

        // qDead: invalid, contains "ba"
        transitions.put("qDead", Map.of(
            'a', Set.of("qDead"),
            'b', Set.of("qDead")
        ));

        // q3 is not needed but kept for structure
        transitions.put("q3", new HashMap<>());

        String start = "q0";
        Set<String> accept = Set.of("q2"); // only ends with "ab" and no "ba"

        NFAtoDFA converter = new NFAtoDFA(states, alphabet, transitions, start, accept);
        converter.convert();
    }
}