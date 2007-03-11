package visidia.misc;

import java.io.Serializable;

/**
 * classe représentant les messages échangés entre les noeuds du réseaux. Pour
 * s'assurer que deux noeuds du réseaux n'ont pas des références sur le même
 * objet message, ce dernier est dupliqué (clone) avant d'être mis dans la file
 * d'attente du destinataire.
 * 
 */

public abstract class Message implements Cloneable, Serializable {

	/**
	 * this field represents the time by which the message have been sent. The
	 * user can ignore it if the algorithm he is writing is asynchronous. When
	 * writing an asynchronous algorithm, this field is deeply used by the
	 * simulator. On the other hand, It can be set by hand by the user who is
	 * programming an algorithm (he must use the set and get method). It also
	 * allows to simulate the Lamport clock as well when writing an algorithm
	 * which class base is LamportAlgorithm
	 */

	private int clock;

	/**
	 * Each message has a type. The programmer may specify the type of the
	 * messages he creates. By default the type is defaultMessageType.
	 */
	private MessageType type = MessageType.defaultMessageType;

	private boolean visualization = true;

	/**
	 * This method allows the user to set the time at which the message is sent.
	 * 
	 */
	public void setMsgClock(int value) {
		this.clock = value;
	}

	/**
	 * This method allows the user to get the time at which the message has been
	 * sent.
	 */
	public int getMsgClock() {
		return this.clock;
	}

	/**
	 * this message set the type of a message. This is only for the purpse of
	 * visuaization. Nevertheless, the user can use create new message types an
	 * use them in his algorithms. The default message type is
	 * DefaultMessageType (red, to be visualized, name = default).
	 * 
	 */

	public void setType(MessageType type) {
		this.type = type;
	}

	/**
	 * return the message type. If the user have set a message type T then the
	 * method return this Type T. Otherwise if no Type have been set, then the
	 * default message type is DefaultMessageType.
	 */

	public MessageType getType() {
		return this.type;
	}

	/**
	 * Each message has a data which can be accessed by the getData() method.
	 */
	public abstract Object getData();

	/**
	 * return a message representation of the message. This is used for the
	 * visualization. For the visualization, it is important to provide a nice
	 * implementation for the this.toString() method.
	 */
	public abstract String toString();

	/**
	 * true, if the message is to be visualized; false otherwise.x
	 */
	public boolean getVisualization() {
		return this.visualization;
	}

	/**
	 * let s be true if the message is to be visualized and false otherwise.
	 */
	public void setVisualization(boolean s) {
		this.visualization = s;
	}

	/**
	 * give a copy (a clone) of this object
	 */
	public abstract Object clone();

}
