package visidia.simulation;

import java.io.Serializable;

/**
 * NextPulseAck object is used to acknowledge the next pulse event : the
 * SimulEventHandler has finished to handle all event occuring at the current
 * phase
 */
public class NextPulseAck implements SimulAck, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4361654914043148L;

	private long num;

	public NextPulseAck(Long evtNumber) {
		this.num = evtNumber.longValue();
	}

	/**
	 * return the acknowledgement number.
	 */
	public Long number() {
		return new Long(this.num);
	}

	/**
	 * return SimulConstants.NEXT_PULSE as acknowledgement type.
	 */
	public int type() {
		return SimulConstants.NEXT_PULSE;
	}
}
