
import java.util.*;
import java.util.stream.Collectors;

public class NfaToDfa {
    private NFA nfa;
    private DFA dfa;
    
    public NfaToDfa(NFA nfa) {
        this.nfa = nfa;
        this.dfa = new DFA();
    }
    
    /**
     * Converts the NFA to DFA using subset construction method
     */
    public DFA convert() {
        // Get the epsilon closure of the NFA's start state
        Set<States> startStateClosure = epsilonClosure(Collections.singleton(nfa.getStartState()));
        
        // Create a new DFA state representing this closure
        States dfaStartState = new States(getStateName(startStateClosure), isAccepting(startStateClosure));
        dfa.setStartState(dfaStartState);
        
        // Queue for processing DFA states
        Queue<States> unprocessedStates = new LinkedList<>();
        unprocessedStates.add(dfaStartState);
        
        // Map to keep track of processed DFA states and their corresponding NFA state sets
        Map<Set<States>, States> processedStates = new HashMap<>();
        processedStates.put(startStateClosure, dfaStartState);
        
        while (!unprocessedStates.isEmpty()) {
            States currentDfaState = unprocessedStates.poll();
            Set<States> currentNfaStates = getNfaStatesForDfaState(currentDfaState);
            
            // Process each symbol in the alphabet (excluding epsilon)
            for (char symbol : nfa.getAlphabet()) {
                if (symbol == 'ε') continue;
                
                // Get move (transition) for the current symbol
                Set<States> moved = move(currentNfaStates, symbol);
                // Get epsilon closure of the moved states
                Set<States> closure = epsilonClosure(moved);
                
                if (!closure.isEmpty()) {
                    States targetDfaState;
                    
                    // Check if we've already processed this state
                    if (processedStates.containsKey(closure)) {
                        targetDfaState = processedStates.get(closure);
                    } else {
                        // Create new DFA state
                        String stateName = getStateName(closure);
                        boolean accepting = isAccepting(closure);
                        targetDfaState = new States(stateName, accepting);
                        
                        // Add to queue and map
                        unprocessedStates.add(targetDfaState);
                        processedStates.put(closure, targetDfaState);
                        dfa.addState(targetDfaState);
                    }
                    
                    // Add transition to DFA
                    dfa.addDFATransition(currentDfaState, symbol, targetDfaState);
                }
            }
        }
        
        return dfa;
    }
    
    /**
     * Computes epsilon closure for a set of states
     */
    private Set<States> epsilonClosure(Set<States> states) {
        Set<States> closure = new HashSet<>(states);
        Queue<States> queue = new LinkedList<>(states);
        
        while (!queue.isEmpty()) {
            States current = queue.poll();
            
            // Get all epsilon transitions from current state
            for (Transition t : current.getTransitions()) {
                if (t.getSymbol() == 'ε' && !closure.contains(t.getTo())) {
                    closure.add(t.getTo());
                    queue.add(t.getTo());
                }
            }
        }
        
        return closure;
    }
    
    /**
     * Computes the move (transition) for a set of states on a given symbol
     */
    private Set<States> move(Set<States> states, char symbol) {
        Set<States> result = new HashSet<>();
        
        for (States s : states) {
            result.addAll(nfa.getNextStates(s, symbol));
        }
        
        return result;
    }
    
    /**
     * Checks if any state in the set is an accepting state
     */
    private boolean isAccepting(Set<States> states) {
        for (States s : states) {
            if (s.Accepting()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Generates a name for a DFA state based on the NFA states it represents
     */
    private String getStateName(Set<States> states) {
        if (states.isEmpty()) return "DEAD";
        
        return states.stream()
                .map(States::getName)
                .sorted()
                .collect(Collectors.joining(",", "{", "}"));
    }
    
    /**
     * Retrieves the NFA states that a DFA state represents
     */
    private Set<States> getNfaStatesForDfaState(States dfaState) {
        String name = dfaState.getName();
        if (name.equals("DEAD")) return Collections.emptySet();
        
        // Remove braces and split by commas
        String[] stateNames = name.substring(1, name.length() - 1).split(",");
        
        return nfa.getAllStates().stream()
                .filter(s -> Arrays.asList(stateNames).contains(s.getName()))
                .collect(Collectors.toSet());
    }
    
    /**
     * Interactive NFA input from the user
     */
    public static void interactiveConversion() {
        Scanner scanner = new Scanner(System.in);
        NFA nfa = new NFA();
        
        System.out.println("=== NFA to DFA Conversion ===");
        System.out.println("Enter NFA details:");
        
        // Get states
        System.out.print("Enter all state names (comma separated): ");
        String[] stateNames = scanner.nextLine().split(",");
        
        // Create states
        Map<String, States> statesMap = new HashMap<>();
        for (String name : stateNames) {
            name = name.trim();
            statesMap.put(name, new States(name, false));
        }
        
        // Set start state
        System.out.print("Enter start state: ");
        String startStateName = scanner.nextLine().trim();
        States startState = statesMap.get(startStateName);
        if (startState == null) {
            System.out.println("Invalid start state!");
            return;
        }
        nfa.setStartState(startState);
        
        // Set accepting states
        System.out.print("Enter accepting states (comma separated): ");
        String[] acceptingNames = scanner.nextLine().split(",");
        for (String name : acceptingNames) {
            name = name.trim();
            States s = statesMap.get(name);
            if (s != null) {
                s.setAccepting(true);
            }
        }
        
        // Add all states to NFA
        statesMap.values().forEach(nfa::addState);
        
        // Get transitions
        System.out.println("Enter transitions (from,symbol,to). Enter 'done' when finished:");
        while (true) {
            System.out.print("Transition: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("done")) break;
            
            String[] parts = input.split(",");
            if (parts.length != 3) {
                System.out.println("Invalid format. Use: from,symbol,to");
                continue;
            }
            
            String fromName = parts[0].trim();
            char symbol = parts[1].trim().charAt(0);
            String toName = parts[2].trim();
            
            States from = statesMap.get(fromName);
            States to = statesMap.get(toName);
            
            if (from == null || to == null) {
                System.out.println("Invalid state name");
                continue;
            }
            
            nfa.addTransition(from, symbol, to);
        }
        
        // Perform conversion
        NfaToDfa converter = new NfaToDfa(nfa);
        DFA dfa = converter.convert();
        
        System.out.println("\n=== Original NFA ===");
        System.out.println(nfa);
        
        System.out.println("\n=== Converted DFA ===");
        System.out.println(dfa);
        
        scanner.close();
    }
    
    public static void main(String[] args) {
        interactiveConversion();
    }
}
