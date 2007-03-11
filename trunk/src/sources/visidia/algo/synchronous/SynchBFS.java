package visidia.algo.synchronous;

import java.util.Collection;
import java.util.LinkedList;

import visidia.misc.MarkedState;
import visidia.misc.MessageType;
import visidia.misc.StringMessage;
import visidia.simulation.Door;
import visidia.simulation.SyncAlgorithm;

public class SynchBFS extends SyncAlgorithm {

	static MessageType wave = new MessageType("wave", true, new java.awt.Color(
			200, 0, 0));

	public Collection getListTypes() {
		Collection<MessageType> typesList = new LinkedList<MessageType>();
		typesList.add(wave);

		return typesList;
	}

	public void init() {

		boolean run = true;

		// int degres = getArity();
		Integer id = this.getId();

		this.nextPulse();

		while (run) {
			if (id == 1) {
				this.putProperty("label", new String("R"));
				this.sendAll(new StringMessage("WAVE", wave));
				run = false;
				this.nextPulse();
			} else {
				if (this.anyMsg()) {
					Door door = new Door();
					/* StringMessage msg = (StringMessage) */this
							.receive(door);
					// je marque mon père
					this.setDoorState(new MarkedState(true), door.getNum());
					this.putProperty("label", new String("L"));
					// j'envoi à tout le monde
					this.sendAll(new StringMessage("WAVE", wave));
					run = false;
				}
				this.nextPulse();
			}
		}
	}

	public Object clone() {
		return new SynchBFS();
	}

}
