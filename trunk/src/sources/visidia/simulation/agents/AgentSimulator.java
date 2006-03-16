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

import java.io.*;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Collection;


import java.util.Vector;

public class AgentSimulator {

    public static final int THREAD_PRIORITY = 1;
    private SimpleGraph graph;

    // simulator threads set
    private SimulatorThreadGroup threadGroup;

    private Hashtable agents;
    
    private Hashtable<Vertex,Integer> vertexAgentsNumber;

    private AgentMover defaultAgentMover = null;
    
    private VQueue evtQ, ackQ;
    
    private NumberGenerator numGen = new NumberGenerator();

    private MovingMonitor movingMonitor;
    private Thread movingMonitorThread;

    private AgentStats stats;

    public AgentSimulator(SimpleGraph netGraph, VQueue evtVQ, 
                          VQueue ackVQ) {
        this(netGraph, new Hashtable(), evtVQ, ackVQ);
    }

    public AgentSimulator(SimpleGraph netGraph, 
                          Hashtable defaultAgentValues,
                          VQueue evtVQ, VQueue ackVQ) {

	graph = (SimpleGraph) netGraph;
        stats = new AgentStats();

	threadGroup = new SimulatorThreadGroup("simulator");
	fillAgentsTable(graph, defaultAgentValues);
        this.evtQ = evtVQ;
        this.ackQ = ackVQ;

        movingMonitor = new MovingMonitor(ackQ);
        movingMonitorThread = new Thread(movingMonitor);
        movingMonitorThread.start();
    }

    private void fillAgentsTable(SimpleGraph graph, 
                                 Hashtable defaultAgentValues) {
        Enumeration vertices;

        agents = new Hashtable();
	vertexAgentsNumber = new Hashtable();
	
        vertices = graph.vertices();

        while (vertices.hasMoreElements()) {
            Vertex vertex = (Vertex) vertices.nextElement();
	    Collection agentsNames = vertex.getAgentsNames();

	    vertexAgentsNumber.put(vertex,new Integer(0));
	    
            if(agentsNames == null){
		continue;
	    }
	    
            Iterator it = agentsNames.iterator();

	    while(it.hasNext()) {
		String agentName = (String)it.next();
		
		if (agentName != null) {
		    createAgent(agentName, vertex, defaultAgentValues);
		}
	    }

            vertex.clearAgentNames();
        }
    }
    
    private Agent createAgent(String agentName, Vertex vertex,
                              Hashtable defaultAgentValues) {
        try {
            String completName = new String("visidia.agents." + agentName);
            return createAgent(Class.forName(completName), vertex,
                               defaultAgentValues);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
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
	    int nbr = (vertexAgentsNumber.get(vertex)).intValue();
	    vertexAgentsNumber.put(vertex,
				   new Integer(nbr+1));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        stats.incrementStat("Created agents");

        return ag;
    }


    public void agentDeath(Agent ag) throws InterruptedException {
	
	ProcessData data = (ProcessData) agents.get(ag);
	Vertex vertex = data.vertex;
	
	agents.remove(ag);
	int nbr = (vertexAgentsNumber.get(vertex)).intValue();
	vertexAgentsNumber.put(vertex,
			       new Integer(nbr-1));
	

	evtQ.put(new AgentMovedEvent(numGen.alloc(),
				     vertex.identity(),
				     new Integer(nbr-1)));

        System.out.println("Algorithm Terminated");
        stats.incrementStat("Terminated algorithms");

	/* Detecting the end of the algorithm */
	if(agents.isEmpty()) {
            evtQ.put(new AlgorithmEndEvent(numGen.alloc()));
        }
    }
    
    public void moveAgentTo(Agent ag, int door) throws InterruptedException {
        ProcessData data = (ProcessData) agents.get(ag);
        Vertex vertexFrom, vertexTo;
        Message msg;
        MessagePacket msgPacket;

        VisidiaAssertion.verify( (0 <= door) && (door <= getArity(ag)) ,
                                 "In moveAgentTo(ag,door) : This door "
                                 + "doesn't exist !", this);

        vertexFrom = data.vertex;
        vertexTo = vertexFrom.neighbour(door);

        msg = new StringMessage(ag.toString());
        msgPacket = new MessagePacket(vertexFrom.identity(), door, 
                                      vertexTo.identity(), msg);
        System.out.println("The agent " + ag.getIdentity()
                           + " is moving to the vertex "
                           + vertexTo.identity());

	int nbr = (vertexAgentsNumber.get(vertexFrom)).intValue();
	vertexAgentsNumber.put(vertexFrom,
			       new Integer(nbr-1));

	pushMessageSendingEvent(msgPacket);

	nbr = (vertexAgentsNumber.get(vertexTo)).intValue();
	vertexAgentsNumber.put(vertexTo,
			       new Integer(nbr+1));
	
        data.vertex = vertexTo;
	data.lastVertexSeen = vertexFrom;

        stats.incrementStat("Moves");
    }

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

    public int entryDoor(Agent ag) {
        return getVertexFor(ag).indexOf(getLastVertexSeen(ag).identity());
    }

    public Object getVertexProperty(Agent ag, Object key) {
        stats.incrementStat("Accesses to vertices WhiteBoard");
        return getVertexFor(ag).getProperty(key);
    }

    public void setVertexProperty(Agent ag, Object key, Object value) {
        stats.incrementStat("Changes in vertices WhiteBoard");
        getVertexFor(ag).setProperty(key, value);
    }

    public void startSimulation(){
        Enumeration enumAgents = agents.elements();

        while (enumAgents.hasMoreElements()) {
            ProcessData data = (ProcessData) enumAgents.nextElement();
            createThreadFor(data.agent).start();
        }
    }

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

        stats.printStats();
        agents.clear();
        SynchronizedAgent.clear();
	vertexAgentsNumber.clear();
    }

    public int getArity(Agent ag) {
        return getVertexFor(ag).degree();
    }

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
                          vertexTo,
                          new Hashtable());

	System.out.println("The agent " + ag.getIdentity()
			   + " creates a clone (num " + ag2.getIdentity()
                           + ") and send him to the vertex "
			   + getVertexFor(ag).neighbour(door).identity());
        
        pushMessageSendingEvent(msgPacket);

        createThreadFor(ag2).start();        
    }

    private void pushMessageSendingEvent(MessagePacket mesgPacket) 
        throws InterruptedException {

	Long key = new Long(numGen.alloc());
	Long keyDep = new Long(numGen.alloc());
	Long keyArr = new Long(numGen.alloc());
        MessageSendingEvent mse;
	AgentMovedEvent dep, arr;
	Vertex vertexTo, vertexFrom;

        mse = new MessageSendingEvent(key,
                                      mesgPacket.message(),
                                      mesgPacket.sender(), 
                                      mesgPacket.receiver());

	vertexFrom = graph.vertex(mesgPacket.sender());
	int nbr = (vertexAgentsNumber.get(vertexFrom)).intValue();

	dep = new AgentMovedEvent(keyDep,
				  mesgPacket.sender(),
				  new Integer(nbr));

	vertexTo = graph.vertex(mesgPacket.receiver());
	nbr = (vertexAgentsNumber.get(vertexTo)).intValue();

	arr = new AgentMovedEvent(keyDep,
				  mesgPacket.receiver(),
				  new Integer(nbr+1));
	
	evtQ.put(dep);
	evtQ.put(mse);
	movingMonitor.waitForAnswer(key);
	evtQ.put(arr);
        	
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
