package librerias.estructurasDeDatos.grafos;

/** Adyacente: representa un vertice adyacente a otro de un grafo y el peso 
 *  de la arista que los une.<br>
 *  
 *  @version Mayo 2018
 */

public class Adyacente { 
    
    public static final int PESO_POR_DEFECTO = 1;
    public static final String ETIQUETA_POR_DEFECTO = "";
    
    protected int destino;
    protected double peso;
    protected String etiqueta;
    
    /**
     * Crea el vertice adyacente a otro de un grafo con un peso por defecto y una etiqueta por defecto
     * @param v Vertice adyacente a otro
     */
    public Adyacente(int v) {
        this(v, PESO_POR_DEFECTO, ETIQUETA_POR_DEFECTO);
    }
    
    /** Crea el vertice v adyacente a otro de un grafo y el peso 
      * de la arista que los une, con una etiqueta por defecto.
      * 
      * @param  v Vertice adyacente a otro
      * @param  p Peso de la arista que une a v con 
      *           el vertice al que es adyacente 
     */
    public Adyacente(int v, double p) { 
        this(v, p, ETIQUETA_POR_DEFECTO);
    }
    
    /**
     * Crea el vertice adyacente a otro de un grafo con un peso por defecto y 
     * una etiqueta determinada
     * @param v Vertice adyacente a otro
     * @param etiqueta Etiqueta de la arista que une a v con 
      *           el vertice al que es adyacente
     */
    public Adyacente(int v, String etiqueta) {
        this(v, PESO_POR_DEFECTO, etiqueta);
    }
    
    /**
     * Crea el vertice adyacente a otro de un grafo con un peso por defecto y 
     * una etiqueta determinada
     * @param v Vertice adyacente a otro
     * @param peso Peso de la arista que une a v con 
      *           el vertice al que es adyacente 
     * @param etiqueta Etiqueta de la arista que une a v con 
      *           el vertice al que es adyacente
     */
    public Adyacente(int v, double peso, String etiqueta) {
        this.destino = v;
        this.peso = peso;
        this.etiqueta = etiqueta;
    }
    
    /** Devuelve el vertice adyacente a otro de un grafo,
      * o vertice destino de la arista que los une.
      * 
      * @return int  Vertice adyacente a otro 
     */
    public int getDestino() { return destino; }

    /** Devuelve el peso de la arista que une a un vertice 
      * de un grafo con un adyacente a este.
      * 
      * @return double  Peso de la arista que une a un adyacente  
      *                 de un vertice de un grafo, 1 si el grafo 
      *                 es No Ponderado
     */
    public double getPeso() { return peso; }
    
    /**
     * Devuelve la etiqueta de la arista que une a un vertice de un grafo
     * con un adyacente a este.
     * @return 
     */
    public String getEtiqueta() { return etiqueta; }
     
    /** Devuelve un String que representa a un adyacente a un 
      * vertice de un grafo y al peso de la arista que los une.
      * 
      * @return  String  que representa a un adyacente
     */     
    public String toString() { return destino + "(" + peso + ") "; }
}