package visidia.gui.presentation.userInterfaceSimulation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import visidia.gui.donnees.conteneurs.*;
import visidia.gui.presentation.*;
import visidia.simulation.*;
import visidia.gui.metier.simulation.SentAgent;
import visidia.gui.presentation.userInterfaceEdition.undo.UndoInfo;

public class AgentsSimulationPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
    
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
	agentsSimulationWindow = simulation;
	objet_sous_souris = null;
	lePas = PAS_PAR_DEFAUT;
	
	if(simulation.getVueGraphe().getGraphe().ordre()!= 0)
	    {  
		size = simulation.getVueGraphe().donnerDimension();
		this.setPreferredSize(size);
		this.revalidate();		
	    } else {
	    size = new Dimension(0,0);
	}

	selectionUnit = new SelectionUnit
	    (new SelectionGetData () {
		    public SelectionDessin getSelectionDessin () {
			return agentsSimulationWindow.selection;
		    }
		    public UndoInfo getUndoInfo () throws NoSuchMethodException {
			throw new NoSuchMethodException ("undo processing not used");
		    }
		    public RecoverableObject getRecoverableObject () {
			return agentsSimulationWindow.getVueGraphe ();
		    }
		},
	     (JPanel) this);

	addMouseListener(selectionUnit);
	addMouseListener(this);
	addMouseMotionListener(selectionUnit);
	addMouseMotionListener(this);
	addKeyListener(this);

	timer = new javax.swing.Timer(30,(ActionListener)this);
	setBackground(new Color(0xe6e6fa));
    }

    public int pas(){
	return lePas;
    }
    
    public void updatePas(int p)
    {
	this.lePas = p;
	synchronized(concurrentObject){
	    SentAgent sentAgent;
	    int size = sentAgentVector.size();
	    for(int i = 0; i < size; i++){
		((SentAgent) sentAgentVector.elementAt(i)).setStep(p);
	    }
	}
    }
    

    /*** PROBLEME SEND TO ****/ /** qui a mis ce truc **/
    /**
     * Redessine les elements du graphique.
     **/
    public void paintComponent(Graphics g) {
	super.paintComponent(g);

	agentsSimulationWindow.getVueGraphe().dessiner(this,g,"agents");

	g.setColor(Color.red);
	
	synchronized(concurrentObject) {
	    SentAgent sentAgent;
	    int size = sentAgentVector.size();
	    for(int i = 0; i < size; i++){
		sentAgent = (SentAgent) sentAgentVector.elementAt(i);
		sentAgent.paint(g);
	    }
	}

	selectionUnit.drawSelection (g);
    }

    public void actionPerformed(ActionEvent e){

	synchronized(concurrentObject){
	    SentAgent sentAgent;
	    int size = sentAgentVector.size();
	    Vector tmpVect = new Vector(size);
	    for(int i = 0; i < size; i++){
		sentAgent = (SentAgent) sentAgentVector.elementAt(i);
		if(sentAgent.isIntoBounds()){
		    sentAgent.moveForward();
		    tmpVect.add(sentAgent);
		}
		else{
		    MessageSendingAck msa = new MessageSendingAck(sentAgent.getEvent().eventNumber());

		    //envoyer un message d'acquitement de fin d'animation
		    try{
			agentsSimulationWindow.getAckPipe().put(msa);
		    }
		    catch(InterruptedException exp){
			exp.printStackTrace();
			return;
		    }
		}
	    }
	    sentAgentVector = tmpVect;
	}
	repaint();
    }

    public boolean isRunning(){
	if(timer.isRunning())
	    return true;
	else 
	    return false; 
    } 

    public void start(){
	timer.start();
    }

    public void pause(){
	timer.stop();
    }

    public void stop(){
	timer.stop();
	synchronized(concurrentObject){
	    sentAgentVector = new Vector(10,10);
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

	synchronized(concurrentObject){
	    SentAgent sentAgent;

	    sentAgent = new
		SentAgent(mse,
			  agentsSimulationWindow.getVueGraphe()
			  .rechercherSommet(mse.sender().toString()).centre(),
			  agentsSimulationWindow.getVueGraphe().
			  rechercherSommet(mse.receiver().toString()).centre(), lePas);
	    sentAgentVector.add(sentAgent);

	}
    }
}
