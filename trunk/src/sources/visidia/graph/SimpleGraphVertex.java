package visidia.graph;

import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Collection;
import java.util.LinkedList;
import java.io.*;

import visidia.visidiassert.VisidiaAssertion;
import visidia.misc.ForbiddenCallException;
import visidia.tools.agents.WhiteBoard;
import visidia.tools.agents.WithWhiteBoard;

public class SimpleGraphVertex  implements Vertex,Serializable {
                            
    //    StringNodeState nodeState ;// StringNodeState("passive");
    Integer id;
    Integer nextDoor;
    Integer previousDoor;
    String nodeState ;
    Vector neighbours;
    Vector edg;
    Object data = null;
    boolean visualization;
    Hashtable connectingPorts = new Hashtable();
    private int size = 0;
    
    private WhiteBoard whiteBoard = null;

    private Collection agentsNames;
    
    /**
     *
     */	
    public SimpleGraphVertex(Integer nodeId){
        this(nodeId,null, new Hashtable());
    }
    
    public SimpleGraphVertex(Integer nodeId, Hashtable defaults, Hashtable properties ) {
	id = nodeId;
	neighbours = new Vector(10,0);
	edg = new Vector(10,0);
	visualization=true;

//         if (defaults != null)
//             whiteBoard = new WhiteBoard(defaults);

        whiteBoard = new WhiteBoard(defaults,properties);
    }

    void addNeighbour(SimpleGraphVertex sgv, SimpleGraphEdge sge){
	Integer neighborIdentity = sgv.identity();
	if( !isNeighbour(neighborIdentity)){
	    neighbours.add(sgv);
	    connectingPorts.put(new Integer(size),neighborIdentity);
	    size+=1;
	    edg.add(sge);
	}
    }
    
    /**
     *
     */	
    void removeNeighbour(SimpleGraphVertex sgv){
	VisidiaAssertion.verify(isNeighbour(sgv.identity()),"id :"+sgv.identity()+" n'est pas voisin de "+identity()+" ",this);	
	int index = indexOf(sgv.identity());
	neighbours.remove(index);
	edg.remove(index);
    }
    
    /**
     *
     */	
    boolean equals(SimpleGraphVertex sgv){
	return sgv.identity().equals(id);
    }
    
    void print(){
	Enumeration e = neighbours();
	System.out.print(id + " ->");
	while( e.hasMoreElements() ){
	    Vertex v = ( Vertex ) e.nextElement();
	    System.out.print(v.identity() + " ");
	}
	System.out.println("");
    }
    
    //implementation de l'interface Vertex
    
    /**
     * retourne l'identité de ce sommet.
     */	
    public Integer identity(){
	return id;
    }
    
    /**
     *retourne le nombre de sommet de ce voisin.
     */	
    public int degree(){
	return neighbours.size();
    }
    
    /**
     * Retourne une enumeration des sommets voisins de ce sommet.
     */	
    public Enumeration neighbours(){
	return neighbours.elements();
    }
    
    /**
     * retourne le voisin de numéro <i>index</i>. 
     * Les voisins sont à partir de 0 dans leur ordre d'arrivée.
     * Ne pas confondre les numéros et les identités.
     * @exception ArrayIndexOutOfBoundsException est levée si <code>index &gt; degree()</code>  
     */	
    public Vertex neighbour(int index){
	return (Vertex) neighbours.get(index);
    }
    
    /**
     * retourne le voisin dont l'identité est <i>id</i>.
     * @exception NoSuchLinkException levée si le sommet identifié par <i>id</i>
     * n'est pas voisin de ce sommet.
     */	
    public Vertex neighbour(Integer id){
	return (Vertex) neighbours.get(indexOf(id));
    }
    


    /**
     * retourne l'arête entre ce sommet et le voisin numéro <i>index</i>. 
     * @exception ArrayIndexOutOfBoundsException est levée si <code>index &gt; degree()</code>  
     */	
    public Edge edge(int index){
	return (Edge) edg.get(index);
    }

    /**
     * retourne l'arête entre ce sommet et le voisin dont l'identité est <i>id</i>.
     * @exception NoSuchLinkException levée si le sommet identifié par <i>id</i>
     * n'est pas voisin de ce sommet.
     */	
    public Edge edge(Integer id){
	return (Edge) edg.get(indexOf(id));
    }


    /**
     * retourne une enumeration d'arêtes dont ce sommet est un extrémite.
     */
    public Enumeration edges(){
	return edg.elements();
    }
    
    /**
     * retourne le numéro du voisin identifié par <i>id</i>.
     * @exception NoSuchLinkException levée si le sommet identifié par <i>id</i>
     * n'est pas voisin de ce sommet.
     */
    public int indexOf(Integer id){
	Enumeration e = neighbours();
	int i = 0;

	while( e.hasMoreElements() ){
	    Vertex v = ( Vertex ) e.nextElement();
	    if( v.identity().equals(id) ){
		return i;
	    }
	    i++;
	} 

	throw new NoSuchLinkException();
    }


    /**
     * retourne <code>true</code> si cet <i>id</i> est voisin de cet sommet.
     */
    public boolean isNeighbour(Integer id){
	try{
	    indexOf(id);
	}
	catch(NoSuchLinkException e){
	    return false;
	}

	return true;
    }
    
    
    
    /**
     *
     */	
    public void setData(Object dt){
	data = dt;
    }
    
    /**
     *
     */	
    public Object getData(){
	return data;
    }

    /**
     * Set agents on this vertex.
     */
    public void setAgentsNames(Collection agentsNames) {
        this.agentsNames = agentsNames;
    }


    /**
     * Get the agent's names on this vertex.
     */
    public Collection getAgentsNames() {
        return agentsNames;
    }

    /**
     * Add an agent name to this vertex.
     */
    public void addAgentName(String agentName) {
        if (getAgentsNames() == null)
            setAgentsNames(new LinkedList());

        getAgentsNames().add(agentName);
    }
    /**
     * Remove all agents names.
     */
    public void clearAgentNames() {
        setAgentsNames(null);
    }

    /**
     * Accesses  the   vertex  white  board  and   returns  the  value
     * associated to  the key. To have  a white board  on this vertex,
     * you must use the constructor with the Hashtable. 
     *
     * @param key The key for the value you want
     *
     * @see #setProperty(Object, Object)
     * @see #SimpleGraphVertex(Integer, Hashtable)
     */
    public Object getProperty(Object key) {
        if (whiteBoard == null)
            throw new
                ForbiddenCallException("This vertex hasn't got any white " +
                                       "board. You should have pass a " +
                                       "Hashtable to the constructor");

        return whiteBoard.getValue(key);
    }

    /**
     * Allow the  user to save a value  in the white board.  To have a
     * white board on  this vertex, you must use  the constructor with
     * the Hashtable.
     *
     * @param key The key for the value you want
     *
     * @see #getProperty(Object)
     * @see #SimpleGraphVertex(Integer, Hashtable)
     */
    public void setProperty(Object key, Object value) {
        if (whiteBoard == null)
            throw new
                ForbiddenCallException("This vertex hasn't got any white " +
                                       "board. You should have pass a " +
                                       "Hashtable to the constructor");
        whiteBoard.setValue(key, value);
    }

    public Set getPropertyKeys() {
        return whiteBoard.keys();
    }

    public void setNext(Integer i) {
	nextDoor = i;
    }

    public Integer getNext() {
	return nextDoor;
    }

    public Integer getPrevious() {
	return previousDoor;
    }
    public void setPrevious(Integer previous) {
	previousDoor = previous;
    }
    public void setNodeState(String state) {
	nodeState = state;
    }
    
    public String getNodeState() {
	return nodeState;
    }

    public void setVisualization(boolean s){
	visualization=s;
    }

    public boolean getVisualization(){
	return visualization;
    }

    /**
     * Return a Hshtable (key,value) where key is the number of a port (door) 
     * and value corresponds to the identity of the neighbor connected on
     * that port
     */
    public Hashtable connectingPorts() {
	return(connectingPorts);
    }

    /*public void setNodeState(StringNodeState nodeState) {
      nodeState = nodeState;
      }

      public String getNodeState() {
      return nodeState.getString();
      }
    **/
} 



