package visidia.gui.presentation.boite;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import visidia.graph.SimpleGraphVertex;
import visidia.gui.donnees.VertexPropertyTableModel;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.userInterfaceSimulation.AgentsSimulationWindow;

/**
 * Cette classe cree une boite utilisee pour modifier l'etat d'un ou de
 * plusieurs sommets selectionne elle est appelee quand on ne selectionne que
 * des sommets et qu'on appui sur le bouton info
 */
public class AgentBoxChangingVertexState extends AbstractDefaultBox implements
		ActionListener, ItemListener, VueEtatPanel {

	// Constructeurs
	SommetDessin vert;

	public AgentBoxChangingVertexState(AgentsSimulationWindow parent,
			SommetDessin s, Hashtable def) {
		this(parent, s, def, "Specifics vertex properties for vertex "
				+ s.getEtiquette());
	}

	/**
	 * Cree une nouvelle boite appelee "titre" pour afficher les
	 * caracteristiques de "un_objet".
	 */
	public AgentBoxChangingVertexState(AgentsSimulationWindow parent,
			SommetDessin s, Hashtable def, String titre) {
		super(parent, titre, true);
		this.etatPanel.ardoise().changerEtat(s.getEtat());
		this.vert = s;

		this.tbModel = new VertexPropertyTableModel(s.getWhiteBoardTable(), def);

		this.table.setModel(this.tbModel);
	}

	// Methodes

	public void updateBox() {
		((VertexPropertyTableModel) this.tbModel).updateKeys();
		this.tbModel.fireTableDataChanged();
	}
	
	/**
	   * According to the button pressed on, it performs one of these actions:
	   * validating changes (<i>buttonDone</i>), removing the selected row (<i>buttonRemove</i>),
	   * adding a new row (<i>buttonAdd</i>) and modifying a row (<i>buttonModify</i>).
	   */
	
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == this.buttonDone) {
			this.dialog.setVisible(false);

			String etat = this.etatPanel.ardoise().donneEtat();
			this.vert.setEtat(etat);

			this.dialog.dispose();
		}
		if (e.getSource() == this.buttonAdd) {

			Object[] possibilities = { "String", "Integer|int", "Byte",
					"Character|char", "Double|double", "Float|float",
					"Long|long", "Short|short", "Boolean|boolean" };

			Object objValue;

			String s = (String) JOptionPane.showInputDialog(this.parent,
					"Select the type:", "Type", JOptionPane.PLAIN_MESSAGE,
					null, possibilities, "String");

			// If a string was returned, say so.
			if ((s != null) && (s.length() > 0)) {

				String name = JOptionPane.showInputDialog(this.parent,
						"Enter the name :");
				String value = JOptionPane.showInputDialog(this.parent,
						"Enter the value :");

				if ((name != null) && (value != null)) {
					objValue = value;

					try {

						if (s.equals("Integer|int")) {
							objValue = new Integer(value);
						} else if (s.equals("Byte")) {
							objValue = new Byte(value);
						} else if (s.equals("Character|char")) {
							objValue = new Character(value.charAt(0));
						} else if (s.equals("Double|double")) {
							objValue = new Double(value);
						} else if (s.equals("Float|float")) {
							objValue = new Float(value);
						} else if (s.equals("Long|long")) {
							objValue = new Long(value);
						} else if (s.equals("Short|short")) {
							objValue = new Short(value);
						} else if (s.equals("Boolean|boolean")) {
							objValue = new Boolean(value);
						}

						this.tbModel.putProperty(name, objValue);
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, e2.getMessage(),
								"Warning", JOptionPane.WARNING_MESSAGE);
					}
				}
			}

		}
		if (e.getSource() == this.buttonRemove) {

			if (this.table.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(this.parent,
						"No property selected !", "Warning",
						JOptionPane.WARNING_MESSAGE);
			} else {
				this.tbModel.removeProperty(this.table.getSelectedRow());
			}

		}
		
          if(e.getSource() == this.buttonModify) {
        	
        	int rowNb = this.table.getRowCount();
        	int i;
        	Object [] possibilities;
        	possibilities = new Object[rowNb];
        	    for (i=0; i<rowNb; i++){
        		   possibilities[i]=this.table.getValueAt(i, 0);
        		}
        	
        	String s = (String) JOptionPane.showInputDialog(this.parent,
                    "Select the proprety to be changed",
                    "Vertex proprety modification",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    possibilities[0]);
        	
        	
        	 if ((s != null) && (s.length() > 0)) {
                 
                 String value = JOptionPane.showInputDialog(this.parent, "Enter the new value :");
        	 
                 int j=0;
                 while (!(possibilities[j]==s)){
                	 j++;
                 }
                 Enumeration elt = this.parent.selection.elements();
                 SommetDessin firstElement = ((SommetDessin)elt.nextElement());
                 SimpleGraphVertex vertex;        
         		 String x = ((SommetDessin)firstElement).getEtiquette();
             	 Integer y;
             	 y = Integer.parseInt(x) ;
             	 vertex =  this.parent.getSimulator().graph.getSimpleGraphVertex(y);
             	 
                if  (firstElement.type().equals("vertex")){
                	if (s == "label") {
                		// a revoir: une méthode à part entière!
                		
                		vertex.changeColor(this.parent.getSimulator(), value);
                	}
                	if(s=="Visualization"){
                		if (value=="true") vertex.setVisualization(true);
                		if (value=="false") vertex.setVisualization(false);
                	}
                	firstElement.setWhiteBoardValue(s, value);
                	this.table.setValueAt(value,j,2);
                }
                 	 
                 
             }
        		
        }
	}

}
