package visidia.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import visidia.gui.presentation.userInterfaceSimulation.*;
import visidia.gui.donnees.conteneurs.*;
import visidia.gui.presentation.*;
import visidia.gui.donnees.*;

/**
 * Cette classe cree une boite utilisee pour modifier l'etat d'un    
 * ou de plusieurs sommets selectionne elle est appelee quand on ne    
 * selectionne que des sommets et qu'on appui sur le bouton info     
 */
public class DefaultBoxVertex
    implements ActionListener, ItemListener, VueEtatPanel
{
    /** La fenetre parent : la boite sera centree sur cette fenetre.*/
    protected AgentsSimulationWindow parent;
    /** Le JDialog dans lequel on va tout afficher.*/
    protected JDialog dialog;
    /** Le bouton Ok*/
    protected JButton buttonOk;
    /** Le bouton Cancel*/
    protected JButton buttonCancel;
    /** Le bouton Apply*/
    protected JButton buttonApply;
    /** the button for changing the algorithms */
    protected JButton buttonChange;
    /** The label for displaying the algorithm used */
    
    protected SommetDessin monSommet;
    protected int vertex_id = -1;
    protected EtatPanel etatPanel;
    /** a table for the state */
    protected JTable table = new JTable();

    //xav
    // Button for adding property to the whiteboard
    protected JButton buttonAdd;
    // Button for removing property from the whiteboard
    protected JButton buttonRemove;
    // Button for refreshing properties from the whiteboard
    protected JButton buttonRefresh;

    protected PropertyTableModel tbModel;
    

    /**
     * Cree une nouvelle boite pour afficher les caractéristiques de
     * "un_objet".  Ces caractéristiques seront modifiables.
     */

    public DefaultBoxVertex(){
    }
    
    public DefaultBoxVertex(AgentsSimulationWindow parent, Hashtable def) {
	this(parent, def, "Default Vertex properties state");
    }
    
    /**
     * Cree une nouvelle boite appelee "titre" pour afficher les
     * caracteristiques de "un_objet".
     */
    public DefaultBoxVertex(AgentsSimulationWindow parent, Hashtable def, 
                            String titre) {
    
        this.dialog = new JDialog(parent, titre);
        this.parent = parent;

        
        //vertex_id = Integer.valueOf(sommet.getEtiquette()).intValue();
        
        etatPanel = new EtatPanel(TableCouleurs.getTableCouleurs(),this);
        
        Panel panelHaut = new Panel();
        panelHaut.setLayout(new BorderLayout());
        panelHaut.add(etatPanel, BorderLayout.NORTH);
        
        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane spane = new JScrollPane(table);

        JPanel panelCentre = new JPanel();
        panelCentre.setLayout(new BorderLayout());
        panelCentre.add(spane, BorderLayout.NORTH);
    
        tbModel = new PropertyTableModel(def);

        table.setModel(tbModel);

        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(panelHaut, BorderLayout.NORTH);
        dialog.getContentPane().add(panelCentre, BorderLayout.CENTER);
        dialog.setSize(400,200);
    
        ajouterBoutons();
    
    }

    //Methodes  
    
       
    /** Affiche la boite et la centre par rapport a "parent".*/
    public void show(Frame parent) {
        dialog.pack();
        dialog.show();
        dialog.setLocationRelativeTo(parent);
    }

    public void updateBox() {
        tbModel.fireTableDataChanged();
    }
  
    /** Ajoute un bouton nomme "label" au panel "pane" */
    public JButton addButton(JPanel pane, String label) {
        JPanel tmp = new JPanel(new FlowLayout());
        JButton button = new JButton(label);
        tmp.add(button);
        button.setSize(button.getMinimumSize());
        pane.add(tmp);
        pane.add(Box.createRigidArea(new Dimension(0, 5)));
        return button;
    }
  
 
    /** Ajoute les boutons en bas de la boite.*/
    public void ajouterBoutons() {
    
        JPanel buttonPane = new JPanel( new BorderLayout());
      
        JPanel addRemoveRefreshPane = new JPanel(new FlowLayout());
      
        buttonAdd = new JButton("Add");
        buttonAdd.addActionListener(this);
      
        buttonRemove = new JButton("Remove");
        buttonRemove.addActionListener(this);

        buttonRefresh = new JButton("Refresh");
        buttonRefresh.addActionListener(this);
      
        addRemoveRefreshPane.add(buttonAdd);
        addRemoveRefreshPane.add(buttonRemove);
        addRemoveRefreshPane.add(buttonRefresh);

        JPanel okCancelApplyPane = new JPanel(new FlowLayout());
      
        buttonOk = new JButton("Ok");
        buttonOk.addActionListener(this);
      
        buttonCancel = new JButton("Cancel");
        buttonCancel.addActionListener(this);
      
        buttonApply = new JButton("Apply");
        buttonApply.addActionListener(this);
      
        okCancelApplyPane.add(buttonOk);
        okCancelApplyPane.add(buttonCancel);    
        okCancelApplyPane.add(buttonApply);
      
        buttonPane.add(addRemoveRefreshPane,BorderLayout.NORTH);
        buttonPane.add(okCancelApplyPane,BorderLayout.SOUTH);
      
        buttonApply.setEnabled(true);
        dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
    }
    
   

    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == buttonOk) {
            try {
                buttonOk();
                dialog.setVisible(false);
                dialog.dispose();
            } catch(NumberFormatException exception) {
                StringTokenizer st =
                    new StringTokenizer(exception.getMessage(), "\n");
                int nb_lignes = st.countTokens();
                String message = new String();
                for(int i = 0; i < nb_lignes; i++)
                    message = message + "\n" + st.nextToken();
                JOptionPane.showMessageDialog(parent,
                                              message, 
                                              "Warning",
                                              JOptionPane.WARNING_MESSAGE);
            }
        }
        if(e.getSource() == buttonApply) {
            try {
                buttonOk();
                parent.repaint();
            } catch(NumberFormatException exception) {
                StringTokenizer st =
                    new StringTokenizer(exception.getMessage(), "\n");
                int nb_lignes = st.countTokens();
                String message = new String();
                for(int i = 0; i < nb_lignes; i++)
                    message = message + "\n" + st.nextToken();
                JOptionPane.showMessageDialog(parent,
                                              message, 
                                              "Warning",
                                              JOptionPane.WARNING_MESSAGE);
            }
        }
        if(e.getSource() == buttonCancel) {
            dialog.setVisible(false);
            dialog.dispose();
        }
        //xav
        if(e.getSource() == buttonAdd) {
            
            
            Object[] possibilities = {"String", "Integer|int", "Byte", "Character|char",
                                      "Double|double","Float|float", "Long|long", 
                                      "Short|short", "Boolean|boolean"};
            
            Object objValue;
            
            String s = (String) JOptionPane.showInputDialog(parent,
                                                            "Select the type:",
                                                            "Type",
                                                            JOptionPane.PLAIN_MESSAGE,
                                                            null,
                                                            possibilities,
                                                            "String");
            
            //If a string was returned, say so.
            if ((s != null) && (s.length() > 0)) {
                
                String name = JOptionPane.showInputDialog(parent, "Enter the name :");
                String value = JOptionPane.showInputDialog(parent, "Enter the value :");
                
                if ( name != null  && value != null )
                    {
                        objValue = value;

                        try{

                            if      ( s.equals("Integer|int") ) {objValue = new Integer(value); }
                            else if ( s.equals("Byte") ) {objValue = new Byte(value);}
                            else if ( s.equals("Character|char") ) {objValue = new Character(value.charAt(0));}
                            else if ( s.equals("Double|double") ) {objValue = new Double(value);}
                            else if ( s.equals("Float|float") ) {objValue = new Float(value);}
                            else if ( s.equals("Long|long") ) {objValue = new Long(value);}
                            else if ( s.equals("Short|short") ) {objValue = new Short(value);}
                            else if ( s.equals("Boolean|boolean") ) {objValue = new Boolean(value);}

                            tbModel.putProperty(name,objValue);
                        }
                        catch(Exception e2) {
                            JOptionPane.showMessageDialog(null,
                                          e2.getMessage(), 
                                          "Warning",
                                          JOptionPane.WARNING_MESSAGE); 
                        }
                    }
            }
            
        }
        //xav
        if(e.getSource() == buttonRemove) {
            
            if (table.getSelectedRow() == -1 ) {
                JOptionPane.showMessageDialog(parent,
                                              "No property selected !", 
                                              "Warning",
                                              JOptionPane.WARNING_MESSAGE);
            }
            else {
                tbModel.removeProperty(table.getSelectedRow());
            }
            
        }
        //xav
        if(e.getSource() == buttonRefresh) {
            tbModel.fireTableDataChanged();
            // setProperties(monSommet.getWhiteBoardTable());
        }
        
    }
    
    //Implementation de VueEtatPanel
    public void elementModified(String s){
	elementModified();
    }
    
    public void elementModified(){

        tbModel.putProperty("label",etatPanel.ardoise().donneEtat());

    }
      
    /** Cette methode est appelee si l'utilisateur appuie sur le bouton Ok.*/
    public void buttonOk() {
	String etat = etatPanel.ardoise().donneEtat();
	int nbRows = tbModel.getRowCount();
	monSommet.setEtat(etat);

	try{
	    for (int i=0;i<nbRows;i++){
		table.editCellAt(i,1); // read the new values edited
		monSommet.setValue((String)tbModel.getValueAt(i,0),tbModel.getValueAt(i,1));
	    }
	}catch(Exception exc){System.out.println(" Problem in Box : "+exc);}
    
	parent.simulationPanel().repaint();
    }


    /** Retourne le JDialog. */
    public JDialog dialog() {
        return dialog;
    }

    public void itemStateChanged(ItemEvent evt) {

    }
    
}

