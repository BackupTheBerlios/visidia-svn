package visidia.agents.agentsmover;

import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.agents.Agent;
import visidia.simulation.agents.AgentMover;

import java.util.Arrays;

/**
 * Provide a linear move for an Agent. On a vertex, the agent go to
 * the first never-visited door.
 *
 * /!\ Warning, this implementation imply that the vertex have an unique
 * identifier !!!
 */
public class LinearAgentMover extends AgentMover {
    
    // Remember the door on which the agent will go next time
    int[] nextDoorToGo;

    public LinearAgentMover(Agent ag) {
        super(ag);
        nextDoorToGo = new int [ag.getNetSize()];

        /* Start on the first door */
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
