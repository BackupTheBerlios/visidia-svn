package visidia.graph;


import java.util.*;
/**
*
*
*/
public interface Vertex {
    /**
     * retourne l'identificateur du sommet.
     */
    public Integer identity();

    /**
     * retourne une enumeration des voisins de ce sommet.
     */
    public Enumeration neighbours();
    
    /**
     * retourne le voisin de numéro <i>index</i>. 
     * Les voisins sont à partir de 0 dans leur ordre d'arrivée.
     * Ne pas confondre les numéros et les identités.
     * @exception ArrayIndexOutOfBoundsException est levée si <code>index &gt; degree()</code>  
     */	
    public Vertex neighbour(int index);
    

    /**
     * retourne le voisin dont l'identité est <i>id</i>.
     * @exception NoSuchLinkException levée si le sommet identifié par <i>id</i>
     * n'est pas voisin de ce sommet.
     */	
    public Vertex neighbour(Integer id);

    /**
     *
     *
     */
    public Edge edge(Integer neighbId);

    /**
     *
     *
     */
    public Enumeration edges();

    /**
     * retourne la position du voisin dont l'identité est <i>neighbourId</i> 
     * dans la liste des succeseurs de ce sommet.
     */
    public int indexOf(Integer neighbourId);

    /**
     *retourne <code>true</code> si le sommet dont l'identité est
     *<i>id</i> est voisin de ce sommet.
     */
    public boolean isNeighbour(Integer id);

    /**
     * retourne le nombre de voisins de ce sommet.
     */
    public int degree();
    
    /**
     * positionne l'objet référence par ce sommet.
     */
    public void setData(Object dt);
    
    /**
     * retourne l'objet référence par ce sommet.
     */	
    public Object getData();

    public Integer getNext();
    public Integer getPrevious();
    public void setNext(Integer i);
    public void setPrevious(Integer i);
    public void setVisualization(boolean s);
    public boolean getVisualization();
}








