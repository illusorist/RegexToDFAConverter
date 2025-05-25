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

    }

    public static void convertAndPrintTransitions(String regex) throws Exception {
        TuringMachine tm =  DFAtoTM.convert(
                NFAtoDFA.convert(
                        RegexToNFA.convertRegexToNFA(regex)));

        System.out.println("/// Start State: " + tm.startStateName);
        for (TMTransition t : tm.tmTransitions){
            System.out.print(t.getCurrentState() + " ---"
                    + t.getReadSymbol() + "---> " + t.getNextState());
            if (tm.acceptStates.contains(t.getNextState())){
                System.out.println(" [ACCEPTING]");} else {
                System.out.println(); }
        }
    }
}
