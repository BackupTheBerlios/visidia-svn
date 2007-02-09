package visidia.gui.metier.simulation;

import java.util.Hashtable;

import javax.swing.JOptionPane;

import visidia.gui.presentation.userInterfaceSimulation.AgentsSimulationWindow;
import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulation;
import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulationDist;
import visidia.misc.MarkedState;
import visidia.misc.SyncState;
import visidia.simulation.AlgorithmEndAck;
import visidia.simulation.AlgorithmEndEvent;
import visidia.simulation.EdgeStateChangeAck;
import visidia.simulation.EdgeStateChangeEvent;
import visidia.simulation.MessageSendingEvent;
import visidia.simulation.NextPulseAck;
import visidia.simulation.NextPulseEvent;
import visidia.simulation.NodePropertyChangeAck;
import visidia.simulation.NodePropertyChangeEvent;
import visidia.simulation.SimulConstants;
import visidia.simulation.SimulEvent;
import visidia.tools.VQueue;
  
/** This class is responsible of dealing with the 
    events coming from the simulator */

public class SimulEventHandler extends Thread {

    // instance variables
    protected FenetreDeSimulation fenetreSimul;
    protected AgentsSimulationWindow agentsSimulationWindow;
    protected FenetreDeSimulationDist fenetreSimulDist;
    /* events file from the simulator
     * that will be traduce to the graphics interface
     * by a state change or a message sent */
    protected VQueue evtPipe ;
    /* ackitment file of events treated by the graphics interface */
    protected VQueue ackPipe ;

    boolean stopped = false;

    boolean synchroSimul = false;
	
	
    public SimulEventHandler(FenetreDeSimulation f, VQueue evtPipe, VQueue ackPipe){	
	this.evtPipe = evtPipe;
	this.ackPipe = ackPipe;
	this.fenetreSimul = f;
    }

    public SimulEventHandler(AgentsSimulationWindow w, VQueue evtPipe, VQueue ackPipe){	
	this.evtPipe = evtPipe;
	this.ackPipe = ackPipe;
	this.agentsSimulationWindow = w;
    }

    public SimulEventHandler(FenetreDeSimulationDist f, VQueue evtPipe, VQueue ackPipe){	
	this.evtPipe = evtPipe;
	this.ackPipe = ackPipe;
	this.fenetreSimulDist = f;
    }
    
    public void abort(){
	this.stopped = true;
	this.interrupt();
    }
    
    public void run(){
	try{
	    while(!this.stopped){
		SimulEvent simEvt = null;
		try{
		    simEvt = (SimulEvent) this.evtPipe.get();
		}
		catch(ClassCastException e){
		    e.printStackTrace();
		    continue;
		}
		
		switch(simEvt.type()){
		    
		case SimulConstants.EDGE_STATE_CHANGE:
		    this.handleEdgeStateChangeEvt(simEvt);
		    break;
		    
		case SimulConstants.MESSAGE_SENT :
		    this.handleMessageSentEvt(simEvt);
		    break;

		case SimulConstants.NODE_PROPERTY_CHANGE :
		    this.handleNodePropertyChangeEvt(simEvt);
		    break; 

		case SimulConstants.ALGORITHM_END :
		    this.handleAlgorithmEndEvent(simEvt);
		    break;
		case SimulConstants.NEXT_PULSE :
		    this.handleNextPulseEvent(simEvt);
		    break;
		}
	    }
	}
	catch(InterruptedException e){
	    // break;
	}
		
    }
    
    public void handleEdgeStateChangeEvt(SimulEvent se) throws InterruptedException{
	EdgeStateChangeEvent esce = (EdgeStateChangeEvent) se;
	if ( this.fenetreSimulDist == null ) {
	    if(esce.state() instanceof MarkedState){
		MarkedState state = (MarkedState)esce.state();
		((this.fenetreSimul.getVueGraphe()).rechercherArete(esce.nodeId1().toString(),esce.nodeId2().toString())).setEtat(state.isMarked());
		
	    }
	    else if(esce.state() instanceof SyncState){
		SyncState state = (SyncState)esce.state();
		((this.fenetreSimul.getVueGraphe()).rechercherArete(esce.nodeId1().toString(),esce.nodeId2().toString())).enluminerBis(state.isSynchronized());
		((this.fenetreSimul.getVueGraphe()).rechercherSommet(esce.nodeId1().toString())).enluminerBis(state.isSynchronized());
		((this.fenetreSimul.getVueGraphe()).rechercherSommet(esce.nodeId2().toString())).enluminerBis(state.isSynchronized());
		
	    }
	    EdgeStateChangeAck esca = new EdgeStateChangeAck(esce.eventNumber());
	    this.ackPipe.put(esca); 
	    
	}
	else {
	    if(esce.state() instanceof MarkedState){
		MarkedState state = (MarkedState)esce.state();
		((this.fenetreSimulDist.getVueGraphe()).rechercherArete(esce.nodeId1().toString(),esce.nodeId2().toString())).setEtat(state.isMarked());
		
	    }
	    else if(esce.state() instanceof SyncState){
		SyncState state = (SyncState)esce.state();
		((this.fenetreSimulDist.getVueGraphe()).rechercherArete(esce.nodeId1().toString(),esce.nodeId2().toString())).enluminerBis(state.isSynchronized());
		((this.fenetreSimulDist.getVueGraphe()).rechercherSommet(esce.nodeId1().toString())).enluminerBis(state.isSynchronized());
		((this.fenetreSimulDist.getVueGraphe()).rechercherSommet(esce.nodeId2().toString())).enluminerBis(state.isSynchronized());
		
	    }
	    EdgeStateChangeAck esca = new EdgeStateChangeAck(esce.eventNumber());
	    this.ackPipe.put(esca); 
	}   
    }
    
    public void handleNodePropertyChangeEvt(SimulEvent se) throws InterruptedException{
	NodePropertyChangeEvent npce = (NodePropertyChangeEvent) se;
	if ( this.fenetreSimulDist == null ) {
	    Hashtable tableSommet = ((this.fenetreSimul.getVueGraphe()).rechercherSommet(npce.nodeId().toString())).getStateTable();
	    tableSommet.put(npce.getKey(),npce.getValue());
	    
	    NodePropertyChangeAck npca = new NodePropertyChangeAck(npce.eventNumber());
	    this.ackPipe.put(npca); 		
	}
	else {
	    Hashtable tableSommet = ((this.fenetreSimulDist.getVueGraphe()).rechercherSommet(npce.nodeId().toString())).getStateTable();
	    tableSommet.put(npce.getKey(),npce.getValue());
	    
	    NodePropertyChangeAck npca = new NodePropertyChangeAck(npce.eventNumber());
	    this.ackPipe.put(npca); 
	}
    }
    
      
    public void handleMessageSentEvt(SimulEvent se){
	MessageSendingEvent mse = (MessageSendingEvent) se;
	if ( this.fenetreSimulDist == null )
	    this.fenetreSimul.simulationPanel().animate(mse);
	else 
	    this.fenetreSimulDist.simulationPanel().animate(mse);
    }
    
    public void handleAlgorithmEndEvent(SimulEvent se) throws InterruptedException{
	AlgorithmEndEvent aee = (AlgorithmEndEvent)se;
	if ( this.fenetreSimulDist == null ) {
	    // fenetreSimul.simulationPanel().pause();
	    //fenetreSimul.but_pause();
	    this.fenetreSimul.simulationPanel().terminatedAlgorithm();
	    JOptionPane.showMessageDialog(this.fenetreSimul,"Algorithms are terminated");
	    // fenetreSimul.simulationPanel().pause();
	    //fenetreSimul.but_pause();
	    
	    AlgorithmEndAck aea = new AlgorithmEndAck(aee.eventNumber());
	    // System.out.println("Threads have terminated: waiting for GUI termination.");
	    this.ackPipe.put(aea);
	    
	    throw new InterruptedException();	
	}
	else {
	    this.fenetreSimulDist.simulationPanel().pause();
	    JOptionPane.showMessageDialog(this.fenetreSimulDist,"Algorithms are terminated");
	    throw new InterruptedException();	
	}   
    }

    private void handleNextPulseEvent(SimulEvent se) throws InterruptedException{
	NextPulseEvent npe = (NextPulseEvent)se;
	if ( this.fenetreSimulDist == null ) {
	    this.fenetreSimul.setUpTimeUnits(npe.pulse());
	    // se bloque jusqu'a la fin de la visualisation et donc de l'acheminement des messages
	    this.fenetreSimul.simulationPanel().nextPulseReady();
	    //System.out.println("SimulEventHandler debloquee");
	    // sert à débloquer le AckHandler
	    NextPulseAck npa = new NextPulseAck(npe.eventNumber());
	    this.ackPipe.put(npa);
	}	
    }
    
    
}


