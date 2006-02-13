package visidia.simulation;

import visidia.graph.*;
import visidia.tools.*;
import visidia.misc.*;

import java.io.*;

public class Simulator {

    public static final int THREAD_PRIORITY = 1;
    private SimpleGraph graph;

    // simulator threads set
    private SimulatorThreadGroup threadGroup;

    Agent agent;
    Vertex agentVertex;
    
	
    public Simulator( SimpleGraph netGraph, VQueue evtVQueue, 
                      VQueue ackVQueue, AlgoChoiceInterface choice){
        this(netGraph, evtVQueue, ackVQueue);
    }

    /**
     * cree une instance de simulateur  a partir du graphe "graph", et
     * des files de messages  "inVQueue" et "outVQueue" qui permettent
     * de communiquer avec l'interface graphique
     */
    public Simulator( SimpleGraph netGraph, VQueue evtVQueue,
                      VQueue ackVQueue){
	graph = (SimpleGraph) netGraph.clone();
	threadGroup = new SimulatorThreadGroup("simulator");
        
        agent = new Agent(this);
        agentVertex = netGraph.vertex(0);
    }

    public void moveAgentTo(int door) {
        System.out.println("Déplacement vers " + Integer.toString(door));
        agentVertex = agentVertex.neighbour(door);
    }

    /**
     * instancie  les thread  a partir  du graphe  et  de l'algorithme
     *donne   en  parametre   et  lance   l'execution.  Pour   le  bon
     *fonctionnement du  systeme les deux files  d'attentes passees en
     *parametre  doivent  etre vide,  mais  ceci  n'est une  condition
     *necessaire.
     */
    public void startSimulation(){
        Thread currThread = new Thread(threadGroup,agent);
        currThread.setPriority(THREAD_PRIORITY);
	currThread.start();

        System.out.println("Start");
    }
}

// Local Variables:
// mode: java
// coding: latin-1
// End:
