package visidia.gui.presentation.boite;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import visidia.gui.presentation.SommetDessin;

public class ListeChoixImage extends JPanel implements ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6790781661435062052L;

	Ardoise ardoise;

	boolean imageChangee = false;

	public ListeChoixImage(SommetDessin un_sommet) {
		this.ardoise = new Ardoise(un_sommet.getImage());
		Vector<String> listeItems = new Vector<String>();
		JList liste = new JList();
		JScrollPane listeAvecAscenseur;
		listeItems.addElement("no icon              ");
		listeItems.addElement("PC                   ");
		listeItems.addElement("station              ");
		listeItems.addElement("portable              ");
		listeItems.addElement("server               ");
		listeItems.addElement("mac                  ");

		liste = new JList(listeItems);
		liste.setSelectedIndex(0);
		this.ardoise.image = this.donneImage((String) liste.getSelectedValue());
		liste.addListSelectionListener(this);
		this.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		this.add(this.ardoise);
		listeAvecAscenseur = new JScrollPane(liste);
		listeAvecAscenseur.setPreferredSize(new Dimension(200, 80));
		this.add(listeAvecAscenseur);
		this.setVisible(true);
	}

	public void valueChanged(ListSelectionEvent evt) {
		this.imageChangee = true;
		this.ardoise.changerImage(this.donneImage((String) ((JList) evt
				.getSource()).getSelectedValue()));
		this.ardoise.repaint();
	}

	ImageIcon donneImage(String s) {
		if ((s == null) || s.equals("no icon              ")) {
			return null;
		} else if (s.equals("PC                   ")) {
			return (new ImageIcon("visidia/gui/donnees/images/image1.gif"));
		} else if (s.equals("station              ")) {
			return (new ImageIcon("visidia/gui/donnees/images/image2.gif"));
		} else if (s.equals("printer              ")) {
			return (new ImageIcon("visidia/gui/donnees/images/image3.gif"));
		} else if (s.equals("server               ")) {
			return (new ImageIcon("visidia/gui/donnees/images/image4.gif"));
		} else if (s.equals("mac                  ")) {
			return (new ImageIcon("visidia/gui/donnees/images/image6.jpg"));
		} else {
			return null;
		}

	}

	public Ardoise ardoise() {
		return this.ardoise;
	}

	public boolean estChangee() {
		return this.imageChangee;
	}
}

class Ardoise extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1276808944927505243L;

	JLabel label = new JLabel("Choose the icon");

	ImageIcon image = null;

	Ardoise(ImageIcon une_image) {
		this.image = une_image;
		this.setPreferredSize(new Dimension(200, 60));
		this.add(this.label, BorderLayout.WEST);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.image != null) {
			this.image.paintIcon(this, g, 100, 20);
		}

	}

	public ImageIcon image() {
		return this.image;
	}

	public void changerImage(ImageIcon uneImage) {
		this.image = uneImage;

	}
}
