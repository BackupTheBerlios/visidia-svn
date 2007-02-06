package visidia.simulation;

import java.io.Serializable;

/**
 * Cette classe représente l'évènement associé à l'envoie d'un message
 * sur le réseaux.
 */
public class AlgorithmEndEvent implements SimulEvent, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -503969335525431962L;
	long evtNum;

	/**
	 * Construct new AlgorithmEndEvent numbered by num;
	 */
	 public AlgorithmEndEvent(long eventNumber){
	 	 this.evtNum = eventNumber;
	 }

    /**
     * return the event number.
     */
    public Long eventNumber(){
		return new Long(this.evtNum);
    }
   
    /**
     * return the type that corresponds to algorithm ending.
     * Event and acknownledge constants are defined
     * in the class SimulConstants.
     * @see SimulConstants
     */
    public int type(){
		return SimulConstants.ALGORITHM_END;
    }
}
