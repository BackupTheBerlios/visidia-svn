package visidia.simulation;

import visidia.misc.*;
import java.io.Serializable;

/**
 * Cette represente l'evenement associe a l'envoie d'un message sur le 
 * reseaux.
 */
public class AlgorithmEndEvent implements SimulEvent, Serializable{
	long evtNum;

	/**
	 * Construct new AlgorithmEndEvent numbered by num;
	 */
	 public AlgorithmEndEvent(long eventNumber){
	 	 evtNum = eventNumber;
	 }

    /**
     * return the event number.
     */
    public Long eventNumber(){
		return new Long(evtNum);
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
