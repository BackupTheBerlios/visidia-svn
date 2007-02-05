package visidia.gui.presentation.boite;

import visidia.gui.presentation.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/** Cette classe "raffine" sa super classe en lui ajoutant les champs
 * permettant d'afficher et de modifier les caracteristiques
 * specifiques d'un sommet cercle
 **/

public class BoiteSommetCircle extends BoiteSommetDessin implements ActionListener {
    
    /** Le panel dans lequel on affiche les propriétés
     * (caracteristiques non graphiques) du sommet.*/
    protected JPanel propertiesPane;
    /** Un label dont on sera amené à modifier le texte */
    protected JLabel tailleLabel;
    /** Un champ de saisie pour le taille du sommet.*/
    protected JTextField tailleTextField;
    
    /* on liste les champs qui sont suceptibles d'etre modifies pas la
     * classe */
    private int cote;
       

    /** Cree une nouvelle boite pour afficher les caracteristiques de
     * "un_sommet"*/
    public BoiteSommetCircle(JFrame parent, SommetCircle un_sommet) {
	this(parent, un_sommet, "Vertex properties", true);
    }
    
    /** Cree une nouvelle boite, centrée sur "parent" pour afficher
     * les caracteristiques de "un_sommet".  La boite sera appelée
     * "titre" Suivant la valeur de "est_editable", les
     * caracteristiques du sommet sont modifiables.*/
    public BoiteSommetCircle(JFrame parent, SommetCircle un_sommet, String titre,
		       boolean est_editable) {
	
	super(parent, un_sommet, titre, est_editable);
	cote = ((SommetCircle)forme).getCote();
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
					      + ((SommetCircle)forme).getEtiquette().toLowerCase()
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
	((SommetCircle)forme).setCote(cote);
    }
}
