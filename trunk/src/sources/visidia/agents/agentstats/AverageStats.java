package visidia.agents.agentstats;

import visidia.simulation.agents.AbstractExperiment;
import visidia.simulation.agents.stats.*;

import visidia.tools.Bag;


import java.util.Set;
import java.util.Hashtable;

public class AverageStats extends AbstractExperiment {
    
    private Hashtable<Class, Integer> agentsByClass;
    private Bag stats;

    private void countAgents() {
	Set keys;
	agentsByClass = new Hashtable(10);

	keys = getBag().keySet();

	for(Object key: keys) {
	    if (key instanceof AgentCreationStat)
		agentsByClass.put(((AgentCreationStat)key).getAgentClass(),
				  new Integer(getBag().getOccurrencesOf(key)));
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
		int agentsForClass = agentsByClass.get(agClass).intValue();
		int movesForClass = getBag().getOccurrencesOf(key);

		stats.add("Average moves by agent (" 
			  + agClass.getSimpleName() + ")",
			  new Integer(movesForClass / agentsForClass));
	    }
	}
    }


    public Bag getStats() {
        computeStats();
        return stats;
    }

}
