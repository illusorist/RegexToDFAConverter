package Turing;

import java.sql.SQLOutput;
import java.util.*;

public class Tape {
    public char[] tape;
    int initial = 0;
    int head;// index of the head
    public String input;

    public Tape(String input){
        this.input = input;
        int length = input.length() + (input.length() / 2);
        if (length < 5) length += 5;
        tape = new char[length];
        head = (tape.length / 5);
        Arrays.fill(tape, '_');
        int loader = head;
        for (int i = 0; i <= input.length()-1; i++){
            tape[loader + i] = input.charAt(i);
        }
    }

    public char read(){
        return tape[head];
    }

    public void write(char c){
        tape[head] = c;
    }

    public void moveHead(char c){

        if (c == 'R' && head < tape.length-1){
            head++;
        }
        else if (c == 'L' && head > 0) {
            head--;
        }
        else{
            System.out.println("Head exceeded the boundary of the simulated tape [Or head movement failed]");
            return;
        }
    }

    public int getHead(){
        return head;
    }

    public void printTape(){
        int current = 0;
        System.out.println(getTape());
        for(int i = 0; i <= head - 1; i++){

            System.out.print(" ");
            current++;
        }

        if (getTape()[current] == '_') return;
        System.out.print("^");
        System.out.println();

    }

    public char[] getTape() {
        return tape;
    }
}
