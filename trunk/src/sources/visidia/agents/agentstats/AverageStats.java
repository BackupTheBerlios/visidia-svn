package visidia.agents.agentstats;

import visidia.simulation.agents;

import java.util.Map;
import java.util.Hashtable;

public class AverageStats extends AbstractExperiment {
    
    Map stats;
    Hashtable<String, Long> agentsByClass;

    private calculateCreatedAgentsByClass(Map<String, Long> baseStats) {
        Set<String> keys = baseStats.keySet();

        for (String key : keys) {
            if (key.startsWith("Created agents")) {
                String className = getInParenthesis(key);
                agentsByClass.put(className, baseStats.get(key));
            }
        }
    }

    private void computeStats() {
        Map<String, Long> baseStats = getMap();
        Set keys = baseStats.keySet();
        calculateCreatedAgentsByClass(baseStats);
        stats = new Hashtable();
        
        for (String key : keys) {
            if (key.startsWith("Moves")) {
                String className = getInParenthesis(key);
                long nbAgents = agentsByClass(className).longValue();
                double movesByAgent;
                movesByAgent = baseStats.get(key).longValue() / nbAgents;
                stats.put("Average moves by agent for " + className,
                          new Long(movesByAgent));
            }
        }
    }

    private String getInParenthesis(String base) {
        int startPar = key.lastIndexOf('(');
        int endPar = key.lastIndexOf(')');
        String className = key.substring(startPar + 1, endPar - 1);
        return className;
    }

    public Map getStats() {
        computeStats();
        return stats;
    }

}
