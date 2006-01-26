package fr.enserb.das.gui.presentation.userInterfaceSimulation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import fr.enserb.das.gui.donnees.conteneurs.*;
import fr.enserb.das.gui.presentation.*;
import fr.enserb.das.simulation.*;
import fr.enserb.das.gui.metier.simulation.SentMessage;
import fr.enserb.das.misc.*;



public class SimulationPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
    
    /** Couleur de fond par defaut du grapheVisuPanel **/
    private static final Color RECT_SELECTION_COULEUR = Color.gray;
    
    private FenetreDeSimulation fenetreDeSimulation;
    protected FormeDessin objet_sous_souris ;
    protected int PAS_PAR_DEFAUT = 10 ; 
    protected int lePas;
    protected javax.swing.Timer timer ;
    protected int x_ancien ;
    protected int y_ancien ;
    int current_x = 0;
    int current_y = 0;
    /** Dimension du graphique */
    protected Dimension size;
    
    protected Vector sentMessageVector = new Vector(10,10);
    
    
    /**
     * Instancie un SimulationPanel associé à la fenetre de simulation 
     * passée en argument.
     **/
     public SimulationPanel(FenetreDeSimulation simulation) {
	fenetreDeSimulation = simulation;
	objet_sous_souris = null;
	lePas = PAS_PAR_DEFAUT;
	this.addMouseListener(this);
	this.addMouseMotionListener(this);
	this.addKeyListener(this);
	if(simulation.getVueGraphe().getGraphe().ordre()!= 0)
	    {  
		size = simulation.getVueGraphe().donnerDimension();
		this.setPreferredSize(size);
		this.revalidate();		
	    }
	else size = new Dimension(0,0);
	timer = new javax.swing.Timer(30,(ActionListener)this);
	setBackground(new Color(0xe6e6fa));
    }
    
    public int pas(){
	return lePas;
    }
    
    public void updatePas(int p)
    {
	this.lePas = p;
	synchronized(sentMessageVector){
	    SentMessage sentMessage;
	    int size = sentMessageVector.size();
	    for(int i = 0; i < size; i++){
		((SentMessage) sentMessageVector.elementAt(i)).setStep(p);
	    }
	}
    }
    
    /**
     * Redéssine les éléments du graphique.
     **/
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	
	fenetreDeSimulation.getVueGraphe().dessiner(this,g);
	g.setColor(Color.red);
	SentMessage sentMessage;
	int size = sentMessageVector.size();
	for(int i = 0; i < size; i++){
	    sentMessage = (SentMessage) sentMessageVector.elementAt(i);
	    sentMessage.paint(g);
	}
    }
 
    public void mousePressed(MouseEvent evt) {
	int x = evt.getX();
	int y = evt.getY();
	size = this.getPreferredSize();
	
	switch(evt.getModifiers()) {
	    // Bouton droit
	case InputEvent.BUTTON3_MASK:
	    appuiBoutonDroit(x, y);
	    break;
	    
	    // shift + bouton droit
	case (InputEvent.BUTTON3_MASK | InputEvent.SHIFT_MASK):
	    appuiShiftBoutonDroit(x, y);
	    break;
	    
 	default:
	    break;
	}
    }
    /**
     * L'appui du Bouton droit de la souris permet de sélectionner un objet
     **/
    public void appuiBoutonDroit(int x, int y) {
	// Remise a zero de la selection
	x_ancien = x;
	y_ancien = y;
	try {
	    objet_sous_souris = fenetreDeSimulation.getVueGraphe().en_dessous(x, y);
	    
	    if (!fenetreDeSimulation.selection().estVide()) {
		fenetreDeSimulation.emptyCurrentSelection(true);
	    }
	    //objet_sous_souris.enluminer(true);
	     fenetreDeSimulation.selection().insererElement(objet_sous_souris);
	    repaint();
	} catch(NoSuchElementException e) {
	    // On vide la selection
	    if(!fenetreDeSimulation.selection().estVide()) {
		fenetreDeSimulation.emptyCurrentSelection(true);
		repaint(0);
	    }
	   }
	
    }
    

    /**
     * L'appui de Shift + bouton droit permet la sélection additive.
     **/
    public void appuiShiftBoutonDroit(int x, int y) {
	x_ancien = x;
	y_ancien = y;
	try {
	    objet_sous_souris = fenetreDeSimulation.getVueGraphe().en_dessous(x, y);
	    if (fenetreDeSimulation.selection().contient(objet_sous_souris)) {
		fenetreDeSimulation.selection().supprimerElement(objet_sous_souris);
		//objet_sous_souris.enluminer(false);
	    } else {
		fenetreDeSimulation.selection().insererElement(objet_sous_souris);
		//objet_sous_souris.enluminer(true);
	    }
	    repaint();
	} catch (NoSuchElementException e) {
	    
	}
  
    }
        
    public void actionPerformed(ActionEvent e){
	synchronized(sentMessageVector){
	    SentMessage sentMessage;
	    Message msg ;
	    int size = sentMessageVector.size();
	    Vector tmpVect = new Vector(size);
	    
	    for(int i = 0; i < size; i++){
		sentMessage = (SentMessage) sentMessageVector.elementAt(i);	
		if(sentMessage.isIntoBounds()){ 
		    msg = sentMessage.getEvent().message();
		    if(msg instanceof SyncMessage){
			if(!FenetreDeSimulation.visuSynchrMess){
			    sentAck(sentMessage);
			}
			else{
			    sentMessage.moveForward();
			    tmpVect.add(sentMessage);
			}
		    }
		    else{ 
			if(!FenetreDeSimulation.visuAlgorithmMess){
			    sentAck(sentMessage);
			}
			else{
			    sentMessage.moveForward();
			    tmpVect.add(sentMessage);
			}
		    }
		}
		else{
		    sentAck(sentMessage);
		}
	    }
	    sentMessageVector = tmpVect;
	}
	repaint();
    }
    
    public void sentAck(SentMessage sentMessage){
	MessageSendingAck msa = new MessageSendingAck(sentMessage.getEvent().eventNumber());
	//envoyer un message d'acquittement de fin d'animation
	try{
	    fenetreDeSimulation.getAckPipe().put(msa);			
	}
	catch(InterruptedException exp){
	    //this interruption should have been caused
	    //by the simulation stop.
	    return;
	}
	
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
	synchronized(sentMessageVector){
	sentMessageVector = new Vector(10,10);
	}
    }
    
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
	switch(evt.getKeyCode()) {
	    // Delete
	case KeyEvent.VK_DELETE:
	case KeyEvent.VK_BACK_SPACE:
	    fenetreDeSimulation.commandeSupprimer();
	    repaint();
	    break;
	default:
	}
    }

    public void keyReleased(KeyEvent evt) {}
    
    public void keyTyped(KeyEvent evt) {}
    
    /* Fin d'implementation de KeyListener */
    
    public Dimension getMinimumSize() {
	return new Dimension(20, 20);
    }

    public void animate(MessageSendingEvent mse){
	synchronized(sentMessageVector){
	    SentMessage sentMessage = new SentMessage(mse, 
						      fenetreDeSimulation.getVueGraphe().rechercherSommet(mse.sender().toString()).centre(),
						      fenetreDeSimulation.getVueGraphe().rechercherSommet(mse.receiver().toString()).centre(),
						      lePas);
	    sentMessageVector.add(sentMessage);
	}
    }
}








