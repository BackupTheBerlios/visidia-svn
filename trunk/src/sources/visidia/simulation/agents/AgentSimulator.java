package visidia.simulation.agents;

import visidia.graph.*;
import visidia.agents.*;
import visidia.tools.VQueue;
import visidia.visidiassert.VisidiaAssertion;

import visidia.simulation.AlgorithmEndEvent;
import visidia.simulation.MessageSendingEvent;
import visidia.simulation.MessagePacket;
import visidia.simulation.EdgeStateChangeEvent;
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
    
    private Hashtable vertexAgentsNumber;

    private AgentMover defaultAgentMover = null;
    
    private VQueue evtQ, ackQ;
    
    private NumberGenerator numGen = new NumberGenerator();

    private MovingMonitor movingMonitor;

    public AgentSimulator(SimpleGraph netGraph, VQueue evtVQ, 
                          VQueue ackVQ) {
        this(netGraph, new Hashtable(), evtVQ, ackVQ);
    }

    public AgentSimulator(SimpleGraph netGraph, 
                          Hashtable defaultAgentValues,
                          VQueue evtVQ, VQueue ackVQ) {

	graph = (SimpleGraph) netGraph;

	threadGroup = new SimulatorThreadGroup("simulator");
	fillAgentsTable(graph, defaultAgentValues);
        this.evtQ = evtVQ;
        this.ackQ = ackVQ;

        movingMonitor = new MovingMonitor(ackQ);
        new Thread(threadGroup, movingMonitor).start();
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
	    
            if(agentsNames == null){
		vertexAgentsNumber.put(vertex, vertex.getAgentsNumber());
		continue;
	    }
	    
            Iterator it = agentsNames.iterator();

	    while(it.hasNext()) {
		String agentName = (String)it.next();
		
		if (agentName != null) {
		    createAgent(agentName, vertex, defaultAgentValues);
		    vertex.incrementAgentsNumber();
		}
	    }
	    vertexAgentsNumber.put(vertex, vertex.getAgentsNumber());
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
            throw new IllegalArgumentException(e.toString());
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

        } catch (Exception e) {
            throw new IllegalArgumentException(e.toString());
        }

        return ag;
    }


    public void agentDeath(Agent ag) throws InterruptedException {
	agents.remove(ag);

	/* Detecting the end of the algorithm */
	if(agents.isEmpty()) {
            evtQ.put(new AlgorithmEndEvent(numGen.alloc()));
        }
        System.out.println("Algorithm Terminated");
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

        msg = new StringMessage("Moving");
        msgPacket = new MessagePacket(vertexFrom.identity(), door, 
                                      vertexTo.identity(), msg);
        System.out.println("The agent " + ag.getIdentity()
                           + " is moving to the vertex "
                           + vertexTo.identity());

	pushMessageSendingEvent(msgPacket);

        data.vertex = vertexTo;
	data.lastVertexSeen = vertexFrom;
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
    }

    public int entryDoor(Agent ag) {
        return getVertexFor(ag).indexOf(getLastVertexSeen(ag).identity());
    }

    public Object getVertexProperty(Agent ag, Object key) {
        return getVertexFor(ag).getProperty(key);
    }

    public void setVertexProperty(Agent ag, Object key, Object value) {
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
        
        while(threadGroup.activeCount() > 0) {
            threadGroup.interrupt();
            try {
                Thread.currentThread().sleep(50);
            } catch (InterruptedException e) {
                throw new SimulationAbortError(e);
            }
            
        }
        agents.clear();
    }

    public int getArity(Agent ag) {
        return getVertexFor(ag).degree();
    }

    public void sleep(Agent ag, long millis) throws InterruptedException {
        getThreadFor(ag).sleep(millis);
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
        msg = new StringMessage("Clone");
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
        MessageSendingEvent mse;

        mse = new MessageSendingEvent(key,
                                      mesgPacket.message(),
                                      mesgPacket.sender(), 
                                      mesgPacket.receiver());
        evtQ.put(mse);
        movingMonitor.waitForAnswer(key);
	
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
