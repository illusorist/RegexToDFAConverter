package Testing;

import RegexToDFA.*;

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

    public static String expandCharacterClass(String content) {
        boolean negated = content.startsWith("^");
        int start = negated ? 1 : 0;

        Set<Character> chars = new LinkedHashSet<>();
        for (int i = start; i < content.length(); i++) {
            char c = content.charAt(i);
            if (i + 2 < content.length() && content.charAt(i + 1) == '-') {
                char end = content.charAt(i + 2);
                for (char ch = c; ch <= end; ch++) {
                    chars.add(ch);
                }
                i += 2; // skip over the range
            } else {
                chars.add(c);
            }
        }

        if (negated) {
            Set<Character> fullSet = new LinkedHashSet<>();
            for (char ch = 32; ch < 127; ch++) { // printable ASCII
                fullSet.add(ch);
            }
            fullSet.removeAll(chars);
            chars = fullSet;
        }

        StringBuilder result = new StringBuilder("(");
        boolean first = true;
        for (char ch : chars) {
            if (!first) result.append("|");
            result.append(ch);
            first = false;
        }
        result.append(")");
        return result.toString();
    }

}
