package visidia.network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;

import visidia.graph.SimpleGraph;
import visidia.misc.EdgeState;
import visidia.misc.Message;
import visidia.misc.MessageType;
import visidia.simulation.AlgorithmDist;
import visidia.tools.LocalNodeTable;
import visidia.tools.VQueue;

/** This class represents a distributed simulator called also the "console".
 * It gothers messages exchanging by the nodes, communicates them to the GUI
 * so they can be visualized.
 * It plays an important role  when runing the nodes on the different hosts 
 * that the users choose.
 * The "console" is a RMI distributed object which is know by all the nodes 
 * and by the GUI. 
 * This is the interface for the Simulato_Rmi implementation. Note that it extends the 
 * java.rmi.remote class
 *
 * @author DERBEL Bilel
 * @version 1.0
 */
public interface Simulator_Rmi_Int extends Remote {

    /** the queue where the node incoming events (sending of messages,
     * state changing ...) are saved before the GUI analyse them
     *
     */
    public VQueue evtVQueue() throws RemoteException;

    /** the queue where the visualization acknowleges from the GUI are put  
     */
    public VQueue ackVQueue() throws RemoteException;

    /** This method allow a node to let the console know that one of 
     * its properties (e.g : label) has changed
     */
    public void pushNodePropertyChangeEvent(Integer nodeId, Object key, Object value)throws InterruptedException, RemoteException; 
    
    /** This method allow a node to let the console know that one of 
     * its edge properties (e.g : Synchronization) has changed 
     */
    public void pushEdgeStateChangeEvent(Integer nodeId1, Integer nodeId2, EdgeState es)throws InterruptedException, RemoteException; 
    
    /** This method allow a node to let the console know that he will send 
     *	a message a neighbor 
     */
    public void pushMessageSendingEvent(Integer senderId, int door,Integer receiverId, Message msg) throws InterruptedException, RemoteException ;
    
    /** This method allow a node to let the console know that the algorithm 
     * running is terminated
     */
    public void terminatedAlgorithm() throws RemoteException;
    
    /** This method is used to make the console abort the simulation
     */
    public void abortSimulation()  throws RemoteException ;

    /** This method is used in the begining of the simulation. 
     * It contact the LocalNodes running on the hosts taking part of the 
     * simulation and make them initializing the remote object represinting 
     * each nodes. 
     */
    public void initializeNodes(LocalNodeTable networkParam) throws RemoteException;

    /** After creating the remote-node abject by the setReseaux method.
     * This method order each remote node to begin running the distributed 
     * algorithm given in parameter.   
     */
    public void startServer(AlgorithmDist algo) throws RemoteException ;
    
    /** for the "pause" button in the GUI
     */
    public void wedge()   throws RemoteException ;
    public void unWedge() throws RemoteException ;
    
    /** this method allow the user to set a property (e.g : label) 
     * of node even when the allgorithm is running.
     */
    public void setNodeProperties(int nodeId, Hashtable properties) throws RemoteException ;
    
    /** set the drawning of messages to bool 
     */
    public void setNodeDrawingMessage(boolean bool) throws RemoteException ;

    /** change the state of the messages used in algorithms to bool.
     * if bool is true then this type is drawn, otherwise it isn't 
     * sent to the "console" so to be drawn.
     */
    public void setMessageType(MessageType msgType, boolean state) throws RemoteException ;
    
    /** return the stubs of all the created nodes
     */
    public Hashtable getGraphStub() throws RemoteException;

    public int getMessageNumber() throws RemoteException;
    
    /** used only when running Visidia as an applet.
     * It doesn't work for the moment. This method sets the simulation graph 
     * to graphApplet
     */
    public void setGraph(SimpleGraph graphApplet) throws RemoteException;

    public void register(NodeServerImpl nsi, String host, String url) throws RemoteException;
    
}




