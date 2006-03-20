package visidia.simulation.agents;

/**
 * Abstract class providing different moving types for the agents. 
 */
public abstract class AgentMover {
    
    private Agent agent=null;

    public AgentMover(Agent ag) {
        agent = ag;
    }

    /**
     * Return the agent.
     */
    protected final Agent agent() {
        return agent;
    }

    /**
     * Move the agent to the next door.
     */
    public final void move() throws InterruptedException {
        move(findNextDoor());
    }
    
    /**
     * Move the agent to a specified door.
     */
    public final void move(int door) throws InterruptedException {
        agent.moveToDoor(door);
    }

    /** 
     * Return the door to which the agent will go.  This method needs to
     * be specialized in the sub-classes.
     */
    protected abstract int findNextDoor();

}
