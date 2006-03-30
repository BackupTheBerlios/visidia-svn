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
    extends AbstractDefaultBox
    implements ActionListener, ItemListener, VueEtatPanel
{

    //Constructeurs
    SommetDessin vert;

    public AgentBoxChangingVertexState(AgentsSimulationWindow parent, SommetDessin s, Hashtable def)
    {
        this(parent,s,def,"Specific vertex property");
    }

    /**
     * Cree une nouvelle boite appelee "titre" pour afficher les
     * caracteristiques de "un_objet".
     */
    public AgentBoxChangingVertexState(AgentsSimulationWindow parent, SommetDessin s, Hashtable def, String titre) 
    {
        super(parent,titre,true);
        etatPanel.ardoise().changerEtat(s.getEtat());
        vert = s;

        tbModel = new VertexPropertyTableModel(s.getWhiteBoardTable(),def);
        
        table.setModel(tbModel);
    }

    //Methodes  
    
    public void updateBox() {
        ((VertexPropertyTableModel) tbModel).updateKeys();
        tbModel.fireTableDataChanged();
    }
  
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == buttonDone) {
            dialog.setVisible(false);

            String etat = etatPanel.ardoise().donneEtat();
            vert.setEtat(etat);

            dialog.dispose();
        }
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
    }
      

}

