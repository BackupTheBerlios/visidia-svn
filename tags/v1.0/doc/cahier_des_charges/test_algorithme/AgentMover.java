import java.lang.reflect.Constructor;

/**
 * Classe abstraite  apportant certains types de  deplacement pour les
 * agents. Une  des sous-classe est LinearAgentMover.  On pourra aussi
 * implementer un RandomAgentMover.
 */
public abstract class AgentMover {

  private Agent agent;
  private Simulator simulator;

  public AgentMover(Agent ag, Simulator sim) {
    this.agent = ag;
    this.simulator = sim;
  }

  public static AgentMover createAgentMover(String agentMoverClassName, 
                                            Agent agent,
                                            Simulator simulator) {
    Constructor  constructor;
    Class agClass;

    try {
      agClass = Class.forName(agentMoverClassName);
      constructor = agClass.getConstructor(new Class []
          {Agent.class, Simulator.class});

      return (AgentMover) 
        constructor.newInstance(new Object[]{agent, simulator});

    } catch (Exception e) {
      throw new IllegalArgumentException("Ne peut creer l'instance !");
    }
  }

  protected final Agent agent() {
    return agent;
  }

  /**
   * Deplace l'agent sur la porte suivante
   */
  public final void move() {
    move(findNextDoor());
  }
    
  /**
   * Deplace l'agent sur une porte donne
   */
  public final void move(int door) {
    simulator.moveAgentTo(agent(), door);
  }

  /** 
   * Retourne  la porte  dans laquelle  on va  aller.  Cette methode
   * devra etre specialisee dans les sous-classes.
   */
  protected abstract int findNextDoor();

}
