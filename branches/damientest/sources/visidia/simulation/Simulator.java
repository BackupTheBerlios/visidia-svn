package visidia.simulation;

import visidia.graph.*;
import visidia.tools.*;
import visidia.misc.*;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.*;
import java.awt.Color;

import visidia.gui.metier.simulation.AlgoChoice;
import visidia.simulation.rules.AbstractRule;
import visidia.rule.RSOptions;
/**
*
*
*
*/
public class Simulator {
    //    public static final int THREAD_PRIORITY = Thread.NORM_PRIORITY;
    public static final int THREAD_PRIORITY = 1;
    private SimpleGraph graph;
    private VQueue evtQ;
    private VQueue ackQ;
    private NumberGenerator numGen = new NumberGenerator();
    private AckHandler ackHandler;

    //simulation execution controle variables
    private boolean started = false;
    private boolean aborted = false;
    private boolean paused = false;
    private Object pauseLock = new Object();


    // simulator threads set
    private SimulatorThreadGroup threadGroup = null;
    private int terminatedThreadCount = 0;
    private Object terminatedThreadCountSynchro = new Object();

    // tampon de messages en transit sur le reseaux
    // i.e qui ne sont pas encore mise dans la file
    // de messages de leur destinataire.
    //
    private Hashtable evtObjectTmp;
    
	//nodes data table.
    protected ProcessData[] procs;
    
	
    /**
     * cree une instance de simulateur a partir du graphe "graph", 
     * et des files de messages "inVQueue" et "outVQueue" qui permettent de  
     * communiquer avec l'interface graphique
     */
    /*
    */
    public Simulator( SimpleGraph netGraph, VQueue evtVQueue, VQueue ackVQueue, AlgoChoiceInterface choice){
	graph = (SimpleGraph) netGraph.clone();
	evtObjectTmp = new Hashtable();
	evtQ = evtVQueue;
	ackQ = ackVQueue;
	threadGroup = new SimulatorThreadGroup("simulator");
	procs = new ProcessData[graph.size()];
	
	Enumeration vertices = graph.vertices();
	Enumeration netVertices = netGraph.vertices();
	while(vertices.hasMoreElements() && netVertices.hasMoreElements()){
	    Vertex vertex = (Vertex) vertices.nextElement();
	    Vertex netVertex = (Vertex) netVertices.nextElement();

	    ProcessData processData = new ProcessData();
	    processData.msgVQueue = new VQueue();
	    Object data = netVertex.getData();
	    if(data instanceof Hashtable){
		processData.props = (Hashtable) data;
	    }
	    vertex.setData(processData);
	    processData.algo = choice.getAlgorithm(vertex.identity().intValue());
	    procs[vertex.identity().intValue()] = processData;
	}
    }


    public Simulator(SimpleGraph netGraph, VQueue evtVQueue, VQueue ackVQueue){
	graph = (SimpleGraph) netGraph.clone();
	evtObjectTmp = new Hashtable();
	evtQ = evtVQueue;
	ackQ = ackVQueue;
	threadGroup = new SimulatorThreadGroup("simulator");
	procs = new ProcessData[graph.size()];
	
	Enumeration vertices = graph.vertices();
	Enumeration netVertices = netGraph.vertices();
	while(vertices.hasMoreElements() && netVertices.hasMoreElements()){
	    Vertex vertex = (Vertex) vertices.nextElement();
	    Vertex netVertex = (Vertex) netVertices.nextElement();

	    ProcessData processData = new ProcessData();
	    processData.msgVQueue = new VQueue();
	    Object data = netVertex.getData();
	    if(data instanceof Hashtable){
		processData.props = (Hashtable) data;
	    }
	    vertex.setData(processData);
	    procs[vertex.identity().intValue()] = processData;
	}
    }


    public VQueue evtVQueue(){
	return evtQ;
    }

    public VQueue ackVQueue(){
	return ackQ;
    }
    
    public SimpleGraph getGraph() {
	return graph;
    }
   
    /**
     * met le message dans la file d'attente du voisin de "srcId" 
     * correspondant a la porte "door".
     */
    boolean sendTo(Integer senderId, int door, Message msg)throws InterruptedException{	

	Integer receiverId = graph.vertex(senderId).neighbour(door).identity();
	
	Vertex receiverVertex = graph.vertex(receiverId);
	int receiveDoor = receiverVertex.indexOf(senderId);
	

	MessagePacket msgPacket = new MessagePacket(senderId, door, 
						    receiverId, receiveDoor, msg);
	
	msg.setVisualization((getDraw(senderId.intValue())
			      ||getDraw(receiverId.intValue())));
	pushMessageSendingEvent(msgPacket); 
	return true;

    }

    boolean sendToNext(Integer senderId, Message msg) throws InterruptedException{
	
	Integer receiverId = graph.vertex(senderId).getNext();
	if(receiverId != null) 
	    {
		Vertex senderVertex = graph.vertex(senderId);
		int senderDoor = senderVertex.indexOf(receiverId);
		return sendTo(senderId, senderDoor, msg);
	    }
	return false;
    }
	
	
    Message getNextMessage(Integer nodeId, Door door, Criterion c)throws InterruptedException{
	ProcessData procData = procs[nodeId.intValue()];

	MessagePacket msgPacket = null;
	if(c != null){
	    msgPacket = (MessagePacket)procData.msgVQueue.get(c);
	}
	else{
	    msgPacket = (MessagePacket)procData.msgVQueue.get();
	}

	if(door != null){
	    door.setNum(msgPacket.receiverDoor()); 
	}

	return msgPacket.message();
    }

    Message getNextMessage(Integer nodeId, Door door)throws InterruptedException{
	return getNextMessage(nodeId, door, null);
    }
    
    boolean emptyVQueue(Integer nodeId, Criterion c) throws InterruptedException{
	ProcessData procData = procs[nodeId.intValue()];
	
	boolean msg = false;
	if(c != null){
	    msg = !(procData.msgVQueue.contains(c));
	}
	else{
	    msg = procData.msgVQueue.isEmpty();
	}

	return msg;
    }

    Message getNextMessageFromPrevious(Integer nodeId, Criterion c) throws InterruptedException{
	Integer previous = graph.vertex(nodeId).getPrevious();
	if(previous != null) 
	    {
		Door previousDoor = new Door(previous.intValue());
		return getNextMessage(nodeId, previousDoor, c);
	    }
	else return null;
    }

    private void pushNodePropertyChangeEvent(Integer nodeId, Object key, Object value)throws InterruptedException{
	Long num = new Long(numGen.alloc());
	Object lock = new Object();
	evtObjectTmp.put(num,lock);	
	NodePropertyChangeEvent npce = new NodePropertyChangeEvent(num,nodeId,key,value);
	synchronized(lock){
	    evtQ.put(npce);
	    lock.wait();
	}
    }
	
    private void pushEdgeStateChangeEvent(Integer nodeId1, Integer nodeId2, EdgeState es)throws InterruptedException{
	Long key = new Long(numGen.alloc());
	Object lock = new Object();
	evtObjectTmp.put(key,lock);	
	EdgeStateChangeEvent esce = new EdgeStateChangeEvent(key,nodeId1, nodeId2,es);
	synchronized(lock){
	    evtQ.put(esce);
	    lock.wait();
	}
    }

    /**
     * Method which put the change color event in the queue
     * @param nodeId1 The first vertex of the edge
     * @param nodeId2 The second vertex of the edge
     * @param EdgeColor The new color
     * @throws java.lang.InterruptedException
     */
    private void pushEdgeColorChangeEvent(Integer nodeId1, Integer nodeId2, EdgeColor ec)throws InterruptedException{
	Long key = new Long(numGen.alloc());
	Object lock = new Object();
	evtObjectTmp.put(key,lock);
	EdgeColorChangeEvent ecce = new EdgeColorChangeEvent(key,nodeId1,nodeId2,ec);
	synchronized(lock){
	    evtQ.put(ecce);
	    lock.wait();
	}
    }

    private void pushMessageSendingEvent(MessagePacket mesgPacket)throws InterruptedException{

	Long key = new Long(numGen.alloc());
	evtObjectTmp.put(key,mesgPacket);
	MessageSendingEvent mse = new MessageSendingEvent(key,mesgPacket.message(),mesgPacket.sender(), mesgPacket.receiver());
	evtQ.put(mse);
	
    }



    /**
     * retourne le degre du noeud <i>id</id> .
     */
    public int getArity(Integer id){
	Vertex vertex = (Vertex)graph.vertex(id);
	return vertex.degree();
    }

    /**
     */
    public void changeEdgeState(Integer id, int door, EdgeState newEdgeState)throws InterruptedException{
	Integer neighbId = graph.vertex(id).neighbour(door).identity();
	pushEdgeStateChangeEvent(id,neighbId,newEdgeState);
    }

    /**
     * Method which get the identificator of a vertex
     * and calls the <i>pushEdgeColorChangeEvent</i> method
     * @param id Identificator of the vertex
     * @param door Number of the door
     * @param newEdgeColor The new color
     * @throws java.lang.InterruptedException
     */
    public void changeEdgeColor(Integer id, int door, EdgeColor newEdgeColor) throws InterruptedException{
	Integer neighbId = graph.vertex(id).neighbour(door).identity();
	pushEdgeColorChangeEvent(id,neighbId,newEdgeColor);
    }

    public int sizeOfTheGraph(){
	return graph.size();
    }


    /**
     * instancie les thread a partir du graphe et de l'algorithme donne 
     *en parametre et lance l'execution. Pour le bon fonctionnement du systeme
     *les deux files d'attentes passees en parametre doivent etre vide, mais
     *ceci n'est une condition necessaire.
     */
    public void startSimulation(){
	if( started ){
	    return;
	    //	    stopSimulation();
	}
	
	//initialise algorithm objects
	int graphSize = graph.size();
	for(int id = 0; id < graphSize; id++){
	    Algorithm a = procs[id].algo;
	    a.setId(new Integer(id)); 
	    Thread currThread = new Thread(threadGroup,a);
	    currThread.setPriority(THREAD_PRIORITY);
	    procs[id].processThread = currThread;
	    a.setSimulator(this);
	}    
	
	// start acknowledge handler
	ackHandler = new AckHandler(ackQ);
	ackHandler.setPriority(9);
	ackHandler.start();

	// start node threads
	for(int id = 0; id < graphSize; id++){
	    procs[id].processThread.start();
	}    


	// temps mis par le simulateur pour demarrer la visualisation pour tout les sommets.
	// a revoir ...
	try{
	    Thread.currentThread().sleep(1000);
	} catch (Exception e) {}
	
	/*
 	while(threadGroup.activeCount() < graphSize){
 	    try{
 	    	Thread.currentThread().sleep(10);
 	    }
 	    catch(InterruptedException e){
		//e.printStackTrace();
 	    }
 	}
	*/
	started = true;

	//System.out.println("thread count after start = "+Thread.activeCount());
    }

    /**
     * This method attempts to stop all threads that are rinning in this simulator.
     * Fistly the abstract method <code>abort()</code> is called on all Algorithm objects,
     * and then all threads that run them are interrupted until there are no active thread
     * on the graph.
     */
     public void abortSimulation(){
 	aborted = true;
	
	

 	Enumeration listVertex = graph.vertices();
 	while(listVertex.hasMoreElements() ){
 	    Vertex currentVertex = (Vertex) listVertex.nextElement() ; 
 	    ((ProcessData)currentVertex.getData()).algo.abort();
 	}
 
 	while(threadGroup.activeCount() > 0){
 	    threadGroup.interrupt();
 	    try{
 	    	Thread.currentThread().sleep(50);
 	    }
 	    catch(InterruptedException e){
		//e.printStackTrace();
 	    }
 	}
	
	if(ackHandler != null){
	while(ackHandler.isAlive()){
 	    ackHandler.interrupt();
 	    try{
 	    	Thread.currentThread().sleep(50);
 	    }
 	    catch(InterruptedException e){
		//e.printStackTrace();
 	    }
 	}
	}
	    
	//	System.out.println("thread count after abort = "+Thread.activeCount());
     }
    

    public boolean containsAliveThreads(){
	if(threadGroup.activeCount() > 0){
	    return true;
	}
	if(ackHandler != null){
	    return ackHandler.isAlive();
	}
	return false;
    }

    /**
     */
	public void wedge(){
		if(!paused){
    			paused = true;
    		}
 	}
    
	public void unWedge(){
    		if(paused){
			synchronized(pauseLock){
	    			paused = false;
				pauseLock.notifyAll();
			}
		}
	}
    	

	void runningControl(){
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
    /*
	public void setAlgoProperties(int id, Hashtable newProps){
		Vertex vtx = graph.vertex(new Integer(id));
		Hashtable props = procs[id].props;
		synchronized(props){
			Enumeration v_enum = props.keys();
			while(v_enum.hasMoreElements()){
				Object key = v_enum.nextElement();
				props.put(key, newProps.get(key));
			}
		}
	}
    */
	public Hashtable getAlgoProperties(int id){
		Hashtable props = procs[id].props;
		synchronized(props){
			return (Hashtable) props.clone();
		}
	}

    Object getNodeProperty(int id, Object key){
	synchronized(procs[id].props){
	    return procs[id].props.get(key);
	}
    }		

    void putNodeProperty(int id, Object key, Object value) throws InterruptedException{
	synchronized(procs[id].props){
	    procs[id].props.put(key, value);
	    pushNodePropertyChangeEvent(new Integer(id), key, value);
	}
    }

     /**
     * Counts terminated threads on the graph. If the nummber equals the graph
     * it sends algorithm termination event.
     */
    void terminatedAlgorithm() throws InterruptedException{
	synchronized(terminatedThreadCountSynchro){
	    terminatedThreadCount++;
	    if(terminatedThreadCount == graph.size()){
		// sends algorithms end notification.
		evtQ.put(new AlgorithmEndEvent(numGen.alloc()));

		//kill the acknowledge handler thread
      		while(ackHandler.isAlive()){
		    ackHandler.interrupt();
		    try{
			Thread.currentThread().sleep(50);
		    }
		    catch(InterruptedException e){
			e.printStackTrace();
		    }
		}
	    }
	}
    }

    class AckHandler extends Thread {
	private VQueue ackPipe = null;

	/**
	 * Acknowledge handlerr is instanciated with the acknowledge queue.
	 */
	AckHandler(VQueue q){
	    ackPipe = q;
	}
	
	public void run(){
	    try{
		SimulAck simAck = null;
		while(! aborted){
		    try{
			simAck = (SimulAck) ackPipe.get();
		    }
		    catch(ClassCastException e){
			e.printStackTrace();
			continue;
		    }
		    
		    switch(simAck.type()){
			
		    case SimulConstants.NODE_PROPERTY_CHANGE : 
			handleNodePropertyChangeAck(simAck);
			break;
			
		    case SimulConstants.EDGE_STATE_CHANGE:
			handleEdgeStateChangeAck((EdgeStateChangeAck)simAck);
			break;
			
		    case SimulConstants.MESSAGE_SENT :
			handleMessageSentAck((MessageSendingAck)simAck);
			break;

		    case SimulConstants.EDGE_COLOR_CHANGE:
			handleEdgeColorChangeAck((EdgeColorChangeAck)simAck);
			break;
		    }
		}
	    }
	    catch(InterruptedException e){
		//this interruption should have been caused
		//by the simulation abort.
		//e.printStackTrace();
	    }
	}
	
	
	public void handleNodePropertyChangeAck(SimulAck sa)throws InterruptedException{
	    Object lock = (Object) evtObjectTmp.remove(sa.number());
	    synchronized(lock){
		// normalement il n' y a qu'un seul thread qui serait bloquee
		//sur le lock.
		lock.notifyAll();
	    }
	    numGen.free(sa.number().longValue());
	}
	
	public void handleEdgeStateChangeAck(SimulAck sa)throws InterruptedException{
	    Object lock = evtObjectTmp.remove(sa.number());
	    synchronized(lock){
		// normalement il n' y a qu'un seul thread qui serait bloquee
		//sur le lock.
		lock.notifyAll();
	    }
	    numGen.free(sa.number().longValue());
	}

	public void handleEdgeColorChangeAck(SimulAck sa)throws InterruptedException{
	    Object lock =evtObjectTmp.remove(sa.number());
	    synchronized(lock){
		lock.notifyAll();
	    }
	    numGen.free(sa.number().longValue());
	}
	
	public void handleMessageSentAck_old(MessageSendingAck msa)throws InterruptedException{
	    MessagePacket msgPacket = (MessagePacket) evtObjectTmp.remove(msa.number());
	    Vertex receiverVertex = graph.vertex(msgPacket.receiver());
	    ((ProcessData)receiverVertex.getData()).msgVQueue.put(msgPacket);
	    numGen.free(msa.number().longValue());
	}
	
	public void handleMessageSentAck(MessageSendingAck msa)throws InterruptedException{
	    MessagePacket msgPacket = (MessagePacket) evtObjectTmp.remove(msa.number());
	    procs[msgPacket.receiver().intValue()].msgVQueue.put(msgPacket);
	    numGen.free(msa.number().longValue());
	}
    }
    
    public void restartNode(int nodeId) {
        if(started) {
            // restart failure detector
	    // procs[nodeId].failureDetector.restartDetection();
            // restart algorithm
            procs[nodeId].processThread.stop();
            Thread currThread = new Thread(threadGroup, procs[nodeId].algo);
            currThread.setPriority(THREAD_PRIORITY);
            procs[nodeId].processThread = currThread;
            procs[nodeId].processThread.start();
        }
    }
    
    /**
     * update node properties
     */
    public void setNodeProperties(int nodeId, Hashtable properties) {
	if (started) 
	    procs[nodeId].props = properties;
    }
    
    public boolean getDraw(int id) {
        if(getNodeProperty(id, "draw messages").equals("yes"))
            return true;
        else
            return false;
    }
    
}
