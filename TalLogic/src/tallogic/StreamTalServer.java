/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallogic;

import java.io.*;
import java.util.*;

/**
 *
 * @author maximo
 */
public class StreamTalServer extends TalServer{
    
    public StreamTalServer() {
        super();
        Scanner read = new Scanner(System.in);
        super.setScanner(read);
        PrintWriter pw = new PrintWriter(System.out); //REMEMBER TO FLUSH
        pw.println("Server started");
        pw.flush();
        super.setPrintWriter(pw);
    }
}
