package visidia.gui.presentation.boite;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EtatPanel extends JPanel implements ListSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8519679276639218002L;

	EtatArdoise ardoise;

	boolean minimumSize = false;

	JList liste;

	Vector listeItems;

	public EtatPanel(Hashtable uneHashtable, VueEtatPanel parent,
			String defaultValue) {
		this(uneHashtable, parent, defaultValue, false);
	}

	/**
	 * - minimiumSize indicates if the panel must be the smallest possible.
	 */
	// PFA2003
	public EtatPanel(Hashtable uneHashtable, VueEtatPanel parent,
			String defaultValue, boolean minimumSize) {
		this.minimumSize = minimumSize;
		this.ardoise = new EtatArdoise(uneHashtable, parent, minimumSize);
		this.listeItems = new Vector();
		this.liste = new JList();
		JScrollPane listeAvecAscenseur;
		this.listeItems.addElement("A");
		this.listeItems.addElement("B");
		this.listeItems.addElement("C");
		this.listeItems.addElement("D");
		this.listeItems.addElement("E");
		this.listeItems.addElement("F");
		this.listeItems.addElement("G");
		this.listeItems.addElement("H");
		this.listeItems.addElement("I");
		this.listeItems.addElement("J");
		this.listeItems.addElement("K");
		this.listeItems.addElement("L");
		this.listeItems.addElement("M");
		this.listeItems.addElement("N");
		this.listeItems.addElement("O");
		this.listeItems.addElement("P");
		this.listeItems.addElement("Q");
		this.listeItems.addElement("R");
		this.listeItems.addElement("S");
		this.listeItems.addElement("T");
		this.listeItems.addElement("U");
		this.listeItems.addElement("V");
		this.listeItems.addElement("W");
		this.listeItems.addElement("X");
		this.listeItems.addElement("Y");
		this.listeItems.addElement("Z");

		this.liste = new JList(this.listeItems);

		this.liste.setSelectedValue(defaultValue, true);
		this.ardoise.changerEtat((String) this.liste.getSelectedValue());
		this.liste.addListSelectionListener(this);

		this.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		this.add(this.ardoise);
		listeAvecAscenseur = new JScrollPane(this.liste);

		// PFA2003
		listeAvecAscenseur.setPreferredSize(minimumSize ? new Dimension(50, 80)
				: new Dimension(200, 80));
		this.add(listeAvecAscenseur);

		this.liste.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				if (((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z'))) {
					String strC = "" + c;
					strC = strC.toUpperCase();
					EtatPanel.this.liste.setSelectedValue(strC, true);
					EtatPanel.this.liste.repaint();
				}
			}
		});

		this.setVisible(true);
	}

	/**
	 * That method must be call when the panel is visible, in order to give the
	 * focus to the list.
	 */
	public void requestFocus() {
		this.liste.requestFocus();
	}

	public EtatPanel(Hashtable uneHashtable, VueEtatPanel parent) {
		this(uneHashtable, parent, "N");
	}

	public void valueChanged(ListSelectionEvent evt) {
		String s = (String) ((JList) evt.getSource()).getSelectedValue();
		this.ardoise.changerEtat(s);
		this.ardoise.repaint();
		this.ardoise.donnePere().elementModified(s);
	}

	public EtatArdoise ardoise() {
		return this.ardoise;
	}
}

class EtatArdoise extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5030524169778288379L;

	VueEtatPanel parent;

	protected String unEtat;

	Hashtable uneHashtable;

	// PFA2003
	boolean minimumSize = false;

	public EtatArdoise(Hashtable dictionnaire, VueEtatPanel parent,
			boolean minimumSize) {
		this.parent = parent;
		this.minimumSize = minimumSize;
		this.uneHashtable = dictionnaire;
		this.setPreferredSize(minimumSize ? new Dimension(50, 50)
				: new Dimension(200, 60));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.uneHashtable.get(this.unEtat) != null) {
			g.setColor((Color) this.uneHashtable.get(this.unEtat));
			if (this.minimumSize)
				g.fillRect(5, 5, 40, 40);
			else
				g.fillRect(100, 20, 40, 40);
		}
	}

	public void changerEtat(String etat) {
		this.unEtat = etat;

	}

	public String donneEtat() {
		return this.unEtat;
	}

	public VueEtatPanel donnePere() {
		return this.parent;
	}
}
