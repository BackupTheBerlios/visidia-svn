package visidia.simulation;

public class LabelChangeEvent implements SimulEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6194741554843059451L;

	private long evtNum;

	protected Integer vertexId = null;

	protected String label;

	public LabelChangeEvent(Long eventNumber, Integer vertId, String label) {
		this.vertexId = new Integer(vertId.intValue());
		this.label = label;
		this.evtNum = eventNumber.longValue();
	}

	public Integer vertexId() {
		return this.vertexId;
	}

	public String label() {
		return this.label;
	}

	public Long eventNumber() {
		return new Long(this.evtNum);
	}

	/**
	 * gives the SimulEvent type.
	 */
	public int type() {
		return SimulConstants.LABEL_CHANGE;
	}
}
