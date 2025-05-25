package Turing;

import RegexToDFA.NFAtoDFA;
import RegexToDFA.RegexToNFA;

public class ConvertRegexToTuringMachine {
    public void convert(String regex, String inputString) throws Exception {
        Tape tape = new Tape(inputString);
        TuringMachine tm =  DFAtoTM.convert(
                NFAtoDFA.convert(
                        RegexToNFA.convertRegexToNFA(regex)), tape.input);
        tm.run();
    }
}
