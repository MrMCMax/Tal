/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talinterface;

/**
 *
 * @author maximo
 */
public interface IConnecter {

    /*
     * Output Methods
     */
    void createAutomata(String path, String name, String states, String alphabet, String transitions, String initialState, String finalState);

    void createER(String er);

    void finish();

    void openAutomataFile(String path);

    void send(String s);
    
    void minimize(int ID);
    /*
     * Input methods
     */
    
    void openAutomata();
    
    void openEReg();
}
