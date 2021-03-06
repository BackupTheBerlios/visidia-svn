package visidia.algo;

import java.util.Collection;
import java.util.LinkedList;

import visidia.misc.IntegerMessage;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.misc.StringMessage;
import visidia.misc.SyncState;
import visidia.misc.SynchronizedRandom;
import visidia.simulation.Algorithm;

public class Coloration extends Algorithm {
	final int starCenter = -1;

	final int notInTheStar = -2;

	static MessageType synchronization = new MessageType("synchronization",
			false, java.awt.Color.blue);

	// static MessageType booleen = new MessageType("booleen", false,
	// java.awt.Color.blue);
	static MessageType color = new MessageType("color", true);

	public Collection getListTypes() {
		Collection<MessageType> typesList = new LinkedList<MessageType>();
		typesList.add(Coloration.synchronization);
		typesList.add(Coloration.color);
		// typesList.add(booleen);
		return typesList;
	}

	public void init() {
		String myColor = new String("X");
		int myC = 0;
		int synchro;
		String neighbours[];

		neighbours = new String[this.getArity()];

		this.putProperty("label", myColor);

		while (true) {
			synchro = this.starSynchro();
			if (synchro == this.starCenter) {
				for (int i = 0; i < this.getArity(); i++) {
					neighbours[i] = ((StringMessage) this.receiveFrom(i))
							.data();
				}

				if ((neighbours[0].compareTo(myColor) == 0)
						&& (neighbours[1].compareTo(myColor) == 0)) {
					myC = (myC + 1) % 3;
					myColor = this.getNewColor(myC);
				} else {
					while ((neighbours[0].compareTo(myColor) == 0)
							|| (neighbours[1].compareTo(myColor) == 0)) {
						myC = (myC + 1) % 3;
						myColor = this.getNewColor(myC);
					}
				}

				this.putProperty("label", myColor);

				this.breakSynchro();

			} else {
				if (synchro != this.notInTheStar) {
					this.sendTo(synchro, new StringMessage(myColor,
							Coloration.color));
				}
			}
		}
	}

	private String getNewColor(int colors) {
		if (colors == 0) {
			return new String("X");
		} else if (colors == 1) {
			return new String("Y");
		} else {
			return new String("Z");
		}
	}

	public int starSynchro() {

		int arite = this.getArity();
		int[] answer = new int[arite];

		/* random */
		int choosenNumber = Math.abs(SynchronizedRandom.nextInt());

		/* Send to all neighbours */
		for (int i = 0; i < arite; i++) {
			this.sendTo(i, new IntegerMessage(new Integer(choosenNumber),
					Coloration.synchronization));
		}

		/* receive all numbers from neighbours */
		for (int i = 0; i < arite; i++) {
			Message msg = this.receiveFrom(i);
			answer[i] = ((IntegerMessage) msg).value();

		}

		int max = choosenNumber;
		for (int i = 0; i < arite; i++) {
			if (answer[i] >= max) {
				max = answer[i];
			}
		}

		for (int i = 0; i < arite; i++) {
			this.sendTo(i, new IntegerMessage(new Integer(max),
					Coloration.synchronization));
		}

		/* get alla answers from neighbours */
		for (int i = 0; i < arite; i++) {
			Message msg = this.receiveFrom(i);
			answer[i] = ((IntegerMessage) msg).value();
		}

		/* get the max */
		max = choosenNumber;
		for (int i = 0; i < arite; i++) {
			if (answer[i] >= max) {
				max = answer[i];
			}
		}

		if (choosenNumber >= max) {
			for (int door = 0; door < this.getArity(); door++) {
				this.setDoorState(new SyncState(true), door);
			}

			for (int i = 0; i < arite; i++) {
				this.sendTo(i,
						new IntegerMessage(1, Coloration.synchronization));
			}

			for (int i = 0; i < arite; i++) {
				/* Message msg= */this.receiveFrom(i);

			}
			return this.starCenter;
		} else {
			int inTheStar = this.notInTheStar;

			for (int i = 0; i < arite; i++) {
				this.sendTo(i,
						new IntegerMessage(0, Coloration.synchronization));
			}

			for (int i = 0; i < arite; i++) {
				Message msg = this.receiveFrom(i);
				if (((IntegerMessage) msg).value() == 1) {
					inTheStar = i;
				}
			}
			return inTheStar;

		}
	}

	public void breakSynchro() {

		for (int door = 0; door < this.getArity(); door++) {
			this.setDoorState(new SyncState(false), door);
		}
	}

	public Object clone() {
		return new Coloration();
	}
}
