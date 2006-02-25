/**
 * Classe  qui sera sous-classee  pour ecrire  les algorithmes  a base
 * d'agents.
 */

package visidia.simulation.agents;

import java.util.Hashtable;

import visidia.tools.agents.WithWhiteBoard;
import visidia.tools.agents.WhiteBoard;

public abstract class Agent implements Runnable, WithWhiteBoard {
    
    private AgentSimulator simulator;
    private WhiteBoard whiteBoard;
        
    public Agent(AgentSimulator sim) {
        this(sim, new Hashtable());
    }

    public Agent(AgentSimulator sim, Hashtable defaultValues) {
        simulator = sim;
        whiteBoard = new WhiteBoard(defaultValues);
    }

    public void setWhiteBoard(WhiteBoard wb) {
        this.whiteBoard = wb;
    }

    public WhiteBoard getWhiteBoard() {
        return whiteBoard;
    }

    public void moveToDoor(int door) {
        simulator.moveAgentTo(this, door);
    }

    protected void sleep(long millis) {
        simulator.sleep(this, millis);
    }

    protected int getArity() {
        return simulator.getArity(this);
    }

    protected int getVertexIdentity() {
        return simulator.getVertexIdentity(this);
    }

    protected int getIdentity() {
        return simulator.getIdentity(this);
    }

    public Object getProperty(Object key) {
        return whiteBoard.getValue(key);
    }

    public void setProperty(Object key, Object value) {
        whiteBoard.setValue(key, value);
    }
        
    public Object getVertexProperty(Object key) {
        return simulator.getVertexProperty(this, key);
    }

    public void setVertexProperty(Object key, Object value) {
        simulator.setVertexProperty(this, key, value);
    }

    public void cloneAndSend(int door) {
        simulator.cloneAndSend(this, door);
    }

    /**
     * Méthode de l'interface Runnable
     */
    public final void run() {
        init();
    };

    /**
     * Méthode qui spécifie l'action de chaque agent
     */
    protected abstract void init();
}
