package visidia.algo;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import visidia.misc.IntegerMessage;
import visidia.misc.IntegerMessageCriterion;
import visidia.misc.MarkedState;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.misc.StringMessage;
import visidia.misc.SyncState;
import visidia.misc.SynchronizedRandom;
import visidia.simulation.Algorithm;

public class Election_Complet extends Algorithm {

	/*
	 * R1: N-0-N ---> N-1-F R2: N ---> E
	 */

	static MessageType synchronization = new MessageType("synchronization",
			false, java.awt.Color.blue);

	// static MessageType booleen = new MessageType("booleen", false,
	// java.awt.Color.blue);
	static MessageType labels = new MessageType("labels", true);

	public Collection getListTypes() {
		Collection<MessageType> typesList = new LinkedList<MessageType>();
		typesList.add(Election_Complet.synchronization);
		typesList.add(Election_Complet.labels);
		// typesList.add(booleen);
		return typesList;
	}

	final String fNode = new String("F");

	final String nNode = new String("N");

	final String eNode = new String("E");

	public void init() {

		// int graphS=getNetSize(); /* la taille du graphe */
		int synchro;
		boolean run = true; /* booleen de fin de l'algorithme */
		String neighbourLabel;
		boolean finishedNode[] = new boolean[this.getArity()];
		// String lastName;
		int nb;
		// Vector name;

		for (int i = 0; i < this.getArity(); i++) {
			finishedNode[i] = false;
		}

		while (run) {

			synchro = this.synchronization(finishedNode);

			nb = this.getArity();
			for (int i = 0; i < this.getArity(); i++) {
				if (finishedNode[i]) {
					nb--;
				}
			}
			if (nb == 0) {
				this.putProperty("label", new String(this.eNode));
				break;
			}

			this.sendTo(synchro, new StringMessage((String) this
					.getProperty("label"), Election_Complet.labels));
			neighbourLabel = ((StringMessage) this.receiveFrom(synchro)).data();

			if ((((String) this.getProperty("label")).compareTo(this.nNode) == 0)
					&& (neighbourLabel.compareTo(this.nNode) == 0)) {

				int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
				this.sendTo(synchro, new IntegerMessage(new Integer(
						choosenNumber)));
				Message msg = this.receiveFrom(synchro);
				int answer = ((IntegerMessage) msg).value();

				if (choosenNumber < answer) {
					this.putProperty("label", new String(this.fNode));
					run = false;
				}
				/*
				 * else if (choosenNumber>answer) { finishedNode[synchro]=true;
				 * nbr_arity--; }
				 */
			}

			// System.out.println(nbr_arity);

		}

		for (int i = 0; i < this.getArity(); i++) {
			if (!finishedNode[i]) {
				this.sendTo(i, new IntegerMessage(new Integer(-1),
						Election_Complet.synchronization));
			}
		}
	}

	/**
	 * essaie de synchroniser le noeud.retourne uniquement si la synchronisation
	 * est reussie.
	 */
	public int synchronization(boolean finishedNode[]) {
		int i = -2;
		int a = this.getArity();

		// interface graphique:je ne suis plus synchro
		for (int door = 0; door < a; door++) {
			this.setDoorState(new SyncState(false), door);
		}

		while (i < -1) {
			i = this.trySynchronize(finishedNode);
		}
		// interface graphique: je suis synchro sur la porte i
		if (i > -1) {
			this.setDoorState(new SyncState(true), i);
		}
		return i;
	}

	/**
	 * Un round de la synchronisation.
	 */
	private int trySynchronize(boolean finishedNode[]) {
		int arite = this.getArity();
		int[] answer = new int[arite];
		int nb;

		/* choice of the neighbour */
		Random generator = new Random();
		int choosenNeighbour = Math.abs((generator.nextInt())) % arite;

		nb = arite;
		for (int i = 0; i < arite; i++) {
			if (finishedNode[i]) {
				nb--;
			}
		}

		if (nb == 0) {
			return -1;
		}

		while (finishedNode[choosenNeighbour]) {
			generator = new Random();
			choosenNeighbour = Math.abs((generator.nextInt())) % arite;
		}

		this.sendTo(choosenNeighbour, new IntegerMessage(new Integer(1),
				Election_Complet.synchronization));
		for (int i = 0; i < arite; i++) {
			if (i != choosenNeighbour) {
				if (!finishedNode[i]) {
					this.sendTo(i, new IntegerMessage(new Integer(0),
							Election_Complet.synchronization));
				}
			}

		}

		for (int i = 0; i < arite; i++) {
			if (!finishedNode[i]) {
				Message msg = this
						.receiveFrom(i, new IntegerMessageCriterion());
				IntegerMessage smsg = (IntegerMessage) msg;

				answer[i] = smsg.value();
				if (answer[i] == -1) {
					finishedNode[i] = true;
				}

			}
		}
		if (answer[choosenNeighbour] == 1) {
			return choosenNeighbour;
		} else {
			return -2;
		}

	}

	public void breakSynchro() {

		for (int door = 0; door < this.getArity(); door++) {
			this.setDoorState(new MarkedState(false), door);
		}
	}

	public Object clone() {
		return new Election_Complet();
	}
}
