package visidia.gui.presentation.userInterfaceSimulation;

import java.awt.event.*;
import javax.swing.*;

/* Cette classe permet l'ajout graphique du menu des options de visualisation
a la fenetre de visualisation */

public final class VisualizationOptions extends JMenu implements ActionListener {
  
    /**
	 * 
	 */
	private static final long serialVersionUID = -1800693958433731771L;

	// Constructeurs 
    public VisualizationOptions(FenetreDeSimulation fenetreSim){
	
	super("Visualize_Events_With");
	//	this.fenetreSim = fenetreSim;
	this.getPopupMenu().setName("PopOptions");
	this.setMnemonic('V');
	// Le menu pour visualiser les messages de l'algorithme 
	this.algorithmOptions = new JMenu("Algorithm events ");
	this.algorithmOptions.getPopupMenu().setName("popAlgorithm");
	// Le menu pour visualiser les messages de synchronisation
	this.synchrOptions = new JMenu("Synchronization events");
	this.synchrOptions.getPopupMenu().setName("popSynchr");
	
	// Les cases a cocher pour les options de visualisation
	this.itemForAllAlgorithmMess =
	    new JCheckBoxMenuItem("All algorithm messages",true);
	this.itemForAllSynchrMess =
	    new JCheckBoxMenuItem("All synchronization messages",true);
	this.itemForAnyAlgorithmMess = 
	    new JCheckBoxMenuItem("No algorithm message");
	this.itemForAnySynchrMess = 
	    new JCheckBoxMenuItem("No synchronization message");
	
	// Ajout des cases a cocher 
	this.algorithmOptions.add(this.itemForAllAlgorithmMess);
	
	this.algorithmOptions.add(this.itemForAnyAlgorithmMess);
	
	this.synchrOptions.add(this.itemForAllSynchrMess);
	
	this.synchrOptions.add(this.itemForAnySynchrMess);
	
	
	this.add(this.algorithmOptions);
	this.add(this.synchrOptions);
	
	this.addReactions();
	this.reactionVisuOptions = new ReactionVisuOptions(this);
    }
    
    // Ajout des listeners 
    private void addReactions(){
	this.itemForAnySynchrMess.addActionListener(this);
	this.itemForAllSynchrMess.addActionListener(this);
	this.synchrOptions.addActionListener(this);
	
	
	this.itemForAllAlgorithmMess.addActionListener(this);
	this.itemForAnyAlgorithmMess.addActionListener(this);
	this.algorithmOptions.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent evt) {
	if(evt.getSource() instanceof JMenuItem)
	    this.reactionVisuOptions.action((JMenuItem)evt.getSource());
    }
    
    
    
    // Methodes 
    
    // Les Accesseurs 
    public JMenu getAlgorithmOptions(){
	return this.algorithmOptions;
    }

    public JMenu getSynchrOptions(){
	return this.synchrOptions;
    }
    public JCheckBoxMenuItem getItemForAllAlgorithmMess(){
	return this.itemForAllAlgorithmMess;
    }
    public JCheckBoxMenuItem getItemForAnyAlgorithmMess(){
	return this.itemForAnyAlgorithmMess;
    }

    public JCheckBoxMenuItem getItemForAllSynchrMess(){
	return this.itemForAllSynchrMess;
    }

    public JCheckBoxMenuItem getItemForAnySynchrMess(){
	return this.itemForAnySynchrMess;
    }


    public FenetreDeSimulation getFenetreDeSimulation(){
	return this.fenetreSim;
    }

    //Les attributs servant au menu 
    private JMenu algorithmOptions,synchrOptions;
    private JCheckBoxMenuItem itemForAllAlgorithmMess,itemForAllSynchrMess; 
    private JCheckBoxMenuItem itemForAnyAlgorithmMess,itemForAnySynchrMess;
    private FenetreDeSimulation fenetreSim;
    
    // Instance de la classe responsable des reactions
    private ReactionVisuOptions reactionVisuOptions;
    
}














