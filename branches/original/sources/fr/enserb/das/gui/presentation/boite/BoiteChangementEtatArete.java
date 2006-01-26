package fr.enserb.das.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import fr.enserb.das.gui.presentation.*;
import fr.enserb.das.gui.presentation.userInterfaceSimulation.*;
import fr.enserb.das.gui.presentation.userInterfaceEdition.Traitements;
import fr.enserb.das.gui.donnees.conteneurs.*;

/*
 * Cette classe crée une boite utilisée pour modifier l'etat    
 * d'un ou de pusieurs aretes selectionnee sur la fenetre de    
 * simulation.                                                  
 */
public class BoiteChangementEtatArete implements ActionListener ,ItemListener{
    
     // instance Variables
    /** The parent window : the box will be centered on this window */
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
    /** Ce booléen vaut VRAI si une des caractéristiques de l'ObjetVisu
	a été modifiée par l'utilisateur, et FAUX sinon.*/
    protected boolean modif;
    /** Une LigneChoixCouleur qui permet d'afficher et de modifier la couleur
	du trait de la forme dessine */
    protected JPanel etatPanel;
    protected JCheckBox but_marquage;
    protected boolean estMarquee;

    //Constructeurs
    /**
     * Crée une nouvelle boite pour afficher les caractéristiques de "un_objet". 
     * Ces caractéristiques seront modifiables.                                  
     */
    public BoiteChangementEtatArete(FenetreDeSimulation parent, Ensemble uneSelection) {
	this(parent, uneSelection , "Edges properties");
    }
    
    /**
     * Crée une nouvelle boite appelée "titre" pour afficher les caractéristiques
     * de "un_objet".                                                            
     */
    public BoiteChangementEtatArete(FenetreDeSimulation parent,
			       Ensemble uneSelection,
			       String titre) {
    
    this.dialog = new JDialog(parent, titre);
    this.parent = parent;
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
  /** Affiche la boite et la centre par rapport à "parent".*/
  public void show(Frame parent) {
    dialog.pack();
    dialog.show();
    dialog.setLocationRelativeTo(parent);
  }
  
  /** Ajoute un bouton nommé "label" au panel "pane" */
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
	parent.repaint();
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
    
    /** Cette méthode est appelée si l'utilisateur appuie sur le bouton Ok.*/
    public void buttonOk() {
	Enumeration e = selectionAretes.elements();
	while(e.hasMoreElements())
	    ((AreteDessin)e.nextElement()).setEtat(estMarquee);
	parent.simulationPanel().repaint();
	
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



