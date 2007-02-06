package visidia.network;

import visidia.simulation.*;
import visidia.graph.*;
import visidia.tools.*;
import visidia.misc.*;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.*;

import java.rmi.*;
import java.rmi.server.*;

/**
 * This is one implementation of the Simultor_Rmi_Int interface. It 
 * allows the initialization of nodes and communicates to the GUI
 * the happening events in the network (e.g : node state changes).
 */
public class Simulator_Rmi extends UnicastRemoteObject implements Simulator_Rmi_Int {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -7826804063720647857L;

	/** The stubs of the graph nodes
     */
    public Hashtable graphStub = new Hashtable();

    /** The Stubs of the LocalNodes 
     */
    public Hashtable nodeServerStub = new Hashtable();
    
    /** The name of the host on which the visualization will be done.
     */
    public String simulatorHost;

    /** The name in the RMI/registry of the  console. It is important 
     * to give not already used name when running two simulation for example.
     */
    public String simulatorUrl;
    
    //public String simulatorPort;

    /** The RMI/Registry port number 
     */
    public String registryPort;

    public static final int THREAD_PRIORITY = 1;
    
    /** the simulation graph
     */
    private SimpleGraph graph;
    private VQueue evtQ;
    private VQueue ackQ;
    private NumberGenerator numGen = new NumberGenerator();
    
    /** This is a thread that notify the node when an events has been 
     * visualisated by the GUI 
     */
    private AckFreeHandler ackHandler;

    private boolean aborted = false;
    private boolean paused = false;
    private Object pauseLock = new Object();


    // simulator threads set
    private SimulatorThreadGroup threadGroup = null;
    private int terminatedThreadCount = 0;
    private Object terminatedThreadCountSynchro = new Object();

    // tampons d'identification des acquittement des evenement suivant le noeud source
    // utilier pour laquittement de la visualisation d'un evenement
    private Hashtable evtObjectTmp;
    
    
    /** creates a new instance of the console using the specified netGraph
     */
    public Simulator_Rmi(SimpleGraph netGraph, VQueue evtVQueue, VQueue ackVQueue, String name,String simUrl, String regPort) throws RemoteException {
	super();
	this.graph = (SimpleGraph) netGraph.clone();
	this.evtObjectTmp = new Hashtable();
	this.simulatorHost = name;
	this.simulatorUrl = simUrl;
	this.registryPort = regPort; 
	this.evtQ = evtVQueue;
	this.ackQ = ackVQueue;
	this.threadGroup = new SimulatorThreadGroup("simulator");
    }


    /** we can initialize the remote-node object using LocalNodes with type 
     * "NodeServer" or "NodeFactory". So, we must specify which type we want
     * to use in the <i> tag </tag> parameter. 
     */
    public void initializeNodes(LocalNodeTable networkParam) throws RemoteException {
	Hashtable hash =  networkParam.content();
	Enumeration v_enum = hash.keys();
	while(v_enum.hasMoreElements()) {
	    String host = (String)v_enum.nextElement();
	    Hashtable localNodes = (Hashtable)hash.get(host);
	    Enumeration e = localNodes.keys();
	    while(e.hasMoreElements()){
		String localNode = (String)e.nextElement();
		Vector vect = (Vector)localNodes.get(localNode);
		try{
		    NodeServer nodeServer = (NodeServer)Naming.lookup("rmi://"+host+":"+this.registryPort+"/NodeServer/"+localNode);
		    this.add(nodeServer.initialize(vect,this.simulatorHost,this.simulatorUrl));
		    Vector v = new Vector();
		    v.addElement(host);
		    v.addElement(localNode);
		    this.nodeServerStub.put(v,nodeServer);
		} catch (Exception expt) {
		    System.out.println("ERREUR : dans l'initialisation des noeuds");
		    expt.printStackTrace();
		}
	    }
	}
    }
    
    
    /** adds stubs of the remote-node objects to the graphStub table
     */ 
    public void add(Object tableStub) {
	if (tableStub instanceof Hashtable){
	    if(tableStub != null){
		Hashtable tableStubTemp = (Hashtable)tableStub;
		Enumeration theNodes = tableStubTemp.keys();
		while(theNodes.hasMoreElements()){
		    String node = (String)theNodes.nextElement();
		    this.graphStub.put(node,tableStubTemp.get(node));
		}
	    }	
	}
    }

    /** using the table graphStub, the console contact the nodes and 
     * order them to begin running the algorithm
     */ 
    public void startServer(AlgorithmDist algo) throws RemoteException {
	/**
	   Initialisation des noeuds distant : 
	   Pour chaque noued --> creation d'une table (key = port, value = neighbor identity)
	**/
	Enumeration theNodes = this.graphStub.keys();
	int j=1;
	int size = this.graph.size();
	Date date1 = new Date();
	System.out.println("Calcul de la table des voisins et transmission des parametres :");
	while(theNodes.hasMoreElements()){
	    try{
		String nodeLocation = (String)theNodes.nextElement();
		NodeInterfaceTry nodeServer = (NodeInterfaceTry)this.graphStub.get(nodeLocation);
		SimpleGraphVertex neighbors = (SimpleGraphVertex)this.graph.vertex(new Integer(nodeLocation));
		Object obj = neighbors.getData();
		Hashtable neighborsTable = neighbors.connectingPorts();
		PortTable pt = new PortTable();
		Enumeration keys =  neighborsTable.keys();
		while(keys.hasMoreElements()) {
		    Integer aPort = (Integer)keys.nextElement();
		    Integer aNode = (Integer)neighborsTable.get(aPort);
		    NodeInterfaceTry nit = (NodeInterfaceTry)this.graphStub.get(aNode.toString());
		    pt.put(aPort,aNode,nit);
		}
		nodeServer.startServer(algo,pt,obj,size);
		j++;
		System.out.print(j+" | ");
	    } catch (Exception e){
		System.out.println("Erreur 1 dans startServer-->Simulator_Rmi :"+e);
		e.printStackTrace();
	    }
	}

	Date date2 = new Date();
	
	/**
	   debut d'execution
	**/

	System.out.println("Demarrage des threads");
	int i = 0;
	Enumeration theNodesBis = this.graphStub.keys();
	while(theNodesBis.hasMoreElements()){
	    try{
		i++;
		System.out.print(i+" | ");
		String nodeLocationBis = (String)theNodesBis.nextElement();
		NodeInterfaceTry nodeServerBis = (NodeInterfaceTry)this.graphStub.get(nodeLocationBis);
		nodeServerBis.startRunning();
	    } catch (Exception e){
		System.out.println("Erreur 2 dans startServer-->Simulator_Rmi :"+e);
	    }
	}
	Date date3 = new Date();
	System.out.println("Demarrage de l initialisation startServer() : "+date1.toString()+" et fin : "+date2.toString());
	System.out.println("Demarrage des noeuds : "+date2.toString()+" et fin : "+date3.toString());
	
	this.ackHandler = new AckFreeHandler(this.ackQ,this.graphStub);
	this.ackHandler.setPriority(9);
	this.ackHandler.start();
	
    }


    public void abortSimulation(){
	this.aborted = true;
	try {
	    Enumeration theServers = this.nodeServerStub.keys();
	    while(theServers.hasMoreElements()){
		Vector server = (Vector)theServers.nextElement();
		NodeServer nodeServer = (NodeServer)this.nodeServerStub.get(server);
		nodeServer.reInitialiser();
	    }
	} catch (Exception e) {
	    System.out.println("Erreur : tentative d'arret des threads "+e);
	}
	
	if(this.ackHandler != null){
	    while(this.ackHandler.isAlive()){
		this.ackHandler.interrupt();
		try{
		    Thread.currentThread().sleep(50);
		}
		catch(InterruptedException e){}
	    }
	}
	//aborted = true;
    }

    /** do nothing
     */
    public boolean containsAliveThreads(){
	if(this.ackHandler != null){
	    return this.ackHandler.isAlive();
	}
	return false;
    }

    /** count the number of nodes which algorithm has terminated.
     * if all the node do nothing (the distributed algorithm is terminated, 
     * it send an AlgorithmEndEvent to the GUI
     */
    public void terminatedAlgorithm() throws RemoteException {
	synchronized(this.terminatedThreadCountSynchro){
	    this.terminatedThreadCount++;
	    if(this.terminatedThreadCount == this.graph.size()){
		try {
		    
		    //NodeInterfaceTry nodeServer = (NodeInterfaceTry)graphStub.get(nodeLocation);
		    //nodeServer.getRound
		    System.out.println("Le nombre final des messages est de : "+this.getMessageNumber());
		    Date dateFin = new Date();
		    System.out.println("Algorithme termine a : "+dateFin.toString());
		    this.evtQ.put(new AlgorithmEndEvent(this.numGen.alloc()));
		    this.abortSimulation();
		} catch (Exception e) {}
	    }
	}
    }
    
    /** contact all the remote nodes and order them to wedge the simulation.
     * This happens when clicking on the "pause" button. 
     */
    public void wedge() throws RemoteException {
	try {
	    Enumeration theNodes = this.graphStub.keys();
	    while(theNodes.hasMoreElements()){
		String node = (String)theNodes.nextElement();
		NodeInterfaceTry nodeServer = (NodeInterfaceTry)this.graphStub.get(node);
		nodeServer.wedge();
	    }
	} catch (Exception e) {
	    System.out.println("Erreur : tentative d interruption des threads"+e);
	}
    
	if(!this.paused){
	    this.paused = true;
	}
    }
    
    /** contact all the remote nodes and order them to unwedge the simulation.
     * This happens when clicking on the "pause" button. 
     */
    public void unWedge() throws RemoteException {
	try {
	    Enumeration theNodes = this.graphStub.keys();
	    while(theNodes.hasMoreElements()){
		String node = (String)theNodes.nextElement();
		NodeInterfaceTry nodeServer = (NodeInterfaceTry)this.graphStub.get(node);
		nodeServer.unWedge();
	    }
	} catch (Exception e) {
	    System.out.println("Erreur : tentative de relance des threads "+e);
	}

	if(this.paused){
	    synchronized(this.pauseLock){
		this.paused = false;
		this.pauseLock.notifyAll();
	    }
	}
    }
    
    
    /** do nothing 
     */
    public void runningControl(){
	if(this.aborted){
	    throw new SimulationAbortError();
	}
	
	if(this.paused){
	    synchronized(this.pauseLock){
		try{
		    this.pauseLock.wait();
		}
		catch(InterruptedException e){
		    throw new SimulationAbortError();
		}
	    }
	}
    } 

    /** chnage a node property by contacting him using his Stub from the 
     * graphStub table
     */
    public void setNodeProperties(int nodeId, Hashtable properties) throws RemoteException {
	try {
	    String node = Integer.toString(nodeId);
	    NodeInterfaceTry nodeServer = ((NodeInterfaceTry)this.graphStub.get(node));
	    nodeServer.setNodeProperties(properties);
	} catch (Exception e) {
	    System.out.println("Erreur : "+e);
	    e.printStackTrace();
	}
	
    }

    /** specify if we want or no drawing the messages on the GUI.
     */
    public void setNodeDrawingMessage(boolean bool) throws RemoteException {
	try {
	    Enumeration theNodes = this.graphStub.keys();
	    while(theNodes.hasMoreElements()){
		String node = (String)theNodes.nextElement();
		NodeInterfaceTry nodeServer = (NodeInterfaceTry)this.graphStub.get(node);
		nodeServer.setNodeDrawingMessage(bool);
	    }
	} catch (Exception e) {
	    System.out.println("Erreur");
	    e.printStackTrace();
	}
    }	

    /** This method allow us to change the properties of the message types 
     */
    public void setMessageType(MessageType msgType, boolean state) throws RemoteException {
	try {
	    Enumeration theNodes = this.graphStub.keys();
	    while(theNodes.hasMoreElements()){
		String node = (String)theNodes.nextElement();
		NodeInterfaceTry nodeServer = (NodeInterfaceTry)this.graphStub.get(node);
		nodeServer.setMessageType(msgType,state);
	    }
	} catch (Exception e) {
	    System.out.println("Erreur : la tentative de changment du type de messages a afficher a echoue "+e);
	    e.printStackTrace();
	}
    }
    
    /**
     */
    public void setGraph(SimpleGraph graphApplet) throws RemoteException{ 
	try {
	    this.graph = (SimpleGraph)graphApplet.clone();
	} catch (Exception e) {
	}
    }
    
    

    /** return tthe stubs of the LocalNodes
     */
    public Hashtable getGraphStub() throws RemoteException {
	return this.nodeServerStub;
    }
    

    public VQueue evtVQueue() throws RemoteException{
	return this.evtQ;
    }

    public VQueue ackVQueue() throws RemoteException{
	return this.ackQ;
    }
    



    public int getMessageNumber() throws RemoteException {
	int totalMessageNumber=0;
	int totalSynch=0;
	int totalSynchMessage=0;
	int totalLabel=0;

	try {
	    Enumeration theNodes = this.graphStub.keys();
	    while(theNodes.hasMoreElements()){
		String node = (String)theNodes.nextElement();
		NodeInterfaceTry nodeServer = (NodeInterfaceTry)this.graphStub.get(node);
		Vector vect = nodeServer.getMessageNumber();
		totalMessageNumber=totalMessageNumber+((Integer)vect.elementAt(0)).intValue();
		totalSynchMessage=totalSynchMessage+((Integer)vect.elementAt(2)).intValue();
		totalLabel=totalLabel+((Integer)vect.elementAt(1)).intValue();
		totalSynch=totalSynch+((Integer)vect.elementAt(3)).intValue();
	    }
	} catch (Exception e) {
	    System.out.println("Erreur : la tentative de changment du type de messages a afficher a echoue "+e);
	    e.printStackTrace();
	}
	System.out.println("Le nombre total de Synchronisation est de : "+totalSynch);
	System.out.println("Le nombre total de label est de : "+totalLabel);
	System.out.println("Le nombre total de message de synchronisation est de : "+totalSynchMessage);
	return totalMessageNumber;
    }


    /** creates a new NodePropertyChangeEvent message and put it in
     * the event queue
     */
    public void pushNodePropertyChangeEvent(Integer nodeId, Object key, Object value) throws InterruptedException, RemoteException {
	try {
	    Long num = new Long(this.numGen.alloc());
	    this.evtObjectTmp.put(num,nodeId);
	    NodePropertyChangeEvent npce = new NodePropertyChangeEvent(num,nodeId,key,value);
	    this.evtQ.put(npce);
	} catch (Exception e) {
	    System.out.println("erreur dasn PushProp-->SimulatorRmi "+e);
	}
    }
    

     /** creates a new EdgeStateChangeEvent message and put it in
     * the event queue
     */
    public void pushEdgeStateChangeEvent(Integer nodeId1, Integer nodeId2, EdgeState es)throws InterruptedException, RemoteException {
	try {
	    Long key = new Long(this.numGen.alloc());
	    this.evtObjectTmp.put(key,nodeId1);
	    EdgeStateChangeEvent esce = new EdgeStateChangeEvent(key,nodeId1, nodeId2,es);
	    this.evtQ.put(esce);
	} catch (Exception e) {
	    System.out.println("Erreur pushEdge-->SimulatorRMi "+e);
	}
    }
    
    /** creates a new MessageSendingEvent  message and put it in
     * the event queue
     */
    public void pushMessageSendingEvent(Integer senderId, int door,Integer receiverId, Message msg)throws InterruptedException, RemoteException {
	try {
	    //construction du paquet pour l'affichage
	    Vertex receiverVertex = this.graph.vertex(receiverId);
	    int receiveDoor = receiverVertex.indexOf(senderId);
	    MessagePacket mesgPacket = new MessagePacket(senderId, door, receiverId, receiveDoor,msg);
	   
	    //Mise du paquet dans la file de message a afficher et
	    //memorisation de l'identifiant du paquet
	    Long key = new Long(this.numGen.alloc());
	    this.evtObjectTmp.put(key,senderId);
	    
	    MessageSendingEvent mse = new MessageSendingEvent(key, mesgPacket.message(),mesgPacket.sender(), mesgPacket.receiver());
	    this.evtQ.put(mse);
	} catch (Exception e) {
	    System.out.println("Erreur pushMessag-->SimulatorRmi "+e);
	}
    }

    
    /**
     * register a local Node
     */
    public void register(NodeServerImpl nsi, String host, String url) throws RemoteException {
	System.out.println("Local Node ("+host+","+url+") try to register him self on the console");
    }

    
    /** Pour les acquitement de visualization 
     */
    class AckFreeHandler extends Thread {
	private VQueue ackPipe = null;
	private Hashtable locReso = null;
	/**
	 * Acknowledge handlerr is instanciated with the acknowledge queue.
	 */
	AckFreeHandler(VQueue q, Hashtable table){
	    this.ackPipe = q;
	    this.locReso = table;
	}
	
	public void run(){
	    try{
		SimulAck simAck = null;
		while(! Simulator_Rmi.this.aborted){
		    try{
			simAck = (SimulAck) this.ackPipe.get();
		    }
		    catch(ClassCastException e){
			e.printStackTrace();
			continue;
		    }
		    
		    switch(simAck.type()){
			
		    case SimulConstants.NODE_PROPERTY_CHANGE : 
			this.handleNodePropertyChangeAck(simAck);
			break;
			
		    case SimulConstants.EDGE_STATE_CHANGE:
			this.handleEdgeStateChangeAck(simAck);
			break;
			
		    case SimulConstants.MESSAGE_SENT :
			this.handleMessageSentAck((MessageSendingAck)simAck);
			break;
		    }
		}
	    }
	    catch(Exception e){
	    }
	}
	
	public void handleNodePropertyChangeAck(SimulAck sa)throws InterruptedException, RemoteException {
	    try {
		Integer nodeSource = (Integer)Simulator_Rmi.this.evtObjectTmp.remove(sa.number());
		NodeInterfaceTry node = (NodeInterfaceTry)this.locReso.get(nodeSource.toString());
		node.free();
		Simulator_Rmi.this.numGen.free(sa.number().longValue());
	    } catch (RemoteException re) {
		this.ignore("Erreur : reseaux indisponible pour acquitement chgt etat noeud");
	    } catch (Exception e ){
		System.out.println("ERREUR : handleNodePropertyChangeAck");
		System.out.println(e);
	    }
	}
	
	public void handleEdgeStateChangeAck(SimulAck sa)throws InterruptedException, RemoteException {
	    try{
		Integer nodeSource = (Integer)Simulator_Rmi.this.evtObjectTmp.remove(sa.number());
		NodeInterfaceTry node = (NodeInterfaceTry)this.locReso.get(nodeSource.toString());
		node.free();
		Simulator_Rmi.this.numGen.free(sa.number().longValue());
	    } catch (RemoteException re) {
		this.ignore("Erreur : reseaux indisponible pour acquitement de chgt etat arrete");
	    } catch (Exception e) { 
		System.out.println("ERREUR : handleEdgeStateChangeAck");
		System.out.println(e);
	    }
	}
	
	public void handleMessageSentAck(MessageSendingAck msa)throws InterruptedException {
	    try {
		Integer nodeSource = (Integer)Simulator_Rmi.this.evtObjectTmp.remove(msa.number());
		NodeInterfaceTry node = (NodeInterfaceTry)this.locReso.get(nodeSource.toString());
		node.free();
		Simulator_Rmi.this.numGen.free(msa.number().longValue());
	    } catch (RemoteException re) {
		this.ignore("Erreur : Impossible de contacter un noeud pour acquitement d'envoie de message");
	    } catch (Exception e) {
		System.out.println("ERREUR : handleMessageSentAck "+e);
	    }
	}


	public void ignore(String erreur) {
	    try {
		System.out.println(erreur);
	    } catch (Exception e) {}
	}
	

    }    
}
