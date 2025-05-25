package Turing;
import mainPackage.*;

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
        return new TuringMachine(
                tape,
                dfa.getStartState().getName(),
                acceptStates,
                tmTransitions
        );
    }
}
