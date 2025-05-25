package RegexToDFA;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition that = (Transition) o;

        return symbol == that.symbol && Objects.equals(from, that.from) && Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        // Return a hashcode that is derived from this input.
        return Objects.hash(from, to, symbol);
    }
}
