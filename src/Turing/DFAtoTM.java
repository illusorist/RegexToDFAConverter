package Turing;
import RegexToDFA.*;

import java.util.*;

public class DFAtoTM {
    public static TuringMachine convert(DFA dfa, String input){
        Tape tape = new Tape(input);
        List<TMTransition> tmTransitions = new ArrayList<>();
        Set<String> acceptStates = new HashSet<>(); // Hashset = no duplicates
        for (States s : dfa.getAllStates()){
            String currentState = s.getName();

            if (s.Accepting()){
                acceptStates.add(currentState);
            }

            for (Map.Entry<Character, States> entry : s.getDFATransitions().entrySet()){
                char symbol = entry.getKey();
                States toState = entry.getValue();

                // add turing machine transition
                tmTransitions.add(new TMTransition(
                        currentState, symbol,
                        toState.getName(),symbol, 'R'
                ));
            }

        }
        System.out.println("Accepting states: " + acceptStates);

        return new TuringMachine(
                tape,
                dfa.getStartState().getName(),
                acceptStates,
                tmTransitions
        );
    }

    public static TuringMachine convert(DFA dfa){
        List<TMTransition> tmTransitions = new ArrayList<>();
        Set<String> acceptStates = new HashSet<>(); // Hashset = no duplicates
        for (States s : dfa.getAllStates()){
            String currentState = s.getName();

            if (s.Accepting()){
                acceptStates.add(currentState);
            }

            for (Map.Entry<Character, States> entry : s.getDFATransitions().entrySet()){
                char symbol = entry.getKey();
                States toState = entry.getValue();

                // add turing machine transition
                tmTransitions.add(new TMTransition(
                        currentState, symbol,
                        toState.getName(),symbol, 'R'
                ));
            }

        }
        System.out.println("Accepting states: " + acceptStates);

        return new TuringMachine(
                dfa.getStartState().getName(),
                acceptStates,
                tmTransitions
        );
    }
}
