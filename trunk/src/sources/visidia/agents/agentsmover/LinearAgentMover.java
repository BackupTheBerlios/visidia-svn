package visidia.agents.agentsmover;

import visidia.simulation.agents.Agent;
import visidia.simulation.agents.AgentMover;

import java.util.Arrays;

/**
 * Provides a linear  move for an Agent. On a vertex,  the agent go to
 * the first never-visited door.
 *
 * /!\ Warning,  this implementation implies  that each vertex  has an
 * unique identifier !!!
 */
public class LinearAgentMover extends AgentMover {
    
    // Remembers the door on which the agent will go next time
    int[] nextDoorToGo;

    /**
     * Constructor.  Allows to  create a  new AgentMover  for  a given
     * Agent.
     */
    public LinearAgentMover(Agent ag) {
        super(ag);
        nextDoorToGo = new int [ag.getNetSize()];

        /* Starts on the first door */
        Arrays.fill(nextDoorToGo, 0);
    }

    protected int findNextDoor() {
        int vertex = agent().getVertexIdentity();
        int doorToGo = nextDoorToGo[vertex];
        int arity = agent().getArity();

        /* The following door is the current one plus 1 */
        nextDoorToGo[vertex] = (nextDoorToGo[vertex] + 1) % arity;

        return doorToGo;
    }
}
