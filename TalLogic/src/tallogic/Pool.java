/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author max
 */
public class Pool {
    
    private final IDProvider<RegularExpression> eregs;
    private final IDProvider<Automata> automatas;
    private int lastID;
    
    public Pool() {
        eregs = new IDProvider<>();
        automatas = new IDProvider<>();
        lastID = 0;
    }
    
    public int getNextAutomataID() {
        return automatas.getNextID();
    }
    
    public int getNextERegID() {
        return eregs.getNextID();
    }
    
    public void addRegularExpression(RegularExpression er) throws InvalidIDException {
        eregs.newID(er);
    }
    
    public void addAutomata(Automata a) throws InvalidIDException {
        automatas.newID(a);
    }
    
    public RegularExpression getReg(int ID) {
        return eregs.get(ID);
    }
    
    public Automata getAutomata(int ID) {
        return automatas.get(ID);
    }
    
    public class InvalidIDException extends Exception {
        public InvalidIDException() {
            super("ID already taken, get new using Pool.getNewID");
        }
    }
}
