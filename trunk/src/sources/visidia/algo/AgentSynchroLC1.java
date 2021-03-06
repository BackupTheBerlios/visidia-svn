package visidia.algo;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import visidia.misc.IntegerMessage;
import visidia.misc.IntegerMessageCriterion;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.misc.SyncState;
import visidia.simulation.Algorithm;

public class AgentSynchroLC1 extends Algorithm {

	static Object notReady = new Object();

	static boolean notReadyBool = true;

	static int[] initialLocation;

	static int numberRdv = 0;

	static MessageType round1 = new MessageType("round1", true,
			java.awt.Color.blue);

	static MessageType round2 = new MessageType("round2", true,
			java.awt.Color.green);

	static MessageType round3 = new MessageType("round3", true,
			java.awt.Color.yellow);

	// static MessageType booleen = new MessageType("booleen", false,
	// java.awt.Color.blue);
	static MessageType labels = new MessageType("labels", true);

	public Collection getListTypes() {
		Collection<MessageType> typesList = new LinkedList<MessageType>();
		typesList.add(AgentSynchroLC1.round1);
		typesList.add(AgentSynchroLC1.round2);
		typesList.add(AgentSynchroLC1.round3);
		typesList.add(AgentSynchroLC1.labels);
		// typesList.add(booleen);
		return typesList;
	}

	public void init() {

		int numberOfPresentAgent = 0;
		boolean agentPresent = false;

		int arite = this.getArity();

		/*
		 * if (((String) getProperty("label")).compareTo("A")==0){ agentPresent =
		 * true; numberOfPresentAgent = 1; }
		 */

		if (this.getId().intValue() == 0) {
			// System.out.println("OK");
			int n = this.getNetSize();
			System.out.println("la taille du graphe est :" + n);
			AgentSynchroLC1.initialLocation = new int[n];
			double _k = Math.log(1.5) * n;
			// System.out.println(_k);
			int k = (int) _k;
			// System.out.println("le nombre d agents utilise pour la simulation
			// est :"+ k);
			Random generator = new Random();
			while (k > 0) {
				int choosenNode = Math.abs((generator.nextInt())) % n;
				// System.out.println(choosenNode);
				AgentSynchroLC1.initialLocation[choosenNode] += 1;
				k--;
			}
			try {
				synchronized (AgentSynchroLC1.notReady) {
					AgentSynchroLC1.notReadyBool = false;
					AgentSynchroLC1.notReady.notifyAll();
				}
			} catch (Exception e) {
			}

		} else {
			try {
				synchronized (AgentSynchroLC1.notReady) {
					if (AgentSynchroLC1.notReadyBool) {
						AgentSynchroLC1.notReady.wait();
					}
				}
			} catch (Exception e) {
			}

			numberOfPresentAgent = AgentSynchroLC1.initialLocation[this.getId()
					.intValue()];
			if (numberOfPresentAgent > 0) {
				agentPresent = true;
			}
		}

		for (int nbr = 0; nbr < 1000; nbr++) {
			Random generator = new Random();

			if (agentPresent) {
				this.putProperty("label", new String("A"));
				boolean tentative = true;

				/*
				 * j'envoi 1 a tous mes voisins
				 */

				for (int i = 0; i < arite; i++) {
					this.sendTo(i, new IntegerMessage(new Integer(1),
							AgentSynchroLC1.round1));
				}

				/*
				 * Symetriquement, je recois les message envoye par les autres
				 */
				for (int i = 0; i < arite; i++) {
					/* Message msg = */this.receiveFrom(i);
				}

				/*
				 * j'envoi a tout le monde 0 pour dire (a ceux qui m'ont choisi)
				 * Non
				 */
				for (int i = 0; i < arite; i++) {
					this.sendTo(i, new IntegerMessage(new Integer(0),
							AgentSynchroLC1.round2));
				}

				/*
				 * Symetriquement je recois les reponse des voisins
				 */

				for (int i = 0; i < arite; i++) {
					Message msg = this.receiveFrom(i);
					IntegerMessage smsg = (IntegerMessage) msg;
					if (smsg.value() == 0) {
						tentative = false;
					}
				}

				if (tentative) {
					for (int door = 0; door < arite; door++) {
						this.setDoorState(new SyncState(true), door);
					}
					AgentSynchroLC1.numberRdv += 1;
				}

				/*
				 * j'envoi tous mes jeton 1 vers des nouvelles direction et 0
				 * aux autres
				 */
				int go = 0;
				int[] goDirection = new int[arite];
				for (int i = 0; i < numberOfPresentAgent; i++) {
					// generator = new Random();
					int reste = Math.abs((generator.nextInt())) % 2;
					if (reste == 1) {
						// generator = new Random();
						int choosenDirection = Math.abs((generator.nextInt()))
								% arite;
						goDirection[choosenDirection] += 1;
						go = go + 1;
					}
				}

				numberOfPresentAgent = numberOfPresentAgent - go;

				for (int i = 0; i < arite; i++) {
					this.sendTo(i, new IntegerMessage(new Integer(
							goDirection[i]), AgentSynchroLC1.labels));
				}

				/*
				 * Symetriquement je regarde s'il y'a de nouveau jeton
				 */
				for (int i = 0; i < arite; i++) {
					Message msg = this.receiveFrom(i,
							new IntegerMessageCriterion());
					IntegerMessage smsg = (IntegerMessage) msg;
					numberOfPresentAgent += smsg.value();
				}

				if (numberOfPresentAgent > 0) {
					agentPresent = true;
				} else {
					agentPresent = false;
				}

				for (int door = 0; door < arite; door++) {
					this.setDoorState(new SyncState(false), door);
				}

			} else {
				this.putProperty("label", new String("N"));
				int[] ilMontChoisit = new int[arite];

				// j'envoi 0 a tout le monde
				for (int i = 0; i < arite; i++) {
					this.sendTo(i, new IntegerMessage(new Integer(0),
							AgentSynchroLC1.round1));
				}

				// je recois les requetes des voisins
				for (int i = 0; i < arite; i++) {
					Message msg = this.receiveFrom(i,
							new IntegerMessageCriterion());
					IntegerMessage smsg = (IntegerMessage) msg;
					// un jeton est arrive
					if (smsg.value() == 1) {
						ilMontChoisit[i] = 1;
					}
				}

				// j'envoi 1 a tout ceux qui m'ont choisit
				for (int i = 0; i < arite; i++) {
					this.sendTo(i, new IntegerMessage(new Integer(
							ilMontChoisit[i]), AgentSynchroLC1.round2));
				}

				// symetriquement je recoit les reponses
				for (int i = 0; i < arite; i++) {
					/* Message msg = */this.receiveFrom(i);
				}

				// j'envoi 0
				for (int i = 0; i < arite; i++) {
					this.sendTo(i, new IntegerMessage(new Integer(0),
							AgentSynchroLC1.round3));
				}

				// je recois les nouveau jeton
				for (int i = 0; i < arite; i++) {
					Message msg = this.receiveFrom(i);
					IntegerMessage smsg = (IntegerMessage) msg;
					numberOfPresentAgent += smsg.value();
				}

				if (numberOfPresentAgent > 0) {
					agentPresent = true;
				} else {
					agentPresent = false;
				}

			}

		}

		if (this.getId().intValue() == 0) {
			System.out.println("nombre de RDV : " + AgentSynchroLC1.numberRdv);
			// System.out.println("##############################################");
			AgentSynchroLC1.notReadyBool = true;
			AgentSynchroLC1.numberRdv = 0;
		}
	}

	public Object clone() {
		return new AgentSynchroLC1();
	}
}
