package visidia.simulation.agents;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import visidia.graph.SimpleGraph;
import visidia.graph.Vertex;
import visidia.misc.EdgeState;
import visidia.misc.Message;
import visidia.misc.StringMessage;
import visidia.rule.RelabelingSystem;
import visidia.simulation.AgentMovedEvent;
import visidia.simulation.AlgorithmEndEvent;
import visidia.simulation.EdgeStateChangeEvent;
import visidia.simulation.LabelChangeEvent;
import visidia.simulation.MessagePacket;
import visidia.simulation.MessageSendingEvent;
import visidia.simulation.NextPulseEvent;
import visidia.simulation.SimulationAbortError;
import visidia.simulation.SimulatorThreadGroup;
import visidia.simulation.agents.stats.AbstractStat;
import visidia.simulation.agents.stats.AgentCreationStat;
import visidia.simulation.agents.stats.EdgeStateStat;
import visidia.simulation.agents.stats.MoveStat;
import visidia.simulation.agents.stats.PulseStat;
import visidia.simulation.agents.stats.SleepStat;
import visidia.simulation.agents.stats.TerminatedStat;
import visidia.simulation.agents.stats.VertexWBAccessStat;
import visidia.simulation.agents.stats.VertexWBChangeStat;
import visidia.tools.Bag;
import visidia.tools.NumberGenerator;
import visidia.tools.VQueue;

/**
 * Class in charge of the simulation. Allows the communication between agents
 * and the graphic interface.
 */
public class AgentSimulator {

	/**
	 * Permet la mise à jour du graph du simultauer lorsque le graphe est
	 * modifié pendant l'exécution
	 */
	public void setGraph(SimpleGraph graph) {
		this.graph = graph;
	}

	/**
	 * Permet d'accéder au graphe de l'AgentSimulator
	 */
	public SimpleGraph getGraph() {
		return this.graph;
	}

	/**
	 * To set the priority of the thread refering to the agents.
	 */
	public static final int THREAD_PRIORITY = 1;

	/**
	 * A link to the graph on which the simulation is done
	 */
	// test: private
	public SimpleGraph graph;

	/**
	 * Cette hashtable permet de stocker pour un Sommet donné, les Agents
	 * présent à l'intérieur de celui-ci
	 */
	private Hashtable<Vertex, Collection> vertexAgentsNumber;

	// simulator threads set
	private SimulatorThreadGroup threadGroup;

	/**
	 * Hashtable which stores informations for each agents. These informations
	 * are stored in a ProcessData Object.
	 */
	private Hashtable<Agent, ProcessData> agents;

	/**
	 * If an agent want to lock the WhiteBoard of a Vertex, all informations are
	 * stored here.
	 */
	private Hashtable<Vertex, Agent> lockedVertices = new Hashtable<Vertex, Agent>();

	/**
	 * Default AgentMover used only if no AgentMover is affected to Agent.
	 */
	private AgentMover defaultAgentMover = null;

	/**
	 * evtQ is the queue of the events sent to the AgentSimulEventHandler. ackQ
	 * is the queue of ackowledgments received from it.
	 */
	private VQueue evtQ, ackQ;

	/**
	 * Generator of key associated to an event.
	 */
	private NumberGenerator numGen = new NumberGenerator();

	/**
	 * The moving monitor of the agents during the simulation
	 */
	private MovingMonitor movingMonitor;

	private Thread movingMonitorThread;

	/**
	 * Storage of statistic informations
	 */
	// private AgentStats stats;
	private Bag stats;

	/**
	 * Constructor. Creates a new AgentSimulator and affect its the specified
	 * graph, the specified event queue, the specified acknowlegdement queue and
	 * a default agents Hashtable.
	 */
	public AgentSimulator(SimpleGraph netGraph, Vector agentsRules,
			VQueue evtVQ, VQueue ackVQ) {
		this(netGraph, new Hashtable(), agentsRules, evtVQ, ackVQ);
	}

	/**
	 * Constructor. Creates a new AgentSimulator and affects it the specified
	 * graph, the specified event queue, the specified acknowledgment queue and
	 * the specified agents Hashtable.
	 */
	public AgentSimulator(SimpleGraph netGraph, Hashtable defaultAgentValues,
			Vector agentsRules, VQueue evtVQ, VQueue ackVQ) {

		this.graph = netGraph;
		this.stats = new Bag();

		this.threadGroup = new SimulatorThreadGroup("simulator");
		this.fillAgentsTable(this.graph, defaultAgentValues, agentsRules);
		this.evtQ = evtVQ;
		this.ackQ = ackVQ;

		this.movingMonitor = new MovingMonitor(this.ackQ);
		this.movingMonitorThread = new Thread(this.movingMonitor);
		this.movingMonitorThread.start();
	}

	/**
	 * Return the collection af agents which are on the vertex that have the
	 * vertexId as the id.
	 * 
	 * @param vertexId
	 *            The vertex id.
	 */
	public Collection getAgentsVertexCollection(int vertexId) {
		return this.vertexAgentsNumber.get(this.graph.vertex(new Integer(
				vertexId)));
	}

	/**
	 * Returns a Set of all the agents.
	 */
	public Set<Agent> getAllAgents() {
		return this.agents.keySet();
	}

	/**
	 * Adds a specified agent to a specified vertex. Returns the new number of
	 * agents on the vertex.
	 * 
	 * @see #removeAgentFromAgent(Vertex, Agent)
	 */
	private int addAgentToVertex(Vertex vertex, Agent ag) {
		synchronized (this.vertexAgentsNumber) {
			if (this.vertexAgentsNumber.get(vertex) != null) {
				this.vertexAgentsNumber.get(vertex).add(ag);
			} else {
				Collection<Agent> colOfAgents = new HashSet();
				colOfAgents.add(ag);
				this.vertexAgentsNumber.put(vertex, colOfAgents);
			}
			return this.vertexAgentsNumber.get(vertex).size();
		}
	}

	/**
	 * Removes a specified agent from a specified vertex. Returns the new number
	 * of agents on the vertex.
	 * 
	 * @see #addAgentToVertex(Vertex, Agent)
	 */
	private int removeAgentFromVertex(Vertex vertex, Agent ag) {
		synchronized (this.vertexAgentsNumber) {
			this.vertexAgentsNumber.get(vertex).remove(ag);
			if (this.vertexAgentsNumber.get(vertex).isEmpty()) {
				this.vertexAgentsNumber.remove(vertex);
				return 0;
			} else {
				return this.vertexAgentsNumber.get(vertex).size();
			}
		}
	}

	/**
	 * Fills the agent table agents given a SimpleGraph and a default values
	 * Hashtable
	 * 
	 * @param graph
	 *            The graph on which the simulation is done
	 * @param defaultAgentValues
	 *            The default values with which the agents are created
	 */
	private void fillAgentsTable(SimpleGraph graph,
			Hashtable defaultAgentValues, Vector agentsRules) {
		Enumeration vertices;

		this.agents = new Hashtable<Agent, ProcessData>();
		this.vertexAgentsNumber = new Hashtable<Vertex, Collection>();

		vertices = graph.vertices();

		while (vertices.hasMoreElements()) {
			Vertex vertex = (Vertex) vertices.nextElement();
			Collection agentsNames = vertex.getAgentsNames();

			if (agentsNames == null) {
				continue;
			}

			Iterator it = agentsNames.iterator();

			while (it.hasNext()) {
				String agentName = (String) it.next();

				if (agentName != null) {
					this.createAgent(agentName, vertex, defaultAgentValues,
							agentsRules);
				}
			}

			vertex.clearAgentNames();
		}
	}

	/**
	 * In charge of the creation of agents. This method gives a name to the
	 * agent created and creat it on a specified vertex.
	 * 
	 * @see #createAgent(Class, Vertex, Hashtable).
	 */
	// test: private
	public Agent createAgent(String agentName, Vertex vertex,
			Hashtable defaultAgentValues, Vector agentsRules) {
		Agent agent;
		String completName;
		boolean mode_rules = false;
		if (agentName.startsWith("Agents Rules")) {
			mode_rules = true;
			completName = new String("visidia.simulation.agents.AgentRules");
		} else {
			completName = new String("visidia.agents." + agentName);
		}

		try {
			agent = this.createAgent(Class.forName(completName), vertex,
					defaultAgentValues);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

		if (mode_rules) {
			int position = agentName.indexOf('_');
			String number = agentName.substring(position + 1);
			int index = new Integer(number).intValue();
			RelabelingSystem rSys = (RelabelingSystem) agentsRules.get(index);
			((AbstractAgentsRules) agent).setRule(rSys);
		}
		return agent;
	}

	/**
	 * In charge of the creation of agent given the agent class. This method
	 * creates a new ProcessData where the informations about the agent are
	 * stored.
	 */
	private Agent createAgent(Class<?> agentClass, Vertex vertex,
			Hashtable defaultAgentValues) {
		Agent ag;

		try {

			ProcessData data = new ProcessData();

			ag = (Agent) agentClass.getConstructor().newInstance();
			ag.setSimulator(this);
			ag.setWhiteBoard(defaultAgentValues);
			data.vertex = vertex;
			data.agent = ag;
			this.agents.put(ag, data);

			this.addAgentToVertex(vertex, ag);

		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

		this.stats.add(new AgentCreationStat(ag.getClass()));

		return ag;
	}

	/**
	 * Handles agents' death. It removes the agents from the Hashtable. When the
	 * Hashtable is empty it is the end of the algorithm.
	 */
	public void agentDeath(Agent ag) throws InterruptedException {

		ProcessData data = this.agents.get(ag);
		Vertex vertex = data.vertex;
		Long key = new Long(this.numGen.alloc());

		int nbr = this.removeAgentFromVertex(vertex, ag);

		this.agents.remove(ag);

		this.evtQ.put(new AgentMovedEvent(key, vertex.identity(), new Integer(
				nbr)));
		this.movingMonitor.waitForAnswer(key);

		this.stats.add(new TerminatedStat(ag.getClass()));

		/* Detecting the end of the algorithm */
		if (this.agents.isEmpty()) {
			this.evtQ.put(new AlgorithmEndEvent(this.numGen.alloc()));
		}
	}

	/**
	 * Moves an Agent to a specified door.
	 * 
	 * @param ag
	 *            the Agent you want to move
	 * @param door
	 *            the door to which you want to move the Agent
	 */
	public void moveAgentTo(Agent ag, int door) throws InterruptedException {
		ProcessData data = this.agents.get(ag);
		Vertex vertexFrom, vertexTo;
		Message msg;
		MessagePacket msgPacket;

		if ((door < 0) || (door >= this.getArity(ag))) {
			throw new IllegalArgumentException("This door doesn't exist !");
		}

		vertexFrom = data.vertex;
		vertexTo = vertexFrom.neighbour(door);

		msg = new StringMessage(ag.toString());
		msgPacket = new MessagePacket(vertexFrom.identity(), door, vertexTo
				.identity(), msg);

		this.pushMessageSendingEvent(msgPacket, ag);

		data.vertex = vertexTo;
		data.lastVertexSeen = vertexFrom;

		this.stats.add(new MoveStat(ag.getClass()));
	}

	/**
	 * Changes the state of the edge associated with the door on the vertex
	 * where the Agent is.
	 * 
	 * @param ag
	 * @param door
	 * @param state
	 */
	public void changeDoorState(Agent ag, int door, EdgeState state)
			throws InterruptedException {

		Vertex vertexFrom, vertexTo;
		Long key = new Long(this.numGen.alloc());
		EdgeStateChangeEvent event;

		vertexFrom = this.getVertexFor(ag);
		vertexTo = vertexFrom.neighbour(door);

		event = new EdgeStateChangeEvent(key, vertexFrom.identity(), vertexTo
				.identity(), state);
		this.evtQ.put(event);
		this.movingMonitor.waitForAnswer(key);
		this.stats.add(new EdgeStateStat(ag.getClass()));
	}

	/**
	 * Returns the door from which an agent comes.
	 * 
	 * @param ag
	 *            the agent you want information about.
	 */
	public int entryDoor(Agent ag) {
		if (this.getLastVertexSeen(ag) == null) {
			throw new IllegalStateException();
		}
		return this.getVertexFor(ag).indexOf(
				this.getLastVertexSeen(ag).identity());
	}

	/**
	 * Sends an event to tell graphical interface that a new pulse is starting
	 * 
	 * @param pulse
	 *            the current pulse
	 */
	public void newPulse(int pulse) throws InterruptedException {

		Long key = new Long(this.numGen.alloc());
		NextPulseEvent event = new NextPulseEvent(key, pulse);

		this.evtQ.put(event);
		this.stats.add(new PulseStat());
	}

	/**
	 * Return the Agent which blocks the Vertex WhiteBoard, or null if nobody
	 * has lock the vertex.
	 * 
	 * @param v
	 *            the vertex you want information about
	 * @see #lockVertexProperties(Agent)
	 */
	public Agent getVertexPropertiesOwner(Vertex v) {
		return this.lockedVertices.get(v);
	}

	/**
	 * Return the Agent which blocks the Vertex WhiteBoard, or null if nobody
	 * has lock the vertex.
	 * 
	 * @param ag
	 *            the Agent which wants to know who has locked the Vertex
	 *            Properties
	 * @see #lockVertexProperties(Agent)
	 */
	public Agent getVertexPropertiesOwner(Agent ag) {
		return this.getVertexPropertiesOwner(this.getVertexFor(ag));
	}

	/**
	 * Return true if the Vertex is locked, otherwise false
	 * 
	 * @param v
	 *            the Vertex you want information about
	 * @see #lockVertexProperties(Agent)
	 */
	public boolean vertexPropertiesLocked(Vertex v) {
		if (this.getVertexPropertiesOwner(v) == null) {
			return false;
		}
		return true;
	}

	/**
	 * Return true if the Vertex is locked, otherwise false
	 * 
	 * @param ag
	 *            the Agent which wants to know if it Vertex properties are
	 *            locked
	 * @see #lockVertexProperties(Agent)
	 */
	public boolean vertexPropertiesLocked(Agent ag) {
		return this.vertexPropertiesLocked(this.getVertexFor(ag));
	}

	/**
	 * Lock the Vertex WhiteBoard where the Agent is. If already locked, wait
	 * until the owner unlocks it
	 * 
	 * @param ag
	 *            the agent which wants to lock it Vertex
	 * @exception IllegalStateException
	 *                if Vertex properties are already locked by the agent given
	 *                in parameter
	 * @see #unlockVertexProperties(Agent)
	 */
	public void lockVertexProperties(Agent ag) {
		Vertex actualVertex = this.getVertexFor(ag);

		if (this.getVertexPropertiesOwner(actualVertex) == ag) {
			throw new IllegalStateException("Try to lock a WhiteBoard"
					+ "already locked by me");
		} else {
			synchronized (actualVertex) {
				while (this.vertexPropertiesLocked(actualVertex)) {

					try {
						actualVertex.wait();
					} catch (InterruptedException e) {
						throw new SimulationAbortError(e);
					}
				}
				this.lockedVertices.put(actualVertex, ag);
			}
		}
	}

	/**
	 * Unlock the Vertex WhiteBoard where the Agent is.
	 * 
	 * @param ag
	 *            the agent which wants to unlock it Vertex
	 * @exception IllegalStateException
	 *                if the WhiteBoard is unlock, or if the Agent is not the
	 *                owner of the lock
	 * @see #lockVertexProperties(Agent)
	 */
	public void unlockVertexProperties(Agent ag) throws IllegalStateException {
		Vertex actualVertex = this.getVertexFor(ag);

		synchronized (actualVertex) {
			if (this.vertexPropertiesLocked(actualVertex)
					&& (this.getVertexPropertiesOwner(actualVertex) == ag)) {
				this.lockedVertices.remove(actualVertex);
				actualVertex.notifyAll();
			} else {
				throw new IllegalStateException("Try to unlock a WhiteBoard "
						+ "that doesn't belong to us");
			}
		}
	}

	/**
	 * Accesses the WhiteBoard of the vertex to get a value. If the Vertex
	 * WhiteBoard is locked by another Agent, wait until the lock's freeing
	 * 
	 * @param ag
	 *            the agent which wants to access the WithBoard.
	 * @param key
	 *            key behind which found the value.
	 * @see #setVertexProperty(Agent, Object, Object)
	 * @see #lockVertexProperties(Agent)
	 */
	public Object getVertexProperty(Agent ag, Object key) {
		Vertex vertex = this.getVertexFor(ag);

		synchronized (vertex) {
			while (this.vertexPropertiesLocked(vertex)
					&& (this.getVertexPropertiesOwner(vertex) != ag)) {
				try {
					vertex.wait();
				} catch (InterruptedException e) {
					throw new SimulationAbortError(e);
				}
			}
			this.stats.add(new VertexWBAccessStat(ag.getClass()));

			return vertex.getProperty(key);
		}
	}

	/**
	 * Accesses the WhiteBoard of the vertex to put a value. If the Vertex
	 * WhiteBoard is locked by another Agent, wait until the lock's freeing
	 * 
	 * @param ag
	 *            the agent that stores the information
	 * @param key
	 *            Key on which the value must be stored
	 * @param value
	 *            value that must be stored.
	 * @see #getVertexProperty(Agent, Object)
	 * @see #lockVertexProperties(Agent)
	 */
	public void setVertexProperty(Agent ag, Object key, Object value) {
		Vertex vertex = this.getVertexFor(ag);

		synchronized (vertex) {
			while (this.vertexPropertiesLocked(vertex)
					&& (this.getVertexPropertiesOwner(vertex) != ag)) {
				try {
					vertex.wait();
				} catch (InterruptedException e) {
					throw new SimulationAbortError(e);
				}
			}
			this.stats.add(new VertexWBChangeStat(ag.getClass()));

			vertex.setProperty(key, value);

			if (key.equals("label")) {
				Long num = new Long(this.numGen.alloc());
				LabelChangeEvent lce;
				lce = new LabelChangeEvent(num, vertex.identity(),
						(String) value);
				try {
					this.evtQ.put(lce);
				} catch (InterruptedException e) {
					throw new SimulationAbortError(e);
				}
			}
		}
	}

	/**
	 * This method returns a collection of all the keys of a the current vertex
	 * for a given agent. If the Vertex WhiteBoard is locked by another Agent,
	 * wait until the lock's freeing
	 * 
	 * @param ag
	 *            agent you want information for.
	 * @see #lockVertexProperties(Agent)
	 */
	public Set getVertexPropertyKeys(Agent ag) {
		Vertex actualVertex = this.getVertexFor(ag);

		synchronized (actualVertex) {
			while (this.vertexPropertiesLocked(actualVertex)
					&& (this.getVertexPropertiesOwner(actualVertex) != ag)) {
				try {
					actualVertex.wait();
				} catch (InterruptedException e) {
					throw new SimulationAbortError(e);
				}
			}
			return actualVertex.getPropertyKeys();
		}
	}

	/**
	 * This method is in charge of the beginning of the simulation. It is called
	 * when the start button is pressed. It creates all the agents' threads.
	 */
	public void startSimulation() {
		Enumeration enumAgents = this.agents.elements();

		while (enumAgents.hasMoreElements()) {
			ProcessData data = (ProcessData) enumAgents.nextElement();
			this.createThreadFor(data.agent).start();
		}
	}

	/**
	 * This method is called to abort the simulation. It interrupts all the
	 * threads and clears all data related to their storage.
	 */
	public void abortSimulation() {

		// Stop the moving monitor
		this.movingMonitor.abortAck();

		while (this.movingMonitorThread.isAlive()) {
			this.movingMonitorThread.interrupt();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// throw new SimulationAbortError(e);
			}
		}

		while (this.threadGroup.activeCount() > 0) {
			this.threadGroup.interrupt();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				throw new SimulationAbortError(e);
			}
		}

		this.agents.clear();
		SynchronizedAgent.clear();
		this.vertexAgentsNumber.clear();
	}

	/**
	 * Returns the degree of the current vertex for the specified agent.
	 * 
	 * @param ag
	 *            agent you want information for.
	 */
	public int getArity(Agent ag) {
		return this.getVertexFor(ag).degree();
	}

	/**
	 * Makes the specified agent fall asleep for a given amount of milliseconds.
	 * 
	 * @param ag
	 *            Agent given to fall asleep
	 * @param millis
	 *            Milliseconds to sleep
	 */
	public void sleep(Agent ag, long millis) throws InterruptedException {
		Thread.sleep(millis);
		this.stats.add(new SleepStat(ag.getClass()), millis);

	}

	/**
	 * Returns the number of vertices of the graph on which the simulation is
	 * done.
	 */
	public int getNetSize() {
		return this.graph.size();
	}

	/**
	 * For a given agent returns the identity of the vertex it is on.
	 * 
	 * @param ag
	 *            The agent you want infromation on.
	 */
	public int getVertexIdentity(Agent ag) {
		return this.getVertexFor(ag).identity().intValue();
	}

	/**
	 * Sets the agentMover of the simulation. This method is used to use the
	 * specified agentMover that you can create yourself.
	 * 
	 * @see AgentMover
	 */
	public void setDefaultAgentMover(AgentMover am) {
		this.defaultAgentMover = am;
	}

	public boolean hasDefaultAgentMover() {
		return this.defaultAgentMover != null;
	}

	public void incrementStat(AbstractStat stat, long increment) {
		this.stats.add(stat, increment);
	}

	public Bag getStats() {
		return this.stats;
	}

	public void clone(Agent ag, Class agClass) {
		Agent ag2;

		ag2 = this.createAgent(agClass, this.getVertexFor(ag), new Hashtable());
		this.createThreadFor(ag2).start();
	}

	public void cloneAndSend(Agent ag, Class agClass, int door)
			throws InterruptedException {

		Agent ag2;
		Vertex vertexFrom, vertexTo;
		Message msg;
		// MessagePacket msgPacket;

		vertexFrom = this.getVertexFor(ag);
		vertexTo = vertexFrom.neighbour(door);
		msg = new StringMessage("Sent clone of " + ag.toString());
		/* msgPacket = */new MessagePacket(vertexFrom.identity(), door,
				vertexTo.identity(), msg);

		ag2 = this.createAgent(agClass, vertexFrom, new Hashtable());

		this.moveAgentTo(ag2, door);

		this.createThreadFor(ag2).start();
	}

	private void pushMessageSendingEvent(MessagePacket mesgPacket, Agent ag)
			throws InterruptedException {

		Long key = new Long(this.numGen.alloc());
		Long keyDep = new Long(this.numGen.alloc());
		Long keyArr = new Long(this.numGen.alloc());
		MessageSendingEvent mse;
		AgentMovedEvent dep, arr;
		Vertex vertexTo, vertexFrom;

		vertexFrom = this.graph.vertex(mesgPacket.sender());
		vertexTo = this.graph.vertex(mesgPacket.receiver());

		mse = new MessageSendingEvent(key, mesgPacket.message(), mesgPacket
				.sender(), mesgPacket.receiver());

		int nbr = this.removeAgentFromVertex(vertexFrom, ag);

		dep = new AgentMovedEvent(keyDep, mesgPacket.sender(), new Integer(nbr));

		this.evtQ.put(dep);
		this.movingMonitor.waitForAnswer(keyDep);

		this.evtQ.put(mse);
		this.movingMonitor.waitForAnswer(key);

		nbr = this.addAgentToVertex(vertexTo, ag);

		arr = new AgentMovedEvent(keyArr, mesgPacket.receiver(), new Integer(
				nbr));

		this.evtQ.put(arr);
		this.movingMonitor.waitForAnswer(keyArr);
	}

	// test :private
	public Thread createThreadFor(Agent ag) {
		ProcessData data = this.getDataFor(ag);

		data.thread = new Thread(this.threadGroup, ag);
		data.thread.setPriority(AgentSimulator.THREAD_PRIORITY);

		return data.thread;
	}

	private Thread getThreadFor(Agent ag) {
		return this.getDataFor(ag).thread;
	}

	private Vertex getVertexFor(Agent ag) {
		return this.getDataFor(ag).vertex;
	}

	private Vertex getLastVertexSeen(Agent ag) {
		return this.getDataFor(ag).lastVertexSeen;
	}

	private ProcessData getDataFor(Agent ag) {
		return this.agents.get(ag);
	}

	private class ProcessData {
		public Agent agent;

		public Vertex vertex;

		public Vertex lastVertexSeen;

		public Thread thread;
	}
}
