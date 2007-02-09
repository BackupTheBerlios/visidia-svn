package visidia.simulation;



import visidia.misc.Message;
import visidia.misc.MessageCriterion;
import visidia.tools.CompoundCriterion;


/**
 * this class allows to handle automatically a Lamport clock.  Each
 * time a node sends a message or a node changes its properties the
 * lamport clock is increased by one.  When receiving a message the
 * lamort clock is set to be the (maximum of the clock value of the
 * received message and the clock of the current node) + 1 (the one
 * who is receiving).  Note that, this can be handled by hand using
 * the setClock and getClock of the Message class.
 * the method description can be found in the Algorithm class.
 */


public abstract class LamportAlgorithm extends Algorithm implements Runnable,Cloneable
{
    int lamportClock = 0;
 
    public LamportAlgorithm(){
	super();
    }


    /**
     * Permet d'envoyer le message "msg" sur la porte "door".
     */
    protected final boolean sendTo(int door, Message msg)
    {
	this.sim.runningControl();
	boolean b;
	this.lamportClock++;
	msg.setMsgClock(this.lamportClock);
	try{
	    b= this.sim.sendTo(this.nodeId, door, msg) ;	
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}
	return b;
    }

    
    protected final boolean sendToNext(Message msg) {
	this.sim.runningControl();
	this.lamportClock++;
	boolean b;
	msg.setMsgClock(this.lamportClock);
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
    protected final void sendAll(Message msg)
    {
	int arite = this.getArity() ;
	for( int i=0; i < arite ; i++)
	    this.sendTo(i, msg );
    }
    
    /**
     * Retourne le premier message recu de la porte "door".Tant que le noeud 
     * ne recoit pas de message sur cette porte, il est bloque.
     */
    protected final Message receiveFrom(int door)
    {
    	this.sim.runningControl();

	Message msg = null;
    	try{
	    msg =  this.sim.getNextMessage( this.nodeId ,null, new DoorCriterion(door));
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}
	
	int stamp = msg.getMsgClock();
	this.lamportClock = Math.max(stamp,this.lamportClock) + 1;
	return msg;
    }

    
    protected final Message receiveFromPrevious() {
	this.sim.runningControl();
	Message msg = null;
    	try{
	    msg = this.sim.getNextMessageFromPrevious(this.nodeId , null);
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}

	int stamp = msg.getMsgClock();
	this.lamportClock = Math.max(stamp,this.lamportClock) + 1;
	return msg;
    }


    protected final Message receiveFrom(int door,MessageCriterion mc)
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

	int stamp = msg.getMsgClock();
	this.lamportClock = Math.max(stamp,this.lamportClock) + 1;
	return msg;
    }  

    protected final Message receive(Door door)
    {
    	this.sim.runningControl();
	Message msg = null;
    	try{
	    msg = this.sim.getNextMessage(this.nodeId, door, null);
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}

	int stamp = msg.getMsgClock();
	this.lamportClock = Math.max(stamp,this.lamportClock) + 1;
	return msg;
    }

    protected final Message receive(Door door, MessageCriterion mc)
    {
    	this.sim.runningControl();
	Message msg = null;
    	try{
	    msg = this.sim.getNextMessage(this.nodeId, door, new MessagePacketCriterion(mc));
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}

	int stamp = msg.getMsgClock();
	this.lamportClock = Math.max(stamp,this.lamportClock) + 1;
	return msg;
    }

    /**
     * Sets this node property. If <code>value</code> is null the property is removed.
     */
    protected void putProperty(String key, Object value){
	super.putProperty(key,value);
	this.lamportClock++;
    }

    
    /**
     * instructions to be executed by the algorithm. The user must
     * implement this method to run his algorithm
     */
    
    public abstract void init(); 


    public abstract Object clone();
    
    

    /**
     * return the value of the lamport clock
     */
    protected int getLamportClock(){
	return this.lamportClock;
    }
}
