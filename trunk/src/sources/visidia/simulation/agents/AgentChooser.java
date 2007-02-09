package visidia.simulation.agents;

import java.util.Enumeration;

import visidia.gui.metier.Graphe;
import visidia.gui.metier.Sommet;
import visidia.gui.presentation.userInterfaceSimulation.AgentsSimulationWindow;

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
	
	this.place();
        //placeAgents(window.getVueGraphe().getGraphe());
    }
    
    /**
     * Private     method     used     by    the     public     method
     * #placeAgents(AgentsSimulatorWndow).   On  each  vertex  of  the
     * specified graph, it chooses to add an agent or not.
     * 
     * @see #chooseForVertex(Integer)
     * @see #chooseForVertes(Sommet)
     * @param graph The graph the agents are added to
     */
    private void placeAgents(Graphe graph) {
        Enumeration e = graph.sommets();

        while(e.hasMoreElements()) {
            this.chooseForVertex((Sommet)e.nextElement());
        }
    }
    
    /**
     * use me to begin placing the agents. Should be called after
     * initializing some parameters.
     */
    protected void placeAgents() {
	this.placeAgents(this.window.getVueGraphe().getGraphe());
    }
    /**
     * For a given vertex this method chooses to add an agent or not.
     *
     * @param  vertex  The graphical  interface  vertex  on which  the
     * choice is done.
     */
    private void chooseForVertex(Sommet vertex) {
        this.chooseForVertex(Integer.decode(vertex.getSommetDessin()
                                       .getEtiquette()));
    }

    /**
     * Adds  a new  agent to  the vertex.  Call this  method  when you
     * decide to put an agent on one vertex.
     *
     * @param vertexId The identity of a  vertex you want to put a new
     * agent on.
     * @param agentName Name  of the agent to put  on the vertex. This
     * is the name of a class.
     */
    protected final void addAgent(Integer vertexId, String agentName) {
        this.window.addAgents(vertexId, agentName);
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

    /**
     * Overwrite me if you want to make any pre-processing before
     * beginning to place any agent. Default : call placeAgents.
     *
     * @see placeAgents
     * @see visidia.agents.agentchooser.RandomAgentChooser
     **/
    protected void place() {
	this.placeAgents();
    }
}
