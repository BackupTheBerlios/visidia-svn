package fr.enserb.das.simulation;


/**
 * This class overrride his super class uncaughtException() method to
 * handle uncaught exceptions from threads.
 */
class SimulatorThreadGroup extends ThreadGroup {
    /**
     * super class equivalente constructor.
     */
    SimulatorThreadGroup(String name){
	super(name);
    }

    /**
     * super class equivalente constructor.
     */
    SimulatorThreadGroup(ThreadGroup parent, String name){
	super(parent, name);
    }

    /**
     * Handle uncaught exceptions raised from this thread group
     * childs.
     */
    public void uncaughtException(Thread t, Throwable e){
	// The simulationAbortError is thrown to force threads 
	// go out thier run method. Other Throwable should
	// be handled normally.
	if(!(e instanceof SimulationAbortError)){
	    super.uncaughtException(t,e);
	}
    }
}
