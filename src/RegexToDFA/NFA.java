package RegexToDFA;

import java.util.*;

public class NFA {
    private Set<States> allStates ;
    private  States startState;
    private  States acceptState;
    private  Set<Character> alphabet;
        public NFA (){
            this.allStates = new HashSet<>(); // hashset ignore duplicate
            this.alphabet = new HashSet<>();
    }
   // adding state
    public void addState(States s) {
        allStates.add(s);
    }
  public void   setStartState(States s){
      this.startState = s;
      addState(s); // Ensure it's included in the allStates set

  }
    public void addTransition(States from, char symbol, States to) {
        Transition t = new Transition(from, to, symbol);
        from.addTransition(t); // Adds to the from state's transition set
        alphabet.add(symbol); // Add symbol to alphabet
        addState(from); // Ensure both states are part of the mainPackage.NFA
        addState(to);

    }
  // Get all states in the mainPackage.NFA
    public Set<States> getAllStates() {
        return allStates;
    }

    // Get the start state
    public States getStartState() {
        return startState;
    }
public Set<States> getAcceptingStates(){
   Set<States> accepting = new HashSet<>(); // htdmn no duplicate
            for (States s : allStates){ // for htdwr 3la kol el states
                if (s.Accepting()){ // lw bt2bl
                    accepting.add(s); // hy3mlha add
                }
            }
          return accepting; // hy return el accept state
            
}
    public Set<Character> getAlphabet() { // optional
        return alphabet;
    }
    public Set<States> getNextStates(States from, char symbol){
    Set<States> result = new HashSet<>(); // htdmn no duplicate
        for (Transition t : from.getTransitions()) {
            if (t.getSymbol() == symbol) {
                result.add(t.getTo());
            }
        }
        return result;
    }
    // Optional: get all transitions (for debugging or export)
    public Set<Transition> getAllTransitions() {
        Set<Transition> all = new HashSet<>();
        for (States s : allStates) {
            all.addAll(s.getTransitions());
        }
        return all;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Start state: ").append(startState).append("\n");
        sb.append("Accepting states: ").append(getAcceptingStates()).append("\n");
        sb.append("All transitions:\n");
        for (Transition t : getAllTransitions()) {
            sb.append("  ").append(t).append("\n");
        }
        return sb.toString();
    }







}
