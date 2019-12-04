/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallogic;

import librerias.estructurasDeDatos.lineales.*;
import librerias.estructurasDeDatos.modelos.*;

/**
 *
 * @author maximo
 */
public class TestMain {
    public static void main(String[] args) {
        final TalServer server = new StreamTalServer();
        Thread t = new Thread(() -> {
            server.listen();
        });
        t.start();
        Automata big = server.openAutomata("/home/maximo/Documentos/Automatas/Book.a");
        Automata minimum = big.minimize();
        System.out.println(minimum.toString());
    }
    
    public static void main1(String args[]) {
        ListaConPI<Integer> l = new LEGListaConPI<>();
    }
}
