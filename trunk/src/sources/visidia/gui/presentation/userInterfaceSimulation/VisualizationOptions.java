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
	algorithmOptions = new JMenu("Algorithm events ");
	algorithmOptions.getPopupMenu().setName("popAlgorithm");
	// Le menu pour visualiser les messages de synchronisation
	synchrOptions = new JMenu("Synchronization events");
	synchrOptions.getPopupMenu().setName("popSynchr");
	
	// Les cases a cocher pour les options de visualisation
	itemForAllAlgorithmMess =
	    new JCheckBoxMenuItem("All algorithm messages",true);
	itemForAllSynchrMess =
	    new JCheckBoxMenuItem("All synchronization messages",true);
	itemForAnyAlgorithmMess = 
	    new JCheckBoxMenuItem("No algorithm message");
	itemForAnySynchrMess = 
	    new JCheckBoxMenuItem("No synchronization message");
	
	// Ajout des cases a cocher 
	algorithmOptions.add(itemForAllAlgorithmMess);
	
	algorithmOptions.add(itemForAnyAlgorithmMess);
	
	synchrOptions.add(itemForAllSynchrMess);
	
	synchrOptions.add(itemForAnySynchrMess);
	
	
	this.add(algorithmOptions);
	this.add(synchrOptions);
	
	addReactions();
	reactionVisuOptions = new ReactionVisuOptions(this);
    }
    
    // Ajout des listeners 
    private void addReactions(){
	itemForAnySynchrMess.addActionListener(this);
	itemForAllSynchrMess.addActionListener(this);
	synchrOptions.addActionListener(this);
	
	
	itemForAllAlgorithmMess.addActionListener(this);
	itemForAnyAlgorithmMess.addActionListener(this);
	algorithmOptions.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent evt) {
	if(evt.getSource() instanceof JMenuItem)
	    reactionVisuOptions.action((JMenuItem)evt.getSource());
    }
    
    
    
    // Methodes 
    
    // Les Accesseurs 
    public JMenu getAlgorithmOptions(){
	return algorithmOptions;
    }

    public JMenu getSynchrOptions(){
	return synchrOptions;
    }
    public JCheckBoxMenuItem getItemForAllAlgorithmMess(){
	return itemForAllAlgorithmMess;
    }
    public JCheckBoxMenuItem getItemForAnyAlgorithmMess(){
	return itemForAnyAlgorithmMess;
    }

    public JCheckBoxMenuItem getItemForAllSynchrMess(){
	return itemForAllSynchrMess;
    }

    public JCheckBoxMenuItem getItemForAnySynchrMess(){
	return itemForAnySynchrMess;
    }


    public FenetreDeSimulation getFenetreDeSimulation(){
	return fenetreSim;
    }

    //Les attributs servant au menu 
    private JMenu algorithmOptions,synchrOptions;
    private JCheckBoxMenuItem itemForAllAlgorithmMess,itemForAllSynchrMess; 
    private JCheckBoxMenuItem itemForAnyAlgorithmMess,itemForAnySynchrMess;
    private FenetreDeSimulation fenetreSim;
    
    // Instance de la classe responsable des reactions
    private ReactionVisuOptions reactionVisuOptions;
    
}














