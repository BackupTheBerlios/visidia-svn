package visidia.gui.presentation.userInterfaceSimulation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import visidia.graph.SimpleGraph;
import visidia.gui.metier.simulation.Convertisseur;
import visidia.gui.presentation.VueGraphe;
import visidia.rule.RelabelingSystem;
import visidia.simulation.AgentMovedAck;
import visidia.simulation.EdgeStateChangeAck;
import visidia.simulation.MessageSendingAck;
import visidia.simulation.SimulConstants;
import visidia.simulation.SimulEvent;
import visidia.simulation.SimulationAbortError;
import visidia.simulation.SimulatorThreadGroup;
import visidia.simulation.agents.AbstractExperiment;
import visidia.simulation.agents.AgentSimulator;
import visidia.tools.Bag;
import visidia.tools.HashTableModel;
import visidia.tools.TableSorter;
import visidia.tools.agents.UpdateTableStats;

public class AgentExperimentationFrame extends JFrame implements ActionListener {

    /**
         * 
         */
    private static final long serialVersionUID = 2305092931027145019L;

    ReadOnlyHashTableModel tableModel;

    private JTable resultTable;

    private JScrollPane scrollPane;

    private JToolBar toolBar;

    private JButton but_start;

    private JButton but_abort;

    private VueGraphe graph;

    private Vector<RelabelingSystem> agentsRules;

    private Hashtable<String, Object> defaultProperties;

    private Hashtable agentsTable;

    private AbstractExperiment expType;

    private ExperimentationThread expThread;

    private SimulatorThreadGroup threadGroup;

    public AgentExperimentationFrame(Bag stats) {
	this.initializeFrame(stats);
    }

    public AgentExperimentationFrame(VueGraphe graph, Hashtable agentsTable,
	    Hashtable<String, Object> defaultProperties,
	    Vector<RelabelingSystem> agentsRules, AbstractExperiment expType) {

	this.agentsRules = agentsRules;
	this.graph = graph;
	this.agentsTable = agentsTable;
	this.defaultProperties = defaultProperties;
	this.expType = expType;

	this.initializeFrame(new Bag());

	this.but_start = new JButton("Start");
	this.but_start.addActionListener(this);

	this.but_abort = new JButton("Abort");
	this.but_abort.addActionListener(this);

	this.toolBar = new JToolBar();
	this.toolBar.add(this.but_start);
	this.toolBar.add(this.but_abort);
	this.getContentPane().add(this.toolBar, BorderLayout.NORTH);

	this.controlEnabling(true);
    }

    private void initializeFrame(Bag stats) {
	this.tableModel = new ReadOnlyHashTableModel(stats);
	TableSorter sorter = new TableSorter(this.tableModel);
	this.resultTable = new JTable(sorter);
	sorter.setTableHeader(this.resultTable.getTableHeader());
	this.scrollPane = new JScrollPane(this.resultTable);
	this.getContentPane().add(this.scrollPane, BorderLayout.CENTER);

	this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public HashTableModel getTableModel() {
	return this.tableModel;
    }

    public AbstractExperiment getExpType() {
	return this.expType;
    }

    public void actionPerformed(ActionEvent evt) {
	if (evt.getSource() == this.but_start) {
	    this.start();
	}
	if (evt.getSource() == this.but_abort) {
	    this.abort();
	}
    }

    private void controlEnabling(boolean enable) {
	this.but_start.setEnabled(enable);
	this.but_abort.setEnabled(!enable);
    }

    public void algoTerminated() {
	JOptionPane.showMessageDialog(this, "Algorithms are terminated");
	this.controlEnabling(true);
	if (this.expThread != null) {
	    this.expThread.abortExperimentation();
	    this.expThread = null;
	}
    }

    public void start() {
	if (this.expThread != null)
	    this.abort();
	this.getTableModel().setProperties(new Hashtable());
	this.threadGroup = new SimulatorThreadGroup("simulator");
	this.expThread = new ExperimentationThread(this.threadGroup,
		Convertisseur.convert(this.graph.getGraphe(), this.agentsTable,
			this.defaultProperties), this.agentsRules, this);
	this.controlEnabling(false);
	this.expThread.start();
    }

    public void abort() {
	if (this.expThread != null) {
	    this.expThread.abortExperimentation();
	    this.expThread = null;
	    this.controlEnabling(true);
	}
    }
}

class ReadOnlyHashTableModel extends HashTableModel {

    /**
         * 
         */
    private static final long serialVersionUID = 5417140615816465751L;

    public ReadOnlyHashTableModel(Bag table) {
	super(table.asHashTable());
    }

    public Class getColumnClass(int column) {
	if (column == 0)
	    return String.class;
	else if (column == 1)
	    return Long.class;
	else
	    throw new ArrayIndexOutOfBoundsException();
    }

    public Object getValueAt(int row, int col) {
	switch (col) {

	case 0:
	    return this.keys.get(row).toString();
	case 1:
	    return this.table.get(this.keys.get(row));

	}
	throw new IllegalArgumentException();
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
	    AgentExperimentationFrame frame) {
	super(group, "Experimentation thread");
	this.agentsRules = agentsRules;
	this.graph = graph;
	this.frame = frame;
	this.frame.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent evt) {
		// Exit the application
		ExperimentationThread.this.abortExperimentation();
	    }
	});

    }

    void abortExperimentation() {
	this.aborted = true;
	this.timer.stop();
	while (this.isAlive()) {
	    this.interrupt();
	    try {
		Thread.sleep(10);
	    } catch (InterruptedException e) {
		if (this.simulator != null)
		    this.simulator.abortSimulation();
		throw new SimulationAbortError(e);
	    }
	}

	if (this.simulator != null)
	    this.simulator.abortSimulation();
    }

    public void run() {

	this.eventVQueue = new visidia.tools.VQueue();
	this.ackVQueue = new visidia.tools.VQueue();

	this.simulator = new AgentSimulator(this.graph, this.agentsRules,
		this.eventVQueue, this.ackVQueue);

	this.simulator.startSimulation();
	this.frame.getExpType().setStats(this.simulator.getStats());
	this.timer = new UpdateTableStats(this.simulator, this.frame
		.getExpType(), this.frame.getTableModel());
	new Thread(this.timer).start();
	this.terminated = false;

	try {
	    this.eventHandleLoop();
	} catch (InterruptedException e) {
	    // this interruption should have been cause
	    // by the simulation stop.
	    if (this.aborted && (this.simulator != null)) {
		// abort current simulation
		this.simulator.abortSimulation();
		this.simulator = null;
	    }
	}
    }

    public void eventHandleLoop() throws InterruptedException {

	SimulEvent simEvt = null;
	while (!this.terminated) {
	    simEvt = (SimulEvent) this.eventVQueue.get();

	    switch (simEvt.type()) {

	    case SimulConstants.EDGE_STATE_CHANGE:
		this.handleEdgeStateChangeEvt(simEvt);
		break;
	    case SimulConstants.MESSAGE_SENT:
		this.handleMessageSentEvt(simEvt);
		break;
	    case SimulConstants.ALGORITHM_END:
		this.handleAlgorithmEndEvent(simEvt);
		break;
	    case SimulConstants.AGENT_MOVED:
		this.handleAgentMovedEvt(simEvt);
		break;
	    case SimulConstants.LABEL_CHANGE:
		break;
	    case SimulConstants.NEXT_PULSE:
		break;
	    default:
		throw new RuntimeException("Unknown event: " + simEvt.type());
	    }
	}
    }

    public void handleEdgeStateChangeEvt(SimulEvent se)
	    throws InterruptedException {
	EdgeStateChangeAck esca = new EdgeStateChangeAck(se.eventNumber());
	this.ackVQueue.put(esca);
    }

    public void handleMessageSentEvt(SimulEvent se) throws InterruptedException {
	MessageSendingAck msa = new MessageSendingAck(se.eventNumber());
	this.ackVQueue.put(msa);
    }

    public void handleAlgorithmEndEvent(SimulEvent se)
	    throws InterruptedException {
	this.terminated = true;
	this.frame.algoTerminated();
    }

    public void handleAgentMovedEvt(SimulEvent se) throws InterruptedException {
	AgentMovedAck ack = new AgentMovedAck(se.eventNumber());
	this.ackVQueue.put(ack);
    }
}
