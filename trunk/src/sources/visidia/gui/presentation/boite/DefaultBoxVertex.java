package visidia.gui.presentation.boite;

//import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import visidia.gui.presentation.userInterfaceSimulation.*;
//import visidia.gui.donnees.conteneurs.*;
//import visidia.gui.presentation.*;
import visidia.gui.donnees.*;

/**
 * Cette classe cree une boite utilisee pour modifier l'etat d'un    
 * ou de plusieurs sommets selectionne elle est appelee quand on ne    
 * selectionne que des sommets et qu'on appui sur le bouton info     
 */
public class DefaultBoxVertex
    extends AbstractDefaultBox
    implements ActionListener, ItemListener, VueEtatPanel
{

    /**
     * Cree une nouvelle boite pour afficher les caractéristiques de
     * "un_objet".  Ces caractéristiques seront modifiables.
     */

    
    public DefaultBoxVertex(AgentsSimulationWindow parent, Hashtable h) {
	this(parent, h,  "Default Vertex properties state");
    }
    
    /**
     * Cree une nouvelle boite appelee "titre" pour afficher les
     * caracteristiques de "un_objet".
     */
    public DefaultBoxVertex(AgentsSimulationWindow parent, Hashtable h, 
                            String titre) {

        super(parent,titre,false);

        tbModel = new PropertyTableModel(h);

        table.setModel(tbModel);
    }

    //Methodes  

    public void updateBox() {
        tbModel.fireTableDataChanged();
    }
  

    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == buttonDone) {
            dialog.setVisible(false);
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

