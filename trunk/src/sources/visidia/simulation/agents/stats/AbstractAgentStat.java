package visidia.simulation.agents.stats;

import visidia.simulation.agents.Agent;

public abstract class AbstractAgentStat extends AbstractStat {

    private Class agClass;

    public AbstractAgentStat(Class agClass) {
	this.agClass = agClass;
    }

    public boolean equals(Object o) {
        if (super.equals(o) == false)
            return false;

	AbstractAgentStat o2 = (AbstractAgentStat) o;
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

	hash = super.hashCode();
	hash = 31 * hash + agClass.hashCode();
	return hash;
    }

    public String toString() {
        return descriptionName() + " (" + getAgentClassName() + ")";
    }
}
