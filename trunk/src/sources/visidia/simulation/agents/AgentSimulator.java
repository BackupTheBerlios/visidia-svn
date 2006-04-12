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
import visidia.simulation.LabelChangeEvent;
import visidia.simulation.NextPulseEvent;

import visidia.simulation.SimulationAbortError;
import visidia.simulation.SimulatorThreadGroup;

import visidia.simulation.agents.stats.*;

import visidia.tools.NumberGenerator;
import visidia.tools.Bag;

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
import java.util.Map;

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
    //    private AgentStats stats;
    private Bag stats;

    /**
     * Constructor.  Creates  a new AgentSimulator and  affect its the
     * specified  graph,  the  specified  event queue,  the  specified
     * acknowlegdement queue and a default agents Hashtable.
     */
    public AgentSimulator(SimpleGraph netGraph, Vector agentsRules,
			  VQueue evtVQ, VQueue ackVQ) {
        this(netGraph, new Hashtable(), agentsRules, evtVQ, ackVQ);
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
        stats = new Bag();

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
        if (vertexAgentsNumber.get(vertex) == null)
            return 0;
        else
            return vertexAgentsNumber.get(vertex).size();
    }
    /** 
     * Return the  collection af agents  which are on the  vertex that
     * have the vertexId as the id.
     *
     * @param vertexId The vertex id.
     */
    public Collection getAgentsVertexCollection(int vertexId) {
 	return vertexAgentsNumber.get(graph.vertex(new Integer(vertexId)));
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
        synchronized (vertexAgentsNumber) {
            if( vertexAgentsNumber.get(vertex) != null)
                vertexAgentsNumber.get(vertex).add(ag);
            else{
                Collection<Agent> colOfAgents  = new HashSet();
                colOfAgents.add(ag);
                vertexAgentsNumber.put(vertex,colOfAgents);
            }
            return vertexAgentsNumber.get(vertex).size();
        }
    }
    
    /**
     * Removes a specified agent  from a specified vertex. Returns the
     * new number of agents on the vertex.
     *
     * @see #addAgentToVertex(Vertex, Agent)
     */
    private int removeAgentFromVertex(Vertex vertex, Agent ag){
        synchronized (vertexAgentsNumber) {
            vertexAgentsNumber.get(vertex).remove(ag);
            if( vertexAgentsNumber.get(vertex).isEmpty() ) {
                vertexAgentsNumber.remove(vertex);
                return 0;
            }
            else
                return vertexAgentsNumber.get(vertex).size();
        }
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
     * In charge of  the creation of agents. This  method gives a name
     * to the agent created and creat it on a specified vertex.
     * 
     * @see #createAgent(Class, Vertex, Hashtable).
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
    /**
     * In charge of the creation  of agent given the agent class. This
     * method creates  a new ProcessData where  the informations about
     * the agent are stored.
     */
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

	    addAgentToVertex(vertex, ag);

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        stats.add(new AgentCreationStat(ag.getClass()));

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

        stats.add(new TerminatedStat(ag.getClass()));

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

        stats.add(new MoveStat(ag.getClass()));
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
        stats.add(new EdgeStateStat(ag.getClass()));
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
     * Sends an event to tell graphical interface that a new pulse
     * is starting
     * @param pulse the current pulse
     */
    public void newPulse(int pulse) 
	throws InterruptedException {
	
	Long key = new Long(numGen.alloc());
	NextPulseEvent event = new NextPulseEvent(key,pulse);

	evtQ.put(event);
 	stats.add(new PulseStat());
    }

    /**
     * Returns the Agent which blocks the Vertex WhiteBoard, or null if
     * nobody has lock the vertex.
     *
     * @param v the vertex you want information about
     * @see #lockVertexProperties(Agent)
     */
    public Agent getVertexPropertiesOwner(Vertex v) {
	return lockedVertices.get(v);
    }

    /**
     * Returns the Agent which blocks the Vertex WhiteBoard, or null if
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
     * Returns true if the Vertex is locked, otherwise false
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
     * Returns true if the Vertex is locked, otherwise false
     * 
     * @param ag the Agent which wants to know if it Vertex
     * properties are locked
     * @see #lockVertexProperties(Agent)
     */
    public boolean vertexPropertiesLocked(Agent ag) {
	return vertexPropertiesLocked(getVertexFor(ag));
    }
    
    
    /**
     * Locks the Vertex WhiteBoard where the Agent is.
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
     * Unlocks the Vertex WhiteBoard  where the Agent is.
     *
     * @param ag the agent which wants to unlock it Vertex
     * @exception IllegalStateException if the WhiteBoard is
     * unlocked, or if the Agent is not the owner of the lock
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
	Vertex vertex = getVertexFor(ag);

	synchronized(vertex) {
	    while(vertexPropertiesLocked(vertex) 
		  && (getVertexPropertiesOwner(vertex) != ag)) {
		try {
		    vertex.wait();
		} catch(InterruptedException e) {
		    throw new SimulationAbortError(e);
		}
	    }
            stats.add(new VertexWBAccessStat(ag.getClass()));


	    return vertex.getProperty(key);
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
	Vertex vertex = getVertexFor(ag);

	synchronized(vertex) {
	    while(vertexPropertiesLocked(vertex) 
		  && (getVertexPropertiesOwner(vertex) != ag)) {
		try {
		    vertex.wait();
		} catch(InterruptedException e) {
		    throw new SimulationAbortError(e);
		}
	    }	
            stats.add(new VertexWBChangeStat(ag.getClass()));


	    vertex.setProperty(key, value);

	    if(key.equals("label")) {
                Long num = new Long(numGen.alloc());
                LabelChangeEvent lce;
                lce = new LabelChangeEvent(num,vertex.identity(),
                                           (String)value);
                try{
                    evtQ.put(lce);		    
                }catch(InterruptedException e){
                    throw new SimulationAbortError(e);
                }
            }
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
        stats.add(new SleepStat(ag.getClass()), millis);

    }
    /**
     * Returns  the number  of  vertices  of the  graph  on which  the
     * simulation is done.
     */
    public int getNetSize() {
        return graph.size();
    }
    /**
     * For a given agent returns the identity of the vertex it is on.
     *
     * @param ag The agent you want infromation on.
     */
    public int getVertexIdentity(Agent ag) {
        return getVertexFor(ag).identity().intValue();
    }
    /**
     * Sets the agentMover  of the simulation. This method  is used to
     * use the specified agentMover that you can create yourself.
     *
     * @see AgentMover
     */
    public void setDefaultAgentMover(AgentMover am) {
        defaultAgentMover = am;
    }

    /**
     * Returns true  if the  simulation has a  defaultAgentMover false
     * otherwise.
     *
     * @see #setDefaultAgentMover(AgentMover)
     */
    public boolean hasDefaultAgentMover() { 
        return defaultAgentMover!=null;
    }

    /**
     * This  method  is  used  to  increment the  field  stat  of  the
     * statistics.
     *
     * @param stat The field incremented.
     * @param increment The increment added to the value of the field.
     */
    public void incrementStat(AbstractStat stat, long increment) {
         stats.add(stat, increment);
    }

    /**
     * Returns all the statistics beeing computed on the simulation.
     */
    public Bag getStats(){
	return stats;
    }

    /**
     * This method allows the agent  ag to create a new agent instance
     * of agClass. It is named clone  even if ag is not necessarily an
     * instance of agClass.
     * 
     * @param ag The agent which is going to create the other agent.
     * @param agClass The class which the agent created is going to be
     * instance of.
     */
    public void clone(Agent ag, Class agClass) {
        Agent ag2;

        ag2 = createAgent(agClass, getVertexFor(ag), new Hashtable());
        createThreadFor(ag2).start();
    }

    /**
     * This method allows the agent  ag to create a new agent instance
     * of agClass.  It creates the agent  on one of  the neighbours of
     * the vertex the agent ag is on.
     *
     * @see #clone(Agent, Class)
     * @param ag The agent which is going to create an agent.
     * @param agClass The class which the agent created is going to be
     * instance of
     */
    public void cloneAndSend(Agent ag, Class agClass, int door) 
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

        ag2 = createAgent(agClass, 
                          vertexFrom,
                          new Hashtable());

	moveAgentTo(ag2, door);
	
        createThreadFor(ag2).start();        
    }

    /**
     * Private method  that transmits events throught the  evtQ to the
     * graphical interface.  It is implemented  so that the  events on
     * the graph are synchronised with the GUI.
     */
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
    
    /**
     * Creates the thread associated to the agent ag.
     *
     * @param ag The agent the thread is created for. 
     */
    private Thread createThreadFor(Agent ag) {
        ProcessData data = getDataFor(ag);

        data.thread = new Thread(threadGroup, ag);
        data.thread.setPriority(THREAD_PRIORITY);

        return data.thread;
    }

    /**
     * Returns the thread associated to the agent ag.
     */
    private Thread getThreadFor(Agent ag) {
        return getDataFor(ag).thread;
    }

    /**
     * Returns the vertex on which the agent ag is.
     */
    private Vertex getVertexFor(Agent ag) {
        return getDataFor(ag).vertex;
    }

    /**
     * Returns the  vertex on which  was the agent ag  before entering
     * the one it is on.
     */
    private Vertex getLastVertexSeen(Agent ag) {
        return getDataFor(ag).lastVertexSeen;
    }
    
    /**
     * Returns ProcessData associated with the agent ag.
     *
     * @see agents
     * @see #createAgent(Class, Vertex, Hashtable) 
     */
    private ProcessData getDataFor(Agent ag) {
        return (ProcessData)agents.get(ag);
    }

    /**
     * Private class that stores information about an agent.
     */
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
