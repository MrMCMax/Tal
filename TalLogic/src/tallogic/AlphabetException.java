/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallogic;

/**
 *
 * @author Máximo
 */
public class AlphabetException extends RuntimeException {
    public AlphabetException(String symbol) {
        super("Symbol not found: " + symbol);
    }
}
