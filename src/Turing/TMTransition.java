package Turing;
import mainPackage.*;

public class TMTransition {
    private String currentState;
    private char readSymbol;
    private String nextState;
    private char writeSymbol;
    private char direction;

    public TMTransition(String currentState, char readSymbol, String nextState, char writeSymbol, char direction){
        this.currentState = currentState;
        this.readSymbol = readSymbol;
        this.nextState = nextState;
        this.writeSymbol = writeSymbol;
        this.direction = direction;
    }

    public String getCurrentState() { return currentState; }
    public char getReadSymbol() { return readSymbol; }
    public String getNextState() { return nextState; }
    public char getWriteSymbol() { return writeSymbol; }
    public char getDirection() { return direction; }

    @Override
    public String toString() {
        return "TMTransition{" +
                "currentState='" + currentState + '\'' +
                ", readSymbol=" + readSymbol +
                ", nextState='" + nextState + '\'' +
                ", writeSymbol=" + writeSymbol +
                ", direction=" + direction +
                '}';
    }
}
