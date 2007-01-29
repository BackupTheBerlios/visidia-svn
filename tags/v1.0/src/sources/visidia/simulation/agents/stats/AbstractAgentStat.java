package visidia.simulation.agents.stats;

import visidia.simulation.agents.Agent;

/**
 * Represents  every  events  which  are  based  on  particular  agent
 * classes. I store the agent class which is related to the event.
 */
public abstract class AbstractAgentStat extends AbstractStat {

    private Class agClass;


    /**
     * Creates a new <code>AbstractAgentStat</code> instance.
     *
     * @param agClass The agent class that produced the event.
     */
    public AbstractAgentStat(Class agClass) {
	this.agClass = agClass;
    }


    public boolean equals(Object o) {
        if (super.equals(o) == false)
            return false;

	AbstractAgentStat o2 = (AbstractAgentStat) o;
	return agClass.equals(o2.agClass);
    }

    /**
     * Returns name of the class used to build this event. Returns the
     * simple name of the agent class.
     */
    public String getAgentClassName() {
	return agClass.getSimpleName();
    }

    /**
     * Returns the agent class associated with this event.
     */
    public Class getAgentClass() {
	return agClass;
    }

    public int hashCode() {
	int hash;

	hash = super.hashCode();
	hash = 31 * hash + agClass.hashCode();
	return hash;
    }

    /**
     * Description of the  event. This method calls #descriptionName()
     * to  get   a  description  of   the  event.   Do   not  override
     * #toString().  Override #descriptionName() instead.
     *
     * @return Returns a new string  which is the concatenation of the
     * description name  and the agent  classe name between  to braces
     * as: <code>An event (AgentClass)</code>.
     *
     * @see #descriptionName()
     */
    public String toString() {
        return descriptionName() + " (" + getAgentClassName() + ")";
    }

    /**
     * Returns  a string  which describes  the event.  Override  me to
     * return a string based on the event you represent.
     *
     * @see #toString()
     */
    protected abstract String descriptionName();
}
