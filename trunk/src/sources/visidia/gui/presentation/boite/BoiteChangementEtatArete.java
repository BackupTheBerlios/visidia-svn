package visidia.gui.presentation.boite;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import visidia.gui.donnees.conteneurs.Ensemble;
import visidia.gui.presentation.AreteDessin;
import visidia.gui.presentation.userInterfaceSimulation.AgentsSimulationWindow;
import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulation;
import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulationDist;

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
    	this.initialisation(uneSelection,titre);
    }
    
    public BoiteChangementEtatArete(AgentsSimulationWindow parent,
				    Ensemble uneSelection,
				    String titre) {
    
	this.parentAgent = parent;
	this.initialisation(uneSelection,titre);    
    
    }
  
    public BoiteChangementEtatArete(FenetreDeSimulationDist parentDist,
				    Ensemble uneSelection,
				    String titre) {
	
	this.parentDist = parentDist;
	this.initialisation(uneSelection,titre);    
    }
  
    public void initialisation(Ensemble uneSelection,
			  String titre) {
	
	this.dialog = new JDialog(this.parent, titre);
	this.modif = false;
       
	this.selectionAretes = uneSelection;
	
	this.etatPanel = new JPanel();
	this.but_marquage = new JCheckBox("Activate edge(s) state");
	this.but_marquage.addItemListener(this);
	this.etatPanel.add(this.but_marquage);
	
	this.dialog.getContentPane().setLayout(new BorderLayout());
	this.dialog.getContentPane().add(this.etatPanel, BorderLayout.NORTH);
	this.ajouterBoutons();
    }
    
    //Methodes  
    /** Affiche la boite et la centre par rapport a "parent".*/
    public void show(Frame parent) {
	this.dialog.pack();
	this.dialog.setVisible(true);
	//dialog.setLocationRelativeTo(parent);
	if ( this.parentDist == null )
	    this.dialog.setLocationRelativeTo(parent);
	else 
	    this.dialog.setLocationRelativeTo(this.parentDist);
    
	
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
    
	this.buttonOk = new JButton("Ok");
	this.buttonOk.addActionListener(this);
    
	this.buttonCancel = new JButton("Cancel");
	this.buttonCancel.addActionListener(this);

   
	this.buttonApply = new JButton("Apply");
	this.buttonApply.addActionListener(this);
    
    
	buttonPane.add(this.buttonOk);
	buttonPane.add(this.buttonCancel);    
	buttonPane.add(this.buttonApply);
	this.buttonApply.setEnabled(this.modif);
	this.dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
    }
  

    public void actionPerformed(ActionEvent e) {
	if(e.getSource() == this.buttonOk) {
	    this.buttonOk();
	    this.dialog.setVisible(false);
	    this.dialog.dispose();
	}
	if(e.getSource() == this.buttonApply) {
	    this.buttonOk();
	    if ( this.parent != null )
		this.parent.repaint();
	    else if( this.parentAgent != null)
		this.parentAgent.repaint();
	    else
		this.parentDist.repaint();
	    this.modif = false;
	    this.buttonApply.setEnabled(false);
     
	}
	if(e.getSource() == this.buttonCancel) {
	    this.dialog.setVisible(false);
	    this.dialog.dispose();
	}
    }
  
    /**
     * Retourne VRAI si une des caractéristiques de l'ObjetVisu a été modifiée,
     * et FAUX sinon.                                                          
     */ 
    public void elementModified() {
	this.modif = true;
	this.buttonApply.setEnabled(this.modif);
    }
    
    /** Cette methode est appelée si l'utilisateur appuie sur le
     * bouton Ok.*/
    public void buttonOk() {
	Enumeration e = this.selectionAretes.elements();
	while(e.hasMoreElements()) {
	    AreteDessin areteCourante = (AreteDessin)e.nextElement();
	    areteCourante.setEtat(this.estMarquee);
	    //areteCourante.setFailure(hasFailure);
	    //parent.setEdgeState(areteCourante.getId1(),
	    //areteCourante.getId2(), hasFailure);
	    //parent.setEdgeState(areteCourante.getId2(),
	    //areteCourante.getId1(), hasFailure);
	}

	if(this.parent != null)
	    this.parent.simulationPanel().repaint();
	else if(this.parentAgent != null)
	    this.parentAgent.simulationPanel().repaint();
	else
	    this.parentDist.simulationPanel().repaint();
    }
    
    
    /** Retourne le JDialog. */
    public JDialog dialog() {
	return this.dialog;
    }
    
    public void itemStateChanged(ItemEvent evt)  
    { 
	if((JCheckBox)evt.getSource() == this.but_marquage)
	    {
		this.estMarquee = !this.estMarquee;
		this.elementModified();
		
	    }
	
    }
}



