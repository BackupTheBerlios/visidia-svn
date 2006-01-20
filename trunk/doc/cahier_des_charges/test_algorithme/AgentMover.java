/**
 * Classe abstraite  apportant certains types de  deplacement pour les
 * agents. Une  des sous-classe est LinearAgentMover.  On pourra aussi
 * implementer un RandomAgentMover.
 */
public abstract class AgentMover {

    private Agent ag;

    public AgentMover (Agent ag) {
        setAgent(ag);
    }

    protected final void setAgent(Agent ag) {
        this.ag = ag;
    }

    protected final Agent agent() {
        return this.ag;
    }

    /**
     * Deplace l'agent sur la porte suivante
     */
    public final void step() {
        agent().goToDoor(findNextDoor());
    }

    /** 
     * Retourne la porte dans laquelle on va aller
     */
    private abstract int findNextDoor();

}
