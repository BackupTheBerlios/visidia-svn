package fr.enserb.das.gui.presentation.userInterfaceSimulation;

import fr.enserb.das.gui.DistributedAlgoSimulator;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import fr.enserb.das.gui.donnees.conteneurs.*;
import fr.enserb.das.gui.donnees.*;
import fr.enserb.das.gui.metier.simulation.*;
import fr.enserb.das.gui.presentation.*;
import fr.enserb.das.gui.presentation.boite.*;
import fr.enserb.das.gui.metier.inputOutput.*;
import fr.enserb.das.gui.presentation.userInterfaceEdition.*;
import fr.enserb.das.algo.*;
import fr.enserb.das.simulation.*;


public class ExperimentationFrame extends JFrame implements ActionListener{
	private JMenuBar menuBar;
	private JMenu fileMenu;
    //	private JMenuItem openGraphMenuItem;
	private JMenuItem saveResultMenuItem;
	private JMenuItem closeMenuItem;
	
	private JToolBar toolBar;
	private JButton startButton;
    //	private JToggleButton pauseButton;
	private JButton abortButton;
	private JLabel testNumberLabel;
	private JTextField testNumberTextField;
    private JLabel status;

	private JScrollPane scrollPane;
	private JTable resultTable;
	private ExperimentationResultTableModel tableModel;
    private JFileChooser fileChooser;
	
	private VueGraphe graph;
        private AlgoChoice algoChoice;
	
	private int sampleCount = -1;
	private int sampleNumber = 1;
	private ExperimentationThread expThread;

//	public ExperimentationFrame(){
//		this(null);
//	}
	
	public ExperimentationFrame(VueGraphe vueGraphe, AlgoChoice algoChoice){
	    if((vueGraphe == null) || (algoChoice == null)){
		throw new IllegalArgumentException("null argument(s)");
	    }
//  		openGraphMenuItem = new JMenuItem("Open graph ...");
//  		openGraphMenuItem.setMnemonic('O');
//  		openGraphMenuItem.addActionListener(this);
		
		saveResultMenuItem = new JMenuItem("Save results ...");
		saveResultMenuItem.setMnemonic('D');
		saveResultMenuItem.addActionListener(this);
			
		closeMenuItem = new JMenuItem("Close ...");
		closeMenuItem.setMnemonic('C');
		closeMenuItem.addActionListener(this);
		
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		fileMenu.addActionListener(this);
//  		fileMenu.add(openGraphMenuItem);
		fileMenu.add(saveResultMenuItem);
		fileMenu.add(closeMenuItem);

		menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
		
		Insets insets = new Insets(0,0,0,0);
		startButton = new JButton("start");
		startButton.addActionListener(this);
		startButton.setMargin(insets);
		
		//pauseButton = new JToggleButton("pause");
		//pauseButton.addActionListener(this);
		//pauseButton.setMargin(insets);
		
		abortButton = new JButton("abort");
		abortButton.addActionListener(this);
		abortButton.setMargin(insets);
		
		testNumberLabel = new JLabel("Sample number : ");
		
		testNumberTextField = new JTextField("5");
		
		toolBar = new JToolBar();
		toolBar.add(startButton);
		//toolBar.add(pauseButton);
		toolBar.add(abortButton);
		toolBar.addSeparator();
		toolBar.add(testNumberLabel);
		toolBar.add(testNumberTextField);
		getContentPane().add(toolBar,BorderLayout.NORTH);
		
		tableModel = new ExperimentationResultTableModel();
		resultTable = new JTable(tableModel);
		scrollPane = new JScrollPane(resultTable);		
		getContentPane().add(scrollPane,BorderLayout.CENTER);

		status = new JLabel();
		getContentPane().add(status,BorderLayout.SOUTH);
		
		fileChooser = new JFileChooser(".");

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);


		this.graph = vueGraphe;
		this.algoChoice = algoChoice;

		controlEnabling();

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				onWindowClosed();
			}
		});
	}
    

    public void onWindowClosed(){
	if(isRunning()){
	    abort();
	}
    }
    
    public VueGraphe getGraph(){
	return graph;
    }
    
    public AlgoChoice getAlgoChoice(){
	return algoChoice;
    }
    
    public void actionPerformed(ActionEvent evt){
	if(evt.getSource() == startButton){
	    start();
	}
	if(evt.getSource() == abortButton){
	    abort();
	}
	if(evt.getSource() == saveResultMenuItem){
	    saveResults();
	}
	if(evt.getSource() == closeMenuItem){
	    setVisible(false);
	}
    }
    
    public boolean isRunning(){
	if(expThread != null){
	    return expThread.isRunning();
	}
	
	return false;
    }


    public void saveResults(){
	if(isRunning()){
	    promptMessage(" experimetation is still running");
	    return;
	}

	if(tableModel.getRowCount() == 0){
	    promptMessage("no results available !!!");
	    return;
	}

	
	if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
	    File file = fileChooser.getSelectedFile();

	    // the file 
	    if(file.exists()){
		int option = JOptionPane.showConfirmDialog(this,
							   "file :("+file+") already exists, overwrite ?",
							   "Confirm",
							   JOptionPane.YES_NO_OPTION);
		if(option == JOptionPane.NO_OPTION){
		    return;
		}
	    }
	    
	    String numBlank = "            ";
	    String mesgCountBlank = "                   ";
	    PrintWriter writer = null;
	    try{
		writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)), true);


		int rowCount = tableModel.getRowCount();
		for(int i = 0; i < rowCount; i++){
		    String numString = tableModel.getValueAt(i,0).toString();
		    StringBuffer numStringBuff = new StringBuffer(numBlank);
		    numStringBuff.replace(0,numString.length(),numString);
		    writer.print(numStringBuff.toString());

		    String mesgCountString = tableModel.getValueAt(i,1).toString();
		    StringBuffer mesgCountStringBuff = new StringBuffer(mesgCountBlank);
		    mesgCountStringBuff.replace(0,mesgCountString.length(),mesgCountString);
		    writer.println(mesgCountStringBuff.toString());

		    // since PrintWriter methods don't throw exception,
		    // we check the operation status before continue. 
		    if(writer.checkError()){
			throw new IOException();
		    }
		}
	    }
	    catch(IOException exp){
		//exp.printStackTrace();
		promptMessage("cannot save results to file ("+file+")"); 
	    }
	    finally{
		writer.close();
	    }
	    
	}
    }


    /**
     *
     */
    private void controlEnabling(){
	if((graph == null)||(algoChoice == null)){
		startButton.setEnabled(false);
		//pauseButton.setEnabled(false);
		abortButton.setEnabled(false);
		return;
	}
	
	if(isRunning()){
	    startButton.setEnabled(false);
	    //pauseButton.setEnabled(true);
	    abortButton.setEnabled(true);
	}
	else{
	    startButton.setEnabled(true);
	    //pauseButton.setEnabled(false);
	    abortButton.setEnabled(false);
	    status.setText(" ");
	}
	
    }
    
    public void close(){
	dispose();
	onWindowClosed();
    }

	public void start(){
	    try{
		sampleNumber = new Integer(testNumberTextField.getText()).intValue();
	    }
	    catch(NumberFormatException e){ 
		//e.printStackTrace();
		promptMessage("bad sample number field");
		return;
	    }

	    tableModel.reset();

	    expThread = new ExperimentationThread(algoChoice, graph, sampleNumber);
	    expThread.start();
	}

	public void abort(){
	    if(expThread != null){
		expThread.abortExperimetation();
		expThread = null;
	    }
	    controlEnabling();
	}




//  	public void pause(boolean d){
//  	}

    class ExperimentationThread extends Thread {
	boolean aborted = false;
    	boolean terminated = true;
	private AlgoChoice algoFactory;
	private VueGraphe vueGraphe;
	private int nbSample = 0;
	private fr.enserb.das.tools.Queue eventQueue;
	private fr.enserb.das.tools.Queue ackQueue;
	private Simulator simulator;

	public ExperimentationThread(AlgoChoice algoFactory,VueGraphe vueGraphe,int nbSample){
	    this.algoFactory = algoFactory;
	    this.vueGraphe = vueGraphe;
	    this.nbSample = nbSample;
	}
	
	boolean isRunning(){
	    return isAlive() && !terminated;
	}

       	void abortExperimetation(){
	    aborted = true;
	    
	    while(isAlive()){
		interrupt();
		try{
		    Thread.currentThread().sleep(10);
		}
		catch(InterruptedException e){
		    e.printStackTrace();
		}
	    }
	    

	    
	}


	public void run(){
	    for(int i = 0; i < sampleNumber; i++){
		tableModel.addLine();
		
		eventQueue = new fr.enserb.das.tools.Queue();
		ackQueue = new fr.enserb.das.tools.Queue();
		
		simulator = new Simulator(Convertisseur.convertir(graph.getGraphe()),
					  eventQueue,
					  ackQueue,
					  algoChoice);
		simulator.startSimulation();
		terminated = false;

		controlEnabling();
		status.setText("simulation '"+i+"' is running...");

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
		if(aborted){
		    break;
		}
	    }

	    controlEnabling();
	}
    
	public void eventHandleLoop() throws InterruptedException{
	    
	    SimulEvent simEvt = null;
	    while(!terminated){
		try{
		    simEvt = (SimulEvent) eventQueue.get();
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
		    
		case SimulConstants.NODE_PROPERTY_CHANGE :
		    handleNodePropertyChangeEvt(simEvt);
		    break; 

		case SimulConstants.ALGORITHM_END :
		    handleAlgorithmEndEvent(simEvt);
		    break;
		}
		
	    }
	    
	}

	public void handleNodePropertyChangeEvt(SimulEvent se) throws InterruptedException{
	    NodePropertyChangeAck npca = new NodePropertyChangeAck(se.eventNumber());
	    ackQueue.put(npca); 		
	}
	public void handleEdgeStateChangeEvt(SimulEvent se) throws InterruptedException{
	    EdgeStateChangeAck esca = new EdgeStateChangeAck(se.eventNumber());
	    ackQueue.put(esca);
	}
	public void handleMessageSentEvt(SimulEvent se) throws InterruptedException{
	    tableModel.increments();
	    MessageSendingAck msa = new MessageSendingAck(se.eventNumber());
	    ackQueue.put(msa);
	}
	
	void handleAlgorithmEndEvent(SimulEvent se) throws InterruptedException{
	    terminated = true;
	}
	
    }
    
    public void promptMessage(String mesg){
	JOptionPane.showMessageDialog(this,mesg);
    }
/*
	public static void main(String[] args){
		JFrame frame = new ExperimentationFrame();
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			}
		});
		frame.pack();
		frame.setVisible(true);
	}
*/
}



class ExperimentationResultTableModel extends AbstractTableModel {
	private Vector lineVector = new Vector(10,10);
	
	public void addLine(){
		lineVector.add(new Integer(0));
		fireTableDataChanged();
	}

	public void addLine(int value){
		lineVector.add(new Integer(0));
		lineVector.set(lineVector.size() - 1, new Integer(value));
		fireTableDataChanged();
	}
	
	public void increments(int row){
		int val = ((Integer)lineVector.get(row)).intValue() + 1;
		lineVector.set(row,new Integer(val));
		fireTableCellUpdated(row,1);
	}
	
	public void increments(){
	    increments(lineVector.size() - 1);
	}
	
	
	public Class getColumnClass(int col){
		switch(col){
			case 0: return String.class;
			case 1: return Integer.class;
		}
		throw new IllegalArgumentException();	
	}
	
    public int getColumnCount(){
		return 2;
    }

    public int getRowCount(){
		return lineVector.size();
    }

    public Object getValueAt(int row, int col){
		switch(col){
			case 0: return String.valueOf(row);
			case 1: return lineVector.get(row);
		}
		throw new IllegalArgumentException();	
    }

    /**
     * Only value column cell are editable.
     */
    public boolean isCellEditable(int row, int col){
		return false;
    }

    public String getColumnName(int col){
		switch(col){
			case 0: return "n°";
			case 1: return "mesg count";
		}
		throw new IllegalArgumentException();	
    }

    void reset(){
	lineVector = new Vector(10,10);
	fireTableDataChanged();
    }
	

}











