package visidia.simulation.agents.stats;

public abstract class AbstractStat {

    public boolean equals(Object o) {
	if (this == o)
	    return true;
	return ((o != null) && (o.getClass() == this.getClass()));
    }

    public int hashCode() {
	int hash;

	hash = this.getClass().hashCode();
	return hash;
    }

    public String toString() {
        return this.descriptionName();
    }

    protected abstract String descriptionName();
}
