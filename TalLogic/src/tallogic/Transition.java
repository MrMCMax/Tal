/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallogic;

/**
 *
 * @author max
 */
public class Transition {
    
    private String initialState;
    private String finalState;
    private String symbol;
    
    public Transition(String iS, String fS, String s) {
        this.initialState = iS;
        this.finalState = fS;
        this.symbol = s;
    }
    
    public String initialState() {
        return initialState;
    }
    
    public String finalState() {
        return finalState;
    }
    
    public String symbol() {
        return symbol;
    }
    
    @Override
    public String toString() {
        return "(" + initialState + " " + symbol + " " + finalState + ")";
    }
    
}
