package visidia.agents.agentstats;

import visidia.simulation.agents.AbstractExperiment;
import visidia.simulation.agents.stats.*;

import visidia.tools.Bag;


import java.util.Set;
import java.util.Hashtable;

public class AverageStats extends AbstractExperiment {
    
    private Hashtable<Class, Long> agentsByClass;
    private Bag stats;

    private void countAgents() {
	Set keys;
	agentsByClass = new Hashtable(10);

	keys = getBag().keySet();

	for(Object key: keys) {
	    if (key instanceof AgentCreationStat)
		agentsByClass.put(((AgentCreationStat)key).getAgentClass(),
				  new Long(getBag().getOccurrencesOf(key)));
	}
    }

    private void computeStats() {
	Set keys;

	stats = new Bag();

	countAgents();

	keys = getBag().keySet();

	for(Object key: keys) {
	    float movesByAgent;

	    if (key instanceof MoveStat) {
		Class agClass = ((MoveStat)key).getAgentClass();
		long agentsForClass = agentsByClass.get(agClass).longValue();
		long movesForClass = getBag().getOccurrencesOf(key);

		stats.add("Average moves by agent (" 
			  + agClass.getSimpleName() + ")",
			  new Long(movesForClass / agentsForClass));
	    }
	}
    }


    public Bag getStats() {
        computeStats();
        return stats;
    }

}
