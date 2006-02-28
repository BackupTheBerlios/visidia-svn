//import java.lang.reflect.Constructor;

package visidia.simulation;

/**
 * Abstract class providing different moving types for the agents. 
 */
public abstract class AgentMover {
    
  private Agent agent;
  private Simulator simulator;

  public AgentMover(Agent ag, Simulator sim) {
    this.agent = ag;
    this.simulator = sim;
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
  public final void move() {
    move(findNextDoor());
  }
    
  /**
   * Move the agent to a specified door.
   */
  public final void move(int door) {
    simulator.moveAgentTo(agent(), door);
  }

  /** 
   * Return the door to which the agent will go.  This method needs to
   * be specialized in the sub-classes.
   */
  protected abstract int findNextDoor();

}
