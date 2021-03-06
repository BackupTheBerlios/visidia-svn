/**
 * implemente l'algorithme de synchronisation en etoile. 
 */

package visidia.algo;

import visidia.misc.IntegerMessage;
import visidia.misc.MarkedState;
import visidia.misc.Message;
import visidia.misc.SynchronizedRandom;
import visidia.simulation.Algorithm;

public class SynchroLC2 extends Algorithm {

	/**
	 * algorithme de synchronisation.
	 */
	public void init() {
		/*
		 * boolean i = synchroEtoile(); if(i==true) setState("centre"); else
		 * setState("perdu");
		 */
		while (true) {
			this.setState("P");
			if (this.synchroEtoile()) {
				this.setState("C");

				int n = this.getArity();

				for (int door = 0; door < n; door++) {
					this.setDoorState(new MarkedState(true), door);
				}

				try {
					Thread.sleep(Math.round(20000 * SynchronizedRandom
							.nextFloat()));
				} catch (InterruptedException e) {
				}

				for (int door = 0; door < n; door++) {
					this.setDoorState(new MarkedState(false), door);
				}
			}

		}

	}

	/**
	 * renvoie <code>true</code> si le noeud est centre d'une etoile
	 */
	public boolean synchroEtoile() {
		int arite = this.getArity();
		int[] answer = new int[arite];

		/* random */
		int choosenNumber = Math.abs(SynchronizedRandom.nextInt());

		/* Send to all neighbours */
		for (int i = 0; i < this.getArity(); i++) {
			this.sendTo(i, new IntegerMessage(new Integer(choosenNumber)));
		}
		// System.out.println( getId() + "nombre: "+choosenNumber);
		/* receive all numbers from neighbours */
		for (int i = 0; i < arite; i++) {
			Message msg = this.receiveFrom(i);
			answer[i] = ((IntegerMessage) msg).value();
		}

		/* get the max */
		int max = choosenNumber;
		for (int element : answer) {
			if (element >= max) {
				max = element;
			}
		}

		for (int i = 0; i < this.getArity(); i++) {
			this.sendTo(i, new IntegerMessage(new Integer(max)));
		}

		/* get alla answers from neighbours */
		for (int i = 0; i < arite; i++) {
			Message msg = this.receiveFrom(i);
			answer[i] = ((IntegerMessage) msg).value();
		}

		/* get the max */
		max = choosenNumber;
		for (int element : answer) {
			if (element >= max) {
				max = element;
			}
		}

		/* results */
		return choosenNumber >= max;
	}

	public String getState() {
		return (String) this.getProperty("label");
	}

	public void setState(String newState) {
		this.putProperty("label", newState);
	}

	public Object clone() {
		return new SynchroLC2();
	}
}
