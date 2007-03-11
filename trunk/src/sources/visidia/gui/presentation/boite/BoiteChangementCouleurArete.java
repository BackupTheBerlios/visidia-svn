package visidia.gui.presentation.boite;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import visidia.gui.donnees.conteneurs.Ensemble;
import visidia.gui.presentation.FormeDessin;
import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulation;

/*
 * Cette classe crée une boite utilisée pour modifier l'état    
 * d'un ou de pusieurs arêtes selectionnées sur la fenêtre de    
 * simulation.                                                  
 */
public class BoiteChangementCouleurArete implements ActionListener {

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

	/**
	 * Ce booleen vaut VRAI si une des caracteristiques de l'ObjetVisu a été
	 * modifiée par l'utilisateur, et FAUX sinon.
	 */
	protected boolean modif;

	/**
	 * Une LigneChoixCouleur qui permet d'afficher et de modifier la couleur du
	 * trait de la forme dessine
	 */
	protected JPanel etatPanel;

	/* Une BoiteCouleur qui permet de modifier la couleur */
	protected BoiteCouleur couleur;

	protected FormeDessin forme;

	// Constructeurs
	/**
	 * Crée une nouvelle boite pour afficher les caracteristiques de "un_objet".
	 * Ces caracteristiques seront modifiables.
	 */
	public BoiteChangementCouleurArete(FenetreDeSimulation parent,
			Ensemble uneSelection) {
		this(parent, uneSelection, "Edges properties");
	}

	/**
	 * Crée une nouvelle boite appelée "titre" pour afficher les
	 * caracteristiques de "un_objet".
	 */
	public BoiteChangementCouleurArete(FenetreDeSimulation parent,
			Ensemble uneSelection, String titre) {

		this.dialog = new JDialog(parent, titre);
		this.parent = parent;
		this.modif = false;

		this.selectionAretes = uneSelection;

		this.etatPanel = new JPanel();

		Enumeration e = this.selectionAretes.elements();
		this.forme = (FormeDessin) e.nextElement();
		this.couleur = new BoiteCouleur(this, "Line Color (R,G,B): ",
				this.forme.couleurTrait().getRed(), this.forme.couleurTrait()
						.getGreen(), this.forme.couleurTrait().getBlue(), true);
		this.etatPanel.add(this.couleur.panel());

		this.dialog.getContentPane().setLayout(new BorderLayout());
		this.dialog.getContentPane().add(this.etatPanel, BorderLayout.NORTH);
		this.ajouterBoutons();
	}

	// Methodes

	/** Affiche la boite et la centré par rapport à "parent". */
	public void show(Frame parent) {
		this.dialog.pack();
		this.dialog.setVisible(true);
		this.dialog.setLocationRelativeTo(parent);
	}

	/** Ajoute les boutons en bas de la boite. */
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
		if (e.getSource() == this.buttonOk) {
			try {
				this.buttonOk();
				this.dialog.setVisible(false);
				this.dialog.dispose();
			} catch (NumberFormatException exception) {
				StringTokenizer st = new StringTokenizer(
						exception.getMessage(), "\n");
				int nb_lignes = st.countTokens();
				String message = new String();
				for (int i = 0; i < nb_lignes; i++) {
					message = message + "\n" + st.nextToken();
				}
				JOptionPane.showMessageDialog(this.parent, message, "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		}
		if (e.getSource() == this.buttonApply) {
			try {
				this.buttonOk();
				this.parent.repaint();
				this.modif = false;
				this.buttonApply.setEnabled(false);
			} catch (NumberFormatException exception) {
				StringTokenizer st = new StringTokenizer(
						exception.getMessage(), "\n");
				int nb_lignes = st.countTokens();
				String message = new String();
				for (int i = 0; i < nb_lignes; i++) {
					message = message + "\n" + st.nextToken();
				}
				JOptionPane.showMessageDialog(this.parent, message, "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		}
		if (e.getSource() == this.buttonCancel) {
			this.dialog.setVisible(false);
			this.dialog.dispose();
		}
	}

	/**
	 * Retourne VRAI si une des caracteristiques de l'ObjetVisu a été modifiée,
	 * et FAUX sinon.
	 */
	public void elementModified() {
		this.modif = true;
		this.buttonApply.setEnabled(this.modif);
	}

	/** Cette méthode est appelée si l'utilisateur appuie sur le bouton Ok. */
	public void buttonOk() {
		Enumeration e = this.selectionAretes.elements();
		Color trait;
		try {
			trait = new Color(this.couleur.R, this.couleur.G, this.couleur.B);
		} catch (NumberFormatException exception) {
			throw new NumberFormatException(
					"Bad argument type for background color:\nAn hexadecimal integer with 6 figures is expected.");
		}
		while (e.hasMoreElements()) {
			((FormeDessin) e.nextElement()).changerCouleurTrait(trait);
		}
		this.parent.simulationPanel().repaint();
	}

	/** Retourne le JDialog. */
	public JDialog dialog() {
		return this.dialog;
	}
}
