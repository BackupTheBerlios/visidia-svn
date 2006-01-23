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

    public static AgentMover createAgentMover(String agentMoverClass, 
                                              Agent agent,
                                              Simulator simulator) {
        Class [] paramsType = new Class[2];
        Object [] params = new Object[2];
        Constructor  constructor;

        try {
            paramsType[0] = Class.forName("Agent");
            paramsType[1] = Class.forName("Simulator");

            constructor = Class.forName(agentMoverClass).getConstructor(paramsType);

            params[0] = agent;
            params[1] = simulator;

            return (AgentMover) constructor.newInstance(params);
        }           catch           (Exception          e)           {
            throw new IllegalArgumentException("Ne peut creer la classe !");
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
