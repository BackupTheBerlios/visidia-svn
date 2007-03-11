package visidia.simulation;

import java.util.Hashtable;
import java.util.Vector;

import visidia.misc.Message;

/**
 * This classe helps the user writing an algorithm in the distibuted synchronous
 * model of computation.<br>
 * 
 * A synchronous algorithm (written by the user) must have the following
 * squeleton <br>
 * BEGIN<br>
 * FOR EVERY PHASE i DO: <br>
 * 1 * actions to do in phase i : <br>
 * action 0 : ... <br>
 * action 1 : ... <br>
 * <br>
 * ... <br>
 * <br>
 * action k : sendTo(...) receive(...) getMsg...(...), anyMsg...(),
 * putProperty() ... ... ;<br>
 * <br>
 * ... <br>
 * ... <br>
 * <br>
 * final action : <br>
 * <br>
 * 2 * nextPulse(); // the nodes declares that he has finished the actions to do
 * in phase i <br> // it is blocked until all other nodes have finished their
 * actions in phase i <br> // if the node v does not declare the end of its
 * current phase then all other nodes <br> // will be bloqued until the node v
 * execute the nextPulse() method.<br> // the receive method automatically
 * execute the nextPulse() method if no message <br> // have been found.<br>
 * END DO <br>
 * END <br>
 * <br>
 * The main task of this class when sending a message is to set the <br>
 * message clock with the value returned by the getPulse methode of <br>
 * class Algorithm. <br>
 * 
 * the receive(...) and getMsg...(...) methods are more sophisticated and <br>
 * the user should take a look. Note that he can always do without by using the
 * <br>
 * existMessage(...) and getMessage(...) methods of class SyncAlgorithm
 */

public abstract class SyncAlgorithm extends Algorithm implements Runnable,
		Cloneable {

	// private Simulator sim = super.sim;

	public SyncAlgorithm() {
	}

	/**
	 * the user use this method to declare that all the action to be exectude in
	 * a pulse have been done and so it declares that the node is ready to enter
	 * the next pulse. bloque les noeuds jusqu'à ce que tous les autres noeuds
	 * du graphes rentrent dans le même pulse : synchronisation avec les autres
	 * noeuds du graphe l'utilisateur met un next pulse quand il estime que les
	 * actions à faire pendant le pulse courant sont terminées
	 */

	protected final void nextPulse() {
		this.sim.runningControl();

		// le noeud declare passer au pulse suivant
		this.sim.nextPulse();

		// le noeud declare avoir recu l'acquittemet pour le pulse
		// courant
		// sim.ackCurrentPulse();
	}

	/**
	 * retourne le pulse courant
	 * 
	 */
	protected final int getPulse() {
		return this.sim.getPulse();
	}

	/**
	 * Send a message on the the specified port (sets also the pulse by which
	 * the message is sent)
	 * 
	 */
	protected boolean sendTo(int door, Message msg) {
		msg.setMsgClock(this.getPulse());
		return super.sendTo(door, msg);
	}

	/**
	 * use the sendTo method on every port of the node. sends a message to all
	 * neighbors.
	 */
	protected void sendAll(Message msg) {
		int arite = this.getArity();
		for (int i = 0; i < arite; i++) {
			this.sendTo(i, msg);
		}
	}

	/**
	 * The most general and powerful method to handle reception of messages. It
	 * returns the message which matches the criterion dpc in the message queue
	 * of the node. If none then return null. The user should take a look at the
	 * DoorPulseCriterion class to learn more about how to initialize the dpc
	 * argument. <br>
	 * Note that if the Pulse field of dpc is set to be this.getPulse(), then
	 * the user can retrieve the messages that have been sent at the current
	 * phase and which arrives before its end. This is not allowed in the
	 * anyMsg... method (a verification is made). In fact, we consider that the
	 * only messages a node can retreive are those sent in the previous pulses.
	 * We do not make any verification here, in order to let the user implement
	 * ad-hoc instructions if he want. Thus, use this method carefully.
	 * 
	 */
	protected final Message getNextMessage(DoorPulseCriterion dpc) {
		try {
			this.sim.runningControl();
			return this.sim.getNextMessage(this.nodeId, dpc);
		} catch (InterruptedException e) {
			throw new SimulationAbortError();
		}
	}

	/**
	 * return true if there exists a message that matches the dpc criterion in
	 * the message queue of the node. The same remark than in the
	 * getNextMessage(...) method, holds when the pulse in dpc is set to be
	 * this.getPulse()
	 * 
	 */
	protected final boolean existMessage(DoorPulseCriterion dpc) {
		try {
			this.sim.runningControl();
			return !this.sim.emptyVQueue(this.nodeId, dpc);
		} catch (Exception e) {
			throw new SimulationAbortError();
		}
	}

	/*
	 * protected final Vector getAllNextMessages(DoorPulseCriterion dpc) { try{
	 * sim.runningControl(); sim.getAllNextMessages(nodeId, dpc); }
	 * catch(InterruptedException e){ throw new SimulationAbortError(); } }
	 */

	/**
	 * return true if the node has received any message which has been sent in
	 * the previous pulse
	 * 
	 */
	protected final boolean anyMsg() {
		DoorPulseCriterion dpc = new DoorPulseCriterion(this.getPulse() - 1);
		return this.existMessage(dpc);
	}

	/**
	 * return true if the node has received a message from the specified door,
	 * and which has been sent in the previous pulse
	 * 
	 */
	protected final boolean anyMsgDoor(int door) {
		DoorPulseCriterion dpc = new DoorPulseCriterion(door,
				this.getPulse() - 1);
		return this.existMessage(dpc);
	}

	/**
	 * return true if the node has received a message which has been sent in the
	 * specified pulse
	 */
	protected final boolean anyMsgPulse(int pulse) {
		if (pulse >= this.getPulse()) {
			return false;
		}

		DoorPulseCriterion dpc = new DoorPulseCriterion(pulse);
		return this.existMessage(dpc);
	}

	/**
	 * return true if the node has received a message from the specified door,
	 * and which has been sent in the specified pulse.
	 * 
	 */
	protected final boolean anyMsgDoorPulse(int door, int pulse) {
		if (pulse >= this.getPulse()) {
			return false;
		}

		DoorPulseCriterion dpc = new DoorPulseCriterion(door, pulse);
		return this.existMessage(dpc);
	}

	/**
	 * The nodes waits until reception of a message. If no message arrives from
	 * the previous pulse, then the node enter the next phase ; call to
	 * nextPulse(). The number of the port from which the message arrives is
	 * written in the Door object (method Door.getNum())
	 * 
	 */
	protected final Message receiveWait(Door door) {
		while (!this.anyMsg()) {
			this.nextPulse();
		}

		DoorPulseCriterion dpc = new DoorPulseCriterion(this.getPulse() - 1);
		Message msg = this.getNextMessage(dpc);
		door.setNum(dpc.getDoor());
		return msg;
	}

	/**
	 * The nodes waits until reception of a message. If no message arrives from
	 * the previous pulse, then the node enter the next phase ; call to
	 * nextPulse().
	 * 
	 */

	protected final Message receiveWait() {
		while (!this.anyMsg()) {
			this.nextPulse();
		}

		DoorPulseCriterion dpc = new DoorPulseCriterion(this.getPulse() - 1);
		Message msg = this.getNextMessage(dpc);
		return msg;
	}

	/**
	 * The nodes waits until reception of a message the specified door. If no
	 * message arrives from the previous pulse, then the node enter the next
	 * phase ; call to nextPulse().
	 * 
	 */
	protected final Message receiveWait(int door) {
		while (!this.anyMsgDoor(door)) {
			this.nextPulse();
		}

		DoorPulseCriterion dpc = new DoorPulseCriterion(door,
				this.getPulse() - 1);
		Message msg = this.getNextMessage(dpc);
		return msg;
	}

	/**
	 * return the first message arrived in the previous pulse and write the door
	 * number in the Door object. If there is no message then return null
	 * 
	 */
	protected final Message receive(Door door) {
		DoorPulseCriterion dpc = new DoorPulseCriterion(this.getPulse() - 1);
		Message msg = this.getNextMessage(dpc);
		if (msg != null) {
			door.setNum(dpc.getDoor());
			return msg;
		}
		return msg;
	}

	/**
	 * return the first message arrived in specified pulse and write the door
	 * number in the Door object. If none then return null
	 * 
	 */
	protected final Message receive(Door door, int pulse) {
		if (pulse >= this.getPulse()) {
			return null;
		}

		DoorPulseCriterion dpc = new DoorPulseCriterion(pulse);
		Message msg = this.getNextMessage(dpc);
		if (msg != null) {
			door.setNum(dpc.getDoor());
			return msg;
		}
		return msg;
	}

	/**
	 * return the message received on the specified port at the previous pulse.
	 * If none, then return null.
	 * 
	 */
	protected final Message receive(int door) {
		DoorPulseCriterion dpc = new DoorPulseCriterion(door,
				this.getPulse() - 1);
		Message msg = this.getNextMessage(dpc);

		return msg;
	}

	/**
	 * return the message received on the specified port and pulse. If none,
	 * then return null.
	 * 
	 */
	protected final Message receive(int door, int pulse) {
		if (pulse >= this.getPulse()) {
			return null;
		}
		DoorPulseCriterion dpc = new DoorPulseCriterion(door, pulse);
		Message msg = this.getNextMessage(dpc);

		return msg;
	}

	/**
	 * DO NOT USE this method. Not implemented yet. returns a hashtable of all
	 * messages received at the previous pulse. the keys of the hashtables are
	 * the doors and the values are a vectors of messages.
	 * 
	 */
	protected final Hashtable receiveAll() {
		// TO DO
		return null;
	}

	/**
	 * DO NOT USE this method. Not implemented yet. returns a vector of all
	 * messages received at the previous pulse on the specified door.
	 * 
	 */
	protected final Vector receiveAllFrom(int door) {
		return null;
		// TO DO
	}

	/**
	 * DO NOT USE this method. Not implemented yet. returns all message received
	 * at a given phase.
	 * 
	 */

	protected final Hashtable receiveAll(int phase) {
		return null;
	}

	/**
	 * deletes all received messages. There is no way to recover these messages
	 * once this method is executed. For performances reasons, it is highly
	 * recommended to purge messages received at previous pulses, if the user is
	 * sure that they will not be used in futur pulse of the algorithm
	 * 
	 */

	protected final void purge() {
		try {
			this.sim.runningControl();
			this.sim.purge(this.nodeId);
		} catch (Exception e) {
			throw new SimulationAbortError();
		}
	}

	public abstract void init();

}
