package visidia.agents.agentstats;

import java.util.Hashtable;
import java.util.Set;

import visidia.simulation.agents.AbstractExperiment;
import visidia.simulation.agents.stats.AgentCreationStat;
import visidia.simulation.agents.stats.MoveStat;
import visidia.tools.Bag;

public class AverageStats extends AbstractExperiment {

	private Hashtable<Class, Long> agentsByClass;

	private Bag stats;

	private void countAgents() {
		Set keys;
		this.agentsByClass = new Hashtable<Class, Long>(10);

		keys = this.getBag().keySet();

		for (Object key : keys) {
			if (key instanceof AgentCreationStat)
				this.agentsByClass.put(((AgentCreationStat) key)
						.getAgentClass(), new Long(this.getBag()
						.getOccurrencesOf(key)));
		}
	}

	private void computeStats() {
		Set keys;

		this.stats = new Bag();

		this.countAgents();

		keys = this.getBag().keySet();

		for (Object key : keys) {

			if (key instanceof MoveStat) {
				Class agClass = ((MoveStat) key).getAgentClass();
				long agentsForClass = this.agentsByClass.get(agClass)
						.longValue();
				long movesForClass = this.getBag().getOccurrencesOf(key);

				this.stats.add("Average moves by agent ("
						+ agClass.getSimpleName() + ")", new Long(movesForClass
						/ agentsForClass));
			}
		}
	}

	public Bag getStats() {
		this.computeStats();
		return this.stats;
	}

}
