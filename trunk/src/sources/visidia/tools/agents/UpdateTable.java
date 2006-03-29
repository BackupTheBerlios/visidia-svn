package visidia.tools.agents;

import visidia.tools.HashTableModel;
import visidia.simulation.agents.AgentSimulator;

public class UpdateTable implements Runnable {

    AgentSimulator sim;
    HashTableModel table;
    long sleepTime;
    boolean stop;

    public UpdateTable(AgentSimulator sim, 
                       HashTableModel table, long sleepTime) {
        this.sim = sim;
        this.table = table;
        this.sleepTime = sleepTime;
        this.stop = false;
    }

    public UpdateTable(AgentSimulator sim, HashTableModel table) {
        this(sim, table, 1000);
    }

    public void stop() {
        stop = true;
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
            table.setProperties(sim.getStats());
        }
    }

}
