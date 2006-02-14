package visidia.simulation;

import visidia.graph.*;
import visidia.agents.*;

import java.io.*;
import java.util.Hashtable;
import java.util.Enumeration;

public class Simulator {

    public static final int THREAD_PRIORITY = 1;
    private SimpleGraph graph;

    // simulator threads set
    private ThreadGroup threadGroup;

    private Hashtable agents;

    public Simulator(SimpleGraph netGraph) {
	graph = (SimpleGraph) netGraph.clone();
	threadGroup = new ThreadGroup("simulator");
        
        fillAgentsTable(netGraph);
    }

    private void fillAgentsTable(SimpleGraph graph) {
        Enumeration vertices;

        agents = new Hashtable();
        vertices = graph.vertices();

        while (vertices.hasMoreElements()) {
            Vertex vertex = (Vertex) vertices.nextElement();
            String agentName = (String) vertex.getData();

            if (agentName != null) 
                createAgent(agentName, vertex);
        }
    }

    private void createAgent(String agentName, Vertex vertex) {
        try {
            Agent ag;
            String compleName = new String("visidia.agents." + agentName);
            ProcessData data = new ProcessData();
            
            ag = (Agent) Class.forName(compleName)
                .getConstructor(new Class [] {Simulator.class})
                .newInstance(new Object [] {this});

            data.vertex = vertex;
            data.agent = ag;
            data.agentIdentity = agents.size();
            agents.put(ag, data);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.toString());
        }
    }

    public void moveAgentTo(Agent ag, int door) {
        ProcessData data = (ProcessData) agents.get(ag);

        data.vertex = data.vertex.neighbour(door);
        System.out.println("L'agent " + data.agentIdentity
                           + " se deplace vers le sommet "
                           + data.vertex.identity());
    }

    public void startSimulation(){
        Enumeration enumAgents = agents.elements();

        while (enumAgents.hasMoreElements()) {
            ProcessData data = (ProcessData) enumAgents.nextElement();

            data.thread = new Thread(threadGroup,data.agent);
            data.thread.setPriority(THREAD_PRIORITY);
            data.thread.start();
        }
        System.out.println("Start");
    }

    public int getArity(Agent ag) {
        ProcessData data = (ProcessData) agents.get(ag);
        return data.vertex.degree();
    }

    public void sleep(Agent ag, long millis) {
        ProcessData data = (ProcessData) agents.get(ag);
        try {
            data.thread.sleep(millis);
        } catch (InterruptedException e) {
            
        }
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
