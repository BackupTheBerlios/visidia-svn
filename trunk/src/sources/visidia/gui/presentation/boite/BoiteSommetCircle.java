package visidia.gui.presentation.boite;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import visidia.gui.presentation.SommetCircle;

/**
 * Cette classe "raffine" sa super classe en lui ajoutant les champs permettant
 * d'afficher et de modifier les caracteristiques specifiques d'un sommet cercle
 */

public class BoiteSommetCircle extends BoiteSommetDessin implements
		ActionListener {

	/**
	 * Le panel dans lequel on affiche les propriétés (caracteristiques non
	 * graphiques) du sommet.
	 */
	protected JPanel propertiesPane;

	/** Un label dont on sera amené à modifier le texte */
	protected JLabel tailleLabel;

	/** Un champ de saisie pour le taille du sommet. */
	protected JTextField tailleTextField;

	/*
	 * on liste les champs qui sont suceptibles d'etre modifies pas la classe
	 */
	private int cote;

	/**
	 * Cree une nouvelle boite pour afficher les caracteristiques de "un_sommet"
	 */
	public BoiteSommetCircle(JFrame parent, SommetCircle un_sommet) {
		this(parent, un_sommet, "Vertex properties", true);
	}

	/**
	 * Cree une nouvelle boite, centrée sur "parent" pour afficher les
	 * caracteristiques de "un_sommet". La boite sera appelée "titre" Suivant la
	 * valeur de "est_editable", les caracteristiques du sommet sont
	 * modifiables.
	 */
	public BoiteSommetCircle(JFrame parent, SommetCircle un_sommet,
			String titre, boolean est_editable) {

		super(parent, un_sommet, titre, est_editable);
		this.cote = ((SommetCircle) this.forme).getCote();
		JPanel taillePanel = new JPanel(new GridLayout(1, 2));
		this.tailleLabel = new JLabel("Size");
		taillePanel.add(this.tailleLabel);
		this.tailleTextField = new JTextField(Integer.toString(this.cote), 6);
		this.tailleTextField.addActionListener(this);
		taillePanel.add(this.tailleTextField);
		this.caracteristicsPane.add(taillePanel);

		this.propertiesPane = new JPanel();
		BoxLayout propertiesLayout = new BoxLayout(this.propertiesPane,
				BoxLayout.Y_AXIS);
		this.propertiesPane.setLayout(propertiesLayout);
		this.propertiesPane.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Properties"));

		this.dialog.getContentPane().add(this.propertiesPane,
				BorderLayout.CENTER);

		this.ligne_non_editable(this.propertiesPane, "Degree :", Integer
				.toString(un_sommet.getSommet().degre()));
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == this.tailleTextField) {
			try {
				this.cote = Integer.parseInt(this.tailleTextField.getText());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this.dialog,
						"Bad argument type for the\n"
								+ ((SommetCircle) this.forme).getEtiquette()
										.toLowerCase()
								+ "\nAn integer is waited.", "Error",
						JOptionPane.ERROR_MESSAGE);
				this.tailleTextField.setText(Integer.toString(this.cote));
			}
			this.elementModified();
		}
		super.actionPerformed(evt);
	}

	public void buttonOk() {
		super.buttonOk();
		((SommetCircle) this.forme).setCote(this.cote);
	}
}
