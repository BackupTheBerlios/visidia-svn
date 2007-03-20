package visidia.simulation.agents;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Set;

import visidia.misc.EdgeState;
import visidia.misc.MarkedState;
import visidia.misc.SyncState;
import visidia.simulation.SimulationAbortError;
import visidia.simulation.agents.stats.AbstractStat;
import visidia.tools.agents.WhiteBoard;
import visidia.tools.agents.WithWhiteBoard;
import visidia.visidiassert.VisidiaAssertion;

/**
 * Abstract class used to implement agent based algorithms. If you want to write
 * a new Agent, subclass it or subclass SynchronizedAgent which allows you to
 * get synchronisation.<br>
 * 
 * All new agents MUST be in the package {@link visidia.agents}.
 * 
 * @see SynchronizedAgent
 * @see AgentSimulator
 */
public abstract class Agent implements Runnable, WithWhiteBoard {

	/**
	 * A link to the simulator responsible for nearly all the actions. Can't do
	 * anything alone.
	 */
	private AgentSimulator simulator;

	/**
	 * A WhiteBoard allows one to store values with keys (like a Hashtable).
	 * With this, the class can store information during its live time.
	 * 
	 * @see getProperty()
	 * @see setProperty()
	 */
	private WhiteBoard whiteBoard;

	/**
	 * An AgentMover is used to move in a specific way. For a random move use
	 * RandomAgentMover for example.
	 * 
	 * @see setAgentMover()
	 * @see move()
	 */
	private AgentMover agentMover = null;

	/**
	 * Unique identifier over all the agents.
	 */
	private int agentIdentity;

	/**
	 * Number of agents in the simulation. Used to set a unique identifier.
	 * 
	 * @see agentIdentity
	 */
	private static int createdAgentCount = 0;

	private static Boolean askForLock = new Boolean(true);

	/**
	 * Default constructor. Creates a new agent and assigns it an unique
	 * identifier. Don't forget to use setSimulator() because an agent can't do
	 * anything without an AgentSimulator.
	 * 
	 * @see #setSimulator(AgentSimulator)
	 */
	public Agent() {
		this.agentIdentity = Agent.createdAgentCount++;
	}

	/**
	 * Affets a simulator to this agent. This is mandatory because an agent
	 * can't do anything by itself.
	 * 
	 * @param simulator
	 *            Affect this simulator to the agent
	 */
	public void setSimulator(AgentSimulator simulator) {
		this.simulator = simulator;
	}

	/**
	 * Removes the existing white board and affect this one.
	 * 
	 * @param wb
	 *            The white board to affect.
	 * @see #getWhiteBoard()
	 */
	public void setWhiteBoard(WhiteBoard wb) {
		this.whiteBoard = wb;
	}

	/**
	 * Creates a new WhiteBoard with defaults values.
	 */
	public void setWhiteBoard(Hashtable<Object,Object> defaults) {
		this.whiteBoard = new WhiteBoard(defaults);
	}

	/**
	 * Returns the WhiteBoard associated with this agent.
	 * 
	 * @see #setWhiteBoard(Hashtable)
	 */
	public WhiteBoard getWhiteBoard() {
		return this.whiteBoard;
	}

	/**
	 * Creates a new agent mover based on the name in parameter and affect it to
	 * the agent.
	 * 
	 * @param agentMoverClassName
	 *            a String representing the AgentMoverClass. Like \a
	 *            RandomAgentMover for exemple.
	 * @see #setAgentMover(AgentMover)
	 */
	public void setAgentMover(String agentMoverClassName) {

		try {
			Constructor constructor;
			Class agClass;
			String completName;
			AgentMover mover;

			completName = new String("visidia.agents.agentsmover."
					+ agentMoverClassName);
			agClass = Class.forName(completName);
			constructor = agClass.getConstructor(Agent.class);
			mover = (AgentMover) constructor.newInstance(this);
			this.setAgentMover(mover);
		} catch (Exception e) {
			throw new IllegalArgumentException("Instance can't be created !", e);
		}
	}

	public Collection agentsOnVertex() {
		return this.simulator.getAgentsVertexCollection(this
				.getVertexIdentity());
	}

	/**
	 * Uses the AgentMover as parameter to move the agent.
	 * 
	 * @see #setAgentMover(String)
	 */
	public void setAgentMover(AgentMover am) {
		this.agentMover = am;
	}

	/**
	 * Returns the current AgentMover.
	 */
	public AgentMover getAgentMover() {
		return this.agentMover;
	}

	/**
	 * Returns the door from which the agent comes.
	 */
	public int entryDoor() {
		return this.simulator.entryDoor(this);
	}

	/**
	 * Low level method to move the agent. You might prefer to use move() in
	 * conjunction with an AgentMover.
	 * 
	 * @param door
	 *            The door to which move
	 * @see #setAgentMover(String)
	 * @see #move()
	 */
	public void moveToDoor(int door) {
		if (this.isDead != true)
		{
			try {
				this.simulator.moveAgentTo(this, door);
			} catch (InterruptedException e) {
				throw new SimulationAbortError(e);
			}
		}
		else{
			this.simulator.realyKillAgent(this);
		}
	}

	/**
	 * Moves the agent using the AgentMover. You should have set an AgentMover
	 * before that using setAgentMover(String) or setAgentMover(AgentMover).
	 * 
	 * @see #setAgentMover(String)
	 * @see #setAgentMover(AgentMover)
	 */
	public void move() {
		VisidiaAssertion.verify(this.agentMover != null,
				"In move() : The AgentMover hasn't been " + "specified yet !",
				this);
		try {
			this.agentMover.move();
		} catch (InterruptedException e) {
			throw new SimulationAbortError(e);
		}
	}

	/**
	 * Moves the agent using the AgentMover to a specified door. You should have
	 * set an AgentMover before that using setAgentMover(String) or
	 * setAgentMover(AgentMover).
	 * 
	 * @param door
	 *            The door to which move
	 * 
	 * @see #setAgentMover(String)
	 * @see #setAgentMover(AgentMover)
	 */
	public void move(int door) {
		VisidiaAssertion.verify(this.agentMover != null,
				"In move() : The AgentMover hasn't been " + "specified yet !",
				this);
		try {
			this.agentMover.move(door);
		} catch (InterruptedException e) {
			throw new SimulationAbortError(e);
		}
	}
	
	
	private boolean isDead = false; 
	public void setDeath()
	{
		this.isDead = true;
	}

	/**
	 * Moves the agent back to the vertex from where it comes.
	 */
	public void moveBack() {
		this.moveToDoor(this.entryDoor());
	}

	/**
	 * Returns the number of doors available from the vertex the agent is on.
	 */
	public int getArity() {
		return this.simulator.getArity(this);
	}

	/**
	 * Use this method if you want to fall asleep for a given amount of
	 * milliseconds.
	 * 
	 * @param millis
	 *            Milliseconds to sleep.
	 */
	protected void sleep(long millis) {
		try {
			this.simulator.sleep(this, millis);
		} catch (InterruptedException e) {
			throw new SimulationAbortError(e);
		}
	}

	/**
	 * Returns the number of vertices in the graph.
	 */
	public int getNetSize() {
		return this.simulator.getNetSize();
	}

	/**
	 * Returns the unique identifier that identifies the currently visited
	 * vertex.
	 */
	public int getVertexIdentity() {
		return this.simulator.getVertexIdentity(this);
	}

	/**
	 * Returns the unique identifier that identifies the agent.
	 */
	public int getIdentity() {
		return this.agentIdentity;
	}

	/**
	 * Gets a value from the WhiteBoard.
	 * 
	 * @param key
	 *            Key behind which found the value.
	 * @see #setProperty(Object, Object)
	 * @see #setWhiteBoard(Hashtable)
	 */
	public Object getProperty(Object key) {
		return this.whiteBoard.getValue(key);
	}

	/**
	 * Puts a value in the WhiteBoard. The key references this value.
	 * 
	 * @see #getProperty(Object)
	 * @see #setWhiteBoard(Hashtable)
	 * @param key
	 *            Key on which the value must be stored
	 * @param value
	 *            Value that must be stored
	 */
	public void setProperty(Object key, Object value) {
		this.whiteBoard.setValue(key, value);
	}

	/**
	 * Returns a collection of all the keys of the WitheBoard.
	 */
	public Set getPropertyKeys() {
		return this.whiteBoard.keys();
	}

	/**
	 * Lock the Vertex WhiteBoard where the Agent is. If already locked, wait
	 * until the owner unlocks it
	 * 
	 * @exception IllegalStateException
	 *                if Vertex properties are already locked by this agent
	 * @see #unlockVertexProperties()
	 */
	public void lockVertexProperties() {
		this.simulator.lockVertexProperties(this);
	}

	/**
	 * Unlock the Vertex WhiteBoard where the Agent is.
	 * 
	 * @exception IllegalStateException
	 *                if the WhiteBoard is unlock, or if the Agent is not the
	 *                owner of the lock
	 * @see #lockVertexProperties()
	 */
	public void unlockVertexProperties() {
		this.simulator.unlockVertexProperties(this);
	}

	/**
	 * Return true if the Vertex is locked, otherwise false
	 * 
	 * @see #lockVertexProperties()
	 */
	public boolean vertexPropertiesLocked() {
		return this.simulator.vertexPropertiesLocked(this);
	}

	/**
	 * 
	 */
	public boolean lockVertexIfPossible() {
		boolean lock;

		synchronized (Agent.askForLock) {
			lock = this.vertexPropertiesLocked();
			if (lock) {
				return false;
			}
			this.lockVertexProperties();
			return true;
		}
	}

	/**
	 * Return the Agent which blocks the Vertex WhiteBoard, or null if nobody
	 * has lock the vertex.
	 * 
	 * @see #lockVertexProperties()
	 */
	public Agent getVertexPropertiesOwner() {
		return this.simulator.getVertexPropertiesOwner(this);
	}

	/**
	 * Like getProperty(), but for the current vertex. Gets a property behind a
	 * key on the vertex. If the Vertex properties are locked by another agent,
	 * wait until the lock's freeing.
	 * 
	 * @param key
	 *            Key behind which value will be find
	 * @see #lockVertexProperties()
	 */
	public Object getVertexProperty(Object key) {
		return this.simulator.getVertexProperty(this, key);
	}

	/**
	 * Sets a value on the current vertex. If the Vertex properties are locked
	 * by another agent, wait until the lock's freeing.
	 * 
	 * @param key
	 *            Key behind which storing the value
	 * @param value
	 *            Value to store on the vertex
	 * @see #lockVertexProperties()
	 */
	public void setVertexProperty(Object key, Object value) {
		this.simulator.setVertexProperty(this, key, value);
	}

	/**
	 * Just like getPropertyKeys(), this method returns a collection of all the
	 * keys but for the current vertex. If the Vertex properties are locked by
	 * another agent, wait until the lock's freeing.
	 * 
	 * @see #lockVertexProperties()
	 */
	public Set getVertexPropertyKeys() {
		return this.simulator.getVertexPropertyKeys(this);
	}

	/**
	 * Used to change the edge associated with the door on the current vertex.
	 * 
	 * @param state
	 *            The new state to affect to the edge
	 * @param door
	 *            Door from which the edge will be changed
	 */
	private void changeDoorState(int door, EdgeState state) {
		try {
			this.simulator.changeDoorState(this, door, state);
		} catch (InterruptedException e) {
			throw new SimulationAbortError(e);
		}
	}

	/**
	 * A shortcut to changeDoorState(). It marks one edges in bold.
	 * 
	 * @param door
	 *            the door on which you want to mark the edge
	 * 
	 * @see #unmarkDoor(int)
	 * @see #changeDoorState(int, EdgeState)
	 */
	public void markDoor(int door) {
		this.changeDoorState(door, new MarkedState(true));
	}

	/**
	 * Removes the mark previously done by markDoor(). The door does not need to
	 * be the same used in markDoor() ; it can be the other site of the edge.
	 * 
	 * @see #markDoor(int)
	 * @see #changeDoorState(int, EdgeState)
	 */
	public void unmarkDoor(int door) {
		this.changeDoorState(door, new MarkedState(false));
	}

	/**
	 * A shortcut to changeDoorState(). It marks one edges in bold.
	 * 
	 * @param door
	 *            the door on which you want to mark the edge as synchronized.
	 *            The edge and its two end points are drawn in red over black
	 * 
	 * @see #unsyncDoor(int)
	 * @see #markDoor(int)
	 * @see #changeDoorState(int, EdgeState)
	 */
	public void syncDoor(int door) {
		this.changeDoorState(door, new SyncState(true));
	}

	/**
	 * Removes the mark previously done by syncDoor(). The door does not need to
	 * be the same used in syncDoor() ; it can be the other end-point of the
	 * edge.
	 * 
	 * @see #syncDoor(int)
	 * @see #markDoor(int)
	 * @see #changeDoorState(int, EdgeState)
	 */
	public void unsyncDoor(int door) {
		this.changeDoorState(door, new SyncState(false));
	}

	/**
	 * Creates a new agent of the same type in the same vertex.
	 * 
	 * @see #createAgent(Class)
	 */
	public void cloneAgent() {
		this.createAgent(this.getClass());
	}

	/**
	 * Creates a new agent of the same type and puts it on one of the neighboor
	 * vertex.
	 * 
	 * @param door
	 *            Door where to send the clone.
	 * @see #createAgentAndSend(Class, int)
	 */
	public void cloneAndSend(int door) {
		this.createAgentAndSend(this.getClass(), door);
	}

	/**
	 * Creates a new agent on the current vertex.
	 * 
	 * @param agClass
	 *            Class from which to create the agent.
	 */
	public void createAgent(Class agClass) {
		this.simulator.clone(this, agClass);
	}

	/**
	 * Creates a new agent and send it to door.
	 * 
	 */
	public void createAgentAndSend(Class agClass, int door) {
		try {
			this.simulator.cloneAndSend(this, agClass, door);
		} catch (InterruptedException e) {
			throw new SimulationAbortError(e);
		}
	}

	/**
	 * Returns a printable string to differentiate the agents. This string will
	 * be printed when an agent is moving from one vertex to another. You may
	 * want to override this method to have something specific for your agents.
	 * 
	 * @return The name of the agent class, an '_' and the identity number of
	 *         the agent are returned by this method
	 */
	public String toString() {
		return this.className() + "_" + this.getIdentity();
	}

	/**
	 * Return the class name of this agent as a String
	 * 
	 * @return A String representing the class name of this agent
	 */
	public String className() {
		String class_name = this.getClass().getName();
		return class_name.substring(class_name.lastIndexOf('.') + 1);
	}

	/**
	 * Method of the Runnable interface. Launches init().
	 * 
	 * @see #init()
	 */
	public final void run() {
		try {
		  this.init();
		}
		catch (java.lang.NullPointerException e) /*une mort brutale*/
		{}
		this.death();
	};

	/**
	 * Override this method to implement your agent.
	 */
	protected abstract void init();

	/**
	 * Kills the agent. This method is automatically called and you should not
	 * call it yourself. Instead, if you want your agent to disappear, you
	 * should return from your #init() method.
	 */
	protected void death() {
		try {
			this.simulator.agentDeath(this);
		} catch (InterruptedException e) {
			throw new SimulationAbortError(e);
		}
		catch (java.lang.NullPointerException e) {}
	}

	/**
	 * Tells the simulator that a SynchronizedAgent starts a new pulse.
	 */
	protected void newPulse(int pulse) {
		try {
			this.simulator.newPulse(pulse);
		} catch (InterruptedException e) {
			throw new SimulationAbortError(e);
		}
	}

	/**
	 * Increments statistics for the key \a stat. Use this method when you want
	 * to count something and get the result at the end.
	 * 
	 * @see #incrementStat(AbstractStat, long)
	 */
	public void incrementStat(AbstractStat stat) {
		this.incrementStat(stat, 1);
	}

	/**
	 * Increments statistics by \a increment for the key \a key. Use this method
	 * when you want to count something and get the result at this end.
	 */
	public void incrementStat(AbstractStat stat, long increment) {
		this.simulator.incrementStat(stat, increment);
	}
}
