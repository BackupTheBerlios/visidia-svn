package visidia.gui.presentation.boite;

import visidia.gui.presentation.userInterfaceSimulation.*;
import visidia.gui.presentation.*;
import visidia.gui.donnees.conteneurs.*;

/** Cette boite affiche les caracteristiques d'une sélection
 * d'éléments du graphe courant.*/
public class AgentBoxSimulationSelection extends BoiteSelection {
    
    SelectionDessin selection;

    public AgentBoxSimulationSelection(AgentsSimulationWindow parent, 
				    SelectionDessin selection, 
				    MultiEnsemble table_types) {
	super(parent, selection.nbElements (), table_types);
	this.selection = selection;
    }
    
    public static void show(AgentsSimulationWindow parent, SelectionDessin selection, 
			    MultiEnsemble table_types) {
	BoiteSelection b = new AgentBoxSimulationSelection(parent, selection, table_types);
	b.showDialog();
    }

}


