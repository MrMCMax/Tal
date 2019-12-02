/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallogic;

import java.io.*;

/**
 * Writes and gets objects of type T from disk.
 * @author maximo
 * @param <T> Type of object to be retrieved
 */
public interface Persistence<T> {
    public T retrieve(String path) throws IOException, BadFormatException;
    public void save(T object, String path) throws IOException;
}
