package visidia.agents.agentstats;

import visidia.simulation.agents.AbstractStatReport;

import visidia.tools.Bag;

/**
 * My  aim is to  render all  the events.  I'm the  simplest statistic
 * report.
 */
public class GlobalReport extends AbstractStatReport {

    public Bag getStats() {
        return getBag();
    }
}
