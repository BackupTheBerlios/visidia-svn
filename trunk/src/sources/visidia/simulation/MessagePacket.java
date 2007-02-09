package visidia.simulation;


import java.io.Serializable;

import visidia.misc.Message;

/**
 * cette classe permet d'encapsuler un message qui est
 * en transite sur le réseaux.
 */
public class MessagePacket implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1392322886326529909L;
	protected Integer srcId = null;
    protected Integer destId = null;
    protected Message mesg = null;
    protected int srcDoor = -1;
    protected int destDoor =  -1;


    /**
     * construit un paquet cotenant un message envoyé par le noeud
     * identifié par <i>senderId</i> au noeud identifié par <i>receiverId</i>.
     */
    public MessagePacket(Integer senderId, int srcDoor, Integer receiverId, int destDoor, Message msg){
	this.srcId = new Integer(senderId.intValue());
	this.destId = new Integer( receiverId.intValue());
	this.mesg = msg;
	this.srcDoor = srcDoor;
	this.destDoor = destDoor;
    }
     public MessagePacket(Integer senderId, int srcDoor, Integer receiverId, Message msg){
	this.srcId = new Integer(senderId.intValue());
	this.destId = new Integer( receiverId.intValue());
	this.mesg = msg;
	this.srcDoor = srcDoor;
    }
    
    public Integer sender(){
	return this.srcId;
    }

    public int senderDoor(){
	return this.srcDoor;
    }

    public Integer receiver(){
	return this.destId;
    }

    public int receiverDoor(){
	return this.destDoor;
    }

    public Message message(){
	return this.mesg;
    }
    
    public void setReceiverDoor(int receiverDoor) {
	this.destDoor = receiverDoor;
    }
}

