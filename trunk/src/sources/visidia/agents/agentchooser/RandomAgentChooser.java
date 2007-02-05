package visidia.agents.agentchooser;

import java.util.Random;
import java.util.Vector;

import visidia.simulation.agents.AgentChooser;
import visidia.misc.AlgoProbParam;

/**
 * Allows user to randomize agent position. To specialize this class,
 * override agentName() and/or probability().  This class puts
 * Handshake agents with a probability 1/2.
 *
 * @see RandomSynchronizedChooser
 */
public class RandomAgentChooser extends AgentChooser {


    private String algoName = "Handshake"; 
    private Vector<String> algoNames = new Vector<String>() ; 
    private float prob = (float)0.5;
    private Random generator= new Random();
    private Random genNames = new Random();


    /**
     * Implements  abstract AgentChooser#chooseForVertex(Integer). For
     * each call,  decides with a probability  given by #probability()
     * if an agent will be placed  there or not. The agents are of the
     * type returned by #agentName().
     *
     * @see #agentName()
     * @see #probability()
     */
    protected final void chooseForVertex(Integer vertexIdentity) {
        if (choose()) {
            addAgent(vertexIdentity, agentName());
        }
    }

    /**
     * Returns a String which contains  an agent name. This one returns
     * "Handshake" and you may want to override this.
     *
     * @return You can  override this method to return  the agent name
     * you want. This one returns "Handshake".
     */
    protected String agentName() {
	if(algoNames == null)
	    return algoName;
	else {
	    if(algoNames.size() == 1)
		return algoNames.elementAt(0);
	    else {
		int index = Math.abs(genNames.nextInt(algoNames.size()));
		return algoNames.elementAt(index);
	    }
	}
    }

    /**
     * Probability  of adding  an agent  on one  vertex.   This method
     * returns 0.5 but you may want to override this.
     *
     * @return You can override  this method to return the probability
     * you want (between 0 and 1). This one returns 0.5.
     */
    protected float probability() {
        return prob;
    }

    /**
     * Returns a random boolean with a probability of True equals to
     * the value returned by the probability() Method.
     *
     * @return true with a probability given by probability(). Returns
     * false otherwise.
     */
    private final boolean choose() {

        float probability = probability();
        float rand = Math.abs(generator.nextFloat());

        if (probability < 0.0 || probability > 1.0)
            throw new IllegalArgumentException("Probability must be "
                                               + "between 0 and 1.");
        return (rand < probability());
    }

    /**
     *  Overwrite AgentChooser#place() method. pre-processing : show a
     *  GUI allowing to customize how to choose agents. 
     *
     **/
    protected void place() {
	AlgoProbParam app = new AlgoProbParam(algoName,Float.toString(prob),this);
	app.start();
    }
    
    public void setParam(String algoName, float prob) {
	this.algoName = algoName;
	this.prob = prob;
	placeAgents();
    }

    
    public void setParam(float prob, Vector<String> algoNames) {
	this.algoNames = algoNames;
	this.prob = prob;
	placeAgents();
    }
}
