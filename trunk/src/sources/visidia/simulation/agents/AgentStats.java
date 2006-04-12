package visidia.simulation.agents;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Class  used to  compute  some  statistics on  the  progress of  the
 * simulation.  Add methods  to  this  class to  compute  in an  other
 * way. The methods implemented only increment the values computed.
 */
public class AgentStats {

    /**
     * Hashtable that contains the values computed and their keys.
     */
    Hashtable<String, Long> stats;

    /**
     * Creates the AgentStats and its Hashtable.
     */
    public AgentStats() {
        stats = new Hashtable();
    }

    /**
     * Returns the value of a statistic given a key of the Hashtable.
     *
     * @param key The key given to seek in the Hashtable.
     */
    public long getStat(String key) {
        Long value = stats.get(key);
        long intValue;

        if (value == null)
            intValue = 0;
        else
            intValue = value.longValue();
        return intValue;
    }

    /**
     * Increments by one the value corresponding to the specified key.
     *
     * @param key The key given to seek in the Hashtable.
     */
    public void incrementStat(String key) {
        incrementStat(key, (long)1);
    }

    /**
     * Increments the value corresponding to the specifies key.
     *
     * @param key The key given to seek in the Hashtable.
     * @param increment Increment that will be added to the value.
     */
    public void incrementStat(String key, long increment) {
        stats.put(key, new Long(getStat(key) + increment));
    }

    /**
     * Returns the Hashtable of the statistics calculated. 
     */
    public Map asMap(){
	return stats;
    }

}
