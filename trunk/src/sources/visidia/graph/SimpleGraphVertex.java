package visidia.graph;

import java.io.Serializable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;

import visidia.misc.ForbiddenCallException;
import visidia.simulation.LabelChangeEvent;
import visidia.simulation.SimulationAbortError;
import visidia.simulation.agents.AgentSimulator;
import visidia.tools.agents.WhiteBoard;
import visidia.visidiassert.VisidiaAssertion;

public class SimpleGraphVertex implements Vertex, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6624280273922021818L;

	// StringNodeState nodeState ;// StringNodeState("passive");
	Integer id;

	Integer nextDoor;

	Integer previousDoor;

	String nodeState;

	Vector<SimpleGraphVertex> neighbours;

	Vector edg;

	Object data = null;

	boolean visualization;

	Hashtable connectingPorts = new Hashtable();

	private int size = 0;

	private WhiteBoard whiteBoard = null;

	private Collection agentsNames;
	  
	public String previousColor; 
	
	
	public SimpleGraphVertex(Integer nodeId) {
		this(nodeId, null, new Hashtable());
	}

	public SimpleGraphVertex(Integer nodeId, Hashtable defaults,
			Hashtable properties) {
		this.id = nodeId;
		this.neighbours = new Vector(10, 0);
		this.edg = new Vector(10, 0);
		this.visualization = true;

		this.whiteBoard = new WhiteBoard(defaults, properties);
	}

	
	 /**
     * Adds a vertex to the graph as a neighbour of the vertex <i>"this"</i>.
     * @param sgv: the vertex to be added as a neighbour of the vertex <i>"this"</i>.
     * @param sge: the edge to be added to make a link between svg and the vertex <i>"this"</i>.
     * @see addNeighbourToSwitchOn(SimpleGraphVertex sgv, SimpleGraphEdge sge)
     */
	void addNeighbour(SimpleGraphVertex sgv, SimpleGraphEdge sge) {
		Integer neighborIdentity = sgv.identity();
		if (!this.isNeighbour(neighborIdentity)) {
			this.neighbours.add(sgv);
			this.connectingPorts.put(new Integer(this.size), neighborIdentity);
			this.size += 1;
			this.edg.add(sge);
		}
	}
	
	 /**
     * Switches on the neighbour <i>svg</i>.
     * @param sgv: the vertex to be added as a neighbour of the vertex <i>"this"</i>.
     * @param sge: The edge to be added to make a link between svg and the vertex <i>"this"</i>.
     * @see addNeighbour(SimpleGraphVertex sgv, SimpleGraphEdge sge)
     */  
    public void addNeighbourToSwitchOn(SimpleGraphVertex sgv, SimpleGraphEdge sge){ 
    	this.addNeighbour(sgv, sge);
    }
    
	
    /**
     *Removes the neighbour <i>sgv</i>.
     *@param svg: It is the vertex to be removed from the 
     * set containing all the neighbours of the vertex <i>"this"</i>.
     * @see swithOffMyNeighbour(SimpleGraphVertex sgv)
     */	
	void removeNeighbour(SimpleGraphVertex sgv) {
		VisidiaAssertion.verify(this.isNeighbour(sgv.identity()), "id :"
				+ sgv.identity() + " n'est pas voisin de " + this.identity()
				+ " ", this);
		int index = this.indexOf(sgv.identity());
		this.neighbours.remove(index);
		this.edg.remove(index);
	
	}

	 /**
     * swithes off the neighbour <i>sgv</i>.
     * @param svg: It is the vertex to be switch off. It is one of
     * the neighbours of the vertex <i>"this"</i>.
     * @see removeNeighbour(SimpleGraphVertex sgv)
     */
    public void SwitchOffMyNeighbour (SimpleGraphVertex svg){    	
    	this.removeNeighbour(svg); 	
    }
    
	/**
	 * 
	 */
	boolean equals(SimpleGraphVertex sgv) {
		return sgv.identity().equals(this.id);
	}

	void print() {
		Enumeration e = this.neighbours();
		System.out.print(this.id + " ->");
		while (e.hasMoreElements()) {
			Vertex v = (Vertex) e.nextElement();
			System.out.print(v.identity() + " ");
		}
		System.out.println("");
	}

	// implementation de l'interface Vertex

	/**
	 * retourne l'identité de ce sommet.
	 */
	public Integer identity() {
		return this.id;
	}

	/**
	 * retourne le nombre de sommet de ce voisin.
	 */
	public int degree() {
		return this.neighbours.size();
	}

	/**
	 * Retourne une enumeration des sommets voisins de ce sommet.
	 */
	public Enumeration<SimpleGraphVertex> neighbours() {
		return this.neighbours.elements();
	}

	/**
	 * retourne le voisin de numéro <i>index</i>. Les voisins sont à partir de
	 * 0 dans leur ordre d'arrivée. Ne pas confondre les numéros et les
	 * identités.
	 * 
	 * @exception ArrayIndexOutOfBoundsException
	 *                est levée si <code>index &gt; degree()</code>
	 */
	public Vertex neighbour(int index) {
		return (Vertex) this.neighbours.get(index);
	}

	/**
	 * retourne le voisin dont l'identité est <i>id</i>.
	 * 
	 * @exception NoSuchLinkException
	 *                levée si le sommet identifié par <i>id</i> n'est pas
	 *                voisin de ce sommet.
	 */
	public Vertex neighbour(Integer id) {
		return (Vertex) this.neighbours.get(this.indexOf(id));
	}

	/**
	 * retourne l'arête entre ce sommet et le voisin numéro <i>index</i>.
	 * 
	 * @exception ArrayIndexOutOfBoundsException
	 *                est levée si <code>index &gt; degree()</code>
	 */
	public Edge edge(int index) {
		return (Edge) this.edg.get(index);
	}

	/**
	 * retourne l'arête entre ce sommet et le voisin dont l'identité est <i>id</i>.
	 * 
	 * @exception NoSuchLinkException
	 *                levée si le sommet identifié par <i>id</i> n'est pas
	 *                voisin de ce sommet.
	 */
	public Edge edge(Integer id) {
		return (Edge) this.edg.get(this.indexOf(id));
	}

	/**
	 * retourne une enumeration d'arêtes dont ce sommet est un extrémite.
	 */
	public Enumeration edges() {
		return this.edg.elements();
	}

	/**
	 * retourne le numéro du voisin identifié par <i>id</i>.
	 * 
	 * @exception NoSuchLinkException
	 *                levée si le sommet identifié par <i>id</i> n'est pas
	 *                voisin de ce sommet.
	 */
	public int indexOf(Integer id) {
		Enumeration e = this.neighbours();
		int i = 0;

		while (e.hasMoreElements()) {
			Vertex v = (Vertex) e.nextElement();
			if (v.identity().equals(id)) {
				return i;
			}
			i++;
		}

		throw new NoSuchLinkException();
	}

	/**
	 * retourne <code>true</code> si cet <i>id</i> est voisin de cet sommet.
	 */
	public boolean isNeighbour(Integer id) {
		try {
			this.indexOf(id);
		} catch (NoSuchLinkException e) {
			return false;
		}

		return true;
	}

	/**
	 * 
	 */
	public void setData(Object dt) {
		this.data = dt;
	}

	/**
	 * 
	 */
	public Object getData() {
		return this.data;
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
		return this.agentsNames;
	}

	/**
	 * Add an agent name to this vertex.
	 */
	public void addAgentName(String agentName) {
		if (this.getAgentsNames() == null) {
			this.setAgentsNames(new LinkedList());
		}

		this.getAgentsNames().add(agentName);
	}

	/**
	 * Remove all agents names.
	 */
	public void clearAgentNames() {
		this.setAgentsNames(null);
	}

	/**
	 * Accesses the vertex white board and returns the value associated to the
	 * key. To have a white board on this vertex, you must use the constructor
	 * with the Hashtable.
	 * 
	 * @param key
	 *            The key for the value you want
	 * 
	 * @see #setProperty(Object, Object)
	 * @see #SimpleGraphVertex(Integer, Hashtable)
	 */
	public Object getProperty(Object key) {
		if (this.whiteBoard == null) {
			throw new ForbiddenCallException(
					"This vertex hasn't got any white "
							+ "board. You should have pass a "
							+ "Hashtable to the constructor");
		}

		return this.whiteBoard.getValue(key);
	}

	/**
	 * Allow the user to save a value in the white board. To have a white board
	 * on this vertex, you must use the constructor with the Hashtable.
	 * 
	 * @param key
	 *            The key for the value you want
	 * 
	 * @see #getProperty(Object)
	 * @see #SimpleGraphVertex(Integer, Hashtable)
	 */
	public void setProperty(Object key, Object value) {
		if (this.whiteBoard == null) {
			throw new ForbiddenCallException(
					"This vertex hasn't got any white "
							+ "board. You should have pass a "
							+ "Hashtable to the constructor");
		}
		this.whiteBoard.setValue(key, value);
	}

	public Set getPropertyKeys() {
		return this.whiteBoard.keys();
	}

	public void setNext(Integer i) {
		this.nextDoor = i;
	}

	public Integer getNext() {
		return this.nextDoor;
	}

	public Integer getPrevious() {
		return this.previousDoor;
	}

	public void setPrevious(Integer previous) {
		this.previousDoor = previous;
	}

	public void setNodeState(String state) {
		this.nodeState = state;
	}

	public String getNodeState() {
		return this.nodeState;
	}

	public void setVisualization(boolean s) {
		this.visualization = s;
	}

	public boolean getVisualization() {
		return this.visualization;
	}

	/**
	 * Return a Hshtable (key,value) where key is the number of a port (door)
	 * and value corresponds to the identity of the neighbor connected on that
	 * port
	 */
	public Hashtable connectingPorts() {
		return (this.connectingPorts);
	}

	/*
	 * public void setNodeState(StringNodeState nodeState) { nodeState =
	 * nodeState; }
	 * 
	 * public String getNodeState() { return nodeState.getString(); }
      */
	/**
	 * it changes the color of the vertex into <i>color</i>.
	 */
	public void changeColor(AgentSimulator sim, String color){
		System.out.println("je suis le changement du couleur");
		this.setProperty("label", color);
		Long numb = new Long(sim.getNumGen().alloc());
		LabelChangeEvent lce;
		lce = new LabelChangeEvent(numb, ((Vertex)this).identity(),(String)color);
		try {
		    sim.getEvtQ().put(lce);
		} catch (InterruptedException e) {
		    throw new SimulationAbortError(e) ;
		}
	}
	
	/**
	 * It stores the previous color of the vertex to be switch off.
	 */
	public void setPreviousColor(String color){
		 previousColor= color;
	}
}
