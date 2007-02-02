package visidia.network;

//import visidia.algo.*;
import visidia.misc.*;
//import visidia.gui.*;
//import visidia.graph.*;
import visidia.tools.*;
import visidia.simulation.*;

import visidia.tools.VQueue;
import visidia.simulation.MessagePacket;
//import visidia.gui.presentation.userInterfaceSimulation.*;

import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
//import java.io.*;



/** This class models a remote node objects. It allows a node to contact a 
 * neighbor, to send him and receive from him messages. It implements the 
 * NodeInterfaceTry.
 *
 * @author DERBEL Bilel
 * @version 1.0
 */
public class NodeTry extends UnicastRemoteObject implements NodeInterfaceTry {

    public static final int THREAD_PRIORITY = 1;
    
    /** this contains the neighbor of the current node
     */
    protected PortTable portTable;


    protected int sizeOfTheGraph;

    /** This is the message queue of a node
     */
    protected VQueue msgVQueue; 
    
    /** this is the property table of a node. e.g : It contains its label. 
     */
    protected Hashtable props;

    /** don't matter about this 
     */
    private Object propsObject = new Object();
    //    protected Object propsObject = new Object();
    
    /** the algorithm to be executed
     */
    protected AlgorithmDist algorithm;
    
    /** The identity of a node
     */
    protected String identity;

    //protected String visuIdentity;
   
    /** the port where the registry is running. By default, it is 1099.
     * The user can specify it when making new instance of the class Node
     */
    protected String registryPort;
    

    protected int messageNumber = 0;
    protected int synchMessages =0;
    protected int synch =0;
    protected int labelMessages = 0;
    /** every node has a thread which runs the algorithms
     */
    protected Thread thread;

    //simulation execution controle variables
    private boolean started = false;
    private boolean aborted = false;
    private boolean paused = false;
    private Object pauseLock = new Object();
    private Object lock = new Object();
    
    /** This indicate if the messages will be drawn or not. If not the 
     *	message is sent directly to its destination without informing the 
     * console about that.
     */
    protected boolean drawMessage ;
    
    /**Using the locationServer table, a node initialize the nodeStub table
     * by gothering the Stubs of its neighbors. 
     */
    //public Hashtable nodeStub = new Hashtable();

    /** a node has a reference on the console so it can contact it and inform 
     * about the happening events
     */
    public Simulator_Rmi_Int sim_Rmi;
    
    public SimulatorThreadGroup threadGroup = null;

    public NodeTry() throws RemoteException {
	super();
    }
	
    /** creates a new remoteNode object.
     * @param nom the identity of the node
     * @param nomVisu the host where the visualization is carried out
     * @param visuUrl the name in the RMI/registry of the console
     * @param liste this correspond to the locationServer table
     * @param regPort the port of the registry 
     */
    public NodeTry(String nom, String nomVisu, String visuUrl, String regPort) throws RemoteException {
	super();
	this.registryPort = regPort;
	this.identity = nom;
	try {
	    this.sim_Rmi = (Simulator_Rmi_Int)Naming.lookup("rmi://"+nomVisu+":"+registryPort+"/"+visuUrl);
	} catch (Exception e) {}
	this.msgVQueue = new VQueue();
	this.threadGroup = new SimulatorThreadGroup(nom);
    }
    
    public NodeTry(String nom, String nomVisu, String visuUrl, String portNumber, String regPort)
	throws RemoteException {
	
	super(Integer.parseInt(portNumber));
	this.registryPort = regPort;
	this.identity = nom;
	
	try {
	    this.sim_Rmi = (Simulator_Rmi_Int)Naming.lookup("rmi://"+nomVisu+":"+registryPort+"/"+visuUrl);
	} catch (Exception e) {}
	
	this.msgVQueue = new VQueue();
	this.threadGroup = new SimulatorThreadGroup(nom);
    }

    
    //**************************************************************
    //retourne le nombre des voisins
    //**************************************************************
    //public int getArity(Integer identite){

    /**return the number of neighbors : in other word the node degree
     */
    public int getArity(){
	return portTable.size();
    }
    
    //**************************************************************
    //retourne la taille du graphe 
    //**************************************************************

    /** return the size of the graph. This is implemented in a wrong way.
     * to be corrected.
     */
    public int sizeOfTheGraph(){
	return sizeOfTheGraph;
    }
    

    //**************************************************************
    //permet a un objet distant de lancer l'execution du thread
    //**************************************************************
    
    /** this initialize the algorithm to execute by the node. After this step,
     *  each node has can contact any one of his neighbors : the nodeStub table
     * is initializated.
     * @param  algo the algorithm to use. 
     */

    public void startServer(AlgorithmDist algo,PortTable pt, Object obj,int size) throws RemoteException {
	
	try{
	    sizeOfTheGraph=size;
	    portTable = pt;
	    Object data = obj;
	    if (data instanceof Hashtable) {
		props = (Hashtable) data;
		if (((String)props.get("draw messages")).equals("yes")){
		    drawMessage=true;
		} else {
		    drawMessage=false;
		}
	    } else {
		props = new Hashtable();
		drawMessage=true;
	    }
	    
	    
	    algorithm = algo;
	    algorithm.setId(new Integer(identity));
	    algorithm.setServer(this);
	    thread = new Thread(threadGroup,algorithm);
	    thread = new Thread(algorithm);
	    thread.setPriority(THREAD_PRIORITY);
	} catch (Exception e) {
	    System.out.println("Erreur lors de la creation du thread : startServer");
	    e.printStackTrace();
	}
    }

    public void startRunning() throws RemoteException {
	try {
	    thread.start();
	    started = true;
	} catch (Exception e) {
		System.out.println("Erreur dans l'init nodeStub "+e);
		System.out.println("Le demarrage du noeud : "+identity+" a echoue");
	}
    }

    /** nothing else to say
     */
    public void abortServer() throws RemoteException {
	aborted = true ;

	algorithm.abort();
	
	if(thread != null){
	    while(threadGroup.activeCount() > 0){
		threadGroup.interrupt();
		try{
		    Thread.currentThread().sleep(50);
		}
		catch(InterruptedException e){
		    e.printStackTrace();
		    System.out.println("erreur dans le abort de Thread"+e);
		}
	    }
	}
	System.out.println("Thread Desactive");
    }
    
    public void wedge() throws RemoteException {
	if(!paused){
	    paused = true;
	}
    } 
    
    public void unWedge() throws RemoteException {
	if(paused){
	    synchronized(pauseLock){
		paused = false;
		pauseLock.notifyAll();
	    }
	}
    }
    
    /** the algorithm always verify if the remotenode object mustbe running.
     * If not (the simulation is paused) it waits.
     */ 
    public void runningControl(){
	if(aborted){
	    throw new SimulationAbortError();
	}
	
	if(paused){
	    synchronized(pauseLock){
		try{
		    pauseLock.wait();
		}
		catch(InterruptedException e){
		    throw new SimulationAbortError();
		}
	    }
	}
    } 
    

    /** notify the console if the algorithms terminates locally
     */
    public void terminatedAlgorithm(){
	try {
	    sim_Rmi.terminatedAlgorithm();
	} catch (Exception e) {
	}
    }

    
    /** do nothing 
     */
    public boolean containsAliveThreads() throws RemoteException {
	if(threadGroup.activeCount() > 0){
	    return true;
	}
	return false;
    }


    /** This change the drawMesage field to bool
     */
    public void setNodeDrawingMessage(boolean bool) throws RemoteException {
	synchronized(propsObject) {
	    if (bool != drawMessage){
		if(bool)
		    props.put("draw messages","yes");
		else 
		    props.put("draw messages","no");
	    }	   
	    drawMessage= bool;
	}
    }

    /** changes the node properties
     */
    public void setNodeProperties(Hashtable properties) throws RemoteException {
	synchronized(propsObject) {
	    props = properties;
	}
	boolean drawMessageNewValue;
	if (((String)properties.get("draw messages")).equals("yes"))
	    drawMessageNewValue=true;
	else 
	    drawMessageNewValue=false;
	
	if (drawMessageNewValue != drawMessage){
	    drawMessage=drawMessageNewValue;
	}    
    }
    
    //*************************************************************
    //if the message will not be visulizated, it is not send to 
    //GUI
    //*************************************************************
    /** change the type of the messages used. Typically, if a message
     * must be drawn or not.
     */
    public void setMessageType(MessageType msgType, boolean state) throws RemoteException {
	algorithm.setMessageType(msgType,state);
    }
    

    //**************************************************************
    //envoi d'un message a un voisin
    //**************************************************************
    
    /** sends a message to a neighbor.
     * Case I : the message doesn't need to be drawn, so it is directly 
     * sent by applying the receiveFrom method of the neighbor remote node.
     * Case II : the message must be drawn. It is sent to the console by 
     * applying the pushNodeSendEvent of the console and then it is sent 
     * to destination.
     * @param senderId the sender id : the node itself : must be ommitted
     * in future implemntation
     * @param door the door on which the message will leave. 
     * This determinize the identity of the neighbor
     * @param msg : the message to be sent. This message contains the
     * informations about the type of message. @see Message 
    */
    public void sendTo(Integer senderId, int door, Message msg){
	try {
	    messageNumber++;
	    Vector v =  portTable.getElement(new Integer(door));
	    Integer receiverId = (Integer)v.get(0);
	    NodeInterfaceTry server = (NodeInterfaceTry)v.get(1);
	    if (msg.getType().getToPaint() && drawMessage){
		synchronized(lock){
		    //Le message est envoye au simulateur pour visualisation
		    sim_Rmi.pushMessageSendingEvent(senderId,door,receiverId,(Message)msg.clone());
		    lock.wait();
		    MessagePacket msgPacket = new MessagePacket(senderId, door, receiverId,(Message)msg.clone());
		    server.receiveFromNode(msgPacket);
		}
	    } else {
		MessagePacket msgPacket = new MessagePacket(senderId, door, receiverId, (Message)msg.clone());
		server.receiveFromNode(msgPacket);
	    }
	}catch (RemoteException re) {
	    ignore("erreur dans send to : noeud introuvable "+re);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new SimulationAbortError();
	}
    }
   
    
    //****************************************************************
    //recuperation d'un message dans la file des messages
    //****************************************************************
    /** get a message in the queue
     * @param nodeId the node identity. This must be ommitted in future 
     *implementation.
     * @param door if specified, the method returns the message that 
     * arrived on this door. To be corrected
     * @param Criterion don't know 
     */
    public Message getNextMessage(Integer nodeId, Door door, Criterion c){
	MessagePacket msgPacket = null;
	try {
	    if(c != null){
		msgPacket = (MessagePacket)msgVQueue.get(c);
	    }
	    else{
		msgPacket = (MessagePacket)msgVQueue.get();
	    }
	} catch (Exception e) {
	    throw new SimulationAbortError();
	}
	return msgPacket.message();
    }
    
    /**
     */
    public Message getNextMessage(Integer nodeId, Door door)throws Exception{
	return getNextMessage(nodeId, door, null);
    }
    
    

    //*****************************************************************
    //Cette methode est appele par un noeud distant. Elle permet de 
    //de rajouter un message a la fiile des messages.
    //*****************************************************************
    /** This is one of te most important method. It allow a remote node to 
     * put a message in the message queue of the current node.
     */

    public void receiveFromNode(MessagePacket mesgPacket) throws RemoteException {
	try {
	    Integer sender = mesgPacket.sender();
	    mesgPacket.setReceiverDoor(portTable.getDoor(sender));
	    msgVQueue.put(mesgPacket);
	} catch (Exception e) {
	    //a revoir
	    throw new SimulationAbortError();
	}
    } 


    /**
     * Sends an EdgeStateChangeEvent Message to the console
     * @param id must be ommitted : this is the identity of the current node
     * @param door the door where the edge state changed
     * @param newEdgeState the new edge state
    */
    public void changeEdgeState(Integer nodeId, int door, EdgeState newEdgeState) throws RemoteException {
	if( drawMessage){
	    Integer neighbId = portTable.getNeighbor(new Integer(door));
	    pushEdgeStateChangeEvent(nodeId,neighbId,newEdgeState);
	}
    }
    
    private void pushEdgeStateChangeEvent(Integer nodeId1, Integer nodeId2, EdgeState es)throws RemoteException {
	try {
	    synchronized(lock){
		sim_Rmi.pushEdgeStateChangeEvent(nodeId1, nodeId2, es);
		lock.wait();
	    }
	} catch (Exception e) {
	    throw new SimulationAbortError();
	}
    }
    
    /** sends an NodePropertyChangeEvent Message to the console
     * @param id must be ommitted : this is the identity of the current node
     * @param key the key that identify a precise property in the property 
     * table
     * @param value the new value of the property that has changed 
     */     
    public void putNodeProperty(Integer nodeId, Object key, Object value) throws RemoteException {
	synchronized(propsObject){
	    props.put(key, value);
	    if (drawMessage)
		pushNodePropertyChangeEvent(nodeId , key, value);
	    
	}
    }
    
    private void pushNodePropertyChangeEvent(Integer nodeId, Object key, Object value)throws RemoteException {
	try {
	    synchronized(lock){
		sim_Rmi.pushNodePropertyChangeEvent(nodeId,key,value);
		lock.wait();
	    }
	} catch (Exception e) {
	    System.out.println("Erreur dans pushNodeProper... de NodeTry "+e);
	    throw new SimulationAbortError();
	}
    }
    
    
    /** return the property identified by the given key
     * @param id must be ommitted. This is the current identity of the node 
     */
    public Object getNodeProperty(int id, Object key) throws RemoteException {
	synchronized(propsObject){
	    return props.get(key);
	}
    }
    
    public Vector getMessageNumber() throws RemoteException {
	Vector vect = new Vector();
	vect.addElement(new Integer(messageNumber));
	vect.addElement(new Integer(labelMessages));
	vect.addElement(new Integer(synchMessages));
	vect.addElement(new Integer(synch));
	return vect;
    }
    
    public void incrementLabelMessages() {
	labelMessages++;
    }
    public void incrementSynchMessages() {
	synchMessages++;
    }
 
    public void incrementSynch() {
	synch++;
    }

    /** this is applied by the console to tell the node that the message 
     * identified by numLock has been drawm in the GUI. It acts as an ack
     * event.
     */
    //public void free(LockIdentifier numLock) throws RemoteException {
    public void free() throws RemoteException {
	try {
	    synchronized(lock){
		lock.notifyAll();
	    }
	} catch (Exception e) {
	    System.out.println("Erreur dans Free "+e);
	}
    }

    /** this must be implemented to do special treatment if the R/M/I fails. 
     * Up to now, we just catch the exception to let the algorithm continue 
     * running.
     */
    public void ignore(String erreur){
	try {
	    System.out.println(erreur);
	} catch (Exception e){}
    }
    
}

