/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallogic;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import tallogic.Pool.InvalidIDException;

/**
 *
 * @author max
 */
public abstract class TalServer implements IServer, TalPersistence {

    public static Pool objectPool;

    private Scanner list;
    private PrintWriter pw;
    private Persistence<Automata> automataFiles;
    private Persistence<RegularExpression> regexFiles;

    public TalServer() {
        objectPool = new Pool();
        automataFiles = new TextFormatSaver(objectPool);
        regexFiles = null;
    }

    protected void setScanner(Scanner s) {
        this.list = s;
    }

    protected void setPrintWriter(PrintWriter pw) {
        this.pw = pw;
    }

    public void listen() {// throws FileNotFoundException {
        try {
            while (list.hasNextLine()) { //Bucle principal que manda peticiones
                String command = list.nextLine(); //Bloqueante
                if (command.equals("Finish")) {
                    finish();
                    break;
                }
                if (command.equals("Echo")) {
                    send();
                }
                if (command.equals("Create ER")) {
                    createER();
                }
                if (command.equals("Get ER")) {
                    int ID = Integer.parseInt(list.nextLine());
                    System.out.println(objectPool.getReg(ID));
                }
                if (command.equals("Open Automata File")) {
                    openAutomataFile();
                }
                if (command.equals("Create Automata")) {
                    createAutomata();
                }
                if (command.equals("Minimize")) {
                    minimize();
                }
            }
        } catch (Exception e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
        }
        System.out.println("Finished Loop");
        list.close();
        pw.close();
        //persistence
        System.exit(1);
    }

    @Override
    public void openAutomataFile() {
        Automata a = openAutomata(list.nextLine());
        if (a != null) {
            printAutomata(a.getID(), false, false, false, true);
        }

    }
    
    @Override
    public void minimize() {
        int ID = Integer.parseInt(list.nextLine());
        Automata a = objectPool.getAutomata(ID);
        Automata minimized = a.minimize();
        try {
            objectPool.addAutomata(minimized);
        } catch (InvalidIDException e) {
            System.err.println("Invalid ID for minimized automata");
        }
        printAutomata(minimized.getID(), false, false, false, true);
    }

    @Override
    public void printAutomata(int ID, boolean states, boolean alphabet,
            boolean initialState, boolean finalStates) {//throws FileNotFoundException {
        Automata a = objectPool.getAutomata(ID);
        //File f = new File("/home/max/Documents/dot.txt");
        //PrintWriter pw = new PrintWriter(f);
        pw.println("Open Automata");
        pw.println(a.getID());
        pw.println(a.getName());
        System.err.println("Printing automata");
        if (states) {
            pw.println("States");
            List<String> stateArray = a.getStates();
            stateArray.forEach((s) -> pw.println(s));
        }
        if (alphabet) {
            pw.println("Alphabet");
            List<String> alphabetArray = a.getAlphabet();
            alphabetArray.forEach((s) -> pw.println(s));
        }
        if (finalStates) {
            pw.println("Final States");
            List<String> finalStateArray = a.getFinalStates();
            finalStateArray.forEach((s) -> pw.println(s));
        }
        if (initialState) {
            pw.println("InitialState");
            pw.println(a.getInitialState());
        }
        pw.println("Transitions");
        List<Transition> transitions = a.getTransitions();
        transitions.forEach((t) -> pw.println(t.initialState() + ", " + t.finalState() + ", " + t.symbol()));
        //t.initialState() + " -> " + t.finalState() + " [ label = \"" + t.symbol() + "\"];")
        pw.println("End Automata");
        pw.flush();
        //pw.close();
    }

    /**
     * Calls persistence layer to open the automata on the path
     *
     * @param s Path
     * @return true if the operation was successful
     */
    @Override
    public Automata openAutomata(String s) {
        try {
            Automata a = automataFiles.retrieve(s);
            objectPool.addAutomata(a);
            return a;
        } catch (IOException e) {
            System.err.println("Error finding automata in " + s);
        } catch (BadFormatException e) {
            System.err.println(e);
        } catch (InvalidIDException e) {
            System.err.println("Persistence provided a bad ID");
        }
        return null;
    }

    @Override
    public void createAutomata() {
        int ID = objectPool.getNextAutomataID();
        String path = list.nextLine().trim();
        String name = list.nextLine();
        if (name.equals("")) {
            name = ID + "";
        }
        String[] states = list.nextLine().trim().split(", ");
        String[] alphabet = list.nextLine().trim().split(", ");
        String tupline = list.nextLine().trim();
        String[] tuples;
        if (tupline.equals("")) {
            tuples = new String[0];
        } else {
            tuples = tupline.split(", ");
        }
        ArrayList<Transition> transitions = new ArrayList<>(tuples.length);
        String initialState = list.nextLine().trim();
        String[] finalStates;
        String fsline = list.nextLine().trim();
        if (fsline.equals("")) {
            finalStates = new String[0];
        } else {
            finalStates = fsline.split(", ");
        }

        for (String t : tuples) {
            //System.out.println("Transition: " + t);
            t = t.substring(1, t.length() - 1); //Without parenthesis
            String[] tokens = t.split(" ");
            Transition tr = new Transition(tokens[0], tokens[2], tokens[1]);
            transitions.add(tr);
        }
        Automata a = new Automata(ID, Arrays.asList(states), Arrays.asList(alphabet),
                transitions, initialState, Arrays.asList(finalStates));
        a.setName(name);
        try {
            automataFiles.save(a, path);
        } catch (IOException e) {
            System.err.println(e);
        }
        try {
            objectPool.addAutomata(a);
            printAutomata(ID, false, false, false, true);
        } catch (Pool.InvalidIDException e) {
            System.err.println("Invalid ID for new automata");
        }
    }

    @Override
    public void createER() {
        int ID = Integer.parseInt(list.nextLine());
        try {
            objectPool.addRegularExpression(new RegularExpression(ID));
        } catch (Pool.InvalidIDException e) {
            System.err.println("Invalid ID for new RE");
        }
    }

    @Override
    public void send() {
        System.err.println(list.nextLine());
    }

    @Override
    public void finish() {
        pw.println("Finish");
        pw.flush();
    }

}
