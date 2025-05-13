public class Main {
    public static void main(String[] args) {

     // ----- test for NFA Class --------
        States q0 = new States();
        States q1 = new States();
        States q2 = new States();
        q1.setAccepting(true);
        q2.setAccepting(true);

        NFA nfa = new NFA();
        nfa.setStartState(q0);
        nfa.addTransition(q0, 'a', q0);
        nfa.addTransition(q0, 'b', q1);
        nfa.addTransition(q1, 'b', q2);
        System.out.println("----- NFA -----");
        System.out.println(nfa);

     // ----- test for DFA Class --------
        DFA dfa = new DFA();
        dfa.setStartState(q0);
        dfa.addDFATransition(q0, 'a', q0);
        dfa.addDFATransition(q0, 'b', q1);
        dfa.addDFATransition(q1, 'b', q2);
        System.out.println("\n----- DFA -----");
        dfa.printDFA();
    }
}
