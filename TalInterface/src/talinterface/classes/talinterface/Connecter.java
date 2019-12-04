/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talinterface;

import graphviz.GraphViz;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.*;
import javafx.scene.image.*;

/**
 *
 * @author maximo
 */
public abstract class Connecter extends Thread implements IConnecter {

    private PrintWriter pw;
    private Scanner listener;
    private boolean closed;
    private BoardController bc;

    /**
     * If the server is launched on this machine, the connection happens with
     * process input/output. If not, then the connection is made with a socket,
     * temporarily on local port 9000
     *
     */
    protected Connecter() {
    }

    public void setBoardController(BoardController contr) {
        this.bc = contr;
    }

    protected void setScanner(Scanner s) {
        this.listener = s;
        this.closed = false;
    }

    protected void setPrintWriter(PrintWriter pw) {
        this.pw = pw;
    }
    
    
    /**
     * This method is the responsible for reading and interpreting all
     * information coming from the server.
     *
     * @param line
     */
    private void processOutput(String line) {
        switch (line) {
            case "Server started":
                System.out.println("Server responded, and active");
                break;
            case "Finish":
                listener.close();
                closed = true;
                break;
            case "Open Automata":
                openAutomata();
                break;
            case "Finished Loop":
                listener.close();
                closed = true;
                finish();
                break;
            default:
                System.out.println("Not recognized:");
                System.out.println(line);
                break;
        }
    }

    @Override
    public void run() {
        String line;
        while (!closed && listener.hasNextLine()) {
            line = listener.nextLine();
            processOutput(line);
        }
        System.out.println("Scanner closed for TalInterface");
    }

    @Override
    public void createER(String er) {
        pw.println("Create ER");
        pw.println(er);
        pw.flush();
    }

    /* Output methods */
    
    @Override
    public void createAutomata(String path, String name, String states, String alphabet,
            String transitions, String initialState, String finalState) {
        //File file = new File("/home/max/Documents/automata.txt");
        //PrintWriter pw = new PrintWriter(file);
        pw.println("Create Automata");
        pw.println(path);
        pw.println(name);
        pw.println(states);
        pw.println(alphabet);
        pw.println(transitions);
        pw.println(initialState);
        pw.println(finalState);
        pw.flush();
        //pw.close();
    }

    @Override
    public void finish() {
        pw.println("Finish");
        pw.flush();
        pw.close();
        System.out.println("Connection closed with the server");
    }

    @Override
    public void send(String s) {
        pw.println(s);
        pw.flush();
    }


    @Override
    public void openAutomataFile(String path) {
        pw.println("Open Automata File");
        pw.println(path);
        pw.flush();
    }
    
    @Override
    public void minimize(int ID) {
        pw.println("Minimize");
        pw.println(ID);
        pw.flush();
    }

    
    /* Input Methods */
    
    @Override
    public void openAutomata() {
        int id = Integer.parseInt(listener.nextLine());
        String name = listener.nextLine();
        GraphViz gv = new GraphViz();
        gv.addln(gv.start_graph());
        gv.addln("rankdir=LR;");
        gv.addln("size=\"8,5;\"");
        String line;
        if (listener.nextLine().equals("Final States")) {
            if (!(line = listener.nextLine()).equals("Transitions")) {
                StringBuilder finalStates = new StringBuilder();
                finalStates.append("node [shape = doublecircle]; ");
                finalStates.append(line).append(" ");
                while (!(line = listener.nextLine()).equals("Transitions")) {
                    finalStates.append(line).append(" ");
                }
                finalStates.append(";");
                gv.addln(finalStates.toString());
            }
        }
        //Transitions
        gv.addln("node [shape = circle];");
        String[] tokens;
        while (!(line = listener.nextLine()).equals("End Automata")) {
            tokens = line.split(", ");
            gv.addln(tokens[0] + " -> " + tokens[1] + " [ label = \"" + tokens[2] + "\" ];");
        }
        gv.addln(gv.end_graph());
        System.out.println(gv.getDotSource());
        Image img = new Image(new ByteArrayInputStream(
            gv.getGraph(gv.getDotSource(), "gif", "dot")));
        ImageItem item = new ImageItem(id, "Automata " + name, img, ImageItem.AUTOMATA);
        javafx.application.Platform.runLater(() -> {
            bc.addImageItem(item);
        });
    }
    
    @Override
    public void openEReg() {
        
    }
}
