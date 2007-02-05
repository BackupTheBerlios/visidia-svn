package visidia.gui.presentation.userInterfaceEdition;

import visidia.network.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.border.*;
import java.util.Vector;
import java.util.Hashtable;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.lang.Runtime;

/*
class ThreadWriter extends Thread{
    protected BufferedReader in;
    protected LocalNodeLauncher lnl;
    public MyThread(LocalNodeLauncher lnl, BufferedReader in ) {
	this.lnl = lnl;
	this.in = in;
    }
    public void run() {
	try {
	    String line = in.readLine();
	    while( line != null){
		this.lnl.write("la reponse du process est : "+line,Color.blue);
		line = in.readLine();
	    }
	} catch (Exception e) {
	}
    }
}
*/

class MyTableModel extends AbstractTableModel {
    
    Vector data, columnNames;
    Vector notEditable;

    public MyTableModel() {
	notEditable = new Vector();
	Vector tmp = new Vector();
	data = new Vector();
	
	tmp.addElement(new Boolean(true));
	tmp.addElement("enter the local Node Url");
	tmp.addElement("none");
	data.addElement(tmp);
    
	columnNames = new Vector();
	columnNames.addElement("Selected");
	columnNames.addElement("Local Node URL");
	columnNames.addElement("State");
    }
    
    public int getColumnCount() {
	return columnNames.size();
    }
    
    public int getRowCount() {
	return data.size();
    }
    
    public String getColumnName(int col) {
	return (String)columnNames.elementAt(col);
    }
    
    public Object getValueAt(int row, int col) {
	return ((Vector)data.elementAt(row)).elementAt(col);
    }
    
    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
    }
    
    
    public void setNotEditable(int row) {
	notEditable.addElement(new Integer(row));
    }
    
    public void setEditable(int row) {
	notEditable.remove(new Integer(row));
    }
	
    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
	//Note that the data/cell address is constant,
	//no matter where the cell appears onscreen.
	if (col == 0)
	    return true;
	if (col == 1) {
	    //    if (notEditable != null)
		if (notEditable.contains(new Integer(row)))
		    return false;
		else 
		    return true;
		//    else 
		//return true;
	} else {
	    return false;
	}
    }

     /*
      * Don't need to implement this method unless your table's
      * data can change.
      */
    public void setValueAt(Object value, int row, int col) {
	((Vector)data.elementAt(row)).set(col,value);
	fireTableCellUpdated(row, col);
    }

    public void remove(int row) {
	data.remove(row);
	fireTableDataChanged();
    }

    public void insert(){
	int index = getRowCount();
	Vector tmp = new Vector();
	tmp.addElement(new Boolean(true));
	tmp.addElement("");
	tmp.addElement("none");
	data.addElement(tmp);
	fireTableRowsInserted(index,index);
    }
    
    public int insert(String url) {
	int index = getRowCount();
	Vector tmp = new Vector();
	tmp.addElement(new Boolean(true));
	tmp.addElement(url);
	tmp.addElement("none");
	data.addElement(tmp);
	fireTableRowsInserted(index,index);
	return index;
    }
    public Vector getSelectedRows() {
	Vector v = new Vector();
	for (int i=0; i<data.size(); i++){
	    if (((Boolean)((Vector)data.elementAt(i)).elementAt(0)).equals(new Boolean(true))){
		v.addElement(new Integer(i));
	    }
	}
	return v;
    }
}


public class LocalNodeLauncher extends JFrame implements ActionListener{
    
    protected JPanel registryPanel, localNodesPanel, localNodesGroup, controlPanel, controlButton,  rmiRegistry, remoteRegistry;
    protected JTabbedPane tabbedPane;
    protected JToolBar toolBar;
    protected JMenuItem new_launcher;
    protected JTable table;
    protected MyTableModel myModel;
    protected JSpinner js;
    protected SpinnerNumberModel spinnerModel;
    protected JScrollPane rmiScroller;
    protected JSplitPane mainPane,contentPane;
    protected JTextPane errorArea;
    protected StyledDocument doc;
    protected MutableAttributeSet keyWord;
    
    protected JButton exit, start, startAll, registerAll, register, killAll, kill, delete, add, startRegistry,killRegistry, clear;
    protected JRadioButton runRegistry,manyLocalNodes;
    protected JCheckBox debug, fullDebug;
    protected JTextField portRegistry, visualizationHost, visualizationUrl,localHost;
    protected Border etchedBorder;
    
    protected Hashtable nodeServers;
    protected Registry registry;
    
    protected int count = 0;
    protected final String DEFAULT_URL = "";


    public LocalNodeLauncher(String title) {
	super(title);
	nodeServers = new Hashtable();
	
	etchedBorder = BorderFactory.createEtchedBorder();
	//Panel contenant la configuration pour les noeuds locaux
	setUpContentPane();
	
	//panel conteannt les bouttons exit etc ...
	setUpControlPanel();

	//panel root de la JFrame
	setUpFrame();
    }
    
    /**
     * add all componenets to frame and show it
     */
    protected void setUpContentPane() {
	/**********************************************************/
	/*     The panel where the local nodes are configured      */
	/**********************************************************/
	setUpLocalNodeSettings();
	/**********************************************************/
	/*     The panel where the registration is configured      */
	/**********************************************************/
	setUpRegistration();
	/******************************************************/
	/*                  Final settings                    */
	/******************************************************/
	contentPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,rmiScroller,localNodesPanel);
	contentPane.setOneTouchExpandable(true);
	contentPane.setDividerLocation(165);
    }

    /** ********************************/
    /*  Handling action events         */
    /***********************************/
    public void actionPerformed(ActionEvent ae) {
	Object source = ae.getSource();
	if ( source instanceof JRadioButton) {
	    source = (JRadioButton)source;
	    if(source == manyLocalNodes)
		js.setEnabled(manyLocalNodes.isSelected());
	    else if (source == runRegistry) {
		startRegistry.setEnabled(runRegistry.isSelected());
	    }
	} else if (source instanceof JButton) {
	    source = (JButton)source;
	    if( source == start) {
		write("\nStarting Selected Nodes",Color.blue);
		start();
		register.setEnabled(true);
		kill.setEnabled(true);
		registerAll.setEnabled(true);
		killAll.setEnabled(true);
	    } else if (source == register) {
		write("\nregistering !",Color.blue);
		register();
	    } else if (source == kill) {
		System.out.println("killing !");
		kill();
	    } else if (source == delete) {
		delete();
	    } else if (source == add) {
		System.out.println("adding !");
		myModel.insert();
	    } else if (source == startAll) {
		System.out.println("starting All !");
		startAll();
	    } else if (source == registerAll) {
		System.out.println("registering All !");
		registerAll();
	    } else if (source == killAll) {
		System.out.println("killing All !");
		killAll();
	    } else if (source == exit) {
		System.out.println("THE END !");
		exit();
	    } else if (source == startRegistry) {
		startRegistry();
	    } else if (source == killRegistry) {
		killRegistry();
	    } else if (source == clear) {
		clear();
	    }
	} else if (source instanceof JMenuItem) {
	    source = (JMenuItem)source;
	    if( source == new_launcher) {
		newLauncher();
	    }
	}
    }
    
    /**
     * create an instance of all selected and not running 
     * localNodes. The local node URLs are no longer editable
     */
    private void start() {
	if(!manyLocalNodes.isSelected()){
	    Vector selected = myModel.getSelectedRows();
	    write(selected.toString(),Color.blue);
	    for(int i=0; i<selected.size();i++) {
		int row = ((Integer)selected.elementAt(i)).intValue();
		if (!(((String)myModel.getValueAt(row,2)).equals("started") || ((String)myModel.getValueAt(row,2)).equals("registered"))) {
		    String hostText = localHost.getText();
		    String portText = portRegistry.getText();
		    String url = (String)myModel.getValueAt(row,1);
		    
		    boolean bool = true;
		    try {
			NodeServer nodeServer = new NodeServerImpl(hostText,portText);
			nodeServer.setUrlName(url);
			try{
			    Naming.bind("rmi://"+hostText+":"+portText+"/NodeServer/"+url,nodeServer);
			} catch (Exception e2) {
			    try {
				UnicastRemoteObject.unexportObject(nodeServer,true);
				bool=false;
			    } catch (Exception e3) {}
			    if(debug.isSelected()){
				write(e2);
			    } 
			}
			if (bool) {
			    nodeServers.put(new Integer(row),nodeServer);
			    myModel.setValueAt("started",row,2);
			    myModel.setNotEditable(row);
			    write("\nLocal Node "+url+" is started",Color.blue);
			}
		    } catch (Exception e1) {
			write(e1);
		    }
		}
	    }
	} else if (manyLocalNodes.isSelected()) {
	    int n = 0;
	    try{
		n = spinnerModel.getNumber().intValue();
	    } catch (Exception e) {
		e.printStackTrace();
		write("\nLocal node Number not compatible",Color.red);
	    }
	    
	    write("\nStrating "+n+" localNodes with Urls : 0 ... "+(n-1),Color.blue);
	    int min = count ;
	    int max = count+n;
	    for(int i=min; i<max;i++) {
		String hostText = localHost.getText();
		String portText = portRegistry.getText();
		boolean bool = true;
		try {
		    NodeServer nodeServer = new NodeServerImpl(hostText,portText);
		    nodeServer.setUrlName(DEFAULT_URL+count);
		    try{
			Naming.bind("rmi://"+hostText+":"+portText+"/NodeServer/"+DEFAULT_URL+count,nodeServer);
		    } catch (Exception e2) {
			try {
			    UnicastRemoteObject.unexportObject(nodeServer,true);
			    bool=false;
			} catch (Exception e3) {}
			if(debug.isSelected()){
			    write(e2);
			} 
		    }
		    if (bool) {
			int index = myModel.insert(DEFAULT_URL+count);
			nodeServers.put(new Integer(index),nodeServer);
			myModel.setValueAt("started",index,2);
			myModel.setNotEditable(index);
			write("\nLocal Node "+DEFAULT_URL+count+" is started",Color.blue);
			count++;
		    }
		} catch (Exception e1) {
		    e1.printStackTrace();
		}
	    }
	}
    }

    private void startAll() {
	for(int i=0; i<myModel.getRowCount();i++) {
	    if (!(((String)myModel.getValueAt(i,2)).equals("started") ||((String)myModel.getValueAt(i,2)).equals("registered"))) {
		String hostText = localHost.getText();
		String portText = portRegistry.getText();
		String url = (String)myModel.getValueAt(i,1);
		boolean bool = true;
		try {
		    NodeServer nodeServer = new NodeServerImpl(hostText,portText);
		    nodeServer.setUrlName(url);
		    try{
			Naming.bind("rmi://"+hostText+":"+portText+"/NodeServer/"+url,nodeServer);
		    } catch (Exception e2) {
			try {
			    UnicastRemoteObject.unexportObject(nodeServer,true);
			    bool=false;
			} catch (Exception e3) {}
			if(debug.isSelected()){
			    write(e2);
			} 
		    }
		    if (bool) {
			nodeServers.put(new Integer(i),nodeServer);
			myModel.setValueAt("started",i,2);
			myModel.setNotEditable(i);
			write("\nLocal Node "+url+" is started",Color.blue);
		    }
		} catch (Exception e1) {
		    e1.printStackTrace();
		}
	    }
	}
    }
    
    /**
     * register all the selected and started local node using the Visidia Registry
     */
    private void register() {
	Vector selected = myModel.getSelectedRows();
	for(int i=0; i<selected.size();i++) {
	    int row = ((Integer)selected.elementAt(i)).intValue(); 
	    if (((String)myModel.getValueAt(row,2)).equals("started")) {
		//String hostText = localHost.getText();
		//String portText = portRegistry.getText();
		String url = (String)myModel.getValueAt(row,1);
		try {
		    NodeServer nodeServer = (NodeServer)nodeServers.get(new Integer(row));
		    nodeServer.register(visualizationHost.getText(),visualizationUrl.getText());
		    myModel.setValueAt("registered",row,2);
		} catch (Exception e) {
		    if(debug.isSelected()){
			write(e);
		    }
		}
	    }
	}
    }

    /**
     * register all the started localNodes
     */
    private void registerAll() {
	for(int i=0; i<myModel.getRowCount();i++) {
	    if (((String)myModel.getValueAt(i,2)).equals("started")) {
		//String hostText = localHost.getText();
		//String portText = portRegistry.getText();
		String url = (String)myModel.getValueAt(i,1);
		try {
		    NodeServer nodeServer = (NodeServer)nodeServers.get(new Integer(i));
		    nodeServer.register(visualizationHost.getText(),visualizationUrl.getText());
		    myModel.setValueAt("registered",i,2);
		} catch (Exception e) {
		    if(debug.isSelected()){
			write(e);
		    }
		}
	    }
	}
    }
    
    private void kill() {
	Vector selected = myModel.getSelectedRows();
	write(selected.toString(),Color.blue);
	for(int i=0; i<selected.size();i++) {
	    int row = ((Integer)selected.elementAt(i)).intValue();
	    if (((String)myModel.getValueAt(row,2)).equals("started") || ((String)myModel.getValueAt(row,2)).equals("registered")) {
		String hostText = localHost.getText();
		String portText = portRegistry.getText();
		String url = (String)myModel.getValueAt(row,1);
		boolean bool = true;
		try {
		    NodeServer nodeServer = (NodeServer)nodeServers.remove(new Integer(row));
		    try {
			Naming.unbind("rmi://"+hostText+":"+portText+"/NodeServer/"+url);
		    } catch (Exception expt) {
			write("\nCouldn't unbind Local Node from registry",Color.red);
			if(fullDebug.isSelected())
			    write(expt);
		    }
		    UnicastRemoteObject.unexportObject(nodeServer,true);
		} catch (Exception e) {
		    bool=false;
		    if(debug.isSelected()){
			write(e);
		    }
		}
		if (bool) {
		    myModel.setValueAt("killed",row,2);
		    myModel.setEditable(row);
		    write("\nLocal Node "+url+" is killed",Color.blue);
		}
	    }
	}
    }
    
    private void killAll(){
	for(int i=0; i<myModel.getRowCount();i++) {
	    if (((String)myModel.getValueAt(i,2)).equals("started") || ((String)myModel.getValueAt(i,2)).equals("registered")) {
		String hostText = localHost.getText();
		String portText = portRegistry.getText();
		String url = (String)myModel.getValueAt(i,1);
		boolean bool = true;
		try {
		    NodeServer nodeServer = (NodeServer)nodeServers.remove(new Integer(i));
		    try {
			Naming.unbind("rmi://"+hostText+":"+portText+"/NodeServer/"+url);
		    } catch (Exception expt) {
			write("\nCouldn't unbind Local Node from registry",Color.red);
			if(fullDebug.isSelected())
			    write(expt);
		    }
		    UnicastRemoteObject.unexportObject(nodeServer,true);
		} catch (Exception e) {
		    bool=false;
		    if(debug.isSelected()){
			write(e);
		    }
		}
		if (bool) {
		    myModel.setValueAt("killed",i,2);
		    myModel.setEditable(i);
		    write("\nLocal Node "+url+" is killed",Color.blue);
		}
	    }
	}
    }
    
    private void delete() {
	int retour = JOptionPane.showConfirmDialog(this,"selected Local Nodes will be killed\ncontinue any way ?","Confirm local node deletion", JOptionPane.YES_NO_OPTION);
	if (retour == JOptionPane.YES_OPTION){
	    kill();
	    Vector selected = myModel.getSelectedRows();
	    while(!selected.isEmpty()) {
		int row = ((Integer)selected.elementAt(0)).intValue();
		myModel.remove(row);
		selected = myModel.getSelectedRows();
	    }
	} else if (retour == JOptionPane.NO_OPTION){
	}
    }
    
    private void exit() {
	int retour = JOptionPane.showConfirmDialog(this,"Exiting will kill the running Local Nodes\nexit any way ?","Confirm Exit", JOptionPane.YES_NO_OPTION);
	if (retour == JOptionPane.YES_OPTION){
	    killAll();
	    this.dispose();
	    this.setVisible(false);
	    System.exit(0);
	} else if (retour == JOptionPane.NO_OPTION){
	}
    }
    
    private void startRegistry() {
	try {
	    write("\nCreating an RMI registry",Color.orange);
	    registry = LocateRegistry.createRegistry((new Integer(portRegistry.getText())).intValue());
	    write("\nRMI registry created",Color.orange);
	    killRegistry.setEnabled(true);
	} catch (Exception e) {
	    if(debug.isSelected()){
		write(e);
		write("\nRegistry creation failed",Color.orange);
	    }
	    
	}
    }
    
    private void killRegistry() {
	try {
	    write("\nDestroying RMI registry",Color.orange);
	    UnicastRemoteObject.unexportObject(registry,true);
	    registry =  null;
	    killRegistry.setEnabled(false);
	    write("\nfinished",Color.orange);
	} catch (Exception e) {
	    if(debug.isSelected()){
		write(e);
		write("\nError when killing RMI registry",Color.orange);
	    }
	}
    }
    
    private void clear() {
	try {
	    doc.remove(0,doc.getLength());
	} catch (Exception e) {
	}
    }
    
    private void newLauncher() {
	try{
	    Runtime r =  Runtime.getRuntime();
	    Process p = r.exec("java -Xmx1024M visidia.network.LocalNodeLauncher");
	} catch (Exception e) {
	    write(e);
	}
    }


    
    public void write(String s,Color c) {
	try {
	    StyleConstants.setForeground(keyWord, c);
	    doc.insertString(doc.getLength(), s, keyWord);
	}catch (Exception e) {}
    }
    
    private void write(Exception e) {
	try {
	    StackTraceElement[] ste = e.getStackTrace();
	    String textError="\n"+e.toString();
	    write(textError,Color.red);
	    if(fullDebug.isSelected()) {
		for(int j=0;j<ste.length;j++){
		    textError+="\n"+ste[j].toString();
		}
		write(textError,Color.black);
	    }
	}catch (Exception expt) {}
    }
    
    
    /** *****************************************************************/
    /*              Method for setting up components                    */
    /********************************************************************/
    protected void setUpControlPanel() {
	controlButton = new JPanel(new FlowLayout());
	killAll = new JButton("Kill All");
	killAll.setEnabled(false);
	startAll = new JButton("Start All");
	registerAll = new JButton ("Register All");
	registerAll.setEnabled(false);
	exit = new JButton("Exit");
	
	killAll.addActionListener(this);
	startAll.addActionListener(this);
	registerAll.addActionListener(this);
	exit.addActionListener(this);
	

	controlButton.add(startAll);
	controlButton.add(registerAll);
	controlButton.add(killAll);
	controlButton.add(exit);

	JPanel debugPanel = new JPanel(new FlowLayout());
	debug = new JCheckBox("Enable Log",true);
	fullDebug = new JCheckBox("Print Error Trace",false);
	clear = new JButton("clear");
	clear.setToolTipText("clear text");
	clear.addActionListener(this);
	debugPanel.add(debug);
	debugPanel.add(fullDebug);
	debugPanel.add(clear);
	errorArea = new JTextPane();
	doc = errorArea.getStyledDocument();
	keyWord = new SimpleAttributeSet();
	doc.setParagraphAttributes(0, 0, keyWord, true);
	errorArea.setEditable(false);
	JScrollPane areaScrollPane = new JScrollPane(errorArea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setBorder(BorderFactory.createTitledBorder(etchedBorder,"Log Info"));	
	controlPanel = new JPanel(new BorderLayout());
	controlPanel.add(debugPanel,BorderLayout.NORTH);
	controlPanel.add(areaScrollPane,BorderLayout.CENTER);
	controlPanel.add(controlButton,BorderLayout.SOUTH);
    }
    
    public void setUpFrame(){
	/*
	JMenuBar menuBar = new JMenuBar();
	JMenu menu = new JMenu("File");
	new_launcher = new JMenuItem("New Launcher");
	new_launcher.addActionListener(this);
	menu.add(new_launcher);
	menuBar.add(menu);
	*/
	mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,contentPane,controlPanel);
	mainPane.setOneTouchExpandable(true);
	mainPane.setDividerLocation(240);
	mainPane.setPreferredSize(new Dimension(560,400));
	
	this.getContentPane().add(mainPane,BorderLayout.CENTER);
	//this.getContentPane().add(menuBar,BorderLayout.NORTH);
	this.pack();
	this.setVisible(true);
    }
    
    
    
    /**
     * first method for SetUpContentPane
     */
    private void setUpLocalNodeSettings() {
	localNodesPanel = new JPanel(new BorderLayout());
	localNodesPanel.setBorder(BorderFactory.createTitledBorder(etchedBorder,"Local Node Settings"));
	
	toolBar = new JToolBar();
	start = new JButton("start");
	register = new JButton("register");
	register.setEnabled(false);
	kill = new JButton("kill");
	kill.setEnabled(false);
	delete = new JButton("delete");
	add = new JButton("add a local Node");

	start.addActionListener(this);
	register.addActionListener(this);
	kill.addActionListener(this);
	delete.addActionListener(this);
	add.addActionListener(this);

	toolBar.add(start);
	toolBar.add(register);
	toolBar.addSeparator(new Dimension(25,0));
	toolBar.add(kill);
	toolBar.add(delete);
	toolBar.addSeparator(new Dimension(25,0));
	toolBar.add(add);

	JPanel panelCenter = new JPanel();
	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	panelCenter.setLayout(gbl);
	gbc.fill = GridBagConstraints.HORIZONTAL;
			

	localNodesGroup =  new JPanel(new GridLayout(0,2));
	localNodesGroup.setBorder(BorderFactory.createTitledBorder(etchedBorder));
	JLabel label1 = new JLabel(" Local Host ");
	manyLocalNodes = new JRadioButton(" Number of Local Nodes ");
	manyLocalNodes.addActionListener(this);
	spinnerModel = new SpinnerNumberModel(new Integer(2),new Integer(1),new Integer(1000),new Integer(1));
	js = new JSpinner(spinnerModel);
	js.setEnabled(false);
	try {
	    localHost = new JTextField(java.net.InetAddress.getLocalHost().getHostName());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	localNodesGroup.add(label1);
	localNodesGroup.add(localHost);
	localNodesGroup.add(manyLocalNodes);
	localNodesGroup.add(js);
	gbc.anchor = GridBagConstraints.NORTH;
	gbc.weightx = 1;
	gbc.weighty = 1;
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbl.setConstraints(localNodesGroup,gbc);
	panelCenter.add(localNodesGroup);

	myModel = new MyTableModel();
        table = new JTable(myModel);
	table.setPreferredScrollableViewportSize(new Dimension(200,100));
	JScrollPane scrollPane = new JScrollPane(table);
	gbc.anchor = GridBagConstraints.NORTH;
	gbc.ipady = 100;
	gbc.weightx = 1;
	gbc.weighty = 1;
	gbc.gridx = 0;
	gbc.gridy = 1;
	gbl.setConstraints( scrollPane,gbc);
	panelCenter.add(scrollPane);

	localNodesPanel.add(panelCenter,BorderLayout.CENTER);
	localNodesPanel.add(toolBar,BorderLayout.NORTH);
    }
    /**
     * second method for SetUpContentPane
     */
    private void setUpRegistration() {
	registryPanel = new JPanel();
	registryPanel.setLayout(new BorderLayout());
	rmiScroller = new JScrollPane(registryPanel);	
	JPanel registryPanelNorth = new JPanel();
	

	/*
	 *first panel for rmiregistry configuration
	 */
	rmiRegistry = new JPanel(new GridLayout(0,1));
	rmiRegistry.setBorder(BorderFactory.createTitledBorder(etchedBorder,"RMI registry"));
	
	runRegistry = new JRadioButton("run registry on port");
	runRegistry.setSelected(false);
	portRegistry = new JTextField("1099");
	rmiRegistry.add(portRegistry);
	startRegistry = new JButton("start registry");
	startRegistry.setEnabled(false);
	killRegistry = new JButton("kill registry");
	killRegistry.setEnabled(false);
	
	rmiRegistry.add(runRegistry);
	rmiRegistry.add(portRegistry);
	rmiRegistry.add(startRegistry);
	rmiRegistry.add(killRegistry);
	
	runRegistry.addActionListener(this);
	startRegistry.addActionListener(this);
	killRegistry.addActionListener(this);

	
	/*
	 * second panel for Visidia registration
	 */
	remoteRegistry = new JPanel();
	remoteRegistry.setBorder(BorderFactory.createTitledBorder(etchedBorder,"Remote registration"));
	
	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	remoteRegistry.setLayout(gbl);
	
	JLabel jl1 = new JLabel(" Host  ");
	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.anchor = GridBagConstraints.NORTH;
	gbc.ipady = 7;
	gbc.weightx = 0;
	gbc.weighty = 0;
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbl.setConstraints(jl1,gbc);
	remoteRegistry.add(jl1);

	visualizationHost = new JTextField();
	gbc.ipady = 7;	
	gbc.weightx = 1;
	gbc.weighty = 1;
	gbc.gridx = 1;
	gbc.gridy = 0;
	gbl.setConstraints(visualizationHost,gbc);
	remoteRegistry.add(visualizationHost);

	JLabel jl2 = new JLabel(" URL ");
	gbc.ipady = 7;
	gbc.weightx = 0;
	gbc.weighty = 0;
	gbc.gridx = 0;
	gbc.gridy = 1;
	gbl.setConstraints(jl2,gbc);
	remoteRegistry.add(jl2);
	
	visualizationUrl = new JTextField();
	gbc.ipady = 7;
	gbc.weightx = 1;
	gbc.weighty = 1;
	gbc.gridx = 1;
	gbc.gridy = 1;
	gbl.setConstraints(visualizationUrl,gbc);
	remoteRegistry.add(visualizationUrl);
	
	GridBagLayout gblNorth = new GridBagLayout();
	GridBagConstraints gbcNorth = new GridBagConstraints();
	registryPanelNorth.setLayout(gblNorth);
	
	gbcNorth.fill = GridBagConstraints.HORIZONTAL;
	gbcNorth.anchor = GridBagConstraints.NORTH;
	gbcNorth.weightx = 1;
	gbcNorth.weighty = 1;
	gbcNorth.gridx = 0;
	gbcNorth.gridy = 0;
	gblNorth.setConstraints(rmiRegistry,gbcNorth);
	registryPanelNorth.add(rmiRegistry);

	gbcNorth.weightx = 1;
	gbcNorth.weighty = 1;
	gbcNorth.gridx = 0;
	gbcNorth.gridy = 1;
	gblNorth.setConstraints(remoteRegistry,gbcNorth);
	registryPanelNorth.add(remoteRegistry);
	
	registryPanel.add(registryPanelNorth,BorderLayout.NORTH);
    }
	
    public static final void main(String[] args) {
	LocalNodeLauncher lnl = new LocalNodeLauncher("Local Node Launcher");
    }
}
    
