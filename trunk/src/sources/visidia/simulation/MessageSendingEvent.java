package visidia.simulation;

import visidia.misc.*;

/**
 * Cette represente l'évènement associe a l'envoie d'un message sur le 
 * réseaux.
 */
public class MessageSendingEvent implements SimulEvent {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3338643625856936387L;
	private long evtNum;
    protected Integer srcId = null;
    protected Integer destId = null;
    protected Message message;
 
    /**
     * construit un évènement associe à l'envoi du 
     * packet <i>mesgPacket</i> sur le réseau de simulation.
     */
    public MessageSendingEvent(Long eventNumber, Message message, Integer senderId, Integer receiverId){
	this.srcId = new Integer(senderId.intValue());
	this.destId = new Integer( receiverId.intValue());
	this.evtNum = eventNumber.longValue();
	//this.msg = message.toString();
	this.message = message;
    }
    
    public Integer sender(){
	return this.srcId;
    }
 
    public Integer receiver(){
	return this.destId;
    }

    /**
     * donne le numero de l'évènement.
     */
    public Long eventNumber(){
	return new Long(this.evtNum);
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
	return this.message;
    }
}




