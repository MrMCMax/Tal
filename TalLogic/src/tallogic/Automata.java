/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallogic;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import librerias.estructurasDeDatos.grafos.Adyacente;
import librerias.estructurasDeDatos.grafos.GrafoDirigido;
import librerias.estructurasDeDatos.jerarquicos.ForestUFSet;
import librerias.estructurasDeDatos.modelos.ListaConPI;
import librerias.estructurasDeDatos.modelos.Partition;

/**
 * An automata is a directed, labeled and not weighted graph.
 *
 * @author max
 */
public class Automata extends GrafoDirigido {
    
    private int ID;
    private String name;
    private ArrayList<String> verticesToStates;                          //Maps a graph vertex to a state name
    private Map<String, Integer> statesToVertices;              //Maps a state name to its vertex
    private Set<String> alphabet;                                  //Contains all the valid labels
    private String initialState;                                //Must be a valid state
    private Map<String, Integer> finalStatesToVertices;         //Must be a subset of states
    private Map<Integer, String> verticesToFinalStates;
    
    //Backup for serialization
    private List<String> stateArray;
    private List<String> alphabetArray;
    private List<Transition> transitionArray;
    private List<String> finalStateArray;
    //The inherited graph structure will hold the transitions between states

    public static final String LAMBDA = "";
    
    /**
     * General constructor
     *
     * @param ID ID
     * @param Q Set of states
     * @param alphabet Sigma
     * @param transitions delta
     * @param initialState q0
     * @param finalStates F
     */
    public Automata(int ID, List<String> Q, List<String> alphabet,
            List<Transition> transitions, String initialState, List<String> finalStates) {
        super(Q.size());
        this.ID = ID;
        this.stateArray = Q;
        this.alphabetArray = alphabet;
        this.transitionArray = transitions;
        this.finalStateArray = finalStates;
        
        this.verticesToStates = new ArrayList<>(Q.size());
        this.statesToVertices = new HashMap<String, Integer>(Q.size());
        this.alphabet = new HashSet<>();
        this.alphabet.addAll(alphabet);
        this.initialState = initialState;
        this.finalStatesToVertices = new HashMap<String, Integer>(Q.size());
        this.verticesToFinalStates = new HashMap<Integer, String>(Q.size());
        
        //Fill states map
        for (int i = 0; i < Q.size(); i++) {
            verticesToStates.add(Q.get(i));
            statesToVertices.put(Q.get(i), i);
        }
        //Fill graph
        for (Transition t : transitions) {
            insertarArista(statesToVertices.get(t.initialState()),
                    statesToVertices.get(t.finalState()),
                    t.symbol());
        }
        //Fill final states maps
        for (String s : finalStates) {
            int v = statesToVertices.get(s);
            finalStatesToVertices.put(s, v);
            verticesToFinalStates.put(v, s);
        }
    }

    protected Automata(int ID, ArrayList<String> v2S, Map<String, Integer> s2V, Set<String> alphabet,
            String iS, Map<String, Integer> fS2V, Map<Integer, String> v2FS, List<Transition> trs) {
        super(v2S.size());
        this.ID = ID;
        this.verticesToStates = v2S;
        this.statesToVertices = s2V;
        this.alphabet = alphabet;
        this.initialState = iS;
        this.finalStatesToVertices = fS2V;
        this.verticesToFinalStates = v2FS;
        
        this.stateArray = v2S;
        this.alphabetArray = new ArrayList<>(alphabet);
        this.transitionArray = trs;
        this.finalStateArray = new ArrayList<>(fS2V.keySet());
        
        for (Transition t : trs) {
            insertarArista(statesToVertices.get(t.initialState()),
                    statesToVertices.get(t.finalState()),
                    t.symbol());
        }
    }

    protected Automata(int ID, ArrayList<String> v2S, Map<String, Integer> s2V, Set<String> alphabet,
            String iS, Map<String, Integer> fS2V, Map<Integer, String> v2FS) {
        this(ID, v2S, s2V, alphabet, iS, fS2V, v2FS, new ArrayList<Transition>(0));
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    
    public String getName() {
        return name == null ? ID + "" : name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns the unique ID of the Automata
     *
     * @return this automata's ID
     */
    public int getID() {
        return ID;
    }
    
    public List<String> getStates() {
        return stateArray;
    }
    
    public List<String> getAlphabet() {
        return alphabetArray;
    }
    
    public List<String> getFinalStates() {
        return finalStateArray;
    }
    
    public String getInitialState() {
        return initialState;
    }
    
    public List<Transition> getTransitions() {
        return transitionArray;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Automata && ((Automata) o).ID == ID;
    }
    
    /**
     * PRECONDITION: Deterministic and complete
     *
     * @return
     */
    public Automata minimize() {
        Partition initial = new ForestUFSet(numV);
        int firstFinal = -1;
        int firstNoFinal = -1;
        for (int i = 0; i < numV; i++) {
            if (verticesToFinalStates.containsKey(i)) {
                //Añadir a estados finales
                if (firstFinal == -1) {
                    firstFinal = i;
                } else {
                    initial.union(initial.find(firstFinal), i);
                }
            } else {
                //Añadir a estados no finales
                if (firstNoFinal == -1) {
                    firstNoFinal = i;
                } else {
                    initial.union(initial.find(firstNoFinal), i);
                }
            }
        }
        //We got the starting partition
        //Get the rest!
        Partition next = initial;
        do {
            initial = next;
            next = new ForestUFSet(numV);
            //Optimization: if the state is marked, its class has already been computed
            boolean[] computed = new boolean[numV]; //Initialized to false
            for (int p = 0; p < super.numV; p++) {
                if (computed[p]) {
                    continue;
                }
                @SuppressWarnings("LocalVariableHidesMemberVariable") 
                int q;
                for (q = p + 1; q < super.numV; q++) {
                    if (computed[q]) {
                        continue;
                    }
                    if (initial.find(p) == initial.find(q)) {
                        int i = 0;
                        boolean same = true;
                        /*
                        while (i < alphabet.length && same) {
                            int destino1 = destinoArista(p, alphabet[i]);
                            int destino2 = destinoArista(q, alphabet[i]);
                            if (initial.find(destino1) != initial.find(destino2)) {
                                same = false;
                            }
                            i++;
                        }
                         */
                        Iterator<String> symbols = alphabet.iterator();
                        int destino1, destino2;
                        String symb;
                        while (symbols.hasNext() && same) {
                            symb = symbols.next();
                            destino1 = destinoArista(p, symb);
                            destino2 = destinoArista(q, symb);
                            if (initial.find(destino1) != initial.find(destino2)) {
                                same = false;
                            }
                        }
                        if (same) {
                            next.union(next.find(p), next.find(q));
                            computed[q] = true;
                        }
                    }
                }
                computed[p] = true;
            }
        } while (!next.equals(initial));
        //We got the final partition in "next"
        //Now we create the Automata object
        ArrayList<Integer> classes = next.classes();
        Map<String, Integer> newStatesToVertices = new HashMap<>(classes.size());
        ArrayList<String> newVerticesToStates = new ArrayList<>(classes.size());
        Map<String, Integer> newFinalStatesToVertices = new HashMap<>(classes.size());
        Map<Integer, String> newVerticesToFinalStates = new HashMap<>(classes.size());
        int id = TalServer.objectPool.getNextAutomataID();
        ArrayList<Transition> transitions = new ArrayList<>(classes.size() * alphabet.size());
        for (int i = 0; i < classes.size(); i++) {
            String name = "B" + i;
            newStatesToVertices.put(name, i);
            newVerticesToStates.add(name);
            if (verticesToFinalStates.containsKey(classes.get(i))) {
                newFinalStatesToVertices.put(name, i);
                newVerticesToFinalStates.put(i, name);
            }
        }
        //Once set all the names, inserting the transitions is easier
        for (int i = 0; i < classes.size(); i++) {
            for (String s : alphabet) {
                int dest = next.find(destinoArista(classes.get(i), s));
                System.out.println(next.classes());
                int classof = next.classOf(dest);
                transitions.add(new Transition(newVerticesToStates.get(i),
                        newVerticesToStates.get(next.classOf(dest)), s));
            }
        }
        String newInitialState = newVerticesToStates.get(next.classOf(
                next.find(statesToVertices.get(initialState))));
        return new Automata(id, newVerticesToStates, newStatesToVertices, alphabet,
                newInitialState, newFinalStatesToVertices, newVerticesToFinalStates, transitions);
    }

    public boolean isDeterministic() {
        boolean det = true;
        int i = 0;
        Set labelsFound = new HashSet<>(alphabet.size());
        while (i < numV && det) {
            ListaConPI<Adyacente> ady = elArray[i];
            ady.inicio();
            labelsFound.clear();
            while (!ady.esFin() && det) {
                String label = ady.recuperar().getEtiqueta();
                if (label.equals("")) {
                    det = false;
                } else {
                    if (!alphabet.contains(label)) {
                        throw new AlphabetException(label);
                    }
                    if (labelsFound.contains(label)) {
                        det = false;
                    } else {
                        labelsFound.add(label);
                    }
                }
            }
        }
        return det;
    }

    public boolean hasEmptyTransitions() {
        return existeEtiqueta(LAMBDA);
    }

    public Automata makeDeterministic() {
        Map<String, Integer> newStatesToVertices = new HashMap<>();
        ArrayList<String> newVerticesToStates = new ArrayList<>();
        Map<String, Integer> newFinalStatesToVertices = new HashMap<>();
        Map<Integer, String> newVerticesToFinalStates = new HashMap<>();
        int id = TalServer.objectPool.getNextAutomataID();

        ArrayList<ArrayList<String>> newStates = new ArrayList<>(numV * numV);
        newStates.add(new ArrayList<>());
        newStates.get(0).add(initialState);
        newStatesToVertices.put("B0", 0);
        newVerticesToStates.add("B0");
        if (finalStatesToVertices.containsKey(initialState)) {
            newFinalStatesToVertices.put("B0", 0);
            newVerticesToFinalStates.put(0, "B0");
        }
        ArrayList<Transition> transitions = new ArrayList<>();

        Map<String, ArrayList<String>> tempStates = new HashMap<>(alphabet.size());
        alphabet.forEach(s -> tempStates.put(s, new ArrayList<>()));
        
        ArrayList<String> currentState;
        int i = 0;
        while (i < newStates.size()) {
            currentState = newStates.get(i);
            for (String q : currentState) {
                ListaConPI<Adyacente> ady = elArray[statesToVertices.get(q)];
                for (ady.inicio(); !ady.esFin(); ady.siguiente()) {
                    Adyacente t = ady.recuperar();
                    tempStates.get(t.getEtiqueta()).add(verticesToStates.get(t.getDestino()));
                }
            }
            for (String s : tempStates.keySet()) {
                //Manual contains method
                int j = 0;
                ArrayList<String> state = tempStates.get(s);
                while (j < newStates.size() && !compareArrayLists(state, newStates.get(j))) {
                    j++;
                }
                String destState = "B" + j;
                if (j == newStates.size()) { //New combination of states
                    newStates.add(state);
                    newVerticesToStates.add(destState);
                    newStatesToVertices.put(destState, j);
                    transitions.add(new Transition("B" + i, destState, s));
                }
                //Test if some state is final
                int k = 0;
                while (k < state.size() && !finalStatesToVertices.containsKey(state.get(k))) {
                    k++;
                }
                if (k < state.size()) {
                    newFinalStatesToVertices.put(destState, j);
                    newVerticesToFinalStates.put(j, destState);
                }
            }
            tempStates.values().forEach((s) -> s.clear());
        }
        return new Automata(id, newVerticesToStates, newStatesToVertices, alphabet,
                "B0", newFinalStatesToVertices, newVerticesToFinalStates, transitions);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append("\n");
        sb.append(stateArray.toString()).append("\n");
        sb.append(alphabetArray.toString()).append("\n");
        sb.append(transitionArray.toString()).append("\n");
        sb.append(initialState).append("\n");
        sb.append(finalStateArray.toString());
        return sb.toString();
    }
    
    
    
    public static <T> boolean compareArrayLists(ArrayList<T> a1, ArrayList<T> a2) {
        if (a1.size() != a2.size()) {
            return false;
        }
        int i = 0;
        while (i < a1.size() && a1.get(i).equals(a2.get(i))) {
            i++;
        }
        return i == a1.size();
    }

    public static <T> int arrayContains(T[] array, T object) {
        int i = 0;
        while (i < array.length && !array[i].equals(object)) {
            i++;
        }
        return i == array.length ? -1 : i;
    }
}

/**
 * Method to return a minimized Automata
 *
 * @return The minimum automata
 */
/*
    public Automata minimize2() {
        //Attributes for new Automata
        ArrayList<String> newStates = new ArrayList<>(this.states.length);
        ArrayList<String> newFinalStates = new ArrayList<>(this.finalStates.length);
        ArrayList<Transition> newTransitions = new ArrayList<>(this.transitions.length);
        String newInitialState = null;

        ArrayList<String> states = new ArrayList<>(this.states.length);
        ArrayList<String> finals = new ArrayList<>(finalStates.length);
        ArrayList<String> notFinals = new ArrayList<>(this.states.length - this.states.length);
        
        states.addAll(Arrays.asList(this.states));
        finals.addAll(Arrays.asList(finalStates));
        notFinals.addAll(states);
        notFinals.removeAll(finals);

        UFSet piK = new ForestUFSet(states.size());

        //Create pi0: Finals and not finals
        int firstFinal;
        int firstNoFinal = -1;
        int i = 0;
        //Get first final
        while (!finals.contains(states.get(i))) {
            i++;
        }
        firstFinal = i;
        //Get first no final, if it exists
        if (finals.size() < states.size()) {
            i = 0;
            while (!notFinals.contains(states.get(i))) {
                i++;
            }
            firstNoFinal = i;
        }
        boolean isFinal;
        for (i = 0; i < states.size(); i++) {
            isFinal = finals.contains(states.get(i));
            if (isFinal) {
                if (i != firstFinal) {
                    piK.union(piK.find(firstFinal), piK.find(i));
                }
            } else {
                if (i != firstNoFinal) {
                    piK.union(piK.find(firstNoFinal), piK.find(i));
                }
            }
        }
        //Now piK contains pi0, finals and no finals
        //It's the time to iterate till we get an UFSet that is equivalent to the previous one
        ArrayList<Integer> classes = new ArrayList<Integer>();
        classes.add(firstFinal);
        if (firstNoFinal != -1) {
            classes.add(firstNoFinal);
        }

        UFSet piK1 = new ForestUFSet(states.size());
        ArrayList<Transition>[] graph = (ArrayList<Transition>[]) new ArrayList[states.size()];
        for (i = 0; i < transitions.length; i++) {
            int start = states.indexOf(transitions[i].initialState());
            if (graph[start] == null) graph[start] = new ArrayList<Transition>();
            graph[start].add(transitions[i]);
        }
        do {
            for (i = 0; i < states.size(); i++) {
                for (int j = i; j < states.size(); j++) {
                    if (piK.find(i) == piK.find(j)) {
                        //Were connected before
                        int w = 0;
                        while (w < graph[i].size() && 
                            graph[i].get(w).finalState().equals(graph[j].get(w).finalState())) {
                            //Have same final state
                            int classI = piK1.find(i);
                            int classJ = piK1.find(j);
                            if (classI != classJ) {
                                piK1.union(classI, classJ);
                            }
                        }
                    }
                }
            }
        } while (!piK1.equals(piK));

        
        /*
        //Temp containers
        ArrayList<ArrayList<String>> firstPartitions = new ArrayList<>(states.length);
        ArrayList<String> finals = new ArrayList<>(finalStates.length);
        ArrayList<String> notFinals = new ArrayList<>(states.length - states.length);

        finals.addAll(Arrays.asList(finalStates));
        notFinals.addAll(Arrays.asList(states));
        notFinals.removeAll(finals);

        firstPartitions.add(notFinals);
        firstPartitions.add(finals);

        ArrayList<String>[][] matrix = (ArrayList<String>[][]) new ArrayList[states.length][alphabet.length];

        ArrayList<ArrayList<String>> newPartitions;

        do {
            //Fill matrix
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    int k = 0;
                    while (k < transitions.length && !transitions[k].initialState().equals(states[i])
                            && !transitions[k].symbol().equals(alphabet[j])) {
                        k++;
                    }
                    if (k == transitions.length) {
                        System.err.println("Automata not complete");
                        return null;
                    } else {
                        int b = 0;
                        while (b < firstPartitions.size() && 
                                !firstPartitions.get(b).contains(transitions[k].finalState())) {
                            b++;
                        }
                        if (b == firstPartitions.size()) {
                            System.err.println("Automata not complete");
                            return null;
                        } else {
                            matrix[i][j] = firstPartitions.get(b);
                        }
                    }
                }
            }
            
            //Create new Blocks
            newPartitions = new ArrayList<>(states.length);
            int row = 0;
            for (int i = 0; i < firstPartitions.size(); i++) {
                ArrayList<String> block = firstPartitions.get(i);
                while (!block.isEmpty()) {
                    int k = row;
                    //while (k < block.size() && !compareArrays)
                }
                
                
                row += firstPartitions.size();
            }

        } while (firstPartitions.size() < firstPartitions.size());

         
        return new Automata(ID + "1", (String[]) newStates.toArray(), alphabet,
                (Transition[]) newTransitions.toArray(), newInitialState,
                (String[]) newFinalStates.toArray());
    }
 */
