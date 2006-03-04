package visidia.simulation;

import java.io.Serializable;

/**
 * NodePropertyChangeAck object is used to acknowledge a property change event.
 */
public class NodePropertyChangeAck implements  SimulAck, Serializable{
    private long num;
    
    public NodePropertyChangeAck(Long evtNumber){
	num = evtNumber.longValue();
    }

    /**
     * return the acknowledgement number.
     */
    public Long number(){
	return new Long(num);
    }

    /**
     * return SimulConstants.NODE_PROPERTY_CHANGE as acknowledgement type.
     */
    public int type(){
	return SimulConstants.NODE_PROPERTY_CHANGE;
    }
}
