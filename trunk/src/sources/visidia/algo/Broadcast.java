package visidia.algo;

import java.util.Collection;
import java.util.LinkedList;

import visidia.misc.MarkedState;
import visidia.misc.MessageType;
import visidia.misc.StringMessage;
import visidia.simulation.Algorithm;
import visidia.simulation.Door;

public class Broadcast extends Algorithm {

	static MessageType wave = new MessageType("Wave", true);

	static MessageType ack = new MessageType("Acknowledgment", true,
			java.awt.Color.blue);

	public Collection getListTypes() {
		Collection<MessageType> typesList = new LinkedList<MessageType>();
		typesList.add(Broadcast.ack);
		typesList.add(Broadcast.wave);
		return typesList;
	}

	public void init() {
		int degres = this.getArity();
		int fatherDoor;
		// int[] childrenDoors = new int[degres];

		String label = (String) this.getProperty("label");

		if (label.compareTo("A") == 0) {
			for (int i = 0; i < degres; i++) {
				this.sendTo(i, new StringMessage("Wave", Broadcast.wave));
			}
		} else {
			Door door = new Door();
			/* Message msg = */this.receive(door);

			fatherDoor = door.getNum();

			this.sendTo(fatherDoor, new StringMessage("Ack", Broadcast.ack));

			this.putProperty("label", new String("A"));
			this.setDoorState(new MarkedState(true), fatherDoor);

			for (int i = 0; i < degres; i++) {
				if (i != fatherDoor) {
					this.sendTo(i, new StringMessage("Wave", Broadcast.wave));
				}
			}
		}
	}

	public Object clone() {
		return new Broadcast();
	}

}
