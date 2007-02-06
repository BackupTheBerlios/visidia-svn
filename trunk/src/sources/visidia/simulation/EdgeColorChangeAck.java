package visidia.simulation;

import java.io.Serializable;

/**
 * This class is used to inform the simulator
 * that the event associated whith the change of the color
 * have been taken into account.
 * @version 1.0
 */
public class EdgeColorChangeAck implements  SimulAck, Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8317862315314045332L;
	private long num;
    
    public EdgeColorChangeAck(Long evtNumber){
	num = evtNumber.longValue();
    }

    public Long number(){
	return new Long(num);
    }

    public int type(){
	return SimulConstants.EDGE_COLOR_CHANGE;
    }
}
