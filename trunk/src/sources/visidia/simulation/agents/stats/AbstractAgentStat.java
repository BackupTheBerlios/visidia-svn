package visidia.simulation.agents.stats;

public abstract class AbstractAgentStat extends AbstractStat {

	private Class agClass;
	private String agName;

	public AbstractAgentStat(Class agClass) {
		this.agClass = agClass;
	}
	
	public AbstractAgentStat(Class agClass, String agName) {
		this.agClass = agClass;
		this.agName = agName;
	}

	public boolean equals(Object o) {
		if (super.equals(o) == false) {
			return false;
		}

		AbstractAgentStat o2 = (AbstractAgentStat) o;
		return this.agClass.equals(o2.agClass);
	}

	public String getAgentClassName() {
		return this.agClass.getSimpleName();
	}
	
	public String getAgentName() {
		return this.agName;
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
