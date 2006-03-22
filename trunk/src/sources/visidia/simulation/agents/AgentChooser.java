package visidia.simulation.agents;

import java.util.Enumeration;

import visidia.gui.presentation.userInterfaceSimulation.*;
import visidia.gui.metier.*;

/**
 * This  class is  used to  place  agents on  a graph  using your  own
 * code. Have a look at the subclasses for exemples. <p>
 *
 * To implement your own chooser, override #chooseForVertex(Integer).
 */
public abstract class AgentChooser {
    
    private AgentsSimulationWindow window;

    /**
     * Used by the simulation window to place agents on the graph.
     *
     * @param window The simulation window which contains the graph on
     * which adding agents.
     */
    public final void placeAgents(AgentsSimulationWindow window) {
        this.window = window;

        placeAgents(window.getVueGraphe().getGraphe());
    }

    private void placeAgents(Graphe graph) {
        Enumeration e = graph.sommets();

        while(e.hasMoreElements()) {
            chooseForVertex((Sommet)e.nextElement());
        }
    }

    private void chooseForVertex(Sommet vertex) {
        chooseForVertex(Integer.decode(vertex.getSommetDessin()
                                       .getEtiquette()));
    }

    /**
     * Adds  a new  agent to  the vertex.  Call this  method  when you
     * decide to put an agent on one vertex.
     *
     * @param vertexId The vertex identity you want to put a new agent
     * on.
     * @param agentName Name  of the agent to put  on the vertex. This
     * is the name of a class.
     */
    protected final void addAgent(Integer vertexId, String agentName) {
        window.addAgents(vertexId, agentName);
    }

    /**
     * Decide here if you want to add an agent on one vertex. Override
     * this method in your subclasses.
     *
     * @param vertexIdentity  The vertex identity on  which you should
     * decide if you want an agent or not.
     *
     * @see visidia.agents.agentchooser.RandomAgentChooser#chooseForVertex(Integer)
     */
    protected abstract void chooseForVertex(Integer vertexIdentity);
}
