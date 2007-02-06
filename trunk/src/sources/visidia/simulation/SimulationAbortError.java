package visidia.simulation;

/**
 * An error that is thrown when an aborted thread try
 * to access simulation API.
 */
public class SimulationAbortError extends Error{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1021261555309603500L;

	public SimulationAbortError() {
        super();
    }

    public SimulationAbortError(Throwable cause) {
        super(cause);
    }
}
