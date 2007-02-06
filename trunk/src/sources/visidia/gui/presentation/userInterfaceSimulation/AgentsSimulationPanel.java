package visidia.gui.presentation.userInterfaceSimulation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import visidia.gui.presentation.*;
import visidia.simulation.*;
import visidia.gui.metier.simulation.SentAgent;
import visidia.gui.presentation.userInterfaceEdition.undo.UndoInfo;

public class AgentsSimulationPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 6633924693735717321L;
	/** Couleur de fond par defaut du grapheVisuPanel **/
    private AgentsSimulationWindow agentsSimulationWindow;
    protected FormeDessin objet_sous_souris ;
    protected int PAS_PAR_DEFAUT = 10 ; 

    /**
     * le pas de visulaisation : vitesse de visulaisation. Bilel.
     **/
    protected int lePas;

    protected javax.swing.Timer timer ;

    
    protected int x_ancien ;
    protected int y_ancien ;
    int current_x = 0;
    int current_y = 0;
    /** Dimension du graphique */
    protected Dimension size;

    protected SelectionUnit selectionUnit;
    

    protected Vector sentAgentVector = new Vector(10,10);
    

    /**
     * Cet objet sert à avoir un accès concurrent aux évènements à
     * visualiser. Bilel.
     *
     */      
    private final Object concurrentObject = new Object();
    
    /**
     * Instancie un SimulationPanel associe a la fenetre de simulation 
     * passee en argument.
     **/
  
    public AgentsSimulationPanel(AgentsSimulationWindow simulation) {
	this.agentsSimulationWindow = simulation;
	this.objet_sous_souris = null;
	this.lePas = this.PAS_PAR_DEFAUT;
	
	if(simulation.getVueGraphe().getGraphe().ordre()!= 0)
	    {  
		this.size = simulation.getVueGraphe().donnerDimension();
		this.setPreferredSize(this.size);
		this.revalidate();		
	    } else {
	    this.size = new Dimension(0,0);
	}

	this.selectionUnit = new SelectionUnit
	    (new SelectionGetData () {
		    public SelectionDessin getSelectionDessin () {
			return AgentsSimulationPanel.this.agentsSimulationWindow.selection;
		    }
		    public UndoInfo getUndoInfo () throws NoSuchMethodException {
			throw new NoSuchMethodException ("undo processing not used");
		    }
		    public RecoverableObject getRecoverableObject () {
			return AgentsSimulationPanel.this.agentsSimulationWindow.getVueGraphe ();
		    }
		},
	     this);

	this.addMouseListener(this.selectionUnit);
	this.addMouseListener(this);
	this.addMouseMotionListener(this.selectionUnit);
	this.addMouseMotionListener(this);
	this.addKeyListener(this);

	this.timer = new javax.swing.Timer(30,this);
	this.setBackground(new Color(0xe6e6fa));
    }

    public int pas(){
	return this.lePas;
    }
    
    public void updatePas(int p)
    {
	this.lePas = p;
	synchronized(this.concurrentObject){
	    //SentAgent sentAgent;
	    int size = this.sentAgentVector.size();
	    for(int i = 0; i < size; i++){
		((SentAgent) this.sentAgentVector.elementAt(i)).setStep(p);
	    }
	}
    }
    

    /*** PROBLEME SEND TO ****/ /** qui a mis ce truc **/
    /**
     * Redessine les elements du graphique.
     **/
    public void paintComponent(Graphics g) {
	super.paintComponent(g);

	this.agentsSimulationWindow.getVueGraphe().dessiner(this,g,"agents");

	g.setColor(Color.red);
	
	synchronized(this.concurrentObject) {
	    SentAgent sentAgent;
	    int size = this.sentAgentVector.size();
	    for(int i = 0; i < size; i++){
		sentAgent = (SentAgent) this.sentAgentVector.elementAt(i);
		sentAgent.paint(g);
	    }
	}

	this.selectionUnit.drawSelection (g);
    }

    public void actionPerformed(ActionEvent e){

	synchronized(this.concurrentObject){
	    SentAgent sentAgent;
	    int size = this.sentAgentVector.size();
	    Vector tmpVect = new Vector(size);
	    for(int i = 0; i < size; i++){
		sentAgent = (SentAgent) this.sentAgentVector.elementAt(i);
		if(sentAgent.isIntoBounds()){
		    sentAgent.moveForward();
		    tmpVect.add(sentAgent);
		}
		else{
		    MessageSendingAck msa = new MessageSendingAck(sentAgent.getEvent().eventNumber());

		    //envoyer un message d'acquitement de fin d'animation
		    try{
			this.agentsSimulationWindow.getAckPipe().put(msa);
		    }
		    catch(InterruptedException exp){
			exp.printStackTrace();
			return;
		    }
		}
	    }
	    this.sentAgentVector = tmpVect;
	}
	this.repaint();
    }

    public boolean isRunning(){
	if(this.timer.isRunning())
	    return true;
	else 
	    return false; 
    } 

    public void start(){
	this.timer.start();
    }

    public void pause(){
	this.timer.stop();
    }

    public void stop(){
	this.timer.stop();
	synchronized(this.concurrentObject){
	    this.sentAgentVector = new Vector(10,10);
	}
    }

    public void mousePressed(MouseEvent evt) {}
    public void mouseClicked(MouseEvent evt) {}
    public void mouseMoved(MouseEvent evt) {}
    public void mouseReleased(MouseEvent evt) {}
    public void mouseDragged(MouseEvent evt) {}
    public void mouseEntered(MouseEvent evt) {}

    public void mouseExited(MouseEvent evt) {}

    /**
     * Implementation de KeyListener.
     **/
    public void keyPressed(KeyEvent evt) {
    }

    public void keyReleased(KeyEvent evt) {}

    public void keyTyped(KeyEvent evt) {}

    /* Fin d'implementation de KeyListener */

    public Dimension getMinimumSize() {
	return new Dimension(20, 20);
    }

    public void animate(MessageSendingEvent mse){

	synchronized(this.concurrentObject){
	    SentAgent sentAgent;

	    sentAgent = new
		SentAgent(mse,
			  this.agentsSimulationWindow.getVueGraphe()
			  .rechercherSommet(mse.sender().toString()).centre(),
			  this.agentsSimulationWindow.getVueGraphe().
			  rechercherSommet(mse.receiver().toString()).centre(), this.lePas);
	    this.sentAgentVector.add(sentAgent);

	}
    }
}
