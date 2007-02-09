package visidia.simulation;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import visidia.graph.SimpleGraph;
import visidia.graph.Vertex;
import visidia.misc.EdgeColor;
import visidia.misc.EdgeState;
import visidia.misc.Message;
import visidia.tools.Criterion;
import visidia.tools.NumberGenerator;
import visidia.tools.VQueue;


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
	this.graph = (SimpleGraph) netGraph.clone();
	this.evtObjectTmp = new Hashtable();
	this.evtQ = evtVQueue;
	this.ackQ = ackVQueue;
	this.threadGroup = new SimulatorThreadGroup("simulator");
	this.procs = new ProcessData[this.graph.size()];
	
	Enumeration vertices = this.graph.vertices();
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
	    this.procs[vertex.identity().intValue()] = processData;
	}
    }


    public Simulator(SimpleGraph netGraph, VQueue evtVQueue, VQueue ackVQueue){
	this.graph = (SimpleGraph) netGraph.clone();
	this.evtObjectTmp = new Hashtable();
	this.evtQ = evtVQueue;
	this.ackQ = ackVQueue;
	this.threadGroup = new SimulatorThreadGroup("simulator");
	this.procs = new ProcessData[this.graph.size()];
	
	Enumeration vertices = this.graph.vertices();
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
	    this.procs[vertex.identity().intValue()] = processData;
	}
    }


    public VQueue evtVQueue(){
	return this.evtQ;
    }

    public VQueue ackVQueue(){
	return this.ackQ;
    }
    
    public SimpleGraph getGraph() {
	return this.graph;
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
	 synchronized(this.lockSync) {
	     try{
		 this.lockSync.notifyAll();
	     } catch (Exception e) {
		 e.printStackTrace();
	     }
	 }
     }

     public void nextPulse() {
	 try{
	     synchronized(this.lockSync){
		 this.countNextPulse=(this.countNextPulse+1)%(this.graph.size()-this.terminatedThreadCount);
		 if(this.countNextPulse == 0) {
		     try{
			 this.pushNextPulseEvent();
		     } catch (Exception e) {
			 // aucune exception n'est normalement levée ici
			 System.out.println(" bug ref 0 Simulator Synchrone");
		     }
		     this.pulse ++;
		 }
		 this.lockSync.wait();
	     }
	} catch (Exception e) {
	    System.out.println(" bug ref 0.1 : Simulator Synchrone");
	    e.printStackTrace();
	}
    }

    
    public int getPulse() {
	return this.pulse ;
    }

    

    /**
     * met le message dans la file d'attente du voisin de "srcId"
     * correspondant à la porte "door".
     */
    boolean sendTo(Integer senderId, int door, Message msg)throws InterruptedException{	

	Integer receiverId = this.graph.vertex(senderId).neighbour(door).identity();
	
	Vertex receiverVertex = this.graph.vertex(receiverId);
	int receiveDoor = receiverVertex.indexOf(senderId);
	

	MessagePacket msgPacket = new MessagePacket(senderId, door, 
						    receiverId, receiveDoor, msg);
	
	msg.setVisualization((this.getDraw(senderId.intValue())
			      ||this.getDraw(receiverId.intValue())));
	this.pushMessageSendingEvent(msgPacket); 
	return true;

    }

    boolean sendToNext(Integer senderId, Message msg) throws InterruptedException{
	
	Integer receiverId = this.graph.vertex(senderId).getNext();
	if(receiverId != null) 
	    {
		Vertex senderVertex = this.graph.vertex(senderId);
		int senderDoor = senderVertex.indexOf(receiverId);
		return this.sendTo(senderId, senderDoor, msg);
	    }
	return false;
    }
	
	
    Message getNextMessage(Integer nodeId, Door door, Criterion c)throws InterruptedException{
	ProcessData procData = this.procs[nodeId.intValue()];

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

	ProcessData procData = this.procs[nodeId.intValue()];

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

	ProcessData procData = this.procs[nodeId.intValue()];

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
	ProcessData procData = this.procs[nodeId.intValue()];
	procData.msgVQueue = new VQueue();
    }

    
    boolean emptyVQueue(Integer nodeId, Criterion c) throws InterruptedException{
	ProcessData procData = this.procs[nodeId.intValue()];
	
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
	Integer previous = this.graph.vertex(nodeId).getPrevious();
	if(previous != null) 
	    {
		Door previousDoor = new Door(previous.intValue());
		return this.getNextMessage(nodeId, previousDoor, c);
	    }
	else return null;
    }

    private void pushNodePropertyChangeEvent(Integer nodeId, Object key, Object value)throws InterruptedException{
	Long num = new Long(this.numGen.alloc());
	Object lock = new Object();
	this.evtObjectTmp.put(num,lock);	
	NodePropertyChangeEvent npce = new NodePropertyChangeEvent(num,nodeId,key,value);
	synchronized(lock){
	    this.evtQ.put(npce);
	    lock.wait();
	}
    }
	
    private void pushEdgeStateChangeEvent(Integer nodeId1, Integer nodeId2, EdgeState es)throws InterruptedException{
	Long key = new Long(this.numGen.alloc());
	Object lock = new Object();
	this.evtObjectTmp.put(key,lock);	
	EdgeStateChangeEvent esce = new EdgeStateChangeEvent(key,nodeId1, nodeId2,es);
	synchronized(lock){
	    this.evtQ.put(esce);
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
	Long key = new Long(this.numGen.alloc());
	Object lock = new Object();
	this.evtObjectTmp.put(key,lock);
	EdgeColorChangeEvent ecce = new EdgeColorChangeEvent(key,nodeId1,nodeId2,ec);
	synchronized(lock){
	    this.evtQ.put(ecce);
	    lock.wait();
	}
    }

    private void pushMessageSendingEvent(MessagePacket mesgPacket) throws InterruptedException{

	Long key = new Long(this.numGen.alloc());
	this.evtObjectTmp.put(key,mesgPacket);
	MessageSendingEvent mse = new MessageSendingEvent(key,mesgPacket.message(),mesgPacket.sender(), mesgPacket.receiver());
	this.evtQ.put(mse);
	
    }


    private void pushNextPulseEvent() throws InterruptedException{
	Long key = new Long(this.numGen.alloc());
	NextPulseEvent npe = new NextPulseEvent(key,this.pulse);
	this.evtQ.put(npe);
    }
  
    

    /**
     * retourne le degrès du noeud <i>id</id> .
     */
    public int getArity(Integer id){
	Vertex vertex = this.graph.vertex(id);
	return vertex.degree();
    }

    /**
     */
    public void changeEdgeState(Integer id, int door, EdgeState newEdgeState)throws InterruptedException{
	Integer neighbId = this.graph.vertex(id).neighbour(door).identity();
	this.pushEdgeStateChangeEvent(id,neighbId,newEdgeState);
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
	Integer neighbId = this.graph.vertex(id).neighbour(door).identity();
	this.pushEdgeColorChangeEvent(id,neighbId,newEdgeColor);
    }

    public int sizeOfTheGraph(){
	return this.graph.size();
    }


    /**
     * instancie les thread à partir du graphe et de l'algorithme
     * donne en parametre et lance l'execution. Pour le bon
     * fonctionnement du système les deux files d'attentes passées en
     * parametre doivent être vide, mais ceci n'est pas une condition
     * nécessaire.
     */
    public void startSimulation(){
	if( this.started ){
	    return;
	    //	    stopSimulation();
	}
	
	//initialise algorithm objects
	int graphSize = this.graph.size();
	for(int id = 0; id < graphSize; id++){
	    Algorithm a = this.procs[id].algo;
	    a.setId(new Integer(id)); 
	    Thread currThread = new Thread(this.threadGroup,a);
	    currThread.setPriority(THREAD_PRIORITY);
	    this.procs[id].processThread = currThread;
	    a.setSimulator(this);
	}    
	
	// start acknowledge handler
	this.ackHandler = new AckHandler(this.ackQ);
	this.ackHandler.setPriority(9);
	this.ackHandler.start();

	// start node threads
	for(int id = 0; id < graphSize; id++){
	    this.procs[id].processThread.start();
	}    


	// temps mis par le simulateur pour demarrer la visualisation
	// pour tout les sommets.  a revoir ...
	try{
	    Thread.sleep(1000);
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
	this.started = true;
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
	 
	this.aborted = true;
	 
	/**
	 * ne fonctionne pas vraiment bien l'arrêt des thread, à
	 * revoir
	 **/
	
	Enumeration listVertex = this.graph.vertices();
	while(listVertex.hasMoreElements() ){
 	    Vertex currentVertex = (Vertex) listVertex.nextElement() ; 
 	    ((ProcessData)currentVertex.getData()).algo.abort();
 	}
	
	System.out.print("   Threads notified");
	try{
	    
	    synchronized(this.lockSync) {
		System.out.print(" .");
		this.lockSync.notifyAll();
	    }
	    System.out.print(".. ");
	    
	} catch (Exception e) {
	    // e.printStackTrace();
	}
	
	try{
	    this.evtQ.notifyAllGet();
	    this.ackQ.notifyAllGet();
	} catch(Exception e) {
	    // e.printStackTrace();
	}
	
	System.out.print(" waiting for threads .");
 	while(this.threadGroup.activeCount() > 0){
 	    this.threadGroup.interrupt();
 	    try{
 	    	Thread.sleep(50);
 	    }
 	    catch(InterruptedException e){
		//e.printStackTrace();
 	    }
 	}

	System.out.print(" .. terminated.  ");
	System.out.println(" Stopping the handler ");
	if(this.ackHandler != null){
	    while(this.ackHandler.isAlive()){
		this.ackHandler.interrupt();
		try{
		    Thread.sleep(50);
		}
		catch(InterruptedException e){
		    //e.printStackTrace();
		}
	    }
	}
	// System.out.println("thread count after abort = "+Thread.activeCount());
    }
    

    public boolean containsAliveThreads(){
	if(this.threadGroup.activeCount() > 0){
	    return true;
	}
	if(this.ackHandler != null){
	    return this.ackHandler.isAlive();
	}
	return false;
    }

    /**
     */
    public void wedge(){
	if(!this.paused){
	    this.paused = true;
	}
    }
    
    public void unWedge(){
	if(this.paused){
	    synchronized(this.pauseLock){
		this.paused = false;
		this.pauseLock.notifyAll();
	    }
	}
    }
    	

    void runningControl(){
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
	Hashtable props = this.procs[id].props;
	synchronized(props){
	    return (Hashtable) props.clone();
	}
    }

    Object getNodeProperty(int id, Object key){
	synchronized(this.procs[id].props){
	    return this.procs[id].props.get(key);
	}
    }		

    void putNodeProperty(int id, Object key, Object value) throws InterruptedException{
	synchronized(this.procs[id].props){
	    this.procs[id].props.put(key, value);
	    this.pushNodePropertyChangeEvent(new Integer(id), key, value);
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
	synchronized(this.lockSync){
	    //if (countNextPulse !=0) {
	    int i = (this.countNextPulse+1)%(this.graph.size()-this.terminatedThreadCount);
	    
	    if((i == 0) && (this.terminatedThreadCount != this.graph.size()-1)) {
		try{
		    this.pushNextPulseEvent();
		} catch (Exception e) {
		    // aucune exception n'est normalement levée ici
		    System.out.println(" bug ref 0.3 Simulator Synchrone");
		}
		this.pulse ++;
	    }	   
	    //}
	    this.terminatedThreadCount++;
	}

	
	if(this.terminatedThreadCount == this.graph.size()){
	    // sends algorithms end notification.
	    this.evtQ.put(new AlgorithmEndEvent(this.numGen.alloc()));
	    System.out.println("Algorithm Terminated");
	    
	    
	    //kill the acknowledge handler thread
	    while(this.ackHandler.isAlive()){
		try{
		    Thread.sleep(50);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		this.aborted = true;
		this.ackHandler.interrupt();
		
		try{
		    this.evtQ.notifyAllGet();
		    this.ackQ.notifyAllGet();
		} catch(Exception e) {
		    e.printStackTrace();
		}
		
		
		try{
		    Thread.sleep(50);
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
	    this.ackPipe = q;
	}
	
	public void run(){
	    try{
		SimulAck simAck = null;
		while(! Simulator.this.aborted){
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
			
		    case SimulConstants.EDGE_STATE_CHANGE :
			this.handleEdgeStateChangeAck(simAck);
			break;
			
		    case SimulConstants.MESSAGE_SENT :
			//System.out.println("Message");
			this.handleMessageSentAck((MessageSendingAck)simAck);
			break;

		    case SimulConstants.EDGE_COLOR_CHANGE :
			this.handleEdgeColorChangeAck(simAck);
			break;
		    
		    case SimulConstants.NEXT_PULSE :
			this.handleNextPulse((NextPulseAck)simAck);
			break; 

		    case SimulConstants.ALGORITHM_END :
			this.handleAlgorithmEnd((AlgorithmEndAck)simAck);
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
	    Object lock = Simulator.this.evtObjectTmp.remove(sa.number());
	    synchronized(lock){
		/**
		 * normalement il n' y a qu'un seul thread (le noeud
		 * qui fait le changement) qui serait bloquée sur le
		 * lock.
		 **/
		lock.notifyAll();
	    }
	    Simulator.this.numGen.free(sa.number().longValue());
	}
	
	public void handleEdgeStateChangeAck(SimulAck sa)throws InterruptedException{
	    Object lock = Simulator.this.evtObjectTmp.remove(sa.number());
	    synchronized(lock){
		/**
		 * normalement il n' y a qu'un seul thread qui serait
		 * bloquee sur le lock.
		 **/
		lock.notifyAll();
	    }
	    Simulator.this.numGen.free(sa.number().longValue());
	}

	public void handleEdgeColorChangeAck(SimulAck sa)throws InterruptedException{
	    Object lock =Simulator.this.evtObjectTmp.remove(sa.number());
	    synchronized(lock){
		lock.notifyAll();
	    }
	    Simulator.this.numGen.free(sa.number().longValue());
	}
	
	public void handleMessageSentAck_old(MessageSendingAck msa)throws InterruptedException{
	    MessagePacket msgPacket = (MessagePacket) Simulator.this.evtObjectTmp.remove(msa.number());
	    Vertex receiverVertex = Simulator.this.graph.vertex(msgPacket.receiver());
	    ((ProcessData)receiverVertex.getData()).msgVQueue.put(msgPacket);
	    Simulator.this.numGen.free(msa.number().longValue());
	}
	
	public void handleMessageSentAck(MessageSendingAck msa)throws InterruptedException{
	    MessagePacket msgPacket = (MessagePacket) Simulator.this.evtObjectTmp.remove(msa.number());
	    Simulator.this.procs[msgPacket.receiver().intValue()].msgVQueue.put(msgPacket);
	    Simulator.this.numGen.free(msa.number().longValue());
	}

	public void handleNextPulse(NextPulseAck npa) {
	    Simulator.this.nextAckPulse();
	    Simulator.this.numGen.free(npa.number().longValue());
	    //System.out.println("Ack 1");
	}

	public void handleAlgorithmEnd(AlgorithmEndAck aea) {
	    Simulator.this.numGen.free(aea.number().longValue());
	    Simulator.this.aborted = true;
	    
	    try{
		this.ackPipe.notifyAllGet();
	    } catch(Exception e) {
		e.printStackTrace();
	    }
	}
	
    }
    
    public void restartNode(int nodeId) {
        if(this.started) {
            // restart failure detector
	    // procs[nodeId].failureDetector.restartDetection();
            // restart algorithm
            this.procs[nodeId].processThread.stop();
            Thread currThread = new Thread(this.threadGroup, this.procs[nodeId].algo);
            currThread.setPriority(THREAD_PRIORITY);
            this.procs[nodeId].processThread = currThread;
            this.procs[nodeId].processThread.start();
        }
    }
    
    /**
     * update node properties
     */
    public void setNodeProperties(int nodeId, Hashtable properties) {
	if (this.started) 
	    this.procs[nodeId].props = properties;
    }
    
    public boolean getDraw(int id) {
        if(this.getNodeProperty(id, "draw messages").equals("yes"))
            return true;
        else
            return false;
    }
    
}
