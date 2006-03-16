package visidia.simulation.agents;

import java.util.Hashtable;
import java.util.Set;

public class AgentStats {

    Hashtable<String, Long> stats;

    public AgentStats() {
        stats = new Hashtable();
    }

    public long getStat(String key) {
        Long value = stats.get(key);
        long intValue;

        if (value == null)
            intValue = 0;
        else
            intValue = value.longValue();
        return intValue;
    }

    public void incrementStat(String key) {
        incrementStat(key, (long)1);
    }

    public void incrementStat(String key, int increment) {
        incrementStat(key, (long)increment);
    }

    public void incrementStat(String key, long increment) {
        stats.put(key, new Long(getStat(key) + increment));
    }

    public void printStats() {
        Set<String> keys = stats.keySet();

        System.out.println("+---------------------+");
        System.out.println("|         Stats       |");
        System.out.println("+---------------------+");

        for(String key : keys)
            System.out.println(key + ": " + stats.get(key));
    }
}
