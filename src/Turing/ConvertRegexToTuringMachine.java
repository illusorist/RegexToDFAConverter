package Turing;

import RegexToDFA.NFAtoDFA;
import RegexToDFA.RegexToNFA;

public class ConvertRegexToTuringMachine {
    public static void convertAndSimulate(String regex, String inputString) throws Exception {
        Tape tape = new Tape(inputString);
        TuringMachine tm =  DFAtoTM.convert(
                NFAtoDFA.convert(
                        RegexToNFA.convertRegexToNFA(regex)), tape.input);
        tm.run();

        for (TMTransition t : tm.tmTransitions){
            System.out.println(t.getCurrentState() + "---"
                    + t.getReadSymbol() + "--->" + t.getNextState());
        }
    }

    public static void convertAndPrintTransitions(String regex) throws Exception {
        TuringMachine tm =  DFAtoTM.convert(
                NFAtoDFA.convert(
                        RegexToNFA.convertRegexToNFA(regex)));

        for (TMTransition t : tm.tmTransitions){
            System.out.println(t.getCurrentState() + "---"
                    + t.getReadSymbol() + "--->" + t.getNextState());
        }
    }
}
