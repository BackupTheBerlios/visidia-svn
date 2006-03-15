package visidia.graph;

import java.util.*;

import visidia.tools.agents.WithWhiteBoard;

/**
*
*
*/
public interface Vertex extends WithWhiteBoard {
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

    /**
     * Set agents on this vertex.
     */
    public void setAgentsNames(Collection agentsNames);

    /**
     * Get the agent's names on this vertex.
     */
    public Collection getAgentsNames();

    /**
     * Add an agent name to this vertex.
     */
    public void addAgentName(String agentName);

    /**
     * Remove all agents names.
     */
    public void clearAgentNames();

    public Integer getNext();
    public Integer getPrevious();
    public void setNext(Integer i);
    public void setPrevious(Integer i);
    public void setVisualization(boolean s);
    public boolean getVisualization();
    public int getAgentsNumber();
    public void incrementAgentsNumber();
    public void decrementAgentsNumber();
}








