package visidia.simulation;
/**
 * This event is used for erasing agent which dead brutaly, it was killed.  
 */
public class AgentDeadEvent implements SimulEvent {
	private static final long serialVersionUID = 7689039483254275033L;
	private long evtNum;
	private String agentName = null;
	
	/**   
	 * @param evtNum - the event number 
	 * @param agName - the name of the agent which must be erased
	 */
	public AgentDeadEvent(Long evtNum, String agName) {
		this.agentName = new String(agName);
		this.evtNum = evtNum;
	}
	
	/**
	 * Gives the name of the agent who is dead.
	 * @return the dead agent name
	 */
	public String getAgentName() {
		return this.agentName;
	}

	/**
	 * Gives the event number.
	 * @return the event number
	 */
	public Long eventNumber() {
		return new Long(this.evtNum);
	}
	
	/**
	 * gives the SimulEvent type.
	 * @return the SimulEvent Type
	 */
	public int type() {
		return SimulConstants.AGENT_DEAD;
	}
}
