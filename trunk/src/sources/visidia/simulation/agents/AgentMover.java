package visidia.simulation.agents;

/**
 * Abstract class providing different moving types for the agents. You
 * should subclass this class to create your own style of move.
 *
 * @see visidia.agents.agentsmover
 */
public abstract class AgentMover {

    /**
     * Associated agent of the mover.
     */
    private Agent agent=null;

    /**
     * Creates a new agent mover.
     *
     * @param ag Agent associated with this mover.
     */
    public AgentMover(Agent ag) {
        this.agent = ag;
    }

    /**
     * Returns the agent associated with this mover.
     */
    protected final Agent agent() {
        return this.agent;
    }

    /**
     * Moves the agent to the next door.
     */
    public void move() throws InterruptedException {
        this.move(this.findNextDoor());
    }
    
    /**
     * Moves the agent to a specified door.
     *
     * @param door Door to which move.
     */
    public final void move(int door) throws InterruptedException {
        this.agent.moveToDoor(door);
    }

    /** 
     * Returns the door to which the agent will go.  This method needs
     * to be specialized in the sub-classes.
     */
    protected abstract int findNextDoor();

}
