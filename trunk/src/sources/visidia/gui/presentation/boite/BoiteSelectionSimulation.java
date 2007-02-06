package visidia.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import visidia.gui.presentation.userInterfaceSimulation.*;
import visidia.gui.presentation.*;
import visidia.gui.donnees.conteneurs.*;

/** Cette boite affiche les caracteristiques d'une sélection
 * d'éléments du graphe courant.*/
public class BoiteSelectionSimulation extends BoiteSelection {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -6339618893162291937L;
	SelectionDessin selection;

    public BoiteSelectionSimulation(FenetreDeSimulation parent, 
				    SelectionDessin selection, 
				    MultiEnsemble table_types) {
	super(parent, selection.nbElements (), table_types);
	this.selection = selection;
    }
    
    public static void show(FenetreDeSimulation parent, SelectionDessin selection, 
			    MultiEnsemble table_types) {
	BoiteSelection b = new BoiteSelectionSimulation(parent, selection, table_types);
	b.showDialog();
    }

    public JComponent createContent() {
	
	JComponent mainPane = super.createContent();
	
	//JPanel firstPane = new JPanel ();
	JPanel secPane = new JPanel ();
	
	mainPane.setLayout(new GridLayout(2, 1));
	//mainPane.add(firstPane);
	mainPane.add(secPane);
      
	//BoxLayout firstLayout = new BoxLayout(firstPane, BoxLayout.Y_AXIS);
	BoxLayout secLayout = new BoxLayout(secPane, BoxLayout.Y_AXIS);
	secPane.setLayout(secLayout);
	
	secPane.setBorder(BorderFactory.createTitledBorder
			  (BorderFactory.createEtchedBorder(),
			   "Drawing messages properties"));
	
	boolean vertexSelected = false;
	Enumeration tous_les_types = table_types.elements();
	while(tous_les_types.hasMoreElements()) {
	    String un_type = (String)tous_les_types.nextElement();
	    if (un_type.equals ("vertex"))
		vertexSelected = true;
	}
	final JCheckBox drawMessageCheckBox = new JCheckBox("Draw messages");
	secPane.add(drawMessageCheckBox);
	drawMessageCheckBox.setEnabled(vertexSelected);
	
	JButton applyButton = new JButton("Apply");
	JPanel applyPane = new JPanel (new FlowLayout (FlowLayout.CENTER));
	applyPane.add(applyButton);
	applyButton.addMouseListener (new MouseAdapter() {
		public void mouseClicked(MouseEvent event) {
		    boolean isSelected = drawMessageCheckBox.isSelected();
		    Enumeration e = selection.elements();
		    while (e.hasMoreElements()) {
			FormeDessin element = ((FormeDessin) e.nextElement());
			if (element.type().equals("vertex")) {
			    SommetDessin vertex = (SommetDessin) element;
			    int id = new Integer (vertex.getEtiquette()).intValue ();
			    Hashtable prop = vertex.getStateTable();
			    prop.put ("draw messages", isSelected ? "yes" : "no");
			    ((FenetreDeSimulation) parent).nodeStateChanged
				(id, (Hashtable) prop.clone()); 
			    vertex.setDrawMessage(isSelected);
			}
		    }
		}
	    });
	secPane.add(applyPane);
	
	return mainPane;
    }
}


