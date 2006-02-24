package visidia.simulation;

import visidia.misc.*;
import java.io.Serializable;

/**
 * Cette classe représente l'évènement associé au passage à un nouveau pulse
 * 
 */
public class NextPulseEvent implements SimulEvent {
    private int pulse;
    private long evtNum;
 
    public NextPulseEvent(Long key, int pulse) {
	this.pulse = pulse;
	evtNum = key;
    }
    
    public Long eventNumber(){
	return new Long(evtNum);
    }
   
    /**
     * donne le type de l'evenement.
     */
    public int type(){
	return SimulConstants.NEXT_PULSE;
    }
    
    public int pulse() {
	return pulse;
    }
    
}




