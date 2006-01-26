package fr.enserb.das.graph;


import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.io.*;
import fr.enserb.das.assert.Assertion;





class SimpleGraphVertex  implements Vertex,Serializable {
    //    StringNodeState nodeState ;// StringNodeState("passive");
    Integer id;
    Integer nextDoor;
    Integer previousDoor;
    String nodeState ;
    Vector neighbours;
    Vector edg;
    Object data = null;
    boolean visualization;
    
    /**
     *
     */	
    public SimpleGraphVertex(Integer nodeId){
	id = nodeId;
	neighbours = new Vector(10,0);
	edg = new Vector(10,0);
	visualization=true;
    }
    
    /**
     *
     */	
    void addNeighbour(SimpleGraphVertex sgv, SimpleGraphEdge sge){
	if( !isNeighbour(sgv.identity()) ){
	    neighbours.add(sgv);
	    edg.add(sge);
	}
    }

    /**
     *
     */	
    void removeNeighbour(SimpleGraphVertex sgv){
	Assertion.verify(isNeighbour(sgv.identity()),"id :"+sgv.identity()+" n'est pas voisin de "+identity()+" ",this);	
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
     * retourne l'identite de ce sommet.
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
     * retourne le voisin de numero <i>index</i>. 
     * Les voisins sont a partir de 0 dans leur ordre d'arriv�e.
     * Ne pas confondre les numeros et les identit�s.
     * @exception ArrayIndexOutOfBoundsException est lev�e si <code>index &gt; degree()</code>  
     */	
    public Vertex neighbour(int index){
	return (Vertex) neighbours.get(index);
    }
    
    /**
     * retourne le voisin dont l'identite est <i>id</i>.
     * @exception NoSuchLinkException lev�e si le sommet identifie par <i>id</i>
     * n'est pas voisin de ce sommet.
     */	
    public Vertex neighbour(Integer id){
	return (Vertex) neighbours.get(indexOf(id));
    }
    


    /**
     * retourne l'arete entre ce sommet et le voisin numero <i>index</i>. 
     * @exception ArrayIndexOutOfBoundsException est lev�e si <code>index &gt; degree()</code>  
     */	
    public Edge edge(int index){
	return (Edge) edg.get(index);
    }

    /**
     * retourne l'arete entre ce sommet et le voisin dont l'identite est <i>id</i>.
     * @exception NoSuchLinkException lev�e si le sommet identifie par <i>id</i>
     * n'est pas voisin de ce sommet.
     */	
    public Edge edge(Integer id){
	return (Edge) edg.get(indexOf(id));
    }


    /**
     * retourne une enumeration d'aretes dont ce sommet est un extremite.
     */
    public Enumeration edges(){
	return edg.elements();
    }
    
    /**
     * retourne le numero du voisin identifie par <i>id</i>.
     * @exception NoSuchLinkException lev�e si le sommet identifie par <i>id</i>
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


    /*public void setNodeState(StringNodeState nodeState) {
      nodeState = nodeState;
      }

      public String getNodeState() {
      return nodeState.getString();
      }
    **/
} 



