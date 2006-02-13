package visidia.simulation;

import visidia.graph.*;

import java.io.*;

public class Simulator {

    public static final int THREAD_PRIORITY = 1;
    private SimpleGraph graph;

    // simulator threads set
    private ThreadGroup threadGroup;

    Agent agent;
    Vertex agentVertex;
    Thread thread;
	
    public Simulator(SimpleGraph netGraph) {
	graph = (SimpleGraph) netGraph.clone();
	threadGroup = new ThreadGroup("simulator");
        
        agent = new BasicAgent(this);
        agentVertex = netGraph.vertex(new Integer(0));
    }

    public void moveAgentTo(int door) {
        agentVertex = agentVertex.neighbour(door);
        System.out.println("Deplacement vers sommet " + 
                           agentVertex.identity());
    }

    public void startSimulation(){
        thread = new Thread(threadGroup,agent);
        thread.setPriority(THREAD_PRIORITY);
	thread.start();
        
        System.out.println("Start");
    }

    public int getArity() {
        return agentVertex.degree();
    }

    public void sleep(long millis) {
        try {
            thread.sleep(millis);
        } catch (InterruptedException e) {
            
        }
    }
}

// Local Variables:
// mode: java
// coding: latin-1
// End:
