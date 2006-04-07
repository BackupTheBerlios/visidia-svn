package visidia.tools.agents;

import visidia.tools.HashTableModel;
import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.agents.AbstractExperiment;

public class UpdateTableStats extends UpdateTable {

    AgentSimulator sim;
    AbstractExperiment expType;

    public UpdateTableStats(AgentSimulator sim, AbstractExperiment expType, 
                            HashTableModel table, long sleepTime) {
        super(table,sleepTime);
        this.sim = sim;
	this.expType = expType;
    }

    public UpdateTableStats(AgentSimulator sim, AbstractExperiment expType, 
			    HashTableModel table) {
        this(sim, expType, table, 1000);
    }

    public void run() {
        while (! stop) {
            try {
                synchronized (this) {
                    wait(1000);
                }
            } catch (InterruptedException e) {
                stop();
            }
	    expType.setStats(sim.getStats());
            ((HashTableModel)table).setProperties(expType.getStats());
        }
    }

}
