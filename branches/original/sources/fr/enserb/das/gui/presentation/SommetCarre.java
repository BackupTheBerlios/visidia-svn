package fr.enserb.das.gui.presentation;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import fr.enserb.das.gui.donnees.*;
import fr.enserb.das.gui.presentation.boite.*;
import fr.enserb.das.gui.metier.*;


/** Raffine sa super-classe pour représenter un sommet sous la forme d'un carré.*/
public class SommetCarre extends SommetDessin{

  // Variables d'instance.

  protected static int monCote = 25; // représente le côté d'un carré
 
  
  //Constructeurs

  // Instancier un nouveau sommet sans étiquette
  

  public SommetCarre(VueGraphe vg, int posx, int 
		     posy, String etiquette,Sommet s){
      super(vg,posx,posy,etiquette,s);
  }

  // constructor which also creates a new Sommet corresponding to this FormeDessin
  public SommetCarre(VueGraphe vg,int posx, int 
		     posy, String etiquette){
      this(vg,posx,posy,etiquette,new Sommet(vg.getGraphe()));
  }
  
    
  
  // Méthodes
  

  // Retourne la forme du sommet
  public String forme() {
    return new String("Square");
  }

  // Agrandit la taille du sommet de "coef" fois
  public void agrandir(float coef) {
    if (coef > 0)
      monCote = (int)(coef * monCote);
  }

  // Retourne le côté du sommet carré
  public int getCote() {
    return monCote;
  }
  
  // Modifie le côté du sommet carré
  public void setCote(int cote){
    monCote = cote;
    vueGraphe.setFontSize(((float)cote/3)+1);
   }

  // Dessine le sommet sur le Graphics passé en argument
  public void dessiner(Component c, Graphics g) {
    int x = centreX();
    int y = centreY();
    int cote = getCote();
    int cote_sur_2 = cote / 2;

    // Dessin de la forme carre du sommet
    if(est_enlumine())
      g.setColor(couleur_fond);
    else
      g.setColor(couleur_trait);
    g.drawRect(posx - cote_sur_2-1, posy - cote_sur_2-1, 2 * cote_sur_2+2,
               2 * cote_sur_2+2);
    g.drawRect(posx - cote_sur_2-2, posy - cote_sur_2-2, 2 * cote_sur_2+4,
               2 * cote_sur_2+4);
    g.drawRect(posx - cote_sur_2-1, posy - cote_sur_2-1, 2 * cote_sur_2+2,
               2 * cote_sur_2+2 + 2*cote_sur_2/3);
    g.drawRect(posx - cote_sur_2-2, posy - cote_sur_2-2, 2 * cote_sur_2+4,
               2 * cote_sur_2+4 + 2*cote_sur_2/3);

   if(est_enlumine())
     g.setColor(couleur_trait);
   else
     g.setColor(couleur_fond);
   g.fillRect(posx - cote_sur_2,posy - cote_sur_2,2 * cote_sur_2,
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
   // affichage d'une icone si elle existe
   if(getImage() != null)
       (getImage()).paintIcon(c, g, x - cote / 2, y - cote / 2);
   
   g.setColor((Color)((TableCouleurs.getTableCouleurs()).get(getEtat().substring(0,1))));
   
   g.fillRect(x - cote/2, y + cote/2 + 2 , cote, cote/3);         	    
   //g.setColor(Color.blue);
   //g.drawString(etat, x - cote/2 + 20, y + cote + 8);
  }
   
  // Teste si le point donné en paramètre appartient au sommet
  public boolean appartient(int x, int y) {
    return ((Math.abs(posx - x) <= (monCote / 2)) &&
	   (Math.abs(posy - y) <= (monCote / 2)));
  }

  // Teste si le sommet est contenu en entier dans une zone rectangulaire.
  // (x1, y1) coordonnees en haut à gauche et (x2, y2) coordonnees en bas à droite.
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
   * Renvoie sous la forme d'une string les caractéristiques graphiques du sommet.
   * coordonnées,
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
	SommetCarre sommetRetour = new SommetCarre(vueGraphe,posx,posy,new String(monEtiquette),le_clone);
	sommetRetour.copyAllVariable(this);
	return sommetRetour;
    }

    // method which copy all the variable from the SommetCarre given in parameters
    public void copyAllVariable(SommetCarre s){
	this.monCote=s.getCote();
	super.copyAllVariable((SommetDessin)s);
    }

    /**
     * Retourne une fenêtre 'BoiteFormeDessin' qui indique les proprietés du
     * sommet. Appele les méthodes pack() et show() pour afficher
     * la fenêtre.
     *
     * Certaines propriétés peuvent éventuellement être modifiables (laissé à
     * l'appréciation du programmeur).
     **/
    public BoiteFormeDessin proprietes(JFrame parent) {
	return new BoiteSommetCarre(parent, this);
    }
}
