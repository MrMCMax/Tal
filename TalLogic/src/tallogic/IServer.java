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
public interface IServer {

    /*
     * Input methods
     */
    
    void createAutomata();

    void createER();

    void finish();

    void openAutomataFile();

    void send();
    
    void minimize();
    
    /*
     * Output methods
     */

    void printAutomata(int ID, boolean states, boolean alphabet, boolean initialState, boolean finalStates);
    
}
