/**
 * This class needs to be sub-classed for writing the Agent oriented
 * algorithms
 */

package visidia.simulation;

import java.util.Hashtable;
import java.lang.reflect.Constructor;

import visidia.tools.WithWhiteBoard;
import visidia.tools.WhiteBoard;

public abstract class Agent implements Runnable, WithWhiteBoard {
    
    private Simulator simulator;
    private WhiteBoard whiteBoard;

    //A specific AgentMover for this Agent
    private AgentMover agentMover = null;

    //The agent's identifier (unique for each Agent)
    private int agentIdentity;
    
    //The number of created Agents
    private static int createdAgentCount = 0;

    public Agent(Simulator sim) {
        this(sim, new Hashtable());
    }

    public Agent(Simulator sim, Hashtable defaultValues) {
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
                {Agent.class, Simulator.class});

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
        //VisidiaAssertion.verify( agentMover != null , "In move() : The AgentMover hasn't been specified yet !", this);
        
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
     * Méthode de l'interface Runnable
     */
    public final void run() {
        init();
    };

    /**
     * Méthode qui spécifie l'action de chaque agent
     */
    protected abstract void init();
}
