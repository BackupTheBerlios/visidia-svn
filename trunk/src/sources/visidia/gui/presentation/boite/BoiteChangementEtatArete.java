package visidia.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import visidia.gui.presentation.*;
import visidia.gui.presentation.userInterfaceSimulation.*;
import visidia.gui.presentation.userInterfaceEdition.Traitements;
import visidia.gui.donnees.conteneurs.*;

/*
 * Cette classe crée une boite utilisée pour modifier l'état    
 * d'une ou de pusieurs arêtes selectionnée sur la fenêtre de    
 * simulation.                                                  
 */
public class BoiteChangementEtatArete implements ActionListener ,ItemListener{
    
    // instance Variables
    /** The parent window : the box will be centered on this window */
    protected FenetreDeSimulationDist parentDist;
    protected AgentsSimulationWindow parentAgent;
    protected FenetreDeSimulation parent;

    /** The JDialog where all will be painted */
    protected JDialog dialog;
    /** The Ok button */
    protected JButton buttonOk;
    /** The Cancel button */
    protected JButton buttonCancel;
    /** The apply button */
    protected JButton buttonApply;
    
    protected Ensemble selectionAretes;
    /** Ce booleen vaut VRAI si une des caracteristiques de l'ObjetVisu
	a ete modifiee par l'utilisateur, et FAUX sinon.*/
    protected boolean modif;
    /** Une LigneChoixCouleur qui permet d'afficher et de modifier la couleur
	du trait de la forme dessine */
    protected JPanel etatPanel;
    protected JCheckBox but_marquage;
    protected boolean estMarquee;

    //Constructeurs
    /**
     * Cree une nouvelle boite pour afficher les caractéristiques de
     * "un_objet".  Ces caracteristiques seront modifiables.
     */
    public BoiteChangementEtatArete(FenetreDeSimulation parent, 
				    Ensemble uneSelection) {
	this(parent, uneSelection , "Edges properties");
    }
    
    public BoiteChangementEtatArete(AgentsSimulationWindow parent, 
				    Ensemble uneSelection) {
	this(parent, uneSelection , "Edges properties");
    }

    public BoiteChangementEtatArete(FenetreDeSimulationDist parent, 
				    Ensemble uneSelection) {
        this(parent, uneSelection , "Edges properties");
    }

    /**
     * Cree une nouvelle boite appelee "titre" pour afficher les
     * caracteristiques de "un_objet".
     */
    public BoiteChangementEtatArete(FenetreDeSimulation parent,
				    Ensemble uneSelection,
				    String titre) {
    
	this.parent = parent;
    	initialisation(uneSelection,titre);
    }
    
    public BoiteChangementEtatArete(AgentsSimulationWindow parent,
				    Ensemble uneSelection,
				    String titre) {
    
	this.parentAgent = parent;
	initialisation(uneSelection,titre);    
    
    }
  
    public BoiteChangementEtatArete(FenetreDeSimulationDist parentDist,
				    Ensemble uneSelection,
				    String titre) {
	
	this.parentDist = parentDist;
	initialisation(uneSelection,titre);    
    }
  
    public void initialisation(Ensemble uneSelection,
			  String titre) {
	
	this.dialog = new JDialog(parent, titre);
	this.modif = false;
       
	this.selectionAretes = uneSelection;
	
	etatPanel = new JPanel();
	but_marquage = new JCheckBox("Activate edge(s) state");
	but_marquage.addItemListener(this);
	etatPanel.add(but_marquage);
	
	dialog.getContentPane().setLayout(new BorderLayout());
	dialog.getContentPane().add(etatPanel, BorderLayout.NORTH);
	ajouterBoutons();
    }
    
    //Methodes  
    /** Affiche la boite et la centre par rapport a "parent".*/
    public void show(Frame parent) {
	dialog.pack();
	dialog.show();
	//dialog.setLocationRelativeTo(parent);
	if ( parentDist == null )
	    dialog.setLocationRelativeTo(parent);
	else 
	    dialog.setLocationRelativeTo(parentDist);
    
	
    }
  
    /** Ajoute un bouton nomme "label" au panel "pane" */
    //public JButton addButton(JPanel pane, String label) {
    //JPanel tmp = new JPanel(new FlowLayout());
    //JButton button = new JButton(label);
    //tmp.add(button);
    //button.setSize(button.getMinimumSize());
    //pane.add(tmp);
    //pane.add(Box.createRigidArea(new Dimension(0, 5)));
    //return button;
    //}
  
 
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
	buttonApply.setEnabled(modif);
	dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
    }
  

    public void actionPerformed(ActionEvent e) {
	if(e.getSource() == buttonOk) {
	    buttonOk();
	    dialog.setVisible(false);
	    dialog.dispose();
	}
	if(e.getSource() == buttonApply) {
	    buttonOk();
	    if ( parent != null )
		parent.repaint();
	    else if( parentAgent != null)
		parentAgent.repaint();
	    else
		parentDist.repaint();
	    modif = false;
	    buttonApply.setEnabled(false);
     
	}
	if(e.getSource() == buttonCancel) {
	    dialog.setVisible(false);
	    dialog.dispose();
	}
    }
  
    /**
     * Retourne VRAI si une des caractéristiques de l'ObjetVisu a été modifiée,
     * et FAUX sinon.                                                          
     */ 
    public void elementModified() {
	modif = true;
	buttonApply.setEnabled(modif);
    }
    
    /** Cette methode est appelée si l'utilisateur appuie sur le
     * bouton Ok.*/
    public void buttonOk() {
	Enumeration e = selectionAretes.elements();
	while(e.hasMoreElements()) {
	    AreteDessin areteCourante = (AreteDessin)e.nextElement();
	    areteCourante.setEtat(estMarquee);
	    //areteCourante.setFailure(hasFailure);
	    //parent.setEdgeState(areteCourante.getId1(),
	    //areteCourante.getId2(), hasFailure);
	    //parent.setEdgeState(areteCourante.getId2(),
	    //areteCourante.getId1(), hasFailure);
	}

	if(parent != null)
	    parent.simulationPanel().repaint();
	else if(parentAgent != null)
	    parentAgent.simulationPanel().repaint();
	else
	    parentDist.simulationPanel().repaint();
    }
    
    
    /** Retourne le JDialog. */
    public JDialog dialog() {
	return dialog;
    }
    
    public void itemStateChanged(ItemEvent evt)  
    { 
	if((JCheckBox)evt.getSource() == but_marquage)
	    {
		estMarquee = !estMarquee;
		elementModified();
		
	    }
	
    }
}



