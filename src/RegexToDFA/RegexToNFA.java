package RegexToDFA;

import java.util.*;


public class RegexToNFA {
    static String operators = "*|().+?";

    public static String addConcat (String regex){
        regex = regex.replace(" ", "");
        if (regex.contains("[") || regex.contains("]")){
            regex = expandCharacterClasses(regex);
        }
        StringBuilder newregex = new StringBuilder();
        for (int i = 0; i < regex.length(); i++){
            if (i ==regex.length() - 1){
                newregex.append(regex.charAt(i));
                break;
            }
            char c = regex.charAt(i);
            char next = regex.charAt(i+1);

            if (
                    (!operators.contains(String.valueOf(c)) || c == ')' || c == '*' || c == '+' || c == '?')
                    && (!operators.contains(String.valueOf(next)) || next == '(')
            ){
                newregex.append(c).append(".");
            } else {
                newregex.append(c);
            }
        }
        return newregex.toString();
    }

    public static String toPostfix(String newRegex) {
        Stack<Character> op = new Stack<>();
        StringBuilder postfixedRegex = new StringBuilder();

        Map<Character, Integer> precedence = new HashMap<>();
        precedence.put('|', 1);
        precedence.put('.', 2);
        precedence.put('*', 3);
        precedence.put('+', 3);
        precedence.put('?', 3);

        for (char c : newRegex.toCharArray()) {
            if (!operators.contains(String.valueOf(c))) {
                postfixedRegex.append(c);  // Operand (normal character)
            } else if (c == '(') {
                op.push(c);
            } else if (c == ')') {
                // Pop until '('
                while (!op.isEmpty() && op.peek() != '(') {
                    postfixedRegex.append(op.pop());
                }
                if (op.isEmpty()) {
                    System.out.println("Invalid Expression! Unmatched parenthesis.");
                    return null;
                }
                op.pop();  // Discard '('
            } else {
                // Pop while precedence is higher or equal
                while (!op.isEmpty() && op.peek() != '(' &&
                        precedence.getOrDefault(op.peek(), 0) >= precedence.getOrDefault(c, 0)) {
                    postfixedRegex.append(op.pop());
                }
                op.push(c);
            }
        }

        while (!op.isEmpty()) {
            char top = op.pop();
            if (top == '(' || top == ')') {
                System.out.println("Invalid Expression! Unmatched parenthesis.");
                return null;
            }
            postfixedRegex.append(top);
        }

        return postfixedRegex.toString();
    }


    public static class NFAFragment {
        public States start;
        public States accept;
        public NFA nfa;

        public NFAFragment(States start, States accept, NFA nfa){
            this.start = start;
            this.accept = accept;
            this.nfa = nfa;
        }
    }

    public static NFAFragment buildFragment(char c){

        States start = new States();
        States accept = new States();
        accept.setAccepting(true);

        NFA nfa = new NFA();
        nfa.setStartState(start);
        nfa.addTransition(start, c, accept);

        return new NFAFragment(start, accept, nfa);

    }

    public static NFA buildNFAfromFragments(String postfixedRegex) throws Exception {
        Stack<NFAFragment> fragments = new Stack<>();

        for (char c : postfixedRegex.toCharArray()){
            if (!operators.contains(String.valueOf(c))){
                fragments.add(buildFragment(c));
                continue;
            } else if (c == '.' && !fragments.isEmpty()) {

                // the first popped fragment from the stack is the fragment that should be
                // on the right
                NFAFragment frag2 = fragments.pop();
                NFAFragment frag1 = fragments.pop();

                NFA newNFA = new NFA();
                copyFragmentIntoNFA(frag1, newNFA);
                copyFragmentIntoNFA(frag2, newNFA);

                newNFA.setStartState(frag1.start);
                newNFA.addTransition(frag1.accept,'ε', frag2.start);
                NFAFragment newFragment = new NFAFragment(frag1.start, frag2.accept, newNFA);
                frag1.accept.setAccepting(false);
                fragments.add(newFragment);
            }
            else if (c == '*' && !fragments.isEmpty()){

                NFAFragment frag = fragments.pop();
                States newStart = new States();
                States newAccept = new States();
                newAccept.setAccepting(true);
                NFA newNFA = new NFA();

                copyFragmentIntoNFA(frag, newNFA);

                newNFA.addTransition(newStart, 'ε', newAccept);
                newNFA.addTransition(newStart, 'ε', frag.start);
                newNFA.addTransition(frag.accept, 'ε', newAccept);
                newNFA.addTransition(frag.accept, 'ε', frag.start);

                frag.accept.setAccepting(false);

                newNFA.setStartState(newStart);
                NFAFragment newfragment = new NFAFragment(newStart, newAccept, newNFA);
                fragments.add(newfragment);
            }
            else if (c == '|' && !fragments.isEmpty()){
                NFAFragment frag2 = fragments.pop();
                NFAFragment frag1 = fragments.pop();

                States newStart = new States();
                States newAccept = new States();
                newAccept.setAccepting(true);

                NFA newNFA = new NFA();
                copyFragmentIntoNFA(frag1, newNFA);
                copyFragmentIntoNFA(frag2, newNFA);

                newNFA.addTransition(newStart, 'ε', frag1.start);
                newNFA.addTransition(newStart, 'ε', frag2.start);
                newNFA.addTransition(frag1.accept, 'ε', newAccept);
                newNFA.addTransition(frag2.accept, 'ε', newAccept);

                frag1.accept.setAccepting(false);
                frag2.accept.setAccepting(false);

                newNFA.setStartState(newStart);
                NFAFragment newfragment = new NFAFragment(newStart, newAccept, newNFA);
                fragments.add(newfragment);

            } else if (c == '+' && !fragments.isEmpty()){

                NFAFragment frag1 = fragments.pop();
                States newStart = new States();
                States newAccept = new States();
                NFA newNFA = new NFA();

                copyFragmentIntoNFA(frag1, newNFA);

                newNFA.addTransition(newStart, 'ε', frag1.start);
                newNFA.addTransition(frag1.accept, 'ε', frag1.start);
                newNFA.addTransition(frag1.accept, 'ε', newAccept);

                frag1.accept.setAccepting(false);


                newNFA.setStartState(newStart);
                newAccept.setAccepting(true);
                NFAFragment newFragment = new NFAFragment(newStart, newAccept, newNFA);
                fragments.add(newFragment);
            }
            else if (c == '?' && !fragments.isEmpty()){

                NFAFragment frag1 = fragments.pop();
                NFA newNFA = new NFA();

                newNFA.setStartState(frag1.start);
                copyFragmentIntoNFA(frag1, newNFA);

                newNFA.addTransition(frag1.start, 'ε', frag1.accept);

                NFAFragment newFragment = new NFAFragment(frag1.start, frag1.accept, newNFA);
                fragments.add(newFragment);
            }

        }

        if (fragments.size() != 1){
            throw new Exception("Invalid Regex");
        }

        return fragments.pop().nfa;
    }


    public static void copyFragmentIntoNFA(NFAFragment frag, NFA target) {
        // Get a copy of the states to iterate over (to avoid exceptions)
        Set<States> statesToCopy = new HashSet<>(frag.nfa.getAllStates());

        // Add all states to the target mainPackage.NFA
        for (States state : statesToCopy) {
            target.addState(state);
        }

        // Then, iterate over the states and add their transitions to the target mainPackage.NFA
        for (States state : statesToCopy) {
            // Get a copy of the set of transition for the current state to iterate over
            Set<Transition> transitionsToCopy = new HashSet<>(state.getTransitions());
            for (Transition t : transitionsToCopy) {
                target.addTransition(state, t.getSymbol(), t.getTo());
            }
        }
    }


    public static NFA convertRegexToNFA (String Regex) throws Exception {
        System.out.println("--- Converting Regex to DFA ---");
        System.out.println("Regex: " + Regex);
        return buildNFAfromFragments(toPostfix(addConcat(Regex)));
    }

    private static String expandCharacterClasses(String regex) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < regex.length(); i++) {
            char current = regex.charAt(i);
            if (current == '[') {
                int end = regex.indexOf(']', i);
                if (end == -1) {
                    throw new IllegalArgumentException("Unmatched [ in regex");
                }

                String classContent = regex.substring(i + 1, end);
                boolean negated = classContent.startsWith("^");
                int startIdx = negated ? 1 : 0;

                Set<Character> chars = new LinkedHashSet<>();
                for (int j = startIdx; j < classContent.length(); j++) {
                    char c = classContent.charAt(j);
                    if (j + 2 < classContent.length() && classContent.charAt(j + 1) == '-') {
                        char endChar = classContent.charAt(j + 2);
                        for (char ch = c; ch <= endChar; ch++) {
                            chars.add(ch);
                        }
                        j += 2;
                    } else {
                        chars.add(c);
                    }
                }

                if (negated) {
                    Set<Character> all = new LinkedHashSet<>();
                    for (char ch = 32; ch < 127; ch++) all.add(ch);
                    all.removeAll(chars);
                    chars = all;
                }

                result.append('(');
                boolean first = true;
                for (char ch : chars) {
                    if (!first) result.append('|');
                    result.append(ch);
                    first = false;
                }
                result.append(')');
                i = end; // skip to after ']'
            } else {
                result.append(current);
            }
        }
        return result.toString();
    }



}
