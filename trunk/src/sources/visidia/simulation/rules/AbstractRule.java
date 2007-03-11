package visidia.simulation.rules;

import java.util.Collection;
import java.util.LinkedList;

import visidia.misc.BooleanMessage;
import visidia.misc.IntegerMessage;
import visidia.misc.MSG_TYPES;
import visidia.misc.MarkedState;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.misc.NeighbourMessage;
import visidia.misc.StringMessage;
import visidia.rule.Neighbour;
import visidia.rule.RSOptions;
import visidia.rule.RelabelingSystem;
import visidia.rule.Rule;
import visidia.rule.Star;
import visidia.simulation.Algorithm;
import visidia.simulation.synchro.SynCT;
import visidia.simulation.synchro.synObj.SynObjectRules;

/**
 * Simulateur des règles de réécritures
 * 
 */
public abstract class AbstractRule extends Algorithm {

	/**
	 * The relabeling System to simulate
	 */
	protected RelabelingSystem relSys = new RelabelingSystem();

	/** The synchronization used for simulation ** */
	public int synType;// type de synchronisation

	/**
	 * default constructor.
	 */
	public AbstractRule() {
		super();
	}

	/**
	 * constructor.from a relabeling system.
	 * 
	 * @param r
	 *            the relabeling system to simulate.
	 */
	public AbstractRule(RelabelingSystem r) {
		super();
		this.setRelSys(r);
	}

	public String toString() {
		return "Abstract Rule" + super.toString();
	}

	/**
	 * return collection of Message Types. message types are defined in
	 * misc.MSG_TYPES. this methode is common to all rules simulators.
	 */
	public Collection getListTypes() {
		Collection<MessageType> typesList = new LinkedList<MessageType>();
		typesList.add(MSG_TYPES.SYNC);
		typesList.add(MSG_TYPES.LABE);
		typesList.add(MSG_TYPES.TERM);
		if (this.synType == SynCT.LC1) {
			typesList.add(MSG_TYPES.MARK);
		}
		if (this.synType == SynCT.RDV) {
			typesList.add(MSG_TYPES.DUEL);
		}
		return typesList;
	}

	abstract public Object clone();

	/**
	 * methode used when copying (or cloning ) the proprieties of an the
	 * algorithm..
	 * 
	 * @param a
	 */
	public void copy(AbstractRule a) {
		super.copy(a);
		this.setRelSys(a.getRelSys());
	}

	// **************** COMMON WITH VISIDIA Distribuee **********************/

	/**
	 * used to print. (for tests).
	 * 
	 * @return
	 */
	public String print() {
		return (" RSAlgo: opt=" + this.relSys.userPreferences.toString()
				+ "\n RS=" + this.relSys.toString());
	}

	/**
	 * this methode receive from all synchronized neighbours their states, and
	 * update the information in the synob. this methode works with all
	 * synchronization algorithms.
	 */

	public void updateNeigborhoodInfo() {
		int door;
		int i;
		/* i receive the update */
		((SynObjectRules) this.synob).setCenterState((String) this
				.getProperty("label"));

		for (i = 0; i < ((SynObjectRules) this.synob).neighbourhood.arity(); i++) {
			door = ((SynObjectRules) this.synob).neighbourhood.neighbourDoor(i);
			if (this.synob.isConnected(door)) {
				Message m = this.receiveFrom(door);
				if (m != null) {
					StringMessage msg = (StringMessage) m;
					Neighbour n = new Neighbour(msg.data(), this.synob
							.getMark(door), door);
					((SynObjectRules) this.synob).neighbourhood.setState(i, n);
				} else {
					this.synob.setConnected(i, false);
				}
			}
		}
	}

	/**
	 * send the states (described in synob) to synchronized neighbours. this
	 * method is used by the center after a transformation. Warning: this
	 * methode is available only for RDV or LC2 synchronization types. it should
	 * be redefined for LC1.
	 */
	public void sendUpdate() {
		Neighbour n;
		String c = ((SynObjectRules) this.synob).neighbourhood.centerState();
		int i;
		this.setMyState(c);
		// System.out.println("in sendup neigh="+synob.neighbourhood);
		for (i = 0; i < ((SynObjectRules) this.synob).neighbourhood.arity(); i++) {
			n = ((SynObjectRules) this.synob).neighbourhood.neighbour(i);
			this.setDoorState(new MarkedState(n.mark()), n.doorNum());
			this.synob.setMark(n.doorNum(), n.mark());
			if (this.synob.isConnected(n.doorNum())) {
				switch (this.synType) {
				case SynCT.LC1:
					this.sendTo(n.doorNum(), new BooleanMessage(n.mark(),
							MSG_TYPES.MARK));
					break;
				default:
					this.sendTo(n.doorNum(), new NeighbourMessage(n,
							MSG_TYPES.LABE));
					break;
				}
			}
		}
	}

	/**
	 * send the state (label) to the neighbour. Warning: only RDV and LC2 use
	 * this methode ( redefined for LC1).
	 * 
	 * @param neighbour
	 *            the neighbour door.
	 */
	public void sendState(int neighbour) {
		if (this.synob.isConnected(neighbour)) {
			this.sendTo(neighbour, new StringMessage(this.getState()));
		}
	}

	/**
	 * set the relabeling system.
	 * 
	 * @param rs
	 *            the new relabeling system.
	 */
	public void setRelSys(RelabelingSystem rs) {
		this.relSys = rs;
	}

	public RelabelingSystem getRelSys() {
		return this.relSys;
	}

	// Works with RDV, LC1, and LC2 and supports various options
	/**
	 * It's the algorithm of simulation of relabeling system. Works with RDV,
	 * LC1, and LC2 and supports various options
	 */
	public void init() {
		int ruleToApply;
		int round = 0;
		int i = 0;
		// initialisation du synob et de synal
		this.synob.init(this.getArity());
		this.synal.set(this);

		while (this.synob.run) {
			round++;
			this.synob.reset();
			if (this.synob.allFinished()) {
				this.synob.run = false;
			} else {
				this.synal.trySynchronize();
			}
			((SynObjectRules) this.synob).refresh();

			if (this.synob.isElected()) {
				/** ** Elected node** */
				/* exchaging states */
				this.updateNeigborhoodInfo();
				/* choosing a rule to apply */
				ruleToApply = this.relSys
						.checkForRule((Star) ((SynObjectRules) this.synob).neighbourhood
								.clone());
				/* applying the rule */
				if (ruleToApply != -1) {
					int kindOfRule = this.applyRule(ruleToApply);
					this.sendUpdate();
					this.synal.breakSynchro();
					if (this.relSys.userPreferences.manageTerm) {
						this.endRuleAction(kindOfRule);
					}
				} else {
					this.sendUpdate();
					this.synal.breakSynchro();
				}

			} else if (this.synob.isNotInStar()) {
				if (this.synob.allFinished()) {
					for (i = 0; i < this.synob.arity; i++) {
						if (!this.synob.hasFinished(i)) {
							this.sendTo(i, new IntegerMessage(SynCT.GLOBAL_END,
									MSG_TYPES.TERM));
						}
					}
					this.synob.run = false;
				}
			} else if (this.synob.isInStar()) {
				this.sendMyState();
				// i receive the update
				this.receiveAndUpdateMyState();
				if (this.synob.allFinished()) {
					for (i = 0; i < this.synob.arity; i++) {
						if (!this.synob.hasFinished(i)) {
							this.sendTo(i, new IntegerMessage(SynCT.GLOBAL_END,
									MSG_TYPES.TERM));
						}
					}
					this.synob.run = false;
				}
			}
		}
	}

	/**
	 * this method do actions depending of the kind of the rule.
	 * 
	 * @param kindOfRule
	 *            possible values defined in class SynCT.
	 */
	public void endRuleAction(int kindOfRule) {
		switch (kindOfRule) {
		case (SynCT.GLOBAL_END): {
			// System.out.println("\n!->TERMINATION GLOBAL: Node"+getId()+"says:
			// Global END !!! *****");
			for (int i = 0; i < this.synob.arity; i++) {
				if (!this.synob.hasFinished(i)) {
					this.synob.setFinished(i, true);
					this.sendTo(i, new IntegerMessage(SynCT.GLOBAL_END,
							MSG_TYPES.TERM));
					// Message de term doit etre recu au debut de la synchron
				}
			}
			this.synob.run = false;
			break;
		}
		case (SynCT.LOCAL_END): {
			// System.out.println("\n!-> TERMINATION LOCAL: Node"+getId()+"says:
			// I have finished by by *****");
			for (int i = 0; i < this.synob.arity; i++) {
				if (!this.synob.hasFinished(i)) {
					this.sendTo(i, new IntegerMessage(SynCT.LOCAL_END,
							MSG_TYPES.TERM));
				}
			}
			this.synob.run = false;
			break;
		}
		}
	}

	/* for RDV LC2,but LC1 should redefine it */
	/**
	 * send the label to the star center if he is connected. in LC1 they are
	 * many centers, so this methode is redefined in LC1Rule.
	 */
	public void sendMyState() {
		// System.out.println("LC2 ou rdv");
		if (this.synob.isConnected(this.synob.center)) {
			this.sendTo(this.synob.center, new StringMessage(((String) this
					.getProperty("label")), MSG_TYPES.LABE));
		}
	}

	/**
	 * for RDV LC2, only LC1 should redefine it
	 * 
	 */

	public void receiveAndUpdateMyState() {
		if (this.synob.isConnected(this.synob.center)) {
			Message m = this.receiveFrom(this.synob.center);
			// System.out.println ("untel " + getId() + " a recu de " +
			// synob.center + " " +
			// synob.isConnected(0) + " " + synob.isConnected(1));
			if (m != null) {
				NeighbourMessage msg = (NeighbourMessage) m;
				this.setMyState(msg.label());
				// marking the Arrow;
				this.setDoorState(new MarkedState(msg.mark()),
						this.synob.center);
				this.synob.setMark(this.synob.center, msg.mark());
			}
		}
	}

	/**
	 * get user preferences
	 * 
	 * @return the RSOptions
	 */
	public RSOptions getRSOptions() {
		return this.relSys.userPreferences;
	}

	/**
	 * return the label of the node.
	 */
	public String getState() {
		return (String) this.getProperty("label");
	}

	public void setMyState(String newState) {
		this.putProperty("label", newState);
	}

	/**
	 * this methode applies the rule on psition i. the contexts is in synob,
	 * modifications are also done in synob.
	 * 
	 * @param i
	 *            position of the rule.
	 * @return
	 */
	public int applyRule(int i) {
		int retour;

		Rule r = (Rule) this.relSys.getRule(i).clone();
		retour = r.getType();
		Star b = new Star(r.befor());
		Star a = (Star) r.after().clone();

		if (((Star) ((SynObjectRules) this.synob).neighbourhood.clone())
				.contains(b)) {
			a.setDoors(b);
			((SynObjectRules) this.synob).neighbourhood.setStates(a);
		} else {
			// lever exception
			// System.out.println("regle non app");
		}
		return retour;
	}

	/**
	 * get help about the relabeling system
	 */
	public String getDescription() {
		return this.relSys.getDescription();
	}

	// PFA2003
	public boolean isRunning() {
		return this.synob.run;
	}
}
