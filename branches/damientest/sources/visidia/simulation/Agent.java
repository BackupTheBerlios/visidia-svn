/**
 * Classe  qui sera sous-classee  pour ecrire  les algorithmes  a base
 * d'agents.
 */

package visidia.simulation;

import java.util.Hashtable;

import visidia.tools.WithWhiteBoard;
import visidia.tools.WhiteBoard;

public abstract class Agent implements Runnable, WithWhiteBoard {
    
    private Simulator simulator;
    private WhiteBoard whiteBoard;
        
    public Agent(Simulator sim) {
        this(sim, new Hashtable());
    }

    public Agent(Simulator sim, Hashtable defaultValues) {
        simulator = sim;
        whiteBoard = new WhiteBoard(defaultValues);
    }

    public void setWhiteBoard(WhiteBoard wb) {
        this.whiteBoard = wb;
    }

    public WhiteBoard getWhiteBoard() {
        return whiteBoard;
    }

    public final void moveToDoor(int door) {
        simulator.moveAgentTo(this, door);
    }

    protected void sleep(long millis) {
        simulator.sleep(this, millis);
    }

    protected int getArity() {
        return simulator.getArity(this);
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
