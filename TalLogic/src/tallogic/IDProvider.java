/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallogic;

import java.util.NoSuchElementException;
import java.util.Stack;

/**
 *
 * @author max
 */
public class IDProvider<T> {
    private T[] array;
    private Stack<Integer> deletedIDs;
    private int pointer;
    
    public IDProvider() {
        this(10);
    
    }
    @SuppressWarnings("unchecked")
    public IDProvider(int n) {
        array = (T[]) new Object[n];
        deletedIDs = new Stack<>();
        pointer = 0;
    }
    
    public int getNextID() {
        if (deletedIDs.size() > 0) {
            return deletedIDs.peek();
        } else {
            return pointer;
        }
    }
    
    public int newID(T object) {
        int res;
        if (deletedIDs.size() > 0) {
            array[res = deletedIDs.pop()] = object;
            return res;
        } else {
            if (pointer > array.length) {
                T[] newArray = (T[]) new Object[array.length * 2];
                System.arraycopy(array, 0, newArray, 0, array.length);
                array = newArray;
            }
            res = pointer;
            array[pointer++] = object;
            return res;
        }
    }
    
    public T get(int id) {
        return array[id];
    }
    
    public void deleteID(int id) {
        if (pointer < id) {
            array[id] = null;
            deletedIDs.push(id);
        } else {
            throw new NoSuchElementException();
        }
    }
}
