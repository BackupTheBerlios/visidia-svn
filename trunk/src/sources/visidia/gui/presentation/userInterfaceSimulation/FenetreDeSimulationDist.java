package visidia.gui.presentation.userInterfaceSimulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import visidia.algo.SimpleRule;
import visidia.gui.DistributedAlgoSimulator;
import visidia.gui.donnees.TableImages;
import visidia.gui.donnees.conteneurs.Ensemble;
import visidia.gui.donnees.conteneurs.MultiEnsemble;
import visidia.gui.metier.Sommet;
import visidia.gui.metier.inputOutput.OpenAlgoAppletDistribue;
import visidia.gui.metier.inputOutput.OpenAlgoDistribue;
import visidia.gui.metier.inputOutput.OpenConfig;
import visidia.gui.metier.inputOutput.OpenGraph;
import visidia.gui.metier.inputOutput.OpenHelpDist;
import visidia.gui.metier.inputOutput.SaveFile;
import visidia.gui.metier.simulation.Convertisseur;
import visidia.gui.metier.simulation.SimulEventHandler;
import visidia.gui.presentation.FormeDessin;
import visidia.gui.presentation.SelectionDessin;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.VueGraphe;
import visidia.gui.presentation.boite.BoiteChangementEtatArete;
import visidia.gui.presentation.boite.BoiteChangementEtatSommetDist;
import visidia.gui.presentation.boite.BoiteDistribue;
import visidia.gui.presentation.boite.BoiteExperimentComplet;
import visidia.gui.presentation.boite.BoiteRegistry;
import visidia.gui.presentation.boite.BoiteSelection;
import visidia.gui.presentation.boite.RemoteObjectBoite;
import visidia.gui.presentation.boite.ThreadCountFrame;
import visidia.gui.presentation.userInterfaceEdition.Fenetre;
import visidia.gui.presentation.userInterfaceEdition.Traitements;
import visidia.misc.MessageType;
import visidia.network.Simulator_Rmi;
import visidia.network.Simulator_Rmi_Int;
import visidia.network.VisidiaRegistry;
import visidia.network.VisidiaRegistryImpl;
import visidia.simulation.AlgorithmDist;
import visidia.tools.LocalNodeTable;
import visidia.tools.VQueue;

/* Represents the algorithm simulation window for a graph */
public class FenetreDeSimulationDist extends Fenetre implements Serializable, ActionListener,ItemListener, WindowListener, ChangeListener {
    
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8912821508346948285L;
	protected LocalNodeTable networkParam;
    protected String simulatorHost;
    protected String simulatorUrl;
    protected String rmiRegistryPort = "1099";
    protected Simulator_Rmi_Int sim_Rmi;
    protected AlgorithmDist algoRmi;
    protected Registry registry;
    protected VisidiaRegistry vr;
    
    // instance of simulator for stop/pause/start actions
    protected SimulEventHandler seh;
    protected JToolBar toolBar;
    protected JButton but_start, but_pause, but_save, but_stop, but_help, but_experimentation, but_threadCount, but_information_distribue;
    protected JButton but_info , but_regles , but_reset;

    // save an execution
    protected ButtonGroup item_group_Config ;
    //protected JRadioButtonMenuItem item_oneServer, item_groupServer;
    protected JCheckBoxMenuItem item_visualization;

    protected ThreadGroup tg=null;

    protected JMenuBar menuBar;
    protected JMenu file, graph, algo, config,experiment;
    protected JMenuItem graph_open ,algo_open, algo_open_vertices, graph_save , graph_save_as ,  file_quit , file_close, file_help , config_reseaux, config_reseaux_file, config_registration, config_help ,config_registry,experiment_complet,experiment_begin;
    protected JMenuItem rules_open , rules_save , rules_save_as ;

    protected MessageChoiceDist messageChoiceDist;
    
    // for the speed scale
    protected JSlider speed_slider;
    protected JLabel speed_label;
   
     /** Panel where the VueGraphe is drawn*/
    protected SimulationPanel simulationPanel;
    protected File fichier_rules_edite;

    /* event pipe for events coming from the simulator */
    protected visidia.tools.VQueue evtPipeIn ;
    /* event pipe for events coming from the Recorder */
    protected visidia.tools.VQueue evtPipeOut ;
    /* ack pipe for acks coming from the graphic interfacs */
    protected visidia.tools.VQueue ackPipeIn ;
    /* ack pipe for acks coming from the Recorder */
    protected visidia.tools.VQueue ackPipeOut ;
 
    /*list of rewriting rules which could be either simple either stared */

    //protected AlgoChoice algoChoice;
    protected boolean simulationAlgo = false;
    protected static ThreadCountFrame threadCountFrame;

    protected Integer experimentSize = new Integer(0);


    public FenetreDeSimulationDist(VueGraphe grapheVisu_edite, File fichier_edit) {
  	
	this(grapheVisu_edite, COULEUR_FOND_PAR_DEFAUT, DIM_X_PAR_DEFAUT,
	     DIM_Y_PAR_DEFAUT, fichier_edit);
	this.addWindowListener(this);
	this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.setSize(650, 600);
	this.pack(); 
	this.setVisible(true);

    }
    
    
    public FenetreDeSimulationDist(VueGraphe grapheVisu_edite , Color couleur_fond, int dim_x,
			       int dim_y, File fichier_edit) {
	
	super();
	this.evtPipeIn = new visidia.tools.VQueue();
	this.evtPipeOut = new visidia.tools.VQueue();
	this.ackPipeIn = new visidia.tools.VQueue();
	this.ackPipeOut = new visidia.tools.VQueue();
	
	this.tg = new ThreadGroup("recorder");

	// The edited graph and the selection object which contains selected objects
	this.vueGraphe = grapheVisu_edite;
	this.selection = new SelectionDessin();

	//algoChoice = new AlgoChoice(grapheVisu_edite.getGraphe().ordre());

	// The manager of components
	this.content = new JPanel();
	this.content.setLayout(new BorderLayout());
	this.fichier_edite = fichier_edit;
	this.mettreAJourTitreFenetre();
		
	// The menu bar
	this.addMenu();
	// Current datas of the edition
       
	// BackGround Color of the GrapheVisuPanel
	this.couleur_de_fond = couleur_fond;
	
	// The edited graph and the selection object which contains selected objects
	//vueGraphe = grapheVisu_edite;
	//selection = new SelectionDessin();
	//algoChoice = new AlgoChoice(grapheVisu_edite.getGraphe().ordre());

	// The panel where the graph is drawn
	this.simulationPanel = new SimulationPanel(this);
	super.setSize(650,600);
	// un setSize est a faire avant l'ajout de composants pour eviter
	// les warnings
	this.scroller = new JScrollPane(this.simulationPanel);
	//scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	//scroller.setVerticalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	this.scroller.setPreferredSize(new Dimension(650,600));
	this.simulationPanel.revalidate();
	
	this.simulationPanel.scrollRectToVisible(new Rectangle((this.vueGraphe.donnerDimension()).width-10,(this.vueGraphe.donnerDimension()).height-10,30,30));
	this.simulationPanel.repaint();
	
	this.scroller.setOpaque(true);
	this.content.add(this.scroller, BorderLayout.CENTER);
	
						   

	this.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    if (FenetreDeSimulationDist.this.simulationPanel != null)
			FenetreDeSimulationDist.this.simulationPanel.stop();
		    if (FenetreDeSimulationDist.this.seh != null)
			FenetreDeSimulationDist.this.seh.abort();
		    FenetreDeSimulationDist.this.commandeClose();
		    FenetreDeSimulationDist.this.raz();
		    FenetreDeSimulationDist.this.setVisible(false);
		    FenetreDeSimulationDist.this.dispose();
		    // Running the garbage collector
		    Runtime.getRuntime().gc();
		    
		}
	    });
	
	// The tool bar
	this.addToolBar();
	
	// On disable les items non-valide pour une applet
	if(!DistributedAlgoSimulator.estStandalone()) 
	    this.disableButtonForApplet();
	

	this.setContentPane(this.content);
	boolean bool = true;
	try {
	    this.registry = LocateRegistry.createRegistry((new Integer(this.rmiRegistryPort)).intValue());
	} catch (RemoteException re) {
	    try {
		this.registry = LocateRegistry.getRegistry((new Integer(this.rmiRegistryPort)).intValue());
	    } catch (Exception e) {
		bool=false;
		JOptionPane.showMessageDialog(this, "Cannot initialize RMI Registry : \n"+e.toString(), "Warning",JOptionPane.WARNING_MESSAGE);
		//e.printStackTrace();
	    }
	}
	try {
	    if(bool){
		this.vr = new VisidiaRegistryImpl(this);
		this.vr.init("Registry",this.registry);
	    } 		
	} catch (Exception e) {
	    JOptionPane.showMessageDialog(this, "The Visidia Registration is not running"+e,"Error",JOptionPane.WARNING_MESSAGE);
	}
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
	this.file_help.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_H, ActionEvent.CTRL_MASK));
	this.file_help.addActionListener(this);
	this.file.add(this.file_help);
	this.file.addSeparator();    
	this.file_close = new JMenuItem("Close", KeyEvent.VK_C);
	this.file_close.setAccelerator(KeyStroke.getKeyStroke(
							 KeyEvent.VK_C, ActionEvent.CTRL_MASK));
	this.file_close.addActionListener(this);
	this.file.add(this.file_close);
	this.file_quit = new JMenuItem("Quit", KeyEvent.VK_Q);
	this.file_quit.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
	this.file_quit.addActionListener(this);
	this.file.add(this.file_quit);
	this.file.addActionListener(this);
	this.menuBar.add(this.file);


	this.graph = new JMenu("Graph");
	this.graph.getPopupMenu().setName("PopGraph");
	this.graph.setMnemonic('G');
    
	this.graph_open = new JMenuItem("Open graph ", KeyEvent.VK_O);
	this.graph_open.setAccelerator(KeyStroke.getKeyStroke(
							 KeyEvent.VK_O, ActionEvent.CTRL_MASK));
	this.graph_open.addActionListener(this);
	this.graph.add(this.graph_open);
    
	this.graph_save = new JMenuItem("Save Graph");
	this.graph_save.addActionListener(this);
	this.graph.add(this.graph_save);
    
	this.graph_save_as = new JMenuItem("Save graph as ...");
	this.graph_save_as.addActionListener(this);
	this.graph.add(this.graph_save_as);
	this.graph.addActionListener(this);
	this.menuBar.add(this.graph);
    
	this.algo = new JMenu("Algorithm");
	this.algo.getPopupMenu().setName("PopAlgo");
	this.algo.setMnemonic('A');
    
	this.algo_open = new JMenuItem("Open algorithm");
	this.algo_open.addActionListener(this);
	this.algo.add(this.algo_open);
	
	this.algo.addSeparator();

	this.algo_open_vertices = new JMenuItem("Put algorithm to vertices");
	this.algo_open_vertices.addActionListener(this);
	this.algo.add(this.algo_open_vertices);
	this.algo.setEnabled(this.vueGraphe.getGraphe().ordre()>0); // if we have an empty graph
    
	this.algo.addActionListener(this);
	this.menuBar.add(this.algo);
   
	this.config = new JMenu("Config");
	this.config.getPopupMenu().setName("PopConfig");
	this.config.setMnemonic('C');
	
	this.item_visualization = new JCheckBoxMenuItem("Visualize Messages",true);
	this.item_visualization.addItemListener(this);
	this.config.add(this.item_visualization);

	this.config.addSeparator();	   
	

	this.config_help = new JMenuItem("Help");
	this.config_help.addActionListener(this);
	this.config.add(this.config_help);
	
	this.config.addSeparator();
	
	this.config_registry = new JMenuItem("configure the registry");
	this.config_registry.addActionListener(this);
	this.config.add(this.config_registry);
	
	this.config.addSeparator();

	this.config_reseaux = new JMenuItem("Set Hosts for the Nodes");
	this.config_reseaux.addActionListener(this);
	this.config.add(this.config_reseaux);
	
	this.config_reseaux_file = new JMenuItem("Set Hosts from a file");
	this.config_reseaux_file.addActionListener(this);
	this.config.add(this.config_reseaux_file);

	this.config_registration = new JMenuItem("Get Local Nodes");
	this.config_registration.addActionListener(this);
	this.config.add(this.config_registration);
    	this.config.addActionListener(this);
	this.menuBar.add(this.config);
	
	
	this.experiment = new JMenu("Expermient");
	this.experiment.getPopupMenu().setName("PopExperiment");
	this.experiment.setMnemonic('E');
	this.experiment.setVisible(false);

	this.experiment_complet = new JMenuItem("Complete Graph", KeyEvent.VK_O);
	this.experiment_complet.addActionListener(this);
	this.experiment.add(this.experiment_complet);
	this.config.addSeparator();
	
	this.experiment_begin  = new JMenuItem("Begin Experiment", KeyEvent.VK_O);
	this.experiment_begin.addActionListener(this);
	this.experiment.add(this.experiment_begin);

	
	this.experiment.addActionListener(this);
	this.menuBar.add(this.experiment);

	this.messageChoiceDist = new MessageChoiceDist(this);
	this.messageChoiceDist.setMnemonic('M');
        this.menuBar.add(this.messageChoiceDist);
	
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
		    FenetreDeSimulationDist.this.commandeClose();
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
	this.speed_slider.setPreferredSize(new Dimension(80,15));
	this.speed_slider.setBackground(this.toolBar.getBackground().brighter());
	JPanel speed_panel = new JPanel();
	    
	speed_panel.setMaximumSize(new Dimension(90,40));
	speed_panel.setBackground(this.toolBar.getBackground());
	this.speed_label = new JLabel("Speed ("+this.simulationPanel.pas()+")");
	this.speed_label.setFont(new Font("Dialog",Font.BOLD,10));
	this.speed_label.setToolTipText("Speed");
	this.speed_label.setAlignmentY(TOP_ALIGNMENT);
	this.speed_label.setForeground(Color.black);
	speed_panel.add(this.speed_slider);
	speed_panel.add(this.speed_label);
	    
	this.toolBar.add(speed_panel);

	this.but_info = new JButton(new ImageIcon(TableImages.getImage("info")));//"fr/enserb/das/gui/donnees/images/info.gif"));
	this.but_info.setToolTipText("Info");
	this.but_info.setAlignmentY(CENTER_ALIGNMENT);
	this.but_info.addActionListener(this);
	this.toolBar.add(this.but_info);
	    
	this.toolBar.addSeparator();
	    
	this.but_help = new JButton(new ImageIcon(TableImages.getImage("help")));
	this.but_help.setToolTipText("Help");
	this.but_help.setAlignmentY(CENTER_ALIGNMENT);
	this.but_help.addActionListener(this);
	this.toolBar.add(this.but_help);

	this.toolBar.addSeparator();
	
	this.but_information_distribue = new JButton("Remote Node");
	this.but_information_distribue.setToolTipText("Remote Node");
	this.but_information_distribue.setAlignmentY(CENTER_ALIGNMENT);
	this.but_information_distribue.addActionListener(this);
	this.toolBar.add(this.but_information_distribue);
	
	this.toolBar.addSeparator();
	
	this.but_experimentation = new JButton("Statistics");
	this.but_experimentation.setToolTipText("Statistics");
	this.but_experimentation.setAlignmentY(CENTER_ALIGNMENT);
	this.but_experimentation.addActionListener(this);
	this.toolBar.add(this.but_experimentation);
	
	this.toolBar.addSeparator();
	
	this.but_threadCount = new JButton("threads");
	this.but_threadCount.setToolTipText("amount of threads that are active in the VM");
	this.but_threadCount.setAlignmentY(CENTER_ALIGNMENT);
	this.but_threadCount.addActionListener(this);
	this.toolBar.add(this.but_threadCount);
	this.toolBar.addSeparator();
	if(threadCountFrame == null){
	    threadCountFrame = new ThreadCountFrame(Thread.currentThread().getThreadGroup());
	}
	
	this.but_reset = new JButton("RESET");
	this.but_reset.setToolTipText("RESET");
	this.but_reset.setAlignmentY(CENTER_ALIGNMENT);
	this.but_reset.addActionListener(this);
	this.but_reset.setEnabled((this.fichier_edite != null));
	this.toolBar.add(this.but_reset); 

	this.content.add(this.toolBar, BorderLayout.NORTH);
    }
    

     // disable the button not used for the applet
    private void disableButtonForApplet(){
	this.file_quit.setEnabled(false);
	this.graph.setEnabled(false);
	//config.setEnabled(false);
	this.but_save.setEnabled(false);
	this.but_experimentation.setEnabled(false);
    }


    /**********************************************************/
    /* this method permit to validate the reset button if a   */
    /* saving is made and then change the title of the window */
    /**********************************************************/
    public void mettreAJourTitreFenetre(File fichier) {
	if(fichier != null) this.but_reset.setEnabled(true);
	super.mettreAJourTitreFenetre(fichier);
    }



    /**********************************************************/
    /* Returns the panel "simulationPanel" which corresponds  */ 
    /*   to the graph visualisation during the simulation     */
    /**********************************************************/
    public SimulationPanel simulationPanel() {
	return this.simulationPanel;
    }
    
    /*********************************************************/
    /* Implementation of ActionListener interface            */
    /* treatment of what is done when pushing buttons or menu*/
    /*********************************************************/
    public void actionPerformed(ActionEvent evt) {
	
	if(evt.getSource() instanceof JButton)
	    this.action_toolbar((JButton)evt.getSource());
	else if(evt.getSource() instanceof JMenuItem)
	    this.action_menu((JMenuItem)evt.getSource());  
    }
    
    /*********************************************************/
    /* Implementation of the ChangeListener interface        */
    /* action on the speed slider                            */
    /*********************************************************/
    public void stateChanged(ChangeEvent evt) {
	if (evt.getSource() == this.speed_slider) {
	    this.speed_label.setText("Speed ("+this.speed_slider.getValue()+")");
	    this.simulationPanel.updatePas(this.speed_slider.getValue());
	}
    }

    /*********************************************************/
    /* Method for making action corresponding                */
    /* to the menu used .                                    */
    /*********************************************************/
    public void action_menu(JMenuItem mi) {
	String le_menu = ((JPopupMenu)mi.getParent()).getName();
	
	if(le_menu == "PopFile") {
	    this.menuFile(mi);}
	else if(le_menu == "PopGraph")
	    this.menuGraph(mi);
	else if(le_menu == "PopAlgo")
	    this.menuAlgo(mi);
	else if(le_menu == "PopConfig")
	    this.menuConfig(mi);
	else if (le_menu == "PopExperiment")
	    this.menuExperiment(mi);
	//	else if(le_menu == "PopTypeMessage")
	//  menuTypeMessage(mi);
    }
    
    /*********************************************************/
    /*  Method for making action corresponding               */
    /* to the button of the toolBar used                     */
    /*********************************************************/
    public void action_toolbar(JButton b) {
	if (b == this.but_start) {
	    this.simulationPanel.start();	 
	    while(this.tg.activeCount() > 0){
		this.tg.interrupt();
		try{
		    Thread.currentThread().sleep(50);
		}
		catch(InterruptedException e){
		}
	    }
	    if (this.networkParam == null){
		System.out.println("you must configure the net for each node");
		JOptionPane.showMessageDialog(this, "you must configure the net for each \n"+" Node before runnig the simulation \n"+"           (Config menu) ",
						  "Error",
					      JOptionPane.WARNING_MESSAGE);
		return; 
	    }
	    
	    if (DistributedAlgoSimulator.estStandalone()) {
		try{
		    this.sim_Rmi = new Simulator_Rmi(Convertisseur.convertir(this.vueGraphe.getGraphe()),this.evtPipeOut,this.ackPipeOut,this.simulatorHost,this.simulatorUrl,this.rmiRegistryPort);
		    Naming.bind("rmi://:"+this.rmiRegistryPort+"/"+this.simulatorUrl,this.sim_Rmi);
		} catch (Exception e) {
		    System.out.println("An error has aquired when creating and binding the\n"+"Simulator to the localhost : <"+this.simulatorHost+","+this.rmiRegistryPort+","+this.simulatorUrl+">\n"+e);
		    JOptionPane.showMessageDialog(this, "An error has aquired when creating and binding the\n"+"Simulator to the localhost : <"+this.simulatorHost+","+this.rmiRegistryPort+","+this.simulatorUrl+">\n"+e,
						  "Error",
						  JOptionPane.WARNING_MESSAGE);
		    return;
		}
	    }
	    
	    this.seh =  new SimulEventHandler(this,this.evtPipeOut,this.ackPipeOut);
	    this.seh.start();
	    
	    try {
		this.sim_Rmi.initializeNodes(this.networkParam);
		this.sim_Rmi.startServer(this.algoRmi);
	    } catch (Exception e) {
		System.out.println("An Error has aquired when starting, initializing and runnig the nodes \n"+e);
		JOptionPane.showMessageDialog(this, "An Error has aquired when starting, initializing and runnig the nodes \n"+e,
					      "Error",
						  JOptionPane.WARNING_MESSAGE);
		return; 
	    }
	    
	    
	    this.but_stop.setEnabled(true);
	    this.but_pause.setEnabled(true);
	    this.but_start.setEnabled(false);
	}
	else if (b == this.but_pause) {
	    if(this.simulationPanel.isRunning()){
		this.simulationPanel.pause();   
		try {
		    this.sim_Rmi.wedge();
		}catch (Exception e) {}
	    }
	    else {
		this.simulationPanel.start();
		try {
		    this.sim_Rmi.unWedge();
		} catch (Exception e) {}
	    }
	}
	else if (b == this.but_stop) {
	    this.simulationPanel.stop();   

	    /* a revoir 
	    try {
		sim_Rmi.abortSimulation();
		Naming.unbind("rmi://:"+rmiRegistryPort+"/"+simulatorUrl);
	    } catch (Exception e) { 
		System.out.println("Erreur dans le unbind : "+e);
	    }
	    */

	    this.seh.abort();

	    this.raz();
	    
	    this.but_start.setEnabled(false);
	    this.but_pause.setEnabled(false);
	    this.but_stop.setEnabled(false);
	}
	else if (b == this.but_experimentation){
	    try{
		System.out.println("the number of messages exchanged at this stage is "+this.sim_Rmi.getMessageNumber());
		//JOptionPane.showMessageDialog(this, "the number of messages exchanged at this stage is "+sim_Rmi.getMessageNumber());
	    } catch (Exception e) {
		System.err.println("Erreur dans count message"+e);
	    }
	}
	else if (b == this.but_threadCount){
	    JOptionPane.showMessageDialog(this, "not implemented");
        }
	
	else if (b == this.but_save) {
	    SaveFile.save(this, this.vueGraphe.getGraphe());
	}
	
	else if (b == this.but_info){
	    this.propertiesControl();
	}
	else if (b == this.but_reset) {
	    this.simulationPanel.stop();
	    try{
		this.sim_Rmi.abortSimulation();
		Naming.unbind("rmi://:"+this.rmiRegistryPort+"/"+this.simulatorUrl);
		Naming.unbind("rmi://:"+this.rmiRegistryPort+"/Registry");
		this.vr = null;
		this.registry = null;
	    } catch (Exception e) { 
		System.out.println("Erreur dans le unbind"+e);
	    }
	    
	    this.seh.abort();
	  	  
	    if (this.fichier_edite != null)
		OpenGraph.open(this,this.fichier_edite);
		//OpenGraph.open(this);
	    this.evtPipeIn = new visidia.tools.VQueue();
	    this.evtPipeOut = new visidia.tools.VQueue();
	    this.ackPipeIn = new visidia.tools.VQueue();
	    this.ackPipeOut = new visidia.tools.VQueue();
	    this.replaceSelection(new SelectionDessin());
	    
	    this.simulationPanel.setPreferredSize(this.vueGraphe.donnerDimension());
	    this.simulationPanel.revalidate();
	    this.simulationPanel.scrollRectToVisible(new Rectangle(650,600,0,0));
	    this.simulationPanel.repaint();
	    
	    this.but_start.setEnabled(true);
	    this.but_pause.setEnabled(false);
	    this.but_stop.setEnabled(false);
	    
	    this.algo.setEnabled(this.vueGraphe.getGraphe().ordre()>0); // if we have an empty graph
	}
	else if (b == this.but_information_distribue) {
	    try {
		Hashtable table = this.sim_Rmi.getGraphStub();
		//RemoteObjectBoite rob = new RemoteObjectBoite(this,"Location of Nodes",table,configHosts);
		RemoteObjectBoite rob = new RemoteObjectBoite(this,"Location of Nodes",table);
		rob.show(this);
	    } catch (Exception e) {
	    }
	}
    }
    
    /*************************************************************/
    /* Method for the fonctionnalities of the "File" menu.       */
    /*************************************************************/
    public void menuFile(JMenuItem mi) {
	
	  
        if(mi == this.file_help) {
	    JOptionPane.showMessageDialog(this,
					  "DistributedAlgoSimulator, v2\n" +
					  "in this window you can't modifie the graph \n"+
					  "except changing the state of edges or vertices\n"+
					  "before starting simulation you must load an algorithm \n "+
					  "or a list of simple rules \n");
	}
	else if(mi == this.file_close)
	    this.commandeClose();
	else if(mi == this.file_quit)
	    System.exit(0);
    }
    
    /*************************************************************/
    /* Methods for "graph" menu.      */
    /*************************************************************/
    public void menuGraph(JMenuItem mi) {
	if(mi == this.graph_open){
	    OpenGraph.open(this);
	    this.algo.setEnabled(this.vueGraphe.getGraphe().ordre()>0); // if we have an empty graph
	    //algoChoice = new AlgoChoice(vueGraphe.getGraphe().ordre());
	    this.replaceSelection(new SelectionDessin());
	    this.simulationPanel.setPreferredSize(this.vueGraphe.donnerDimension());
	    this.simulationPanel.revalidate();
	    this.simulationPanel.scrollRectToVisible(new Rectangle(650,600,0,0));
	    this.simulationPanel.repaint();
	    this.but_start.setEnabled(false);
	    this.but_pause.setEnabled(false);
	    this.but_stop.setEnabled(false);
	}
	
	else if(mi == this.graph_save) {
	    
	    SaveFile.save(this, this.vueGraphe.getGraphe());
	}
	else if(mi == this.graph_save_as) {
	    this.fichier_edite = null;
	    SaveFile.saveAs(this, this.vueGraphe.getGraphe());
	}
    }
    
    /*************************************************************/
    /* Method for the fonctionnalities of the "Algo" menu.       */
    /*************************************************************/
    public void menuAlgo(JMenuItem mi) {
        if(mi == this.algo_open){
	    if(DistributedAlgoSimulator.estStandalone())
		OpenAlgoDistribue.open(this);
	    else
		OpenAlgoAppletDistribue.open(this);
	    this.simulationAlgo = true ;
	    this.but_start.setEnabled(true);
	}
    }
    /*************************************************************/
    /* Method for the fonctionnalities of the "rules" menu.      */
    /*************************************************************/


    /*************************************************************/
    /* Method for the fonctionnalities of the "trace" menu.      */
    /*************************************************************/
    
    public void menuConfig(JMenuItem mi){
	if (mi == this.config_help) {
	    OpenHelpDist openHelpDist = new OpenHelpDist("Help on the configuration");
	    openHelpDist.show();
	}
	else if (mi == this.config_registry) {
	    if(DistributedAlgoSimulator.estStandalone()){
		BoiteRegistry boiteRegistry = 
		    new BoiteRegistry(this,"Registry configuration");
		boiteRegistry.show(this);
	    }
	}
	else if(mi == this.config_reseaux){
	    BoiteDistribue boiteDistribue = 
		new BoiteDistribue(this, this.vueGraphe.cloner(), "configuration du reseaux");
	    boiteDistribue.show(this);
	}

	else if(mi == this.config_reseaux_file) {
	    if(DistributedAlgoSimulator.estStandalone()){
		OpenConfig oc = new OpenConfig();
		oc.open(this,this.vueGraphe.getGraphe().ordre());
	    }
	}
	else if (mi == this.config_registration) {
	    if(DistributedAlgoSimulator.estStandalone()){
		try {
		    if (!this.rmiRegistryPort.equals("1099")){
			this.registry.unbind("rmi://:"+this.rmiRegistryPort+"/Registry");
			this.registry = null;
			this.registry = LocateRegistry.createRegistry((new Integer(this.rmiRegistryPort)).intValue());
			this.vr.init("Registry",this.registry);
			this.vr.showLocalNodes(this.vueGraphe.getGraphe().ordre());
		    } else {
			this.vr.showLocalNodes(this.vueGraphe.getGraphe().ordre());
		    }
		} catch (Exception e) {
		    JOptionPane.showMessageDialog(this, "Cannot initialize RMI Registry : \n"+e.toString(), "Warning",JOptionPane.WARNING_MESSAGE);
		    e.printStackTrace();
		}
	    }
	}
    }
	
    public void menuExperiment(JMenuItem mi){
	if (mi == this.experiment_complet) {
	    BoiteExperimentComplet bec = new BoiteExperimentComplet(this,"size");
	    bec.show(this);
	}
	else if (mi == this.experiment_begin) {
	}
    }
    
    public void itemStateChanged(ItemEvent evt) {
	if((JCheckBoxMenuItem)evt.getSource() == this.item_visualization){
	    Enumeration enumerationSommets = this.vueGraphe.getGraphe().sommets();
	    Sommet unSommet;
	    boolean bool;
	    String boolString;
	    
	    if (this.item_visualization.isSelected()){
		bool = true;
		boolString = "yes";
	    } else { 
		bool = false;
		boolString = "no";
	    }
	    
	    while(enumerationSommets.hasMoreElements()){
		unSommet = (Sommet)enumerationSommets.nextElement();
		
		unSommet.getSommetDessin().setDrawMessage(bool);
		unSommet.getSommetDessin().setValue("Draw Messages",boolString);
	    }
	    if (this.sim_Rmi != null) {
		try {
		    this.sim_Rmi.setNodeDrawingMessage(bool);
		} catch (Exception e) {
		    System.out.println("Erreur au niveau Fenetre de Simulation");
		}
	    }
	}
    }	
    
    /********************************/
    /** Closing the current window **/
    /********************************/
    public void commandeClose() {
	if(this.sim_Rmi != null ){
	    this.simulationPanel.stop();
	    try{
		this.sim_Rmi.abortSimulation();
		Naming.unbind("rmi://:"+this.rmiRegistryPort+"/"+this.simulatorUrl);
		Naming.unbind("rmi://:"+this.rmiRegistryPort+"/Registry");
		this.vr = null;
		this.registry = null;
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    
	    this.seh.abort();
	}
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
	    while(e.hasMoreElements()) {
		FormeDessin forme = (FormeDessin)e.nextElement();
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
    protected String titre() {
	return "Algorithm simulator";
    }

    public String type(){
	return "Simulator";
    }

    public File fichier_rules_edite() {
	return this.fichier_rules_edite;
    }

    // Implementation of the Listeners
    
    public void windowOpened(WindowEvent e) {}
    
    public void windowClosing(WindowEvent e) {}
    
    public void windowClosed(WindowEvent e) {}
    
    public void windowIconified(WindowEvent e) {}
    
    public void windowDeiconified(WindowEvent e) {}
    
    public void windowActivated(WindowEvent e) {this.content.repaint();}
    
    public void windowDeactivated(WindowEvent e) {}

    public void commandeSupprimer() { // Penser au repaint()
	
	// Deleting the elements of the selection
	if(!this.selection.estVide()) {
	    Enumeration e = this.selection.elements();
	    while (e.hasMoreElements()) {
		FormeDessin forme = (FormeDessin)e.nextElement();
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
	    FormeDessin firstElement = ((FormeDessin)e.nextElement());
	    if (!Traitements.sommetDessin(this.selection.elements()).hasMoreElements()) {
		// we have only edges
		e = this.selection.elements();
		Ensemble listeElements = new Ensemble();
		listeElements.inserer(e);
		BoiteChangementEtatArete boiteArete =
		    new BoiteChangementEtatArete(this, listeElements);
		boiteArete.show(this);
	    }
	    else if ((this.selection.nbElements() == 1) && 
		     (firstElement.type().equals("vertex"))){
		BoiteChangementEtatSommetDist boiteSommet = 
		    new BoiteChangementEtatSommetDist(this, (SommetDessin)firstElement);
		boiteSommet.show(this);
	    }
	    else{
		e = this.selection.elements();
		visidia.gui.donnees.conteneurs.MultiEnsemble table_des_types = new MultiEnsemble();
		while(e.hasMoreElements())
		    table_des_types.inserer(((FormeDessin)e.nextElement()).type());
		BoiteSelection.show(this, this.selection.nbElements(), table_des_types);
	    }
	}
    }
    
   public void changerVueGraphe(VueGraphe grapheVisu){
	this.content.remove(this.scroller);
	this.selection.deSelect();
	this.vueGraphe = grapheVisu;
	this.simulationPanel = new SimulationPanel(this);
	this.simulationPanel.updatePas(this.speed_slider.getValue());
	this.scroller = new JScrollPane(this.simulationPanel);
	this.scroller.setPreferredSize(new Dimension(650,600));
        this.scroller.setOpaque(true);
        this.content.add(this.scroller, BorderLayout.CENTER);
    }
    
    public visidia.tools.VQueue getEvtPipe(){
	return this.evtPipeOut;
    }     
    public visidia.tools.VQueue getAckPipe(){
	return this.ackPipeOut;
    }     
    public void addRule(SimpleRule uneRegle){
    }
    public void incrementRules(){
    }
    public int numberOfRules(){
	return 0 ;
    }
    public void changeRules(Vector uneListeRegle){
    
    }
    
    public void setEdgeState(int id1, int id2, boolean hasFailure) {
    }
    public void nodeStateChanged(int nodeId, Hashtable properties) {
	try{
	    if (this.sim_Rmi != null){
		System.out.println("Reaction dans Fenetre de Simulation");
		this.sim_Rmi.setNodeProperties(nodeId, properties);
	    }
	} catch (Exception e) {
	    System.out.println ("Erreur lors de la modification des proprietes dans FenetreDeSimulation : "+e);
	}
    }


    public void setNetworkParam(LocalNodeTable table,String host, String url){
	this.networkParam = table;
	//table.print();
	this.simulatorHost = host;
	this.simulatorUrl = url;
    }

    
    public void setExperimentSize(Integer i) {
	this.experimentSize=i;
    }

    public void setRegistryPort(String portNumber){
	this.rmiRegistryPort=portNumber;
    }
    
    public void setAlgo(AlgorithmDist algo) {
	try {
	    this.algoRmi = algo;
	} catch (Exception e){
	    System.out.println("Erreur dans getAlgo : "+e);
	}
    }
    
    public void unSetAlgo() {
	    this.but_start.setEnabled(true);
    }    


    public Vector regles(){
	return null ;
    }
    
    
    public MessageChoiceDist getMenuChoice(){
        return this.messageChoiceDist;
    }
    
    public void setMenuChoice(JMenu menu) {
        this.messageChoiceDist=(MessageChoiceDist) menu;
    }
    
    public void setMessageType(MessageType msgType, boolean msgTypeState) {
	try {
	    if (this.sim_Rmi == null){
		this.algoRmi.setMessageType(msgType,msgTypeState);
	    }
	    else
		this.sim_Rmi.setMessageType(msgType,msgTypeState);
	} catch (Exception e) {
	    System.out.println("Erreur : "+e);
	    e.printStackTrace();
	}
    }

    private void raz() {
	this.networkParam=null;
	this.simulatorHost=null;
	this.simulatorUrl=null;
	this.rmiRegistryPort = "1099";
	this.sim_Rmi=null;
	this.algoRmi=null;
	this.menuBar.remove(this.messageChoiceDist);
	this.messageChoiceDist=new MessageChoiceDist(this);
	this.menuBar.add(this.messageChoiceDist);
	this.evtPipeIn = new VQueue();
	this.evtPipeOut = new VQueue() ;
	this.ackPipeIn = new VQueue();
	this.ackPipeOut = new VQueue();
	this.simulationAlgo = false;
	this.setJMenuBar(this.menuBar);
    }
}
