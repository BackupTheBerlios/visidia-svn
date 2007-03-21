package visidia.gui.presentation.boite;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import visidia.gui.donnees.AgentPropertyTableModel;
import visidia.gui.presentation.userInterfaceSimulation.AgentsSimulationWindow;
import visidia.tools.agents.UpdateTableAgent;
import visidia.tools.agents.WhiteBoard;

/**
 * Cette classe cree une boite utilisee pour modifier l'etat d'un ou de
 * plusieurs sommets selectionne elle est appelee quand on ne selectionne que
 * des sommets et qu'on appui sur le bouton info
 */
public class AgentBoxProperty extends AbstractDefaultBox implements
		ActionListener, ItemListener, VueEtatPanel {

	UpdateTableAgent timer;

	// Constructeurs

	public AgentBoxProperty(AgentsSimulationWindow parent, WhiteBoard wb,
			String agentName) {
		this(parent, wb, "Agent property for " + agentName, agentName);
	}

	/**
	 * Cree une nouvelle boite appelee "titre" pour afficher les
	 * caracteristiques de "un_objet".
	 */
	public AgentBoxProperty(AgentsSimulationWindow parent, WhiteBoard wb,
			String titre, String agentName) {
		super(parent, titre, false);

		this.tbModel = new AgentPropertyTableModel(wb);

		this.table.setModel(this.tbModel);

		this.dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				AgentBoxProperty.this.closingWindow();
			}
		});

		this.timer = new UpdateTableAgent(
				(AgentPropertyTableModel) this.tbModel);
		new Thread(this.timer).start();

	}

	// Methodes

	public void updateBox() {
		System.out.println("AgentBoxProperty.updateBox()");
		this.tbModel.fireTableDataChanged();
	}

	public void closingWindow() {
		this.parent.removeWindow(this);
		this.close();
	}

	public void close() {
		this.timer.stop();
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == this.buttonDone) {

			this.timer.stop();
			this.timer = null;

			this.dialog.setVisible(false);
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
        	for (i=0; i<rowNb; i++)	possibilities[i]=this.table.getValueAt(i, 0);
  
        	String s = (String) JOptionPane.showInputDialog(this.parent,
                    "Select the proprety to be changed",
                    "Agent propreties modification",
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
      
                     this.table.setValueAt(value,j,2);
                 	 
                 
            }
        }

	}

}
