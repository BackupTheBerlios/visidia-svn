package visidia.simulation;

import visidia.graph.*;
import visidia.tools.*;
import visidia.misc.*;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;


/**
 * Attention : classe centrale. Elle représente l'entité principale
 * qui permet l'envoi et la réception de messages. Toutes les
 * communications des threads simulants les noeuds sont prises en
 * compte ici.
 *
 */


public class Simulator {
    // public static final int THREAD_PRIORITY = Thread.NORM_PRIORITY;
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


    /*
     * tampon de messages en transit sur le reseaux i.e qui ne sont
     * pas encore mise dans la file de messages de leur destinataire.
     */
    private Hashtable evtObjectTmp;
    
    //nodes data table.
    protected ProcessData[] procs;
    
	
    /**
     * crée une instance de simulateur à partir du graphe "graph", et
     * des files de messages "inVQueue" et "outVQueue" qui permettent
     * de communiquer avec l'interface graphique
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
      * compte le nombre de fois qu'un acquitement de visualation
      * relatif aux pulses est arrivé
      */
     //private int countPreviousPulseAck = 0;
     private Object lockSync = new Object();

     /** 
      * le nombre de noeud qui sont passés au pulse suivant
      */
     private int countNextPulse = 0;

     /** 
      * le pulse courant
      */
     private int pulse = 1;



     /** 
      * acquitement des traitements de visualizations et surtout
      * d'envoi de messages. On compte deux fois : un pour le
      * siulEventHandler et deux pour le SimulationPanel
      *
      */
     protected void nextAckPulse() {
	 synchronized(lockSync) {
	     try{
		 lockSync.notifyAll();
	     } catch (Exception e) {
		 e.printStackTrace();
	     }
	 }
     }

     public void nextPulse() {
	 try{
	     synchronized(lockSync){
		 countNextPulse=(countNextPulse+1)%(graph.size()-terminatedThreadCount);
		 if(countNextPulse == 0) {
		     try{
			 pushNextPulseEvent();
		     } catch (Exception e) {
			 // aucune exception n'est normalement levée ici
			 System.out.println(" bug ref 0 Simulator Synchrone");
		     }
		     pulse ++;
		 }
		 lockSync.wait();
	     }
	} catch (Exception e) {
	    System.out.println(" bug ref 0.1 : Simulator Synchrone");
	    e.printStackTrace();
	}
    }

    
    public int getPulse() {
	return pulse ;
    }

    

    /**
     * met le message dans la file d'attente du voisin de "srcId"
     * correspondant à la porte "door".
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

    /**
     * should work with any well deifined criterion. For the moment it
     * works with the DoorPulseCriterion. return the first message
     * received by nodeId which matchs the criterion dpc. If none,
     * then return null.
     *
     */
    Message getNextMessage(Integer nodeId, DoorPulseCriterion dpc)throws InterruptedException{

	ProcessData procData = procs[nodeId.intValue()];

	MessagePacket msgPacket = null;
	msgPacket = (MessagePacket)procData.msgVQueue.getNoWait(dpc);
	
	if(msgPacket != null) {
	    dpc.setDoor(msgPacket.receiverDoor()); 
	    dpc.setPulse(msgPacket.message().getMsgClock()); 
	    return msgPacket.message();
	} 
	
	return null;

    }


    /**
     * return a vector of all message received by nodeId and matching
     * the criterion dpc. If none then return null. Not used for the moment don't work
     */
    Vector getAllNextMessages(Integer nodeId, DoorPulseCriterion dpc)throws InterruptedException{

	ProcessData procData = procs[nodeId.intValue()];

	Vector msgPacketVect = null;
	msgPacketVect = procData.msgVQueue.getAllNoWait(dpc);
	
	if(msgPacketVect != null) {
	    Vector v = new Vector();
	    dpc.setDoor(((MessagePacket)msgPacketVect.elementAt(0)).receiverDoor()); 
	    dpc.setPulse(((MessagePacket)msgPacketVect.elementAt(0)).message().getMsgClock()); 
	    
	    for(int i = 0; i< msgPacketVect.size(); i++) {
		v.addElement(((MessagePacket)msgPacketVect.elementAt(0)).message());
	    }
	    return v;
	}
	return null;
    }

    
    void purge(Integer nodeId) {
	ProcessData procData = procs[nodeId.intValue()];
	procData.msgVQueue = new VQueue();
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
    private void pushEdgeColorChangeEvent(Integer nodeId1, Integer nodeId2, EdgeColor ec) throws InterruptedException{
	Long key = new Long(numGen.alloc());
	Object lock = new Object();
	evtObjectTmp.put(key,lock);
	EdgeColorChangeEvent ecce = new EdgeColorChangeEvent(key,nodeId1,nodeId2,ec);
	synchronized(lock){
	    evtQ.put(ecce);
	    lock.wait();
	}
    }

    private void pushMessageSendingEvent(MessagePacket mesgPacket) throws InterruptedException{

	Long key = new Long(numGen.alloc());
	evtObjectTmp.put(key,mesgPacket);
	MessageSendingEvent mse = new MessageSendingEvent(key,mesgPacket.message(),mesgPacket.sender(), mesgPacket.receiver());
	evtQ.put(mse);
	
    }


    private void pushNextPulseEvent() throws InterruptedException{
	Long key = new Long(numGen.alloc());
	NextPulseEvent npe = new NextPulseEvent(key,pulse);
	evtQ.put(npe);
    }
  
    

    /**
     * retourne le degrès du noeud <i>id</id> .
     */
    public int getArity(Integer id){
	Vertex vertex = graph.vertex(id);
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
     * instancie les thread à partir du graphe et de l'algorithme
     * donne en parametre et lance l'execution. Pour le bon
     * fonctionnement du système les deux files d'attentes passées en
     * parametre doivent être vide, mais ceci n'est pas une condition
     * nécessaire.
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


	// temps mis par le simulateur pour demarrer la visualisation
	// pour tout les sommets.  a revoir ...
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
	System.out.println("thread count after start = "+Thread.activeCount());
    }

    /**
     * This method attempts to stop all threads that are rinning in
     * this simulator.  Fistly the abstract method
     * <code>abort()</code> is called on all Algorithm objects, and
     * then all threads that run them are interrupted until there are
     * no active thread on the graph.
     */
    public void abortSimulation(){
	 
	aborted = true;
	 
	/**
	 * ne fonctionne pas vraiment bien l'arrêt des thread, à
	 * revoir
	 **/
	
	Enumeration listVertex = graph.vertices();
	while(listVertex.hasMoreElements() ){
 	    Vertex currentVertex = (Vertex) listVertex.nextElement() ; 
 	    ((ProcessData)currentVertex.getData()).algo.abort();
 	}
	
	System.out.print("   Threads notified");
	try{
	    
	    synchronized(lockSync) {
		System.out.print(" .");
		lockSync.notifyAll();
	    }
	    System.out.print(".. ");
	    
	} catch (Exception e) {
	    // e.printStackTrace();
	}
	
	try{
	    evtQ.notifyAllGet();
	    ackQ.notifyAllGet();
	} catch(Exception e) {
	    // e.printStackTrace();
	}
	
	System.out.print(" waiting for threads .");
 	while(threadGroup.activeCount() > 0){
 	    threadGroup.interrupt();
 	    try{
 	    	Thread.currentThread().sleep(50);
 	    }
 	    catch(InterruptedException e){
		//e.printStackTrace();
 	    }
 	}

	System.out.print(" .. terminated.  ");
	System.out.println(" Stopping the handler ");
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
	// System.out.println("thread count after abort = "+Thread.activeCount());
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
     * Counts terminated threads on the graph. If the nummber equals
     * the graph it sends algorithm termination event.
     */
    void terminatedAlgorithm() throws InterruptedException{

	// Si par hasard un thread (sommet) termine et les n-1 autres
	// threads sont toujours actif. En plus si les n-1 threads
	// font appel à la méthode nextPulse() avant que notre thread
	// n'appele terminated thread (avant d'incrémenter la variable
	// terminated thread. Alors il y'a deadlock des n-1 thread: le
	// handler ne les réveille pas car personne n'a exécuter la
	// méthode pushNextPulseEvent(). Un thread qui termine doit
	// vérifier que les threads encore actifs ne sont pas een
	// attente d'être débloqués ...
	synchronized(lockSync){
	    //if (countNextPulse !=0) {
	    int i = (countNextPulse+1)%(graph.size()-terminatedThreadCount);
	    
	    if((i == 0) && (terminatedThreadCount != graph.size()-1)) {
		try{
		    pushNextPulseEvent();
		} catch (Exception e) {
		    // aucune exception n'est normalement levée ici
		    System.out.println(" bug ref 0.3 Simulator Synchrone");
		}
		pulse ++;
	    }	   
	    //}
	    terminatedThreadCount++;
	}

	
	if(terminatedThreadCount == graph.size()){
	    // sends algorithms end notification.
	    evtQ.put(new AlgorithmEndEvent(numGen.alloc()));
	    System.out.println("Algorithm Terminated");
	    
	    
	    //kill the acknowledge handler thread
	    while(ackHandler.isAlive()){
		try{
		    Thread.currentThread().sleep(50);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		aborted = true;
		ackHandler.interrupt();
		
		try{
		    evtQ.notifyAllGet();
		    ackQ.notifyAllGet();
		} catch(Exception e) {
		    e.printStackTrace();
		}
		
		
		try{
		    Thread.currentThread().sleep(50);
		}
		
		catch(InterruptedException e){
		    e.printStackTrace();
		}
	    }
	}
    }
    
    
    class AckHandler extends Thread {
	private VQueue ackPipe = null;
	
	/**
	 * Acknowledge handler is instanciated with the acknowledge queue.
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
			
		    case SimulConstants.EDGE_STATE_CHANGE :
			handleEdgeStateChangeAck(simAck);
			break;
			
		    case SimulConstants.MESSAGE_SENT :
			//System.out.println("Message");
			handleMessageSentAck((MessageSendingAck)simAck);
			break;

		    case SimulConstants.EDGE_COLOR_CHANGE :
			handleEdgeColorChangeAck(simAck);
			break;
		    
		    case SimulConstants.NEXT_PULSE :
			handleNextPulse((NextPulseAck)simAck);
			break; 

		    case SimulConstants.ALGORITHM_END :
			handleAlgorithmEnd((AlgorithmEndAck)simAck);
			break; 
		    }
		}
	    }
	    catch(InterruptedException e){
		// this interruption should have been caused by the
		// simulation abort.  
		// e.printStackTrace();
	    }
	}
	
	
	public void handleNodePropertyChangeAck(SimulAck sa)throws InterruptedException{
	    Object lock = evtObjectTmp.remove(sa.number());
	    synchronized(lock){
		/**
		 * normalement il n' y a qu'un seul thread (le noeud
		 * qui fait le changement) qui serait bloquée sur le
		 * lock.
		 **/
		lock.notifyAll();
	    }
	    numGen.free(sa.number().longValue());
	}
	
	public void handleEdgeStateChangeAck(SimulAck sa)throws InterruptedException{
	    Object lock = evtObjectTmp.remove(sa.number());
	    synchronized(lock){
		/**
		 * normalement il n' y a qu'un seul thread qui serait
		 * bloquee sur le lock.
		 **/
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

	public void handleNextPulse(NextPulseAck npa) {
	    nextAckPulse();
	    numGen.free(npa.number().longValue());
	    //System.out.println("Ack 1");
	}

	public void handleAlgorithmEnd(AlgorithmEndAck aea) {
	    numGen.free(aea.number().longValue());
	    aborted = true;
	    
	    try{
		ackPipe.notifyAllGet();
	    } catch(Exception e) {
		e.printStackTrace();
	    }
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
