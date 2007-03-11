package visidia.simulation;

import java.io.Serializable;

/**
 * cette classe est utilisée pour informer le système de simulation que
 * l'évènement correspondant à un envoie de message à été prise en compte.
 */
public class EdgeStateChangeAck implements SimulAck, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3118200555455758230L;

	private long num;

	public EdgeStateChangeAck(Long evtNumber) {
		this.num = evtNumber.longValue();
	}

	public Long number() {
		return new Long(this.num);
	}

	public int type() {
		return SimulConstants.EDGE_STATE_CHANGE;
	}
}
