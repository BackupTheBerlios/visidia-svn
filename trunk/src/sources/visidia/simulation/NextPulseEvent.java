package visidia.simulation;

/**
 * Cette classe represente l'evenement associe au passage a un nouveau pulse
 * 
 */
public class NextPulseEvent implements SimulEvent {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8490569645856290450L;
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




