package visidia.simulation.agents.stats;

public abstract class AbstractAgentStat extends AbstractStat {

	private Class agClass;

	public AbstractAgentStat(Class agClass) {
		this.agClass = agClass;
	}

	public boolean equals(Object o) {
		if (super.equals(o) == false)
			return false;

		AbstractAgentStat o2 = (AbstractAgentStat) o;
		return this.agClass.equals(o2.agClass);
	}

	public String getAgentClassName() {
		return this.agClass.getSimpleName();
	}

	public Class getAgentClass() {
		return this.agClass;
	}

	public int hashCode() {
		int hash;

		hash = super.hashCode();
		hash = 31 * hash + this.agClass.hashCode();
		return hash;
	}

	public String toString() {
		return this.descriptionName() + " (" + this.getAgentClassName() + ")";
	}
}
