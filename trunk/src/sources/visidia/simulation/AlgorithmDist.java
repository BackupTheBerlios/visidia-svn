package visidia.simulation;

import java.io.Serializable;
import java.util.Hashtable;

import visidia.misc.EdgeState;
import visidia.misc.Message;
import visidia.misc.MessageCriterion;
import visidia.misc.MessageType;
import visidia.network.NodeTry;
import visidia.tools.CompoundCriterion;

public abstract class AlgorithmDist implements Runnable, Cloneable,
		Serializable {
	protected final static StartSignal beginExec = new StartSignal();

	protected Hashtable messageTypeTable = new Hashtable();

	private Integer nodeId = null;

	protected NodeTry server;

	public Hashtable getListTypes() {
		return this.messageTypeTable;
	}

	public MessageType getType(MessageType type) {
		return (MessageType) this.messageTypeTable.get(type);
	}

	protected void addMessageType(MessageType mesgType) {
		this.messageTypeTable.put(mesgType.getType(), mesgType);
	}

	public void setMessageType(MessageType msgType, boolean state) {
		((MessageType) this.messageTypeTable.get(msgType.getType()))
				.setToPaint(state);
	}

	/**
	 * Permet d'envoyer le message "msg" sur la porte "door".
	 */
	protected void sendTo(int door, Message msg) {
		msg.setType((MessageType) this.messageTypeTable.get((msg.getType())
				.getType()));
		this.server.runningControl();

		try {
			this.server.sendTo(this.nodeId, door, msg);
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("Error when sending a message. Please report the error");
			throw new SimulationAbortError();
		}
	}

	/**
	 * Envoie le message "msg" à tous les voisins de ce noeud.
	 */
	protected void sendAll(Message msg) {
		int arite = this.getArity();
		for (int i = 0; i < arite; i++) {
			this.sendTo(i, msg);
		}
	}

	/**
	 * Retourne le premier message reçu de la porte "door". Tant que le noeud ne
	 * reçoit pas de message sur cette porte, il est bloqué.
	 */
	protected Message receiveFrom(int door) {
		this.server.runningControl();
		Message msg = null;
		try {
			msg = this.server.getNextMessage(this.nodeId, null,
					new DoorCriterion(door));
		} catch (Exception e) {
			throw new SimulationAbortError();
		}
		return msg;
	}

	protected Message receiveFrom(int door, MessageCriterion mc) {
		this.server.runningControl();
		DoorCriterion dc = new DoorCriterion(door);
		MessagePacketCriterion mpc = new MessagePacketCriterion(mc);
		CompoundCriterion c = new CompoundCriterion();
		c.add(dc);
		c.add(mpc);

		Message msg = null;
		try {
			msg = this.server.getNextMessage(this.nodeId, null, c);
		} catch (Exception e) {
			throw new SimulationAbortError();
		}
		return msg;
	}

	/**
	 * Retourne le premier message de la file d'attente de ce noeud. si la file
	 * est vide,le noeud est endormi jusqu'à réception d'un message.
	 */
	protected Message receive(Door door) {
		this.server.runningControl();
		Message msg = null;
		try {
			msg = this.server.getNextMessage(this.nodeId, door, null);
		} catch (Exception e) {
			throw new SimulationAbortError();
		}
		return msg;
	}

	protected Message receive(Door door, MessageCriterion mc) {
		this.server.runningControl();
		Message msg = null;
		try {
			msg = this.server.getNextMessage(this.nodeId, door,
					new MessagePacketCriterion(mc));
		} catch (Exception e) {
			throw new SimulationAbortError();
		}
		return msg;
	}

	/**
	 * Retourne le degrès du sommet.
	 */
	protected int getArity() {
		this.server.runningControl();
		return this.server.getArity();
	}

	/**
	 * Permet de relier le noeud à son serveur de noeuds.
	 */
	public void setServer(NodeTry s) {
		this.server = s;
	}

	/**
	 * Permet de changer l'identité du noeud, en cas de rv_enumerotation.
	 */
	public void setId(Integer id) {
		this.nodeId = new Integer(id.intValue());
	}

	/**
	 * retourne l'identité du noeud.
	 */
	protected Integer getId() {
		this.server.runningControl();
		return this.nodeId;
	}

	/**
	 * Algorithme à executer par ce noeud. L'utilisateur doit implémenter cette
	 * méthode s'il désire écrire un algorithme sans passer par les règles de
	 * réécriture.
	 */
	public abstract void init(); // algorithme de l'utilisateur.

	/**
	 * Methode faisant appel a <code>init()</code>. Cette méthode est
	 * nécessaire lors de l'instanciation du thread,un thread doit posséder la
	 * méthode <code>run()</code>.
	 */
	public final void run() {
		this.init();
		try {
			this.server.terminatedAlgorithm();
		} catch (Exception e) {
			throw new SimulationAbortError();
		}
	}

	protected void setDoorState(EdgeState st, int door) {
		this.server.runningControl();
		try {
			this.server.changeEdgeState(this.nodeId, door, st);
		} catch (Exception ie) {
			throw new SimulationAbortError();
		}
	}

	protected int getNetSize() {
		this.server.runningControl();
		return this.server.sizeOfTheGraph();
	}

	/**
	 * This method is call to message the current algorithm to stop running. It
	 * should be overrided by subclasses.
	 */
	public void abort() {
	}

	public abstract Object clone();

	/**
	 * Sets this node property. If <code>value</code> is null the property is
	 * removed.
	 */
	protected void putProperty(String key, Object value) {
		try {
			this.server.putNodeProperty(this.nodeId, key, value);
		} catch (Exception e) {
			throw new SimulationAbortError();
		}
	}

	/**
	 * Returns the property value identified by <code>key</code>. If no
	 * property identified by <code>key</code> exits, it <code>returns</code>
	 * null.
	 */
	protected Object getProperty(String key) {
		Object obj = new Object();
		try {
			obj = this.server.getNodeProperty(this.getId().intValue(), key);
		} catch (Exception e) {
			throw new SimulationAbortError();
		}
		return obj;
	}

	protected void incrementSynchMessages() {
		this.server.incrementSynchMessages();
	}

	protected void incrementSynch() {
		this.server.incrementSynch();
	}

	protected void incrementLabelMessages() {
		this.server.incrementLabelMessages();
	}
}
