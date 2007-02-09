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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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
import visidia.gui.donnees.TableImages;
import visidia.gui.donnees.conteneurs.Ensemble;
import visidia.gui.donnees.conteneurs.MultiEnsemble;
import visidia.gui.metier.inputOutput.OpenAlgo;
import visidia.gui.metier.inputOutput.OpenAlgoApplet;
import visidia.gui.metier.inputOutput.OpenGraph;
import visidia.gui.metier.inputOutput.SaveFile;
import visidia.gui.metier.inputOutput.SaveTrace;
import visidia.gui.metier.simulation.AlgoChoice;
import visidia.gui.metier.simulation.Convertisseur;
import visidia.gui.metier.simulation.SimulEventHandler;
import visidia.gui.presentation.FormeDessin;
import visidia.gui.presentation.HelpDialog;
import visidia.gui.presentation.SelectionDessin;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.VueGraphe;
import visidia.gui.presentation.boite.BoiteChangementCouleurArete;
import visidia.gui.presentation.boite.BoiteChangementEtatSommet;
import visidia.gui.presentation.boite.BoiteSelectionSimulation;
import visidia.gui.presentation.boite.PulseCounter;
import visidia.gui.presentation.boite.ThreadCountFrame;
import visidia.gui.presentation.starRule.StarRuleFrame;
import visidia.gui.presentation.userInterfaceEdition.Editeur;
import visidia.gui.presentation.userInterfaceEdition.Fenetre;
import visidia.gui.presentation.userInterfaceEdition.Traitements;
import visidia.rule.RSOptions;
import visidia.rule.RelabelingSystem;
import visidia.simulation.Algorithm;
import visidia.simulation.ObjectWriter;
import visidia.simulation.RecorderAck;
import visidia.simulation.RecorderEvent;
import visidia.simulation.Simulator;
import visidia.simulation.rules.AbstractRule;
import visidia.simulation.rules.LC1Rule;
import visidia.simulation.rules.LC2Rule;
import visidia.simulation.rules.RDVRule;
import visidia.simulation.synchro.SynCT;
import visidia.simulation.synchro.synObj.SynObject;
import visidia.simulation.synchro.synObj.SynObjectRules;

/* Represents the algorithm simulation window for a graph */
public class FenetreDeSimulation 
    extends Fenetre
    implements Serializable, ActionListener, WindowListener, ChangeListener,
	       ApplyStarRulesSystem {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 2200055017799019244L;
	protected static final String GENERAL_TITLE = "Algroithm Simulator";
    protected String algoTitle;
    
    // instance of simulator for stop/pause/start actions
    protected Simulator sim;
    protected SimulEventHandler seh;
    protected JToolBar toolBar;
    protected JButton but_start, but_pause, but_save, but_stop, but_help, but_experimentation, but_threadCount;
    protected JButton but_info , but_regles , but_reset;

    // protected PulseButton global_clock;
    protected PulseCounter global_clock;

    
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
    protected JMenuItem graph_open, algo_open, algo_open_vertices, graph_save , graph_save_as , 
	file_quit , file_close, file_help, graph_select_all, graph_disconnect, graph_reconnect;
    protected JMenuItem rules_open, rules_new;
    //protected JMenuItem new_simple_rules ;
    //protected JMenuItem new_star_rules ;
    // Menu pour les options au niveau de la visualisation
    protected JMenu visualizationOptions ;
    // for the speed scale
    protected ChoiceMessage2 choiceMessage;
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
    //PFA2003
    protected AbstractRule rsAlgo; // The algorithm witch will simulate the relabeling system

    protected AlgoChoice algoChoice;
    protected boolean simulationAlgo = false;
    protected boolean simulationRules = false;
    protected static ThreadCountFrame threadCountFrame;
    public static boolean visuAlgorithmMess = true;
    public static boolean visuSynchrMess = true;
    public Editeur editeur;

    public ChoiceMessage2 getMenuChoice(){
        return this.choiceMessage;
    }
    public void setMenuChoice(JMenu menu) {
        this.choiceMessage=(ChoiceMessage2) menu;
    }
    public FenetreDeSimulation(VueGraphe grapheVisu_edite, File fichier_edit,Editeur editeur) {
        
        this(grapheVisu_edite, COULEUR_FOND_PAR_DEFAUT, DIM_X_PAR_DEFAUT,
	     DIM_Y_PAR_DEFAUT, fichier_edit);
        this.addWindowListener(this);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setSize(650, 600);
        this.pack();
        this.setVisible(true);
	this.editeur = editeur;
    }
    
    
    public FenetreDeSimulation(VueGraphe grapheVisu_edite , Color couleur_fond, int dim_x,
			       int dim_y, File fichier_edit) {
        
        super();
        this.evtPipeIn = new visidia.tools.VQueue();
        this.evtPipeOut = new visidia.tools.VQueue();
        this.ackPipeIn = new visidia.tools.VQueue();
        this.ackPipeOut = new visidia.tools.VQueue();
        this.fileSaveTrace = new File("trace_1.trace");
        this.writer = new ObjectWriter();
        
        this.tg = new ThreadGroup("recorder");
        
        // The edited graph and the selection object which contains selected objects
        this.vueGraphe = grapheVisu_edite;
        this.selection = new SelectionDessin();
        
        this.algoChoice = new AlgoChoice(grapheVisu_edite.getGraphe().ordre());
        
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
        
        this.algoChoice = new AlgoChoice(grapheVisu_edite.getGraphe().ordre());
        
        // The panel where the graph is drawn
        this.simulationPanel = new SimulationPanel(this);
        super.setSize(800,600);
	// simulationPanel.setPreferredSize(vueGraphe.donnerDimension());
	// simulationPanel.revalidate();
        // un setSize est a faire avant l'ajout de composants pour eviter
        // les warnings
        this.scroller = new JScrollPane(this.simulationPanel);
        this.scroller.setPreferredSize(new Dimension(800,600));
	//scroller.setPreferredSize(vueGraphe.donnerDimension());
	this.simulationPanel.revalidate();
        
        this.simulationPanel.scrollRectToVisible(new Rectangle((this.vueGraphe.donnerDimension()).width-10,(this.vueGraphe.donnerDimension()).height-10,30,30));
        this.simulationPanel.repaint();
        
        this.scroller.setOpaque(true);
        this.content.add(this.scroller, BorderLayout.CENTER);
        
        
        
        this.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    FenetreDeSimulation.this.setVisible(false);
		    FenetreDeSimulation.this.dispose();
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
        this.graph = new JMenu("graph");
        this.graph.getPopupMenu().setName("PopGraph");
        this.graph.setMnemonic('G');
        
        this.graph_open = new JMenuItem("Open graph ", KeyEvent.VK_O);
        this.graph_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
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
	
	//PFA2003
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
	this.graph_select_all.setToolTipText("Select all the elements of the graph");
	this.graph.add(this.graph_select_all);

        this.graph.addActionListener(this);
        this.menuBar.add(this.graph);

 
        this.algo = new JMenu("Algorithm");
        this.algo.getPopupMenu().setName("PopAlgo");
        this.algo.setMnemonic('A');
        
        this.algo_open = new JMenuItem("Open algorithm ");
        this.algo_open.addActionListener(this);
        this.algo.add(this.algo_open);
        this.algo.addSeparator();
        this.algo_open_vertices = new JMenuItem("Put algorithm to vertices ");
        this.algo_open_vertices.addActionListener(this);
        this.algo.add(this.algo_open_vertices);
        this.algo.setEnabled(this.vueGraphe.getGraphe().ordre()>0); // if we have an empty graph
        
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
                
        this.trace = new JMenu("Trace");
        this.trace.getPopupMenu().setName("PopTrace");
        this.trace.setMnemonic('T');
        
        this.item_nothing = new JRadioButtonMenuItem("Nothing");
        this.item_nothing.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        this.item_nothing.addActionListener(this);
        this.item_nothing.setSelected(true);
        this.item_group = new ButtonGroup();
        this.item_group.add(this.item_nothing);
        this.trace.add(this.item_nothing);
        
        this.item_saveTrace = new JRadioButtonMenuItem("Save trace");
        this.item_saveTrace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        this.item_saveTrace.addActionListener(this);
        this.item_group.add(this.item_saveTrace);
        this.trace.add(this.item_saveTrace);
        
        this.item_replay = new JRadioButtonMenuItem("Replay");
        this.item_replay.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        this.item_replay.addActionListener(this);
        this.item_group.add(this.item_replay);
        this.trace.add(this.item_replay);
        
        this.trace.addSeparator();
        
        this.item_chose = new JMenuItem("Chose a file");
        this.item_chose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        this.item_chose.addActionListener(this);
        this.trace.add(this.item_chose);
        
        this.trace.addSeparator();
        
        this.item_file = new JMenuItem(this.fileSaveTrace.getName());
        this.trace.add(this.item_file);
        
        this.menuBar.add(this.trace);
        
        
        /*
	  visualizationOptions  = new VisualizationOptions(this);
	  menuBar.add(visualizationOptions);
         
	*/
        
        this.choiceMessage=new ChoiceMessage2(this.algoChoice);
        this.menuBar.add(this.choiceMessage);
        
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
		    FenetreDeSimulation.this.commandeClose();
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
        this.speed_slider.setPreferredSize(new Dimension(80,15));
        this.speed_slider.setBackground(this.toolBar.getBackground().brighter());
        JPanel speed_panel = new JPanel();
        
        speed_panel.setMaximumSize(new Dimension(85,40));
        speed_panel.setBackground(this.toolBar.getBackground());
        this.speed_label = new JLabel("Speed ("+this.simulationPanel.pas()+")");
        this.speed_label.setFont(new Font("Dialog",Font.BOLD,10));
        this.speed_label.setToolTipText("Speed");
        this.speed_label.setAlignmentY(TOP_ALIGNMENT);
        this.speed_label.setForeground(Color.black);
        speed_panel.add(this.speed_slider);
        speed_panel.add(this.speed_label);
        
        this.toolBar.add(speed_panel);

	
        // global_clock = new PulseButton();
	// toolBar.add(global_clock);



        this.toolBar.addSeparator();

	
        
        this.but_info = new JButton(new ImageIcon(TableImages.getImage("info")));//"visidia/gui/donnees/images/info.gif"));
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
        
        this.but_experimentation = new JButton("Statistics");
        this.but_experimentation.setToolTipText("Statistics");
        this.but_experimentation.setAlignmentY(CENTER_ALIGNMENT);
        this.but_experimentation.addActionListener(this);
        this.toolBar.add(this.but_experimentation);
        this.toolBar.addSeparator();
        
        this.but_threadCount = new JButton("Threads");
        this.but_threadCount.setToolTipText("Amount of threads that are active in the VM");
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
	
        this.global_clock = new PulseCounter();
        this.content.add(this.toolBar, BorderLayout.NORTH);
	this.content.add(this.global_clock, BorderLayout.SOUTH);
    }
    
    
    // disable the button not used for the applet
    private void disableButtonForApplet(){
        this.file_quit.setEnabled(false);
        this.rules.setEnabled(false);
        this.graph.setEnabled(false);
        this.trace.setEnabled(false);
        this.rules_new.setEnabled(false);
        this.but_save.setEnabled(false);
        this.but_experimentation.setEnabled(false);
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
        else if(le_menu == "PopRules")
            this.menuRules(mi);
        else if(le_menu == "PopTrace")
            this.menuTrace(mi);
        /*else if(le_menu == "PopRules_new")
	  menuNew(mi);*/
        
    }
    
    /*********************************************************/
    /*  Method for making action corresponding               */
    /* to the button of the toolBar used                     */
    /*********************************************************/

    public void but_start() {
	this.simulationPanel.start();
	// modifications for the recorder
	this.sim = null;
	// destruction of ths old threads
	while(this.tg.activeCount() > 0){
	    this.tg.interrupt();
	    try{
		Thread.currentThread().sleep(50);
	    }
	    catch(InterruptedException e){
	    }
	}
        
	if (this.item_saveTrace.isSelected()){
	    this.fileSaveTrace.delete();
	    try {
		this.writer.close();
	    }
	    catch (Exception e) {
	    }
	    this.writer.open(this.fileSaveTrace);
	    this.writer.writeObject(this.vueGraphe.getGraphe());
            
	    RecorderEvent recorderEvent = new RecorderEvent(this.evtPipeIn, this.evtPipeOut, this.writer);
	    RecorderAck recorderAck = new RecorderAck(this.ackPipeIn, this.ackPipeOut, this.writer);
	    new Thread(this.tg, recorderEvent).start();
	    new Thread(this.tg, recorderAck).start();
	    this.sim = new Simulator(Convertisseur.convertir(this.vueGraphe.getGraphe()),this.evtPipeIn,this.ackPipeOut,this.algoChoice);
	}
	else if (this.item_replay.isSelected()){
	    visidia.simulation.Reader reader = new visidia.simulation.Reader(this.ackPipeOut, this.evtPipeOut, this.fileSaveTrace);
	    reader.read();
	    new Thread(this.tg, reader).start();
	}
	else if (this.item_nothing.isSelected())
	    this.sim = new Simulator(Convertisseur.convertir(this.vueGraphe.getGraphe()),this.evtPipeOut,this.ackPipeOut,this.algoChoice);
	
        
	/*if(simulationRegles)
	  algoChoice.putAlgorithmToAllVertices(new AlgoRule(rulesList));
	  else if(!algoChoice.verticesHaveAlgorithm()) {
	  if (!item_replay.isSelected()) {
	  JOptionPane.showMessageDialog(this, "you must enter an algorithm or rules ",
	  "warning",
	  JOptionPane.WARNING_MESSAGE);
	  return;
	  }
	  }
	*/
	
	if (this.simulationRules) {
	    //System.out.println("rules");
	    this.simulationRules = false;
	} else if (!this.algoChoice.verticesHaveAlgorithm()) {
	    if (!this.item_replay.isSelected()) {
		JOptionPane.showMessageDialog
		    (this, "you must enter an algorithm or rules ",
		     "warning", JOptionPane.WARNING_MESSAGE);
		return;
	    }
	}
	
	if (this.item_saveTrace.isSelected())
	    this.seh = new SimulEventHandler(this,this.evtPipeOut,this.ackPipeIn);
	else
	    this.seh =  new SimulEventHandler(this,this.evtPipeOut,this.ackPipeOut);

	this.seh.start();

	if (!this.item_replay.isSelected()) {
	    this.sim.startSimulation();
	}
        
	this.but_stop.setEnabled(true);
	this.but_pause.setEnabled(true);
	this.but_start.setEnabled(false);
	
    }
    
    public void but_pause() {
	if(this.simulationPanel.isRunning()){
	    this.simulationPanel.pause();
	    this.sim.wedge();
	}
	else {
	    this.simulationPanel.start();
	    this.sim.unWedge();
	}
    }

    public void but_stop() {
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
	
	this.evtPipeIn = new visidia.tools.VQueue();
	this.evtPipeOut = new visidia.tools.VQueue();
	this.ackPipeIn = new visidia.tools.VQueue();
	this.ackPipeOut = new visidia.tools.VQueue();
	
	this.but_start.setEnabled(false);
	this.but_pause.setEnabled(false);
	this.but_stop.setEnabled(false);
	this.but_reset.setEnabled(true);
	this.global_clock.initState();
    }

    public void but_experimentation() {
	if ((this.vueGraphe.getGraphe().sommets().hasMoreElements()) &&
	    (this.algoChoice.verticesHaveAlgorithm())) {
	    JFrame frame = new ExperimentationFrame(this.vueGraphe, this.algoChoice);
	    frame.setTitle("Algorithm Experiments ["+this.algoTitle+"]");
	    frame.pack();
	    frame.setVisible(true);
	}
	else {
	    JOptionPane.showMessageDialog(this, "Load a graph and an algorithm");
	}
    }
    

    public void but_reset() {
	this.simulationPanel.stop();
	if (this.sim != null)
	    this.sim.abortSimulation();
	
	this.seh.abort();
	
	/*
	  if (fichier_edite != null)
	  OpenGraph.open(this,fichier_edite);
	*/
	
	this.vueGraphe = this.editeur.getGraphClone();
	
	this.evtPipeIn = new visidia.tools.VQueue();
	this.evtPipeOut = new visidia.tools.VQueue();
	this.ackPipeIn = new visidia.tools.VQueue();
	this.ackPipeOut = new visidia.tools.VQueue();
	this.replaceSelection(new SelectionDessin());
	this.simulationPanel.setPreferredSize(this.vueGraphe.donnerDimension());
	this.simulationPanel.revalidate();
	this.simulationPanel.scrollRectToVisible(new Rectangle(650,600,0,0));
	this.simulationPanel.repaint();
	
	//algo.setEnabled(vueGraphe.getGraphe().ordre()>0); // if we have an empty graph
	
	this.but_start.setEnabled(true);
	this.but_pause.setEnabled(false);
	this.but_stop.setEnabled(false);
	this.but_reset.setEnabled(false);
	this.global_clock.initState();
	
    }

    public void action_toolbar(JButton b) {
        if (b == this.but_start){
	    this.but_start();
        }
        else if (b == this.but_pause) {
	    this.but_pause();
        }
	else if (b == this.but_stop) {
	    this.but_stop();
        }
	else if (b == this.but_experimentation){
	    this.but_experimentation();
        }
	else if (b == this.but_threadCount){
            threadCountFrame.pack();
            threadCountFrame.setVisible(true);
        }
	else if (b == this.but_save) {
            SaveFile.save(this, this.vueGraphe.getGraphe());
        }
        else if (b == this.but_info){
            this.propertiesControl();
        }
	//PFA2003
	else if (b == this.but_help){
	    if (this.algoChoice.verticesHaveAlgorithm()) {
		Algorithm a = this.algoChoice.getAlgorithm(0);
		HelpDialog hd = new HelpDialog(this, "Algorithm description");
		hd.setText(a.getDescription());
		hd.setVisible(true);
		hd.setEditable(false);
	    } else {
		JOptionPane.showMessageDialog
		    (this, "You must enter an algorithm or rules ",
		     "warning", JOptionPane.WARNING_MESSAGE);
	    }
	}
        else if (b == this.but_reset) {
	    this.but_reset();
	}
    }


    public void setUpTimeUnits(int pulse) {
	//PulseFrame pulseFrame = new PulseFrame();
	//pulseFrame.setPulse(pulse);
	//global_clock.setToolTipText("Click to view time units");
	this.global_clock.setPulse(pulse);
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
    /* Method for the fonctionnalities of the "graph" menu.      */
    /*************************************************************/
    public void menuGraph(JMenuItem mi) {
        if(mi == this.graph_open){
            OpenGraph.open(this);
            this.algo.setEnabled(this.vueGraphe.getGraphe().ordre()>0); // if we have an empty graph
            this.algoChoice = new AlgoChoice(this.vueGraphe.getGraphe().ordre());
            this.replaceSelection(new SelectionDessin());
            this.simulationPanel.setPreferredSize(this.vueGraphe.donnerDimension());
            this.simulationPanel.revalidate();
            this.simulationPanel.scrollRectToVisible(new Rectangle(650,600,0,0));
            this.simulationPanel.repaint();
            if (this.item_replay.isSelected())
                this.but_start.setEnabled(true);
            else
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
	else if (mi == this.graph_select_all) {
	    //PFA2003
	    Enumeration e = this.vueGraphe.listeAffichage();
	    while(e.hasMoreElements()) {
		FormeDessin objetVisu = (FormeDessin)e.nextElement();
		this.selection.insererElement(objetVisu);
	    }
	    this.repaint ();
	}
    }
    
    /*************************************************************/
    /* Method for the fonctionnalities of the "Algo" menu.       */
    /*************************************************************/
    public void menuAlgo(JMenuItem mi) {
        if (mi == this.algo_open_vertices){
            if (!this.selection.estVide()){
                if(DistributedAlgoSimulator.estStandalone()){
                    OpenAlgo.openForVertex(this.selection.elements(),this);
                    System.out.println("choix de l'algo reussi");}
                else
                    OpenAlgoApplet.openForVertices(this.selection.elements(),this);
                this.but_start.setEnabled(this.algoChoice.verticesHaveAlgorithm());
            }
        }
        if(mi == this.algo_open){
            if(this.simulationRules){
                JOptionPane.showMessageDialog(this, "you had already entered rules",
					      "warning",
					      JOptionPane.WARNING_MESSAGE);
            }
            
            else {
		// PFA2003
		boolean ok = true;
                if(DistributedAlgoSimulator.estStandalone())
                    ok = OpenAlgo.open(this);
                else
                    OpenAlgoApplet.open(this);
                this.simulationAlgo = ok ;
                if(! this.but_start.isEnabled())
		    this.but_start.setEnabled(ok);
            }
        }
    }
    
    /*************************************************************/
    /* Method for the fonctionnalities of the "rules" menu.      */
    /*************************************************************/
    public void menuRules(JMenuItem mi) {
	if (mi == this.rules_open) {
            if(this.simulationAlgo){
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
			this.applyStarRulesSystem(rSys);
		    } catch (IOException ioe) {
			System.out.println (ioe);
		    } catch (ClassNotFoundException cnfe) {
			System.out.println (cnfe);
		    }
		}
	    }
        } else if (mi == this.rules_new) {
	    if (this.simulationAlgo) {
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
	if (this.simulationRules) {
	    JOptionPane.showMessageDialog(this, 
					  "An algorithm has already been selected",
					  "Warning",
					  JOptionPane.WARNING_MESSAGE);
	    return;
	}
	this.simulationRules = true;
	this.rsAlgo = this.buildAlgoRule(rSys);
	this.getAlgorithms().putAlgorithmToAllVertices(this.rsAlgo);
	this.getMenuChoice().setListTypes(this.rsAlgo.getListTypes());
	this.but_start.setEnabled(true);
    }
    
    /*************************************************************/
    /* Method for the fonctionnalities of the "trace" menu.      */
    /*************************************************************/
    public void menuTrace(JMenuItem mi) {
        if (mi == this.item_replay){
            this.but_start.setEnabled(true);
            this.but_reset.setEnabled(true);
            
            this.evtPipeIn = new visidia.tools.VQueue();
            this.evtPipeOut = new visidia.tools.VQueue();
            this.ackPipeIn = new visidia.tools.VQueue();
            this.ackPipeOut = new visidia.tools.VQueue();
            
            OpenGraph.open(this,this.fileSaveTrace);
            this.algoChoice = new AlgoChoice(this.vueGraphe.getGraphe().ordre());
            this.replaceSelection(new SelectionDessin());
            this.simulationPanel.setPreferredSize(this.vueGraphe.donnerDimension());
            this.simulationPanel.revalidate();
            this.simulationPanel.scrollRectToVisible(new Rectangle(650,600,0,0));
            this.simulationPanel.repaint();
            
        }
        else if (mi == this.item_nothing) {
            if (this.simulationAlgo == false)
                this.but_start.setEnabled(false);
        }
        else if (mi == this.item_saveTrace){
            if (this.simulationAlgo == false)
                this.but_start.setEnabled(false);
        }
        else if(mi == this.item_chose) {
            File f = SaveTrace.save(this);
            if (f != null) {
                this.fileSaveTrace = f;
                this.item_file.setText(f.getName());
                
                OpenGraph.open(this,this.fileSaveTrace);
                this.algoChoice = new AlgoChoice(this.vueGraphe.getGraphe().ordre());
                this.replaceSelection(new SelectionDessin());
                this.simulationPanel.setPreferredSize(this.vueGraphe.donnerDimension());
                this.simulationPanel.revalidate();
                this.simulationPanel.scrollRectToVisible(new Rectangle(650,600,0,0));
                this.simulationPanel.repaint();
            }
        }
    }
    
    
          
    /********************************/
    /** Closing the current window **/
    /********************************/
    public void commandeClose() {
        if (this.sim != null) {   // we kill the threads
            this.simulationPanel.stop();
            this.sim.abortSimulation();
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
    /**********************************************************/
    /* this method permit to validate the reset button if a   */
    /* saving is made and then change the title of the window */
    /**********************************************************/
    public void mettreAJourTitreFenetre(File fichier) {
        if(fichier != null) this.but_reset.setEnabled(true);
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
                BoiteChangementCouleurArete boiteArete =
		    new BoiteChangementCouleurArete(this, listeElements);
                boiteArete.show(this);
            }
            else if ((this.selection.nbElements() == 1) &&
		     (firstElement.type().equals("vertex"))){
                BoiteChangementEtatSommet boiteSommet =
		    new BoiteChangementEtatSommet(this, (SommetDessin)firstElement);
                boiteSommet.show(this);
            }
            else{
                e = this.selection.elements();
                visidia.gui.donnees.conteneurs.MultiEnsemble table_des_types = new MultiEnsemble();
                while(e.hasMoreElements())
                    table_des_types.inserer(((FormeDessin)e.nextElement()).type());
		BoiteSelectionSimulation.show(this, this.selection, table_des_types);
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
    
    // load an algorithm
    public AlgoChoice getAlgorithms(){
        return this.algoChoice;
    }
    
    public visidia.tools.VQueue getEvtPipe(){
        return this.evtPipeOut;
    }
    public visidia.tools.VQueue getAckPipe(){
        if (this.item_saveTrace.isSelected())
            return this.ackPipeIn;
        else
            return this.ackPipeOut;
    }
    public void setEdgeState(int id1, int id2, boolean hasFailure) {
        this.edgesStates[id1][id2] = hasFailure;
    }
    
    public static void setVisuAlgorithmMess(boolean b){
        visuAlgorithmMess = b;
    }
    
    public static void setVisuSynchrMess(boolean b){
        visuSynchrMess = b;
    }
    
    
    public void nodeStateChanged(int nodeId, Hashtable properties) {
        //System.out.println("aaaa= "+nodeId+" gggg = "+ properties);
	if (this.sim != null)
	    this.sim.setNodeProperties(nodeId, properties);
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

