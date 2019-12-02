package librerias.estructurasDeDatos.grafos;

import librerias.estructurasDeDatos.modelos.ListaConPI; 
import librerias.estructurasDeDatos.lineales.LEGListaConPI;

/** Implementacion de un grafo Dirigido (Normal, Ponderado o Etiquetado) mediante 
 *  Listas de Adyacencia.<br>
 * 
 * @version Diciembre 2019
 */
public class GrafoDirigido extends Grafo { 
    
    protected int numV, numA;
    protected ListaConPI<Adyacente>[] elArray;
    
    /** Crea un grafo Dirigido vacio con nV vertices. 
     *  @param nV  Numero de vertices del grafo
     */
    @SuppressWarnings("unchecked")
    public GrafoDirigido(int nV) {
        super(true);
        populate(nV);
    }
    
    public GrafoDirigido() {
        super(true);
        numV = 0;
        numA = 0;
        elArray = null;
    }
    
    private void populate(int nV) {
        numV = nV; numA = 0;
        elArray = new ListaConPI[numV]; 
        for (int i = 0; i < numV; i++) {
            elArray[i] = new LEGListaConPI<Adyacente>();
        }
    }
    
    /** Devuelve el numero de vertices de un grafo. 
      *  @return  int Numero de vertices
     */
    @Override
    public int numVertices() { return numV; }

    /** Devuelve el numero de aristas de un grafo
      * @return     Numero de aristas 
     */
    @Override
    public int numAristas() { return numA; }
    
    /** Comprueba si la arista (i,j) esta en un grafo.
      * @param i    Vertice origen
      * @param j    Vertice destino
      * @return boolean true si (i,j) esta y false en caso contrario
     */    
    @Override
    public boolean existeArista(int i, int j) {
        ListaConPI<Adyacente> l = elArray[i]; 
        boolean esta = false;
        for (l.inicio(); !l.esFin() && !esta; l.siguiente()) {
            if (l.recuperar().destino == j) { esta = true; }
        }
        return esta;   
    }
    
    /** Comprueba si la arista (i,j) con etiqueta s esta en un grafo Etiquetado.
      * @param i    Vertice origen
      * @param j    Vertice destino
      * @param s    Etiqueta de (i, j)
      * @return boolean true si (i,j) esta y false en caso contrario
     */
    @Override
    public boolean existeArista(int i, int j, String s) {
        ListaConPI<Adyacente> l = elArray[i]; 
        boolean esta = false;
        for (l.inicio(); !l.esFin() && !esta; l.siguiente()) {
            Adyacente a = l.recuperar();
            if (a.destino == j && a.etiqueta.equals(s)) { esta = true; }
        }
        return esta;   
    }
    
    public boolean existeEtiqueta(String et) {
        ListaConPI<Adyacente> l;
        int i = 0;
        boolean existe = false;
        while (i < numV && !existe) {
            l = elArray[i];
            l.inicio();
            while (!l.esFin() && !existe) {
                if (l.recuperar().etiqueta.equals(et)) {
                    existe = true;
                }
                l.siguiente();
            }
            i++;
        }
        return existe;
    }
    
    /**
     * Devuelve el destino de la arista incidente desde i con etiqueta s, o -1
     * si no existe.
     * @param i vertice origen
     * @param s etiqueta
     * @return vertice destino de (i, j, s), o -1 si no existe
     */
    @Override
    public int destinoArista(int i, String s) {
        ListaConPI<Adyacente> l = elArray[i];
        for (l.inicio(); !l.esFin(); l.siguiente()) {
            if (l.recuperar().getEtiqueta().equals(s)) {
                return l.recuperar().getDestino();
            }
        }
        return -1;
    }
    
    /** Devuelve el peso de la arista (i,j) de un grafo, 0 si dicha arista 
      * no esta en el grafo.
      * @return double Peso de la arista (i,j), 0 si no existe
     */
    @Override
    public double pesoArista(int i, int j) {
        ListaConPI<Adyacente> l = elArray[i];
        for (l.inicio(); !l.esFin(); l.siguiente()) {
            if (l.recuperar().destino == j) {
                return l.recuperar().peso;
            }
        }
        return 0.0;
    }
    
    /** Si no esta, inserta la arista (i, j) en un grafo no Ponderado 
      * (al final de la Lista de adyacentes a i).
      * @param i    Vertice origen
      * @param j    Vertice destino
     */       
    @Override
    public void insertarArista(int i, int j) {
        insertarArista(i, j, Adyacente.PESO_POR_DEFECTO, 
                Adyacente.ETIQUETA_POR_DEFECTO);
    }

    /** Si no esta, inserta la arista (i, j) de peso p en un grafo Ponderado 
      * (al final de la Lista de adyacentes a i).
      * @param i    Vertice origen
      * @param j    Vertice destino
      * @param p    Peso de (i, j)
     */  
    @Override
    public void insertarArista(int i, int j, double p) {
        insertarArista(i, j, p, Adyacente.ETIQUETA_POR_DEFECTO);
    }
       
    /** Si no esta, inserta la arista (i, j) de etiqueta s en un grafo Etiquetado 
      * (al final de la Lista de adyacentes a i).
      * @param i    Vertice origen
      * @param j    Vertice destino
      * @param s    Etiqueta de (i, j)
     */  
    @Override
    public void insertarArista(int i, int j, String s) {
        insertarArista(i, j, Adyacente.PESO_POR_DEFECTO, s);
    }
    
    /** Si no esta, inserta la arista (i, j) de etiqueta s y peso p 
     *  en un grafo Etiquetado y Ponderado
      * (al final de la Lista de adyacentes a i).
      * @param i    Vertice origen
      * @param j    Vertice destino
      * @param p    Peso de (i, j)
      * @param s    Etiqueta de (i, j)
     */  
    @Override
    public void insertarArista(int i, int j, double p, String s) {
        if (!existeArista(i, j)) { 
            elArray[i].insertar(new Adyacente(j, p, s)); 
            numA++; 
        }
    }
    
    /** Devuelve una Lista Con PI que contiene los adyacentes al vertice i.
      * @param i Vertice del que se obtienen los adyacentes
      * @return ListaConPI con los vertices adyacentes a i
     */ 
    @Override
    public ListaConPI<Adyacente> adyacentesDe(int i) {
        return elArray[i];
    }

}

