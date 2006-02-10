package visidia.simulation;

import visidia.misc.*;
import java.io.Serializable;

/**
 * Cette represente l'evenement associe a l'envoie d'un message sur le 
 * reseaux.
 */
public class MessageSendingEvent implements SimulEvent {
    private long evtNum;
    private String msg;
    protected Integer srcId = null;
    protected Integer destId = null;
    protected Message message;
 
    /**
     * construit un evenement associe a l'envoye du 
     * packet <i>mesgPacket</i> sur le reseau de simulation.
     */
    public MessageSendingEvent(Long eventNumber, Message message, Integer senderId, Integer receiverId){
	srcId = new Integer(senderId.intValue());
	destId = new Integer( receiverId.intValue());
	evtNum = eventNumber.longValue();
	//this.msg = message.toString();
	this.message = message;
    }
    
    public Integer sender(){
	return srcId;
    }
 
    public Integer receiver(){
	return destId;
    }

    /**
     * donne le numero de l'evenement.
     */
    public Long eventNumber(){
	return new Long(evtNum);
    }
   
    /**
     * donne le type de l'evenement.
     */
    public int type(){
	return SimulConstants.MESSAGE_SENT;
    }


    /**
     * retourme le message envoye
     */
    public Message message(){
	return message;
    }
}




