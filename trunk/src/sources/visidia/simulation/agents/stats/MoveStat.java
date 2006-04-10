package visidia.simulation.agents.stats;

import visidia.simulation.agents.Agent;

public class MoveStat extends AbstractStat {

    private Class agClass;

    public MoveStat(Class agClass) {
	this.agClass = agClass;
    }

    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null || o.getClass() != this.getClass())
	    return false;

	MoveStat o2 = (MoveStat) o;
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
	return "Moves (" + getAgentClassName() + ")";
    }
}