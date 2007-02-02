package visidia.gui.metier.simulation;

//import java.awt.*;
//import java.awt.event.*;
import javax.swing.*;
import java.util.*;
//import java.io.*;
import visidia.misc.*;
import visidia.simulation.*;
import visidia.tools.*;
import visidia.gui.presentation.userInterfaceSimulation.*;
  
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
	stopped = true;
	interrupt();
    }
    
    public void run(){
	try{
	    while(!stopped){
		SimulEvent simEvt = null;
		try{
		    simEvt = (SimulEvent) evtPipe.get();
		}
		catch(ClassCastException e){
		    e.printStackTrace();
		    continue;
		}
		
		switch(simEvt.type()){
		    
		case SimulConstants.EDGE_STATE_CHANGE:
		    handleEdgeStateChangeEvt(simEvt);
		    break;
		    
		case SimulConstants.MESSAGE_SENT :
		    handleMessageSentEvt(simEvt);
		    break;

		case SimulConstants.NODE_PROPERTY_CHANGE :
		    handleNodePropertyChangeEvt(simEvt);
		    break; 

		case SimulConstants.ALGORITHM_END :
		    handleAlgorithmEndEvent(simEvt);
		    break;
		case SimulConstants.NEXT_PULSE :
		    handleNextPulseEvent(simEvt);
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
	if ( fenetreSimulDist == null ) {
	    if(esce.state() instanceof MarkedState){
		MarkedState state = (MarkedState)esce.state();
		((fenetreSimul.getVueGraphe()).rechercherArete(esce.nodeId1().toString(),esce.nodeId2().toString())).setEtat(state.isMarked());
		
	    }
	    else if(esce.state() instanceof SyncState){
		SyncState state = (SyncState)esce.state();
		((fenetreSimul.getVueGraphe()).rechercherArete(esce.nodeId1().toString(),esce.nodeId2().toString())).enluminerBis(state.isSynchronized());
		((fenetreSimul.getVueGraphe()).rechercherSommet(esce.nodeId1().toString())).enluminerBis(state.isSynchronized());
		((fenetreSimul.getVueGraphe()).rechercherSommet(esce.nodeId2().toString())).enluminerBis(state.isSynchronized());
		
	    }
	    EdgeStateChangeAck esca = new EdgeStateChangeAck(esce.eventNumber());
	    ackPipe.put(esca); 
	    
	}
	else {
	    if(esce.state() instanceof MarkedState){
		MarkedState state = (MarkedState)esce.state();
		((fenetreSimulDist.getVueGraphe()).rechercherArete(esce.nodeId1().toString(),esce.nodeId2().toString())).setEtat(state.isMarked());
		
	    }
	    else if(esce.state() instanceof SyncState){
		SyncState state = (SyncState)esce.state();
		((fenetreSimulDist.getVueGraphe()).rechercherArete(esce.nodeId1().toString(),esce.nodeId2().toString())).enluminerBis(state.isSynchronized());
		((fenetreSimulDist.getVueGraphe()).rechercherSommet(esce.nodeId1().toString())).enluminerBis(state.isSynchronized());
		((fenetreSimulDist.getVueGraphe()).rechercherSommet(esce.nodeId2().toString())).enluminerBis(state.isSynchronized());
		
	    }
	    EdgeStateChangeAck esca = new EdgeStateChangeAck(esce.eventNumber());
	    ackPipe.put(esca); 
	}   
    }
    
    public void handleNodePropertyChangeEvt(SimulEvent se) throws InterruptedException{
	NodePropertyChangeEvent npce = (NodePropertyChangeEvent) se;
	if ( fenetreSimulDist == null ) {
	    Hashtable tableSommet = ((fenetreSimul.getVueGraphe()).rechercherSommet(npce.nodeId().toString())).getStateTable();
	    tableSommet.put(npce.getKey(),npce.getValue());
	    
	    NodePropertyChangeAck npca = new NodePropertyChangeAck(npce.eventNumber());
	    ackPipe.put(npca); 		
	}
	else {
	    Hashtable tableSommet = ((fenetreSimulDist.getVueGraphe()).rechercherSommet(npce.nodeId().toString())).getStateTable();
	    tableSommet.put(npce.getKey(),npce.getValue());
	    
	    NodePropertyChangeAck npca = new NodePropertyChangeAck(npce.eventNumber());
	    ackPipe.put(npca); 
	}
    }
    
      
    public void handleMessageSentEvt(SimulEvent se){
	MessageSendingEvent mse = (MessageSendingEvent) se;
	if ( fenetreSimulDist == null )
	    fenetreSimul.simulationPanel().animate(mse);
	else 
	    fenetreSimulDist.simulationPanel().animate(mse);
    }
    
    public void handleAlgorithmEndEvent(SimulEvent se) throws InterruptedException{
	if ( fenetreSimulDist == null ) {
	    //fenetreSimul.simulationPanel().pause();
	    fenetreSimul.but_pause();
	    JOptionPane.showMessageDialog(fenetreSimul,"Algorithms are terminated");
	    //fenetreSimul.simulationPanel().pause();
	    fenetreSimul.but_pause();
	    throw new InterruptedException();	
	}
	else {
	    fenetreSimulDist.simulationPanel().pause();
	    JOptionPane.showMessageDialog(fenetreSimulDist,"Algorithms are terminated");
	    throw new InterruptedException();	
	}   
    }

    private void handleNextPulseEvent(SimulEvent se) throws InterruptedException{
	NextPulseEvent npe = (NextPulseEvent)se;
	if ( fenetreSimulDist == null ) {
	    fenetreSimul.setUpTimeUnits(npe.pulse());
	    // se bloque jusqu'a la fin de la visualisation et donc de l'acheminement des messages
	    fenetreSimul.simulationPanel().nextPulseReady();
	    //System.out.println("SimulEventHandler debloquee");
	    // sert à débloquer le AckHandler
	    NextPulseAck npa = new NextPulseAck(npe.eventNumber());
	    ackPipe.put(npa);
	}	
    }
    
    
}


