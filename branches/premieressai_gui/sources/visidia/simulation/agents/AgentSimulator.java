package visidia.simulation.agents;

import visidia.graph.*;
import visidia.agents.*;

import java.io.*;
import java.util.Hashtable;
import java.util.Enumeration;

public class AgentSimulator {

    public static final int THREAD_PRIORITY = 1;
    private SimpleGraph graph;

    // simulator threads set
    private ThreadGroup threadGroup;

    private Hashtable agents;

    public AgentSimulator(SimpleGraph netGraph) {
        this(netGraph, new Hashtable());
    }

    public AgentSimulator(SimpleGraph netGraph, Hashtable defaultAgentValues) {
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
            String agentName = (String) vertex.getData();

            if (agentName != null) 
                createAgent(agentName, vertex, defaultAgentValues);
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
            data.agentIdentity = agents.size();
            agents.put(ag, data);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.toString());
        }

        return ag;
    }

    public void moveAgentTo(Agent ag, int door) {
        ProcessData data = (ProcessData) agents.get(ag);

        data.vertex = data.vertex.neighbour(door);
        System.out.println("L'agent " + data.agentIdentity
                           + " se deplace vers le sommet "
                           + data.vertex.identity());
    }

    public Object getVertexProperty(Agent ag, Object key) {
        return "coucou";
        // return getVertexFor(ag).getProperty(key);
    }

    public void setVertexProperty(Agent ag, Object key, Object value) {
        //getVertexFor(ag).setProperty(key, value);
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

    public int getVertexIdentity(Agent ag) {
        return getVertexFor(ag).identity().intValue();
    }

    public int getIdentity(Agent ag) {
        return getAgentIdentityFor(ag);
    }

    public void clone(Agent ag) {
        Agent ag2;
	System.out.println("L'agent " + getAgentIdentityFor(ag)
			   + " cr�e un clone.");
        ag2 = createAgent(ag.getClass(), getVertexFor(ag), new Hashtable());
        createThreadFor(ag2).start();
    }

    public void cloneAndSend(Agent ag, int door) {
        Agent ag2;

        ag2 = createAgent(ag.getClass(), 
                         getVertexFor(ag).neighbour(door),
                         new Hashtable());

	System.out.println("L'agent " + getAgentIdentityFor(ag)
			   + " cr�e un clone (num " + getAgentIdentityFor(ag2)
                           + ") et l'envoie sur le sommet "
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

    private int getAgentIdentityFor(Agent ag) {
        return getDataFor(ag).agentIdentity;
    }

    private ProcessData getDataFor(Agent ag) {
        return (ProcessData)agents.get(ag);
    }

    private class ProcessData {
        public int    agentIdentity;
        public Agent  agent;
        public Vertex vertex;
        public Thread thread;
    }
}

// Local Variables:
// mode: java
// coding: latin-1
// End: