package visidia.gui.presentation.userInterfaceSimulation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
//import java.io.*;
//import visidia.gui.donnees.conteneurs.*;
import visidia.gui.presentation.*;
import visidia.simulation.*;
import visidia.gui.metier.simulation.SentMessage;
import visidia.gui.presentation.userInterfaceEdition.undo.UndoInfo;

public class SimulationPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
    
    /** Couleur de fond par defaut du grapheVisuPanel **/
    //private static final Color RECT_SELECTION_COULEUR = Color.gray;

    private FenetreDeSimulationDist fenetreDeSimulationDist;
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

    protected SelectionUnit selectionUnit;
    
    protected Vector sentMessageVector = new Vector(10,10);
    

    /**
     * cet objet sert a bloquer le SimulEventHandler en mode synchrone
     * en attendant la fin de la visualization des messages
     */
      
    private final Object waitObject = new Object();
    
    /**
     * Instancie un SimulationPanel associe a la fenetre de simulation 
     * passee en argument.
     **/
    public SimulationPanel(FenetreDeSimulation simulation) {
	fenetreDeSimulation = simulation;
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
			return fenetreDeSimulation.selection;
		     }
		     public UndoInfo getUndoInfo () throws NoSuchMethodException {
			 throw new NoSuchMethodException ("undo processing not used");
		     }
		     public RecoverableObject getRecoverableObject () {
			 return fenetreDeSimulation.getVueGraphe ();
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


     public SimulationPanel(FenetreDeSimulationDist simulation) {
	 fenetreDeSimulationDist = simulation;
	 objet_sous_souris = null;
	 lePas = PAS_PAR_DEFAUT;

	 if (simulation.getVueGraphe().getGraphe().ordre()!= 0) {  
	     size = simulation.getVueGraphe().donnerDimension();
	     this.setPreferredSize(size);
	     this.revalidate();		
	 } else {
	     size = new Dimension(0,0);
	 }

	 selectionUnit = new SelectionUnit
	     (new SelectionGetData () {
		     public SelectionDessin getSelectionDessin () {
			 return fenetreDeSimulationDist.selection;
		     }
		     public UndoInfo getUndoInfo () throws NoSuchMethodException {
			 throw new NoSuchMethodException ("undo processing not used");
		     }
		     public RecoverableObject getRecoverableObject () {
			 return fenetreDeSimulationDist.getVueGraphe ();
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
	 synchronized(sentMessageVector){
	     SentMessage sentMessage;
	     int size = sentMessageVector.size();
	     for(int i = 0; i < size; i++){
		 ((SentMessage) sentMessageVector.elementAt(i)).setStep(p);
	     }
	 }
     }


     /*** PROBLEME SEND TO ****/
     /**
      * Redessine les elements du graphique.
      **/
     public void paintComponent(Graphics g) {
	 super.paintComponent(g);

	 if (fenetreDeSimulationDist == null)
	     fenetreDeSimulation.getVueGraphe().dessiner(this,g);

	 else
	     fenetreDeSimulationDist.getVueGraphe().dessiner(this,g);

	 g.setColor(Color.red);
	 SentMessage sentMessage;
	 synchronized(sentMessageVector) {
	     int size = sentMessageVector.size();
	     for(int i = 0; i < size; i++){
		 sentMessage = (SentMessage) sentMessageVector.elementAt(i);
		 sentMessage.paint(g);
	     }
	 }

	 selectionUnit.drawSelection (g);
     }

     public void actionPerformed(ActionEvent e){

	 synchronized(sentMessageVector){
	     SentMessage sentMessage;
	     int size = sentMessageVector.size();
	     Vector tmpVect = new Vector(size);
	     //int i=size-1;
	     for(int i = 0; i < size; i++){
		 //for(int i= size-1; i >=0; i--){
		 sentMessage = (SentMessage) sentMessageVector.elementAt(i);
		 if(sentMessage.isIntoBounds()){
		     sentMessage.moveForward();
		     tmpVect.add(sentMessage);
		 }
		 else{
		     MessageSendingAck msa = new MessageSendingAck(sentMessage.getEvent().eventNumber());

		     //envoyer un message d'acquitement de fin d'animation
		     try{
			 if ( fenetreDeSimulationDist == null) {
			     fenetreDeSimulation.getAckPipe().put(msa);
			     //System.out.println("UN MESSAGE TRANSMIS");
			 }
                         else
			     fenetreDeSimulationDist.getAckPipe().put(msa);

		     }
		     catch(InterruptedException exp){
			 //this interruption should have been caused
			 //by the simulation stop.
			 return;
		     }
		     //supprimer le message de la liste des message a etre afficher
		     //sentMessageVector.removeElementAt(i);
		 }
	     }

	     if(tmpVect.size() == 0) {
		 try{
		     synchronized(waitObject) {
			 waitObject.notify();
		     }
		 } catch (Exception ex) {
		     ex.printStackTrace();
		     System.out.println("Bug ref 1 : SimulationPanel, a reporter en precisant\n les conditions");
		 }
	     }
	     sentMessageVector = tmpVect;
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
	 synchronized(sentMessageVector){
	     sentMessageVector = new Vector(10,10);
	 }
	 synchronized(waitObject) {
	     try{
		 waitObject.notifyAll();
	     } catch (Exception e) {
		 e.printStackTrace();
	     }
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
	 switch(evt.getKeyCode()) {
	     // Delete
	 case KeyEvent.VK_DELETE:
	 case KeyEvent.VK_BACK_SPACE:
	     if ( fenetreDeSimulationDist == null )
		 fenetreDeSimulation.commandeSupprimer();
	     else 
		 fenetreDeSimulationDist.commandeSupprimer();
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


     public void nextPulseReady() {
	 // manque le marquage graphique du pulse en cours

	 // attendre la fin de la visualization des messages du pulse en cours
	 // et envoyer un acquitement
	 // synchronized(sentMessageVector){
	 if(sentMessageVector.size() != 0) {
	     try{
		 synchronized(waitObject) {
		     //System.out.println("Bloque");
		     waitObject.wait();
		     //System.out.println("Debloque");
		 }
		 //sentMessageVector.wait();
	     } catch (Exception e) {
		 System.out.println("bug ref 0 : Simulation Panel, a reporter en precisant les conditions");
		 e.printStackTrace();
	     }
	 }
     }

    public void animate(MessageSendingEvent mse){
	if ( fenetreDeSimulationDist == null) {
	    synchronized(sentMessageVector){
		SentMessage sentMessage = new SentMessage(mse, fenetreDeSimulation.getVueGraphe().rechercherSommet(mse.sender().toString()).centre(), fenetreDeSimulation.getVueGraphe().rechercherSommet(mse.receiver().toString()).centre(), lePas);
		sentMessageVector.add(sentMessage);
	    }
	}
        else {
	    synchronized(sentMessageVector){
		SentMessage sentMessage = new SentMessage(mse, fenetreDeSimulationDist.getVueGraphe().rechercherSommet(mse.sender().toString()).centre(), fenetreDeSimulationDist.getVueGraphe().rechercherSommet(mse.receiver().toString()).centre(), lePas);
		sentMessageVector.add(sentMessage);
	    }
	}

    }

}










