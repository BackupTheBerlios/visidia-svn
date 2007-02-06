package visidia.tools.agents;

import visidia.tools.HashTableModel;
import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.agents.AbstractExperiment;
import visidia.tools.Bag;

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
        while (! this.stop) {
            try {
                synchronized (this) {
                    this.wait(1000);
                }
            } catch (InterruptedException e) {
                this.stop();
            }
	    this.expType.setStats(this.sim.getStats());
	    Bag stats = this.expType.getStats();
	    if (stats != null)
		((HashTableModel)this.table).setProperties(stats.asHashTable());
        }
    }

}
