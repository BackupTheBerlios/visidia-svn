/**
 * I'm an abstract class used  to implement agent based algorithms. If
 * you  want   to  write  a   new  Agent,  subclass  me   or  subclass
 * SynchronizedAgent which allows you to get synchronisation.
 *
 * @see SynchronizedAgent
 */
package visidia.simulation.agents;

import java.util.Hashtable;
import java.lang.reflect.Constructor;

import visidia.tools.agents.WithWhiteBoard;
import visidia.tools.agents.WhiteBoard;
import visidia.misc.EdgeState;
import visidia.misc.MarkedState;

import visidia.simulation.SimulationAbortError;

import visidia.visidiassert.*;

public abstract class Agent implements Runnable, WithWhiteBoard {
    
    /**
     * A  link  to  the   simulator  responsible  for  nearly  all  my
     * actions. I can't do anything alone.
     */
    private AgentSimulator simulator;

    /**
     * A  WhiteBoard allows  one to  store  values with  keys (like  a
     * Hashtable). With  this, I can store information  during my live
     * time. 
     * 
     * @see getProperty()
     * @see setProperty()
     */
    private WhiteBoard whiteBoard;

    /**
     * An AgentMover  is used to move  myself in a specific  way. If I
     * want to move randomly, I can use RandomAgentMover for example.
     *
     * @see setAgentMover()
     * @see move()
     */
    private AgentMover agentMover = null;

    /**
     * Unique identifier over all the agents.
     */
    private int agentIdentity;
    
    /**
     * Number  of agents  in  the  simulation. Used  to  set a  unique
     * identifier. 
     *
     * @see agentIdentity
     */
    private static int createdAgentCount = 0;

    /**
     * Default constructor. Create a new agent and assign it an unique
     * identifier. Don't forget to  use setSimulator() because I can't
     * do anything without an AgentSimulator.
     *
     * @see setSimulator()
     */
    public Agent() {
	agentIdentity = createdAgentCount++;
    }

    /**
     * Affet a simulator  to this agent. This is  mandatory because an
     * agent can't do anything by itself.
     *
     * @param simulator Affect this simulator to the agent
     */
    public void setSimulator(AgentSimulator simulator) {
        this.simulator = simulator;
    }

    /**
     * Remove the existing white board and affect this one.
     *
     * @param wb The white board to affect.
     * @see getWhiteBoard()
     */
    public void setWhiteBoard(WhiteBoard wb) {
        this.whiteBoard = wb;
    }

    /**
     * Create a new WhiteBoard with defaults values.
     */
    public void setWhiteBoard(Hashtable defaults) {
        this.whiteBoard = new WhiteBoard(defaults);
    }

    /**
     * Return the white board associated with this agent.
     *
     * @see setWhiteBoard()
     */
    public WhiteBoard getWhiteBoard() {
        return whiteBoard;
    }

    
    /**
     * Create a  new agent  mover based on  the name in  parameter and
     * affect it to me.
     *
     * @param   agentMoverClassName    a   String   representing   the
     * AgentMoverClass. Like \a RandomAgentMover for exemple.
     * @see setAgentMover(AgentMover)
     */
    public void setAgentMover(String agentMoverClassName) {
        Constructor  constructor;
        Class agClass;

        try {
            String completName;
            completName = new String("visidia.agentsmover."
                                     + agentMoverClassName);
            agClass = Class.forName(completName);
            constructor = agClass.getConstructor(new Class []
                {Agent.class, AgentSimulator.class});

            setAgentMover( (AgentMover) 
                           constructor.newInstance(new Object[]
                               {this, simulator}));
            
        } catch (Exception e) {
            throw new 
                IllegalArgumentException("Instance can't be created !");
        }
    }
    
    /**
     * Use the AgentMover as parameter to move me.
     *
     * @see setAgentMover(String)
     */
    public void setAgentMover(AgentMover am) {
        agentMover = am;
    }

    /**
     * Return the current AgentMover.
     */
    public AgentMover getAgentMover() {
        return agentMover;
    }

    /**
     * Return the door from which the agents come from.
     */
    protected int entryDoor() {
        return simulator.entryDoor(this);
    }

    /**
     * Low level method to move me.  You might prefer to use move() in
     * conjunction with an AgentMover.
     *
     * @param The door to which move
     * @see setAgentMover(String)
     * @see move()
     */
    public void moveToDoor(int door) {
        try {
            simulator.moveAgentTo(this, door);
        } catch (InterruptedException e) {
            throw new SimulationAbortError(e);
        }
    }

    /**
     * Move the  Agent using  the AgentMover. You  should have  set an
     * AgentMover   before   that   using   setAgentMover(String)   or
     * setAgentMover(AgentMover).
     *
     * @see setAgentMover(String)
     * @see setAgentMover(AgentMover)
     */
    public void move() {
        VisidiaAssertion.verify( agentMover != null ,
                                 "In move() : The AgentMover hasn't been " +
                                 "specified yet !",
                                 this);
        try {
            agentMover.move();
        } catch (InterruptedException e) {
            throw new SimulationAbortError(e);
        }
    }
    
    /**
     * Moves the Agent back to the vertex from where it comes.
     */
    public void moveBack() {
	moveToDoor(entryDoor());
    }

    /**
     * Return the number of doors available from the vertex I'm in.
     */
    public int getArity() {
        return simulator.getArity(this);
    }

    /**
     * Use this method  if you want to fall asleep  for a given amount
     * of milliseconds.
     *
     * @param Milliseconds to sleep.
     */
    protected void sleep(long millis) {
        try {
            simulator.sleep(this, millis);
        } catch (InterruptedException e) {
            throw new SimulationAbortError(e);
        }
    }

    /**
     * Return the number of vertices in the graph.
     */
    public int getNetSize() {
        return simulator.getNetSize();
    }

    /**
     * Return  the  unique identifier  that  identifies the  currently
     * visited vertex.
     */
    public int getVertexIdentity() {
        return simulator.getVertexIdentity(this);
    }

    /**
     * Return the unique identifier that identifies me.
     */
    protected int getIdentity() {
        return agentIdentity;
    }

    /**
     * Get a value from the WhiteBoard.
     *
     * @param key Key behind which found the value.
     * @see setProperty()
     * @see setWhiteBoard()
     */
    public Object getProperty(Object key) {
        return whiteBoard.getValue(key);
    }

    /**
     * Put a value in the WhiteBoard. The key reference this value.
     *
     * @see getProperty()
     * @see setWhiteBoard()
     * @param key Key on which I must store the value
     * @param value Value that must be stored
     */
    public void setProperty(Object key, Object value) {
        whiteBoard.setValue(key, value);
    }
        
    /**
     * Like getProperty(), but for  the current vertex. Get a property
     * behind a key on the vertex.
     *
     * @param key Key behind which value will be find
     */
    public Object getVertexProperty(Object key) {
        return simulator.getVertexProperty(this, key);
    }

    /**
     * Set a value on the current vertex.
     *
     * @param key Key behind which storing the value
     * @param value Value to store on the vertex
     */
    public void setVertexProperty(Object key, Object value) {
        simulator.setVertexProperty(this, key, value);
    }

    /**
     * Used to change the edge associated with the door on the current
     * vertex.
     *
     * @param state The new state to affect to the edge
     * @param door Door from which the edge will be changed
     */
    public void changeDoorState(int door, EdgeState state) {
        try {
            simulator.changeDoorState(this, door, state);
        } catch (InterruptedException e) {
            throw new SimulationAbortError(e);
        }
    }

    /**
     * A  shortcut to  changeDoorState(). It  marks the  last  edge on
     * which the agents has moved.
     */
    public void markEntryDoor() {
        changeDoorState(entryDoor(), new MarkedState(true));
    }

    /**
     * Create a new agent of the same type in the same vertex.
     */
    public void cloneAgent() {
	simulator.clone(this);
    }

    /**
     * Create a  new agent of the  same type and  put it on one  of my
     * neighboor vertex.
     *
     * @param door Door where to send the clone.
     */
    public void cloneAndSend(int door) {
        try {
            simulator.cloneAndSend(this, door);
        } catch (InterruptedException e) {
            throw new SimulationAbortError(e);
        }
    }

    /**
     * Return a String which represent the Agent : the name of the
     * agent class, an '_' and the identity number of the agent. 
     */
    public String toString() {
	return this.getClass().getName() + "_" + getIdentity();
    }


    /**
     * Method of the Runnable interface. Launch init().
     *
     * @see init();
     */
    public final void run() {
        init();
	death();
    };

    /**
     * Override this  method to implement  your agent.
     */
    protected abstract void init();

    /**
     * Kill the agent.
     */
    protected void death() {
        try {
            simulator.agentDeath(this);
        } catch (InterruptedException e) {
            throw new SimulationAbortError(e);
        }
    };

}
