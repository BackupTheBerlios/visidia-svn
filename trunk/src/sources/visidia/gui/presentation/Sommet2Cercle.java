package visidia.gui.presentation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JFrame;

import visidia.gui.donnees.TableCouleurs;
import visidia.gui.metier.Sommet;
import visidia.gui.presentation.boite.BoiteFormeDessin;
import visidia.gui.presentation.boite.BoiteSommet2Cercle;

/** Raffine sa super-classe pour representer un sommet sous la forme d'un carre. */
public class Sommet2Cercle extends SommetDessin {

	// Variables d'instance.

	/**
	 * 
	 */
	private static final long serialVersionUID = -704655892882655775L;

	protected static int monCote = 20; // represente le cote d'un carre

	// Constructeurs

	// Instancier un nouveau sommet sans etiquette

	public Sommet2Cercle(VueGraphe vg, int posx, int posy, String etiquette,
			Sommet s) {
		super(vg, posx, posy, etiquette, s);
		this.stateTable.put("id", etiquette);
	}

	// constructor which also creates a new Sommet corresponding to this
	// FormeDessin
	public Sommet2Cercle(VueGraphe vg, int posx, int posy, String etiquette) {
		this(vg, posx, posy, etiquette, new Sommet(vg.getGraphe()));
	}

	// Methodes

	// Retourne la forme du sommet
	public String forme() {
		return new String("Two Circles");
	}

	// Agrandit la taille du sommet de "coef" fois
	public void agrandir(float coef) {
		if (coef > 0)
			monCote = (int) (coef * monCote);
	}

	// Retourne le cote du sommet routage
	public int getCote() {
		return monCote;
	}

	// Modifie le cote du sommet routage
	public void setCote(int cote) {
		monCote = cote;
		this.vueGraphe.setFontSize(((float) monCote / 3) + 1);
	}

	public void dessiner(Component c, Graphics g, String tag) {
		this.dessiner(c, g);
		g.setColor(Color.black);
		if (tag.equals("agents")) {
			// draw on vertices the number of agents
			String mesg = this.getNbr();
			// System.out.println(mesg);
			int stringSize = (int) (g.getFontMetrics().getStringBounds(mesg, g)
					.getWidth());
			g.drawString(mesg, this.centreX() - stringSize / 2, this.centreY()
					+ this.getCote() / 4);
		}
	}

	// Dessine le sommet sur le Graphics passe en argument
	public void dessiner(Component c, Graphics g) {
		int x = this.centreX();
		int y = this.centreY();
		int _rayon = this.getCote();

		// on fait l'interieur
		if (this.est_enlumine())
			g.setColor(this.couleur_trait);
		else
			g.setColor(this.couleur_fond);

		// La bordure est rouge si enluminerBis
		if (this.est_enlumineBis())
			g.setColor(Color.red);

		// g.fillOval(posx-rayon,posy-rayon,2*rayon,2*rayon);
		g.fillOval(this.posx - _rayon / 2 - 4, this.posy - _rayon / 2 - 2,
				_rayon + 8, _rayon + 8);

		// Dessin de la forme carre du sommet
		if (this.est_enlumine())
			g.setColor(this.couleur_fond);
		else
			g.setColor(this.couleur_trait);

		// La bordure est rouge si enluminerBis
		if (this.est_enlumineBis())
			g.setColor(Color.red);

		// g.drawOval(posx-rayon,posy-rayon,2*rayon,2*rayon);
		g.drawOval(this.posx - _rayon / 2 - 4, this.posy - _rayon / 2 - 2,
				_rayon + 8, _rayon + 8);

		// affichage de l'etiquette
		if ((this.getVueGraphe()).afficherEtiquettes()) {
			g.setColor(Color.blue);
			if (this.est_enlumine())
				g.setFont((this.getVueGraphe()).fontGras());
			else
				g.setFont((this.getVueGraphe()).fontNormal());
			// dans ancienne version y avait ca :
			// g.drawString((getEtiquette()+ " , "+((String)getValue("arg
			// 1")))+" ,",x - rayon , y + rayon + 12);
			g.drawString((this.getEtiquette()) + " , " + this.getEtat(), x
					- _rayon / 2, y + _rayon + 8);

		}
		// affichage d'une icone si elle existe
		if (this.getImage() != null)
			(this.getImage()).paintIcon(c, g, x - _rayon / 2, y - _rayon / 2);

		g.setColor((Color) ((TableCouleurs.getTableCouleurs()).get(this
				.getEtat())));

		g.fillOval(x - _rayon / 2, y - _rayon / 2 + 2, _rayon, _rayon);
		// g.setColor(Color.blue);
		// g.drawString(etat, x -Rayon/2 + 20, y + Rayon + 8);

	}

	// Teste si le point donne en parametre appartient au sommet
	public boolean appartient(int x, int y) {
		return ((((this.posx - x) * (this.posx - x)) + ((this.posy - y) * (this.posy - y))) <= (monCote * monCote));
	}

	// Teste si le sommet est contenu en entier dans une zone rectangulaire.
	// (x1, y1) coordonnees en haut a gauche et (x2, y2) coordonnees en bas a
	// droite.
	public boolean estDansRegion(int x1, int y1, int x2, int y2) {
		return (((x1 <= (this.posx - monCote)) && (y1 <= this.posy - monCote)) && ((x2 >= (this.posx + monCote)) && (y2 >= this.posy
				+ monCote / 2)));
	}

	// returns the distance from the center to determines the positions of edges
	public float rayon(float angle) {
		float tempo = Math.abs(angle);

		tempo = Math.min(tempo, (float) Math.PI - tempo);
		return monCote
				/ (float) (2 * Math.cos(Math.min(tempo, (float) Math.PI / 2
						- tempo)));
	}

	/**
	 * Renvoie sous la forme d'une string les caracteristiques graphiques du
	 * sommet. coordonnees, forme, Rayon, couleur du trait, couleur
	 * d'enlumination.
	 */
	public String graphicProperties() {
		return new String("(" + Integer.toString(this.centreX()) + ","
				+ Integer.toString(this.centreY()) + ")\t" + this.forme()
				+ "\t" + Integer.toString(this.getCote()) + "\t"
				+ this.couleurTrait().getRed() + ","
				+ this.couleurTrait().getGreen() + ","
				+ this.couleurTrait().getBlue() + "\t"
				+ +this.couleurFond().getRed() + ","
				+ this.couleurFond().getGreen() + ","
				+ this.couleurFond().getBlue());
	}

	/*
	 * methode qui cree et renvoie une copie du sommet cette copie est placee
	 * dans la liste d'affichage
	 */

	public Object cloner(VueGraphe vueGraphe) {
		Sommet le_clone = (Sommet) this.getSommet().cloner(
				vueGraphe.getGraphe());
		Sommet2Cercle sommetRetour = new Sommet2Cercle(vueGraphe, this.posx,
				this.posy, new String(this.monEtiquette), le_clone);
		sommetRetour.copyAllVariable(this);
		return sommetRetour;
	}

	// method which copy all the variable from the Sommet2Cercle given in
	// parameters
	public void copyAllVariable(Sommet2Cercle s) {
		Sommet2Cercle.monCote = s.getCote();
		super.copyAllVariable(s);
	}

	/**
	 * Retourne une fenetre 'BoiteFormeDessin' qui indique les proprietes du
	 * sommet. Appele les methodes pack() et show() pour afficher la fenetre.
	 * 
	 * Certaines proprietes peuvent eventuellement etre modifiables (laisse a
	 * l'appreciation du programmeur).
	 */
	public BoiteFormeDessin proprietes(JFrame parent) {
		return new BoiteSommet2Cercle(parent, this);
	}
}
