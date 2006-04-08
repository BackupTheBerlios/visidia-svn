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

import java.lang.ArrayIndexOutOfBoundsException;

import visidia.gui.DistributedAlgoSimulator;
import visidia.gui.donnees.conteneurs.*;
import visidia.gui.donnees.*;
import visidia.gui.metier.simulation.*;
import visidia.gui.presentation.*;
import visidia.gui.presentation.boite.*;
import visidia.gui.metier.inputOutput.*;
import visidia.gui.presentation.userInterfaceEdition.*;
import visidia.algo.*;
import visidia.agents.*;
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
import visidia.simulation.agents.*;

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
    private AbstractExperiment expType;

    private ExperimentationThread expThread;
    private SimulatorThreadGroup threadGroup;

    public AgentExperimentationFrame(Map stats) {
        initializeFrame(stats);
    }

    public AgentExperimentationFrame(VueGraphe graph, Hashtable agentsTable, 
                                     Hashtable<String,Object> defaultProperties,
                                     Vector<RelabelingSystem> agentsRules, AbstractExperiment expType) {
	
        this.agentsRules = agentsRules;
        this.graph = graph;
        this.agentsTable = agentsTable;
        this.defaultProperties = defaultProperties;
	this.expType = expType;

        initializeFrame(new Hashtable());

        but_start = new JButton("Start");
        but_start.addActionListener(this);
      
        but_abort = new JButton("Abort");
        but_abort.addActionListener(this);
        
        toolBar = new JToolBar();
        toolBar.add(but_start);
        toolBar.add(but_abort);
        getContentPane().add(toolBar,BorderLayout.NORTH);

        controlEnabling(true);
    }

    private void initializeFrame(Map stats) {
	tableModel = new ReadOnlyHashTableModel(stats);
	TableSorter sorter = new TableSorter(tableModel);
	resultTable = new JTable(sorter);
	sorter.setTableHeader(resultTable.getTableHeader());
	scrollPane = new JScrollPane(resultTable);		
	getContentPane().add(scrollPane,BorderLayout.CENTER);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);        
    }
    
    private void initializeTable(Map stats) {

    }

    public HashTableModel getTableModel() {
        return tableModel;
    } 

    public AbstractExperiment getExpType() {
	return expType;
    }

    public void actionPerformed(ActionEvent evt) {
	if(evt.getSource() == but_start){
	    start();
	}
	if(evt.getSource() == but_abort){
	    abort();
	}
    }

    private void controlEnabling(boolean enable){
        but_start.setEnabled(  enable );
        but_abort.setEnabled( !enable );
    }

    public void algoTerminated() {
        JOptionPane.showMessageDialog(this,"Algorithms are terminated");
        controlEnabling(true);
	if (expThread != null) {
	    expThread.abortExperimentation();
	    expThread = null;
	}
    }

    public void start() {
        if (expThread != null)
            abort();
        getTableModel().setProperties(new Hashtable());
        threadGroup = new SimulatorThreadGroup("simulator");
	expThread = new ExperimentationThread(threadGroup, 
                                              Convertisseur.convert(graph.getGraphe(),
                                                             agentsTable,
                                                             defaultProperties),
                                              agentsRules, this);
        controlEnabling(false);
	expThread.start();
    }
    
    public void abort() {
	if(expThread != null){
	    expThread.abortExperimentation();
	    expThread = null;
            controlEnabling(true);
	}
    }
}

class ReadOnlyHashTableModel extends HashTableModel {
    
    public ReadOnlyHashTableModel(Map table){
        super(table);
    }

    public Class getColumnClass(int column) {
	if(column == 0)
	    return String.class;
	else if(column == 1)
	    return Long.class;
	else
	    throw new ArrayIndexOutOfBoundsException();
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
	timer.stop();
        while(isAlive()){
            interrupt();
            try{
                Thread.currentThread().sleep(10);
            }
            catch(InterruptedException e){
		if (simulator != null)
		    simulator.abortSimulation();
                throw new SimulationAbortError(e);
            }
        }

	if (simulator != null)
	    simulator.abortSimulation();
    }
    
    
    public void run(){
            
        eventVQueue = new visidia.tools.VQueue();
        ackVQueue = new visidia.tools.VQueue();
		
        simulator = new AgentSimulator(graph,
                                       agentsRules,
                                       eventVQueue,
                                       ackVQueue);

	simulator.startSimulation();
	frame.getExpType().setStats(simulator.getStats());
	timer = new UpdateTableStats(simulator, frame.getExpType(), frame.getTableModel());
        new Thread(timer).start();
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
		simulator = null;
            }
        }
    }
    
    public void eventHandleLoop() throws InterruptedException{
	    
        SimulEvent simEvt = null;
        while(!terminated){
            simEvt = (SimulEvent) eventVQueue.get();
		
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
        frame.algoTerminated();
    }

    public void handleAgentMovedEvt(SimulEvent se) throws InterruptedException {
        AgentMovedAck ack = new AgentMovedAck(se.eventNumber());
        ackVQueue.put(ack);
    }
}
    

