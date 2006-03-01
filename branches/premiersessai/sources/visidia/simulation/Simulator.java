package visidia.simulation;

import visidia.graph.*;
import visidia.agents.*;

import visidia.tools.*;
import visidia.misc.*;

import visidia.visidiassert.VisidiaAssertion;

import java.io.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

public class Simulator {

    public static final int THREAD_PRIORITY = 1;
    private SimpleGraph graph;

    //NumberGenerator is used for generating event number
    private NumberGenerator numGen = new NumberGenerator();
    private VQueue evtQ;
    private VQueue ackQ;

    private Hashtable agents;
    private AgentMover defaultAgentMover = null;

    // Simulator threads set
    private ThreadGroup threadGroup;

    public Simulator(SimpleGraph netGraph,  VQueue evtVQueue, VQueue ackVQueue) {
        this(netGraph, new Hashtable(), evtVQueue, ackVQueue);
    }

    public Simulator(SimpleGraph netGraph, Hashtable defaultAgentValues, VQueue evtVQueue, VQueue ackVQueue) {
	graph = (SimpleGraph) netGraph.clone();

        evtQ = evtVQueue;
	ackQ = ackVQueue;

	threadGroup = new ThreadGroup("simulator");

	fillAgentsTable(netGraph, defaultAgentValues);
    }

    private void fillAgentsTable(SimpleGraph graph, 
                                 Hashtable defaultAgentValues) {
        Enumeration vertices;

        agents = new Hashtable();
        vertices = graph.vertices();

        while (vertices.hasMoreElements()) {
            Vertex vertex = (Vertex) vertices.nextElement();
	    Vector vec = (Vector)vertex.getData();
            Enumeration e = vec.elements();

	    while(e.hasMoreElements()) {
		String agentName = (String)e.nextElement();

		if (agentName != null) 
		    createAgent(agentName, vertex, defaultAgentValues);
	    }
        }
    }

    private Agent createAgent(String agentName, Vertex vertex,
                             Hashtable defaultAgentValues) {
        try {
            String completName = new String("visidia.agents." + agentName);
            return createAgent(Class.forName(completName), vertex, defaultAgentValues);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.toString());
        }
    }

    private Agent createAgent(Class agentClass, Vertex vertex,
                             Hashtable defaultAgentValues) {
        Agent ag;

        try {

            ProcessData data = new ProcessData();
 
            ag = (Agent) agentClass
                .getConstructor(new Class [] {Simulator.class, Hashtable.class})
                .newInstance(new Object [] {this, defaultAgentValues});

	    data.vertex = vertex;
            data.agent = ag;
            agents.put(ag, data);
	    System.out.println("Creation of the agent " + ag.getIdentity()
                               + " (" + agentClass.getName()
			       + ") on the vertex " + vertex.identity());

        } catch (Exception e) {
            throw new IllegalArgumentException(e.toString());
        }

        return ag;
    }

    /**
     * Move agent to a neighbour
     * @param ag The agent to move
     * @param door The door used to go to the neighbour
     */
    public void moveAgentTo(Agent ag, int door) {
        ProcessData data = (ProcessData) agents.get(ag);

        VisidiaAssertion.verify( (0 <= door) && (door <= getArity(ag)) ,
                                 "In moveAgentTo(ag,door) : This door doesn't exist !",
                                 this);

        data.vertex = data.vertex.neighbour(door);
        System.out.println("The agent " + ag.getIdentity()
                           + " is moving to the vertex "
                           + data.vertex.identity());
    }

    /**
     * Return a vertex property
     * @param ag The agent which is on the desired vertex
     * @param key The object used as a key
     */
    public Object getVertexProperty(Agent ag, Object key) {
        return getVertexFor(ag).getProperty(key);
    }

    public void setVertexProperty(Agent ag, Object key, Object value) {
        getVertexFor(ag).setProperty(key, value);
        
        try {
        pushNodePropertyChangeEvent(new Integer(getVertexFor(ag).identity()),
                                    key, value);
        }
        catch(InterruptedException e)
            {
                // Nothing for now 
            }
    }

    public void startSimulation(){
        Enumeration enumAgents = agents.elements();

        while (enumAgents.hasMoreElements()) {
            ProcessData data = (ProcessData) enumAgents.nextElement();
            createThreadFor(data.agent).start();
        }
        System.out.println("Start");
    }

    public int getArity(Agent ag) {
        return getVertexFor(ag).degree();
    }

    public void sleep(Agent ag, long millis) {
        try {
            getThreadFor(ag).sleep(millis);
        } catch (InterruptedException e) {
            
        }
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

    public void cloneAndSend(Agent ag, int door) {
        Agent ag2;

        ag2 = createAgent(ag.getClass(), 
                         getVertexFor(ag).neighbour(door),
                         new Hashtable());

	System.out.println("The agent " + ag.getIdentity()
			   + " creates a clone (num " + ag2.getIdentity()
                           + ") and send it to the vertex "
			   + getVertexFor(ag).neighbour(door).identity());
        
        createThreadFor(ag2).start();        
    }

    /**
     * Return the events queue
     */
    public VQueue evtVQueueg(){
	return evtQ;
    }

    /**
     * Return the acknowlegdements queue
     */
    public VQueue ackVQueue(){
	return ackQ;
    }

    /**
     * Push the event of changing a vertex property (in the events queue)
     * @param nodeId The vertex identifier
     * @param key The object used as the key
     * @param value The object used as the value
     * @throws java.lang.InterruptedException
     */
    private void pushNodePropertyChangeEvent(Integer nodeId, Object key, Object value)throws InterruptedException{
	Long num = new Long(numGen.alloc());
        //	Object lock = new Object();
	//evtObjectTmp.put(num,lock);	
	NodePropertyChangeEvent npce = new NodePropertyChangeEvent(num,nodeId,key,value);
	//synchronized(lock){
	    evtQ.put(npce);
            //    lock.wait();
            //}
    }

    /**
     * Push the event of changing an edge property (in the events queue)
     * @param nodeId1 The vertex identifier of the first edge extremity
     * @param nodeId2 The vertex identifier of the second edge extremity
     * @param es The new edge state
     * @throws java.lang.InterruptedException
     */
    private void pushEdgeStateChangeEvent(Integer nodeId1, Integer nodeId2, EdgeState es)throws InterruptedException{
	Long key = new Long(numGen.alloc());
        //	Object lock = new Object();
	//evtObjectTmp.put(key,lock);	
	EdgeStateChangeEvent esce = new EdgeStateChangeEvent(key,nodeId1, nodeId2,es);
	//synchronized(lock){
	    evtQ.put(esce);
	    //lock.wait();
            //}
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

    private ProcessData getDataFor(Agent ag) {
        return (ProcessData)agents.get(ag);
    }

    private class ProcessData {
        public Agent  agent;
        public Vertex vertex;
        public Thread thread;
    }
}

// Local Variables:
// mode: java
// coding: latin-1
// End:
