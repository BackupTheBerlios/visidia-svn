package visidia.simulation;

import visidia.misc.*;
//import java.io.Serializable;

/**
 * Cette represente l'évènement associe a l'envoie d'un message sur le 
 * réseaux.
 */
public class MessageSendingEvent implements SimulEvent {
    private long evtNum;
    private String msg;
    protected Integer srcId = null;
    protected Integer destId = null;
    protected Message message;
 
    /**
     * construit un évènement associe à l'envoi du 
     * packet <i>mesgPacket</i> sur le réseau de simulation.
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
     * donne le numero de l'évènement.
     */
    public Long eventNumber(){
	return new Long(evtNum);
    }
   
    /**
     * donne le type de l'évènement.
     */
    public int type(){
	return SimulConstants.MESSAGE_SENT;
    }


    /**
     * retourme le message envoyé
     */
    public Message message(){
	return message;
    }
}




