package visidia.simulation.rules;

import visidia.misc.BooleanMessage;
import visidia.misc.MSG_TYPES;
import visidia.misc.MarkedState;
import visidia.misc.Message;
import visidia.misc.StringMessage;
import visidia.rule.RelabelingSystem;
import visidia.simulation.synchro.SynCT;
import visidia.simulation.synchro.synAlgos.LC1;

public class LC1Rule extends AbstractRule {

	public LC1Rule() {
		super();
		this.synType = SynCT.LC1;
		this.synal = new LC1();
	}

	public LC1Rule(RelabelingSystem r) {
		super(r);
		this.synType = SynCT.LC1;
		this.synal = new LC1();
	}

	public Object clone() {
		LC1Rule algo = new LC1Rule();
		algo.copy(this);
		return algo;
	}

	public String toString() {
		return ("RSAlgo: synal=" + this.synType + " opt="
				+ this.relSys.userPreferences.toString() + "\n RS=" + this.relSys
				.toString());
	}

	/* for LC1 */
	public void sendMyState() {
		for (int i = 0; i < this.synob.getCenters().size(); i++) {
			int door = ((Integer) this.synob.getCenters().elementAt(i))
					.intValue();
			if (this.synob.isConnected(door)) {
				this.sendTo(door, new StringMessage(((String) this
						.getProperty("label")), MSG_TYPES.LABE));
			}
		}
	}

	/* for LC1 */
	public void receiveAndUpdateMyState() {
		for (int i = 0; i < this.synob.getCenters().size(); i++) {
			int neighbour = ((Integer) this.synob.getCenters().elementAt(i))
					.intValue();
			if (this.synob.isConnected(neighbour)) {
				Message msg = this.receiveFrom(neighbour);
				if (msg != null) {
					this.setDoorState(new MarkedState(((BooleanMessage) msg)
							.data()), neighbour);
					this.synob
							.setMark(neighbour, ((BooleanMessage) msg).data());
				}
			}
		}
	}
}
