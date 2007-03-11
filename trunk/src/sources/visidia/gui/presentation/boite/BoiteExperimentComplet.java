package visidia.gui.presentation.boite;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulationDist;

/**
 * Cette classe cree une boite pour specifier l'emplacement des serveurs
 * relatifs a chaque noeud du graphe.
 */
public class BoiteExperimentComplet implements ActionListener {

	protected FenetreDeSimulationDist parent;

	/** Le JDialog dans lequel on va tout afficher. */
	protected JDialog dialog;

	/** Le bouton Ok */
	protected JButton buttonOk;

	/** Le bouton Cancel */
	protected JButton buttonCancel;

	protected JPanel panel, buttonPane;

	protected JLabel jl;

	protected JTextField sizeField;

	// protected HashTable table;

	// Constructeurs
	public BoiteExperimentComplet(FenetreDeSimulationDist parent, String titre) {

		this.dialog = new JDialog(parent, titre);
		this.parent = parent;
		this.dialog.getContentPane().setLayout(new GridLayout(2, 0));
		this.ajouterBoutons();

	}

	// Methodes
	/** Affiche la boite et la centre par rapport a "parent". */
	public void show(Frame parent) {
		this.dialog.pack();
		this.dialog.setVisible(true);
		this.dialog.setLocationRelativeTo(parent);
	}

	/**
	 * Ajoute les boutons en bas de la boite. Suivant "est_editable", certains
	 * d'entre eux seront actifs ou non.
	 */
	public void ajouterBoutons() {

		this.buttonPane = new JPanel(new FlowLayout());
		this.panel = new JPanel(new GridLayout(1, 2));

		// ajout des bouttons ok et cancel
		this.buttonOk = new JButton("Ok");
		this.buttonOk.addActionListener(this);

		this.buttonCancel = new JButton("Cancel");
		this.buttonCancel.addActionListener(this);

		// ajout des champs de saisie de l'emplacement du serveur
		// pour chaque noued du graphe
		this.jl = new JLabel("graph size : ");
		this.sizeField = new JTextField("");

		this.panel.add(this.jl);
		this.panel.add(this.sizeField);

		this.buttonPane.add(this.buttonOk);
		this.buttonPane.add(this.buttonCancel);

		this.dialog.getContentPane().add(this.panel, BorderLayout.CENTER);
		this.dialog.getContentPane().add(this.buttonPane, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.buttonOk) {
			String size = this.sizeField.getText();
			this.parent.setExperimentSize(new Integer(size));

			System.out.println(size);
			this.dialog.setVisible(false);
			this.dialog.dispose();
		}

		if (e.getSource() == this.buttonCancel) {
			this.dialog.setVisible(false);
			this.dialog.dispose();
		}
	}

	/** Retourne le JDialog. */
	public JDialog dialog() {
		return this.dialog;
	}
}
