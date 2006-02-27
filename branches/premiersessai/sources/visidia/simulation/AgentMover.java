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

//   public static AgentMover createAgentMover(String agentMoverClassName, 
//                                             Agent agent,
//                                             Simulator simulator) {
//     Constructor  constructor;
//     Class agClass;

//     try {
//       agClass = Class.forName(agentMoverClassName);
//       constructor = agClass.getConstructor(new Class []
//           {Agent.class, Simulator.class});

//       return (AgentMover) 
//         constructor.newInstance(new Object[]{agent, simulator});

//     } catch (Exception e) {
//       throw new IllegalArgumentException("Instance can't be found !");
//     }
//   }

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
   * Return the door in which the agent will go on. This method need
   * to be specialized in the sub-classes.
   */
  protected abstract int findNextDoor();

}
