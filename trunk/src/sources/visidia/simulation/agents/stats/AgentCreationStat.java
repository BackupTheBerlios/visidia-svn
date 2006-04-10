package visidia.simulation.agents.stats;

import visidia.simulation.agents.Agent;

public class AgentCreationStat extends AbstractStat {

    private Class agClass;

    public AgentCreationStat(Class agClass) {
	this.agClass = agClass;
    }

    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null || o.getClass() != this.getClass())
	    return false;

	AgentCreationStat o2 = (AgentCreationStat) o;
	return agClass.equals(o2.agClass);
    }

    public String getAgentClassName() {
	return agClass.getSimpleName();
    }

    public Class getAgentClass() {
	return agClass;
    }

    public int hashCode() {
	int hash;

	hash = getClass().hashCode();
	hash = 31 * hash + agClass.hashCode();
	return hash;
    }

    public String toString() {
	return "Created agent (" + getAgentClassName() + ")";
    }
}