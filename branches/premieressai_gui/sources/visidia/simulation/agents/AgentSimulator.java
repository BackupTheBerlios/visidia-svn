package visidia.simulation.agents;

import visidia.graph.*;
import visidia.agents.*;

import visidia.visidiassert.VisidiaAssertion;

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
    private ThreadGroup threadGroup;

    private Hashtable agents;

    private AgentMover defaultAgentMover = null;

    public AgentSimulator(SimpleGraph netGraph) {
        this(netGraph, new Hashtable());
    }

    public AgentSimulator(SimpleGraph netGraph, 
                          Hashtable defaultAgentValues) {
	graph = (SimpleGraph) netGraph.clone();

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
	    Collection agentsNames = vertex.getAgentsNames();

            if(agentsNames == null)
                continue;

            Iterator it = agentsNames.iterator();

	    while(it.hasNext()) {
		String agentName = (String)it.next();

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
                .getConstructor(new Class [] {AgentSimulator.class, Hashtable.class})
                .newInstance(new Object [] {this, defaultAgentValues});

	    data.vertex = vertex;
            data.agent = ag;
            agents.put(ag, data);

        } catch (Exception e) {
            throw new IllegalArgumentException(e.toString());
        }

        return ag;
    }


    public void agentDeath(Agent ag) {
	agents.remove(ag);
    }

    public void killAgent(Agent ag) {
	getThreadFor(ag).interrupt();
	agentDeath(ag);
    }

    


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

//     public int getIdentity(Agent ag) {
//         return getAgentIdentityFor(ag);
//     }

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
                           + ") and send him to the vertex "
			   + getVertexFor(ag).neighbour(door).identity());
        
        createThreadFor(ag2).start();        
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
