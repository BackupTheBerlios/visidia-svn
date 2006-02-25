package visidia.simulation;



import visidia.misc.*;
import visidia.tools.*;
import visidia.graph.*;
import java.util.*;
import java.awt.Color;


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
	sim.runningControl();
	boolean b;
	lamportClock++;
	msg.setMsgClock(lamportClock);
	try{
	    b= sim.sendTo(nodeId, door, msg) ;	
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}
	return b;
    }

    
    protected final boolean sendToNext(Message msg) {
	sim.runningControl();
	lamportClock++;
	boolean b;
	msg.setMsgClock(lamportClock);
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
    protected final void sendAll(Message msg)
    {
	int arite = getArity() ;
	for( int i=0; i < arite ; i++)
	    sendTo(i, msg );
    }
    
    /**
     * Retourne le premier message recu de la porte "door".Tant que le noeud 
     * ne recoit pas de message sur cette porte, il est bloque.
     */
    protected final Message receiveFrom(int door)
    {
    	sim.runningControl();

	Message msg = null;
    	try{
	    msg =  sim.getNextMessage( nodeId ,null, new DoorCriterion(door));
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}
	
	int stamp = msg.getMsgClock();
	lamportClock = Math.max(stamp,lamportClock) + 1;
	return msg;
    }

    
    protected final Message receiveFromPrevious() {
	sim.runningControl();
	Message msg = null;
    	try{
	    msg = sim.getNextMessageFromPrevious(nodeId , null);
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}

	int stamp = msg.getMsgClock();
	lamportClock = Math.max(stamp,lamportClock) + 1;
	return msg;
    }


    protected final Message receiveFrom(int door,MessageCriterion mc)
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

	int stamp = msg.getMsgClock();
	lamportClock = Math.max(stamp,lamportClock) + 1;
	return msg;
    }  

    protected final Message receive(Door door)
    {
    	sim.runningControl();
	Message msg = null;
    	try{
	    msg = sim.getNextMessage(nodeId, door, null);
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}

	int stamp = msg.getMsgClock();
	lamportClock = Math.max(stamp,lamportClock) + 1;
	return msg;
    }

    protected final Message receive(Door door, MessageCriterion mc)
    {
    	sim.runningControl();
	Message msg = null;
    	try{
	    msg = sim.getNextMessage(nodeId, door, new MessagePacketCriterion(mc));
	}
	catch(InterruptedException e){
	    throw new SimulationAbortError();
	}

	int stamp = msg.getMsgClock();
	lamportClock = Math.max(stamp,lamportClock) + 1;
	return msg;
    }

    /**
     * Sets this node property. If <code>value</code> is null the property is removed.
     */
    protected void putProperty(String key, Object value){
	super.putProperty(key,value);
	lamportClock++;
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
	return lamportClock;
    }
}
