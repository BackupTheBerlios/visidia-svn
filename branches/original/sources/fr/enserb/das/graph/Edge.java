package fr.enserb.das.graph;
/**
 * Represente l'arete d'un graphe simple. Ces extermites  
 *sont representes par vertexX (X = 1; 2). Bien qu'il y aune distinction
 *entre les deux extremites une arete ne devrait pas comporter la notion de sens; 
 *En effet on a:
 *<pre>
 * e1 = (v11,v12) et e2 = (v21,v22)
 * 
 *                      e1 = e2
 *
 *                 si et seulement si
 *
 *     { v11 = v21 et v12 = v22 } ou { v11 = v22 et v12 = v21 }
 *
 *</pre>
 */
public interface Edge {

    /**
     * Retourne le premier sommet de l'arete
     */
    public Vertex vertex1();
    
    /**
     *Retourne le deuxieme sommet de l'arete
     */
    public Vertex vertex2();
    
    /**
     *retourne l'objet reference par cet arete.
     */
    public Object getData();
    
    /**
     *positionne l'objet reference par cet arete.
     */
    public void setData(Object dt);
}
