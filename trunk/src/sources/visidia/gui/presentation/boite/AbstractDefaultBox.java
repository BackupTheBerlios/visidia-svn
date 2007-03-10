package visidia.gui.presentation.boite;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import visidia.gui.donnees.AbstractPropertyTableModel;
import visidia.gui.donnees.TableCouleurs;
import visidia.gui.presentation.userInterfaceSimulation.AgentsSimulationWindow;

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
	this(parent, "Default properties state for all vertices",true);
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
                this.etatPanel = new EtatPanel(TableCouleurs.getTableCouleurs(),this);
        
                panelHaut = new Panel();
                panelHaut.setLayout(new BorderLayout());
                panelHaut.add(this.etatPanel, BorderLayout.NORTH);
            }
        
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane spane = new JScrollPane(this.table);

        JPanel panelCentre = new JPanel();
        panelCentre.setLayout(new BorderLayout());
        panelCentre.add(spane, BorderLayout.NORTH);
    
        this.dialog.getContentPane().setLayout(new BorderLayout());
        if(createEtatPanel == true)
            this.dialog.getContentPane().add(panelHaut, BorderLayout.NORTH);
        this.dialog.getContentPane().add(panelCentre, BorderLayout.CENTER);
        this.dialog.setSize(400,200);
    
        this.ajouterBoutons();
    
    }

    //Methodes  
    
       
    /** Affiche la boite et la centre par rapport a "parent".*/
    public void show(Frame parent) {
        this.dialog.pack();
        this.dialog.setVisible(true);
        this.dialog.setLocationRelativeTo(parent);
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
      
        this.buttonAdd = new JButton("Add");
        this.buttonAdd.addActionListener(this);
      
        this.buttonRemove = new JButton("Remove");
        this.buttonRemove.addActionListener(this);

        addRemovePane.add(this.buttonAdd);
        addRemovePane.add(this.buttonRemove);

        JPanel okCancelApplyPane = new JPanel(new FlowLayout());
      
        this.buttonDone = new JButton("Done");
        this.buttonDone.addActionListener(this);
      
        okCancelApplyPane.add(this.buttonDone);    
      
        buttonPane.add(addRemovePane,BorderLayout.NORTH);
        buttonPane.add(okCancelApplyPane,BorderLayout.SOUTH);
      
        this.dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
    }
    
   

    abstract public void actionPerformed(ActionEvent e);    


    //Implementation de VueEtatPanel
    public void elementModified(String s){
	this.elementModified();
    }
    
    public void elementModified(){
        this.tbModel.putProperty("label",this.etatPanel.ardoise().donneEtat());
    }

    /** Retourne le JDialog. */
    public JDialog dialog() {
        return this.dialog;
    }

    public void itemStateChanged(ItemEvent evt) {

    }

   
}

