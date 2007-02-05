package visidia.simulation;

import visidia.misc.*;
import visidia.tools.*;
import java.util.*;

import  visidia.simulation.synchro.synObj.*;
import  visidia.simulation.synchro.synAlgos.*;

public abstract class Algorithm implements Runnable,Cloneable
{
    protected final static StartSignal beginExec = new StartSignal();
    protected Integer nodeId = null ;
    protected Simulator sim ; 
    protected Object pauseLock = new Object();

    /* for local synchronization */
    // Notice that if synal is null, that instance is 
    // an instance of a synchronization algorithm
    //PFA2003
    protected AbSynAlgo synal = null;
    protected SynObject synob;


    public Algorithm(){
    }


    public Collection getListTypes(){
	Collection typesList = new LinkedList();
	typesList.add(MessageType.defaultMessageType);
	return typesList;
    }

    /**
     * Permet d'envoyer le message "msg" sur la porte "door".
     */
    protected boolean sendTo(Integer door, Message msg)
    {
	sim.runningControl();
	boolean b;
	try{
	    b= sim.sendTo(nodeId, door.intValue(), msg) ;	
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}
	return b;
    }

    /**
     * Permet d'envoyer le message "msg" sur la porte "door".
     */
    protected boolean sendTo(int door, Message msg)
    {
	sim.runningControl();
	boolean b;
	try{
	    b= sim.sendTo(nodeId, door, msg) ;	
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}
	return b;
    }

    
    protected boolean sendToNext(Message msg) {
	sim.runningControl();
	boolean b;
	try{
	    b=sim.sendToNext(nodeId, msg) ;	
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}
	return b;
    }

    
    /**
     * Envoie le message "msg" a tous les voisins de ce noeud.
     */
    protected void sendAll(Message msg)
    {
	int arite = getArity() ;
	for( int i=0; i < arite ; i++)
	    sendTo(i, msg );
    }
    
    /**
     * Retourne le premier message recu de la porte "door".Tant que le noeud 
     * ne recoit pas de message sur cette porte, il est bloque.
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
	
	return msg;
    }

    protected Message receiveFrom(Integer door)
    {
	return receiveFrom(door.intValue());
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

	return msg;
    }
    

    protected  Message receiveFrom(int door,MessageCriterion mc)
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

	return msg;
    }  

    /**
     * Retourne le premier message de la file d'attente de ce noeud.
     *si la file est vide,le noeud est endormi jusqu'a reception d'un message.
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

	return msg;
    }

    /**
     * return true if the node has received a message
     **/
    protected boolean anyMsg()
    {
    	sim.runningControl();
	boolean msg = false;
    	try{
	    msg = sim.emptyVQueue(nodeId, null);
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}
	
	return !msg;
    }


    /**
     * return true if the node has received from the specified door.
     **/
    protected boolean anyMsgDoor(int door)
    {
    	sim.runningControl();

	boolean msg = false;
    	try{
	    msg = sim.emptyVQueue(nodeId , new DoorCriterion(door));
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}
	
	return !msg;
    }

    /**
     * Retourne le degre du sommet.
     */

    protected final int getArity()
    {
    	sim.runningControl();
	return sim.getArity(nodeId);
    }

    /**
     * Returns a random connected door
     */
    //PFA 2003
    protected final int getRandomConnectedDoor() {
	Random r = new Random();
	int value = 0;
	for (int k = 0; k < 5; k++) {
	    
	    value = Math.abs(r.nextInt() % getArity());
	    
	}
	
	return value;
    }

    
    protected final int nextDoor() {
	Integer next = sim.getGraph().vertex(getId()).getNext();
	return sim.getGraph().vertex(getId()).indexOf(next);

    }

    protected final int previousDoor() {
	Integer previous = sim.getGraph().vertex(getId()).getPrevious();
	return sim.getGraph().vertex(getId()).indexOf(previous);
    }

    //PFA2003
    public final void copy(Algorithm a){
	if(a.synob() != null)
	    setSynob((SynObject) a.synob().clone());
	if(a.synal() != null){
	    AbSynAlgo syn = (AbSynAlgo) a.synal().clone();
	    syn.setSynob((SynObject) synob());
	    setSynal( syn );
	   
	}
    }
    /**
     * Necessery to use shared synchronization algorithms.
     *@param synob the name of the SynObject.
     *@param synal the name of a synchronisation algorithm already defined.
     *it loads synObject and synal given in parametes and prepares their use in the current algorithm. 
     */
    public final void  setSynchronisation(String synal_name, String synob_name)
    {
	SynObject so;
	AbSynAlgo a;
	try {	
	    so =  (SynObject)Class.forName("visidia.simulation.synchro.synObj."+synob_name).newInstance();
	    a = (AbSynAlgo)Class.forName("visidia.simulation.synchro.synAlgos."+synal_name).newInstance();
	    setSynob(so);
	    setSynal(a);
	    a.set(this);
	    synob.init(getArity());
	}
    
	catch(Exception excpt) {
	    System.out.println("Problem:Algorithm de synchronisation non trouve: " + excpt);
	}
   
    }

    /**
     * this method permites to an algorithm (especially
     * synchronization) to copy the identity of the original
     * algorithm.
     */
    public void set(Algorithm a){
	synob = a.synob();
	setSimulator(a.getSimulator());
	setId(a.getId());
    }
   

    /**
     * Permet de relier le noeud au simulateur. "s" est defini comme
     * simulateur du noeud.
     */
    final void setSimulator(Simulator s){
	sim = s;
    }

    /**
     * Permet de changer l'identite du noeud, en cas de rv_enumerotation.
     */
    final void setId(Integer id){
	nodeId = new Integer(id.intValue());
    }

    /**
     * retourne l'identite du noeud.
     */
    protected final Integer getId(){
    	sim.runningControl();
	return nodeId;
    }


    /**
     * Algorithme a executer par ce noeud. L'utilisateur doit implementer cette
     * methode s'il desire ecrire un algorithme sans passer par les regles
     * de réécriture. 
     */
    public abstract void init();


    /**
     * Methode faisant appel a <code>init()</code>. 
     * Cette methode est necessaire lors de l'instanciation 
     * du thread,un thread doit posseder la methode <code>run()</code>.
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

    protected final void setDoorState(EdgeState st,int door){
    	sim.runningControl();
    	try{
	    sim.changeEdgeState(getId(),door,st);
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}
    }

    protected final int getNetSize(){
    	sim.runningControl();
	return sim.sizeOfTheGraph();
    }

    /** 
     * This method changes the color of an edge.
     * Warning: You must include the java.awt.Color in your Algorithm
     * @param door The number of the door
     * @param color A new color for the edge: <i>new ColorState(Color)</i>
     **/
    protected final void setEdgeColor(int door, EdgeColor color){
	sim.runningControl();
	try{
	    sim.changeEdgeColor(getId(),door,color);
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}
    }

    /*
    // Permet de retourner le vertex voisin de la porte
    Vertex voisin = sim.getGraph().vertex(getId()).neighbour(door);
	
    // Retourne l'arete(de type Edge) entre la porte et le vertex voisin
    edge = voisin.edge(sim.getGraph().vertex(getId()).identity());
    // Retourne l'objet representant l'arete
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
    }
    
    /**
     * Returns the property value identified by <code>key</code>. If no property identified
     * by <code>key</code> exits, it <code>returns</code> null.
     */
    protected Object getProperty(String key){
	return sim.getNodeProperty(getId().intValue(),key);
    }

    public Simulator getSimulator(){
	sim.runningControl();
	return sim;
    }
    public SynObject getSynob(){
	sim.runningControl();
	return synob;
    }
    public AbSynAlgo getSynal(){
	sim.runningControl();
	return synal;
    }
    public SynObject synob(){
	if(synob != null)
	    return synob;
	return null;
    }
    public AbSynAlgo synal(){
	if(synal != null)
	    return synal;
	return null;
    }

    public void setSynob(SynObject s){
	synob = s;
    }
    public void setSynal(AbSynAlgo s){
        synal = s;
    }

    /**
     * Wakeup the thread if it was waiting to be connected 
     * to one neighbour at least.
     * Can be called also if the vertex is not waiting.
     */
    //PFA2003
    public final void unlockPause() {
	if (synal != null) {
	    synchronized(synal.pauseLock) {
		synal.unlockPause();
	    }
	} else {  // synal == null
	    // if there is no synal, that instance of AlgorithmDist
	    // is an synchronisation algorithm, so we apply the notify method 
	    synchronized(pauseLock) {
		pauseLock.notifyAll();
	    }
	}
    }

    /**
     * Returns a string description of the algorithm
     * Returns an empty string by default.
     */
    //PFA2003
    public String getDescription() {
	return "";
    }
    
    /**
     * True if the thread is currently running.
     * Returns true by default
     */
    //PFA2003
    public boolean isRunning() {
	return true; 
    }
}
