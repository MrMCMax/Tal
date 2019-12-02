package librerias.estructurasDeDatos.lineales;


/**
 * Write a description of class LEGListaConPIOrdenada here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class LEGListaConPIOrdenada<E extends Comparable<E>> extends LEGListaConPI<E> {
    
    public LEGListaConPIOrdenada() { super(); }
    
    /** Inserta e en una ListaConPI de forma que sus elementos 
     *  queden ordenados Ascendentemente.
     *  @param e Elemento a insertar.
     */
    @Override
    public void insertar(E e) {
        inicio();
        while (!esFin() && recuperar().compareTo(e) < 0) {
            siguiente();
        }
        super.insertar(e);
    }
}
