/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallogic;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author max
 */
public class TalLogic {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final TalServer server = new StreamTalServer();
        server.listen();
    }

}
//ab*(ab + c)a
