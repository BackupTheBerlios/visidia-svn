package visidia.gui.presentation.userInterfaceSimulation;

import visidia.gui.DistributedAlgoSimulator;
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
import visidia.simulation.*;
import visidia.simulation.agents.AgentSimulator;
import visidia.tools.*;
import visidia.misc.MessageType;
import visidia.simulation.rules.*;
import visidia.simulation.synchro.*;
import visidia.rule.*;
import visidia.gui.presentation.starRule.*;
import visidia.simulation.synchro.synObj.*;

/* Represents the algorithm simulation window for a graph */
public class AgentsSimulationWindow 
    extends Fenetre
    implements Serializable, ActionListener, WindowListener, ChangeListener,
	       ApplyStarRulesSystem {
    
    protected static final String GENERAL_TITLE = "Agents Simulator";
    protected String algoTitle;
    
    // instance of simulator for stop/pause/start actions
    protected AgentSimulator sim;
    protected AgentSimulEventHandler seh;
    protected JToolBar toolBar;
    protected JButton but_start, but_pause, but_save, but_stop, but_help, but_experimentation, but_threadCount;
    protected JButton but_info , but_regles , but_reset;
    protected PulseButton global_clock;

    // save an execution
    protected JMenu trace;
    protected ButtonGroup item_group;
    protected JRadioButtonMenuItem item_nothing, item_saveTrace, item_replay;
    protected File fileSaveTrace;
    protected ObjectWriter writer;
    
    protected JMenuItem item_chose, item_file;
    protected ThreadGroup tg=null;
    
    protected JMenuBar menuBar;
    protected JMenu file , rules ,  graph,algo;
    protected JMenuItem graph_open, algo_open, algo_placeAgent, algo_open_vertices, graph_save , graph_save_as , 
	file_quit , file_close, file_help, graph_select_all, graph_disconnect, graph_reconnect;
    protected JMenuItem rules_open, rules_new;
    //protected JMenuItem new_simple_rules ;
    //protected JMenuItem new_star_rules ;
    // Menu pour les options au niveau de la visualisation
    protected JMenu visualizationOptions ;
    // for the speed scale
//     protected ChoiceMessage2 choiceMessage;
    protected JMenuItem synchro, others;
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
    protected Vector rulesList  ;
    protected boolean[][] edgesStates = null;
    protected AbstractRule rsAlgo; // The algorithm witch will simulate the relabeling system

    protected boolean simulationAlgo = false;
    protected boolean simulationRules = false;
    protected static ThreadCountFrame threadCountFrame;
    public static boolean visuAlgorithmMess = true;
    public static boolean visuSynchrMess = true;
    public Editeur editeur;

    protected Hashtable agentsTable;


    public AgentsSimulationWindow(VueGraphe grapheVisu_edite, File fichier_edit,Editeur editeur) {
        
        this(grapheVisu_edite, COULEUR_FOND_PAR_DEFAUT, DIM_X_PAR_DEFAUT,
	     DIM_Y_PAR_DEFAUT, fichier_edit);
        this.addWindowListener(this);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setSize(650, 600);
        this.pack();
        this.setVisible(true);
	this.editeur = editeur;
    }
    
    
    public AgentsSimulationWindow(VueGraphe grapheVisu_edite , Color couleur_fond, int dim_x,
			       int dim_y, File fichier_edit) {
        
        super();
        evtPipeIn = new visidia.tools.VQueue();
        evtPipeOut = new visidia.tools.VQueue();
        ackPipeIn = new visidia.tools.VQueue();
        ackPipeOut = new visidia.tools.VQueue();
        fileSaveTrace = new File("trace_1.trace");
        writer = new ObjectWriter();
        
        tg = new ThreadGroup("recorder");
        
        // The edited graph and the selection object which contains selected objects
        vueGraphe = grapheVisu_edite;
        selection = new SelectionDessin();
        
	agentsTable = new Hashtable();

	// The manager of components
        content = new JPanel();
        content.setLayout(new BorderLayout());
        fichier_edite = fichier_edit;
        mettreAJourTitreFenetre();
        rulesList = new Vector();
        
        // The menu bar
        this.addMenu();
        // Current datas of the edition
        
        // BackGround Color of the GrapheVisuPanel
        couleur_de_fond = couleur_fond;
        
        // The edited graph and the selection object which contains selected objects
        vueGraphe = grapheVisu_edite;
        selection = new SelectionDessin();
        
	// The panel where the graph is drawn
        simulationPanel = new SimulationPanel(this);
        super.setSize(DIM_X_PAR_DEFAUT,DIM_Y_PAR_DEFAUT);
        // un setSize est a faire avant l'ajout de composants pour eviter
        // les warnings
        scroller = new JScrollPane(simulationPanel);
        scroller.setPreferredSize(new Dimension(DIM_X_PAR_DEFAUT,DIM_Y_PAR_DEFAUT));
        simulationPanel.revalidate();
        
        simulationPanel.scrollRectToVisible(new Rectangle((vueGraphe.donnerDimension()).width-10,(vueGraphe.donnerDimension()).height-10,30,30));
        simulationPanel.repaint();
        
        scroller.setOpaque(true);
        content.add(scroller, BorderLayout.CENTER);
        
        
        
        this.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
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
    }

    
    /**
     *
     **/
    public void addAgents(Integer id, String agent) {

	boolean ok;
	if(!agentsTable.containsKey(id)) {
	    agentsTable.put(id, new ArrayList());
	}
	ok = ((ArrayList)agentsTable.get(id)).add(agent);
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
        graph = new JMenu("graph");
        graph.getPopupMenu().setName("PopGraph");
        graph.setMnemonic('G');
        
        graph_open = new JMenuItem("Open graph ", KeyEvent.VK_O);
        graph_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        graph_open.addActionListener(this);
        graph.add(graph_open);
        
        graph_save = new JMenuItem("Save Graph");
        graph_save.addActionListener(this);
        graph.add(graph_save);
        
        graph_save_as = new JMenuItem("Save graph as...");
        graph_save_as.addActionListener(this);
        graph.add(graph_save_as);
        graph.addActionListener(this);
	menuBar.add(graph);
	
	graph.addSeparator();
	
	// graphe connection and disconnection
	
	/*graph_disconnect = new JMenuItem("Disconnect");
	  graph_disconnect.addActionListener(this);
	  graph_disconnect.setToolTipText("Disconnect the selected elements");
	  graph.add(graph_disconnect);
	  graph_reconnect = new JMenuItem("Reconnect");
	  graph_reconnect.addActionListener(this);
	  graph_reconnect.setToolTipText("Reconnect the selected elements");
	  graph.add(graph_reconnect);*/
	
	graph_select_all = new JMenuItem("Select all");
	graph_select_all.addActionListener(this);
	graph_select_all.setToolTipText("Select all the elements of the graph");
	graph.add(graph_select_all);

        graph.addActionListener(this);
        menuBar.add(graph);

 
        algo = new JMenu("Agents"); //!!
        algo.getPopupMenu().setName("PopAlgo");
        algo.setMnemonic('A');
        
        algo_open = new JMenuItem("Add Agents...");
        algo_open.addActionListener(this);
        algo.add(algo_open);

        algo_placeAgent = new JMenuItem("Place Agents...");
        algo_placeAgent.addActionListener(this);
        algo.add(algo_placeAgent);

        algo.setEnabled(vueGraphe.getGraphe().ordre()>0); // if we have an empty graph
        
	algo.addActionListener(this);
	menuBar.add(algo);
        
	rules = new JMenu("Rules");
	rules.getPopupMenu().setName("PopRules");
	rules.setMnemonic('R');
	
	rules_new = new JMenuItem("New relabeling system");
	rules_new.addActionListener(this);
	rules.add(rules_new);
	
	rules_open = new JMenuItem("Open rules...");
	rules_open.addActionListener(this);
	rules.add(rules_open);
	
	menuBar.add(rules);
                
        trace = new JMenu("Trace");
        trace.getPopupMenu().setName("PopTrace");
        trace.setMnemonic('T');
        
        item_nothing = new JRadioButtonMenuItem("Nothing");
        item_nothing.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        item_nothing.addActionListener(this);
        item_nothing.setSelected(true);
        item_group = new ButtonGroup();
        item_group.add(item_nothing);
        trace.add(item_nothing);
        
        item_saveTrace = new JRadioButtonMenuItem("Save trace");
        item_saveTrace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        item_saveTrace.addActionListener(this);
        item_group.add(item_saveTrace);
        trace.add(item_saveTrace);
        
        item_replay = new JRadioButtonMenuItem("Replay");
        item_replay.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        item_replay.addActionListener(this);
        item_group.add(item_replay);
        trace.add(item_replay);
        
        trace.addSeparator();
        
        item_chose = new JMenuItem("Chose a file");
        item_chose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        item_chose.addActionListener(this);
        trace.add(item_chose);
        
        trace.addSeparator();
        
        item_file = new JMenuItem(fileSaveTrace.getName());
        trace.add(item_file);
        
        menuBar.add(trace);
        
        
        /*
	  visualizationOptions  = new VisualizationOptions(this);
	  menuBar.add(visualizationOptions);
         
	*/
        
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
        
        speed_panel.setMaximumSize(new Dimension(85,40));
        speed_panel.setBackground(toolBar.getBackground());
        speed_label = new JLabel("Speed ("+simulationPanel.pas()+")");
        speed_label.setFont(new Font("Dialog",Font.BOLD,10));
        speed_label.setToolTipText("Speed");
        speed_label.setAlignmentY(TOP_ALIGNMENT);
        speed_label.setForeground(Color.black);
        speed_panel.add(speed_slider);
        speed_panel.add(speed_label);
        
        toolBar.add(speed_panel);


        global_clock = new PulseButton();
	toolBar.add(global_clock);



        toolBar.addSeparator();

	
        
        but_info = new JButton(new ImageIcon(TableImages.getImage("info")));//"visidia/gui/donnees/images/info.gif"));
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
        
        but_experimentation = new JButton("Statistics");
        but_experimentation.setToolTipText("Statistics");
        but_experimentation.setAlignmentY(CENTER_ALIGNMENT);
        but_experimentation.addActionListener(this);
        toolBar.add(but_experimentation);
        toolBar.addSeparator();
        
        but_threadCount = new JButton("theads");
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
        rules.setEnabled(false);
        graph.setEnabled(false);
        trace.setEnabled(false);
        rules_new.setEnabled(false);
        but_save.setEnabled(false);
        but_experimentation.setEnabled(false);
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
        else if(le_menu == "PopRules")
            menuRules(mi);
        else if(le_menu == "PopTrace")
            menuTrace(mi);
        /*else if(le_menu == "PopRules_new")
	  menuNew(mi);*/
        
    }
    
    /*********************************************************/
    /*  Method for making action corresponding               */
    /* to the button of the toolBar used                     */
    /*********************************************************/

    public void but_start() {
	simulationPanel.start();
	// modifications for the recorder
	sim = null;
	// destruction of ths old threads
	while(tg.activeCount() > 0){
	    tg.interrupt();
	    try{
		Thread.currentThread().sleep(50);
	    }
	    catch(InterruptedException e){
	    }
	}
        
	if (item_saveTrace.isSelected()){
	    fileSaveTrace.delete();
	    try {
		writer.close();
	    }
	    catch (Exception e) {
	    }
	    writer.open(fileSaveTrace);
	    writer.writeObject(vueGraphe.getGraphe());
            
	    RecorderEvent recorderEvent = new RecorderEvent(evtPipeIn, evtPipeOut, writer);
	    RecorderAck recorderAck = new RecorderAck(ackPipeIn, ackPipeOut, writer);
	    new Thread(tg, recorderEvent).start();
	    new Thread(tg, recorderAck).start();
//dam 	    sim = new AgentSimulator(Convertisseur.convertir(vueGraphe.getGraphe()),evtPipeIn,ackPipeOut,algoChoice);
	}
	else if (item_replay.isSelected()){
	    visidia.simulation.Reader reader = new visidia.simulation.Reader(ackPipeOut, evtPipeOut, fileSaveTrace);
	    reader.read();
	    new Thread(tg, reader).start();
	}
	else if (item_nothing.isSelected())
	    sim = new AgentSimulator(Convertisseur
                                     .convert(vueGraphe.getGraphe(),
                                              agentsTable),
                                     evtPipeOut, ackPipeOut);

        selection.deSelect();

        seh =  new AgentSimulEventHandler(this,evtPipeOut,ackPipeOut);
 	seh.start();

	but_stop.setEnabled(true);
	but_pause.setEnabled(true);
	but_start.setEnabled(false);
	
        sim.startSimulation();

    }
    
    public void but_pause() {
	if(simulationPanel.isRunning()){
	    simulationPanel.pause();
//dam 	    sim.wedge();
	}
	else {
	    simulationPanel.start();
//dam 	    sim.unWedge();
	}
    }

    public void but_stop() {
	System.out.println("Stopping the Simulation panel");
	simulationPanel.stop();
	System.out.println("  ==> Stopped");
	System.out.println("Stopping the Simulator");
	if (sim != null)
 	    sim.abortSimulation();

	System.out.println("  ==> Stopped");
	
	System.out.println("Stopping the Simulator Event Handler");
	seh.abort();
	System.out.println(" ==> Stopped");
	
	evtPipeIn = new visidia.tools.VQueue();
	evtPipeOut = new visidia.tools.VQueue();
	ackPipeIn = new visidia.tools.VQueue();
	ackPipeOut = new visidia.tools.VQueue();
	
	but_start.setEnabled(false);
	but_pause.setEnabled(false);
	but_stop.setEnabled(false);
	but_reset.setEnabled(true);
	global_clock.initState();
    }

    public void but_experimentation() {
//jbnada 	if ((vueGraphe.getGraphe().sommets().hasMoreElements()) &&
// 	    (algoChoice.verticesHaveAlgorithm())) {
// 	    JFrame frame = new ExperimentationFrame(vueGraphe, algoChoice);
// 	    frame.setTitle("Algorithm Experiments ["+algoTitle+"]");
// 	    frame.pack();
// 	    frame.setVisible(true);
// 	}
// 	else {
// 	    JOptionPane.showMessageDialog(this, "Load a graph and an algorithm");
// 	}
    }
    

    public void but_reset() {
	simulationPanel.stop();
	if (sim != null)
 	    sim.abortSimulation();
	
        if (seh != null)
            seh.abort();
	
	/*
	  if (fichier_edite != null)
	  OpenGraph.open(this,fichier_edite);
	*/
	
        agentsTable.clear();

	vueGraphe = editeur.getGraphClone();
	
	evtPipeIn = new visidia.tools.VQueue();
	evtPipeOut = new visidia.tools.VQueue();
	ackPipeIn = new visidia.tools.VQueue();
	ackPipeOut = new visidia.tools.VQueue();
	replaceSelection(new SelectionDessin());
	simulationPanel.setPreferredSize(vueGraphe.donnerDimension());
	simulationPanel.revalidate();
	simulationPanel.scrollRectToVisible(new Rectangle(650,600,0,0));
	simulationPanel.repaint();
	
	//algo.setEnabled(vueGraphe.getGraphe().ordre()>0); // if we have an empty graph
	
	but_start.setEnabled(false);
	but_pause.setEnabled(false);
	but_stop.setEnabled(false);
	but_reset.setEnabled(false);
	global_clock.initState();
	
    }

    public void action_toolbar(JButton b) {
        if (b == but_start){
	    but_start();
        }
        else if (b == but_pause) {
	    but_pause();
        }
	else if (b == but_stop) {
	    but_stop();
        }
	else if (b == but_experimentation){
	    but_experimentation();
        }
	else if (b == but_threadCount){
            threadCountFrame.pack();
            threadCountFrame.setVisible(true);
        }
	else if (b == but_save) {
            SaveFile.save(this, vueGraphe.getGraphe());
        }
        else if (b == but_info){
            propertiesControl();
        }
	//PFA2003
	else if (b == but_help){
// 	    if (algoChoice.verticesHaveAlgorithm()) {
// 		Algorithm a = algoChoice.getAlgorithm(0);
// 		HelpDialog hd = new HelpDialog(this, "Algorithm description");
// 		hd.setText(a.getDescription());
// 		hd.setVisible(true);
// 		hd.setEditable(false);
// 	    } else {
// 		JOptionPane.showMessageDialog
// 		    (this, "You must enter an algorithm or rules ",
// 		     "warning", JOptionPane.WARNING_MESSAGE);
// 	    }
	}
        else if (b == but_reset) {
	    but_reset();
	}
    }


    public void setUpTimeUnits(int pulse) {
	//PulseFrame pulseFrame = new PulseFrame();
	//pulseFrame.setPulse(pulse);
	//global_clock.setToolTipText("Click to view time units");
	global_clock.setPulse(pulse);
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
    /* Method for the fonctionnalities of the "graph" menu.      */
    /*************************************************************/
    public void menuGraph(JMenuItem mi) {
        if(mi == graph_open){
            OpenGraph.open(this);
            algo.setEnabled(vueGraphe.getGraphe().ordre()>0); // if we have an empty graph
//             algoChoice = new AlgoChoice(vueGraphe.getGraphe().ordre());
            replaceSelection(new SelectionDessin());
            simulationPanel.setPreferredSize(vueGraphe.donnerDimension());
            simulationPanel.revalidate();
            simulationPanel.scrollRectToVisible(new Rectangle(650,600,0,0));
            simulationPanel.repaint();
            if (item_replay.isSelected())
                but_start.setEnabled(true);
            else
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
	else if (mi == graph_select_all) {
	    //PFA2003
	    Enumeration e = vueGraphe.listeAffichage();
	    while(e.hasMoreElements()) {
		FormeDessin objetVisu = (FormeDessin)e.nextElement();
		selection.insererElement(objetVisu);
	    }
	    repaint ();
	}
    }
    
    /*************************************************************/
    /* Method for the fonctionnalities of the "Algo" menu.       */
    /*************************************************************/
    public void menuAlgo(JMenuItem mi) {

	//nada jb
//         if (mi == algo_open_vertices){
//             if (!selection.estVide()){
//                 if(DistributedAlgoSimulator.estStandalone()){
//  //                    OpenAlgo.openForVertex(selection.elements(),this);
//                     System.out.println("choix de l'algo reussi");}
//                 else
//  //                    OpenAlgoApplet.openForVertices(selection.elements(),this);
//                 but_start.setEnabled(algoChoice.verticesHaveAlgorithm());
//             }
//         }
        if(mi == algo_open){
            if(simulationRules){
                JOptionPane.showMessageDialog(this, "you had already entered rules",
					      "warning",
					      JOptionPane.WARNING_MESSAGE);
            }
            
            else {
		boolean ok = true;

                if(selection.estVide()) {
                    JOptionPane
                        .showMessageDialog(this,
                                           "You must select at least one"
                                           + " vertex!",
                                           "Warning",
                                           JOptionPane.WARNING_MESSAGE);
                    return;
                }

		if(DistributedAlgoSimulator.estStandalone())
		    ok = OpenAgents.open(selection.elements(),this);
		else
 //                    OpenAlgoApplet.open(this);
                    ;
                simulationAlgo = ok ;

		if(! but_start.isEnabled())
		    but_start.setEnabled(ok);
            }
        }

        if (mi == algo_placeAgent) {
            boolean ok = true;

            ok = OpenAgentChooser.open(this);
            simulationAlgo = ok ;

            if(! but_start.isEnabled())
                but_start.setEnabled(ok);
        }
    }
    
    /*************************************************************/
    /* Method for the fonctionnalities of the "rules" menu.      */
    /*************************************************************/
    public void menuRules(JMenuItem mi) {
	if (mi == rules_open) {
            if(simulationAlgo){
                JOptionPane.showMessageDialog(this, 
					      "An algorithm has already been selected",
					      "Warning",
					      JOptionPane.WARNING_MESSAGE);
            } else {
		final javax.swing.filechooser.FileFilter filter = 
		    new javax.swing.filechooser.FileFilter () {
			public boolean accept (File f) {
			    String n = f.getName ();
			    return n.endsWith ("srs");
			}
			public String getDescription () {
			    return "srs (star rules system) files";
			}
		    };
		
		JFileChooser chooser = new JFileChooser ();
		chooser.setDialogType (JFileChooser.OPEN_DIALOG);
		chooser.setFileFilter (filter);
		chooser.setCurrentDirectory (new File ("./"));
		int returnVal = chooser.showOpenDialog (this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    try {
			String fName = chooser.getSelectedFile().getPath();
			FileInputStream istream = new FileInputStream(fName);
			ObjectInputStream p = new ObjectInputStream(istream);
			RelabelingSystem rSys = (RelabelingSystem) p.readObject();
			istream.close();
			applyStarRulesSystem(rSys);
		    } catch (IOException ioe) {
			System.out.println (ioe);
		    } catch (ClassNotFoundException cnfe) {
			System.out.println (cnfe);
		    }
		}
	    }
        } else if (mi == rules_new) {
	    if (simulationAlgo) {
		int res = JOptionPane.showConfirmDialog 
		    (this, "The algorithm has already been selected ;\n if " 
		     + "you continue, you will not be able to apply new rules.\n "
		     + "Continue ? ", "Open new rule", JOptionPane.YES_NO_OPTION);
		if (res == JOptionPane.NO_OPTION) {
		    return;
		}
	    }
	    StarRuleFrame starRuleFrame = new StarRuleFrame((JFrame) this,
							    (ApplyStarRulesSystem) this);
	    starRuleFrame.setVisible(true);
	}
    }
    
    //PFA2003
    public void applyStarRulesSystem(RelabelingSystem rSys) {
	if (simulationRules) {
	    JOptionPane.showMessageDialog(this, 
					  "An algorithm has already been selected",
					  "Warning",
					  JOptionPane.WARNING_MESSAGE);
	    return;
	}
	simulationRules = true;
	rsAlgo = buildAlgoRule(rSys);
// 	getAlgorithms().putAlgorithmToAllVertices(rsAlgo);
// 	getMenuChoice().setListTypes(rsAlgo.getListTypes());
	but_start.setEnabled(true);
    }
    
    /*************************************************************/
    /* Method for the fonctionnalities of the "trace" menu.      */
    /*************************************************************/
    public void menuTrace(JMenuItem mi) {
        if (mi == item_replay){
            but_start.setEnabled(true);
            but_reset.setEnabled(true);
            
            evtPipeIn = new visidia.tools.VQueue();
            evtPipeOut = new visidia.tools.VQueue();
            ackPipeIn = new visidia.tools.VQueue();
            ackPipeOut = new visidia.tools.VQueue();
            
            OpenGraph.open(this,fileSaveTrace);
//             algoChoice = new AlgoChoice(vueGraphe.getGraphe().ordre());
            replaceSelection(new SelectionDessin());
            simulationPanel.setPreferredSize(vueGraphe.donnerDimension());
            simulationPanel.revalidate();
            simulationPanel.scrollRectToVisible(new Rectangle(650,600,0,0));
            simulationPanel.repaint();
            
        }
        else if (mi == item_nothing) {
            if (simulationAlgo == false)
                but_start.setEnabled(false);
        }
        else if (mi == item_saveTrace){
            if (simulationAlgo == false)
                but_start.setEnabled(false);
        }
        else if(mi == item_chose) {
            File f = SaveTrace.save(this);
            if (f != null) {
                fileSaveTrace = f;
                item_file.setText(f.getName());
                
                OpenGraph.open(this,fileSaveTrace);
//                 algoChoice = new AlgoChoice(vueGraphe.getGraphe().ordre());
                replaceSelection(new SelectionDessin());
                simulationPanel.setPreferredSize(vueGraphe.donnerDimension());
                simulationPanel.revalidate();
                simulationPanel.scrollRectToVisible(new Rectangle(650,600,0,0));
                simulationPanel.repaint();
            }
        }
    }
    
    
          
    /********************************/
    /** Closing the current window **/
    /********************************/
    public void commandeClose() {
        if (sim != null) {   // we kill the threads
            simulationPanel.stop();
//dam             sim.abortSimulation();
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
    /**********************************************************/
    /* this method permit to validate the reset button if a   */
    /* saving is made and then change the title of the window */
    /**********************************************************/
    public void mettreAJourTitreFenetre(File fichier) {
        if(fichier != null) but_reset.setEnabled(true);
        super.mettreAJourTitreFenetre(fichier);
    }

    public void mettreAJourTitreFenetre(String nom_Algo) {
	algoTitle = nom_Algo;
	super.mettreAJourTitreFenetre(nom_Algo);
    }

    
    protected String titre() {
        return "Algorithm Simulator";
    }
    
    public String getAlgoTitle() {
	return algoTitle;
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
//                 BoiteChangementCouleurArete boiteArete =
// 		    new BoiteChangementCouleurArete(this, listeElements);
                BoiteChangementEtatArete boiteArete =
		    new BoiteChangementEtatArete(this, listeElements);

                boiteArete.show(this);
            }
            else if ((selection.nbElements() == 1) &&
		     (firstElement.type().equals("vertex"))){
                AgentBoxChangingVertexState boiteSommet =
		    new AgentBoxChangingVertexState(this, (SommetDessin)firstElement);
                boiteSommet.show(this);
            }
            else{
                e = selection.elements();
                visidia.gui.donnees.conteneurs.MultiEnsemble table_des_types = new MultiEnsemble();
                while(e.hasMoreElements())
                    table_des_types.inserer(((FormeDessin)e.nextElement()).type());
		AgentBoxSimulationSelection.show(this, selection, table_des_types);
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
        if (item_saveTrace.isSelected())
            return ackPipeIn;
        else
            return ackPipeOut;
    }
    public void setEdgeState(int id1, int id2, boolean hasFailure) {
        edgesStates[id1][id2] = hasFailure;
    }
    
    public static void setVisuAlgorithmMess(boolean b){
        visuAlgorithmMess = b;
    }
    
    public static void setVisuSynchrMess(boolean b){
        visuSynchrMess = b;
    }
    
    
    public void nodeStateChanged(int nodeId, Hashtable properties) {
        //System.out.println("aaaa= "+nodeId+" gggg = "+ properties);
	if (sim != null)
//dam 	    sim.setNodeProperties(nodeId, properties);
            ;
        //sim.restartNode(nodeId);
    }

    /**
     * Makes an AbstractRule from a RelabelingSystem
     */
    public AbstractRule buildAlgoRule(RelabelingSystem r){
	AbstractRule algo;
	int synType;//user choice
	int type;//default choice
	SynObject synob;
	RSOptions options = r.getOptions();
	if (options.defaultSynchronisation() != -1) {
	    type = r.defaultSynchronisation();
	    /* user choice */
	    synType = options.defaultSynchronisation();
	    if ((synType  == SynCT.RDV) && (type == SynCT.LC1)) {
		JOptionPane.showMessageDialog
		    (this, "The rendez-vous synchronisation cannot be used\n" +
		     "because of context or arity", "Error",
		     JOptionPane.WARNING_MESSAGE);
	    }
	    if ((synType  == SynCT.LC1)&&(type== SynCT.RDV)){
		JOptionPane.showMessageDialog
		    (this, "The use of use of LC1 is not recommended :\n" +
		     "check if you modify the neighbors.", "Error",
		     JOptionPane.WARNING_MESSAGE);
	    }
	} else {
	    /* default option */
	    synType = r.defaultSynchronisation();
	}
	
	switch(synType){
	case SynCT.LC2:
	    r.dupplicateSimpleRules(synType);
	    algo = new LC2Rule(r);
	    break;
	case SynCT.LC1:
	    algo = new LC1Rule(r);
	    break;
	default:
	    r.dupplicateSimpleRules(synType);
	    algo = new RDVRule(r);
	    break;
	}
	synob = new SynObjectRules();
    
	algo.setSynob(synob);
	return algo;
	
    }
}

