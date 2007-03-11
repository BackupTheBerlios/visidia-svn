package visidia.network;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.AbstractTableModel;

import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulationDist;
import visidia.tools.LocalNodeTable;

public class VisidiaRegistryImpl extends UnicastRemoteObject implements
		VisidiaRegistry {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5861826658680264074L;

	private FenetreDeSimulationDist parent;

	// private LocalNodeSelection lns;
	private Vector data;

	public VisidiaRegistryImpl(FenetreDeSimulationDist parent)
			throws RemoteException {
		super();
		this.parent = parent;
		this.data = new Vector();
	}

	public void showLocalNodes(int sizeOfTheGraph) throws RemoteException {
		LocalNodeSelection lns = new LocalNodeSelection("Local Node Selection",
				sizeOfTheGraph, this.parent, this);
		this.refresh(lns);
		lns.setVisible(true);
	}

	public void refresh(LocalNodeSelection lns) {
		lns.clean();
		for (int i = 0; i < this.data.size(); i++) {
			Vector ieme = (Vector) this.data.elementAt(i);
			lns.insert((String) ieme.elementAt(0), (String) ieme.elementAt(1));
		}
	}

	public void init(String url, Registry registry) throws RemoteException {
		try {
			registry.bind(url, this);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.parent,
					"The Visidia Registration is not running : \n"
							+ e.toString(), "Error",
					JOptionPane.WARNING_MESSAGE);
			System.out
					.println("##########################################################");
			System.out
					.println("Erreur lors de l'initialisation de la VisidiaRegsitry");
			System.out
					.println("##########################################################");
			System.out.println("");
			e.printStackTrace();
			System.out.println("");
			System.out
					.println("##########################################################");
		}
	}

	public void register(NodeServer localNode, String host, String url)
			throws RemoteException {
		Vector tmp = new Vector();
		tmp.addElement(host);
		tmp.addElement(url);
		if (!this.data.contains(tmp))
			this.data.addElement(tmp);
		System.out.println("Le noeud local (" + host + ";" + url
				+ ")vient de s'enregistrer");
	}

	public void delete(Vector v) {
		while (!v.isEmpty()) {
			Vector tmp = (Vector) v.remove(0);
			if (this.data.contains(tmp)) {
				// boolean bool =
				this.data.remove(tmp);
			}
		}
	}
}

class LocalNodeSelection extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4575074465934081038L;

	private JPanel mainPanel;

	private JToolBar toolBar;

	private JButton refresh, apply, cancel, selectAll, deselectAll, delete;

	private JTextField visuHost, visuUrl;

	private TableModel table;

	private int sizeOfTheGraph;

	private FenetreDeSimulationDist ancetre;

	private VisidiaRegistryImpl parent;

	public LocalNodeSelection(String title, int sizeOfTheGraph,
			FenetreDeSimulationDist ancetre, VisidiaRegistryImpl parent) {
		super(title);
		this.ancetre = ancetre;
		this.parent = parent;
		this.sizeOfTheGraph = sizeOfTheGraph;
		this.mainPanel = new JPanel(new BorderLayout());
		this.toolBar = new JToolBar();
		this.refresh = new JButton("refresh");
		this.apply = new JButton("apply");
		this.cancel = new JButton("cancel");
		this.selectAll = new JButton("select All");
		this.deselectAll = new JButton("deselect All");
		this.delete = new JButton("delete");
		try {
			this.visuHost = new JTextField(java.net.InetAddress.getLocalHost()
					.getHostName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.visuUrl = new JTextField("Simulator");

		this.refresh.addActionListener(this);
		this.apply.addActionListener(this);
		this.cancel.addActionListener(this);
		this.selectAll.addActionListener(this);
		this.deselectAll.addActionListener(this);
		this.delete.addActionListener(this);

		JPanel center = new JPanel(new BorderLayout());
		this.table = new TableModel();
		JTable jtable = new JTable(this.table);
		jtable.setPreferredScrollableViewportSize(new Dimension(500, 150));
		JScrollPane scrollPane = new JScrollPane(jtable);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel visuPanel = new JPanel(new GridLayout(1, 0));
		JLabel jl1 = new JLabel("Visualization Host");
		JLabel jl2 = new JLabel("Visualization URL");
		visuPanel.add(jl1);
		visuPanel.add(this.visuHost);
		visuPanel.add(jl2);
		visuPanel.add(this.visuUrl);
		visuPanel
				.setBorder(BorderFactory.createCompoundBorder(BorderFactory
						.createCompoundBorder(BorderFactory
								.createTitledBorder("Console Info"),
								BorderFactory.createEmptyBorder(5, 5, 5, 5)),
						visuPanel.getBorder()));
		center.add(scrollPane, BorderLayout.CENTER);
		center.add(visuPanel, BorderLayout.SOUTH);

		JPanel controlPanel = new JPanel();
		controlPanel.add(this.apply);
		controlPanel.add(this.cancel);

		this.toolBar.add(this.refresh);
		this.toolBar.add(this.selectAll);
		this.toolBar.add(this.deselectAll);
		this.toolBar.addSeparator(new Dimension(180, 0));
		this.toolBar.add(this.delete);

		this.mainPanel.add(this.toolBar, BorderLayout.NORTH);
		this.mainPanel.add(center, BorderLayout.CENTER);
		this.mainPanel.add(controlPanel, BorderLayout.SOUTH);

		this.getContentPane().add(this.mainPanel, BorderLayout.CENTER);
		this.pack();
	}

	public void actionPerformed(ActionEvent ae) {
		JButton source = (JButton) ae.getSource();
		if (source == this.cancel) {
			try {
				this.cancel();
			} catch (Exception e) {
			}
		} else if (source == this.apply) {
			try {
				this.apply();
			} catch (Exception e) {
			}
		} else if (source == this.selectAll) {
			this.selectAll();
		} else if (source == this.deselectAll) {
			this.deselectAll();
		} else if (source == this.refresh) {
			this.refresh();
		} else if (source == this.delete) {
			this.delete();
		}
	}

	private void delete() {
		Vector v = new Vector();
		Vector selectedRows = this.getSelectedRows();
		while (!selectedRows.isEmpty()) {
			int index = ((Integer) selectedRows.remove(0)).intValue();
			String aHost = (String) this.getValueAt(index, 0);
			String localNode = (String) this.getValueAt(index, 1);

			this.table.remove(index);
			this.table.fireTableDataChanged();

			selectedRows = this.getSelectedRows();
			Vector tmp = new Vector();
			tmp.addElement(aHost);
			tmp.addElement(localNode);
			v.addElement(tmp);
		}
		this.table.fireTableDataChanged();
		this.parent.delete(v);
	}

	private void apply() throws Exception {
		LocalNodeTable lnt = new LocalNodeTable();

		Vector selectedRows = this.getSelectedRows();
		int localNodeNumber = selectedRows.size();
		if (localNodeNumber == 0)
			return;
		if (this.sizeOfTheGraph <= localNodeNumber) {
			int i = 0;
			while (!selectedRows.isEmpty() && (i < this.sizeOfTheGraph)) {
				int index = ((Integer) selectedRows.remove(0)).intValue();
				String aHost = (String) this.getValueAt(index, 0);
				String localNode = (String) this.getValueAt(index, 1);
				Vector v = new Vector();
				v.addElement(new Integer(i));
				lnt.addLocalNode(aHost, localNode, v);
				i++;
			}
		} else {
			int pas = this.sizeOfTheGraph / localNodeNumber;
			int reste = this.sizeOfTheGraph - localNodeNumber * pas;
			int current = 0;

			while (!selectedRows.isEmpty()) {
				int index = ((Integer) selectedRows.remove(0)).intValue();
				String aHost = (String) this.getValueAt(index, 0);
				String localNode = (String) this.getValueAt(index, 1);
				if (reste > 0) {
					Vector vect = new Vector();
					for (int j = current; j < current + pas + 1; j++)
						vect.addElement(new Integer(j));
					lnt.addLocalNode(aHost, localNode, vect);
					current = current + pas + 1;
					reste -= 1;
				} else {
					Vector vect = new Vector();
					for (int j = current; j < current + pas; j++)
						vect.addElement(new Integer(j));
					lnt.addLocalNode(aHost, localNode, vect);
					current = current + pas;
				}
			}
		}
		// lnt.print();
		String in2 = new String();
		String in3 = new String();
		if (this.visuHost.getText().equals("")) {
			try {
				in2 = java.net.InetAddress.getLocalHost().getHostName();
			} catch (Exception e) {
			}
		} else {
			in2 = this.visuHost.getText();
		}
		if (this.visuUrl.getText().equals(""))
			in3 = "Simulator";
		else
			in3 = this.visuUrl.getText();

		this.ancetre.setNetworkParam(lnt, in2, in3);

		this.setVisible(false);
		this.dispose();
		try {
			// this.finalize();
		} catch (Exception except) {
			throw new Exception();
			// except.printStackTrace();
		}
	}

	private void cancel() throws Exception {
		try {
			this.setVisible(false);
			this.dispose();
			// this.finalize();
		} catch (Exception e) {
			// e.printStackTrace();
			throw new Exception();
		}
	}

	private void refresh() {
		this.parent.refresh(this);
	}

	private void selectAll() {
		this.table.selectAll();
	}

	private void deselectAll() {
		this.table.deselectAll();
	}

	public Vector getSelectedRows() {
		return this.table.getSelectedRows();
	}

	public Object getValueAt(int row, int col) {
		return this.table.getValueAt(row, col);
	}

	public void insert(String host, String url) {
		this.table.insert(host, url);
	}

	public void clean() {
		this.table.clean();
	}
}

class TableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2939182826721062106L;

	Vector data, columnNames;

	public TableModel() {
		// Vector tmp = new Vector();
		this.data = new Vector();

		this.columnNames = new Vector();
		this.columnNames.addElement("Host");
		this.columnNames.addElement("URL");
		this.columnNames.addElement("Selected");
	}

	public int getColumnCount() {
		return this.columnNames.size();
	}

	public int getRowCount() {
		return this.data.size();
	}

	public String getColumnName(int col) {
		return (String) this.columnNames.elementAt(col);
	}

	public Object getValueAt(int row, int col) {
		return ((Vector) this.data.elementAt(row)).elementAt(col);
	}

	/*
	 * JTable uses this method to determine the default renderer/ editor for
	 * each cell. If we didn't implement this method, then the last column would
	 * contain text ("true"/"false"), rather than a check box.
	 */
	public Class getColumnClass(int c) {
		return this.getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		if (col == 2) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		((Vector) this.data.elementAt(row)).set(col, value);
		this.fireTableCellUpdated(row, col);
	}

	public void selectAll() {
		for (int i = 0; i < this.data.size(); i++) {
			this.setValueAt(new Boolean(true), i, 2);
		}
	}

	public void deselectAll() {
		for (int i = 0; i < this.data.size(); i++) {
			this.setValueAt(new Boolean(false), i, 2);
		}
	}

	public void remove(int index) {
		this.data.remove(index);
		this.fireTableDataChanged();
	}

	public void clean() {
		this.data = new Vector();
		this.fireTableDataChanged();
	}

	public void insert(String host, String url) {
		int index = this.getRowCount();
		Vector tmp = new Vector();
		tmp.addElement(host);
		tmp.addElement(url);
		tmp.addElement(new Boolean(true));
		if (this.data.contains(tmp))
			return;
		tmp.set(2, new Boolean(false));
		if (this.data.contains(tmp))
			return;
		this.data.addElement(tmp);
		this.fireTableRowsInserted(index, index);
	}

	public Vector getSelectedRows() {
		Vector v = new Vector();
		for (int i = 0; i < this.data.size(); i++) {
			if (((Boolean) ((Vector) this.data.elementAt(i)).elementAt(2))
					.equals(new Boolean(true))) {
				v.addElement(new Integer(i));
			}
		}
		return v;
	}
}
