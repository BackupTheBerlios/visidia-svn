package visidia.gui.presentation.userInterfaceSimulation;


//import java.awt.*;
import javax.swing.*;

// Cette clase permet la reaction des cases a cocher 

public class ReactionVisuOptions{ 
    public ReactionVisuOptions(VisualizationOptions visuOptions){
	this.visuOptions = visuOptions;
    }
    
    // L'action a realiser quand on clique sur un des menus 
    public void action(JMenuItem mi){	
	String le_menu = ((JPopupMenu)mi.getParent()).getName();
	if(le_menu == visuOptions.getAlgorithmOptions().getPopupMenu().getName())
	    actionAlgorithm(mi);
	if(le_menu == visuOptions.getSynchrOptions().getPopupMenu().getName())
	    actionSynchr(mi);
    }
    
    //L'action a realiser si c'est le menu pour l'algorithme 
    public void actionAlgorithm(JMenuItem mi){
	if(mi == visuOptions.getItemForAllAlgorithmMess()){
	    if(visuOptions.getItemForAllAlgorithmMess().getState()){
		visuOptions.getItemForAnyAlgorithmMess().setState(false);
		FenetreDeSimulation.setVisuAlgorithmMess(true);
	    }
	    else{
		visuOptions.getItemForAnyAlgorithmMess().setState(true);
		FenetreDeSimulation.setVisuAlgorithmMess(false);
	    }
		
	}
	if(mi == visuOptions.getItemForAnyAlgorithmMess()){
	    if(visuOptions.getItemForAnyAlgorithmMess().getState()){
		visuOptions.getItemForAllAlgorithmMess().setState(false);
		FenetreDeSimulation.setVisuAlgorithmMess(false);
	    }
	    else{
		visuOptions.getItemForAllAlgorithmMess().setState(true);
		FenetreDeSimulation.setVisuAlgorithmMess(true);
	    }
	} 
    }
    
    // L'action pour la synchronisation 
    public void actionSynchr(JMenuItem mi){
	if(mi == visuOptions.getItemForAllSynchrMess()){
	    if(visuOptions.getItemForAllSynchrMess().getState()){
		visuOptions.getItemForAnySynchrMess().setState(false);
		FenetreDeSimulation.setVisuSynchrMess(true);
	    }
	    else{
		visuOptions.getItemForAnyAlgorithmMess().setState(true);
		FenetreDeSimulation.setVisuSynchrMess(false);
	    }
	}
	if(mi == visuOptions.getItemForAnySynchrMess()){	
	    if(visuOptions.getItemForAnySynchrMess().getState()){
		visuOptions.getItemForAllSynchrMess().setState(false);
		FenetreDeSimulation.setVisuSynchrMess(false);
	    }
	    else{	
		visuOptions.getItemForAllSynchrMess().setState(true);
		FenetreDeSimulation.setVisuSynchrMess(true);
	    } 
	}
	
    }
    public VisualizationOptions getVisuOptions(){
	return visuOptions;
    }
    // Une instance de la classe responsable de la visualisation
    private VisualizationOptions visuOptions;    
    
}











