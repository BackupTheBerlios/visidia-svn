package fr.enserb.das.gui.presentation;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import fr.enserb.das.gui.donnees.*;
import fr.enserb.das.gui.presentation.boite.*;
import fr.enserb.das.gui.metier.*;


/** Raffine sa super-classe pour repr�senter un sommet sous la forme d'un carr�.*/
public class SommetRoutage extends SommetDessin{

  // Variables d'instance.

  protected static int rayon = 20; // repr�sente le c�t� d'un carr�
 
  
  //Constructeurs

  // Instancier un nouveau sommet sans �tiquette
  

  public SommetRoutage(VueGraphe vg, int posx, int 
		     posy, String etiquette,Sommet s){
      super(vg,posx,posy,etiquette,s);
      stateTable.put("id",etiquette);
      }

  // constructor which also creates a new Sommet corresponding to this FormeDessin
  public SommetRoutage(VueGraphe vg, int posx, int 
		     posy, String etiquette){
      this(vg,posx,posy,etiquette,new Sommet(vg.getGraphe()));
  }
  
    
  
  // M�thodes
  

  // Retourne la forme du sommet
  public String forme() {
    return new String("Square");
  }

  // Agrandit la taille du sommet de "coef" fois
  public void agrandir(float coef) {
    if (coef > 0)
      rayon = (int)(coef * rayon);
  }

  // Retourne le c�t� du sommet carr�
  public int getRayon() {
    return rayon;
  }
  
  // Modifie le c�t� du sommet carr�
  public void setRayon(int rayon){
    this.rayon = rayon;
  }

  // Dessine le sommet sur le Graphics pass� en argument
  public void dessiner(Component c, Graphics g) {
    int x = centreX();
    int y = centreY();
  
    // on fait l'interieur
    if(est_enlumine())
     g.setColor(couleur_trait);
   else
     g.setColor(couleur_fond);
    g.fillOval(posx-rayon,posy-rayon,2*rayon,2*rayon);


    // Dessin de la forme carre du sommet
    if(est_enlumine())
      g.setColor(couleur_fond);
    else
      g.setColor(couleur_trait);

    g.drawOval(posx-rayon,posy-rayon,2*rayon,2*rayon);
    
  
   // affichage de l'etiquette
   if((getVueGraphe()).afficherEtiquettes()){
       g.setColor(Color.blue);
       if(est_enlumine())
	   g.setFont((getVueGraphe()).fontGras());
       else
	   g.setFont((getVueGraphe()).fontNormal());
       g.drawString((getEtiquette()+ " , "+((String)getValue("arg 1")))+" ,",x - rayon , y + rayon + 12);
   }
   // affichage d'une icone si elle existe
   if(getImage() != null)
       (getImage()).paintIcon(c, g, x - rayon / 2, y - rayon / 2);
   
   g.setColor((Color)((TableCouleurs.getTableCouleurs()).get(getEtat())));  
   
   g.fillOval(x - rayon/2, y- rayon/2 + 2 , rayon, rayon);         	    
   //g.setColor(Color.blue);
   //g.drawString(etat, x -Rayon/2 + 20, y + Rayon + 8);
   
  }
   
  // Teste si le point donn� en param�tre appartient au sommet
  public boolean appartient(int x, int y) {
      return ( ( ((posx - x)*(posx - x)) + ((posy - y)*(posy - y)))  <= (rayon*rayon));
  }

  // Teste si le sommet est contenu en entier dans une zone rectangulaire.
  // (x1, y1) coordonnees en haut � gauche et (x2, y2) coordonnees en bas � droite.
  public boolean estDansRegion(int x1, int y1, int x2, int y2) {
    return ((x1 <= (posx - rayon) && (y1 <= posy - rayon)) &&
	   (x2 >= (posx + rayon ) && (y2 >= posy + rayon / 2)));
  }

	
    // returns the distance from the center to determines the positions of edges
  public float rayon(float angle){
      return rayon;
  }

   /**
   * Renvoie sous la forme d'une string les caract�ristiques graphiques du sommet.
   * coordonn�es,
   * forme,
   * Rayon,
   * couleur du trait,
   * couleur d'enlumination.
   **/
  public String graphicProperties() {
      return new String("(" + Integer.toString(centreX()) + ","
			+ Integer.toString(centreY()) + ")\t" 
			+ forme() + "\t"
			+ Integer.toString(getRayon()) + "\t" 
			+ couleurTrait().getRed() + "," + couleurTrait().getGreen() + "," + couleurTrait().getBlue() + "\t" +
			+ couleurFond().getRed() + "," + couleurFond().getGreen() + "," + couleurFond().getBlue());
  }

    /* methode qui cree et renvoie une copie du sommet 
     * cette copie est placee dans la liste d'affichage
     */
    
    public Object cloner(VueGraphe vueGraphe){
	Sommet le_clone = (Sommet)getSommet().cloner(vueGraphe.getGraphe());
	SommetRoutage sommetRetour = new SommetRoutage(vueGraphe,posx,posy,new String(monEtiquette),le_clone);
	sommetRetour.copyAllVariable(this);
	return sommetRetour;
    }  

    // method which copy all the variable from the SommetRoutage given in parameters
    public void copyAllVariable(SommetRoutage s){
	this.rayon=s.getRayon();
	super.copyAllVariable((SommetDessin)s);
    }

    /**
     * Retourne une fen�tre 'BoiteFormeDessin' qui indique les propriet�s du
     * sommet. Appele les m�thodes pack() et show() pour afficher
     * la fen�tre.
     *
     * Certaines propri�t�s peuvent �ventuellement �tre modifiables (laiss� �
     * l'appr�ciation du programmeur).
     **/
    public BoiteFormeDessin proprietes(JFrame parent) {
	return new BoiteSommetDessin(parent, this);
    }
}