package visidia.simulation.agents.stats;

/**
 * Abstract  class   to  represent   an  event  occurred   during  the
 * simulation. Each time something occurred during the simulation, the
 * simulator creates new  instances of me so that  reports can present
 * as much information as possible.
 * <p>
 * Have a look at my subclasses to  get an idea of what type of events
 * I'm usefull for.
 *
 */
public abstract class AbstractStat {

    /**
     * Verifies that two objects represent the same event.  <p>
     *
     * You must be very carrefull with the #equals implementation: for
     * the  reports to  work properly,  equals methods  should returns
     * true when the event is the same (same type of event, same class
     * of agent...). In  this case, a counter will  be incremented and
     * reports will work in good conditions.  <p>
     *
     * When  you override  this  method, don'tt  forget  to call  this
     * implementation  through   <code>super.equals(o)</code>.   If  I
     * return  <code>false</code>,  your  implementation must  returns
     * <code>false</code> too.  If I return <code>true</code>, you can
     * do   further   tests   before   deciding  whether   to   return
     * <code>true</code> or <code>false</code>.
     *
     * @see #hashCode
     */
    public boolean equals(Object o) {
	if (this == o)
	    return true;
	return (o != null && o.getClass() == this.getClass());
    }

    /**
     * Returns  an int that  identify the  event in  hashtables. Great
     * care must be taken  when implementing this function. All fields
     * tested in  #equals(Object), shoud take part  in the hashcoding.
     * <p>
     *
     * When   you  override   this  method,   don't  forget   to  call
     * <code>super.hashCode()</code> and base  your hashCode on what I
     * return.
     *
     * @see #equals(Object)
     */
    public int hashCode() {
	int hash;

	hash = getClass().hashCode();
	return hash;
    }

    /**
     * Description  of the  event.   Used in  reports when  displaying
     * stats.
     *
     * @see #descriptionName()
     */
    public abstract String toString();

}
