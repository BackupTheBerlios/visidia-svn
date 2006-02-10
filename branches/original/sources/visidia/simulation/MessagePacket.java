package visidia.simulation;


import java.io.Serializable;
import visidia.misc.*;

/**
 * cette classe permet d'encapsuler un message qui est
 * en transite sur le reseaux.
 */
public class MessagePacket implements Serializable {
    protected Integer srcId = null;
    protected Integer destId = null;
    protected Message mesg = null;
    protected int srcDoor = -1;
    protected int destDoor =  -1;


    /**
     * construit un paquet cotenant un message envoye par le noeud
     * identifie par <i>senderId</i> au noeud identifie par <i>receiverId</i>.
     */
    public MessagePacket(Integer senderId, int srcDoor, Integer receiverId, int destDoor, Message msg){
	srcId = new Integer(senderId.intValue());
	destId = new Integer( receiverId.intValue());
	mesg = msg;
	this.srcDoor = srcDoor;
	this.destDoor = destDoor;
    }
     public MessagePacket(Integer senderId, int srcDoor, Integer receiverId, Message msg){
	srcId = new Integer(senderId.intValue());
	destId = new Integer( receiverId.intValue());
	mesg = msg;
	this.srcDoor = srcDoor;
    }
    
    public Integer sender(){
	return srcId;
    }

    public int senderDoor(){
	return srcDoor;
    }

    public Integer receiver(){
	return destId;
    }

    public int receiverDoor(){
	return destDoor;
    }

    public Message message(){
	return mesg;
    }
    
    public void setReceiverDoor(int receiverDoor) {
	this.destDoor = receiverDoor;
    }
}

