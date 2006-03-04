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
 * Cette classe créee une boite utilisée pour modifier l'état d'un
 * ou de plusieurs sommets selectionné elle est applée quand on ne
 * selectionne que des sommets et qu'on appui sur le bouton info
 */
public class BoiteChangementEtatSommetDist implements 
					       ActionListener, 
					       ItemListener  
{
    
    /** La fenetre parent : la boite sera centrée sur cette fenetre.*/
    protected FenetreDeSimulationDist parent;

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
    protected JLabel algoUsed;
    protected SommetDessin monSommet;
    protected int vertex_id = -1;
    protected EtatPanelDist etatPanel;
    
    /** a table for the state */
    protected JTable table = new JTable();

    /** failure and drawing information */
    protected JCheckBox but_failure,but_drawMessage;
    protected boolean hasFailure = false;
    protected boolean drawMessage= true;
    protected boolean hasFailureOldValue = hasFailure;
    protected boolean drawMessageOldValue= drawMessage;

    public BoiteChangementEtatSommetDist(FenetreDeSimulationDist parent, SommetDessin sommet) {
        this(parent, sommet, "Vertex properties state");
    }
    
    public BoiteChangementEtatSommetDist(FenetreDeSimulationDist p, SommetDessin sommet, String titre) {
        
        this.dialog = new JDialog(p, titre);
        this.parent = p;
        
        monSommet = sommet;
        
        vertex_id = Integer.valueOf(sommet.getEtiquette()).intValue();
        
	//Determination of the algorithm name used
	String algoString = new String();
        //if (parent.getAlgorithms().getAlgorithm(vertex_id) == null)
	algoString = "None";
	//else algoString = parent.getAlgorithms().getAlgorithm(vertex_id).getClass().getName();
  
	algoUsed = new JLabel("Algorithm used : "+algoString);
        
	//the panel taht contains the different colors for the vertice 
	//and the vertice label too.
        etatPanel = new EtatPanelDist(TableCouleurs.getTableCouleurs(),this);
        
        // failure interface
        //2004
	//hasFailure = monSommet.getFailure();
        hasFailureOldValue = hasFailure;
        but_failure = new JCheckBox("Vertex failure", hasFailure);
        but_failure.addItemListener(this);
	
	drawMessage = monSommet.getDrawMessage();
	drawMessageOldValue = drawMessage;
	but_drawMessage= new JCheckBox("Draw sending Message",drawMessage) ;
	but_drawMessage.addItemListener(this);
	    
        Panel panelHaut = new Panel();
        panelHaut.setLayout(new BorderLayout());
        panelHaut.add(etatPanel, BorderLayout.NORTH);
        panelHaut.add(but_failure, BorderLayout.CENTER);
	panelHaut.add(but_drawMessage, BorderLayout.SOUTH);
        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane spane = new JScrollPane(table);
        
        JPanel panelCentre = new JPanel();
        panelCentre.setLayout(new BorderLayout());
        panelCentre.add(spane, BorderLayout.NORTH);
        panelCentre.add(algoUsed, BorderLayout.CENTER);
        
        setProperties((monSommet.getStateTable()));
        
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(panelHaut, BorderLayout.NORTH);
        dialog.getContentPane().add(panelCentre, BorderLayout.CENTER);
        
        dialog.setSize(400,200);
        
        ajouterBoutons();
    }
    //Methodes
    
    
    /** setting the state */
    public void setProperties(Hashtable props) {
        table.setModel(new PropertyTableModel(props));
    }
    
    /** Affiche la boite et la centre par rapport a "parent".*/
    public void show(Frame parent) {
        dialog.pack();
        dialog.show();
	dialog.setLocationRelativeTo(parent);
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
        JPanel buttonPane = new JPanel(new FlowLayout());
        
        buttonOk = new JButton("Ok");
        buttonOk.addActionListener(this);
        
        buttonCancel = new JButton("Cancel");
        buttonCancel.addActionListener(this);
        
        
        buttonApply = new JButton("Apply");
        buttonApply.addActionListener(this);
        
        
        buttonPane.add(buttonOk);
        buttonPane.add(buttonCancel);
        buttonPane.add(buttonApply);
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
    }
    
    public void elementModified(){
        PropertyTableModel mod =(PropertyTableModel)table.getModel();
        mod.putProperty("label", etatPanel.ardoise().donneEtat());
        if(hasFailure)
            mod.putProperty("failure", "yes");
        else
            mod.putProperty("failure", "no");
	
	if(drawMessage)
	    mod.putProperty("draw messages","yes");
	else
	    mod.putProperty("draw messages","no");
    }
    
    
    /** Cette methode est appelee si l'utilisateur appuie sur le bouton Ok.*/
    public void buttonOk() {
        String etat = etatPanel.ardoise().donneEtat();
        PropertyTableModel mod =(PropertyTableModel)table.getModel();
        int nbRows = mod.getRowCount();
       	monSommet.setEtat(etat);
        //2004
	//monSommet.setFailure(hasFailure);
	monSommet.setDrawMessage(drawMessage);
	
	try{
            for (int i=0;i<nbRows;i++){
                table.editCellAt(i,1); // read the new values edited
                monSommet.setValue((String)mod.getValueAt(i,0), mod.getValueAt(i,1));
			    }
        } catch(Exception exc) { 
	    System.out.println(" Problem in Box : " + exc); 
	}
        // if failure state has changed: notify the simulator
        if(hasFailureOldValue != hasFailure)
	    {
		parent.nodeStateChanged(Integer.valueOf(monSommet.getEtiquette()).intValue(), mod.getProperties());
	    }
	
	if(drawMessageOldValue != drawMessage){
	    parent.nodeStateChanged(Integer.valueOf(monSommet.getEtiquette()).intValue(), mod.getProperties());
	}

	parent.simulationPanel().repaint();
    }
    
    /** Retourne le JDialog. */
    public JDialog dialog() {
        return dialog;
    }
    
    public void itemStateChanged(ItemEvent evt) {
        if((JCheckBox)evt.getSource() == but_failure) {
            hasFailure = !hasFailure;
            elementModified();
        }
	if((JCheckBox)evt.getSource() == but_drawMessage){
	    drawMessage = !drawMessage;
	    elementModified();
	}
    }
}
