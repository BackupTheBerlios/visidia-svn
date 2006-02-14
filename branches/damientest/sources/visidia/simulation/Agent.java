/**
 * Classe  qui sera sous-classee  pour ecrire  les algorithmes  a base
 * d'agents.
 */

package visidia.simulation;

public abstract class Agent implements Runnable {
    
    private Simulator simulator;

    public Agent (Simulator sim) {
        simulator = sim;
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

    /**
     * M�thode de l'interface Runnable
     */
    public final void run() {
        init();
    };

    /**
     * M�thode qui sp�cifie l'action de chaque agent
     */
    protected abstract void init();
}
