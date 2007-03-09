package visidia.gui.presentation.userInterfaceSimulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import visidia.gui.DistributedAlgoSimulator;
import visidia.gui.donnees.GuiProperty;
import visidia.gui.donnees.TableImages;
import visidia.gui.donnees.conteneurs.Ensemble;
import visidia.gui.donnees.conteneurs.MultiEnsemble;
import visidia.gui.metier.inputOutput.OpenAgentChooser;
import visidia.gui.metier.inputOutput.OpenAgents;
import visidia.gui.metier.inputOutput.OpenGraph;
import visidia.gui.metier.inputOutput.OpenStats;
import visidia.gui.metier.inputOutput.SaveFile;
import visidia.gui.metier.simulation.AgentSimulEventHandler;
import visidia.gui.metier.simulation.Convertisseur;
import visidia.gui.presentation.FormeDessin;
import visidia.gui.presentation.SelectionDessin;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.VueGraphe;
import visidia.gui.presentation.boite.AbstractDefaultBox;
import visidia.gui.presentation.boite.AgentBoxChangingVertexState;
import visidia.gui.presentation.boite.AgentBoxProperty;
import visidia.gui.presentation.boite.AgentBoxSimulationSelection;
import visidia.gui.presentation.boite.BoiteChangementEtatArete;
import visidia.gui.presentation.boite.DefaultBoxVertex;
import visidia.gui.presentation.boite.PulseCounter;
import visidia.gui.presentation.boite.ThreadCountFrame;
import visidia.gui.presentation.starRule.StarRuleFrame;
import visidia.gui.presentation.userInterfaceEdition.Editeur;
import visidia.gui.presentation.userInterfaceEdition.Fenetre;
import visidia.gui.presentation.userInterfaceEdition.Traitements;
import visidia.rule.RSOptions;
import visidia.rule.RelabelingSystem;
import visidia.simulation.ObjectWriter;
import visidia.simulation.agents.AbstractExperiment;
import visidia.simulation.agents.Agent;
import visidia.simulation.agents.AgentSimulator;
import visidia.simulation.rules.AbstractRule;
import visidia.simulation.synchro.SynCT;
import visidia.tools.Bag;
import visidia.tools.agents.UpdateTable;
import visidia.tools.agents.UpdateTableStats;

/** Represents the algorithm simulation window for a graph 
 * 
 * Fenêtre principale affichée une fois le bouton "Agent Simulation" cliqué
 * Le graphe qui représente l'exécution s'affiche dans AgentsSimulationPanel
 * Le simulateur d'agent est continu par la fenêtre dans l'attribut sim de classe
 * AgentSimulator
 * 
 * */
public class AgentsSimulationWindow extends Fenetre implements Serializable,
		ActionListener, WindowListener, ChangeListener, ApplyStarRulesSystem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3485256720687047514L;

	protected static final String GENERAL_TITLE = "Agents Simulator";

	protected String algoTitle;

	// instance of simulator for stop/pause/start actions
	protected AgentSimulator sim;

	protected AgentSimulEventHandler seh;

	protected JToolBar toolBar;

	protected JButton but_start, but_pause, but_save, but_stop, but_help,
			but_experimentation, but_threadCount;

	protected JButton but_info, but_regles, but_reset;

	protected JButton but_default;

	protected JButton but_agents;

	protected PulseCounter global_clock;

	// save an execution
	protected ButtonGroup item_group;

	protected JRadioButtonMenuItem item_nothing, item_replay;

	protected ObjectWriter writer;

	protected JMenuItem item_chose, item_file;

	protected ThreadGroup tg = null;

	protected JMenuBar menuBar;

	protected JMenu file, rules, graph, algo;

	protected JMenuItem graph_open, algo_open, algo_placeAgent,
			algo_open_vertices, graph_save, graph_save_as, file_quit,
			file_close, file_help, graph_select_all, graph_disconnect,
			graph_reconnect;

	protected JMenuItem rules_open, rules_new;

	//protected JMenuItem new_simple_rules ;
	//protected JMenuItem new_star_rules ;
	// Menu pour les options au niveau de la visualisation
	protected JMenu visualizationOptions;

	// for the speed scale
	//     protected ChoiceMessage2 choiceMessage;
	protected JMenuItem synchro, others;

	protected JSlider speed_slider;

	protected JLabel speed_label;

	/** Panel where the VueGraphe is drawn*/
	protected AgentsSimulationPanel simulationPanel;

	protected File fichier_rules_edite;

	/* event pipe for events coming from the simulator */
	protected visidia.tools.VQueue evtPipeIn;

	/* event pipe for events coming from the Recorder */
	protected visidia.tools.VQueue evtPipeOut;

	/* ack pipe for acks coming from the graphic interfacs */
	protected visidia.tools.VQueue ackPipeIn;

	/* ack pipe for acks coming from the Recorder */
	protected visidia.tools.VQueue ackPipeOut;

	/*list of rewriting rules which could be either simple either stared */
	protected Vector rulesList;

	protected boolean[][] edgesStates = null;

	protected AbstractRule rsAlgo; // The algorithm witch will simulate the relabeling system

	protected static ThreadCountFrame threadCountFrame;

	public static boolean visuAlgorithmMess = true;

	public static boolean visuSynchrMess = true;

	public Editeur editeur;

	protected Hashtable agentsTable;

	protected Vector<RelabelingSystem> agentsRules = null;

	protected Hashtable<SommetDessin, AgentBoxChangingVertexState> boxVertices; // To store the

	// AgentBoxChangingVertex for
	// each SommetDessin (needed for automatic refresh)

	private Hashtable<String, Object> defaultProperties; // To initialize the whiteboards

	private UpdateTable timer;

	private Vector<AgentBoxProperty> boxAgents;

	public AgentsSimulationWindow(VueGraphe grapheVisu_edite,
			File fichier_edit, Editeur editeur) {

		this(grapheVisu_edite, COULEUR_FOND_PAR_DEFAUT, DIM_X_PAR_DEFAUT,
				DIM_Y_PAR_DEFAUT, fichier_edit);
		this.addWindowListener(this);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setSize(DIM_X_PAR_DEFAUT, DIM_Y_PAR_DEFAUT);
		this.pack();
		this.setVisible(true);
		this.editeur = editeur;
	}

	public AgentsSimulationWindow(VueGraphe grapheVisu_edite,
			Color couleur_fond, int dim_x, int dim_y, File fichier_edit) {

		super();

		this.evtPipeIn = new visidia.tools.VQueue();
		this.evtPipeOut = new visidia.tools.VQueue();
		this.ackPipeIn = new visidia.tools.VQueue();
		this.ackPipeOut = new visidia.tools.VQueue();

		this.writer = new ObjectWriter();

		this.tg = new ThreadGroup("recorder");

		// The edited graph and the selection object which contains selected objects
		this.vueGraphe = grapheVisu_edite;
		this.selection = new SelectionDessin();

		this.agentsTable = new Hashtable();

		this.boxVertices = new Hashtable<SommetDessin, AgentBoxChangingVertexState>();
		this.boxAgents = new Vector<AgentBoxProperty>();
		this.defaultProperties = new Hashtable<String, Object>();

		// The manager of components
		this.content = new JPanel();
		this.content.setLayout(new BorderLayout());
		this.fichier_edite = fichier_edit;
		this.mettreAJourTitreFenetre();
		this.rulesList = new Vector();

		// The menu bar
		this.addMenu();
		// Current datas of the edition

		// BackGround Color of the GrapheVisuPanel
		this.couleur_de_fond = couleur_fond;

		// The edited graph and the selection object which contains selected objects
		this.vueGraphe = grapheVisu_edite;

		this.selection = new SelectionDessin();

		// The panel where the graph is drawn
		this.simulationPanel = new AgentsSimulationPanel(this);
		super.setSize(DIM_X_PAR_DEFAUT, DIM_Y_PAR_DEFAUT);
		// un setSize est a faire avant l'ajout de composants pour eviter
		// les warnings
		this.scroller = new JScrollPane(this.simulationPanel);
		this.scroller.setPreferredSize(new Dimension(DIM_X_PAR_DEFAUT,
				DIM_Y_PAR_DEFAUT));
		this.simulationPanel.revalidate();

		this.simulationPanel.scrollRectToVisible(new Rectangle((this.vueGraphe
				.donnerDimension()).width - 10, (this.vueGraphe
				.donnerDimension()).height - 10, 30, 30));
		this.simulationPanel.repaint();

		this.scroller.setOpaque(true);
		this.content.add(this.scroller, BorderLayout.CENTER);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				AgentsSimulationWindow.this.setVisible(false);
				AgentsSimulationWindow.this.dispose();
				// Running the garbage collector
				Runtime.getRuntime().gc();

			}
		});

		// The tool bar
		this.addToolBar();

		// On disable les items non-valide pour une applet
		if (!DistributedAlgoSimulator.estStandalone())
			this.disableButtonForApplet();

		this.setContentPane(this.content);
	}

	/**
	 *
	 **/
	public void addAgents(Integer id, String agent) {

		boolean ok;
		SommetDessin vert = this.getVueGraphe().rechercherSommet(id.toString());
		int nbr;

		if (!this.agentsTable.containsKey(id)) {
			this.agentsTable.put(id, new ArrayList());
		}
		ok = ((ArrayList) this.agentsTable.get(id)).add(agent);

		vert.changerCouleurFond(Color.red);

		nbr = ((ArrayList) this.agentsTable.get(id)).size();
		String nbrStr = new String().valueOf(nbr);

		// bug ici (bilel) : ne marche pas avec les sommet cercle
		// ((SommetCarre)vert).setNbr(nbrStr); : version avant
		// correction 
		vert.setNbr(nbrStr);

		this.simulationPanel().repaint();

	}

	/**
	 * This method adds the Menu bar, its menus and items to the editor
	 **/

	protected void addMenu() {

		this.menuBar = new JMenuBar();
		this.menuBar.setOpaque(true);
		this.menuBar.setPreferredSize(new Dimension(650, 20));

		// Build the menu File
		this.file = new JMenu("File");
		this.file.getPopupMenu().setName("PopFile");
		this.file.setMnemonic('F');

		this.file_help = new JMenuItem("Help", KeyEvent.VK_H);
		this.file_help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				ActionEvent.CTRL_MASK));
		this.file_help.addActionListener(this);
		this.file.add(this.file_help);
		this.file.addSeparator();
		this.file_close = new JMenuItem("Close", KeyEvent.VK_C);
		this.file_close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.CTRL_MASK));
		this.file_close.addActionListener(this);
		this.file.add(this.file_close);
		this.file_quit = new JMenuItem("Quit", KeyEvent.VK_Q);
		this.file_quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.CTRL_MASK));
		this.file_quit.addActionListener(this);
		this.file.add(this.file_quit);
		this.file.addActionListener(this);
		this.menuBar.add(this.file);
		this.graph = new JMenu("Graph");
		this.graph.getPopupMenu().setName("PopGraph");
		this.graph.setMnemonic('G');

		this.graph_open = new JMenuItem("Open graph ", KeyEvent.VK_O);
		this.graph_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));
		this.graph_open.addActionListener(this);
		this.graph.add(this.graph_open);

		this.graph_save = new JMenuItem("Save Graph");
		this.graph_save.addActionListener(this);
		this.graph.add(this.graph_save);

		this.graph_save_as = new JMenuItem("Save graph as...");
		this.graph_save_as.addActionListener(this);
		this.graph.add(this.graph_save_as);
		this.graph.addActionListener(this);
		this.menuBar.add(this.graph);

		this.graph.addSeparator();

		// graphe connection and disconnection

		/*graph_disconnect = new JMenuItem("Disconnect");
		 graph_disconnect.addActionListener(this);
		 graph_disconnect.setToolTipText("Disconnect the selected elements");
		 graph.add(graph_disconnect);
		 graph_reconnect = new JMenuItem("Reconnect");
		 graph_reconnect.addActionListener(this);
		 graph_reconnect.setToolTipText("Reconnect the selected elements");
		 graph.add(graph_reconnect);*/

		this.graph_select_all = new JMenuItem("Select all");
		this.graph_select_all.addActionListener(this);
		this.graph_select_all
				.setToolTipText("Select all the elements of the graph");
		this.graph.add(this.graph_select_all);

		this.graph.addActionListener(this);
		this.menuBar.add(this.graph);

		this.algo = new JMenu("Agents"); //!!
		this.algo.getPopupMenu().setName("PopAlgo");
		this.algo.setMnemonic('A');

		this.algo_open = new JMenuItem("Add Agents...");
		this.algo_open.addActionListener(this);
		this.algo.add(this.algo_open);

		this.algo_placeAgent = new JMenuItem("Place Agents...");
		this.algo_placeAgent.addActionListener(this);
		this.algo.add(this.algo_placeAgent);

		this.algo.setEnabled(this.vueGraphe.getGraphe().ordre() > 0); // if we have an empty graph

		this.algo.addActionListener(this);
		this.menuBar.add(this.algo);

		this.rules = new JMenu("Rules");
		this.rules.getPopupMenu().setName("PopRules");
		this.rules.setMnemonic('R');

		this.rules_new = new JMenuItem("New relabeling system");
		this.rules_new.addActionListener(this);
		this.rules.add(this.rules_new);

		this.rules_open = new JMenuItem("Open rules...");
		this.rules_open.addActionListener(this);
		this.rules.add(this.rules_open);

		this.menuBar.add(this.rules);

		/*
		 visualizationOptions  = new VisualizationOptions(this);
		 menuBar.add(visualizationOptions);
		 
		 */

		this.setJMenuBar(this.menuBar);
	}

	/**
	 * This method adds the tool bar and its buttons to the editor
	 **/
	protected void addToolBar() {

		this.toolBar = new JToolBar();
		this.toolBar.setBackground(new Color(120, 120, 120));
		this.toolBar.setOpaque(true);
		this.toolBar.setPreferredSize(new Dimension(650, 42));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				AgentsSimulationWindow.this.commandeClose();
			}
		});

		//Build buttons on the tool bar
		this.but_start = new JButton("start");
		this.but_start.setToolTipText("Start");
		this.but_start.setAlignmentY(CENTER_ALIGNMENT);
		this.but_start.setEnabled(false);
		this.but_start.addActionListener(this);
		this.toolBar.add(this.but_start);

		this.but_pause = new JButton("pause");
		this.but_pause.setToolTipText("Pause");
		this.but_pause.setAlignmentY(CENTER_ALIGNMENT);
		this.but_pause.setEnabled(false);
		this.but_pause.addActionListener(this);
		this.toolBar.add(this.but_pause);

		this.but_stop = new JButton("stop");
		this.but_stop.setToolTipText("Stop");
		this.but_stop.setAlignmentY(CENTER_ALIGNMENT);
		this.but_stop.addActionListener(this);
		this.but_stop.setEnabled(false);
		this.toolBar.add(this.but_stop);

		this.toolBar.addSeparator();
		this.but_save = new JButton("save");
		this.but_save.setToolTipText("Save");
		this.but_save.setAlignmentY(CENTER_ALIGNMENT);
		this.but_save.addActionListener(this);
		this.toolBar.add(this.but_save);

		this.toolBar.addSeparator();

		// slider for speed modification
		this.speed_slider = new JSlider(1, 20, 10);
		this.speed_slider.addChangeListener(this);
		this.speed_slider.setToolTipText("Speed");
		this.speed_slider.setAlignmentY(TOP_ALIGNMENT);
		this.speed_slider.setAlignmentX(LEFT_ALIGNMENT);
		this.speed_slider.setPreferredSize(new Dimension(80, 15));
		this.speed_slider
				.setBackground(this.toolBar.getBackground().brighter());
		JPanel speed_panel = new JPanel();

		speed_panel.setMaximumSize(new Dimension(85, 40));
		speed_panel.setBackground(this.toolBar.getBackground());
		this.speed_label = new JLabel("Speed (" + this.simulationPanel.pas()
				+ ")");
		this.speed_label.setFont(new Font("Dialog", Font.BOLD, 10));
		this.speed_label.setToolTipText("Speed");
		this.speed_label.setAlignmentY(TOP_ALIGNMENT);
		this.speed_label.setForeground(Color.black);
		speed_panel.add(this.speed_slider);
		speed_panel.add(this.speed_label);

		this.toolBar.add(speed_panel);

		this.toolBar.addSeparator();

		this.but_info = new JButton(new ImageIcon(TableImages
				.getImage("vertexwb")));
		this.but_info.setToolTipText("Info");
		this.but_info.setAlignmentY(CENTER_ALIGNMENT);
		this.but_info.addActionListener(this);
		this.toolBar.add(this.but_info);

		this.but_default = new JButton(new ImageIcon(TableImages
				.getImage("vertexdefwb")));
		this.but_default.setToolTipText("Initialisation");
		this.but_default.setAlignmentY(CENTER_ALIGNMENT);
		this.but_default.addActionListener(this);
		this.toolBar.add(this.but_default);

		this.toolBar.addSeparator();

		this.but_agents = new JButton(new ImageIcon(TableImages
				.getImage("agentwb")));
		this.but_agents.setToolTipText("Agent whiteboard");
		this.but_agents.setAlignmentY(CENTER_ALIGNMENT);
		this.but_agents.setEnabled(false);
		this.but_agents.addActionListener(this);
		this.toolBar.add(this.but_agents);

		this.toolBar.addSeparator();

		this.but_help = new JButton(new ImageIcon(TableImages.getImage("help")));
		this.but_help.setToolTipText("Help");
		this.but_help.setAlignmentY(CENTER_ALIGNMENT);
		this.but_help.addActionListener(this);
		this.toolBar.add(this.but_help);

		this.toolBar.addSeparator();

		this.but_experimentation = new JButton("Statistics");
		this.but_experimentation.setToolTipText("Statistics");
		this.but_experimentation.setAlignmentY(CENTER_ALIGNMENT);
		this.but_experimentation.addActionListener(this);
		this.but_experimentation.setEnabled(false);
		this.toolBar.add(this.but_experimentation);
		this.toolBar.addSeparator();

		this.but_threadCount = new JButton("Threads");
		this.but_threadCount
				.setToolTipText("Amount of threads that are active in the VM");
		this.but_threadCount.setAlignmentY(CENTER_ALIGNMENT);
		this.but_threadCount.addActionListener(this);
		this.toolBar.add(this.but_threadCount);
		this.toolBar.addSeparator();
		if (threadCountFrame == null) {
			threadCountFrame = new ThreadCountFrame(Thread.currentThread()
					.getThreadGroup());
		}

		this.but_reset = new JButton("RESET");
		this.but_reset.setToolTipText("RESET");
		this.but_reset.setAlignmentY(CENTER_ALIGNMENT);
		this.but_reset.addActionListener(this);
		this.but_reset.setEnabled((this.fichier_edite != null));
		this.toolBar.add(this.but_reset);

		this.global_clock = new PulseCounter();

		this.content.add(this.toolBar, BorderLayout.NORTH);
		this.content.add(this.global_clock, BorderLayout.SOUTH);

	}

	// disable the button not used for the applet
	private void disableButtonForApplet() {
		this.file_quit.setEnabled(false);
		this.rules.setEnabled(false);
		this.graph.setEnabled(false);
		this.rules_new.setEnabled(false);
		this.but_save.setEnabled(false);
		this.but_experimentation.setEnabled(false);
	}

	/**********************************************************/
	/* Returns the panel "simulationPanel" which corresponds  */
	/*   to the graph visualisation during the simulation     */
	/**********************************************************/
	public AgentsSimulationPanel simulationPanel() {
		return this.simulationPanel;
	}

	/*********************************************************/
	/* Implementation of ActionListener interface            */
	/* treatment of what is done when pushing buttons or menu*/
	/*********************************************************/
	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() instanceof JButton)
			this.action_toolbar((JButton) evt.getSource());
		else if (evt.getSource() instanceof JMenuItem)
			this.action_menu((JMenuItem) evt.getSource());
	}

	/*********************************************************/
	/* Implementation of the ChangeListener interface        */
	/* action on the speed slider                            */
	/*********************************************************/
	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource() == this.speed_slider) {
			this.speed_label.setText("Speed (" + this.speed_slider.getValue()
					+ ")");
			this.simulationPanel.updatePas(this.speed_slider.getValue());
		}
	}

	/*********************************************************/
	/* Method for making action corresponding                */
	/* to the menu used .                                    */
	/*********************************************************/
	public void action_menu(JMenuItem mi) {
		String le_menu = ((JPopupMenu) mi.getParent()).getName();

		if (le_menu == "PopFile") {
			this.menuFile(mi);
		} else if (le_menu == "PopGraph")
			this.menuGraph(mi);
		else if (le_menu == "PopAlgo")
			this.menuAlgo(mi);
		else if (le_menu == "PopRules")
			this.menuRules(mi);
		/*else if(le_menu == "PopRules_new")
		 menuNew(mi);*/

	}

	/*********************************************************/
	/*  Method for making action corresponding               */
	/* to the button of the toolBar used                     */
	/*********************************************************/

	public void but_start() {
		// deselect all selected elements
		this.selection.deSelect();

		// enable drawing on vertices the number of agents
		GuiProperty.drawNbr = true;

		this.simulationPanel.start();
		// modifications for the recorder
		this.sim = null;
		// destruction of ths old threads
		while (this.tg.activeCount() > 0) {
			this.tg.interrupt();
			try {
				Thread.currentThread().sleep(50);
			} catch (InterruptedException e) {
			}
		}

		this.sim = new AgentSimulator(Convertisseur.convert(this.vueGraphe
				.getGraphe(), this.agentsTable, this.defaultProperties),
				this.agentsRules, this.evtPipeOut, this.ackPipeOut);

		this.seh = new AgentSimulEventHandler(this, this.evtPipeOut,
				this.ackPipeOut);
		this.seh.start();

		this.but_stop.setEnabled(true);
		this.but_pause.setEnabled(true);
		this.but_start.setEnabled(false);

		this.but_agents.setEnabled(true);
		this.but_experimentation.setEnabled(true);

		this.algo_open.setEnabled(false);
		this.algo_placeAgent.setEnabled(false);
		this.rules_open.setEnabled(false);
		this.rules_new.setEnabled(false);

		this.sim.startSimulation();

	}

	public void but_pause() {
		if (this.simulationPanel.isRunning()) {
			this.simulationPanel.pause();
			// bilel : ne sert à rien puisque l'interface graphique
			//doit acquitter pour que les threads continuent à tourner
			//dam sim.wedge();
		} else {
			this.simulationPanel.start();
			//dam 	    sim.unWedge();
		}
	}

	public void but_stop() {
		this.stopAll();

		this.evtPipeIn = new visidia.tools.VQueue();
		this.evtPipeOut = new visidia.tools.VQueue();
		this.ackPipeIn = new visidia.tools.VQueue();
		this.ackPipeOut = new visidia.tools.VQueue();

		this.but_start.setEnabled(false);
		this.but_pause.setEnabled(false);
		this.but_stop.setEnabled(false);
		this.but_reset.setEnabled(true);

		this.but_agents.setEnabled(false);

		this.global_clock.initState();
	}

	public void but_experimentation() {
		AgentExperimentationFrame statsFrame;
		Bag expStats;
		AbstractExperiment classStats = OpenStats.open(this);

		if (classStats == null)
			return;

		if (this.sim == null) {
			statsFrame = new AgentExperimentationFrame(this.vueGraphe,
					this.agentsTable, this.defaultProperties, this.agentsRules,
					classStats);
		} else {
			classStats.setStats(this.sim.getStats());
			expStats = classStats.getStats();

			statsFrame = new AgentExperimentationFrame(expStats);
			if (this.timer == null) {
				this.timer = new UpdateTableStats(this.sim, classStats,
						statsFrame.getTableModel());
				new Thread(this.timer).start();
			}
		}

		statsFrame.setTitle("Agents Experiments");
		statsFrame.pack();
		statsFrame.setVisible(true);

	}

	public void but_reset() {
		this.simulationPanel.stop();
		if (this.sim != null) {
			this.sim.abortSimulation();
			this.sim = null;
		}
		if (this.seh != null)
			this.seh.abort();

		/*
		 if (fichier_edite != null)
		 OpenGraph.open(this,fichier_edite);
		 */

		this.agentsTable.clear();

		this.vueGraphe = this.editeur.getGraphClone();

		this.evtPipeIn = new visidia.tools.VQueue();
		this.evtPipeOut = new visidia.tools.VQueue();
		this.ackPipeIn = new visidia.tools.VQueue();
		this.ackPipeOut = new visidia.tools.VQueue();
		this.replaceSelection(new SelectionDessin());
		this.simulationPanel.setPreferredSize(this.vueGraphe.donnerDimension());
		this.simulationPanel.revalidate();
		this.simulationPanel.scrollRectToVisible(new Rectangle(650, 600, 0, 0));
		this.simulationPanel.repaint();

		//algo.setEnabled(vueGraphe.getGraphe().ordre()>0); // if we have an empty graph

		this.but_start.setEnabled(false);
		this.but_pause.setEnabled(false);
		this.but_stop.setEnabled(false);
		this.but_reset.setEnabled(true);
		this.but_experimentation.setEnabled(false);

		/* enable the button to add agents */
		this.algo_open.setEnabled(true);
		this.algo_placeAgent.setEnabled(true);
		this.rules_open.setEnabled(true);
		this.rules_new.setEnabled(true);

		/* reinitialize the pulse counter */
		this.global_clock.initState();

	}

	public void action_toolbar(JButton b) {
		if (b == this.but_start) {
			this.but_start();
		} else if (b == this.but_pause) {
			this.but_pause();
		} else if (b == this.but_stop) {
			this.but_stop();
		} else if (b == this.but_experimentation) {
			this.but_experimentation();
		} else if (b == this.but_threadCount) {
			threadCountFrame.pack();
			threadCountFrame.setVisible(true);
		} else if (b == this.but_save) {
			SaveFile.save(this, this.vueGraphe.getGraphe());
		} else if (b == this.but_info) {
			this.propertiesControl();
		} else if (b == this.but_default) {
			this.but_initWhiteboard();
		} else if (b == this.but_agents) {
			this.but_agentsWhiteboard();
		}
		//PFA2003
		else if (b == this.but_help) {
			System.out.println("BUTTON HELP : NOT IMPLMENTED YET");
		} else if (b == this.but_reset) {
			this.but_reset();
		}
	}

	public void setPulse(int pulse) {
		this.global_clock.setPulse(pulse);
	}

	/*************************************************************/
	/* Method for the fonctionnalities of the "File" menu.       */
	/*************************************************************/
	public void menuFile(JMenuItem mi) {

		if (mi == this.file_help) {
			JOptionPane
					.showMessageDialog(
							this,
							"DistributedAlgoSimulator, v2\n"
									+ "in this window you can't modifie the graph \n"
									+ "except changing the state of edges or vertices\n"
									+ "before starting simulation you must load an algorithm \n "
									+ "or a list of simple rules \n");
		} else if (mi == this.file_close)
			this.commandeClose();
		else if (mi == this.file_quit)
			System.exit(0);
	}

	/*************************************************************/
	/* Method for the fonctionnalities of the "graph" menu.      */
	/*************************************************************/
	public void menuGraph(JMenuItem mi) {
		if (mi == this.graph_open) {
			OpenGraph.open(this);
			this.algo.setEnabled(this.vueGraphe.getGraphe().ordre() > 0); // if we have an empty graph
			//             algoChoice = new AlgoChoice(vueGraphe.getGraphe().ordre());
			this.replaceSelection(new SelectionDessin());
			this.simulationPanel.setPreferredSize(this.vueGraphe
					.donnerDimension());
			this.simulationPanel.revalidate();
			this.simulationPanel.scrollRectToVisible(new Rectangle(650, 600, 0,
					0));
			this.simulationPanel.repaint();
			if (this.item_replay.isSelected()) {
				this.but_start.setEnabled(true);
				this.but_experimentation.setEnabled(true);
			} else
				this.but_start.setEnabled(false);
			this.but_pause.setEnabled(false);
			this.but_stop.setEnabled(false);
		}

		else if (mi == this.graph_save) {

			SaveFile.save(this, this.vueGraphe.getGraphe());
		} else if (mi == this.graph_save_as) {
			this.fichier_edite = null;
			SaveFile.saveAs(this, this.vueGraphe.getGraphe());
		} else if (mi == this.graph_select_all) {
			//PFA2003
			Enumeration e = this.vueGraphe.listeAffichage();
			while (e.hasMoreElements()) {
				FormeDessin objetVisu = (FormeDessin) e.nextElement();
				this.selection.insererElement(objetVisu);
			}
			this.repaint();
		}
	}

	/*************************************************************/
	/* Method for the fonctionnalities of the "Algo" menu.       */
	/*************************************************************/
	public void menuAlgo(JMenuItem mi) {
		if (mi == this.algo_open) {
			boolean ok = true;

			if (this.selection.estVide()) {
				JOptionPane.showMessageDialog(this,
						"You must select at least one" + " vertex!", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (DistributedAlgoSimulator.estStandalone())
				ok = OpenAgents.open(this.selection.elements(), this);
			else
				// When using Visidia with an applet: not implemented
				// yet 
				// OpenAlgoApplet.open(this);
				;

			if (!this.but_start.isEnabled()) {
				this.but_start.setEnabled(ok);
				this.but_experimentation.setEnabled(ok);
			}

		}

		if (mi == this.algo_placeAgent) {
			boolean ok = true;

			ok = OpenAgentChooser.open(this);

			if (!this.but_start.isEnabled()) {
				this.but_start.setEnabled(ok);
				this.but_experimentation.setEnabled(ok);
			}
		}
	}

	/*************************************************************/
	/* Method for the fonctionnalities of the "rules" menu.      */
	/*************************************************************/
	public void menuRules(JMenuItem mi) {
		if (this.selection.estVide()) {
			JOptionPane.showMessageDialog(this, "You must select at least one"
					+ " vertex!", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (mi == this.rules_open) {
			final javax.swing.filechooser.FileFilter filter = new javax.swing.filechooser.FileFilter() {
				public boolean accept(File f) {
					String n = f.getName();
					return n.endsWith("srs");
				}

				public String getDescription() {
					return "srs (star rules system) files";
				}
			};

			JFileChooser chooser = new JFileChooser();
			chooser.setDialogType(JFileChooser.OPEN_DIALOG);
			chooser.setFileFilter(filter);
			chooser.setCurrentDirectory(new File("./"));
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					String fName = chooser.getSelectedFile().getPath();
					FileInputStream istream = new FileInputStream(fName);
					ObjectInputStream p = new ObjectInputStream(istream);
					RelabelingSystem rSys = (RelabelingSystem) p.readObject();
					istream.close();
					this.applyStarRulesSystem(rSys);
				} catch (IOException ioe) {
					System.out.println(ioe);
				} catch (ClassNotFoundException cnfe) {
					System.out.println(cnfe);
				}
			}
		} else if (mi == this.rules_new) {
			StarRuleFrame starRuleFrame = new StarRuleFrame(this, this);
			starRuleFrame.setVisible(true);
		}
	}

	public void applyStarRulesSystem(RelabelingSystem rSys) {
		this.rulesWarnings(rSys);

		if (this.agentsRules == null)
			this.agentsRules = new Vector();

		int size = this.agentsRules.size();

		this.agentsRules.add(rSys);

		Enumeration e = this.selection.elements();
		while (e.hasMoreElements()) {
			int id;
			id = Integer
					.decode(((SommetDessin) e.nextElement()).getEtiquette());
			this.addAgents(id, "Agents Rules_" + size);
		}

		this.but_start.setEnabled(true);
		this.but_experimentation.setEnabled(true);
	}

	private void stopAll() {

		if (this.sim != null) { // we kill the threads

			System.out.println("Stopping the timer");
			if (this.timer != null) {
				this.timer.stop();
				this.timer = null;
			}

			System.out.println("Stopping the Simulation panel");
			this.simulationPanel.stop();
			System.out.println("  ==> Stopped");
			System.out.println("Stopping the Simulator");
			if (this.sim != null)
				this.sim.abortSimulation();

			System.out.println("  ==> Stopped");

			System.out.println("Stopping the Simulator Event Handler");
			this.seh.abort();
			System.out.println(" ==> Stopped");

			this.seh.abort();
		}
	}

	/********************************/
	/** Closing the current window **/
	/********************************/
	public void commandeClose() {

		Iterator it = this.boxAgents.iterator();
		while (it.hasNext()) {
			AgentBoxProperty box = (AgentBoxProperty) it.next();
			box.close();
			it.remove();
		}

		this.stopAll();
		GuiProperty.drawNbr = false;
		this.setVisible(false);
		this.dispose();
		// collecting the garbage
		Runtime.getRuntime().gc();
	}

	/**
	 * Selection of all the graph
	 */
	public void commandeToutSelectionner() { // Penser au repaint()
		int i = 0;
		Enumeration e = this.vueGraphe.listeAffichage();
		if (e.hasMoreElements()) {
			while (e.hasMoreElements()) {
				FormeDessin forme = (FormeDessin) e.nextElement();
				this.selection.insererElement(forme);
				forme.enluminer(true);
				i++;
			}
		}
	}

	/**
	 * this method retrurns the string corresponding to the title of the
	 * window
	 **/
	/**********************************************************/
	/* this method permit to validate the reset button if a   */
	/* saving is made and then change the title of the window */
	/**********************************************************/
	public void mettreAJourTitreFenetre(File fichier) {
		if (fichier != null)
			this.but_reset.setEnabled(true);
		super.mettreAJourTitreFenetre(fichier);
	}

	public void mettreAJourTitreFenetre(String nom_Algo) {
		this.algoTitle = nom_Algo;
		super.mettreAJourTitreFenetre(nom_Algo);
	}

	protected String titre() {
		return "Algorithm Simulator";
	}

	public String getAlgoTitle() {
		return this.algoTitle;
	}

	public String type() {
		return "Simulator";
	}

	public File fichier_rules_edite() {
		return this.fichier_rules_edite;
	}

	// Implementation of the Listeners

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
		this.content.repaint();
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void commandeSupprimer() { // Penser au repaint()

		// Deleting the elements of the selection
		if (!this.selection.estVide()) {
			Enumeration e = this.selection.elements();
			while (e.hasMoreElements()) {
				FormeDessin forme = (FormeDessin) e.nextElement();
				forme.delete();
			}
		}

	}

	private void replaceSelection(SelectionDessin new_selection) {
		// Deletes the initial selection and replaces it with the new one
		this.emptyCurrentSelection(true);
		this.selection = new_selection;
		this.selection.select();
	}

	/**
	 * Method to empty the current selection
	 */
	public void emptyCurrentSelection(boolean deselect) { // Penser au repaint()
		if (!this.selection.estVide())
			if (deselect) {
				this.selection.deSelect();
			}
	}

	// action on the property button with a selection

	private void propertiesControl() {
		if (this.selection.estVide())
			System.out.println("empty");
		else {
			Enumeration e = this.selection.elements();
			FormeDessin firstElement = ((FormeDessin) e.nextElement());
			if (!Traitements.sommetDessin(this.selection.elements())
					.hasMoreElements()) {
				// we have only edges
				e = this.selection.elements();
				Ensemble listeElements = new Ensemble();
				listeElements.inserer(e);
				//                 BoiteChangementCouleurArete boiteArete =
				// 		    new BoiteChangementCouleurArete(this, listeElements);
				BoiteChangementEtatArete boiteArete = new BoiteChangementEtatArete(
						this, listeElements);

				boiteArete.show(this);
			} else if ((this.selection.nbElements() == 1)
					&& (firstElement.type().equals("vertex"))) {

				// if ( boxVertices.containsKey(firstElement) ) {
				//                     boxVertices.remove(firstElement);
				//                 }

				AgentBoxChangingVertexState agentBox = new AgentBoxChangingVertexState(
						this, (SommetDessin) firstElement,
						this.defaultProperties);
				this.boxVertices.put((SommetDessin) firstElement, agentBox);
				agentBox.show(this);
			} else {
				e = this.selection.elements();
				visidia.gui.donnees.conteneurs.MultiEnsemble table_des_types = new MultiEnsemble();
				while (e.hasMoreElements())
					table_des_types.inserer(((FormeDessin) e.nextElement())
							.type());
				AgentBoxSimulationSelection.show(this, this.selection,
						table_des_types);
			}
		}
	}

	private void but_initWhiteboard() {
		DefaultBoxVertex defBox = new DefaultBoxVertex(this,
				this.defaultProperties);
		defBox.show(this);
	}

	private void but_agentsWhiteboard() {

		Object[] agents = this.sim.getAllAgents().toArray();

		Agent ag = (Agent) JOptionPane.showInputDialog(this,
				"Select the agent:", "Agent's whiteboard editor",
				JOptionPane.PLAIN_MESSAGE, null, agents, null);

		if (ag != null) {

			AgentBoxProperty agentBox = new AgentBoxProperty(this, ag
					.getWhiteBoard(), ag.toString());
			this.boxAgents.addElement(agentBox);
			agentBox.show(this);
		}

	}

	public void removeWindow(AbstractDefaultBox box) {
		this.boxAgents.remove(box);
	}

	public void changerVueGraphe(VueGraphe grapheVisu) {
		this.content.remove(this.scroller);
		this.selection.deSelect();
		this.vueGraphe = grapheVisu;
		this.simulationPanel = new AgentsSimulationPanel(this);
		this.simulationPanel.updatePas(this.speed_slider.getValue());
		this.scroller = new JScrollPane(this.simulationPanel);
		this.scroller.setPreferredSize(new Dimension(650, 600));
		this.scroller.setOpaque(true);
		this.content.add(this.scroller, BorderLayout.CENTER);
	}

	public visidia.tools.VQueue getEvtPipe() {
		return this.evtPipeOut;
	}

	public visidia.tools.VQueue getAckPipe() {
		return this.ackPipeOut;
	}

	public void setEdgeState(int id1, int id2, boolean hasFailure) {
		this.edgesStates[id1][id2] = hasFailure;
	}

	public static void setVisuAlgorithmMess(boolean b) {
		visuAlgorithmMess = b;
	}

	public static void setVisuSynchrMess(boolean b) {
		visuSynchrMess = b;
	}

	public void nodeStateChanged(int nodeId, Hashtable properties) {
		//System.out.println("aaaa= "+nodeId+" gggg = "+ properties);
		if (this.sim != null)
			//dam 	    sim.setNodeProperties(nodeId, properties);
			;
		//sim.restartNode(nodeId);
	}

	public void rulesWarnings(RelabelingSystem r) {
		int synType;//user choice
		int type;//default choice
		//SynObject synob;
		RSOptions options = r.getOptions();
		if (options.defaultSynchronisation() != -1) {
			type = r.defaultSynchronisation();
			/* user choice */
			synType = options.defaultSynchronisation();
			if ((synType == SynCT.RDV) && (type == SynCT.LC1)) {
				JOptionPane.showMessageDialog(this,
						"The rendez-vous synchronisation cannot be used\n"
								+ "because of context or arity", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
			if ((synType == SynCT.LC1) && (type == SynCT.RDV)) {
				JOptionPane.showMessageDialog(this,
						"The use of use of LC1 is not recommended :\n"
								+ "check if you modify the neighbors.",
						"Error", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			/* default option */
			synType = r.defaultSynchronisation();
		}
	}

	public void updateVertexState(SommetDessin vert) {
		AgentBoxChangingVertexState box = this.boxVertices.get(vert);

		if (box != null) // An AgentBoxChangingVertexState is
			box.updateBox(); // open for this vertex

	}

	public Hashtable getDefaultProperties() {
		return this.defaultProperties;
	}
	
/* Solution non fonctionnelle
 * 
	public void updateSimulationGraphe() {
		//visidia.gui.metier.simulation.Convertisseur c = new visidia.gui.metier.simulation.Convertisseur();
		if (this.sim != null && this.editeur != null) {
			SimpleGraph s = this.sim.getGraph();
			//this.sim.setGraph(Convertisseur.convertir(this.editeur.graph(),s.getDefaultVertexValues()));
			this.sim.setGraph(Convertisseur.convertir(this.editeur.graph(),
					new Hashtable()));
		}
	}*/

}
