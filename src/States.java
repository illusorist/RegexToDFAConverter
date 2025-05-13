import java.util.*;
public class States {
    int id=0;
    static int counter = 0;

    //NFA Use
    private boolean Accepting;
    public Set<Transition> transitions;

    //DFA use (optional)
    private Set<States> nfaSubset; // used if this is a DFA state
    private Map<Character, States> dfaTransitions;

    public States() {
        this.id = counter++;
        this.transitions = new HashSet<>();
        this.Accepting = false;
        this.dfaTransitions = new HashMap<>();//
    }

    //DFA constructor
    public States(Set<States> nfaSubset) {
        this.id = counter++;
        this.nfaSubset = nfaSubset;
        this.Accepting = nfaSubset.stream().anyMatch(States::Accepting);
        this.dfaTransitions = new HashMap<>();
    }

    public int getId() {
        return id;
    }
    public boolean Accepting() {
        return Accepting;
    }
    public void setAccepting(boolean Accepting) {
        this.Accepting = Accepting;
    }
    public Set<Transition> getTransitions() {
        return transitions;
    }
    public void addTransition(Transition t) {
        transitions.add(t);
    }
    public void addDFATransition(char symbol, States to) {
        dfaTransitions.put(symbol, to);
    }

    public Map<Character, States> getDFATransitions() {
        return dfaTransitions;
    }

    public Set<States> getNfaSubset() {
        return nfaSubset;
    }

    @Override
    public String toString() {
        return "q" + id + (Accepting ? " (accepting)" : "");  // Updated label
    }
}
