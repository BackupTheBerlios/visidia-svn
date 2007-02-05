package visidia.gui.presentation;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import visidia.gui.donnees.*;
import visidia.gui.presentation.boite.*;
import visidia.gui.metier.*;


/** Raffine sa super-classe pour representer un sommet sous la forme d'un carre.*/
public class Sommet2Cercle extends SommetDessin{

    // Variables d'instance.

    protected static int monCote = 20; // represente le cote d'un carre
 
  
    //Constructeurs

    // Instancier un nouveau sommet sans etiquette
  

    public Sommet2Cercle(VueGraphe vg, int posx, int 
			 posy, String etiquette,Sommet s){
	super(vg,posx,posy,etiquette,s);
	stateTable.put("id",etiquette);
    }

    // constructor which also creates a new Sommet corresponding to this FormeDessin
    public Sommet2Cercle(VueGraphe vg, int posx, int 
			 posy, String etiquette){
	this(vg,posx,posy,etiquette,new Sommet(vg.getGraphe()));
    }
  
    
  
    // Methodes
  

    // Retourne la forme du sommet
    public String forme() {
	return new String("Two Circles");
    }

    // Agrandit la taille du sommet de "coef" fois
    public void agrandir(float coef) {
	if (coef > 0)
	    monCote = (int)(coef * monCote);
    }

    // Retourne le cote du sommet routage
    public int getCote() {
	return monCote;
    }
    
    // Modifie le cote du sommet routage
    public void setCote(int cote){
	monCote  = cote;
	vueGraphe.setFontSize(((float)monCote/3)+1);
    }
        
    public void dessiner(Component c, Graphics g, String tag) {
	this.dessiner(c,g);
	g.setColor(Color.black);
	if(tag.equals("agents")) {
	    // draw on vertices the number of agents
	    String mesg = getNbr();
	    // System.out.println(mesg);
	    int stringSize = (int)(g.getFontMetrics().
				   getStringBounds(mesg,g).
				   getWidth());
	    g.drawString(mesg,centreX()-stringSize/2 , centreY()+getCote()/4 );
	}	    
    }

    // Dessine le sommet sur le Graphics passe en argument
    public void dessiner(Component c, Graphics g) {
	int x = centreX();
	int y = centreY();
 	int _rayon = getCote();


 
	// on fait l'interieur
	if(est_enlumine())
	    g.setColor(couleur_trait);
	else
	    g.setColor(couleur_fond);

	// La bordure est rouge si enluminerBis
	if (est_enlumineBis())
	    g.setColor(Color.red);


	//g.fillOval(posx-rayon,posy-rayon,2*rayon,2*rayon);
	g.fillOval(posx- _rayon/2-4,posy- _rayon/2-2,_rayon+8,_rayon+8);

	// Dessin de la forme carre du sommet
	if(est_enlumine())
	    g.setColor(couleur_fond);
	else
	    g.setColor(couleur_trait);
	
	// La bordure est rouge si enluminerBis
	if (est_enlumineBis())
	    g.setColor(Color.red);
	
	//g.drawOval(posx-rayon,posy-rayon,2*rayon,2*rayon);
	g.drawOval(posx-_rayon/2-4,posy- _rayon/2 -2,_rayon+8,_rayon+8);

	// affichage de l'etiquette
	if((getVueGraphe()).afficherEtiquettes()){
	    g.setColor(Color.blue);
	    if(est_enlumine())
		g.setFont((getVueGraphe()).fontGras());
	    else
		g.setFont((getVueGraphe()).fontNormal());
	    // dans ancienne version y avait ca :
	    //g.drawString((getEtiquette()+ " , "+((String)getValue("arg 1")))+" ,",x - rayon , y + rayon + 12);
	    g.drawString((getEtiquette())+ " , "+getEtat(),x - _rayon /2 , y + _rayon + 8);

	}
	// affichage d'une icone si elle existe
	if(getImage() != null)
	    (getImage()).paintIcon(c, g, x - _rayon / 2, y - _rayon / 2);
   
	g.setColor((Color)((TableCouleurs.getTableCouleurs()).get(getEtat())));  
   
	g.fillOval(x - _rayon/2, y- _rayon/2 + 2 , _rayon, _rayon);         	    
	//g.setColor(Color.blue);
	//g.drawString(etat, x -Rayon/2 + 20, y + Rayon + 8);
   
    }
   
    // Teste si le point donne en parametre appartient au sommet
    public boolean appartient(int x, int y) {
	return ( ( ((posx - x)*(posx - x)) + ((posy - y)*(posy - y)))  <= (monCote*monCote));
    }

    // Teste si le sommet est contenu en entier dans une zone rectangulaire.
    // (x1, y1) coordonnees en haut a gauche et (x2, y2) coordonnees en bas a droite.
    public boolean estDansRegion(int x1, int y1, int x2, int y2) {
	return ((x1 <= (posx - monCote) && (y1 <= posy - monCote)) &&
		(x2 >= (posx + monCote ) && (y2 >= posy + monCote / 2)));
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
     * Rayon,
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
	Sommet2Cercle sommetRetour = new Sommet2Cercle(vueGraphe,posx,posy,new String(monEtiquette),le_clone);
	sommetRetour.copyAllVariable(this);
	return sommetRetour;
    }  

    // method which copy all the variable from the Sommet2Cercle given in parameters
    public void copyAllVariable(Sommet2Cercle s){
	this.monCote=s.getCote();
	super.copyAllVariable((SommetDessin)s);
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
	return new BoiteSommet2Cercle(parent, this);
    }
}
