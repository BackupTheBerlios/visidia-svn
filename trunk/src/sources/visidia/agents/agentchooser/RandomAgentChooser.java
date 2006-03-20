package visidia.agents.agentchooser;

import java.util.Random;

import visidia.simulation.agents.AgentChooser;

/**
 * Allow user  to randomize agent position. To  specialize this class,
 * override   agentName()  and/or   probability().  This   class  puts
 * BasicAgent with a probability of 1/2.
 *
 * @see RandomSynchronizedChooser
 */
public class RandomAgentChooser extends AgentChooser {

    Random generator=null;

    /**
     * Implement  abstract  AgentChooser#chooseForVertex(Integer). For
     * each call, I decide  with a probability given by #probability()
     * if an agent  will be placed here or not. The  agents are of the
     * type returned by #agentName().
     *
     * @see agentName()
     * @see probability()
     */
    protected final void chooseForVertex(Integer vertexIdentity) {
        if (choose()) {
            addAgent(vertexIdentity, agentName());
        }
    }

    /**
     * Returns a String which contain  an agent name. This one returns
     * "BasicAgent" and you may want to override this.
     *
     * @return You can  override this method to return  the agent name
     * you want. This one returns "BasicAgent".
     */
    protected String agentName() {
        return "BasicAgent";
    }

    /**
     * Probability  by  which adding  an  agent  on  one vertex.  This
     * methods returns 0.5 but you may want to override this.
     *
     * @return You can override  this method to return the probability
     * you want (between 0 and 1). This one returns 0.5.
     */
    protected float probability() {
        return (float)0.5;
    }

    /**
     * Return a  random boolean  with a probability  of True  given by
     * probability().
     *
     * @return true with a probability given by probability(). Returns
     * false otherwise.
     */
    private final boolean choose() {

        if (generator == null)
            generator = new Random();

        float probability = probability();
        float rand = generator.nextFloat();

        if (probability < 0.0 || probability > 1.0)
            throw new IllegalArgumentException("Probability must be "
                                               + "between 0 and 1.");
        return (rand < probability());
    }
}
