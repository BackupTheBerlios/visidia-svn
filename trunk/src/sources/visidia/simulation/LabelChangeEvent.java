package visidia.simulation;

public class LabelChangeEvent implements SimulEvent {
    private long evtNum;
    protected Integer vertexId = null;
    protected String label;
 
    public LabelChangeEvent(Long eventNumber, Integer vertId, String label){
	this.vertexId = new Integer(vertId.intValue());
	this.label = label;
	this.evtNum = eventNumber.longValue();
    }
    
    public Integer vertexId(){
	return vertexId;
    }
 
    public String label(){
	return label;
    }

    public Long eventNumber(){
	return new Long(evtNum);
    }
   
    /**
     * gives the SimulEvent type.
     */
    public int type(){
	return SimulConstants.LABEL_CHANGE;
    }
}




