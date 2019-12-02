/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallogic;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author maximo
 */
public class SocketTalServer extends TalServer {
    
    Socket client;
    
    public SocketTalServer(int port) {
        super();
        ServerSocket ss;
        try {
            ss = new ServerSocket(port);
            System.out.println("Listening...");
            client = ss.accept(); //Bloqueante
            Scanner s = new Scanner(client.getInputStream());
            PrintWriter pw = new PrintWriter(client.getOutputStream());
            pw.println("Server started");
            super.setScanner(s);
            super.setPrintWriter(pw);
        } catch (IOException e) {
            System.err.println("Port unavailable");
        }
    }
}
