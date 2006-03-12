package visidia.agents.agentchooser;

import java.util.Random;
import java.util.Enumeration;

import visidia.graph.SimpleGraph;
import visidia.graph.Vertex;
import visidia.gui.metier.Graphe;
import visidia.gui.metier.Sommet;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.userInterfaceSimulation.*;

/**
 * Allow user  to randomize agent position. To  specialize this class,
 * override agentName() and  probability(). This class puts BasicAgent
 * with a probability of 1/2.
 */
public class RandomAgentChooser {

    public static void place(AgentsSimulationWindow window) {

        Graphe graph = window.getVueGraphe().getGraphe();
        Enumeration e = graph.sommets();

        while(e.hasMoreElements()) {
            if (choose()) {
                Integer id;

                System.out.println("Type : " + e.nextElement());
                id = Integer.decode(((Sommet)e.nextElement())
                                    .getSommetDessin().getEtiquette());
                window.addAgents(id, agentName());

                System.out.println("Place agent " + agentName() 
                                   + " on vertex " + id + ".");
            }
        }
    }

    protected static String agentName() {
        return "BasicAgent";
    }

    protected static float probability() {
        return (float)0.5;
    }

    /**
     * Return a  random boolean  with a probability  of True  given by
     * probability().
     */
    private static boolean choose() {
        Random generator = new Random();
        float rand = generator.nextFloat();

        if (probability() < 0.0 || probability() > 1.0)
            throw new IllegalArgumentException("Probability must be "
                                               + "between 0 and 1.");

        return (rand < probability());
            
    }

}
