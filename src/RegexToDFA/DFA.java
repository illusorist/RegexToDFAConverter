package RegexToDFA;

import java.util.*;

public class DFA {
    private States startState;
    private Set<States> allStates;

    public DFA() {
        this.allStates = new HashSet<>();
    }

    public void setStartState(States s) {
        this.startState = s;
        addState(s); // ensure it's included
    }

    public States getStartState() {
        return startState;
    }

    public void addState(States s) {
        allStates.add(s);
    }

    public Set<States> getAllStates() {
        return allStates;
    }

    public void addDFATransition(States from, char symbol, States to) {
        from.addDFATransition(symbol, to);
        addState(from);
        addState(to);
    }

    public Set<States> getAcceptingStates() {
        Set<States> accepting = new HashSet<>();
        for (States s : allStates) {
            if (s.Accepting()) {
                accepting.add(s);
            }
        }
        return accepting;
    }

    public void printDFA() {
        System.out.println("Start state: " + startState);
        System.out.println("Accepting states: " + getAcceptingStates());
        System.out.println("All transitions:");
        for (States state : allStates) {
            for (Map.Entry<Character, States> entry : state.getDFATransitions().entrySet()) {
                if (entry.getKey() == ' '){
                    System.out.println("  " + state + " --" + entry.getKey() + "--> " + entry.getValue() +" ⁉️⁉️⁉️⁉️⁉️⁉️⁉️⁉️⁉️⁉️خخخخخخخخخخخخخخخ");
                    continue;
                }
                System.out.println("  " + state + " --" + entry.getKey() + "--> " + entry.getValue());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Start state: ").append(startState).append("\n");
        sb.append("Accepting states: ").append(getAcceptingStates()).append("\n");
        sb.append("All transitions:\n");
        for (States state : allStates) {
            for (Map.Entry<Character, States> entry : state.getDFATransitions().entrySet()) {
                sb.append("  ").append(state)
                        .append(" --").append(entry.getKey())
                        .append("--> ").append(entry.getValue()).append("\n");
            }
        }
        return sb.toString();
    }
}
