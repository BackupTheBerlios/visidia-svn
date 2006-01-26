package fr.enserb.das.simulation;

import fr.enserb.das.misc.*;
import fr.enserb.das.tools.*;
import fr.enserb.das.graph.*;
import java.util.*;
import java.awt.Color;


public abstract class Algorithm implements Runnable,Cloneable
{
    protected final static StartSignal beginExec = new StartSignal();

    private Integer nodeId = null ;
    private Simulator sim ; 
    int lamportClock = 0;
    
    public Collection getListTypes(){
	Collection typesList = new LinkedList();
	typesList.add(MessageType.defaultMessageType);
	return typesList;
    }

    
    /**
     * Permet d'envoyer le message "msg" sur la porte "door".
     */
    protected void sendTo(int door, Message msg)
    {
    	sim.runningControl();

	lamportClock++;
	msg.setLamportClockStamp(lamportClock);
    	try{
	    sim.sendTo(nodeId, door, msg) ;	
	}
	catch(InterruptedException e){
		throw new SimulationAbortError();
	}

    } 
    protected void sendToNext(Message msg) {
	sim.runningControl();
	lamportClock++;
	msg.setLamportClockStamp(lamportClock);
    	try{
	    sim.sendToNext(nodeId, msg) ;	
	}
	catch(InterruptedException e){
		throw new SimulationAbortError();
	}
    }

    
    /**
     * Envoie le message "msg" à tous les voisins de ce noeud.
     */
    protected void sendAll(Message msg)
    {
	int arite = getArity() ;
	for( int i=0; i < arite ; i++)
		sendTo(i, msg );
    }
    
    /**
     * Retourne le premier message reçu de la porte "door".Tant que le noeud 
     * ne reçoit pas de message sur cette porte,il est bloqué.
     */
    protected Message receiveFrom(int door)
    {
    	sim.runningControl();

	Message msg = null;
    	try{
		msg =  sim.getNextMessage( nodeId ,null, new DoorCriterion(door));
	}
	catch(InterruptedException e){
		throw new SimulationAbortError();
	}
	
	int stamp = msg.getLamportClockStamp();
	lamportClock = Math.max(stamp,lamportClock) + 1;
	return msg;
    }

    
    protected Message receiveFromPrevious() {
	sim.runningControl();
	Message msg = null;
    	try{
		msg = sim.getNextMessageFromPrevious(nodeId , null);
	}
	catch(InterruptedException e){
		throw new SimulationAbortError();
	}

	int stamp = msg.getLamportClockStamp();
	lamportClock = Math.max(stamp,lamportClock) + 1;
	return msg;
    }
    protected Message receiveFrom(int door,MessageCriterion mc)
    {
    	sim.runningControl();

	DoorCriterion dc = new DoorCriterion(door);
	MessagePacketCriterion mpc = new MessagePacketCriterion(mc);
	CompoundCriterion c = new  CompoundCriterion();
	c.add(dc);
	c.add(mpc);

	Message msg = null;
    	try{
		msg = sim.getNextMessage(nodeId , null, c);
	}
	catch(InterruptedException e){
		throw new SimulationAbortError();
	}

	int stamp = msg.getLamportClockStamp();
	lamportClock = Math.max(stamp,lamportClock) + 1;
	return msg;
    }  

    /**
     * Retourne le premier message de la file d'attente de ce noeud.
     *si la file est vide,le noeud est endormi jusqu'à réception d'un message.
     */
    protected Message receive(Door door)
    {
    	sim.runningControl();
	Message msg = null;
    	try{
		msg = sim.getNextMessage(nodeId, door, null);
	}
	catch(InterruptedException e){
		throw new SimulationAbortError();
	}

	int stamp = msg.getLamportClockStamp();
	lamportClock = Math.max(stamp,lamportClock) + 1;
	return msg;
    }

    protected Message receive(Door door, MessageCriterion mc)
    {
    	sim.runningControl();
	Message msg = null;
    	try{
		msg = sim.getNextMessage(nodeId, door, new MessagePacketCriterion(mc));
	}
	catch(InterruptedException e){
		throw new SimulationAbortError();
	}

	int stamp = msg.getLamportClockStamp();
	lamportClock = Math.max(stamp,lamportClock) + 1;
	return msg;
    }

   

    /**
     * Retourne le degré du sommet.
     */

    protected int getArity()
    {
    	sim.runningControl();
	return sim.getArity(nodeId);
    }
    
    protected int nextDoor() {
	Integer next = sim.getGraph().vertex(getId()).getNext();
	return sim.getGraph().vertex(getId()).indexOf(next);

    }

    protected int previousDoor() {
	Integer previous = sim.getGraph().vertex(getId()).getPrevious();
	return sim.getGraph().vertex(getId()).indexOf(previous);
    }
    /**
     * Permet de relier le noeud au simulateur. "s" est défini comme 
     * simulateur du noeud. 
     */
    void setSimulator(Simulator s){
	sim = s;
    }

    /**
     * Permet de changer l'identité du noeud, en cas de renumérotation.
     */
    void setId(Integer id){
	nodeId = new Integer(id.intValue());
    }

    /**
     * retourne l'identité du noeud.
     */
    protected Integer getId(){
    	sim.runningControl();
	return nodeId;
    }


    /**
     * Algorithme à exécuter par ce noeud. L'utilisateur doit implémenter cette
     * méthode si il désire écrire un algorithme sans passer par les règles
     * de réécriture. 
     */
    public abstract void init(); //algorithme de l utilisateur.


    /**
     * Méthode faisant appel à <code>init()</code>. 
     * Cette méthode est nécessaire lors de l'instanciation 
     * du thread,un thread doit posséder la méthode <code>run()</code>.
     */
    public final void run()
    {
	init();
	try{
	    sim.terminatedAlgorithm();
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}
    }


    protected void setDoorState(EdgeState st,int door){
    	sim.runningControl();
    	try{
		sim.changeEdgeState(getId(),door,st);
	}
	catch(InterruptedException e){
		throw new SimulationAbortError();
	}
    }

    protected int getNetSize(){
    	sim.runningControl();
	return sim.sizeOfTheGraph();
    }

    /** 
     * This method changes the color of an edge.
     * Warning: You must include the java.awt.Color in your Algorithm
     * @param door The number of the door
     * @param color A new color for the edge: <i>new ColorState(Color)</i>
     **/
    protected void setEdgeColor(int door, EdgeColor color){
	sim.runningControl();
	try{
	    sim.changeEdgeColor(getId(),door,color);
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}
    }

    /*Edge edge;
	// Permet de retourner le vertex voisin de la porte
	Vertex voisin = sim.getGraph().vertex(getId()).neighbour(door);
	
	// Retourne l'arete(de type Edge) entre la porte et le vertex voisin
	edge = voisin.edge(sim.getGraph().vertex(getId()).identity());
	// Retourne l'objet représentant l'arete
	edge.setColor(color);*/
    

    /**
     * This method is call to message the current algorithm to
     * stop running. It should be overrided by subclasses. 
     */
    protected void abort(){
    }

    public abstract Object clone();
    
    /**
     * Sets this node property. If <code>value</code> is null the property is removed.
     */
    protected void putProperty(String key, Object value){
	try{
	    sim.putNodeProperty(getId().intValue(), key, value);
	}
	catch(InterruptedException e){
		throw new SimulationAbortError();
	}
	lamportClock++;
    }
    
    /**
     * Returns the property value identified by <code>key</code>. If no property identified
     * by <code>key</code> exits, it <code>returns</code> null.
     */
    protected Object getProperty(String key){
	return sim.getNodeProperty(getId().intValue(),key);
    }

    /**
     *
     */
    protected int getLamportClock(){
	return lamportClock;
    }
}
