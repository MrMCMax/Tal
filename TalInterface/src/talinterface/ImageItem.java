/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talinterface;

import javafx.scene.image.*;

/**
 *
 * @author maximo
 */
public class ImageItem {
    
    public static final int AUTOMATA = 0;
    public static final int REGEX = 1;
    
    private String name;
    private Image rep;
    private int type;
    private int ID;
    
    public ImageItem(int ID, String name, Image rep, int type) {
        this.ID = ID;
        this.name = name;
        this.rep = rep;
        this.type = type;
    }
    
    public String toString() {
        return name;
    }
    
    public Image getImage() {
        return rep;
    }
    
    public int getID() {
        return ID;
    }
}
