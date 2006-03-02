/**
 * This class needs to be sub-classed for writing the Agent oriented
 * algorithms
 */

package visidia.simulation.agents;

import java.util.Hashtable;
import java.lang.reflect.Constructor;

import visidia.tools.agents.WithWhiteBoard;
import visidia.tools.agents.WhiteBoard;

import visidia.visidiassert.*;

public abstract class Agent implements Runnable, WithWhiteBoard {
    
    private AgentSimulator simulator;
    private WhiteBoard whiteBoard;

    //A specific AgentMover for this Agent
    private AgentMover agentMover = null;

    //The agent identifier (unique for each Agent)
    private int agentIdentity;
    
    //The number of created Agents
    private static int createdAgentCount = 0;

    public Agent(AgentSimulator sim) {
        this(sim, new Hashtable());
    }

    public Agent(AgentSimulator sim, Hashtable defaultValues) {
        simulator = sim;
        whiteBoard = new WhiteBoard(defaultValues);
	agentIdentity = createdAgentCount++;
    }

    public void setWhiteBoard(WhiteBoard wb) {
        this.whiteBoard = wb;
    }

    public WhiteBoard getWhiteBoard() {
        return whiteBoard;
    }

    
    public void setAgentMover(String agentMoverClassName) {
        Constructor  constructor;
        Class agClass;

        try {
            String completName = new String("visidia.agentsmover." + agentMoverClassName);
            agClass = Class.forName(completName);
            constructor = agClass.getConstructor(new Class []
                {Agent.class, AgentSimulator.class});

            System.out.println(agClass.getName());

            setAgentMover( (AgentMover) 
                           constructor.newInstance(new Object[]
                               {this, simulator}));
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Instance can't be found !");
        }
    }
    
    public void setAgentMover(AgentMover am) {
        agentMover = am;
    }

    public AgentMover getAgentMover() {
        return agentMover;
    }

    public void moveToDoor(int door) {
        simulator.moveAgentTo(this, door);
    }

    /**
     * Move the Agent using the AgentMover
     */
    public void move() {
        VisidiaAssertion.verify( agentMover != null ,
                                 "In move() : The AgentMover hasn't been specified yet !",
                                 this);
        
        agentMover.move();
    }

    //protected
    public int getArity() {
        return simulator.getArity(this);
    }

    protected void sleep(long millis) {
        simulator.sleep(this, millis);
    }

    //protected
    public int getNetSize() {
        return simulator.getNetSize();
    }

    //protected
    public int getVertexIdentity() {
        return simulator.getVertexIdentity(this);
    }

    protected int getIdentity() {
        return agentIdentity;
    }

    public Object getProperty(Object key) {
        return whiteBoard.getValue(key);
    }

    public void setProperty(Object key, Object value) {
        whiteBoard.setValue(key, value);
    }
        
    public Object getVertexProperty(Object key) {
        return simulator.getVertexProperty(this, key);
    }

    public void setVertexProperty(Object key, Object value) {
        simulator.setVertexProperty(this, key, value);
    }

    public void cloneAndSend(int door) {
        simulator.cloneAndSend(this, door);
    }

    /**
     * Method of the Runnable interface. Launch init().
     *
     * @see init();
     */
    public final void run() {
        init();
	simulator.agentDeath(this);
    };

    /* Do nothing now, but may change */
    public void death() {};

    /* Agent Suicide */
    public void killMe() {
	simulator.killAgent(this);
    }

    public void killAgent(Agent ag) {
	simulator.killAgent(ag);
    }

    /**
     * Override this  method to implement  your agent.
     */
    protected abstract void init();
}
