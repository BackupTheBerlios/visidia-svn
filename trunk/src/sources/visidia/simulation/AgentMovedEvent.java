package visidia.simulation;

public class AgentMovedEvent implements SimulEvent {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7689039483254275033L;
	private long evtNum;
    protected Integer vertexId = null;
    protected Integer nbrAg = null;
 
    public AgentMovedEvent(Long eventNumber, Integer vertId, Integer nbrAgents){
	this.vertexId = new Integer(vertId.intValue());
	this.nbrAg = new Integer(nbrAgents.intValue());
	this.evtNum = eventNumber.longValue();
    }
    
    public Integer vertexId(){
	return this.vertexId;
    }
 
    public Integer nbrAg(){
	return this.nbrAg;
    }

    public Long eventNumber(){
	return new Long(this.evtNum);
    }
   
    /**
     * gives the SimulEvent type.
     */
    public int type(){
	return SimulConstants.AGENT_MOVED;
    }
}




