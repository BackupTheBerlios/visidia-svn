package visidia.simulation;

public class AgentDeadEvent implements SimulEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7689039483254275033L;
	private long evtNum;
	
	protected String agentName = null;
	
	/** This event is used for erasing agent which dead brutaly, it was killed.  
	 * 
	 * @param evtNum - the event number 
	 * @param agName - the name of the agent which must be erased
	 */
	public AgentDeadEvent(Long evtNum, String agName) {
		this.agentName = new String(agName);
		this.evtNum = evtNum;
	}
	/** 
	 * 
	 * @return the dead agent name
	 */
	public String getAgentName() {
		return this.agentName;
	}

	public Long eventNumber() {
		return new Long(this.evtNum);
	}
	
	/**
	 * gives the SimulEvent type.
	 */
	public int type() {
		return SimulConstants.AGENT_DEAD;
	}
}
