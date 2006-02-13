package visidia.simulation;

import visidia.network.*;
import visidia.gui.*;
import visidia.misc.Message;
import visidia.misc.*;
import visidia.tools.*;
import visidia.gui.presentation.userInterfaceSimulation.*;

import java.util.*;
import java.io.Serializable;

public abstract class AlgorithmDist implements Runnable,Cloneable,Serializable
{
    protected final static StartSignal beginExec = new StartSignal();
    protected Hashtable messageTypeTable=new Hashtable();
    private Integer nodeId = null ;
    protected NodeTry server ; 
    int lamportClock = 0;
    
    public Hashtable getListTypes(){
	//if (messageTypeTable.isEmpty())
	//this.addMessageType(MessageType.defaultMessageType);
	return messageTypeTable;
    }
    
    public MessageType getType(MessageType type) {
	return (MessageType)messageTypeTable.get(type);
    }
    
    protected void addMessageType(MessageType mesgType) {
	messageTypeTable.put(mesgType.getType(),mesgType);
    }
    
    public void setMessageType(MessageType msgType, boolean state) {
	((MessageType) messageTypeTable.get(msgType.getType())).setToPaint(state);
    }

    /**
     * Permet d'envoyer le message "msg" sur la porte "door".
     */
    protected void sendTo(int door, Message msg){
	msg.setType((MessageType)messageTypeTable.get((msg.getType()).getType()));
	server.runningControl();
	lamportClock++;
	msg.setLamportClockStamp(lamportClock);
    	
	try{
	    server.sendTo(nodeId, door, msg) ;	
	} catch(Exception e){
	    e.printStackTrace();
	    throw new SimulationAbortError();
	}
    } 

    /**
     * Envoie le message "msg" a tous les voisins de ce noeud.
     */
    protected void sendAll(Message msg){
	int arite = getArity() ;
	for( int i=0; i < arite ; i++)
	    sendTo(i, msg );
    }
    
    /**
     * Retourne le premier message recu de la porte "door".Tant que le noeud 
     * ne recoit pas de message sur cette porte,il est bloque.
     */
    protected Message receiveFrom(int door){
	server.runningControl();
    	Message msg = null;
    	try{
	    msg =  server.getNextMessage( nodeId ,null, new DoorCriterion(door));
	}catch(Exception e){
	    throw new SimulationAbortError();
	}
	int stamp = msg.getLamportClockStamp();
	lamportClock = Math.max(stamp,lamportClock) + 1;
	return msg;
    }


    protected Message receiveFrom(int door,MessageCriterion mc){
	server.runningControl();
	DoorCriterion dc = new DoorCriterion(door);
	MessagePacketCriterion mpc = new MessagePacketCriterion(mc);
	CompoundCriterion c = new  CompoundCriterion();
	c.add(dc);
	c.add(mpc);

	Message msg = null;
    	try{
	    msg = server.getNextMessage(nodeId , null, c);
	} catch(Exception e){
	    throw new SimulationAbortError();
	}
	int stamp = msg.getLamportClockStamp();
	lamportClock = Math.max(stamp,lamportClock) + 1;
	return msg;
    }  

    /**
     * Retourne le premier message de la file d'attente de ce noeud.
     *si la file est vide,le noeud est endormi jusqu'a reception d'un message.
     */
    protected Message receive(Door door){
	server.runningControl();
	Message msg = null;
    	try{
	    msg = server.getNextMessage(nodeId, door, null);
	}
	catch(Exception e){
	    throw new SimulationAbortError();
	}
	int stamp = msg.getLamportClockStamp();
	lamportClock = Math.max(stamp,lamportClock) + 1;
	return msg;
    }

    protected Message receive(Door door, MessageCriterion mc){
	server.runningControl();
	Message msg = null;
    	try{
	    msg = server.getNextMessage(nodeId, door, new MessagePacketCriterion(mc));
	}
	catch(Exception e){
	    throw new SimulationAbortError();
	}
	int stamp = msg.getLamportClockStamp();
	lamportClock = Math.max(stamp,lamportClock) + 1;
	return msg;
    }

    /**
     * Retourne le degre du sommet.
     */
    protected int getArity(){
	server.runningControl();
	return server.getArity();
    }

    /**
     * Permet de relier le noeud a son serveur de noeuds.
     */
    public void setServer(NodeTry s){
	server = s;
    }

    /**
     * Permet de changer l'identite du noeud, en cas de rv_enumerotation.
     */
    public void setId(Integer id){
	nodeId = new Integer(id.intValue());
    }

    /**
     * retourne l'identite du noeud.
     */
    protected Integer getId(){
	server.runningControl();
	return nodeId;
    }


    /**
     * Algorithme a executer par ce noeud. L'utilisateur doit implementer cette
     * methode si il desire ecrire un algorithme sans passer par les regles
     * de reecriture. 
     */
    public abstract void init(); //algorithme de l utilisateur.


    /**
     * Methode faisant appel a <code>init()</code>. 
     * Cette methode est necessaire lors de l'instanciation 
     * du thread,un thread doit posseder la methode <code>run()</code>.
     */
    public final void run(){
	init();
	try {
	    server.terminatedAlgorithm();
	}catch(Exception e){
	    throw new SimulationAbortError();
	}
    }


    protected void setDoorState(EdgeState st,int door){
	server.runningControl();
	try{
	    server.changeEdgeState(nodeId,door,st);
	} catch (Exception ie){
	    throw new SimulationAbortError();
	}
    }
	
    protected int getNetSize(){
	server.runningControl();
	return server.sizeOfTheGraph();
    }

    /**
     * This method is call to message the current algorithm to
     * stop running. It should be overrided by subclasses. 
     */
    public void abort(){
    }

    public abstract Object clone();
    
    /**
     * Sets this node property. If <code>value</code> is null the property is removed.
     */
    protected void putProperty(String key, Object value){
	try{
	    server.putNodeProperty(nodeId, key, value);
	} catch (Exception e){
	    throw new SimulationAbortError();
	}
	lamportClock++;
    }
    
    /**
     * Returns the property value identified by <code>key</code>. If no property identified
     * by <code>key</code> exits, it <code>returns</code> null.
     */
    protected Object getProperty(String key){
	Object obj = new Object();
	try {
	    obj = server.getNodeProperty(getId().intValue(),key);
	} catch (Exception e) {
	    throw new SimulationAbortError();
	}
	return obj;
    }
 

    protected int getLamportClock(){
	return lamportClock;
    }
    
    

    protected void incrementSynchMessages() {
	server.incrementSynchMessages();
    }
    
    protected void incrementSynch() {
	server.incrementSynch();
    }

    protected void incrementLabelMessages(){
	server.incrementLabelMessages();
    }
}
