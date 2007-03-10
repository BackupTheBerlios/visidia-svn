package visidia.gui.presentation.userInterfaceSimulation;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.AbstractTableModel;

import visidia.gui.metier.simulation.AlgoChoice;
import visidia.gui.metier.simulation.Convertisseur;
import visidia.gui.presentation.VueGraphe;
import visidia.simulation.EdgeStateChangeAck;
import visidia.simulation.MessageSendingAck;
import visidia.simulation.NodePropertyChangeAck;
import visidia.simulation.SimulConstants;
import visidia.simulation.SimulEvent;
import visidia.simulation.Simulator;


public class ExperimentationFrame extends JFrame implements ActionListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2826405311778427198L;
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
	
	this.saveResultMenuItem = new JMenuItem("Save results ...");
	this.saveResultMenuItem.setMnemonic('D');
	this.saveResultMenuItem.addActionListener(this);
	
	this.closeMenuItem = new JMenuItem("Close ...");
	this.closeMenuItem.setMnemonic('C');
	this.closeMenuItem.addActionListener(this);
	
	this.fileMenu = new JMenu("File");
	this.fileMenu.setMnemonic('F');
	this.fileMenu.addActionListener(this);
	//  		fileMenu.add(openGraphMenuItem);
	this.fileMenu.add(this.saveResultMenuItem);
	this.fileMenu.add(this.closeMenuItem);
	
	this.menuBar = new JMenuBar();
	this.menuBar.add(this.fileMenu);
	this.setJMenuBar(this.menuBar);
	
	Insets insets = new Insets(0,0,0,0);
	this.startButton = new JButton("start");
	this.startButton.addActionListener(this);
	this.startButton.setMargin(insets);
	
	//pauseButton = new JToggleButton("pause");
	//pauseButton.addActionListener(this);
	//pauseButton.setMargin(insets);
	
	this.abortButton = new JButton("abort");
	this.abortButton.addActionListener(this);
	this.abortButton.setMargin(insets);
		
	this.testNumberLabel = new JLabel("Sample number : ");
	
	this.testNumberTextField = new JTextField("5");
		
	this.toolBar = new JToolBar();
	this.toolBar.add(this.startButton);
	//toolBar.add(pauseButton);
	this.toolBar.add(this.abortButton);
	this.toolBar.addSeparator();
	this.toolBar.add(this.testNumberLabel);
	this.toolBar.add(this.testNumberTextField);
	this.getContentPane().add(this.toolBar,BorderLayout.NORTH);
		
	this.tableModel = new ExperimentationResultTableModel();
	this.resultTable = new JTable(this.tableModel);
	this.scrollPane = new JScrollPane(this.resultTable);		
	this.getContentPane().add(this.scrollPane,BorderLayout.CENTER);

	this.status = new JLabel();
	this.getContentPane().add(this.status,BorderLayout.SOUTH);
		
	this.fileChooser = new JFileChooser(".");

	this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);


	this.graph = vueGraphe;
	this.algoChoice = algoChoice;

	this.controlEnabling();

	this.addWindowListener(new java.awt.event.WindowAdapter() {
		public void windowClosing(java.awt.event.WindowEvent e) {
		    ExperimentationFrame.this.onWindowClosed();
		}
	    });
    }
    

    public void onWindowClosed(){
	if(this.isRunning()){
	    this.abort();
	}
    }
    
    public VueGraphe getGraph(){
	return this.graph;
    }
    
    public AlgoChoice getAlgoChoice(){
	return this.algoChoice;
    }
    
    public void actionPerformed(ActionEvent evt){
	if(evt.getSource() == this.startButton){
	    this.start();
	}
	if(evt.getSource() == this.abortButton){
	    this.abort();
	}
	if(evt.getSource() == this.saveResultMenuItem){
	    this.saveResults();
	}
	if(evt.getSource() == this.closeMenuItem){
	    this.setVisible(false);
	}
    }
    
    public boolean isRunning(){
	if(this.expThread != null){
	    return this.expThread.isRunning();
	}
	
	return false;
    }


    public void saveResults(){
	if(this.isRunning()){
	    this.promptMessage(" experimetation is still running");
	    return;
	}

	if(this.tableModel.getRowCount() == 0){
	    this.promptMessage("no results available !!!");
	    return;
	}

	
	if(this.fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
	    File file = this.fileChooser.getSelectedFile();

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


		int rowCount = this.tableModel.getRowCount();
		for(int i = 0; i < rowCount; i++){
		    String numString = this.tableModel.getValueAt(i,0).toString();
		    StringBuffer numStringBuff = new StringBuffer(numBlank);
		    numStringBuff.replace(0,numString.length(),numString);
		    writer.print(numStringBuff.toString());

		    String mesgCountString = this.tableModel.getValueAt(i,1).toString();
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
		this.promptMessage("cannot save results to file ("+file+")"); 
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
	if((this.graph == null)||(this.algoChoice == null)){
	    this.startButton.setEnabled(false);
	    //pauseButton.setEnabled(false);
	    this.abortButton.setEnabled(false);
	    return;
	}
	
	if(this.isRunning()){
	    this.startButton.setEnabled(false);
	    //pauseButton.setEnabled(true);
	    this.abortButton.setEnabled(true);
	}
	else{
	    this.startButton.setEnabled(true);
	    //pauseButton.setEnabled(false);
	    this.abortButton.setEnabled(false);
	    this.status.setText(" ");
	}
	
    }
    
    public void close(){
	this.dispose();
	this.onWindowClosed();
    }

    public void start(){
	try{
	    this.sampleNumber = new Integer(this.testNumberTextField.getText()).intValue();
	}
	catch(NumberFormatException e){ 
	    //e.printStackTrace();
	    this.promptMessage("bad sample number field");
	    return;
	}

	this.tableModel.reset();

	this.expThread = new ExperimentationThread(this.algoChoice, this.graph, this.sampleNumber);
	this.expThread.start();
    }

    public void abort(){
	if(this.expThread != null){
	    this.expThread.abortExperimetation();
	    this.expThread = null;
	}
	this.controlEnabling();
    }




    //  	public void pause(boolean d){
    //  	}

    class ExperimentationThread extends Thread {
	boolean aborted = false;
    	boolean terminated = true;
	private visidia.tools.VQueue eventVQueue;
	private visidia.tools.VQueue ackVQueue;
	private Simulator simulator;

	public ExperimentationThread(AlgoChoice algoFactory,VueGraphe vueGraphe,int nbSample){
	}
	
	boolean isRunning(){
	    return this.isAlive() && !this.terminated;
	}

       	void abortExperimetation(){
	    this.aborted = true;
	    
	    while(this.isAlive()){
		this.interrupt();
		try{
		    Thread.sleep(10);
		}
		catch(InterruptedException e){
		    e.printStackTrace();
		}
	    }
	    

	    
	}


	public void run(){
	    for(int i = 0; i < ExperimentationFrame.this.sampleNumber; i++){
		ExperimentationFrame.this.tableModel.addLine();
		
		this.eventVQueue = new visidia.tools.VQueue();
		this.ackVQueue = new visidia.tools.VQueue();
		
		this.simulator = new Simulator(Convertisseur.convertir(ExperimentationFrame.this.graph.getGraphe()),
					  this.eventVQueue,
					  this.ackVQueue,
					  ExperimentationFrame.this.algoChoice);
		this.simulator.startSimulation();
		this.terminated = false;

		ExperimentationFrame.this.controlEnabling();
		ExperimentationFrame.this.status.setText("simulation '"+i+"' is running...");

		try{
		    this.eventHandleLoop();
		}
		catch(InterruptedException e){
		    //this interruption should have been cause
		    //by the simulation stop.
		    if( this.aborted && (this.simulator != null)){
			//abort current simulation
			this.simulator.abortSimulation();
		    }
		}
		if(this.aborted){
		    break;
		}
	    }

	    ExperimentationFrame.this.controlEnabling();
	}
    
	public void eventHandleLoop() throws InterruptedException{
	    
	    SimulEvent simEvt = null;
	    while(!this.terminated){
		try{
		    simEvt = (SimulEvent) this.eventVQueue.get();
		    //		    Thread.currentThread().sleep(10);
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
		}
		
	    }
	    
	}

	public void handleNodePropertyChangeEvt(SimulEvent se) throws InterruptedException{
	    NodePropertyChangeAck npca = new NodePropertyChangeAck(se.eventNumber());
	    this.ackVQueue.put(npca); 		
	}
	public void handleEdgeStateChangeEvt(SimulEvent se) throws InterruptedException{
	    EdgeStateChangeAck esca = new EdgeStateChangeAck(se.eventNumber());
	    this.ackVQueue.put(esca);
	}
	public void handleMessageSentEvt(SimulEvent se) throws InterruptedException{
	    ExperimentationFrame.this.tableModel.increments();
	    MessageSendingAck msa = new MessageSendingAck(se.eventNumber());
	    this.ackVQueue.put(msa);
	}
	
	void handleAlgorithmEndEvent(SimulEvent se) throws InterruptedException{
	    this.terminated = true;
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
    /**
	 * 
	 */
	private static final long serialVersionUID = -6346018981396430376L;
	private Vector lineVector = new Vector(10,10);
	
    public void addLine(){
	this.lineVector.add(new Integer(0));
	this.fireTableDataChanged();
    }

    public void addLine(int value){
	this.lineVector.add(new Integer(0));
	this.lineVector.set(this.lineVector.size() - 1, new Integer(value));
	this.fireTableDataChanged();
    }
	
    public void increments(int row){
	int val = ((Integer)this.lineVector.get(row)).intValue() + 1;
	this.lineVector.set(row,new Integer(val));
	this.fireTableCellUpdated(row,1);
    }
	
    public void increments(){
	this.increments(this.lineVector.size() - 1);
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
	return this.lineVector.size();
    }

    public Object getValueAt(int row, int col){
	switch(col){
	case 0: return String.valueOf(row);
	case 1: return this.lineVector.get(row);
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
	case 0: return "n ";
	case 1: return "mesg count";
	}
	throw new IllegalArgumentException();	
    }

    void reset(){
	this.lineVector = new Vector(10,10);
	this.fireTableDataChanged();
    }
	

}











