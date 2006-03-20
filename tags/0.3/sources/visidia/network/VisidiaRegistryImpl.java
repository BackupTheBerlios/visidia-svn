package visidia.network;

import java.rmi.*;
import java.rmi.Naming;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import visidia.gui.presentation.*;
import visidia.gui.presentation.userInterfaceEdition.*;
import visidia.gui.presentation.userInterfaceSimulation.*;
import visidia.gui.donnees.*;
import visidia.gui.metier.*;
import visidia.tools.LocalNodeTable;


public class VisidiaRegistryImpl extends UnicastRemoteObject implements VisidiaRegistry {
    private FenetreDeSimulationDist parent;
    //private LocalNodeSelection lns;
    private Vector data;
    public VisidiaRegistryImpl(FenetreDeSimulationDist parent) throws RemoteException {
	super();
	this.parent = parent;
	data = new Vector();
    }

    public void showLocalNodes(int sizeOfTheGraph) throws RemoteException {
	LocalNodeSelection lns = new LocalNodeSelection("Local Node Selection",sizeOfTheGraph,parent,this);
	refresh(lns);
	lns.setVisible(true);
    }
    public void refresh(LocalNodeSelection lns) {
	lns.clean();
	for (int i=0; i<data.size(); i++){
	    Vector ieme = (Vector)data.elementAt(i);
	    lns.insert((String)ieme.elementAt(0),(String)ieme.elementAt(1));
	}
    }
    
    public void init(String url, Registry registry) throws RemoteException {
	try {
	    registry.bind(url,this);
	} catch (Exception e) {
	    JOptionPane.showMessageDialog(parent,"The Visidia Registration is not running : \n"+e.toString(),"Error",JOptionPane.WARNING_MESSAGE);
	    System.out.println("##########################################################");
	    System.out.println("Erreur lors de l'initialisation de la VisidiaRegsitry");
	    System.out.println("##########################################################");
	    System.out.println("");
	    e.printStackTrace();
	    System.out.println("");
	    System.out.println("##########################################################");
	}
    }
    
    public void register(NodeServer localNode,String host, String url) throws RemoteException {
	Vector tmp = new Vector();
	tmp.addElement(host);
	tmp.addElement(url);
	if (!data.contains(tmp))
	    data.addElement(tmp);
	System.out.println("Le noeud local ("+host+";"+url+")vient de s'enregistrer");
    }

    public void delete (Vector v) {
	while(!v.isEmpty()) {
	    Vector tmp = (Vector)v.remove(0);
	    if (data.contains(tmp)){
		boolean bool = data.remove(tmp);
	    }
	}
    }
}

class LocalNodeSelection extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private JToolBar toolBar;
    private JButton refresh, apply, cancel, selectAll, deselectAll, delete;
    private JTextField visuHost, visuUrl;
    private TableModel table;
    private int sizeOfTheGraph;
    private FenetreDeSimulationDist ancetre;
    private VisidiaRegistryImpl parent;



    public LocalNodeSelection(String title,int sizeOfTheGraph,FenetreDeSimulationDist ancetre, VisidiaRegistryImpl parent) {
	super(title);
	this.ancetre=ancetre;
	this.parent=parent;
	this.sizeOfTheGraph = sizeOfTheGraph;
	mainPanel = new JPanel(new BorderLayout());
	toolBar = new JToolBar();
       	refresh = new JButton("refresh");
	apply = new JButton("apply");
	cancel = new JButton("cancel");
	selectAll = new JButton("select All");
	deselectAll = new JButton("deselect All");
	delete = new JButton("delete");
	try{
	    visuHost = new JTextField(java.net.InetAddress.getLocalHost().getHostName());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	visuUrl = new JTextField("Simulator");
	

	refresh.addActionListener(this);
	apply.addActionListener(this);
	cancel.addActionListener(this);
	selectAll.addActionListener(this);
	deselectAll.addActionListener(this);
	delete.addActionListener(this);
	

	JPanel center = new JPanel(new BorderLayout());
	table = new TableModel();
	JTable jtable = new  JTable(table);
	jtable.setPreferredScrollableViewportSize(new Dimension(500,150));
	JScrollPane scrollPane = new JScrollPane(jtable);
	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	JPanel visuPanel = new JPanel(new GridLayout(1,0));
	JLabel jl1 = new JLabel("Visualization Host");
	JLabel jl2 = new JLabel("Visualization URL");
	visuPanel.add(jl1);
	visuPanel.add(visuHost);
	visuPanel.add(jl2);
	visuPanel.add(visuUrl);
	visuPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Console Info"),
                                BorderFactory.createEmptyBorder(5,5,5,5)),
                visuPanel.getBorder()));
	center.add(scrollPane,BorderLayout.CENTER);
	center.add(visuPanel,BorderLayout.SOUTH);

	
	JPanel controlPanel = new JPanel();
	controlPanel.add(apply);
	controlPanel.add(cancel);

	toolBar.add(refresh);
	toolBar.add(selectAll);
	toolBar.add(deselectAll);
	toolBar.addSeparator(new Dimension(180,0));
	toolBar.add(delete);
	
	mainPanel.add(toolBar,BorderLayout.NORTH);
	mainPanel.add(center,BorderLayout.CENTER);
	mainPanel.add(controlPanel,BorderLayout.SOUTH);
	
	this.getContentPane().add(mainPanel,BorderLayout.CENTER);
	this.pack();
    }
    
    public void actionPerformed(ActionEvent ae) {
	JButton source = (JButton)ae.getSource();
	if (source == cancel) {
	    try{
		cancel();
	    } catch (Exception e) {
	    }
	} else if (source == apply) {
	    try {
		apply();
	    } catch (Exception e) {
	    }
	} else if (source == selectAll) {
	    selectAll();
	} else if (source == deselectAll) {
	    deselectAll();
	} else if (source == refresh) {
	    refresh();
	} else if (source == delete) {
	    delete();
	}
    }

    private void delete() {
	Vector v = new Vector();
	Vector selectedRows = getSelectedRows();
	while(!selectedRows.isEmpty()) {
	    int index =  ((Integer)selectedRows.remove(0)).intValue();
	    String aHost = (String)getValueAt(index,0);
	    String localNode = (String)getValueAt(index,1);
	    
	    table.remove(index);
	    table.fireTableDataChanged();
	    
	    selectedRows =  getSelectedRows();
	    Vector tmp = new Vector();
	    tmp .addElement(aHost);
	    tmp .addElement(localNode);
	    v.addElement(tmp);
	}
	table.fireTableDataChanged();
	parent.delete(v);
    }
    


    private void apply() throws Exception {
	LocalNodeTable lnt = new LocalNodeTable();
	
	Vector selectedRows = getSelectedRows();
	int localNodeNumber = selectedRows.size();
	if (localNodeNumber == 0) 
	    return ;
	if(sizeOfTheGraph<=localNodeNumber){
	    int i = 0;
	    while(!selectedRows.isEmpty() && i<sizeOfTheGraph) {
		int index =  ((Integer)selectedRows.remove(0)).intValue();
		String aHost = (String)getValueAt(index,0);
		String localNode = (String)getValueAt(index,1);
		Vector v = new Vector();
		v.addElement(new Integer(i));
		lnt.addLocalNode(aHost,localNode,v);
		i++;
	    }
	} else {
	    int pas = sizeOfTheGraph/localNodeNumber;
	    int reste = sizeOfTheGraph-localNodeNumber*pas;
	    int current = 0;
	    
	    while(!selectedRows.isEmpty()){
		int index =  ((Integer)selectedRows.remove(0)).intValue();
		String aHost = (String)getValueAt(index,0);
		String localNode = (String)getValueAt(index,1);
		if (reste>0){
		    Vector vect = new Vector();
		    for(int j=current; j<current+pas+1; j++)
			vect.addElement(new Integer(j));
		    lnt.addLocalNode(aHost,localNode,vect);
		    current=current+pas+1;
		    reste-=1;
		} else {
		    Vector vect = new Vector();
		    for(int j=current; j<current+pas; j++)
			vect.addElement(new Integer(j));
		    lnt.addLocalNode(aHost,localNode,vect);
		    current=current+pas;
		}
	    }
	}
	//lnt.print();
	String in2 = new String(); String in3= new String();
	if (visuHost.getText().equals("")){
	    try {
		in2 = java.net.InetAddress.getLocalHost().getHostName();
	    } catch (Exception e) {
	    }
	} else {
	    in2 = visuHost.getText();
	}
	if (visuUrl.getText().equals(""))
	    in3 = "Simulator";
	else
	    in3 = visuUrl.getText();
	
	ancetre.setNetworkParam(lnt,in2,in3);
	
	this.setVisible(false);
	this.dispose();
	try{
	    //this.finalize();
	} catch (Exception except) {
	    throw new Exception();
	    //except.printStackTrace();
	}
    }
    
    private void cancel() throws Exception {
	try{
	    this.setVisible(false);
	    this.dispose();
	    //this.finalize();
	} catch (Exception e) {
	    //e.printStackTrace();
	    throw new Exception();
	}
    }
    
    private void refresh() {
	parent.refresh(this);
    }

    private void selectAll() {
	table.selectAll();
    }

    private void deselectAll() {
	table.deselectAll();
    }

    public Vector getSelectedRows() {
	return table.getSelectedRows();
    }
    
    public Object getValueAt(int row, int col) {
	return table.getValueAt(row,col);
    }
    
    public void insert(String host,String url) {
	table.insert(host,url);
    }

    public void clean() {
	table.clean();
    }
}

class TableModel extends AbstractTableModel {
    Vector data, columnNames;
    public TableModel() {
	//Vector tmp = new Vector();
	data = new Vector();
	
	columnNames = new Vector();
	columnNames.addElement("Host");
	columnNames.addElement("URL");
	columnNames.addElement("Selected");
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
    
    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
	//Note that the data/cell address is constant,
	//no matter where the cell appears onscreen.
	if (col == 2) { 
	    return true;
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

    public void selectAll() {
	for (int i=0;i<data.size();i++) {
	    setValueAt(new Boolean(true),i,2);
	}
    }
    
    public void deselectAll() {
	for (int i=0;i<data.size();i++) {
	    setValueAt(new Boolean(false),i,2);
	}
    }
    
    public void remove(int index) {
	data.remove(index);
	fireTableDataChanged();
    }

    public void clean() {
	data = new Vector();
	fireTableDataChanged();
    }
    
    public void insert(String host, String url){
	int index = getRowCount();
	Vector tmp = new Vector();
	tmp.addElement(host);
	tmp.addElement(url);
	tmp.addElement(new Boolean(true));
	if (data.contains(tmp))
	    return;
	tmp.set(2,new Boolean(false));
	if (data.contains(tmp))
	    return;
	data.addElement(tmp);
	fireTableRowsInserted(index,index);
    }
    
    public Vector getSelectedRows() {
	Vector v = new Vector();
	for (int i=0; i<data.size(); i++){
	    if (((Boolean)((Vector)data.elementAt(i)).elementAt(2)).equals(new Boolean(true))){
		v.addElement(new Integer(i));
	    }
	}
	return v;
    }
}
