public class Transition {
    private States from;
    private States to;
    private char symbol;  // 'ε' for epsilon transitions

    public Transition(States from, States to, char symbol) {
        this.from = from;
        this.to = to;
        this.symbol = symbol;
    }

    public States getFrom() { return from; }
    public States getTo() { return to; }
    public char getSymbol() { return symbol; }

    @Override
    public String toString() {
        return from + " --" + symbol + "--> " + to;
    }
}
