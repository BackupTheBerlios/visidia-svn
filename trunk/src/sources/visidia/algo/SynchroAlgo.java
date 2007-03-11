package visidia.algo;

import java.util.Random;

import visidia.misc.Message;
import visidia.misc.SyncMessage;
import visidia.misc.SyncMessageCriterion;
import visidia.misc.SyncState;
import visidia.simulation.Algorithm;

public abstract class SynchroAlgo extends Algorithm {

	/**
	 * essaie de synchroniser le noeud.retourne uniquement si la synchronisation
	 * est reussie.
	 */
	public int synchronization() {
		int i = -1;
		int a = this.getArity();

		// interface graphique:je ne suis plus synchro
		for (int door = 0; door < a; door++)
			this.setDoorState(new SyncState(false), door);

		while (i < 0) {
			i = this.trySynchronize();
		}
		// interface graphique: je suis synchro sur la porte i
		this.setDoorState(new SyncState(true), i);
		return i;
	}

	/**
	 * Un round de la synchronisation.
	 */
	private int trySynchronize() {
		int arite = this.getArity();
		int[] answer = new int[arite];

		/* choice of the neighbour */
		Random generator = new Random();
		int choosenNeighbour = Math.abs((generator.nextInt())) % arite;

		this.sendTo(choosenNeighbour, new SyncMessage(new Integer(1)));
		for (int i = 0; i < arite; i++) {
			if (i != choosenNeighbour)
				this.sendTo(i, new SyncMessage(new Integer(0)));

		}

		for (int i = 0; i < arite; i++) {
			Message msg = this.receiveFrom(i, new SyncMessageCriterion());
			SyncMessage smsg = (SyncMessage) msg;

			answer[i] = smsg.value();
		}

		if (answer[choosenNeighbour] == 1) {
			return choosenNeighbour;
		} else {
			return -1;
		}
	}
}
