package visidia.gui.presentation.userInterfaceSimulation;

import visidia.gui.metier.*;
import visidia.graph.*;
import visidia.network.*;
import java.rmi.*;
import java.rmi.registry.*; 
import java.rmi.Naming;
import visidia.gui.*; 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
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
//import visidia.algoRMI.*;
import visidia.simulation.*;
import visidia.tools.*;
import visidia.misc.*;

/* Represents the algorithm simulation window for a graph */
public class FenetreDeSimulationDist extends Fenetre implements Serializable, ActionListener,ItemListener, WindowListener, ChangeListener {
    
    
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
	evtPipeIn = new visidia.tools.VQueue();
	evtPipeOut = new visidia.tools.VQueue();
	ackPipeIn = new visidia.tools.VQueue();
	ackPipeOut = new visidia.tools.VQueue();
	
	tg = new ThreadGroup("recorder");

	// The edited graph and the selection object which contains selected objects
	vueGraphe = grapheVisu_edite;
	selection = new SelectionDessin();

	//algoChoice = new AlgoChoice(grapheVisu_edite.getGraphe().ordre());

	// The manager of components
	content = new JPanel();
	content.setLayout(new BorderLayout());
	fichier_edite = fichier_edit;
	mettreAJourTitreFenetre();
		
	// The menu bar
	this.addMenu();
	// Current datas of the edition
       
	// BackGround Color of the GrapheVisuPanel
	couleur_de_fond = couleur_fond;
	
	// The edited graph and the selection object which contains selected objects
	//vueGraphe = grapheVisu_edite;
	//selection = new SelectionDessin();
	//algoChoice = new AlgoChoice(grapheVisu_edite.getGraphe().ordre());

	// The panel where the graph is drawn
	simulationPanel = new SimulationPanel(this);
	super.setSize(650,600);
	// un setSize est a faire avant l'ajout de composants pour eviter
	// les warnings
	scroller = new JScrollPane(simulationPanel);
	//scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	//scroller.setVerticalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	scroller.setPreferredSize(new Dimension(650,600));
	simulationPanel.revalidate();
	
	simulationPanel.scrollRectToVisible(new Rectangle((vueGraphe.donnerDimension()).width-10,(vueGraphe.donnerDimension()).height-10,30,30));
	simulationPanel.repaint();
	
	scroller.setOpaque(true);
	content.add(scroller, BorderLayout.CENTER);
	
						   

	this.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    if (simulationPanel != null)
			simulationPanel.stop();
		    if (seh != null)
			seh.abort();
		    commandeClose();
		    raz();
		    setVisible(false);
		    dispose();
		    // Running the garbage collector
		    Runtime.getRuntime().gc();
		    
		}
	    });
	
	// The tool bar
	this.addToolBar();
	
	// On disable les items non-valide pour une applet
	if(!DistributedAlgoSimulator.estStandalone()) 
	    disableButtonForApplet();
	

	this.setContentPane(content);
	boolean bool = true;
	try {
	    registry = LocateRegistry.createRegistry((new Integer(rmiRegistryPort)).intValue());
	} catch (RemoteException re) {
	    try {
		registry = LocateRegistry.getRegistry((new Integer(rmiRegistryPort)).intValue());
	    } catch (Exception e) {
		bool=false;
		JOptionPane.showMessageDialog(this, "Cannot initialize RMI Registry : \n"+e.toString(), "Warning",JOptionPane.WARNING_MESSAGE);
		//e.printStackTrace();
	    }
	}
	try {
	    if(bool){
		vr = new VisidiaRegistryImpl(this);
		vr.init("Registry",registry);
	    } 		
	} catch (Exception e) {
	    JOptionPane.showMessageDialog(this, "The Visidia Registration is not running"+e,"Error",JOptionPane.WARNING_MESSAGE);
	}
    }
	
    /**
   * This method adds the Menu bar, its menus and items to the editor
   **/
    protected void addMenu() {

	menuBar = new JMenuBar();
	menuBar.setOpaque(true);
	menuBar.setPreferredSize(new Dimension(650, 20));
    
	// Build the menu File
	file = new JMenu("File");
	file.getPopupMenu().setName("PopFile");
	file.setMnemonic('F');
   
	file_help = new JMenuItem("Help", KeyEvent.VK_H);
	file_help.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_H, ActionEvent.CTRL_MASK));
	file_help.addActionListener(this);
	file.add(file_help);
	file.addSeparator();    
	file_close = new JMenuItem("Close", KeyEvent.VK_C);
	file_close.setAccelerator(KeyStroke.getKeyStroke(
							 KeyEvent.VK_C, ActionEvent.CTRL_MASK));
	file_close.addActionListener(this);
	file.add(file_close);
	file_quit = new JMenuItem("Quit", KeyEvent.VK_Q);
	file_quit.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
	file_quit.addActionListener(this);
	file.add(file_quit);
	file.addActionListener(this);
	menuBar.add(file);


	graph = new JMenu("Graph");
	graph.getPopupMenu().setName("PopGraph");
	graph.setMnemonic('G');
    
	graph_open = new JMenuItem("Open graph ", KeyEvent.VK_O);
	graph_open.setAccelerator(KeyStroke.getKeyStroke(
							 KeyEvent.VK_O, ActionEvent.CTRL_MASK));
	graph_open.addActionListener(this);
	graph.add(graph_open);
    
	graph_save = new JMenuItem("Save Graph");
	graph_save.addActionListener(this);
	graph.add(graph_save);
    
	graph_save_as = new JMenuItem("Save graph as ...");
	graph_save_as.addActionListener(this);
	graph.add(graph_save_as);
	graph.addActionListener(this);
	menuBar.add(graph);
    
	algo = new JMenu("Algorithm");
	algo.getPopupMenu().setName("PopAlgo");
	algo.setMnemonic('A');
    
	algo_open = new JMenuItem("Open algorithm");
	algo_open.addActionListener(this);
	algo.add(algo_open);
	
	algo.addSeparator();

	algo_open_vertices = new JMenuItem("Put algorithm to vertices");
	algo_open_vertices.addActionListener(this);
	algo.add(algo_open_vertices);
	algo.setEnabled(vueGraphe.getGraphe().ordre()>0); // if we have an empty graph
    
	algo.addActionListener(this);
	menuBar.add(algo);
   
	config = new JMenu("Config");
	config.getPopupMenu().setName("PopConfig");
	config.setMnemonic('C');
	
	item_visualization = new JCheckBoxMenuItem("Visualize Messages",true);
	item_visualization.addItemListener(this);
	config.add(item_visualization);

	config.addSeparator();	   
	

	config_help = new JMenuItem("Help");
	config_help.addActionListener(this);
	config.add(config_help);
	
	config.addSeparator();
	
	config_registry = new JMenuItem("configure the registry");
	config_registry.addActionListener(this);
	config.add(config_registry);
	
	config.addSeparator();

	config_reseaux = new JMenuItem("Set Hosts for the Nodes");
	config_reseaux.addActionListener(this);
	config.add(config_reseaux);
	
	config_reseaux_file = new JMenuItem("Set Hosts from a file");
	config_reseaux_file.addActionListener(this);
	config.add(config_reseaux_file);

	config_registration = new JMenuItem("Get Local Nodes");
	config_registration.addActionListener(this);
	config.add(config_registration);
    	config.addActionListener(this);
	menuBar.add(config);
	
	
	experiment = new JMenu("Expermient");
	experiment.getPopupMenu().setName("PopExperiment");
	experiment.setMnemonic('E');
	experiment.setVisible(false);

	experiment_complet = new JMenuItem("Complete Graph", KeyEvent.VK_O);
	experiment_complet.addActionListener(this);
	experiment.add(experiment_complet);
	config.addSeparator();
	
	experiment_begin  = new JMenuItem("Begin Experiment", KeyEvent.VK_O);
	experiment_begin.addActionListener(this);
	experiment.add(experiment_begin);

	
	experiment.addActionListener(this);
	menuBar.add(experiment);

	messageChoiceDist = new MessageChoiceDist(this);
	messageChoiceDist.setMnemonic('M');
        menuBar.add(messageChoiceDist);
	
	this.setJMenuBar(menuBar);
    }
    /**
     * This method adds the tool bar and its buttons to the editor
     **/
    protected void addToolBar() {
	
	toolBar = new JToolBar();
	toolBar.setBackground(new Color(120, 120, 120));
	toolBar.setOpaque(true);
	toolBar.setPreferredSize(new Dimension(650, 42));
	
	this.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    commandeClose();
		}
	    });
	
	//Build buttons on the tool bar
	but_start = new JButton("start");
	but_start.setToolTipText("Start");
	but_start.setAlignmentY(CENTER_ALIGNMENT);
	but_start.setEnabled(false);
	but_start.addActionListener(this);
	toolBar.add(but_start);
	
	but_pause = new JButton("pause");
	but_pause.setToolTipText("Pause");
	but_pause.setAlignmentY(CENTER_ALIGNMENT);
	but_pause.setEnabled(false);
	but_pause.addActionListener(this);
	toolBar.add(but_pause);
	    
	but_stop = new JButton("stop");
	but_stop.setToolTipText("Stop");
	but_stop.setAlignmentY(CENTER_ALIGNMENT);
	but_stop.addActionListener(this);
	but_stop.setEnabled(false);
	toolBar.add(but_stop);
	toolBar.addSeparator();
	    
	toolBar.addSeparator();
	but_save = new JButton("save");
	but_save.setToolTipText("Save");
	but_save.setAlignmentY(CENTER_ALIGNMENT);
	but_save.addActionListener(this);
	toolBar.add(but_save);
	    
	toolBar.addSeparator();
	 
	// slider for speed modification 
	speed_slider = new JSlider(1, 20, 10);
	speed_slider.addChangeListener(this);
	speed_slider.setToolTipText("Speed");
	speed_slider.setAlignmentY(TOP_ALIGNMENT);
	speed_slider.setAlignmentX(LEFT_ALIGNMENT);
	speed_slider.setPreferredSize(new Dimension(80,15));
	speed_slider.setBackground(toolBar.getBackground().brighter());
	JPanel speed_panel = new JPanel();
	    
	speed_panel.setMaximumSize(new Dimension(90,40));
	speed_panel.setBackground(toolBar.getBackground());
	speed_label = new JLabel("Speed ("+simulationPanel.pas()+")");
	speed_label.setFont(new Font("Dialog",Font.BOLD,10));
	speed_label.setToolTipText("Speed");
	speed_label.setAlignmentY(TOP_ALIGNMENT);
	speed_label.setForeground(Color.black);
	speed_panel.add(speed_slider);
	speed_panel.add(speed_label);
	    
	toolBar.add(speed_panel);

	but_info = new JButton(new ImageIcon(TableImages.getImage("info")));//"fr/enserb/das/gui/donnees/images/info.gif"));
	but_info.setToolTipText("Info");
	but_info.setAlignmentY(CENTER_ALIGNMENT);
	but_info.addActionListener(this);
	toolBar.add(but_info);
	    
	toolBar.addSeparator();
	    
	but_help = new JButton(new ImageIcon(TableImages.getImage("help")));
	but_help.setToolTipText("Help");
	but_help.setAlignmentY(CENTER_ALIGNMENT);
	but_help.addActionListener(this);
	toolBar.add(but_help);

	toolBar.addSeparator();
	
	but_information_distribue = new JButton("Remote Node");
	but_information_distribue.setToolTipText("Remote Node");
	but_information_distribue.setAlignmentY(CENTER_ALIGNMENT);
	but_information_distribue.addActionListener(this);
	toolBar.add(but_information_distribue);
	
	toolBar.addSeparator();
	
	but_experimentation = new JButton("Statistics");
	but_experimentation.setToolTipText("Statistics");
	but_experimentation.setAlignmentY(CENTER_ALIGNMENT);
	but_experimentation.addActionListener(this);
	toolBar.add(but_experimentation);
	
	toolBar.addSeparator();
	
	but_threadCount = new JButton("threads");
	but_threadCount.setToolTipText("amount of threads that are active in the VM");
	but_threadCount.setAlignmentY(CENTER_ALIGNMENT);
	but_threadCount.addActionListener(this);
	toolBar.add(but_threadCount);
	toolBar.addSeparator();
	if(threadCountFrame == null){
	    threadCountFrame = new ThreadCountFrame(Thread.currentThread().getThreadGroup());
	}
	
	but_reset = new JButton("RESET");
	but_reset.setToolTipText("RESET");
	but_reset.setAlignmentY(CENTER_ALIGNMENT);
	but_reset.addActionListener(this);
	but_reset.setEnabled((fichier_edite != null));
	toolBar.add(but_reset); 

	content.add(toolBar, BorderLayout.NORTH);
    }
    

     // disable the button not used for the applet
    private void disableButtonForApplet(){
	file_quit.setEnabled(false);
	graph.setEnabled(false);
	//config.setEnabled(false);
	but_save.setEnabled(false);
	but_experimentation.setEnabled(false);
    }


    /**********************************************************/
    /* this method permit to validate the reset button if a   */
    /* saving is made and then change the title of the window */
    /**********************************************************/
    public void mettreAJourTitreFenetre(File fichier) {
	if(fichier != null) but_reset.setEnabled(true);
	super.mettreAJourTitreFenetre(fichier);
    }



    /**********************************************************/
    /* Returns the panel "simulationPanel" which corresponds  */ 
    /*   to the graph visualisation during the simulation     */
    /**********************************************************/
    public SimulationPanel simulationPanel() {
	return simulationPanel;
    }
    
    /*********************************************************/
    /* Implementation of ActionListener interface            */
    /* treatment of what is done when pushing buttons or menu*/
    /*********************************************************/
    public void actionPerformed(ActionEvent evt) {
	
	if(evt.getSource() instanceof JButton)
	    action_toolbar((JButton)evt.getSource());
	else if(evt.getSource() instanceof JMenuItem)
	    action_menu((JMenuItem)evt.getSource());  
    }
    
    /*********************************************************/
    /* Implementation of the ChangeListener interface        */
    /* action on the speed slider                            */
    /*********************************************************/
    public void stateChanged(ChangeEvent evt) {
	if (evt.getSource() == speed_slider) {
	    speed_label.setText("Speed ("+speed_slider.getValue()+")");
	    simulationPanel.updatePas(speed_slider.getValue());
	}
    }

    /*********************************************************/
    /* Method for making action corresponding                */
    /* to the menu used .                                    */
    /*********************************************************/
    public void action_menu(JMenuItem mi) {
	String le_menu = ((JPopupMenu)mi.getParent()).getName();
	
	if(le_menu == "PopFile") {
	    menuFile(mi);}
	else if(le_menu == "PopGraph")
	    menuGraph(mi);
	else if(le_menu == "PopAlgo")
	    menuAlgo(mi);
	else if(le_menu == "PopConfig")
	    menuConfig(mi);
	else if (le_menu == "PopExperiment")
	    menuExperiment(mi);
	//	else if(le_menu == "PopTypeMessage")
	//  menuTypeMessage(mi);
    }
    
    /*********************************************************/
    /*  Method for making action corresponding               */
    /* to the button of the toolBar used                     */
    /*********************************************************/
    public void action_toolbar(JButton b) {
	if (b == but_start) {
	    simulationPanel.start();	 
	    while(tg.activeCount() > 0){
		tg.interrupt();
		try{
		    Thread.currentThread().sleep(50);
		}
		catch(InterruptedException e){
		}
	    }
	    if (networkParam == null){
		System.out.println("you must configure the net for each node");
		JOptionPane.showMessageDialog(this, "you must configure the net for each \n"+" Node before runnig the simulation \n"+"           (Config menu) ",
						  "Error",
					      JOptionPane.WARNING_MESSAGE);
		return; 
	    }
	    
	    if (DistributedAlgoSimulator.estStandalone()) {
		try{
		    sim_Rmi = new Simulator_Rmi(Convertisseur.convertir(vueGraphe.getGraphe()),evtPipeOut,ackPipeOut,simulatorHost,simulatorUrl,rmiRegistryPort);
		    Naming.bind("rmi://:"+rmiRegistryPort+"/"+simulatorUrl,sim_Rmi);
		} catch (Exception e) {
		    System.out.println("An error has aquired when creating and binding the\n"+"Simulator to the localhost : <"+simulatorHost+","+rmiRegistryPort+","+simulatorUrl+">\n"+e);
		    JOptionPane.showMessageDialog(this, "An error has aquired when creating and binding the\n"+"Simulator to the localhost : <"+simulatorHost+","+rmiRegistryPort+","+simulatorUrl+">\n"+e,
						  "Error",
						  JOptionPane.WARNING_MESSAGE);
		    return;
		}
	    }
	    
	    seh =  new SimulEventHandler(this,evtPipeOut,ackPipeOut);
	    seh.start();
	    
	    try {
		sim_Rmi.initializeNodes(networkParam);
		sim_Rmi.startServer(algoRmi);
	    } catch (Exception e) {
		System.out.println("An Error has aquired when starting, initializing and runnig the nodes \n"+e);
		JOptionPane.showMessageDialog(this, "An Error has aquired when starting, initializing and runnig the nodes \n"+e,
					      "Error",
						  JOptionPane.WARNING_MESSAGE);
		return; 
	    }
	    
	    
	    but_stop.setEnabled(true);
	    but_pause.setEnabled(true);
	    but_start.setEnabled(false);
	}
	else if (b == but_pause) {
	    if(simulationPanel.isRunning()){
		simulationPanel.pause();   
		try {
		    sim_Rmi.wedge();
		}catch (Exception e) {}
	    }
	    else {
		simulationPanel.start();
		try {
		    sim_Rmi.unWedge();
		} catch (Exception e) {}
	    }
	}
	else if (b == but_stop) {
	    simulationPanel.stop();   

	    /* a revoir 
	    try {
		sim_Rmi.abortSimulation();
		Naming.unbind("rmi://:"+rmiRegistryPort+"/"+simulatorUrl);
	    } catch (Exception e) { 
		System.out.println("Erreur dans le unbind : "+e);
	    }
	    */

	    seh.abort();

	    this.raz();
	    
	    but_start.setEnabled(false);
	    but_pause.setEnabled(false);
	    but_stop.setEnabled(false);
	}
	else if (b == but_experimentation){
	    try{
		System.out.println("the number of messages exchanged at this stage is "+sim_Rmi.getMessageNumber());
		//JOptionPane.showMessageDialog(this, "the number of messages exchanged at this stage is "+sim_Rmi.getMessageNumber());
	    } catch (Exception e) {
		System.err.println("Erreur dans count message"+e);
	    }
	}
	else if (b == but_threadCount){
	    JOptionPane.showMessageDialog(this, "not implemented");
        }
	
	else if (b == but_save) {
	    SaveFile.save(this, vueGraphe.getGraphe());
	}
	
	else if (b == but_info){
	    propertiesControl();
	}
	else if (b == but_reset) {
	    simulationPanel.stop();
	    try{
		sim_Rmi.abortSimulation();
		Naming.unbind("rmi://:"+rmiRegistryPort+"/"+simulatorUrl);
		Naming.unbind("rmi://:"+rmiRegistryPort+"/Registry");
		vr = null;
		registry = null;
	    } catch (Exception e) { 
		System.out.println("Erreur dans le unbind"+e);
	    }
	    
	    seh.abort();
	  	  
	    if (fichier_edite != null)
		OpenGraph.open(this,fichier_edite);
		//OpenGraph.open(this);
	    evtPipeIn = new visidia.tools.VQueue();
	    evtPipeOut = new visidia.tools.VQueue();
	    ackPipeIn = new visidia.tools.VQueue();
	    ackPipeOut = new visidia.tools.VQueue();
	    replaceSelection(new SelectionDessin());
	    
	    simulationPanel.setPreferredSize(vueGraphe.donnerDimension());
	    simulationPanel.revalidate();
	    simulationPanel.scrollRectToVisible(new Rectangle(650,600,0,0));
	    simulationPanel.repaint();
	    
	    but_start.setEnabled(true);
	    but_pause.setEnabled(false);
	    but_stop.setEnabled(false);
	    
	    algo.setEnabled(vueGraphe.getGraphe().ordre()>0); // if we have an empty graph
	}
	else if (b == but_information_distribue) {
	    try {
		Hashtable table = sim_Rmi.getGraphStub();
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
	
	  
        if(mi == file_help) {
	    JOptionPane.showMessageDialog(this,
					  "DistributedAlgoSimulator, v2\n" +
					  "in this window you can't modifie the graph \n"+
					  "except changing the state of edges or vertices\n"+
					  "before starting simulation you must load an algorithm \n "+
					  "or a list of simple rules \n");
	}
	else if(mi == file_close)
	    commandeClose();
	else if(mi == file_quit)
	    System.exit(0);
    }
    
    /*************************************************************/
    /* Methods for "graph" menu.      */
    /*************************************************************/
    public void menuGraph(JMenuItem mi) {
	if(mi == graph_open){
	    OpenGraph.open(this);
	    algo.setEnabled(vueGraphe.getGraphe().ordre()>0); // if we have an empty graph
	    //algoChoice = new AlgoChoice(vueGraphe.getGraphe().ordre());
	    replaceSelection(new SelectionDessin());
	    simulationPanel.setPreferredSize(vueGraphe.donnerDimension());
	    simulationPanel.revalidate();
	    simulationPanel.scrollRectToVisible(new Rectangle(650,600,0,0));
	    simulationPanel.repaint();
	    but_start.setEnabled(false);
	    but_pause.setEnabled(false);
	    but_stop.setEnabled(false);
	}
	
	else if(mi == graph_save) {
	    
	    SaveFile.save(this, vueGraphe.getGraphe());
	}
	else if(mi == graph_save_as) {
	    fichier_edite = null;
	    SaveFile.saveAs(this, vueGraphe.getGraphe());
	}
    }
    
    /*************************************************************/
    /* Method for the fonctionnalities of the "Algo" menu.       */
    /*************************************************************/
    public void menuAlgo(JMenuItem mi) {
        if(mi == algo_open){
	    if(DistributedAlgoSimulator.estStandalone())
		OpenAlgoDistribue.open(this);
	    else
		OpenAlgoAppletDistribue.open(this);
	    simulationAlgo = true ;
	    but_start.setEnabled(true);
	}
    }
    /*************************************************************/
    /* Method for the fonctionnalities of the "rules" menu.      */
    /*************************************************************/


    /*************************************************************/
    /* Method for the fonctionnalities of the "trace" menu.      */
    /*************************************************************/
    
    public void menuConfig(JMenuItem mi){
	if (mi == config_help) {
	    OpenHelpDist openHelpDist = new OpenHelpDist("Help on the configuration");
	    openHelpDist.show();
	}
	else if (mi == config_registry) {
	    if(DistributedAlgoSimulator.estStandalone()){
		BoiteRegistry boiteRegistry = 
		    new BoiteRegistry(this,"Registry configuration");
		boiteRegistry.show(this);
	    }
	}
	else if(mi == config_reseaux){
	    BoiteDistribue boiteDistribue = 
		new BoiteDistribue(this, this.vueGraphe.cloner(), "configuration du reseaux");
	    boiteDistribue.show(this);
	}

	else if(mi == config_reseaux_file) {
	    if(DistributedAlgoSimulator.estStandalone()){
		OpenConfig oc = new OpenConfig();
		oc.open(this,vueGraphe.getGraphe().ordre());
	    }
	}
	else if (mi == config_registration) {
	    if(DistributedAlgoSimulator.estStandalone()){
		try {
		    if (!rmiRegistryPort.equals("1099")){
			registry.unbind("rmi://:"+rmiRegistryPort+"/Registry");
			registry = null;
			registry = LocateRegistry.createRegistry((new Integer(rmiRegistryPort)).intValue());
			vr.init("Registry",registry);
			vr.showLocalNodes(vueGraphe.getGraphe().ordre());
		    } else {
			vr.showLocalNodes(vueGraphe.getGraphe().ordre());
		    }
		} catch (Exception e) {
		    JOptionPane.showMessageDialog(this, "Cannot initialize RMI Registry : \n"+e.toString(), "Warning",JOptionPane.WARNING_MESSAGE);
		    e.printStackTrace();
		}
	    }
	}
    }
	
    public void menuExperiment(JMenuItem mi){
	if (mi == experiment_complet) {
	    BoiteExperimentComplet bec = new BoiteExperimentComplet(this,"size");
	    bec.show(this);
	}
	else if (mi == experiment_begin) {
	}
    }
    
    public void itemStateChanged(ItemEvent evt) {
	if((JCheckBoxMenuItem)evt.getSource() == item_visualization){
	    Enumeration enumerationSommets = vueGraphe.getGraphe().sommets();
	    Sommet unSommet;
	    boolean bool;
	    String boolString;
	    
	    if (item_visualization.isSelected()){
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
		    sim_Rmi.setNodeDrawingMessage(bool);
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
	if(sim_Rmi != null ){
	    simulationPanel.stop();
	    try{
		sim_Rmi.abortSimulation();
		Naming.unbind("rmi://:"+rmiRegistryPort+"/"+simulatorUrl);
		Naming.unbind("rmi://:"+rmiRegistryPort+"/Registry");
		vr = null;
		registry = null;
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    
	    seh.abort();
	}
	setVisible(false);
	dispose();
	// collecting the garbage 
	Runtime.getRuntime().gc();
    }
    
    /**
     * Selection of all the graph
     */
    public void commandeToutSelectionner() { // Penser au repaint()
	int i = 0;
	Enumeration e = vueGraphe.listeAffichage();
	if (e.hasMoreElements()) {
	    while(e.hasMoreElements()) {
		FormeDessin forme = (FormeDessin)e.nextElement();
		selection.insererElement(forme);
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
	return fichier_rules_edite;
    }

    // Implementation of the Listeners
    
    public void windowOpened(WindowEvent e) {}
    
    public void windowClosing(WindowEvent e) {}
    
    public void windowClosed(WindowEvent e) {}
    
    public void windowIconified(WindowEvent e) {}
    
    public void windowDeiconified(WindowEvent e) {}
    
    public void windowActivated(WindowEvent e) {content.repaint();}
    
    public void windowDeactivated(WindowEvent e) {}

    public void commandeSupprimer() { // Penser au repaint()
	
	// Deleting the elements of the selection
	if(!selection.estVide()) {
	    Enumeration e = selection.elements();
	    while (e.hasMoreElements()) {
		FormeDessin forme = (FormeDessin)e.nextElement();
		forme.delete();
	    }
	}
	
    }

    private void replaceSelection(SelectionDessin new_selection) {
        // Deletes the initial selection and replaces it with the new one
        emptyCurrentSelection(true);
        selection = new_selection;
        selection.select();
    }
    

    /**
     * Method to empty the current selection
     */
    public void emptyCurrentSelection(boolean deselect) { // Penser au repaint()
	if (!selection.estVide()) 
	    if (deselect) {
		selection.deSelect();
	    }
    }

    // action on the property button with a selection
	
    private void propertiesControl() {
	if (selection.estVide()) 
	    System.out.println("empty");
	else {
	    Enumeration e = selection.elements();
	    FormeDessin firstElement = ((FormeDessin)e.nextElement());
	    if (!Traitements.sommetDessin(selection.elements()).hasMoreElements()) {
		// we have only edges
		e = selection.elements();
		Ensemble listeElements = new Ensemble();
		listeElements.inserer(e);
		BoiteChangementEtatArete boiteArete =
		    new BoiteChangementEtatArete(this, listeElements);
		boiteArete.show(this);
	    }
	    else if ((selection.nbElements() == 1) && 
		     (firstElement.type().equals("vertex"))){
		BoiteChangementEtatSommetDist boiteSommet = 
		    new BoiteChangementEtatSommetDist(this, (SommetDessin)firstElement);
		boiteSommet.show(this);
	    }
	    else{
		e = selection.elements();
		visidia.gui.donnees.conteneurs.MultiEnsemble table_des_types = new MultiEnsemble();
		while(e.hasMoreElements())
		    table_des_types.inserer(((FormeDessin)e.nextElement()).type());
		BoiteSelection.show(this, selection.nbElements(), table_des_types);
	    }
	}
    }
    
   public void changerVueGraphe(VueGraphe grapheVisu){
	content.remove(scroller);
	selection.deSelect();
	this.vueGraphe = grapheVisu;
	this.simulationPanel = new SimulationPanel(this);
	simulationPanel.updatePas(speed_slider.getValue());
	scroller = new JScrollPane(this.simulationPanel);
	scroller.setPreferredSize(new Dimension(650,600));
        scroller.setOpaque(true);
        content.add(scroller, BorderLayout.CENTER);
    }
    
    public visidia.tools.VQueue getEvtPipe(){
	return evtPipeOut;
    }     
    public visidia.tools.VQueue getAckPipe(){
	return ackPipeOut;
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
	    if (sim_Rmi != null){
		System.out.println("Reaction dans Fenetre de Simulation");
		sim_Rmi.setNodeProperties(nodeId, properties);
	    }
	} catch (Exception e) {
	    System.out.println ("Erreur lors de la modification des proprietes dans FenetreDeSimulation : "+e);
	}
    }


    public void setNetworkParam(LocalNodeTable table,String host, String url){
	networkParam = table;
	//table.print();
	simulatorHost = host;
	simulatorUrl = url;
    }

    
    public void setExperimentSize(Integer i) {
	experimentSize=i;
    }

    public void setRegistryPort(String portNumber){
	rmiRegistryPort=portNumber;
    }
    
    public void setAlgo(AlgorithmDist algo) {
	try {
	    algoRmi = algo;
	} catch (Exception e){
	    System.out.println("Erreur dans getAlgo : "+e);
	}
    }
    
    public void unSetAlgo() {
	    but_start.setEnabled(true);
    }    


    public Vector regles(){
	return null ;
    }
    
    
    public MessageChoiceDist getMenuChoice(){
        return messageChoiceDist;
    }
    
    public void setMenuChoice(JMenu menu) {
        messageChoiceDist=(MessageChoiceDist) menu;
    }
    
    public void setMessageType(MessageType msgType, boolean msgTypeState) {
	try {
	    if (sim_Rmi == null){
		algoRmi.setMessageType(msgType,msgTypeState);
	    }
	    else
		this.sim_Rmi.setMessageType(msgType,msgTypeState);
	} catch (Exception e) {
	    System.out.println("Erreur : "+e);
	    e.printStackTrace();
	}
    }

    private void raz() {
	networkParam=null;
	simulatorHost=null;
	simulatorUrl=null;
	rmiRegistryPort = "1099";
	sim_Rmi=null;
	algoRmi=null;
	menuBar.remove(messageChoiceDist);
	messageChoiceDist=new MessageChoiceDist(this);
	menuBar.add(messageChoiceDist);
	evtPipeIn = new VQueue();
	evtPipeOut = new VQueue() ;
	ackPipeIn = new VQueue();
	ackPipeOut = new VQueue();
	simulationAlgo = false;
	this.setJMenuBar(menuBar);
    }
}
