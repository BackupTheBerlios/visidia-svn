package fr.enserb.das.gui.presentation.boite;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import fr.enserb.das.gui.presentation.userInterfaceSimulation.*;
import fr.enserb.das.algo.*;
import fr.enserb.das.misc.*;

/*************************************************************************************
 * Cette classe crée une boite utilisée pour saisir les regles de réecriture simples *
 * a chaque appui sur le bouton suivant la regle editee s'affiche en dessous et est  *
 * automatiquement stockée dans la liste des regles de réecriture  que l'on passe    *
 * ensuite comme argument au simulateur.                                             *
 ************************************************************************************/
public class BoiteSimpleRule implements ActionListener {
    
    /** La fenêtre parent : la boite sera centrée sur cette fenêtre.*/
    protected FenetreDeSimulation parent;
    /** Le JDialog dans lequel on va tout afficher.*/
    protected JDialog dialog;
    
    /** Le bouton Ok*/
    protected JButton buttonOk;
    /** Le bouton Cancel*/
    protected JButton buttonCancel;
    /** Le bouton Suivant qui sert a valider la regle saisie et passer a la suivante */
    protected JButton buttonSuivant;
    
    /** Ce booléen vaut VRAI on a entré une nouvelle et qu'on n'a pas encore valide et FAUX sinon.*/
    protected boolean modif;
    /** c'est la planche qui sert a saisir les regles une a une */
    protected RulePanel plancheRegle;
    /** c'est la planche qui affiche les regles deja saisies */
    protected JPanel infoPane ; 
    

    // Constructeur 
    public BoiteSimpleRule(FenetreDeSimulation parent, String titre) {
	this.dialog = new JDialog(parent, titre);
	dialog.getContentPane().setLayout(new BorderLayout());
	this.parent = parent;
	this.modif = false;		
	this.plancheRegle = new RulePanel(this);
	
	plancheRegle.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
								""));
	
	dialog.getContentPane().add(plancheRegle,BorderLayout.NORTH);
	infoPane = new JPanel(new GridLayout(3, 2));  
	BoxLayout infoLayout = new BoxLayout(infoPane, BoxLayout.Y_AXIS);
	infoPane.setLayout(infoLayout);
	infoPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
							    "Rules already edited "));
	
	
	dialog.getContentPane().add(infoPane, BorderLayout.CENTER); 
	
	ajouterBoutons(); 
	
    }
    //Modificateurs 
    /********************************************************* 
     * Affiche la boite et la centre par rapport à "parent". *
     ********************************************************/
    public void show(Frame parent) {
	dialog.pack();
	dialog.show();
	dialog.setLocationRelativeTo(parent);
    }

    /********************************************************* 
     * Ajoute un bouton nommé "label" au panel "pane"        *
     ********************************************************/
    public JButton addButton(JPanel pane, String label) {
	JPanel tmp = new JPanel(new FlowLayout());
	JButton button = new JButton(label);
	tmp.add(button);
	button.setSize(button.getMinimumSize());
	pane.add(tmp);
	pane.add(Box.createRigidArea(new Dimension(0, 5)));
	return button;
    }
    
    
    /******************************************************************
     * Ajoute les boutons en bas de la boite. Suivant "est_editable", *
     * certains d'entre eux seront actifs ou non.                     *
     *****************************************************************/
    public void ajouterBoutons() {
	JPanel buttonPane = new JPanel(new FlowLayout());
	
	buttonOk = new JButton("Ok");
	buttonOk.addActionListener(this);
	
	buttonCancel = new JButton("Cancel");
	buttonCancel.addActionListener(this);
	
	
	buttonSuivant= new JButton("Suivant");
	buttonSuivant.addActionListener(this);
	
	
	buttonPane.add(buttonOk);
	buttonPane.add(buttonCancel);    
	buttonPane.add(buttonSuivant);
	buttonSuivant.setEnabled(modif);
	dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
    }
    
    /*************************************************************************
     * Implementation de la methode de l'interface ActionListener.           *
     * c'est cette methode qui capture les evenements de clique des boutons  *
     ************************************************************************/
    public void actionPerformed(ActionEvent e) {
	if(e.getSource() == buttonOk) {
	    
	    if(modif == true)
		{ 
		    try {
			this.addRule(plancheRegle.etatSommet1,plancheRegle.estMarquee12,
				     plancheRegle.etatSommet2,plancheRegle.etatSommet3,
				     plancheRegle.estMarquee34,plancheRegle.etatSommet4);
			
			Arrow gauche = new Arrow(plancheRegle.etatSommet1,
						 plancheRegle.etatSommet2,
						 plancheRegle.estMarquee12);
			Arrow droite = new Arrow(plancheRegle.etatSommet3,
						 plancheRegle.etatSommet4,
						 plancheRegle.estMarquee34);
			
			parent.addRule(new SimpleRule(gauche,droite));
		    }
		    catch(NumberFormatException exception) {
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
	    buttonOk();
	}
	if(e.getSource() == buttonSuivant) {
	    try {
		
		Arrow gauche = new Arrow(plancheRegle.etatSommet1,
					 plancheRegle.etatSommet2,
					 plancheRegle.estMarquee12);
		Arrow droite = new Arrow(plancheRegle.etatSommet3,
					 plancheRegle.etatSommet4,
					 plancheRegle.estMarquee34);
		parent.addRule(new SimpleRule(gauche,droite));
		dialog.getContentPane().remove(plancheRegle);
		dialog.getContentPane().remove(infoPane);
		
		this.addRule(plancheRegle.etatSommet1,plancheRegle.estMarquee12,
			     plancheRegle.etatSommet2,plancheRegle.etatSommet3,
			     plancheRegle.estMarquee34,plancheRegle.etatSommet4);
		
		this.plancheRegle = new RulePanel(this);
		parent.incrementRules();  
		dialog.setTitle("Regle Numero:" + parent.numberOfRules());
		
		dialog.getContentPane().add(this.plancheRegle,BorderLayout.NORTH);
		dialog.getContentPane().add(this.infoPane,BorderLayout.CENTER);
		this.show(this.parent);
		parent.repaint();
		modif = false;
		buttonSuivant.setEnabled(false);
	    } catch(NumberFormatException exception) {
		StringTokenizer st =
		    new StringTokenizer(exception.getMessage(), "\n");
		int nb_lignes = st.countTokens();
		String message = new String();
		this.plancheRegle = new RulePanel(this);this.plancheRegle = new RulePanel(this);this.plancheRegle = new RulePanel(this);	for(int i = 0; i < nb_lignes; i++)
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
    
    /************************************************************************
     * Retourne VRAI si une regle vient d'etre editee mais n'est pas encore *
     * validée, et FAUX s'il n'y a pas de regles a valider                  *
     ***********************************************************************/
    public void elementModified() {
	modif = true;
	buttonSuivant.setEnabled(modif);
    }
    
    /***********************************************************************
     * Cette méthode est appelée si l'utilisateur appuie sur le bouton Ok  *
     * elle se charge de la fermeture de la fenetre .                      *
     **********************************************************************/
    public void buttonOk() {
	dialog.setVisible(false);
	dialog.dispose();
    }
    
    /*******************************************************************
     * Cette methode ajoute la regle saisie dans la planche "infoPane" *
     * pour etre affichee automatiquement .                            *
     ******************************************************************/
    public void addRule(String som1,boolean ar12,String som2,String som3,boolean ar34,String som4){
	
	String sommet1 = new String(som1);
	String sommet2 = new String(som2);
	String sommet3 = new String(som3);
	String sommet4 = new String(som4);
	String arete12 = new String("");
	String arete34 = new String("");
	if(ar12)
	    arete12 = new String("X");
	if(ar34)
	    arete34 = new String("X");
	
	    
	infoPane.add(new JLabel(sommet1+"--"+arete12+"--"+sommet2+"==>"+sommet3+"--"+arete34+"--"+sommet4));
	
  }
    //Accesseurs 
    
    /** Retourne le JDialog. */
    public JDialog dialog() {
	return dialog;
    }
}






