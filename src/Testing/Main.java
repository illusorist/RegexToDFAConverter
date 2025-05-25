package Testing;
import Turing.*;

import Turing.ConvertRegexToTuringMachine;

public class Main {
    public static void main(String[] args) throws Exception {

                    // This simulates a Turing Machine tape
//        ConvertRegexToTuringMachine.convertAndSimulate("b[0-9]+", "b");

        ConvertRegexToTuringMachine.convertAndPrintTransitions("ab(c|d)*");
    }
}