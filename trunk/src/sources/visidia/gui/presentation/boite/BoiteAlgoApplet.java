package visidia.gui.presentation.boite;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import visidia.gui.metier.inputOutput.OpenAlgoApplet;
import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulation;

/**
 * This class creates a dialog box that load algorithms from an applet
 */
public class BoiteAlgoApplet implements ActionListener {

	/** La fenetre parent : la boite sera centree sur cette fenetre. */
	protected FenetreDeSimulation parent;

	/** Le JDialog dans lequel on va tout afficher. */
	protected JDialog dialog;

	/** Le bouton Cancel */
	protected JButton buttonCancel;

	/** Le bouton Open */
	protected JButton buttonOpen;

	/** La table d'algo */
	protected JList liste;

	/* enumeration de sommets */
	protected Enumeration enumOfVertices = null;

	// Constructeurs

	public BoiteAlgoApplet(FenetreDeSimulation fenetre, Vector tableAlgo) {
		this(fenetre, tableAlgo, "Loading algorithms");
	}

	// ouverture de la boite opour une enumeration de sommets
	public BoiteAlgoApplet(FenetreDeSimulation fenetre, Vector tableAlgo,
			Enumeration enumOfVertices) {
		this(fenetre, tableAlgo, "Loading algorithms for vertices");
		this.enumOfVertices = enumOfVertices;
	}

	public BoiteAlgoApplet(FenetreDeSimulation fenetre, Vector tableAlgo,
			String titre) {
		this.parent = fenetre;
		this.dialog = new JDialog(this.parent, titre);

		this.liste = new JList(tableAlgo);
		this.liste.setSize(600, 400);

		JScrollPane listScrollPane = new JScrollPane(this.liste);

		// dialog.setLayout(new FlowLayout(FlowLayout.RIGHT,5,5));
		this.dialog.getContentPane().setLayout(new BorderLayout());
		this.dialog.getContentPane().add(listScrollPane, BorderLayout.CENTER);
		this.dialog.setSize(600, 400);

		this.ajouterBoutons();
		this.dialog.setVisible(true);
	}

	/** Affiche la boite et la centre par rapport a "parent". */
	public void show() {
		this.dialog.pack();
		this.dialog.setVisible(true);
		this.dialog.setLocationRelativeTo(null);
	}

	public void ajouterBoutons() {
		JPanel buttonPane = new JPanel(new FlowLayout());

		this.buttonOpen = new JButton("Open");
		this.buttonOpen.addActionListener(this);

		this.buttonCancel = new JButton("Cancel");
		this.buttonCancel.addActionListener(this);

		buttonPane.add(this.buttonOpen);
		buttonPane.add(this.buttonCancel);

		this.dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.buttonOpen) {
			try {
				if (this.enumOfVertices == null) {
					OpenAlgoApplet.setAlgorithm((String) (this.liste
							.getSelectedValue()));
				} else {
					OpenAlgoApplet.setAlgorithmForVertices((String) (this.liste
							.getSelectedValue()), this.enumOfVertices);
				}
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

		if (e.getSource() == this.buttonCancel) {
			this.dialog.setVisible(false);
			this.dialog.dispose();
		}
	}

}
