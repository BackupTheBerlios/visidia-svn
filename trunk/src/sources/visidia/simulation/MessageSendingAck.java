package visidia.simulation;

/**
 * cette classe est utilisée pour informer le système de simulation 
 * que l'évènement correspondant à un envoie de message
 * a été prise en compte.
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
