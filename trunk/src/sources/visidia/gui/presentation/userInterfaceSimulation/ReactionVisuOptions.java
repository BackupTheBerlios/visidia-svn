package visidia.gui.presentation.userInterfaceSimulation;

import javax.swing.*;

// Cette clase permet la reaction des cases a cocher 

public class ReactionVisuOptions{ 
    public ReactionVisuOptions(VisualizationOptions visuOptions){
	this.visuOptions = visuOptions;
    }
    
    // L'action a realiser quand on clique sur un des menus 
    public void action(JMenuItem mi){	
	String le_menu = ((JPopupMenu)mi.getParent()).getName();
	if(le_menu == this.visuOptions.getAlgorithmOptions().getPopupMenu().getName())
	    this.actionAlgorithm(mi);
	if(le_menu == this.visuOptions.getSynchrOptions().getPopupMenu().getName())
	    this.actionSynchr(mi);
    }
    
    //L'action a realiser si c'est le menu pour l'algorithme 
    public void actionAlgorithm(JMenuItem mi){
	if(mi == this.visuOptions.getItemForAllAlgorithmMess()){
	    if(this.visuOptions.getItemForAllAlgorithmMess().getState()){
		this.visuOptions.getItemForAnyAlgorithmMess().setState(false);
		FenetreDeSimulation.setVisuAlgorithmMess(true);
	    }
	    else{
		this.visuOptions.getItemForAnyAlgorithmMess().setState(true);
		FenetreDeSimulation.setVisuAlgorithmMess(false);
	    }
		
	}
	if(mi == this.visuOptions.getItemForAnyAlgorithmMess()){
	    if(this.visuOptions.getItemForAnyAlgorithmMess().getState()){
		this.visuOptions.getItemForAllAlgorithmMess().setState(false);
		FenetreDeSimulation.setVisuAlgorithmMess(false);
	    }
	    else{
		this.visuOptions.getItemForAllAlgorithmMess().setState(true);
		FenetreDeSimulation.setVisuAlgorithmMess(true);
	    }
	} 
    }
    
    // L'action pour la synchronisation 
    public void actionSynchr(JMenuItem mi){
	if(mi == this.visuOptions.getItemForAllSynchrMess()){
	    if(this.visuOptions.getItemForAllSynchrMess().getState()){
		this.visuOptions.getItemForAnySynchrMess().setState(false);
		FenetreDeSimulation.setVisuSynchrMess(true);
	    }
	    else{
		this.visuOptions.getItemForAnyAlgorithmMess().setState(true);
		FenetreDeSimulation.setVisuSynchrMess(false);
	    }
	}
	if(mi == this.visuOptions.getItemForAnySynchrMess()){	
	    if(this.visuOptions.getItemForAnySynchrMess().getState()){
		this.visuOptions.getItemForAllSynchrMess().setState(false);
		FenetreDeSimulation.setVisuSynchrMess(false);
	    }
	    else{	
		this.visuOptions.getItemForAllSynchrMess().setState(true);
		FenetreDeSimulation.setVisuSynchrMess(true);
	    } 
	}
	
    }
    public VisualizationOptions getVisuOptions(){
	return this.visuOptions;
    }
    // Une instance de la classe responsable de la visualisation
    private VisualizationOptions visuOptions;    
    
}











