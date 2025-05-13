import java.util.*;
public class States {
    int id=0;

    static int counter = 0;
    private boolean Accepting;
    public Set<Transition> transitions;
    public States() {
        this.id = counter++;
        this.transitions = new HashSet<>();
        this.Accepting = false;
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
    @Override
    public String toString() {
        return "q" + id + (Accepting ? " (accepting)" : "");  // Updated label
    }
}
