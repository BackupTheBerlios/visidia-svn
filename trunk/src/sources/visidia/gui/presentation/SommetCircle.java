package visidia.gui.presentation;

import javax.swing.*;
import java.awt.*;
import visidia.gui.donnees.*;
import visidia.gui.presentation.boite.*;
import visidia.gui.metier.*;


/** Raffine sa super-classe pour representer un sommet sous la forme d'un cercle.*/
public class SommetCircle extends SommetDessin{

    /**
	 * 
	 */
	private static final long serialVersionUID = 702590799803023307L;
	protected static int monCote = 20; // représente le rayon d'un cercle
    
    
    //Constructeurs
    
    // Instancier un nouveau sommet sans etiquette
    public SommetCircle(VueGraphe vg, int posx, int 
			posy, String etiquette,Sommet s){
	super(vg,posx,posy,etiquette,s);
    }

    // constructor which also creates a new Sommet corresponding to this FormeDessin
    public SommetCircle(VueGraphe vg,int posx, int 
			posy, String etiquette){
	this(vg,posx,posy,etiquette,new Sommet(vg.getGraphe()));
    }
  
    
  
    // Méthodes
  

    // Retourne la forme du sommet
    public String forme() {
	return new String("Circle");
    }

    // Agrandit la taille du sommet de "coef" fois
    public void agrandir(float coef) {
	if (coef > 0)
	    monCote = (int)(coef * monCote);
    }

    // Retourne le cote du sommet carre
    public int getCote() {
	return monCote;
    }
  
    // Modifie le cote du sommet carre
    public void setCote(int cote){
	monCote = cote;
	vueGraphe.setFontSize(((float)cote/3)+1);
    }
    
    public void dessiner(Component c, Graphics g, String tag) {
	this.dessiner(c,g);
	g.setColor(Color.black);
	if(tag.equals("agents")) {
	    // draw on vertices the number of agents
	    String mesg = getNbr();
	    int stringSize = (int)(g.getFontMetrics().
				   getStringBounds(mesg,g).
				   getWidth());
	    g.drawString(mesg,centreX()-stringSize/2 , centreY() );
	}	    
    }
    

    // Dessine le sommet sur le Graphics passe en argument
    public void dessiner(Component c, Graphics g) {
	int x = centreX();
	int y = centreY();
	int cote = getCote();
	int cote_sur_2 = cote / 2;

	// Dessin de la forme cercke du sommet
	if(est_enlumine())
	    g.setColor(couleur_fond);
	else
	    g.setColor(couleur_trait);
    
	// La bordure est rouge si enluminerBis
	if (est_enlumineBis())
	    g.setColor(Color.red);

    
	//dessin du conteur
	g.drawOval(posx - cote_sur_2-1, posy - cote_sur_2-1, 2 * cote_sur_2+2,
		   2 * cote_sur_2+2);
	g.drawOval(posx - cote_sur_2-2, posy - cote_sur_2-2, 2 * cote_sur_2+4,
		   2 * cote_sur_2+4);
	g.drawOval(posx - cote_sur_2, posy - cote_sur_2, 2 * cote_sur_2,
		   2 * cote_sur_2);


	// affichage de l'etiquette
	if((getVueGraphe()).afficherEtiquettes()){
	    g.setColor(Color.blue);
	    if(est_enlumine())
		g.setFont((getVueGraphe()).fontGras());
	    else
		g.setFont((getVueGraphe()).fontNormal());
	    g.drawString((getEtiquette())+" , "+getEtat(),x - cote /2 , y + cote + 8);
	}
	
	//dessin de l'interieur
	g.setColor((Color)((TableCouleurs.getTableCouleurs()).get(getEtat().substring(0,1))));

	g.fillOval(posx - cote_sur_2,posy - cote_sur_2,2 * cote_sur_2,
		   2 * cote_sur_2);

	
    }
   
    // Teste si le point donne en parametre appartient au sommet
    public boolean appartient(int x, int y) {
	return ((Math.abs(posx - x) <= (monCote / 2)) &&
		(Math.abs(posy - y) <= (monCote / 2)));
    }

    // Teste si le sommet est contenu en entier dans une zone rectangulaire.
    // (x1, y1) coordonnees en haut a gauche et (x2, y2) coordonnees en bas a droite.
    public boolean estDansRegion(int x1, int y1, int x2, int y2) {
	return ((x1 <= (posx - (monCote / 2))) && (y1 <= (posy - (monCote / 2))) &&
		(x2 >= (posx + (monCote / 2))) && (y2 >= (posy + (monCote / 2))));
    }

	
    // returns the distance from the center to determines the positions of edges
    public float rayon(float angle){
	float tempo = Math.abs(angle);

	tempo = Math.min(tempo, (float)Math.PI - tempo);
	return monCote / (float)(2*Math.cos(Math.min(tempo, (float)Math.PI / 2 - tempo)));
    }

    /**
     * Renvoie sous la forme d'une string les caracteristiques graphiques du sommet.
     * coordonnees,
     * forme,
     * cote,
     * couleur du trait,
     * couleur d'enlumination.
     **/
    public String graphicProperties() {
	return new String("(" + Integer.toString(centreX()) + ","
			  + Integer.toString(centreY()) + ")\t" 
			  + forme() + "\t"
			  + Integer.toString(getCote()) + "\t" 
			  + couleurTrait().getRed() + "," + couleurTrait().getGreen() + "," + couleurTrait().getBlue() + "\t" +
			  + couleurFond().getRed() + "," + couleurFond().getGreen() + "," + couleurFond().getBlue());
    }

    /* methode qui cree et renvoie une copie du sommet 
     * cette copie est placee dans la liste d'affichage
     */
    
    public Object cloner(VueGraphe vueGraphe){
	Sommet le_clone = (Sommet)getSommet().cloner(vueGraphe.getGraphe());
	SommetCircle sommetRetour = new SommetCircle(vueGraphe,posx,posy,new String(monEtiquette),le_clone);
	sommetRetour.copyAllVariable(this);
	return sommetRetour;
    }

    // method which copy all the variable from the SommetCircle given in parameters
    public void copyAllVariable(SommetCircle s){
	this.monCote=s.getCote();
	super.copyAllVariable(s);
    }

    /**
     * Retourne une fenetre 'BoiteFormeDessin' qui indique les proprietes du
     * sommet. Appele les methodes pack() et show() pour afficher
     * la fenetre.
     *
     * Certaines proprietes peuvent eventuellement etre modifiables (laisse a
     * l'appreciation du programmeur).
     **/
    public BoiteFormeDessin proprietes(JFrame parent) {
	return new BoiteSommetCircle(parent, this);
    }
}

