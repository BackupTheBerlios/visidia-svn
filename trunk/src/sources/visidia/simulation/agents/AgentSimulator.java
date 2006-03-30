package visidia.simulation.agents;

import visidia.graph.*;
import visidia.agents.*;
import visidia.tools.VQueue;
import visidia.visidiassert.VisidiaAssertion;

import visidia.simulation.AlgorithmEndEvent;
import visidia.simulation.MessageSendingEvent;
import visidia.simulation.MessagePacket;
import visidia.simulation.EdgeStateChangeEvent;
import visidia.simulation.AgentMovedEvent;

import visidia.simulation.SimulationAbortError;
import visidia.simulation.SimulatorThreadGroup;

import visidia.tools.NumberGenerator;
import visidia.misc.Message;
import visidia.misc.StringMessage;
import visidia.misc.EdgeState;

import visidia.rule.RelabelingSystem;

import java.io.*;
import java.util.Hashtable;
import java.util.Set;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Collection;
import java.util.HashSet;

import java.util.Vector;

/**
 * Class  in charge  of the  simulation. Allows the communication
 * between agents and the graphic interface.
 **/
public class AgentSimulator {

    /**
     * To set the priority of the thread refering to the agents.
     */    
    public static final int THREAD_PRIORITY = 1;

    
    /**
     * A link to the graph on which the simulation is done 
     */
    private SimpleGraph graph;

    // simulator threads set
    private SimulatorThreadGroup threadGroup;

    /**
     * Hashtable  which stores  informations for  each  agents.  These
     * informations are stored in a ProcessData Object.
     */
    private Hashtable agents;
    
    private Hashtable<Vertex,Collection> vertexAgentsNumber;

    /**
     * If an agent want to lock the WhiteBoard of a Vertex,
     * all informations are stored here. 
     */
    private Hashtable<Vertex,Agent> lockedVertices = new Hashtable();

    /**
     * Default AgentMover used only if no AgentMover is affected to
     * Agent. 
     */
    private AgentMover defaultAgentMover = null;
    
    /**
     * evtQ is the queue of the events sent to the AgentSimulEventHandler.
     * ackQ is the queue of ackowledgments received from it.
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
    private AgentStats stats;

    /**
     * Constructor.  Creates  a new AgentSimulator and  affect its the
     * specified  graph,  the  specified  event queue,  the  specified
     * acknowlegdement queue and a default agents Hashtable.
     */
    public AgentSimulator(SimpleGraph netGraph, Vector agentsRules,
			  VQueue evtVQ, VQueue ackVQ) {
        this(netGraph, new Hashtable(), new Vector(), evtVQ, ackVQ);
    }

    /**
     * Constructor. Creates  a new  AgentSimulator and affects  it the
     * specified  graph,  the  specified  event queue,  the  specified
     * acknowledgment queue and the specified agents Hashtable.
     */ 
    public AgentSimulator(SimpleGraph netGraph, 
                          Hashtable defaultAgentValues,
			  Vector agentsRules,
                          VQueue evtVQ, VQueue ackVQ) {

	graph = (SimpleGraph) netGraph;
        stats = new AgentStats();

	threadGroup = new SimulatorThreadGroup("simulator");
	fillAgentsTable(graph, defaultAgentValues, agentsRules);
        this.evtQ = evtVQ;
        this.ackQ = ackVQ;

        movingMonitor = new MovingMonitor(ackQ);
        movingMonitorThread = new Thread(movingMonitor);
        movingMonitorThread.start();
    }

    /**
     * Returns the number of agents on the specified vertex.
     * 
     * @param vertex The vertex on which information is given.
     */
    private int getAgentsVertexNumber(Vertex vertex){
	return vertexAgentsNumber.get(vertex).size();
    }

    public Hashtable<Vertex, Collection> getAgentPositions() {
	return vertexAgentsNumber;
    }

    /**
     * Returns a Set of all the agents.
     */
    public Set<Agent> getAllAgents() {
        return agents.keySet();
    }

    /**
     * Adds a specified  agent to a specified vertex.  Returns the new
     * number of agents on the vertex.
     *
     * @see #removeAgentFromAgent(Vertex, Agent)
     */
    private int addAgentToVertex(Vertex vertex, Agent ag){
	if( vertexAgentsNumber.get(vertex) != null)
	    vertexAgentsNumber.get(vertex).add(ag);
	else{
	    try{
		Collection<Agent> colOfAgents  = new HashSet();
		colOfAgents.add(ag);
		vertexAgentsNumber.put(vertex,colOfAgents);
		// System.out.println("Ajout d'un agent sur le sommet"+vertex.identity().intValue());
	    } 
	    catch(IllegalArgumentException iae){
		System.out.println("Exception "+iae.getMessage());
	    }
	}
	return vertexAgentsNumber.get(vertex).size();
    }
    
    /**
     * Removes a specified agent  from a specified vertex. Returns the
     * new number of agents on the vertex.
     *
     * @see #addAgentToVertex(Vertex, Agent)
     */
    private int removeAgentFromVertex(Vertex vertex, Agent ag){
	if( vertexAgentsNumber.get(vertex) != null)
	    try{
		System.out.println("remove Agent From vertex :" +vertex.
				    identity().intValue());
		vertexAgentsNumber.get(vertex).remove(ag);
	    }
	    catch(NullPointerException npe){
		System.out.println("Exception "+npe.getMessage());
	    }
	
	return vertexAgentsNumber.get(vertex).size();
    }

    /**
     * Fills the agent table agents  given a SimpleGraph and a default
     * values Hashtable
     *
     * @param graph The graph on which the simulation is done
     * @param  defaultAgentValues The  default values  with  which the
     * agents are created
     */
    private void fillAgentsTable(SimpleGraph graph, 
                                 Hashtable defaultAgentValues,
				 Vector agentsRules) {
        Enumeration vertices;

        agents = new Hashtable();
	vertexAgentsNumber = new Hashtable();
	
        vertices = graph.vertices();

        while (vertices.hasMoreElements()) {
            Vertex vertex = (Vertex) vertices.nextElement();
	    Collection agentsNames = vertex.getAgentsNames();

	    Collection<Agent> colOfAgents  = new HashSet();
	    vertexAgentsNumber.put(vertex, colOfAgents);
	    
            if(agentsNames == null){
		continue;
	    }
	    
            Iterator it = agentsNames.iterator();

	    while(it.hasNext()) {
		String agentName = (String)it.next();
		
		if (agentName != null) {
		    createAgent(agentName, vertex, defaultAgentValues, agentsRules);
		}
	    }

            vertex.clearAgentNames();
        }
    }

    /**
     *
     *
     */
    private Agent createAgent(String agentName, Vertex vertex,
                              Hashtable defaultAgentValues,
			      Vector agentsRules) {
	Agent agent;
	String completName;
	boolean mode_rules = false;
	if(agentName.startsWith("Agents Rules")){
	    mode_rules = true;
	    completName = new String("visidia.simulation.agents.AgentRules");
	}
	else
	    completName = new String("visidia.agents." + agentName);

        try {
	    agent = createAgent(Class.forName(completName), vertex,
                               defaultAgentValues);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

	if (mode_rules) {
	    int position = agentName.indexOf('_');
	    String number = agentName.substring(position + 1);
	    int index = new Integer(number).intValue();
	    RelabelingSystem rSys = (RelabelingSystem) agentsRules.get(index);
	    ((AbstractAgentsRules)agent).setRule(rSys);
	}
	return agent;
    }

    private Agent createAgent(Class agentClass, Vertex vertex,
                              Hashtable defaultAgentValues) {
        Agent ag;

        try {

            ProcessData data = new ProcessData();

            ag = (Agent) agentClass.getConstructor().newInstance();
            ag.setSimulator(this);
            ag.setWhiteBoard(defaultAgentValues);
	    data.vertex = vertex;
            data.agent = ag;
            agents.put(ag, data);

	    int nbr = addAgentToVertex(vertex, ag);

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        stats.incrementStat("Created agents");

        return ag;
    }

    /**
     * Handles  agents'  death.   It   removes  the  agents  from  the
     * Hashtable. When  the Hashtable  is empty it  is the end  of the
     * algorithm.
     */
    public void agentDeath(Agent ag) throws InterruptedException {
	
	ProcessData data = (ProcessData) agents.get(ag);
	Vertex vertex = data.vertex;
	Long key = new Long(numGen.alloc());

	int nbr = removeAgentFromVertex(vertex,ag);
	
	agents.remove(ag);

	evtQ.put(new AgentMovedEvent(key,
				     vertex.identity(),
				     new Integer(nbr)));
	movingMonitor.waitForAnswer(key);

        System.out.println("Algorithm Terminated");
        stats.incrementStat("Terminated algorithms");

	/* Detecting the end of the algorithm */
	if(agents.isEmpty()) {
            evtQ.put(new AlgorithmEndEvent(numGen.alloc()));
        }
    }
    
    /**
     * Moves an Agent to a specified door.
     *
     * @param ag the Agent you want to move
     * @param door the door to which you want to move the Agent
     */
    public void moveAgentTo(Agent ag, int door) throws InterruptedException {
        ProcessData data = (ProcessData) agents.get(ag);
        Vertex vertexFrom, vertexTo;
        Message msg;
        MessagePacket msgPacket;
        
        
        if( door < 0 || door >= getArity(ag))
            throw new IllegalArgumentException("This door doesn't exist !");

        vertexFrom = data.vertex;
        vertexTo = vertexFrom.neighbour(door);

        msg = new StringMessage(ag.toString());
        msgPacket = new MessagePacket(vertexFrom.identity(), door, 
                                      vertexTo.identity(), msg);
	
	pushMessageSendingEvent(msgPacket,ag);
	

	data.vertex = vertexTo;
	data.lastVertexSeen = vertexFrom;

        stats.incrementStat("Moves");
    }

    /**
     * Changes the state  of the edge associated with  the door on the
     * vertex where the Agent is.
     *
     * @param ag
     * @param door
     * @param state
     */
    public void changeDoorState(Agent ag, int door, EdgeState state) 
        throws InterruptedException {

        Vertex vertexFrom, vertexTo;
        Long key = new Long(numGen.alloc());
        EdgeStateChangeEvent event;

        vertexFrom = getVertexFor(ag);
        vertexTo = vertexFrom.neighbour(door);

        event = new EdgeStateChangeEvent(key, 
                                         vertexFrom.identity(),
                                         vertexTo.identity(),
                                         state);
        evtQ.put(event);
        movingMonitor.waitForAnswer(key);
        stats.incrementStat("Edge state changes");
    }

    /**
     * Returns the door from which an agent comes.
     * 
     * @param ag the agent you want information about.
     */
    public int entryDoor(Agent ag) {
	if (getLastVertexSeen(ag) == null)
            throw new IllegalStateException();
        return getVertexFor(ag).indexOf(getLastVertexSeen(ag).identity());
    }

    /**
     * Return the Agent which blocks the Vertex WhiteBoard, or null if
     * nobody has lock the vertex.
     *
     * @param v the vertex you want information about
     * @see #lockVertexProperties(Agent)
     */
    public Agent getVertexPropertiesOwner(Vertex v) {
	return lockedVertices.get(v);
    }

    /**
     * Return the Agent which blocks the Vertex WhiteBoard, or null if
     * nobody has lock the vertex.
     *
     * @param ag the Agent which wants to know who has locked the
     * Vertex Properties
     * @see #lockVertexProperties(Agent)
     */
    public Agent getVertexPropertiesOwner(Agent ag) {
	return getVertexPropertiesOwner(getVertexFor(ag));
    }


    /**
     * Return true if the Vertex is locked, otherwise false
     * 
     * @param v the Vertex you want information about
     * @see #lockVertexProperties(Agent)
     */
    public boolean vertexPropertiesLocked(Vertex v) {
	if(getVertexPropertiesOwner(v) == null)
	    return false;
	return true;
    }


    /**
     * Return true if the Vertex is locked, otherwise false
     * 
     * @param ag the Agent which wants to know if it Vertex
     * properties are locked
     * @see #lockVertexProperties(Agent)
     */
    public boolean vertexPropertiesLocked(Agent ag) {
	return vertexPropertiesLocked(getVertexFor(ag));
    }
    
    
    /**
     * Lock the Vertex WhiteBoard where the Agent is.
     * If already locked, wait until the owner unlocks it
     * 
     * @param ag the agent which wants to lock it Vertex
     * @exception IllegalStateException if Vertex properties are
     * already locked by the agent given in parameter
     * @see #unlockVertexProperties(Agent)
     */
    public void lockVertexProperties(Agent ag) {
	Vertex actualVertex = getVertexFor(ag);

	if(getVertexPropertiesOwner(actualVertex) == ag)
	    throw new  IllegalStateException("Try to lock a WhiteBoard"
					     + "already locked by me"); 

	else {
	    synchronized(actualVertex) {
		while(vertexPropertiesLocked(actualVertex)) {
		    
		    try {
			actualVertex.wait();
		    } catch(InterruptedException e) {
			throw new SimulationAbortError(e);
		    }
		}
		lockedVertices.put(actualVertex, ag);
	    }
	}
    }

    /**
     * Unlock the Vertex WhiteBoard  where the Agent is.
     *
     * @param ag the agent which wants to unlock it Vertex
     * @exception IllegalStateException if the WhiteBoard is
     * unlock, or if the Agent is not the owner of the lock
     * @see #lockVertexProperties(Agent)
     */
    public void unlockVertexProperties(Agent ag) 
	throws IllegalStateException {
	Vertex actualVertex = getVertexFor(ag);

	synchronized(actualVertex) {
	    if(vertexPropertiesLocked(actualVertex)
	       && (getVertexPropertiesOwner(actualVertex) == ag)) {
		lockedVertices.remove(actualVertex);
		actualVertex.notifyAll();
	    }
	    else
		throw new IllegalStateException("Try to unlock a WhiteBoard "
						+ "that doesn't belong to us");
	}
    }

    /**
     * Accesses the WhiteBoard of the vertex to get a value.
     * If the Vertex WhiteBoard is locked by another Agent, wait
     * until the lock's freeing 
     *
     * @param ag the agent which wants to access the WithBoard.
     * @param key key behind which found the value.
     * @see #setVertexProperty(Agent, Object, Object)
     * @see #lockVertexProperties(Agent)
     */
    public Object getVertexProperty(Agent ag, Object key) {
	Vertex actualVertex = getVertexFor(ag);

	synchronized(actualVertex) {
	    while(vertexPropertiesLocked(actualVertex) 
		  && (getVertexPropertiesOwner(actualVertex) != ag)) {
		try {
		    actualVertex.wait();
		} catch(InterruptedException e) {
		    throw new SimulationAbortError(e);
		}
	    }
	    stats.incrementStat("Accesses to vertices WhiteBoard");
	    return actualVertex.getProperty(key);
	}
    }

    /**
     * Accesses the WhiteBoard of the vertex to put a value.
     * If the Vertex WhiteBoard is locked by another Agent, wait
     * until the lock's freeing
     *
     * @param ag the agent that stores the information
     * @param key Key on which the value must be stored
     * @param value value that must be stored.
     * @see #getVertexProperty(Agent, Object)
     * @see #lockVertexProperties(Agent)
     */
    public void setVertexProperty(Agent ag, Object key, Object value) {
	Vertex actualVertex = getVertexFor(ag);

	synchronized(actualVertex) {
	    while(vertexPropertiesLocked(actualVertex) 
		  && (getVertexPropertiesOwner(actualVertex) != ag)) {
		try {
		    actualVertex.wait();
		} catch(InterruptedException e) {
		    throw new SimulationAbortError(e);
		}
	    }	
	    stats.incrementStat("Changes in vertices WhiteBoard");
	    actualVertex.setProperty(key, value);
	}
    }


    /**
     * This  method returns  a collection  of all  the keys  of  a the
     * current vertex for a given agent.
     * If the Vertex WhiteBoard is locked by another Agent, wait
     * until the lock's freeing
     *
     * @param ag agent you want information for.
     * @see #lockVertexProperties(Agent)
     */
    public Set getVertexPropertyKeys(Agent ag) {
	Vertex actualVertex = getVertexFor(ag);

	synchronized(actualVertex) {
	    while(vertexPropertiesLocked(actualVertex) 
		  && (getVertexPropertiesOwner(actualVertex) != ag)) {
		try {
		    actualVertex.wait();
		} catch(InterruptedException e) {
		    throw new SimulationAbortError(e);
		}
	    }
        return actualVertex.getPropertyKeys();
	}
    }

    /**
     * This method  is in charge  of the beginning of  the simulation.
     * It is called when the  start button is pressed.  It creates all
     * the agents' threads.
     */
    public void startSimulation(){
        Enumeration enumAgents = agents.elements();

        while (enumAgents.hasMoreElements()) {
            ProcessData data = (ProcessData) enumAgents.nextElement();
            createThreadFor(data.agent).start();
        }
    }

    /**
     * This method  is called to abort the  simulation.  It interrupts
     * all the threads and clears all data related to their storage.
     */
    public void abortSimulation() {

        while(movingMonitorThread.isAlive()) {
            movingMonitorThread.interrupt();
            try {
                Thread.currentThread().sleep(50);
            } catch (InterruptedException e) {
                throw new SimulationAbortError(e);
            }
        }

        while(threadGroup.activeCount() > 0) {
            threadGroup.interrupt();
            try {
                Thread.currentThread().sleep(50);
            } catch (InterruptedException e) {
                throw new SimulationAbortError(e);
            }
        }

        agents.clear();
        SynchronizedAgent.clear();
	vertexAgentsNumber.clear();
    }

    /**
     * Returns  the degree  of the  current vertex  for  the specified
     * agent.
     * 
     * @param ag agent you want information for. 
     */
    public int getArity(Agent ag) {
        return getVertexFor(ag).degree();
    }

    /**
     * Makes the  specified agent  fall asleep for  a given  amount of
     * milliseconds.
     * 
     * @param ag Agent given to fall asleep
     * @param millis Milliseconds to sleep
     */
    public void sleep(Agent ag, long millis) throws InterruptedException {
        getThreadFor(ag).sleep(millis);
        stats.incrementStat("Asleep (ms)", millis);
    }

    public int getNetSize() {
        return graph.size();
    }

    public int getVertexIdentity(Agent ag) {
        return getVertexFor(ag).identity().intValue();
    }

    public void setDefaultAgentMover(AgentMover am) {
        defaultAgentMover = am;
    }

    public boolean hasDefaultAgentMover() { 
        return defaultAgentMover!=null;
    }

    public void incrementStat(String key, Long increment) {
        stats.incrementStat(key, increment);
    }

    public Hashtable getStats(){
	return stats.getHashTable();
    }


    public void clone(Agent ag) {
        Agent ag2;
	System.out.println("The agent " + ag.getIdentity()
			   + " creates a clone.");
        ag2 = createAgent(ag.getClass(), getVertexFor(ag), new Hashtable());
        createThreadFor(ag2).start();
    }

    public void cloneAndSend(Agent ag, int door) 
        throws InterruptedException {
        
        Agent ag2;
        Vertex vertexFrom, vertexTo;
        Message msg;
        MessagePacket msgPacket;

        vertexFrom = getVertexFor(ag);
        vertexTo = vertexFrom.neighbour(door);
        msg = new StringMessage("Sent clone of "+ag.toString());
        msgPacket = new MessagePacket(vertexFrom.identity(), door, 
                                      vertexTo.identity(), msg);

        ag2 = createAgent(ag.getClass(), 
                          vertexFrom,
                          new Hashtable());

	moveAgentTo(ag2, door);
	
	System.out.println("The agent " + ag.getIdentity()
			   + " creates a clone (num " + ag2.getIdentity()
                           + ") and send him to the vertex "
			   + getVertexFor(ag).neighbour(door).identity());
        

        createThreadFor(ag2).start();        
    }

    private void pushMessageSendingEvent(MessagePacket mesgPacket, Agent ag) 
        throws InterruptedException {

	Long key = new Long(numGen.alloc());
	Long keyDep = new Long(numGen.alloc());
	Long keyArr = new Long(numGen.alloc());
        MessageSendingEvent mse;
	AgentMovedEvent dep, arr;
	Vertex vertexTo, vertexFrom;

	vertexFrom = graph.vertex(mesgPacket.sender());
	vertexTo = graph.vertex(mesgPacket.receiver());

        mse = new MessageSendingEvent(key,
                                      mesgPacket.message(),
                                      mesgPacket.sender(), 
                                      mesgPacket.receiver());


	int nbr = removeAgentFromVertex(vertexFrom, ag);
	/*	System.out.println("--"+ vertexFrom.identity().intValue()+"--"+nbr);*/
	
	dep = new AgentMovedEvent(keyDep,
				  mesgPacket.sender(),
				  new Integer(nbr));


	evtQ.put(dep);
	movingMonitor.waitForAnswer(keyDep);

	evtQ.put(mse);
	movingMonitor.waitForAnswer(key);


	nbr = addAgentToVertex(vertexTo, ag);

	arr = new AgentMovedEvent(keyArr,
				  mesgPacket.receiver(),
				  new Integer(nbr));
	
	evtQ.put(arr);
	movingMonitor.waitForAnswer(keyArr);
    }

    private Thread createThreadFor(Agent ag) {
        ProcessData data = getDataFor(ag);

        data.thread = new Thread(threadGroup, ag);
        data.thread.setPriority(THREAD_PRIORITY);

        return data.thread;
    }

    private Thread getThreadFor(Agent ag) {
        return getDataFor(ag).thread;
    }

    private Vertex getVertexFor(Agent ag) {
        return getDataFor(ag).vertex;
    }

    private Vertex getLastVertexSeen(Agent ag) {
        return getDataFor(ag).lastVertexSeen;
    }

    private ProcessData getDataFor(Agent ag) {
        return (ProcessData)agents.get(ag);
    }

    private class ProcessData {
        public Agent  agent;
        public Vertex vertex;
        public Vertex lastVertexSeen;
        public Thread thread;
    }
}

// Local Variables:
// mode: java
// coding: latin-1
// End:
