package visidia.simulation;

import java.io.Serializable;

/**
 * NodePropertyChangeAck object is used to acknowledge a property change event.
 */
public class NodePropertyChangeAck implements  SimulAck, Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7325381516876419185L;
	private long num;
    
    public NodePropertyChangeAck(Long evtNumber){
	this.num = evtNumber.longValue();
    }

    /**
     * return the acknowledgement number.
     */
    public Long number(){
	return new Long(this.num);
    }

    /**
     * return SimulConstants.NODE_PROPERTY_CHANGE as acknowledgement type.
     */
    public int type(){
	return SimulConstants.NODE_PROPERTY_CHANGE;
    }
}
