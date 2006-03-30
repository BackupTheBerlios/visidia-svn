package visidia.tools.agents;

import visidia.tools.HashTableModel;
import visidia.simulation.agents.AgentSimulator;

public class UpdateTableStats extends UpdateTable {

    AgentSimulator sim;

    public UpdateTableStats(AgentSimulator sim, 
                            HashTableModel table, long sleepTime) {
        super(table,sleepTime);
        this.sim = sim;
    }

    public UpdateTableStats(AgentSimulator sim, HashTableModel table) {
        this(sim, table, 1000);
    }

    public void run() {
        while (! stop) {
            try {
                System.out.println("dd //jb");
                synchronized (this) {
                    wait(1000);
                }
            } catch (InterruptedException e) {
                stop();
            }
            ((HashTableModel)table).setProperties(sim.getStats());
        }
    }

}
