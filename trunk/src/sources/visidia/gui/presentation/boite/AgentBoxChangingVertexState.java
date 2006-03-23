package visidia.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import visidia.gui.presentation.userInterfaceSimulation.*;
import visidia.gui.donnees.conteneurs.*;
import visidia.gui.presentation.*;
import visidia.gui.donnees.*;

import visidia.gui.presentation.userInterfaceEdition.Fenetre;
import javax.swing.event.*;

/**
 * Cette classe cree une boite utilisee pour modifier l'etat d'un    
 * ou de plusieurs sommets selectionne elle est appelee quand on ne    
 * selectionne que des sommets et qu'on appui sur le bouton info     
*/
public class AgentBoxChangingVertexState
    extends DefaultBoxVertex
    implements ActionListener, ItemListener, VueEtatPanel
{

    //Constructeurs

    /**
     * Cree une nouvelle boite pour afficher les caractéristiques de
     * "un_objet".  Ces caractéristiques seront modifiables.
     */
    public AgentBoxChangingVertexState(AgentsSimulationWindow parent, SommetDessin sommet) {
	this(parent, sommet, "Vertex properties state");
    }
    
    /**
     * Cree une nouvelle boite appelee "titre" pour afficher les
     * caracteristiques de "un_objet".
     */
    public AgentBoxChangingVertexState(AgentsSimulationWindow parent, SommetDessin sommet, String titre) {
       
    this.dialog = new JDialog(parent, titre);
    this.parent = parent;

    monSommet = sommet;
  
    vertex_id = Integer.valueOf(sommet.getEtiquette()).intValue();
    
    etatPanel = new EtatPanel(TableCouleurs.getTableCouleurs(),this);
    
    Panel panelHaut = new Panel();
    panelHaut.setLayout(new BorderLayout());
    panelHaut.add(etatPanel, BorderLayout.NORTH);

    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane spane = new JScrollPane(table);

    JPanel panelCentre = new JPanel();
    panelCentre.setLayout(new BorderLayout());
    panelCentre.add(spane, BorderLayout.NORTH);
    
        
    tbModel = new AgentPropertyTableModel(monSommet.getWhiteBoardTable(),
                                          parent.getDefaultProperties());
    
    table.setModel(tbModel);

    dialog.getContentPane().setLayout(new BorderLayout());
    dialog.getContentPane().add(panelHaut, BorderLayout.NORTH);
    dialog.getContentPane().add(panelCentre, BorderLayout.CENTER);
    dialog.setSize(400,200);
    
    ajouterBoutons();

    }

    //Methodes  
    
    public void updateBox() {
        ((AgentPropertyTableModel) tbModel).updateKeys();
        tbModel.fireTableDataChanged();
    }
  
   
      
    /** Cette methode est appelee si l'utilisateur appuie sur le bouton Ok.*/
    public void buttonOk() {
	String etat = etatPanel.ardoise().donneEtat();
	AgentPropertyTableModel mod =(AgentPropertyTableModel)table.getModel();
	int nbRows = mod.getRowCount();
	monSommet.setEtat(etat);

	try{
	    for (int i=0;i<nbRows;i++){
		table.editCellAt(i,1); // read the new values edited
		monSommet.setValue((String)mod.getValueAt(i,0),mod.getValueAt(i,1));
	    }
	}catch(Exception exc){System.out.println(" Problem in Box : "+exc);}
    
	parent.simulationPanel().repaint();
    }


}

