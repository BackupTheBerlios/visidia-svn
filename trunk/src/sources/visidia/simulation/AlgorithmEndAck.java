package visidia.simulation;

import java.io.Serializable;

/**
 * used to handle the terination of the algorithm
 */
public class AlgorithmEndAck implements  SimulAck, Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6352831579822684755L;
	private long num;
    
    public AlgorithmEndAck(Long evtNumber){
	this.num = evtNumber.longValue();
    }

    /**
     * return the acknowledgement number.
     */
    public Long number(){
	return new Long(this.num);
    }

    /**
     * return SimulConstants.ALGORITHM_END as acknowledgement type.
     */
    public int type(){
	return SimulConstants.ALGORITHM_END;
    }
}
