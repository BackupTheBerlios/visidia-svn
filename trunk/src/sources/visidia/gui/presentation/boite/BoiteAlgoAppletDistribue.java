package visidia.gui.presentation.boite;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import visidia.gui.metier.inputOutput.OpenAlgoAppletDistribue;
import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulationDist;

/**
 * This class creates a dialog box that load algorithms from an applet
 */
public class BoiteAlgoAppletDistribue implements ActionListener {

	/** La fenetre parent : la boite sera centree sur cette fenetre. */
	protected FenetreDeSimulationDist parent;

	/** Le JDialog dans lequel on va tout afficher. */
	protected JDialog dialog;

	/** Le bouton Cancel */
	protected JButton buttonCancel;

	/** Le bouton Open */
	protected JButton buttonOpen;

	/** La table d'algo */
	protected JList liste;

	// Constructeurs

	public BoiteAlgoAppletDistribue(FenetreDeSimulationDist fenetre,
			Vector tableAlgo) {
		this(fenetre, tableAlgo, "Loading algorithms");
	}

	public BoiteAlgoAppletDistribue(FenetreDeSimulationDist fenetre,
			Vector tableAlgo, String titre) {
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
				OpenAlgoAppletDistribue.setAlgorithm((String) (this.liste
						.getSelectedValue()));
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
