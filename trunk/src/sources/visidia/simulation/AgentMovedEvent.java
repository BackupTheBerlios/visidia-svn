package visidia.simulation;

import visidia.misc.*;
import java.io.Serializable;



public class AgentMovedEvent implements SimulEvent {
    private long evtNum;
    protected Integer vertexId = null;
    protected Integer nbrAg = null;
 
    public AgentMovedEvent(Long eventNumber, Integer vertId, Integer nbrAgents){
	this.vertexId = new Integer(vertId.intValue());
	this.nbrAg = new Integer(nbrAgents.intValue());
	this.evtNum = eventNumber.longValue();
    }
    
    public Integer vertexId(){
	return vertexId;
    }
 
    public Integer nbrAg(){
	return nbrAg;
    }

    public Long eventNumber(){
	return new Long(evtNum);
    }
   
    /**
     * gives the SimulEvent type.
     */
    public int type(){
	return SimulConstants.AGENT_MOVED;
    }
}




