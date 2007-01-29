package visidia.simulation.agents;

import visidia.tools.Bag;

import visidia.simulation.agents.stats.*;

/**
 * Base abstract class  for all reports you want  to develop. You must
 * override #getStats() in order to provide a new report.
 *
 * @see visidia.agents.agentreport For different kind of reports.
 */
public abstract class AbstractStatReport {

    /**
     * Contains events reported by the simulator.
     */
    private Bag stats;

    public AbstractStatReport() {}

    public final void setStats(Bag stats) {
        this.stats = stats;
    }
    
    /**
     * Returns  all  the  events   reported  by  the  simulator  in  a
     * bag. Subclasses should  call this method when they  want to get
     * the events to start working on their own report.
     */
    protected Bag getBag() {
        return stats;
    }

    /**
     * Return a new report. Subclasses  must override this in order to
     * provide  their own  report. The  simplest implementation  is to
     * return  here what  #getBag() returns  in order  to get  all the
     * event in one report.
     *
     * @see visidia.agents.agentreport.GlobalReport#getStats For the
     * simplest implementation
     *
     * @see     visidia.agents.agentreport.AverageMoveReport#getStats
     * For a more complicated implementation
     *
     */
    public abstract Bag getStats();
}
