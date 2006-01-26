package fr.enserb.das.gui.presentation.boite;

import fr.enserb.das.gui.presentation.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/** Cette classe "raffine" sa super classe en lui ajoutant les champs permettant *  d'afficher et de modifier les caractéristiques spécifiques d'un sommet carre*/

public class BoiteSommetCarre extends BoiteSommetDessin implements ActionListener {
    
    /** Le panel dans lequel on affiche les propriétés (caractéristiques non graphiques) du sommet.*/
    protected JPanel propertiesPane;
    /** Un label dont on sera amené à modifier le texte */
    protected JLabel tailleLabel;
    /** Un champ de saisie pour le taille du sommet.*/
    protected JTextField tailleTextField;
    /** Le panel dans lequel on affiche la liste des images disponibles*/
    protected ListeChoixImage iconPane;

    /* on liste les champs qui sont suceptibles d'etre modifies pas la classe */
    private int cote;
       

    /** Crée une nouvelle boite pour afficher les caractéristiques de "un_sommet"*/
    public BoiteSommetCarre(JFrame parent, SommetCarre un_sommet) {
	this(parent, un_sommet, "Vertex properties", true);
    }
    
    /** Crée une nouvelle boite, centrée sur "parent" pour afficher les caractéristiques de "un_sommet".
     * La boite sera appelée "titre"
     * Suivant la valeur de "est_editable", les caractéristiques du sommet sont modifiables.*/
    public BoiteSommetCarre(JFrame parent, SommetCarre un_sommet, String titre,
		       boolean est_editable) {
	
	super(parent, un_sommet, titre, est_editable);
	cote = ((SommetCarre)forme).getCote();
	JPanel taillePanel = new JPanel(new GridLayout(1, 2));
	tailleLabel = new JLabel("Size");
	taillePanel.add(tailleLabel);
	tailleTextField = new JTextField(Integer.toString(cote), 6);
	tailleTextField.addActionListener(this);
	taillePanel.add(tailleTextField);
	caracteristicsPane.add(taillePanel);
    
	propertiesPane = new JPanel();
	BoxLayout propertiesLayout = new BoxLayout(propertiesPane, BoxLayout.Y_AXIS);
	propertiesPane.setLayout(propertiesLayout);
	propertiesPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
								  "Properties"));
	
	dialog.getContentPane().add(propertiesPane, BorderLayout.CENTER);

	iconPane = new ListeChoixImage(un_sommet);
	caracteristicsPane.add(iconPane);
	
	ligne_non_editable(propertiesPane,
			   "Degree :", 
			   Integer.toString(un_sommet.getSommet().degre()));
    }
    
    
    public void actionPerformed(ActionEvent evt) {
	if (evt.getSource() == tailleTextField) {
	    try {
		cote =Integer.parseInt(tailleTextField.getText());
	    } catch (NumberFormatException e) {
		JOptionPane.showMessageDialog(dialog,
					      "Bad argument type for the\n"
					      + ((SommetCarre)forme).getEtiquette().toLowerCase()
					      + "\nAn integer is waited.",
					      "Error",
					      JOptionPane.ERROR_MESSAGE);
		tailleTextField.setText(Integer.toString(cote));
	    }
	    elementModified();
	}
	super.actionPerformed(evt);
    }
    
    
    

    public void buttonOk() {
	super.buttonOk();
	((SommetCarre)forme).setCote(cote);
	if(iconPane.estChangee())
	    ((SommetCarre)forme).changerImage(iconPane.ardoise().image());
    }
}











