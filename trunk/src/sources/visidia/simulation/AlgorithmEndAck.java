package visidia.simulation;

import java.io.Serializable;

/**
 * used to handle the terination of the algorithm
 */
public class AlgorithmEndAck implements  SimulAck, Serializable{
    private long num;
    
    public AlgorithmEndAck(Long evtNumber){
	num = evtNumber.longValue();
    }

    /**
     * return the acknowledgement number.
     */
    public Long number(){
	return new Long(num);
    }

    /**
     * return SimulConstants.ALGORITHM_END as acknowledgement type.
     */
    public int type(){
	return SimulConstants.ALGORITHM_END;
    }
}
