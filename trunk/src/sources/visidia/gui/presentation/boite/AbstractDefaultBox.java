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
public abstract class AbstractDefaultBox
    implements ActionListener, ItemListener, VueEtatPanel
{
    /** La fenetre parent : la boite sera centree sur cette fenetre.*/
    protected AgentsSimulationWindow parent;
    /** Le JDialog dans lequel on va tout afficher.*/
    protected JDialog dialog;
    /** Le bouton Done*/
    protected JButton buttonDone;
    /** the button for changing the algorithms */
    protected JButton buttonChange;

    protected int vertex_id = -1;
    protected EtatPanel etatPanel;
    /** a table for the state */
    protected JTable table = new JTable();

    // Button for adding property to the whiteboard
    protected JButton buttonAdd;
    // Button for removing property from the whiteboard
    protected JButton buttonRemove;

    protected AbstractPropertyTableModel tbModel;

    protected int lastItemSelected = -1 ;
    
    public AbstractDefaultBox(AgentsSimulationWindow parent) {
	this(parent, "Default properties state",true);
    }
    
    /**
     * Cree une nouvelle boite appelee "titre" pour afficher les
     * caracteristiques de "un_objet".
     */
    public AbstractDefaultBox(AgentsSimulationWindow parent, String titre, boolean createEtatPanel) {
    
        Panel panelHaut = null;
    
        this.dialog = new JDialog(parent, titre);
        this.parent = parent;

        if (createEtatPanel == true)
            {
                etatPanel = new EtatPanel(TableCouleurs.getTableCouleurs(),this);
        
                panelHaut = new Panel();
                panelHaut.setLayout(new BorderLayout());
                panelHaut.add(etatPanel, BorderLayout.NORTH);
            }
        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane spane = new JScrollPane(table);

        JPanel panelCentre = new JPanel();
        panelCentre.setLayout(new BorderLayout());
        panelCentre.add(spane, BorderLayout.NORTH);
    
        dialog.getContentPane().setLayout(new BorderLayout());
        if(createEtatPanel == true)
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

    abstract public void updateBox();
  
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
      
        JPanel addRemovePane = new JPanel(new FlowLayout());
      
        buttonAdd = new JButton("Add");
        buttonAdd.addActionListener(this);
      
        buttonRemove = new JButton("Remove");
        buttonRemove.addActionListener(this);

        addRemovePane.add(buttonAdd);
        addRemovePane.add(buttonRemove);

        JPanel okCancelApplyPane = new JPanel(new FlowLayout());
      
        buttonDone = new JButton("Done");
        buttonDone.addActionListener(this);
      
        okCancelApplyPane.add(buttonDone);    
      
        buttonPane.add(addRemovePane,BorderLayout.NORTH);
        buttonPane.add(okCancelApplyPane,BorderLayout.SOUTH);
      
        dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
    }
    
   

    abstract public void actionPerformed(ActionEvent e);    


    //Implementation de VueEtatPanel
    public void elementModified(String s){
	elementModified();
    }
    
    public void elementModified(){
        tbModel.putProperty("label",etatPanel.ardoise().donneEtat());
    }

    /** Retourne le JDialog. */
    public JDialog dialog() {
        return dialog;
    }

    public void itemStateChanged(ItemEvent evt) {

    }

   
}

