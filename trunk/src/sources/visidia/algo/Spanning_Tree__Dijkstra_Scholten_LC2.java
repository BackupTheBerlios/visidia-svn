package visidia.algo;

import java.util.Collection;
import java.util.LinkedList;

import visidia.misc.IntegerMessage;
import visidia.misc.MarkedState;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.misc.StringMessage;
import visidia.misc.SyncState;
import visidia.misc.SynchronizedRandom;
import visidia.simulation.Algorithm;

public class Spanning_Tree__Dijkstra_Scholten_LC2 extends Algorithm {

	final int starCenter = -1;

	final int notInTheStar = -2;

	static MessageType synchronization = new MessageType("synchronization",
			false, java.awt.Color.blue);

	static MessageType termination = new MessageType("termination", false,
			java.awt.Color.green);

	static MessageType labels = new MessageType("labels", true);

	public Collection getListTypes() {
		Collection<MessageType> typesList = new LinkedList<MessageType>();
		typesList.add(Spanning_Tree__Dijkstra_Scholten_LC2.synchronization);
		typesList.add(Spanning_Tree__Dijkstra_Scholten_LC2.labels);
		typesList.add(Spanning_Tree__Dijkstra_Scholten_LC2.termination);
		return typesList;
	}

	public void init() {

		final String active = new String("Ac");
		final String passive = new String("Pa");
		final Integer p0 = new Integer(-1);
		final StringMessage mes = new StringMessage(new String("mes"),
				Spanning_Tree__Dijkstra_Scholten_LC2.labels);
		final StringMessage noMes = new StringMessage(new String("No Mes"),
				Spanning_Tree__Dijkstra_Scholten_LC2.labels);
		final StringMessage sig = new StringMessage(new String("sig"),
				Spanning_Tree__Dijkstra_Scholten_LC2.labels);
		final String nodeA = new String("A");
		final String nodeN = new String("N");

		String myState = nodeN;
		String state = passive;
		int sc = 0;
		Integer father = null;
		// boolean initial=false;
		int synchro;
		boolean run = true;
		String neighboursLabel;
		boolean finishedNode[];

		finishedNode = new boolean[this.getArity()];
		for (int i = 0; i < this.getArity(); i++) {
			finishedNode[i] = false;
		}

		// Si le neoud est p0 etiquete avec un noeud distingue A
		if (((String) this.getProperty("label")).compareTo("A") == 0) {
			state = active;
			father = p0;
			myState = nodeA;
			this.putProperty("label", new String("A : " + active + " : sc="
					+ sc));
		} else {
			this.putProperty("label", new String("N : " + passive + " : sc="
					+ sc));
		}

		while (run) {

			synchro = this.starSynchro(finishedNode);
			if (synchro == -3) {
				run = false;
			} else if (synchro == this.starCenter) {
				int neighbourA = -1;
				boolean neighbourN = false;

				for (int door = 0; door < this.getArity(); door++) {
					if (!finishedNode[door]) {
						neighboursLabel = ((StringMessage) this
								.receiveFrom(door)).data();

						if (neighboursLabel.compareTo(nodeA) == 0) {
							neighbourA = door;
						}

						if (neighboursLabel.compareTo(nodeN) == 0) {
							neighbourN = true;
						}
					}
				}

				// System.out.println(getId()+" : "+neighbourN);

				if ((myState.compareTo(nodeN) == 0) && (neighbourA != -1)) {
					myState = nodeA;
					state = active;
					father = new Integer(neighbourA);
					this.putProperty("label", new String("A : " + state
							+ " : sc=" + sc));
					this.setDoorState(new MarkedState(true), neighbourA);

					for (int door = 0; door < this.getArity(); door++) {
						if (!finishedNode[door]) {
							if (door == father.intValue()) {
								this.sendTo(door, mes);
							} else {
								this.sendTo(door, noMes);
							}
						}
					}
				} else {
					if ((myState.compareTo(nodeA) == 0) && (!neighbourN)
							&& (state.compareTo(active) == 0)) {
						state = passive;
						this.putProperty("label", new String("A : " + state
								+ " : sc=" + sc));

						if (sc == 0) {
							if (father == p0) {
								this.sendAll(noMes);
								run = false;
							} else {
								if (father != null) {
									for (int i = 0; i < this.getArity(); i++) {
										if (!finishedNode[i]) {
											if (i == father.intValue()) {
												this.sendTo(i, sig);
												// setDoorState(new
												// MarkedState(false),i);
											} else {
												this.sendTo(i, noMes);
											}
										}
									}
								} else {
									this.sendAll(noMes);
								}

								father = null;
							}
						} else {
							this.sendAll(noMes);
						}
					} else if ((myState.compareTo(nodeA) == 0)
							&& (state.compareTo(passive) == 0) && (sc == 0)
							&& (father != null)) {
						if (father == p0) {
							this.sendAll(noMes);
							run = false;
						} else {
							for (int i = 0; i < this.getArity(); i++) {
								if (!finishedNode[i]) {
									if (i == father.intValue()) {
										this.sendTo(i, sig);
										// setDoorState(new
										// MarkedState(false),i);
									} else {
										this.sendTo(i, noMes);
									}
								}
							}

							father = null;
						}
					} else {
						this.sendAll(noMes);
					}
				}

				this.breakSynchro();
			}

			else if (synchro != this.notInTheStar) {
				String son;

				this.sendTo(synchro, new StringMessage(myState,
						Spanning_Tree__Dijkstra_Scholten_LC2.labels));

				son = ((StringMessage) this.receiveFrom(synchro)).data();

				if (son.compareTo(mes.data()) == 0) {
					sc++;
					this.putProperty("label", new String("A : " + state
							+ " : sc=" + sc));
				} else if (son.compareTo(sig.data()) == 0) {
					sc--;
					this.putProperty("label", new String("A : " + state
							+ " : sc=" + sc));
				}

			}
		}

		this.sendAll(new IntegerMessage(new Integer(-3),
				Spanning_Tree__Dijkstra_Scholten_LC2.termination));

	}

	public int starSynchro(boolean finishedNode[]) {

		int arite = this.getArity();
		int[] answer = new int[arite];
		boolean theEnd = false;

		/* random */
		int choosenNumber = Math.abs(SynchronizedRandom.nextInt());

		/* Send to all neighbours */
		for (int i = 0; i < arite; i++) {
			if (!finishedNode[i]) {
				this.sendTo(i, new IntegerMessage(new Integer(choosenNumber),
						Spanning_Tree__Dijkstra_Scholten_LC2.synchronization));
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
				if (answer[i] == -3) {
					finishedNode[i] = true;
					theEnd = true;
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
		if (theEnd) {
			max = -3;
		}

		for (int i = 0; i < arite; i++) {
			if (!finishedNode[i]) {
				this.sendTo(i, new IntegerMessage(new Integer(max),
						Spanning_Tree__Dijkstra_Scholten_LC2.synchronization));
			}
		}
		/* get alla answers from neighbours */
		for (int i = 0; i < arite; i++) {
			if (!finishedNode[i]) {
				Message msg = this.receiveFrom(i);
				answer[i] = ((IntegerMessage) msg).value();
				if (answer[i] == -3) {
					theEnd = true;
				}
			}
		}

		/* get the max */
		max = choosenNumber;
		for (int i = 0; i < arite; i++) {
			if (!finishedNode[i]) {
				if (answer[i] >= max) {
					max = answer[i];
				}
			}
		}

		if (choosenNumber >= max) {
			for (int door = 0; door < arite; door++) {
				if (!finishedNode[door]) {
					this.setDoorState(new SyncState(true), door);
				}
			}

			if (!theEnd) {
				for (int i = 0; i < arite; i++) {
					if (!finishedNode[i]) {
						this
								.sendTo(
										i,
										new IntegerMessage(
												new Integer(1),
												Spanning_Tree__Dijkstra_Scholten_LC2.synchronization));
					}
				}

				for (int i = 0; i < arite; i++) {
					if (!finishedNode[i]) {
						this.receiveFrom(i);
					}
				}

				return this.starCenter;
			} else {
				for (int i = 0; i < arite; i++) {
					if (!finishedNode[i]) {
						this
								.sendTo(
										i,
										new IntegerMessage(
												new Integer(-3),
												Spanning_Tree__Dijkstra_Scholten_LC2.termination));
					}
				}

				for (int i = 0; i < arite; i++) {
					if (!finishedNode[i]) {
						this.receiveFrom(i);
					}
				}

				return -3;
			}
		} else {
			int inTheStar = this.notInTheStar;

			for (int i = 0; i < arite; i++) {
				if (!finishedNode[i]) {
					this
							.sendTo(
									i,
									new IntegerMessage(
											new Integer(0),
											Spanning_Tree__Dijkstra_Scholten_LC2.synchronization));
				}
			}

			for (int i = 0; i < arite; i++) {
				if (!finishedNode[i]) {
					Message msg = this.receiveFrom(i);
					if (((IntegerMessage) msg).value() == 1) {
						inTheStar = i;
					} else if (((IntegerMessage) msg).value() == -3) {
						finishedNode[i] = true;
						theEnd = true;
					}
				}
			}
			if (inTheStar != this.notInTheStar) {
				return inTheStar;
			} else if (theEnd) {
				return -3;
			} else {
				return this.notInTheStar;
			}

		}
	}

	public void breakSynchro() {

		for (int door = 0; door < this.getArity(); door++) {
			this.setDoorState(new SyncState(false), door);
		}
	}

	public Object clone() {
		return new Spanning_Tree__Dijkstra_Scholten_LC2();
	}
}
