/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talinterface;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author maximo
 */
public class SocketConnecter extends Connecter {
    public int port;
    
    public SocketConnecter(int port) throws IOException, UnknownHostException {
        super();
        System.out.println("Connecting...");
        Socket client = new Socket(InetAddress.getByName("127.0.0.1"), port);
        Scanner listener = new Scanner(client.getInputStream());
        PrintWriter pw = new PrintWriter(client.getOutputStream());
        super.setScanner(listener);
        super.setPrintWriter(pw);
    }
}
