package visidia.simulation;

/**
 * Class used to inform the simulator that an agant moving event has been
 * handled. que l'évènement correspondant à un envoie de message a été prise en
 * compte.
 */
public class AgentMovedAck implements SimulAck {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4737249681721338073L;

	private long num;

	public AgentMovedAck(Long evtNumber) {
		this.num = evtNumber.longValue();
	}

	public Long number() {
		return new Long(this.num);
	}

	public int type() {
		return SimulConstants.AGENT_MOVED;
	}
}
