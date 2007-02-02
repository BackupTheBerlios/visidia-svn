package visidia.simulation.agents.stats;

//import visidia.simulation.agents.Agent;

public class SleepStat extends AbstractAgentStat {

    public SleepStat(Class agClass) {
        super(agClass);
    }

    public String descriptionName() {
	return "Sleep time";
    }
}
