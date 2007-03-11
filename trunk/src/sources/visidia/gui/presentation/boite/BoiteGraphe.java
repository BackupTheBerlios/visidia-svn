package visidia.gui.presentation.boite;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import visidia.gui.presentation.userInterfaceEdition.Editeur;

/**
 * Cette boite affiche les proprietes d'un graphe et de son editeur (couleur de
 * fond, taille...) Elle permet aussi de les modifier.
 */
public class BoiteGraphe extends Boite implements ActionListener,
		ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2720159674297753898L;

	/** Le bouton "Apply", qui permet d'appliquer les changements. */
	protected JButton applyButton;

	/** Le bouton de choix de la couleur de fond. */
	protected JButton colorChooserButton;

	/** Le curseur permettant de regler la largeur du plan de travail. */
	protected JSlider x_slider;

	/** Le curseur permettant de regler la hauteur du plan de travail. */
	protected JSlider y_slider;

	protected JLabel colorLabel, heightLabel, widthLabel;

	/** L'editeur dont on affiche les proprietes. */
	protected Editeur editeur;

	/**
	 * Un booleen qui vaut VRAI si une des valeurs affichees dans la boite est
	 * differente de la valeur effective.
	 */
	protected boolean modif = false;

	/** La valeur de la largeur du plan de travail affichee dans la boite. */
	protected int new_width;

	/** La valeur de la hauteur du plan de travail affichee dans la boite. */
	protected int new_height;

	/** La couleur du plan de travail affichee dans la boite. */
	protected Color newColor;

	/** Cree une nouvelle boite. */
	public BoiteGraphe(Editeur parent) {

		super(parent, "Document Properties", JOptionPane.OK_CANCEL_OPTION);
		this.editeur = parent;
	}

	public static void show(Editeur parent) {
		BoiteGraphe b = new BoiteGraphe(parent);
		b.showDialog();
	}

	public JComponent createContent() {

		JPanel mainPane = new JPanel();
		BorderLayout mainLayout = new BorderLayout();
		mainLayout.setVgap(10);
		mainPane.setLayout(mainLayout);

		JPanel infoPane = new JPanel(new GridLayout(3, 2));
		infoPane.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "Graph caracteritics"));

		// infoPane.add(new JLabel("Graph model :"));
		// infoPane.add(new JLabel(editeur.graph().type()));
		infoPane.add(new JLabel("Number of vertices :"));
		infoPane
				.add(new JLabel(Integer.toString(this.editeur.graph().ordre())));
		infoPane.add(new JLabel("Number of edges :"));
		infoPane
				.add(new JLabel(Integer.toString(this.editeur.graph().taille())));

		mainPane.add(infoPane, BorderLayout.NORTH);

		JPanel gfxPropPane = new JPanel(new GridLayout(3, 2));

		gfxPropPane.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "Other document properties"));

		this.colorLabel = new JLabel("Background color : "
				+ Integer.toString(this.editeur.couleur_de_fond().getRed())
				+ ", "
				+ Integer.toString(this.editeur.couleur_de_fond().getBlue())
				+ ", "
				+ Integer.toString(this.editeur.couleur_de_fond().getGreen()));
		this.newColor = this.editeur.couleur_de_fond();
		this.colorChooserButton = new JButton("Change color");
		this.colorChooserButton.addActionListener(this);

		this.new_width = this.editeur.grapheVisuPanel().getPreferredSize().width;
		this.widthLabel = new JLabel("Desk width : "
				+ Integer.toString(this.new_width));
		this.x_slider = new JSlider(0, 2 * this.new_width, this.new_width);
		this.x_slider.addChangeListener(this);

		this.new_height = this.editeur.grapheVisuPanel().getPreferredSize().height;
		this.heightLabel = new JLabel("Desk height : "
				+ Integer.toString(this.new_height));
		this.y_slider = new JSlider(0, 2 * this.new_height, this.new_height);
		this.y_slider.addChangeListener(this);

		gfxPropPane.add(this.colorLabel);
		gfxPropPane.add(this.colorChooserButton);
		gfxPropPane.add(this.widthLabel);
		gfxPropPane.add(this.x_slider);
		gfxPropPane.add(this.heightLabel);
		gfxPropPane.add(this.y_slider);

		mainPane.add(gfxPropPane, BorderLayout.CENTER);

		this.applyButton = new JButton("Apply");
		this.applyButton.addActionListener(this);
		this.applyButton.setEnabled(this.modif);

		mainPane.add(this.applyButton, BorderLayout.SOUTH);

		return mainPane;
	}

	/**
	 * Cette methode est appelee quand l'utilisateur clique sur le bouton
	 * <b>Choose color</b> ou sur le bouton <b>Apply</b>
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.colorChooserButton) {
			Color choosedColor = JColorChooser.showDialog(this.parent,
					"Choose color", this.editeur.couleur_de_fond());
			if (this.newColor != null) {
				this.modif = true;
				this.applyButton.setEnabled(this.modif);
				this.newColor = choosedColor;
			}
			this.colorLabel.setText("Background color : "
					+ Integer.toString(this.newColor.getRed()) + ", "
					+ Integer.toString(this.newColor.getBlue()) + ", "
					+ Integer.toString(this.newColor.getGreen()));
			this.colorChooserButton.setBackground(this.newColor);
		}
		if (e.getSource() == this.applyButton) {
			this.modif = false;
			this.applyButton.setEnabled(this.modif);
			this.boutonOkAppuye();
		}
	}

	/**
	 * Cette methode est appelee quand l'utilisateur actionne l'une des deux
	 * jauges permettant de regler la taille du plan de travail.
	 */
	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource() == this.x_slider) {
			this.modif = true;
			this.applyButton.setEnabled(this.modif);
			this.new_width = this.x_slider.getValue();
			this.widthLabel.setText("Desk width : "
					+ Integer.toString(this.new_width));
		}
		if (evt.getSource() == this.y_slider) {
			this.modif = true;
			this.applyButton.setEnabled(this.modif);
			this.new_height = this.y_slider.getValue();
			this.heightLabel.setText("Desk height : "
					+ Integer.toString(this.new_height));
		}
	}

	public void boutonOkAppuye() {
		this.editeur.change_couleur_de_fond(this.newColor);
		this.editeur.grapheVisuPanel().setBackground(this.newColor);
		this.editeur.grapheVisuPanel().setPreferredSize(
				new Dimension(this.new_width, this.new_height));
		this.editeur.grapheVisuPanel().revalidate();
		this.editeur.grapheVisuPanel().repaint();
	}
}
