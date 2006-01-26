package fr.enserb.das.simulation;

import java.io.Serializable;

/**
 * elle est utilisee pour informer le systeme de simulation 
 * que l'evenement correspondant a un envoie de message
 * a ete prise en compte.
 */
public class MessageSendingAck implements  SimulAck {
    private long num;
    
    public MessageSendingAck(Long evtNumber){
	num = evtNumber.longValue();
    }

    public Long number(){
	return new Long(num);
    }

    public int type(){
	return SimulConstants.MESSAGE_SENT;
    }
}
