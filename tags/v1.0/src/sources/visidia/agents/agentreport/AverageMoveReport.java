package visidia.agents.agentreport;

import visidia.simulation.agents.AbstractStatReport;
import visidia.simulation.agents.stats.*;

import visidia.tools.Bag;


import java.util.Set;
import java.util.Hashtable;

/**
 * I'm a report which displays average moves by agent type.
 */
public class AverageMoveReport extends AbstractStatReport {
    
    /**
     * Used to store the number of agents of type given by Class. Keys
     * represent the class of agents (BasicAgent, SpanningTree...) and
     * values represent the number of agents for that class.
     */
    private Hashtable<Class, Long> agentsByClass;

    /**
     * Will  store the  report  information. Everything  that will  be
     * displayed on the report will be in that variable.
     */
    private Bag stats;

    /**
     * Used to fill the <code>agentsByClass</code> instance variable.
     */
    private void countAgents() {
	Set keys;
	agentsByClass = new Hashtable(10);

	/**
	 * Keys is  a Set  containing all the  events reported  by the
	 * simulator.
	 */
	keys = getBag().keySet();

	/**
	 * For each events
	 */
	for(Object key: keys) {
	    if (key instanceof AgentCreationStat) // if    it   is   a
						  // creation event
		agentsByClass.put(((AgentCreationStat)key).getAgentClass(),
				  new Long(getBag().getOccurrencesOf(key)));
	}
    }

    /**
     * Count the moves by agent type.
     */
    private void computeStats() {
	Set keys;

	stats = new Bag();

	countAgents();

	/**
	 * Keys is a set containing all the events.
	 */
	keys = getBag().keySet();

	for(Object key: keys) {
	    float movesByAgent;

	    if (key instanceof MoveStat) { // if it is a move event
		Class agClass = ((MoveStat)key).getAgentClass();
		long agentsForClass = agentsByClass.get(agClass).longValue();
		long movesForClass = getBag().getOccurrencesOf(key);

		stats.add("Average moves by agent (" 
			  + agClass.getSimpleName() + ")",
			  new Long(movesForClass / agentsForClass));
	    }
	}
    }

    /**
     * Calculate the  number of  moves by agent  type and  returns the
     * results that will be displayed.
     */
    public Bag getStats() {
        computeStats();
        return stats;
    }

}
