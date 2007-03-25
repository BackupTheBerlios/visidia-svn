package visidia.simulation.agents.stats;

public abstract class AbstractAgentStat extends AbstractStat {

	private Class agClass;
	private Integer agId;

	public AbstractAgentStat(Class agClass) {
		this.agClass = agClass;
	}
	
	public AbstractAgentStat(Class agClass, Integer agId) {
		this.agClass = agClass;
		this.agId = agId;
	}

	public boolean equals(Object o) {
		if (super.equals(o) == false) {
			return false;
		}

		AbstractAgentStat o2 = (AbstractAgentStat) o;
		return this.agClass.equals(o2.agClass) && this.agId == o2.agId;
	}

	public String getAgentClassName() {
		return this.agClass.getSimpleName();
	}
	
	public Integer getAgentId() {
		return this.agId;
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
		if (this.agId == null)
			return this.descriptionName() + " (" + this.getAgentClassName() + ")";
		else
			return this.descriptionName() + " (" + this.getAgentClassName() + " : "+ this.getAgentId() + ")";
	}
}
