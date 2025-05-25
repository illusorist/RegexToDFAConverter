package mainPackage;

import Turing.DFAtoTM;
import Turing.Tape;
import Turing.TuringMachine;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {

        // ----- test for mainPackage.NFA Class --------

//        NFAtoDFA.convertAndPrint(RegexToNFA.convertRegexToNFA("(1*01*01*)*"));

        Tape tape = new Tape("b");
        TuringMachine tm =  DFAtoTM.convert(
                                NFAtoDFA.convert(
                                        RegexToNFA.convertRegexToNFA("a*b")), tape.input);
        tm.run();

    }
}