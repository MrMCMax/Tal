/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librerias.estructurasDeDatos.jerarquicos;

import java.util.ArrayList;
import librerias.estructurasDeDatos.modelos.Partition;
import librerias.estructurasDeDatos.modelos.UFSet;

/**
 *
 * @author max
 */
public class ForestUFSet implements Partition {

    int[] elArray;
    
    private ArrayList<Integer> classes;
    private boolean updated;
    
    public ForestUFSet(int n) {
        elArray = new int[n];
        for (int i = 0; i < elArray.length; i++) {
            elArray[i] = -1;
        }
        classes = null;
        updated = false;
    }
    
    @Override
    public int find(int i) {
        if (elArray[i] < 0) return i;
        return elArray[i] = find(elArray[i]);
    }

    @Override
    public void union(int claseI, int claseJ) {
        if (elArray[claseI] == elArray[claseJ]) {
            elArray[claseI] = claseJ;
            elArray[claseJ]--;
        } else {
            if (elArray[claseI] < elArray[claseJ]) {
                elArray[claseJ] = claseI;
            } else {
                elArray[claseI] = claseJ;
            }
        }
        updated = false;
    }
    
    @Override
    public boolean equals(Object o) {
        System.out.println("Using equals from ForestUFSet");
        if (!(o instanceof UFSet)) return false;
        UFSet other = (UFSet) o;
        for (int i = 0; i < elArray.length; i++) {
            if (this.find(i) != other.find(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ArrayList<Integer> classes() {
        ArrayList<Integer> c = new ArrayList<>(elArray.length);
        for (int i = 0; i < elArray.length; i++) {
            if (elArray[i] < 0) {
                c.add(i);
            }
        }
        this.classes = c;
        updated = true;
        return c;
    }
    
    @Override
    public int classOf(int i) {
        if (updated) {
            int j = 0;
            while (j < classes.size() && !(i == classes.get(j))) { j++; }
            return j;
        } else {
            classes();
            return classOf(i);
        }
    }
}
