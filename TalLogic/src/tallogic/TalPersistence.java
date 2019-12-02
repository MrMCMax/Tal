/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallogic;

/**
 *
 * @author maximo
 */
public interface TalPersistence {

    /**
     * Calls persistence layer to open the automata on the path, and then
     * updates the interface.
     *
     * @param s Path
     * @return Automata if the operation was successful, null if not
     */
    Automata openAutomata(String s);
    
}
