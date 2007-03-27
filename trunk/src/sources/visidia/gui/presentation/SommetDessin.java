package visidia.gui.presentation;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Hashtable;

import javax.swing.ImageIcon;

import visidia.gui.metier.Sommet;

/** the geometric shape of a vertex */
public abstract class SommetDessin extends FormeDessin {

	// position of the center of the vertex
	int posx;

	int posy;

	protected boolean drawMessage = true;

	protected ImageIcon uneImage = null;

	protected String monEtiquette; // label(id number) of a vertex 

	protected Hashtable stateTable = new Hashtable();

	protected Hashtable wbTable = new Hashtable();

	// number displayed in the middle of a vertex
	protected String nbr = new String("0");

	// Constructor
	public SommetDessin(VueGraphe vg, int x, int y, String etiquette, Sommet s) {
		this.vueGraphe = vg;
		this.monEtiquette = etiquette;
		this.graphObject = s;
		s.setSommetDessin(this);
		this.placer(x, y);
		this.vueGraphe.insererListeAffichage(this);
		this.stateTable.put("label", "N"); // the state is saved in the table
		this.stateTable.put("draw messages", "yes");
		this.stateTable.put("Visualization", "true");
		this.wbTable.put("Visualization", "true");
		this.wbTable.put("label", "N");
	}

	public SommetDessin(VueGraphe vg, int x, int y, String etiquette) {
		this(vg, x, y, etiquette, new Sommet(vg.getGraphe()));
	}

	// Abstracts methods
	public abstract void dessiner(Component c, Graphics g);

	public abstract void agrandir(float coef);

	public abstract float rayon(float angle);

	// permit to clone the vertex.
	public Object cloner() {
		return this.cloner(this.vueGraphe);
	}

	public abstract Object cloner(VueGraphe vue);

	// Move a vertex of (dx, dy)
	public void deplacer(int dx, int dy) {
		this.placer(this.posx + dx, this.posy + dy);
	}

	// Accessor
	public String type() {
		return new String("vertex");
	}

	public int centreX() {
		return this.posx;
	}

	public int centreY() {
		return this.posy;
	}

	public Point centre() {
		return new Point(this.posx, this.posy);
	}

	public String getEtiquette() {
		return this.monEtiquette;
	}

	public ImageIcon getImage() {
		return this.uneImage;
	}

	public String getEtat() {
		return (String) this.stateTable.get("label");
	}

	public Hashtable getStateTable() {
		return this.stateTable;
	}

	public Hashtable getWhiteBoardTable() {
		return this.wbTable;
	}

	public Sommet getSommet() {
		return ((Sommet) this.graphObject);
	}

	// Modificators
	public void setEtiquette(String etiquette) {
		this.monEtiquette = etiquette;
	}

	public void placer(int x, int y) {
		this.posx = x;
		this.posy = y;
		this.getSommet().repositionnerAretes();
	}

	public void setEtat(String state) {
		this.stateTable.put("label", state);
	}

	public void changerImage(ImageIcon image) {
		this.uneImage = image;
	}

	public void setSommet(Sommet s) {
		this.graphObject = s;
	}

	// modify a value of the table
	public void setValue(String key, Object value) {
		this.stateTable.remove(key);
		this.stateTable.put(key, value);
	}

	public void setWhiteBoardValue(String key, Object value) {
		this.wbTable.remove(key);
		this.wbTable.put(key, value);
	}

	public String getValue(String key) {
		return (String) this.stateTable.get(key);
	}

	public String getWhiteBoardValue(String key) {
		return (String) this.wbTable.get(key);
	}

	// method which copy all the variable from the SommetDessin given in
	// parameters
	public void copyAllVariable(SommetDessin s) {
		this.uneImage = s.getImage();
		this.monEtiquette = s.getEtiquette();
		this.stateTable.put("label", s.getEtat());
		super.copyAllVariable(s);
	}

	/**
	 * Make the fusion of the two vertices
	 */

	public void fusionner(SommetDessin s) {
		this.getSommet().fusionner(s.getSommet());
		this.getVueGraphe().supprimerListeAffichage(s);
		this.getVueGraphe().numero_sommet--;
	}

	public void setDrawMessage(boolean bool) {
		this.drawMessage = bool;
		if (this.drawMessage) {
			this.setValue("draw messages", "yes");
		} else {
			this.setValue("draw messages", "no");
		}
	}

	public boolean getDrawMessage() {
		return this.drawMessage;
	}

	public void setNbr(String nbr) {
		this.nbr = nbr;
	}

	public String getNbr() {
		return this.nbr;
	}

}
