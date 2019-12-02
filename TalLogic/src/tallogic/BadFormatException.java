/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallogic;

import java.io.*;

/**
 *
 * @author maximo
 */
public class BadFormatException extends Exception {
    public BadFormatException(File f) {
        super("Bad format for object " + f.getName());
    }
}
