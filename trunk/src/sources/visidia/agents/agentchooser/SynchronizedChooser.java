package visidia.agents.agentchooser;

import java.util.Random;
import java.util.Enumeration;

import visidia.graph.SimpleGraph;
import visidia.graph.Vertex;
import visidia.gui.metier.Graphe;
import visidia.gui.metier.Sommet;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.userInterfaceSimulation.*;

public class SynchronizedChooser {

    public static void place(AgentsSimulationWindow window) {

        Graphe graph = window.getVueGraphe().getGraphe();
        Enumeration e = graph.sommets();

        while(e.hasMoreElements()) {
	    Sommet vertex = (Sommet)e.nextElement();
            if (choose()) {
                Integer id;

                id = Integer.decode(vertex.getSommetDessin().getEtiquette());
                window.addAgents(id, agentName());
		
                System.out.println("Place agent " + agentName() 
                                   + " on vertex " + id + ".");
            }
        }
    }

    /**
     * Return a  random boolean  with a probability  of True  given by
     * probability().
     */
    protected static boolean choose() {
        Random generator = new Random();
        float rand = generator.nextFloat();

        if (probability() < 0.0 || probability() > 1.0)
            throw new IllegalArgumentException("Probability must be "
                                               + "between 0 and 1.");

	System.out.println(rand);
        return (rand < probability());
            
    }


    protected static String agentName() {
        Random generator = new Random();
        int value = generator.nextInt(4) + 1;

        return "BasicSynchronizedAgent" + value;
    }

    protected static float probability() {
        return (float)0.7;
    }

}
