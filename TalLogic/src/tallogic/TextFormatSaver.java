/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallogic;

import java.io.*;
import java.util.*;
import static tallogic.TalServer.objectPool;

/**
 *
 * @author maximo
 */
public class TextFormatSaver implements Persistence<Automata> {

    private Pool objectPool;

    public TextFormatSaver(Pool p) {
        objectPool = p;
    }

    @Override
    public Automata retrieve(String path) throws IOException, BadFormatException {
        Automata a = null;
        int id = objectPool.getNextAutomataID();
        File f = new File(path);
        Scanner s = new Scanner(f);
        try {
            String name = s.nextLine();
            List<String> states, alphabet, finalStates;
            List<Transition> transitions;
            String sl = s.nextLine().trim();
            states = Arrays.asList(sl.substring(sl.indexOf("[") + 1, sl.lastIndexOf("]")).split(", "));
            String al = s.nextLine().trim();
            alphabet = Arrays.asList(al.substring(al.indexOf("[") + 1, al.lastIndexOf("]")).split(", "));
            String tupline = s.nextLine().trim();
            String[] tl = tupline.substring(1, tupline.length() - 1).split(", "); //Without brackets
            transitions = new ArrayList(tl.length);
            String[] tokens;
            for (String t : tl) {
                tokens = t.trim().substring(1, t.length() - 1).split(" "); //Without parenthesis
                transitions.add(new Transition(tokens[0], tokens[2], tokens[1]));
            }
            String is = s.nextLine().trim();
            finalStates = null;
            String fs = s.nextLine().trim();
            if (fs.equals("[]")) {
                finalStates = new ArrayList(0);
            } else {
                finalStates = Arrays.asList(fs.substring(fs.indexOf("[") + 1, fs.lastIndexOf("]")).split(", "));
            }
            a = new Automata(id, states, alphabet, transitions, is, finalStates);
            a.setName(name);
            return a;
        } catch (NoSuchElementException e) {
            throw new BadFormatException(f);
        }
    }

    @Override
    public void save(Automata object, String path) throws IOException {
        try (PrintWriter pw = new PrintWriter(new File(path))) {
            pw.println(object.toString());
            pw.flush();
        }
    }
}
