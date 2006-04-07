package visidia.gui.presentation.userInterfaceSimulation;

import visidia.tools.HashTableModel;
import visidia.rule.RelabelingSystem;
import visidia.graph.SimpleGraph;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import java.util.Map;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import visidia.gui.DistributedAlgoSimulator;
import visidia.gui.donnees.conteneurs.*;
import visidia.gui.donnees.*;
import visidia.gui.metier.simulation.*;
import visidia.gui.presentation.*;
import visidia.gui.presentation.boite.*;
import visidia.gui.metier.inputOutput.*;
import visidia.gui.presentation.userInterfaceEdition.*;
import visidia.algo.*;
import visidia.simulation.*;
import visidia.simulation.agents.AgentSimulator;
import visidia.tools.*;
import visidia.tools.agents.*;
import visidia.misc.MessageType;
import visidia.simulation.rules.*;
import visidia.simulation.synchro.*;
import visidia.rule.*;
import visidia.gui.presentation.starRule.*;
import visidia.simulation.synchro.synObj.*;
import visidia.simulation.agents.Agent;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import visidia.gui.donnees.conteneurs.*;
import visidia.gui.donnees.*;
import visidia.gui.metier.simulation.*;
import visidia.gui.presentation.*;
import visidia.gui.presentation.boite.*;
import visidia.gui.metier.inputOutput.*;
import visidia.gui.presentation.userInterfaceEdition.*;
import visidia.algo.*;
import visidia.simulation.*;

public class AgentExperimentationFrame extends JFrame implements ActionListener {
    
    ReadOnlyHashTableModel tableModel;	
    private JTable resultTable;
    private JScrollPane scrollPane;
    
    private JToolBar toolBar;
    private JButton but_start;
    private JButton but_abort;

    private VueGraphe graph;
    private Vector<RelabelingSystem> agentsRules;
    private Hashtable<String,Object> defaultProperties;
    private Hashtable agentsTable;

    private ExperimentationThread expThread;

    public AgentExperimentationFrame(Map stats) {
        initializeFrame(stats);
    }

    public AgentExperimentationFrame(VueGraphe graph, Hashtable agentsTable, 
                                     Hashtable<String,Object> defaultProperties,
                                     Vector<RelabelingSystem> agentsRules) {
	
        this.agentsRules = agentsRules;
        this.graph = graph;
        this.agentsTable = agentsTable;
        this.defaultProperties = defaultProperties;

        initializeFrame(new Hashtable());

        but_start = new JButton("Start");
        but_start.addActionListener(this);
      
        but_abort = new JButton("Abort");
        but_abort.addActionListener(this);
        
        toolBar = new JToolBar();
        toolBar.add(but_start);
        toolBar.add(but_abort);
        getContentPane().add(toolBar,BorderLayout.NORTH);
    }

    private void initializeFrame(Map stats) {
	tableModel = new ReadOnlyHashTableModel(stats);
	resultTable = new JTable(tableModel);
	scrollPane = new JScrollPane(resultTable);		
	getContentPane().add(scrollPane,BorderLayout.CENTER);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);        
    }
    
    private void initializeTable(Map stats) {

    }

    public HashTableModel getTableModel() {
        return tableModel;
    } 

    public void actionPerformed(ActionEvent evt) {
	if(evt.getSource() == but_start){
	    start();
	}
	if(evt.getSource() == but_abort){
	    abort();
	}
    }

    private void controlEnabling(){
        but_start.setEnabled(true);
        but_abort.setEnabled(false);
    }

    public void start() {
        if (expThread != null)
            abort();
        SimulatorThreadGroup threadGroup;
        getTableModel().setProperties(new Hashtable());
	threadGroup = new SimulatorThreadGroup("simulator");
	expThread = new ExperimentationThread(threadGroup, 
                                              Convertisseur.convert(graph.getGraphe(),
                                                             agentsTable,
                                                             defaultProperties),
                                              agentsRules, this);
	expThread.start();
    }
    
    public void abort() {
	if(expThread != null){
	    expThread.abortExperimentation();
	    expThread = null;
	}
    }
}

class ReadOnlyHashTableModel extends HashTableModel {
    
    public ReadOnlyHashTableModel(Map table){
        super(table);
    }
}

class ExperimentationThread extends Thread {
    boolean aborted = false;
    boolean terminated = true;
    private visidia.tools.VQueue eventVQueue;
    private visidia.tools.VQueue ackVQueue;
    private SimpleGraph graph;
    private Vector<RelabelingSystem> agentsRules;
    private AgentSimulator simulator;
    private AgentExperimentationFrame frame;
    private UpdateTableStats timer;

    public ExperimentationThread(ThreadGroup group, SimpleGraph graph, 
                                 Vector<RelabelingSystem> agentsRules,
                                 AgentExperimentationFrame frame){
        super(group, "Experimentation thread");
        this.agentsRules = agentsRules;
        this.graph = graph;
        this.frame = frame;
    }
    
    void abortExperimentation(){
        aborted = true;
	    
        while(isAlive()){
            interrupt();
            try{
                Thread.currentThread().sleep(10);
            }
            catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }
    
    
    public void run(){
            
        eventVQueue = new visidia.tools.VQueue();
        ackVQueue = new visidia.tools.VQueue();
		
        simulator = new AgentSimulator(graph,
                                       agentsRules,
                                       eventVQueue,
                                       ackVQueue);

        timer = new UpdateTableStats(simulator, frame.getTableModel());
        new Thread(timer).start();

        simulator.startSimulation();
        terminated = false;

        try{
            eventHandleLoop();
        }
        catch(InterruptedException e){
            //this interruption should have been cause
            //by the simulation stop.
            if( aborted && simulator != null){
                //abort current simulation
                simulator.abortSimulation();
            }
        }
        timer.stop();
    }
    
    public void eventHandleLoop() throws InterruptedException{
	    
        SimulEvent simEvt = null;
        while(!terminated){
            try{
                simEvt = (SimulEvent) eventVQueue.get();
                //		    Thread.currentThread().sleep(10);
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
            case SimulConstants.ALGORITHM_END :
                handleAlgorithmEndEvent(simEvt);
                break;
            case SimulConstants.AGENT_MOVED :
                handleAgentMovedEvt(simEvt);
                break;
            case SimulConstants.LABEL_CHANGE :
                break;
            case SimulConstants.NEXT_PULSE :
                break;
            default:
                throw new RuntimeException("Unknown event: " + simEvt.type());
            }
        }
    }

    public void handleEdgeStateChangeEvt(SimulEvent se) throws InterruptedException{
        EdgeStateChangeAck esca = new EdgeStateChangeAck(se.eventNumber());
        ackVQueue.put(esca);
    }
    public void handleMessageSentEvt(SimulEvent se) throws InterruptedException{
        MessageSendingAck msa = new MessageSendingAck(se.eventNumber());
        ackVQueue.put(msa);
    }
	
    public void handleAlgorithmEndEvent(SimulEvent se) throws InterruptedException{
        terminated = true;
    }

    public void handleAgentMovedEvt(SimulEvent se) throws InterruptedException {
        AgentMovedAck ack = new AgentMovedAck(se.eventNumber());
        ackVQueue.put(ack);
    }
}
    
