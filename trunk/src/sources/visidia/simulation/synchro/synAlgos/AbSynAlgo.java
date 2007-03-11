package visidia.simulation.synchro.synAlgos;

import java.util.Collection;
import java.util.LinkedList;

import visidia.misc.MessageType;
import visidia.misc.MSG_TYPES;
import visidia.misc.SyncState;
import visidia.rule.Star;
import visidia.simulation.Algorithm;
import visidia.simulation.synchro.synObj.SynObjectRules;

/**
 * all synchronisation algorithms should extend this class
 * 
 */

public abstract class AbSynAlgo extends Algorithm implements IntSynchronization {

	protected int answer[];

	public AbSynAlgo() {
		super();
	}

	public Collection getListTypes() {
		Collection<MessageType> typesList = new LinkedList<MessageType>();
		typesList.add(MSG_TYPES.SYNC);
		typesList.add(MSG_TYPES.TERM);
		return typesList;
	}

	public void setNeighbourhood(Star neighbourhood) {
		((SynObjectRules) this.synob).setNeighbourhood(neighbourhood);
	}

	abstract public Object clone();

	public String toString() {
		return "Abstract Synchro";
	}

	public void init() {
	}

	/**
	 * breaks synchronisation. in fact it informs the gui.
	 */
	public void breakSynchro() {
		for (int door = 0; door < this.getArity(); door++) {
			this.setDoorState(new SyncState(false), door);
		}
	}

}
