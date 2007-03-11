package visidia.gui.presentation.boite;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import visidia.gui.presentation.AreteDessin;
import visidia.gui.presentation.SommetDessin;

/**
 * Cette classe "raffine" sa super classe en lui ajoutant les champs permettant
 * d'afficher et de modifier les caracteristiques specifiques d'une arete :
 */
public class BoiteAreteDessin extends BoiteFormeDessin implements
		ActionListener {

	// Variable d'instance
	/** Le champ de saisie de l'abscisse de l'origine de la flèche. */
	protected JTextField origineX;

	/** Le champ de saisie de l'ordonnée de l'origine de la flèche. */
	protected JTextField destinationX;

	/** Le champ de saisie de l'abscisse de la destination de la flèche. */
	protected JTextField origineY;

	/** Le champ de saisie de l'ordonnée de la destination de la flèche. */
	protected JTextField destinationY;

	/** La valeur de l'abscisse de l'origine de la flèche. */
	protected int initialStartX;

	/** La valeur de l'ordonnée de l'origine de la flèche. */
	protected int initialStartY;

	/** La valeur de l'abscisse de la destination de la flèche. */
	protected int initialEndX;

	/** La valeur de l'ordonnée de la destination de la flèche. */
	protected int initialEndY;

	// Construsteurs
	/**
	 * Cree une nouvelle boite pour afficher les caracteristiques de "une_arete"
	 */
	public BoiteAreteDessin(JFrame parent, AreteDessin une_arete) {
		this(parent, une_arete, "Edge properties", true);
	}

	/**
	 * Cree une nouvelle boite, centrée sur "parent" pour afficher les
	 * caracteristiques de "une_arete". La boite sera appelée "titre" Suivant la
	 * valeur de "est_editable", les caracteristiques de l'arête sont
	 * modifiables.
	 */
	public BoiteAreteDessin(JFrame parent, AreteDessin une_arete, String titre,
			boolean est_editable) {
		super(parent, une_arete, titre, est_editable);
		SommetDessin s = ((AreteDessin) this.forme).getArete().origine()
				.getSommetDessin();
		this.initialStartX = s.centreX();
		this.initialStartY = s.centreY();
		s = ((AreteDessin) this.forme).getArete().destination()
				.getSommetDessin();
		this.initialEndX = s.centreX();
		this.initialEndY = s.centreY();

		this.origineX = this.ligne_editable(this.caracteristicsPane,
				"Origin X :", Integer.toString(this.initialStartX),
				est_editable);
		this.origineX.addActionListener(this);

		this.origineY = this.ligne_editable(this.caracteristicsPane,
				"Origin Y :", Integer.toString(this.initialStartY),
				est_editable);
		this.origineY.addActionListener(this);

		this.destinationX = this.ligne_editable(this.caracteristicsPane,
				"Destination X :", Integer.toString(this.initialEndX),
				est_editable);
		this.destinationX.addActionListener(this);

		this.destinationY = this.ligne_editable(this.caracteristicsPane,
				"Destination Y :", Integer.toString(this.initialEndY),
				est_editable);
		this.destinationY.addActionListener(this);
	}

	// Methodes

	/**
	 * Cette méthode est appelée quand l'utilisateur actionne un des boutons de
	 * la boite.
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		// int x1, y1, x2, y2;
		if (e.getSource() == this.origineX) {
			try {
				/* x1 = */Integer.parseInt(this.origineX.getText());
			} catch (NumberFormatException exception) {
				JOptionPane.showMessageDialog(this.dialog,
						"Bad argument type for starting point X:\n"
								+ "An integer is expected.", "Error",
						JOptionPane.ERROR_MESSAGE);
				this.origineX.setText(Integer.toString(this.initialStartX));
			}
			this.elementModified();
		}
		if (e.getSource() == this.origineY) {
			try {
				/* y1 = */Integer.parseInt(this.origineY.getText());
			} catch (NumberFormatException exception) {
				JOptionPane.showMessageDialog(this.dialog,
						"Bad argument type for starting point Y:\n"
								+ "An integer is expected.", "Error",
						JOptionPane.ERROR_MESSAGE);
				this.origineY.setText(Integer.toString(this.initialStartY));
			}
			this.elementModified();
		}
		if (e.getSource() == this.destinationX) {
			try {
				/* x2 = */Integer.parseInt(this.destinationX.getText());
			} catch (NumberFormatException exception) {
				JOptionPane.showMessageDialog(this.dialog,
						"Bad argument type for ending point X:\n"
								+ "An integer is expected.", "Error",
						JOptionPane.ERROR_MESSAGE);
				this.destinationX.setText(Integer.toString(this.initialEndX));
			}
			this.elementModified();
		}
		if (e.getSource() == this.destinationY) {
			try {
				/* y2 = */Integer.parseInt(this.destinationY.getText());
			} catch (NumberFormatException exception) {
				JOptionPane.showMessageDialog(this.dialog,
						"Bad argument type for ending point Y:\n"
								+ "An integer is expected.", "Error",
						JOptionPane.ERROR_MESSAGE);
				this.destinationY.setText(Integer.toString(this.initialEndY));
			}
			this.elementModified();
		}
		super.actionPerformed(e);
	}

	/**
	 * Cette méthode est appelée quand l'utilisateur appuie sur le bouton Ok
	 */
	public void buttonOk() {
		int x1, y1, x2, y2;
		try {
			x1 = Integer.parseInt(this.origineX.getText());
		} catch (NumberFormatException exception) {
			throw new NumberFormatException(
					"Bad argument type for starting point X:\nAn integer is expected.");
		}
		try {
			y1 = Integer.parseInt(this.origineY.getText());
		} catch (NumberFormatException exception) {
			throw new NumberFormatException(
					"Bad argument type for end point Y:\nAn integer is expected.");
		}
		try {
			x2 = Integer.parseInt(this.destinationX.getText());
		} catch (NumberFormatException exception) {
			throw new NumberFormatException(
					"Bad argument type for starting point X:\nAn integer is expected.");
		}
		try {
			y2 = Integer.parseInt(this.destinationY.getText());
		} catch (NumberFormatException exception) {
			throw new NumberFormatException(
					"Bad argument type for end point Y:\nAn integer is expected.");
		}

		super.buttonOk();

		((AreteDessin) this.forme).getArete().origine().getSommetDessin()
				.placer(x1, y1);
		((AreteDessin) this.forme).getArete().destination().getSommetDessin()
				.placer(x2, y2);

	}

	public void origineXSetEditable(boolean t) {
		this.origineX.setEditable(t);
	}

	public void origineYSetEditable(boolean t) {
		this.origineY.setEditable(t);
	}

	public void destinationXSetEditable(boolean t) {
		this.destinationX.setEditable(t);
	}

	public void destinationYSetEditable(boolean t) {
		this.destinationY.setEditable(t);
	}

}
