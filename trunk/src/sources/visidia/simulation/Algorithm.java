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
	this.sim.runningControl();
	boolean b;
	try{
	    b= this.sim.sendTo(this.nodeId, door.intValue(), msg) ;	
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
	this.sim.runningControl();
	boolean b;
	try{
	    b= this.sim.sendTo(this.nodeId, door, msg) ;	
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}
	return b;
    }

    
    protected boolean sendToNext(Message msg) {
	this.sim.runningControl();
	boolean b;
	try{
	    b=this.sim.sendToNext(this.nodeId, msg) ;	
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
	int arite = this.getArity() ;
	for( int i=0; i < arite ; i++)
	    this.sendTo(i, msg );
    }
    
    /**
     * Retourne le premier message recu de la porte "door".Tant que le noeud 
     * ne recoit pas de message sur cette porte, il est bloque.
     */
    protected Message receiveFrom(int door)
    {
    	this.sim.runningControl();

	Message msg = null;
    	try{
	    msg =  this.sim.getNextMessage( this.nodeId ,null, new DoorCriterion(door));
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}
	
	return msg;
    }

    protected Message receiveFrom(Integer door)
    {
	return this.receiveFrom(door.intValue());
    }
    
    
    protected Message receiveFromPrevious() {
	this.sim.runningControl();
	Message msg = null;
    	try{
	    msg = this.sim.getNextMessageFromPrevious(this.nodeId , null);
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}

	return msg;
    }
    

    protected  Message receiveFrom(int door,MessageCriterion mc)
    {
    	this.sim.runningControl();

	DoorCriterion dc = new DoorCriterion(door);
	MessagePacketCriterion mpc = new MessagePacketCriterion(mc);
	CompoundCriterion c = new  CompoundCriterion();
	c.add(dc);
	c.add(mpc);

	Message msg = null;
    	try{
	    msg = this.sim.getNextMessage(this.nodeId , null, c);
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
    	this.sim.runningControl();
	Message msg = null;
    	try{
	    msg = this.sim.getNextMessage(this.nodeId, door, null);
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}

	return msg;
    }
    
    protected Message receive(Door door, MessageCriterion mc)
    {
    	this.sim.runningControl();
	Message msg = null;
    	try{
	    msg = this.sim.getNextMessage(this.nodeId, door, new MessagePacketCriterion(mc));
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
    	this.sim.runningControl();
	boolean msg = false;
    	try{
	    msg = this.sim.emptyVQueue(this.nodeId, null);
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
    	this.sim.runningControl();

	boolean msg = false;
    	try{
	    msg = this.sim.emptyVQueue(this.nodeId , new DoorCriterion(door));
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
    	this.sim.runningControl();
	return this.sim.getArity(this.nodeId);
    }

    /**
     * Returns a random connected door
     */
    //PFA 2003
    protected final int getRandomConnectedDoor() {
	Random r = new Random();
	int value = 0;
	for (int k = 0; k < 5; k++) {
	    
	    value = Math.abs(r.nextInt() % this.getArity());
	    
	}
	
	return value;
    }

    
    protected final int nextDoor() {
	Integer next = this.sim.getGraph().vertex(this.getId()).getNext();
	return this.sim.getGraph().vertex(this.getId()).indexOf(next);

    }

    protected final int previousDoor() {
	Integer previous = this.sim.getGraph().vertex(this.getId()).getPrevious();
	return this.sim.getGraph().vertex(this.getId()).indexOf(previous);
    }

    //PFA2003
    public final void copy(Algorithm a){
	if(a.synob() != null)
	    this.setSynob((SynObject) a.synob().clone());
	if(a.synal() != null){
	    AbSynAlgo syn = (AbSynAlgo) a.synal().clone();
	    syn.setSynob(this.synob());
	    this.setSynal( syn );
	   
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
	    this.setSynob(so);
	    this.setSynal(a);
	    a.set(this);
	    this.synob.init(this.getArity());
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
	this.synob = a.synob();
	this.setSimulator(a.getSimulator());
	this.setId(a.getId());
    }
   

    /**
     * Permet de relier le noeud au simulateur. "s" est defini comme
     * simulateur du noeud.
     */
    final void setSimulator(Simulator s){
	this.sim = s;
    }

    /**
     * Permet de changer l'identite du noeud, en cas de rv_enumerotation.
     */
    final void setId(Integer id){
	this.nodeId = new Integer(id.intValue());
    }

    /**
     * retourne l'identite du noeud.
     */
    protected final Integer getId(){
    	this.sim.runningControl();
	return this.nodeId;
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
	this.init();
	try{
	    this.sim.terminatedAlgorithm();
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}
    }

    protected final void setDoorState(EdgeState st,int door){
    	this.sim.runningControl();
    	try{
	    this.sim.changeEdgeState(this.getId(),door,st);
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}
    }

    protected final int getNetSize(){
    	this.sim.runningControl();
	return this.sim.sizeOfTheGraph();
    }

    /** 
     * This method changes the color of an edge.
     * Warning: You must include the java.awt.Color in your Algorithm
     * @param door The number of the door
     * @param color A new color for the edge: <i>new ColorState(Color)</i>
     **/
    protected final void setEdgeColor(int door, EdgeColor color){
	this.sim.runningControl();
	try{
	    this.sim.changeEdgeColor(this.getId(),door,color);
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
	    this.sim.putNodeProperty(this.getId().intValue(), key, value);
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
	return this.sim.getNodeProperty(this.getId().intValue(),key);
    }

    public Simulator getSimulator(){
	this.sim.runningControl();
	return this.sim;
    }
    public SynObject getSynob(){
	this.sim.runningControl();
	return this.synob;
    }
    public AbSynAlgo getSynal(){
	this.sim.runningControl();
	return this.synal;
    }
    public SynObject synob(){
	if(this.synob != null)
	    return this.synob;
	return null;
    }
    public AbSynAlgo synal(){
	if(this.synal != null)
	    return this.synal;
	return null;
    }

    public void setSynob(SynObject s){
	this.synob = s;
    }
    public void setSynal(AbSynAlgo s){
        this.synal = s;
    }

    /**
     * Wakeup the thread if it was waiting to be connected 
     * to one neighbour at least.
     * Can be called also if the vertex is not waiting.
     */
    //PFA2003
    public final void unlockPause() {
	if (this.synal != null) {
	    synchronized(this.synal.pauseLock) {
		this.synal.unlockPause();
	    }
	} else {  // synal == null
	    // if there is no synal, that instance of AlgorithmDist
	    // is an synchronisation algorithm, so we apply the notify method 
	    synchronized(this.pauseLock) {
		this.pauseLock.notifyAll();
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
