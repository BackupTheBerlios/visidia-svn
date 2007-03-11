package visidia.algo;

import java.util.Collection;
import java.util.LinkedList;

import visidia.misc.IntegerMessage;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.misc.SyncState;
import visidia.misc.SynchronizedRandom;
import visidia.simulation.Algorithm;

public class DetectStable extends Algorithm {
	final int starCenter = -1;

	final int notInTheStar = -2;

	static MessageType synchronization = new MessageType("synchronization",
			false, java.awt.Color.blue);

	// static MessageType booleen = new MessageType("booleen", false,
	// java.awt.Color.blue);
	static MessageType labels = new MessageType("labels", true);

	public Collection getListTypes() {
		Collection<MessageType> typesList = new LinkedList<MessageType>();
		typesList.add(DetectStable.synchronization);
		typesList.add(DetectStable.labels);
		// typesList.add(booleen);
		return typesList;
	}

	public void init() {

		final int arity = this.getArity();

		int graphSize = this.getNetSize();
		int neighboursA[];
		int myA = -1;
		int synchro;
		boolean run = true; /* booleen de fin de l'algorithme */
		boolean myP = false;
		int myNumber = Math.abs(SynchronizedRandom.nextInt());
		String myState = new String("F: " + myA);
		boolean finishedNode[];

		this.putProperty("label", new String(myState));

		neighboursA = new int[arity];

		finishedNode = new boolean[arity];
		for (int i = 0; i < arity; i++) {
			finishedNode[i] = false;
		}

		while (run) {

			int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
			if ((choosenNumber >= myNumber) && (!myP)) {
				myP = true;
				myState = "T: " + myA;
				this.putProperty("label", new String(myState));
			}

			synchro = this.starSynchro(finishedNode);
			if (synchro == this.starCenter) {
				int minA = 2 * graphSize;

				for (int door = 0; door < arity; door++) {
					if (!finishedNode[door]) {
						neighboursA[door] = ((IntegerMessage) this
								.receiveFrom(door)).value();
						if (neighboursA[door] < minA) {
							minA = neighboursA[door];
						}
					}
				}
				if (myP) {
					myA = 1 + minA;
					myState = "T: " + myA;
				}

				this.putProperty("label", new String(myState));

				if (myA > graphSize) {
					run = false;
				}

				this.breakSynchro();

			} else {
				if (synchro != this.notInTheStar) {

					this.sendTo(synchro, new IntegerMessage(new Integer(myA),
							DetectStable.labels));
				}
			}
		}

		for (int i = 0; i < this.getArity(); i++) {
			if (!finishedNode[i]) {
				this.sendTo(i, new IntegerMessage(new Integer(-1),
						DetectStable.synchronization));
			}
		}

	}

	public int starSynchro(boolean finishedNode[]) {

		int arite = this.getArity();
		int[] answer = new int[arite];

		/* random */
		int choosenNumber = Math.abs(SynchronizedRandom.nextInt());

		/* Send to all neighbours */
		for (int i = 0; i < arite; i++) {
			if (!finishedNode[i]) {
				this.sendTo(i, new IntegerMessage(new Integer(choosenNumber),
						DetectStable.synchronization));
			}
		}
		/* receive all numbers from neighbours */
		for (int i = 0; i < arite; i++) {
			if (!finishedNode[i]) {
				Message msg = this.receiveFrom(i);
				answer[i] = ((IntegerMessage) msg).value();
				if (answer[i] == -1) {
					finishedNode[i] = true;
				}
			}
		}

		/* get the max */
		int max = choosenNumber;
		for (int i = 0; i < arite; i++) {
			if (!finishedNode[i]) {
				if (answer[i] >= max) {
					max = answer[i];
				}
			}
		}

		for (int i = 0; i < arite; i++) {
			if (!finishedNode[i]) {
				this.sendTo(i, new IntegerMessage(new Integer(max),
						DetectStable.synchronization));
			}
		}
		/* get alla answers from neighbours */
		for (int i = 0; i < arite; i++) {
			if (!finishedNode[i]) {
				Message msg = this.receiveFrom(i);
				answer[i] = ((IntegerMessage) msg).value();
			}
		}

		/* get the max */
		max = choosenNumber;
		for (int i = 0; i < arite; i++) {
			if (answer[i] >= max) {
				max = answer[i];
			}
		}

		if (choosenNumber >= max) {
			for (int door = 0; door < arite; door++) {
				if (!finishedNode[door]) {
					this.setDoorState(new SyncState(true), door);
				}
			}

			for (int i = 0; i < arite; i++) {
				if (!finishedNode[i]) {
					this.sendTo(i, new IntegerMessage(new Integer(1),
							DetectStable.synchronization));
				}
			}

			for (int i = 0; i < arite; i++) {
				if (!finishedNode[i]) {
					/* Message msg= */this.receiveFrom(i);
				}
			}

			return this.starCenter;
		} else {
			int inTheStar = this.notInTheStar;

			for (int i = 0; i < arite; i++) {
				if (!finishedNode[i]) {
					this.sendTo(i, new IntegerMessage(new Integer(0),
							DetectStable.synchronization));
				}
			}

			for (int i = 0; i < arite; i++) {
				if (!finishedNode[i]) {
					Message msg = this.receiveFrom(i);
					if (((IntegerMessage) msg).value() == 1) {
						inTheStar = i;
					}
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
		return new DetectStable();
	}
}
