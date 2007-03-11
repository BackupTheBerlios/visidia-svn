package visidia.gui.presentation.boite;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import visidia.gui.donnees.PropertyTableModel;
import visidia.gui.donnees.TableCouleurs;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.userInterfaceSimulation.FenetreDeSimulationDist;

/**
 * Cette classe créee une boite utilisée pour modifier l'état d'un ou de
 * plusieurs sommets selectionné elle est applée quand on ne selectionne que des
 * sommets et qu'on appui sur le bouton info
 */
public class BoiteChangementEtatSommetDist implements ActionListener,
		ItemListener {

	/** La fenetre parent : la boite sera centrée sur cette fenetre. */
	protected FenetreDeSimulationDist parent;

	/** Le JDialog dans lequel on va tout afficher. */
	protected JDialog dialog;

	/** Le bouton Ok */
	protected JButton buttonOk;

	/** Le bouton Cancel */
	protected JButton buttonCancel;

	/** Le bouton Apply */
	protected JButton buttonApply;

	/** the button for changing the algorithms */
	protected JButton buttonChange;

	/** The label for displaying the algorithm used */
	protected JLabel algoUsed;

	protected SommetDessin monSommet;

	protected int vertex_id = -1;

	protected EtatPanelDist etatPanel;

	/** a table for the state */
	protected JTable table = new JTable();

	/** failure and drawing information */
	protected JCheckBox but_failure, but_drawMessage;

	protected boolean hasFailure = false;

	protected boolean drawMessage = true;

	protected boolean hasFailureOldValue = this.hasFailure;

	protected boolean drawMessageOldValue = this.drawMessage;

	public BoiteChangementEtatSommetDist(FenetreDeSimulationDist parent,
			SommetDessin sommet) {
		this(parent, sommet, "Vertex properties state");
	}

	public BoiteChangementEtatSommetDist(FenetreDeSimulationDist p,
			SommetDessin sommet, String titre) {

		this.dialog = new JDialog(p, titre);
		this.parent = p;

		this.monSommet = sommet;

		this.vertex_id = Integer.valueOf(sommet.getEtiquette()).intValue();

		// Determination of the algorithm name used
		String algoString = new String();
		// if (parent.getAlgorithms().getAlgorithm(vertex_id) == null)
		algoString = "None";
		// else algoString =
		// parent.getAlgorithms().getAlgorithm(vertex_id).getClass().getName();

		this.algoUsed = new JLabel("Algorithm used : " + algoString);

		// the panel taht contains the different colors for the vertice
		// and the vertice label too.
		this.etatPanel = new EtatPanelDist(TableCouleurs.getTableCouleurs(),
				this);

		// failure interface
		// 2004
		// hasFailure = monSommet.getFailure();
		this.hasFailureOldValue = this.hasFailure;
		this.but_failure = new JCheckBox("Vertex failure", this.hasFailure);
		this.but_failure.addItemListener(this);

		this.drawMessage = this.monSommet.getDrawMessage();
		this.drawMessageOldValue = this.drawMessage;
		this.but_drawMessage = new JCheckBox("Draw sending Message",
				this.drawMessage);
		this.but_drawMessage.addItemListener(this);

		Panel panelHaut = new Panel();
		panelHaut.setLayout(new BorderLayout());
		panelHaut.add(this.etatPanel, BorderLayout.NORTH);
		panelHaut.add(this.but_failure, BorderLayout.CENTER);
		panelHaut.add(this.but_drawMessage, BorderLayout.SOUTH);

		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane spane = new JScrollPane(this.table);

		JPanel panelCentre = new JPanel();
		panelCentre.setLayout(new BorderLayout());
		panelCentre.add(spane, BorderLayout.NORTH);
		panelCentre.add(this.algoUsed, BorderLayout.CENTER);

		this.setProperties((this.monSommet.getStateTable()));

		this.dialog.getContentPane().setLayout(new BorderLayout());
		this.dialog.getContentPane().add(panelHaut, BorderLayout.NORTH);
		this.dialog.getContentPane().add(panelCentre, BorderLayout.CENTER);

		this.dialog.setSize(400, 200);

		this.ajouterBoutons();
	}

	// Methodes

	/** setting the state */
	public void setProperties(Hashtable props) {
		this.table.setModel(new PropertyTableModel(props));
	}

	/** Affiche la boite et la centre par rapport a "parent". */
	public void show(Frame parent) {
		this.dialog.pack();
		this.dialog.setVisible(true);
		this.dialog.setLocationRelativeTo(parent);
	}

	/** Ajoute un bouton nomme "label" au panel "pane" */
	public JButton addButton(JPanel pane, String label) {
		JPanel tmp = new JPanel(new FlowLayout());
		JButton button = new JButton(label);
		tmp.add(button);
		button.setSize(button.getMinimumSize());
		pane.add(tmp);
		pane.add(Box.createRigidArea(new Dimension(0, 5)));
		return button;
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
		this.buttonApply.setEnabled(true);
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

	public void elementModified() {
		PropertyTableModel mod = (PropertyTableModel) this.table.getModel();
		mod.putProperty("label", this.etatPanel.ardoise().donneEtat());
		if (this.hasFailure) {
			mod.putProperty("failure", "yes");
		} else {
			mod.putProperty("failure", "no");
		}

		if (this.drawMessage) {
			mod.putProperty("draw messages", "yes");
		} else {
			mod.putProperty("draw messages", "no");
		}
	}

	/** Cette methode est appelee si l'utilisateur appuie sur le bouton Ok. */
	public void buttonOk() {
		String etat = this.etatPanel.ardoise().donneEtat();
		PropertyTableModel mod = (PropertyTableModel) this.table.getModel();
		int nbRows = mod.getRowCount();
		this.monSommet.setEtat(etat);
		// 2004
		// monSommet.setFailure(hasFailure);
		this.monSommet.setDrawMessage(this.drawMessage);

		try {
			for (int i = 0; i < nbRows; i++) {
				this.table.editCellAt(i, 1); // read the new values edited
				this.monSommet.setValue((String) mod.getValueAt(i, 0), mod
						.getValueAt(i, 1));
			}
		} catch (Exception exc) {
			System.out.println(" Problem in Box : " + exc);
		}
		// if failure state has changed: notify the simulator
		if (this.hasFailureOldValue != this.hasFailure) {
			this.parent.nodeStateChanged(Integer.valueOf(
					this.monSommet.getEtiquette()).intValue(), mod
					.getProperties());
		}

		if (this.drawMessageOldValue != this.drawMessage) {
			this.parent.nodeStateChanged(Integer.valueOf(
					this.monSommet.getEtiquette()).intValue(), mod
					.getProperties());
		}

		this.parent.simulationPanel().repaint();
	}

	/** Retourne le JDialog. */
	public JDialog dialog() {
		return this.dialog;
	}

	public void itemStateChanged(ItemEvent evt) {
		if ((JCheckBox) evt.getSource() == this.but_failure) {
			this.hasFailure = !this.hasFailure;
			this.elementModified();
		}
		if ((JCheckBox) evt.getSource() == this.but_drawMessage) {
			this.drawMessage = !this.drawMessage;
			this.elementModified();
		}
	}
}
