package visidia.gui.presentation.userInterfaceEdition;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import visidia.network.NodeServer;
import visidia.network.NodeServerImpl;

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

	/**
	 * 
	 */
	private static final long serialVersionUID = 5496722266864029765L;

	Vector data;

	Vector<String> columnNames;

	Vector notEditable;

	public MyTableModel() {
		this.notEditable = new Vector();
		Vector tmp = new Vector();
		this.data = new Vector();

		tmp.addElement(new Boolean(true));
		tmp.addElement("enter the local Node Url");
		tmp.addElement("none");
		this.data.addElement(tmp);

		this.columnNames = new Vector<String>();
		this.columnNames.addElement("Selected");
		this.columnNames.addElement("Local Node URL");
		this.columnNames.addElement("State");
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

	public void setNotEditable(int row) {
		this.notEditable.addElement(new Integer(row));
	}

	public void setEditable(int row) {
		this.notEditable.remove(new Integer(row));
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		if (col == 0) {
			return true;
		}
		if (col == 1) {
			// if (notEditable != null)
			if (this.notEditable.contains(new Integer(row))) {
				return false;
			} else {
				return true;
				// else
				// return true;
			}
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

	public void remove(int row) {
		this.data.remove(row);
		this.fireTableDataChanged();
	}

	public void insert() {
		int index = this.getRowCount();
		Vector tmp = new Vector();
		tmp.addElement(new Boolean(true));
		tmp.addElement("");
		tmp.addElement("none");
		this.data.addElement(tmp);
		this.fireTableRowsInserted(index, index);
	}

	public int insert(String url) {
		int index = this.getRowCount();
		Vector tmp = new Vector();
		tmp.addElement(new Boolean(true));
		tmp.addElement(url);
		tmp.addElement("none");
		this.data.addElement(tmp);
		this.fireTableRowsInserted(index, index);
		return index;
	}

	public Vector getSelectedRows() {
		Vector v = new Vector();
		for (int i = 0; i < this.data.size(); i++) {
			if (((Boolean) ((Vector) this.data.elementAt(i)).elementAt(0))
					.equals(new Boolean(true))) {
				v.addElement(new Integer(i));
			}
		}
		return v;
	}
}

public class LocalNodeLauncher extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7808189195838882926L;

	protected JPanel registryPanel, localNodesPanel, localNodesGroup,
			controlPanel, controlButton, rmiRegistry, remoteRegistry;

	protected JTabbedPane tabbedPane;

	protected JToolBar toolBar;

	protected JMenuItem new_launcher;

	protected JTable table;

	protected MyTableModel myModel;

	protected JSpinner js;

	protected SpinnerNumberModel spinnerModel;

	protected JScrollPane rmiScroller;

	protected JSplitPane mainPane, contentPane;

	protected JTextPane errorArea;

	protected StyledDocument doc;

	protected MutableAttributeSet keyWord;

	protected JButton exit, start, startAll, registerAll, register, killAll,
			kill, delete, add, startRegistry, killRegistry, clear;

	protected JRadioButton runRegistry, manyLocalNodes;

	protected JCheckBox debug, fullDebug;

	protected JTextField portRegistry, visualizationHost, visualizationUrl,
			localHost;

	protected Border etchedBorder;

	protected Hashtable nodeServers;

	protected Registry registry;

	protected int count = 0;

	protected final String DEFAULT_URL = "";

	public LocalNodeLauncher(String title) {
		super(title);
		this.nodeServers = new Hashtable();

		this.etchedBorder = BorderFactory.createEtchedBorder();
		// Panel contenant la configuration pour les noeuds locaux
		this.setUpContentPane();

		// panel conteannt les bouttons exit etc ...
		this.setUpControlPanel();

		// panel root de la JFrame
		this.setUpFrame();
	}

	/**
	 * add all componenets to frame and show it
	 */
	protected void setUpContentPane() {
		/** ******************************************************* */
		/* The panel where the local nodes are configured */
		/** ******************************************************* */
		this.setUpLocalNodeSettings();
		/** ******************************************************* */
		/* The panel where the registration is configured */
		/** ******************************************************* */
		this.setUpRegistration();
		/** *************************************************** */
		/* Final settings */
		/** *************************************************** */
		this.contentPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				this.rmiScroller, this.localNodesPanel);
		this.contentPane.setOneTouchExpandable(true);
		this.contentPane.setDividerLocation(165);
	}

	/** ******************************* */
	/* Handling action events */
	/** ******************************** */
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		if (source instanceof JRadioButton) {
			source = (JRadioButton) source;
			if (source == this.manyLocalNodes) {
				this.js.setEnabled(this.manyLocalNodes.isSelected());
			} else if (source == this.runRegistry) {
				this.startRegistry.setEnabled(this.runRegistry.isSelected());
			}
		} else if (source instanceof JButton) {
			source = (JButton) source;
			if (source == this.start) {
				this.write("\nStarting Selected Nodes", Color.blue);
				this.start();
				this.register.setEnabled(true);
				this.kill.setEnabled(true);
				this.registerAll.setEnabled(true);
				this.killAll.setEnabled(true);
			} else if (source == this.register) {
				this.write("\nregistering !", Color.blue);
				this.register();
			} else if (source == this.kill) {
				System.out.println("killing !");
				this.kill();
			} else if (source == this.delete) {
				this.delete();
			} else if (source == this.add) {
				System.out.println("adding !");
				this.myModel.insert();
			} else if (source == this.startAll) {
				System.out.println("starting All !");
				this.startAll();
			} else if (source == this.registerAll) {
				System.out.println("registering All !");
				this.registerAll();
			} else if (source == this.killAll) {
				System.out.println("killing All !");
				this.killAll();
			} else if (source == this.exit) {
				System.out.println("THE END !");
				this.exit();
			} else if (source == this.startRegistry) {
				this.startRegistry();
			} else if (source == this.killRegistry) {
				this.killRegistry();
			} else if (source == this.clear) {
				this.clear();
			}
		} else if (source instanceof JMenuItem) {
			source = (JMenuItem) source;
			if (source == this.new_launcher) {
				this.newLauncher();
			}
		}
	}

	/**
	 * create an instance of all selected and not running localNodes. The local
	 * node URLs are no longer editable
	 */
	private void start() {
		if (!this.manyLocalNodes.isSelected()) {
			Vector selected = this.myModel.getSelectedRows();
			this.write(selected.toString(), Color.blue);
			for (int i = 0; i < selected.size(); i++) {
				int row = ((Integer) selected.elementAt(i)).intValue();
				if (!(((String) this.myModel.getValueAt(row, 2))
						.equals("started") || ((String) this.myModel
						.getValueAt(row, 2)).equals("registered"))) {
					String hostText = this.localHost.getText();
					String portText = this.portRegistry.getText();
					String url = (String) this.myModel.getValueAt(row, 1);

					boolean bool = true;
					try {
						NodeServer nodeServer = new NodeServerImpl(hostText,
								portText);
						nodeServer.setUrlName(url);
						try {
							Naming.bind("rmi://" + hostText + ":" + portText
									+ "/NodeServer/" + url, nodeServer);
						} catch (Exception e2) {
							try {
								UnicastRemoteObject.unexportObject(nodeServer,
										true);
								bool = false;
							} catch (Exception e3) {
							}
							if (this.debug.isSelected()) {
								this.write(e2);
							}
						}
						if (bool) {
							this.nodeServers.put(new Integer(row), nodeServer);
							this.myModel.setValueAt("started", row, 2);
							this.myModel.setNotEditable(row);
							this.write("\nLocal Node " + url + " is started",
									Color.blue);
						}
					} catch (Exception e1) {
						this.write(e1);
					}
				}
			}
		} else if (this.manyLocalNodes.isSelected()) {
			int n = 0;
			try {
				n = this.spinnerModel.getNumber().intValue();
			} catch (Exception e) {
				e.printStackTrace();
				this.write("\nLocal node Number not compatible", Color.red);
			}

			this.write("\nStrating " + n + " localNodes with Urls : 0 ... "
					+ (n - 1), Color.blue);
			int min = this.count;
			int max = this.count + n;
			for (int i = min; i < max; i++) {
				String hostText = this.localHost.getText();
				String portText = this.portRegistry.getText();
				boolean bool = true;
				try {
					NodeServer nodeServer = new NodeServerImpl(hostText,
							portText);
					nodeServer.setUrlName(this.DEFAULT_URL + this.count);
					try {
						Naming.bind("rmi://" + hostText + ":" + portText
								+ "/NodeServer/" + this.DEFAULT_URL
								+ this.count, nodeServer);
					} catch (Exception e2) {
						try {
							UnicastRemoteObject
									.unexportObject(nodeServer, true);
							bool = false;
						} catch (Exception e3) {
						}
						if (this.debug.isSelected()) {
							this.write(e2);
						}
					}
					if (bool) {
						int index = this.myModel.insert(this.DEFAULT_URL
								+ this.count);
						this.nodeServers.put(new Integer(index), nodeServer);
						this.myModel.setValueAt("started", index, 2);
						this.myModel.setNotEditable(index);
						this.write("\nLocal Node " + this.DEFAULT_URL
								+ this.count + " is started", Color.blue);
						this.count++;
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private void startAll() {
		for (int i = 0; i < this.myModel.getRowCount(); i++) {
			if (!(((String) this.myModel.getValueAt(i, 2)).equals("started") || ((String) this.myModel
					.getValueAt(i, 2)).equals("registered"))) {
				String hostText = this.localHost.getText();
				String portText = this.portRegistry.getText();
				String url = (String) this.myModel.getValueAt(i, 1);
				boolean bool = true;
				try {
					NodeServer nodeServer = new NodeServerImpl(hostText,
							portText);
					nodeServer.setUrlName(url);
					try {
						Naming.bind("rmi://" + hostText + ":" + portText
								+ "/NodeServer/" + url, nodeServer);
					} catch (Exception e2) {
						try {
							UnicastRemoteObject
									.unexportObject(nodeServer, true);
							bool = false;
						} catch (Exception e3) {
						}
						if (this.debug.isSelected()) {
							this.write(e2);
						}
					}
					if (bool) {
						this.nodeServers.put(new Integer(i), nodeServer);
						this.myModel.setValueAt("started", i, 2);
						this.myModel.setNotEditable(i);
						this.write("\nLocal Node " + url + " is started",
								Color.blue);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * register all the selected and started local node using the Visidia
	 * Registry
	 */
	private void register() {
		Vector selected = this.myModel.getSelectedRows();
		for (int i = 0; i < selected.size(); i++) {
			int row = ((Integer) selected.elementAt(i)).intValue();
			if (((String) this.myModel.getValueAt(row, 2)).equals("started")) {
				// String hostText = localHost.getText();
				// String portText = portRegistry.getText();
				// String url = (String)this.myModel.getValueAt(row,1);
				try {
					NodeServer nodeServer = (NodeServer) this.nodeServers
							.get(new Integer(row));
					nodeServer.register(this.visualizationHost.getText(),
							this.visualizationUrl.getText());
					this.myModel.setValueAt("registered", row, 2);
				} catch (Exception e) {
					if (this.debug.isSelected()) {
						this.write(e);
					}
				}
			}
		}
	}

	/**
	 * register all the started localNodes
	 */
	private void registerAll() {
		for (int i = 0; i < this.myModel.getRowCount(); i++) {
			if (((String) this.myModel.getValueAt(i, 2)).equals("started")) {
				// String hostText = localHost.getText();
				// String portText = portRegistry.getText();
				// String url = (String)this.myModel.getValueAt(i,1);
				try {
					NodeServer nodeServer = (NodeServer) this.nodeServers
							.get(new Integer(i));
					nodeServer.register(this.visualizationHost.getText(),
							this.visualizationUrl.getText());
					this.myModel.setValueAt("registered", i, 2);
				} catch (Exception e) {
					if (this.debug.isSelected()) {
						this.write(e);
					}
				}
			}
		}
	}

	private void kill() {
		Vector selected = this.myModel.getSelectedRows();
		this.write(selected.toString(), Color.blue);
		for (int i = 0; i < selected.size(); i++) {
			int row = ((Integer) selected.elementAt(i)).intValue();
			if (((String) this.myModel.getValueAt(row, 2)).equals("started")
					|| ((String) this.myModel.getValueAt(row, 2))
							.equals("registered")) {
				String hostText = this.localHost.getText();
				String portText = this.portRegistry.getText();
				String url = (String) this.myModel.getValueAt(row, 1);
				boolean bool = true;
				try {
					NodeServer nodeServer = (NodeServer) this.nodeServers
							.remove(new Integer(row));
					try {
						Naming.unbind("rmi://" + hostText + ":" + portText
								+ "/NodeServer/" + url);
					} catch (Exception expt) {
						this.write(
								"\nCouldn't unbind Local Node from registry",
								Color.red);
						if (this.fullDebug.isSelected()) {
							this.write(expt);
						}
					}
					UnicastRemoteObject.unexportObject(nodeServer, true);
				} catch (Exception e) {
					bool = false;
					if (this.debug.isSelected()) {
						this.write(e);
					}
				}
				if (bool) {
					this.myModel.setValueAt("killed", row, 2);
					this.myModel.setEditable(row);
					this
							.write("\nLocal Node " + url + " is killed",
									Color.blue);
				}
			}
		}
	}

	private void killAll() {
		for (int i = 0; i < this.myModel.getRowCount(); i++) {
			if (((String) this.myModel.getValueAt(i, 2)).equals("started")
					|| ((String) this.myModel.getValueAt(i, 2))
							.equals("registered")) {
				String hostText = this.localHost.getText();
				String portText = this.portRegistry.getText();
				String url = (String) this.myModel.getValueAt(i, 1);
				boolean bool = true;
				try {
					NodeServer nodeServer = (NodeServer) this.nodeServers
							.remove(new Integer(i));
					try {
						Naming.unbind("rmi://" + hostText + ":" + portText
								+ "/NodeServer/" + url);
					} catch (Exception expt) {
						this.write(
								"\nCouldn't unbind Local Node from registry",
								Color.red);
						if (this.fullDebug.isSelected()) {
							this.write(expt);
						}
					}
					UnicastRemoteObject.unexportObject(nodeServer, true);
				} catch (Exception e) {
					bool = false;
					if (this.debug.isSelected()) {
						this.write(e);
					}
				}
				if (bool) {
					this.myModel.setValueAt("killed", i, 2);
					this.myModel.setEditable(i);
					this
							.write("\nLocal Node " + url + " is killed",
									Color.blue);
				}
			}
		}
	}

	private void delete() {
		int retour = JOptionPane.showConfirmDialog(this,
				"selected Local Nodes will be killed\ncontinue any way ?",
				"Confirm local node deletion", JOptionPane.YES_NO_OPTION);
		if (retour == JOptionPane.YES_OPTION) {
			this.kill();
			Vector selected = this.myModel.getSelectedRows();
			while (!selected.isEmpty()) {
				int row = ((Integer) selected.elementAt(0)).intValue();
				this.myModel.remove(row);
				selected = this.myModel.getSelectedRows();
			}
		} else if (retour == JOptionPane.NO_OPTION) {
		}
	}

	private void exit() {
		int retour = JOptionPane.showConfirmDialog(this,
				"Exiting will kill the running Local Nodes\nexit any way ?",
				"Confirm Exit", JOptionPane.YES_NO_OPTION);
		if (retour == JOptionPane.YES_OPTION) {
			this.killAll();
			this.dispose();
			this.setVisible(false);
			System.exit(0);
		} else if (retour == JOptionPane.NO_OPTION) {
		}
	}

	private void startRegistry() {
		try {
			this.write("\nCreating an RMI registry", Color.orange);
			this.registry = LocateRegistry.createRegistry((new Integer(
					this.portRegistry.getText())).intValue());
			this.write("\nRMI registry created", Color.orange);
			this.killRegistry.setEnabled(true);
		} catch (Exception e) {
			if (this.debug.isSelected()) {
				this.write(e);
				this.write("\nRegistry creation failed", Color.orange);
			}

		}
	}

	private void killRegistry() {
		try {
			this.write("\nDestroying RMI registry", Color.orange);
			UnicastRemoteObject.unexportObject(this.registry, true);
			this.registry = null;
			this.killRegistry.setEnabled(false);
			this.write("\nfinished", Color.orange);
		} catch (Exception e) {
			if (this.debug.isSelected()) {
				this.write(e);
				this.write("\nError when killing RMI registry", Color.orange);
			}
		}
	}

	private void clear() {
		try {
			this.doc.remove(0, this.doc.getLength());
		} catch (Exception e) {
		}
	}

	private void newLauncher() {
		try {
			Runtime r = Runtime.getRuntime();
			// Process p =
			r.exec("java -Xmx1024M visidia.network.LocalNodeLauncher");
		} catch (Exception e) {
			this.write(e);
		}
	}

	public void write(String s, Color c) {
		try {
			StyleConstants.setForeground(this.keyWord, c);
			this.doc.insertString(this.doc.getLength(), s, this.keyWord);
		} catch (Exception e) {
		}
	}

	private void write(Exception e) {
		try {
			StackTraceElement[] ste = e.getStackTrace();
			String textError = "\n" + e.toString();
			this.write(textError, Color.red);
			if (this.fullDebug.isSelected()) {
				for (StackTraceElement element : ste) {
					textError += "\n" + element.toString();
				}
				this.write(textError, Color.black);
			}
		} catch (Exception expt) {
		}
	}

	/** **************************************************************** */
	/* Method for setting up components */
	/** ***************************************************************** */
	protected void setUpControlPanel() {
		this.controlButton = new JPanel(new FlowLayout());
		this.killAll = new JButton("Kill All");
		this.killAll.setEnabled(false);
		this.startAll = new JButton("Start All");
		this.registerAll = new JButton("Register All");
		this.registerAll.setEnabled(false);
		this.exit = new JButton("Exit");

		this.killAll.addActionListener(this);
		this.startAll.addActionListener(this);
		this.registerAll.addActionListener(this);
		this.exit.addActionListener(this);

		this.controlButton.add(this.startAll);
		this.controlButton.add(this.registerAll);
		this.controlButton.add(this.killAll);
		this.controlButton.add(this.exit);

		JPanel debugPanel = new JPanel(new FlowLayout());
		this.debug = new JCheckBox("Enable Log", true);
		this.fullDebug = new JCheckBox("Print Error Trace", false);
		this.clear = new JButton("clear");
		this.clear.setToolTipText("clear text");
		this.clear.addActionListener(this);
		debugPanel.add(this.debug);
		debugPanel.add(this.fullDebug);
		debugPanel.add(this.clear);
		this.errorArea = new JTextPane();
		this.doc = this.errorArea.getStyledDocument();
		this.keyWord = new SimpleAttributeSet();
		this.doc.setParagraphAttributes(0, 0, this.keyWord, true);
		this.errorArea.setEditable(false);
		JScrollPane areaScrollPane = new JScrollPane(this.errorArea);
		areaScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setBorder(BorderFactory.createTitledBorder(
				this.etchedBorder, "Log Info"));
		this.controlPanel = new JPanel(new BorderLayout());
		this.controlPanel.add(debugPanel, BorderLayout.NORTH);
		this.controlPanel.add(areaScrollPane, BorderLayout.CENTER);
		this.controlPanel.add(this.controlButton, BorderLayout.SOUTH);
	}

	public void setUpFrame() {
		/*
		 * JMenuBar menuBar = new JMenuBar(); JMenu menu = new JMenu("File");
		 * new_launcher = new JMenuItem("New Launcher");
		 * new_launcher.addActionListener(this); menu.add(new_launcher);
		 * menuBar.add(menu);
		 */
		this.mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				this.contentPane, this.controlPanel);
		this.mainPane.setOneTouchExpandable(true);
		this.mainPane.setDividerLocation(240);
		this.mainPane.setPreferredSize(new Dimension(560, 400));

		this.getContentPane().add(this.mainPane, BorderLayout.CENTER);
		// this.getContentPane().add(menuBar,BorderLayout.NORTH);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * first method for SetUpContentPane
	 */
	private void setUpLocalNodeSettings() {
		this.localNodesPanel = new JPanel(new BorderLayout());
		this.localNodesPanel.setBorder(BorderFactory.createTitledBorder(
				this.etchedBorder, "Local Node Settings"));

		this.toolBar = new JToolBar();
		this.start = new JButton("start");
		this.register = new JButton("register");
		this.register.setEnabled(false);
		this.kill = new JButton("kill");
		this.kill.setEnabled(false);
		this.delete = new JButton("delete");
		this.add = new JButton("add a local Node");

		this.start.addActionListener(this);
		this.register.addActionListener(this);
		this.kill.addActionListener(this);
		this.delete.addActionListener(this);
		this.add.addActionListener(this);

		this.toolBar.add(this.start);
		this.toolBar.add(this.register);
		this.toolBar.addSeparator(new Dimension(25, 0));
		this.toolBar.add(this.kill);
		this.toolBar.add(this.delete);
		this.toolBar.addSeparator(new Dimension(25, 0));
		this.toolBar.add(this.add);

		JPanel panelCenter = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		panelCenter.setLayout(gbl);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		this.localNodesGroup = new JPanel(new GridLayout(0, 2));
		this.localNodesGroup.setBorder(BorderFactory
				.createTitledBorder(this.etchedBorder));
		JLabel label1 = new JLabel(" Local Host ");
		this.manyLocalNodes = new JRadioButton(" Number of Local Nodes ");
		this.manyLocalNodes.addActionListener(this);
		this.spinnerModel = new SpinnerNumberModel(new Integer(2), new Integer(
				1), new Integer(1000), new Integer(1));
		this.js = new JSpinner(this.spinnerModel);
		this.js.setEnabled(false);
		try {
			this.localHost = new JTextField(java.net.InetAddress.getLocalHost()
					.getHostName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.localNodesGroup.add(label1);
		this.localNodesGroup.add(this.localHost);
		this.localNodesGroup.add(this.manyLocalNodes);
		this.localNodesGroup.add(this.js);
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbl.setConstraints(this.localNodesGroup, gbc);
		panelCenter.add(this.localNodesGroup);

		this.myModel = new MyTableModel();
		this.table = new JTable(this.myModel);
		this.table.setPreferredScrollableViewportSize(new Dimension(200, 100));
		JScrollPane scrollPane = new JScrollPane(this.table);
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.ipady = 100;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbl.setConstraints(scrollPane, gbc);
		panelCenter.add(scrollPane);

		this.localNodesPanel.add(panelCenter, BorderLayout.CENTER);
		this.localNodesPanel.add(this.toolBar, BorderLayout.NORTH);
	}

	/**
	 * second method for SetUpContentPane
	 */
	private void setUpRegistration() {
		this.registryPanel = new JPanel();
		this.registryPanel.setLayout(new BorderLayout());
		this.rmiScroller = new JScrollPane(this.registryPanel);
		JPanel registryPanelNorth = new JPanel();

		/*
		 * first panel for rmiregistry configuration
		 */
		this.rmiRegistry = new JPanel(new GridLayout(0, 1));
		this.rmiRegistry.setBorder(BorderFactory.createTitledBorder(
				this.etchedBorder, "RMI registry"));

		this.runRegistry = new JRadioButton("run registry on port");
		this.runRegistry.setSelected(false);
		this.portRegistry = new JTextField("1099");
		this.rmiRegistry.add(this.portRegistry);
		this.startRegistry = new JButton("start registry");
		this.startRegistry.setEnabled(false);
		this.killRegistry = new JButton("kill registry");
		this.killRegistry.setEnabled(false);

		this.rmiRegistry.add(this.runRegistry);
		this.rmiRegistry.add(this.portRegistry);
		this.rmiRegistry.add(this.startRegistry);
		this.rmiRegistry.add(this.killRegistry);

		this.runRegistry.addActionListener(this);
		this.startRegistry.addActionListener(this);
		this.killRegistry.addActionListener(this);

		/*
		 * second panel for Visidia registration
		 */
		this.remoteRegistry = new JPanel();
		this.remoteRegistry.setBorder(BorderFactory.createTitledBorder(
				this.etchedBorder, "Remote registration"));

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.remoteRegistry.setLayout(gbl);

		JLabel jl1 = new JLabel(" Host  ");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.ipady = 7;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbl.setConstraints(jl1, gbc);
		this.remoteRegistry.add(jl1);

		this.visualizationHost = new JTextField();
		gbc.ipady = 7;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbl.setConstraints(this.visualizationHost, gbc);
		this.remoteRegistry.add(this.visualizationHost);

		JLabel jl2 = new JLabel(" URL ");
		gbc.ipady = 7;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbl.setConstraints(jl2, gbc);
		this.remoteRegistry.add(jl2);

		this.visualizationUrl = new JTextField();
		gbc.ipady = 7;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbl.setConstraints(this.visualizationUrl, gbc);
		this.remoteRegistry.add(this.visualizationUrl);

		GridBagLayout gblNorth = new GridBagLayout();
		GridBagConstraints gbcNorth = new GridBagConstraints();
		registryPanelNorth.setLayout(gblNorth);

		gbcNorth.fill = GridBagConstraints.HORIZONTAL;
		gbcNorth.anchor = GridBagConstraints.NORTH;
		gbcNorth.weightx = 1;
		gbcNorth.weighty = 1;
		gbcNorth.gridx = 0;
		gbcNorth.gridy = 0;
		gblNorth.setConstraints(this.rmiRegistry, gbcNorth);
		registryPanelNorth.add(this.rmiRegistry);

		gbcNorth.weightx = 1;
		gbcNorth.weighty = 1;
		gbcNorth.gridx = 0;
		gbcNorth.gridy = 1;
		gblNorth.setConstraints(this.remoteRegistry, gbcNorth);
		registryPanelNorth.add(this.remoteRegistry);

		this.registryPanel.add(registryPanelNorth, BorderLayout.NORTH);
	}
}
