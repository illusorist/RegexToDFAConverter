package Testing;

import mainPackage.*;

import java.util.*;

public class RegexToNFAMain {
    public static void main(String[] args) throws Exception {

        printNFA(RegexToNFA.convertRegexToNFA("a?"));

    }

    public static void printNFA(NFA nfa) {

        // This adds the states to a list and sorts it by state id
        Set<States> allStates = nfa.getAllStates();
        List<States> StatesList = new ArrayList<>(allStates);
        StatesList.sort(Comparator.comparingInt(States::getId));

        System.out.println("Start State: " + nfa.getStartState().getName());
        for (States state : StatesList) {
            String type = "";
            if (state == nfa.getStartState()) type += " [START]";
            if (state.Accepting()) type += " [ACCEPT]";

            System.out.println("State " + state.getName() + type);

            for (Transition t : state.getTransitions()) {
                System.out.println("  -- " + t.getSymbol() + " --> " + t.getTo().getName());
            }
        }
    }
}
