package visidia.gui.presentation;

import javax.swing.*;
import java.awt.*;
import visidia.gui.donnees.*;
import visidia.gui.presentation.boite.*;
import visidia.gui.metier.*;


/** Raffine sa super-classe pour representer un sommet sous la forme d'un carre.*/
public class SommetCarre extends SommetDessin{
    
    // Variables d'instance.
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -3875616012589868149L;
	protected static int monCote = 20; // represente le cote d'un carre
 
  
  //Constructeurs

  // Instancier un nouveau sommet sans etiquette
  

  public SommetCarre(VueGraphe vg, int posx, int 
		     posy, String etiquette,Sommet s){
      super(vg,posx,posy,etiquette,s);
  }

  // constructor which also creates a new Sommet corresponding to this FormeDessin
  public SommetCarre(VueGraphe vg,int posx, int 
		     posy, String etiquette){
      this(vg,posx,posy,etiquette,new Sommet(vg.getGraphe()));
  }
  
    
  
  // Methodes
  

  // Retourne la forme du sommet
  public String forme() {
    return new String("Square");
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
    this.vueGraphe.setFontSize(((float)cote/3)+1);
   }

    public void dessiner(Component c, Graphics g, String tag) {
	this.dessiner(c,g);
	g.setColor(Color.black);
	if(tag.equals("agents")) {
	    // draw on vertices the number of agents
	    String mesg = this.getNbr();
	    int stringSize = (int)(g.getFontMetrics().
				   getStringBounds(mesg,g).
				   getWidth());
	    g.drawString(mesg,this.centreX()-stringSize/2 , this.centreY() );
	}	    
    }
    
    // Dessine le sommet sur le Graphics passe en argument
    public void dessiner(Component c, Graphics g) {
	int x = this.centreX();
	int y = this.centreY();
	int cote = this.getCote();
	int cote_sur_2 = cote / 2;
	
	// Dessin de la forme carre du sommet
	if(this.est_enlumine())
	    g.setColor(this.couleur_fond);
	else
	    g.setColor(this.couleur_trait);
	
	// La bordure est rouge si enluminerBis
	if (this.est_enlumineBis())
	    g.setColor(Color.red);
	
	g.drawRect(this.posx - cote_sur_2-1, this.posy - cote_sur_2-1, 2 * cote_sur_2+2,
		   2 * cote_sur_2+2);
	g.drawRect(this.posx - cote_sur_2-2, this.posy - cote_sur_2-2, 2 * cote_sur_2+4,
		   2 * cote_sur_2+4);
	g.drawRect(this.posx - cote_sur_2-1, this.posy - cote_sur_2-1, 2 * cote_sur_2+2,
		   2 * cote_sur_2+2 + 2*cote_sur_2/3);
	g.drawRect(this.posx - cote_sur_2-2, this.posy - cote_sur_2-2, 2 * cote_sur_2+4,
		   2 * cote_sur_2+4 + 2*cote_sur_2/3);
	
	
	if(this.est_enlumine())
	    g.setColor(this.couleur_trait);
	else
	    g.setColor(this.couleur_fond);
	
	g.fillRect(this.posx - cote_sur_2,this.posy - cote_sur_2,2 * cote_sur_2,
		   2 * cote_sur_2);
	
	// affichage de l'etiquette
	if((this.getVueGraphe()).afficherEtiquettes()){
	    g.setColor(Color.blue);
	    if(this.est_enlumine())
		g.setFont((this.getVueGraphe()).fontGras());
	    else
		g.setFont((this.getVueGraphe()).fontNormal());
	    g.drawString((this.getEtiquette())+" , "+this.getEtat(),x - cote /2 , y + cote + 8);
	}
	
	// affichage d'une icone si elle existe
	if(this.getImage() != null)
	    (this.getImage()).paintIcon(c, g, x - cote / 2, y - cote / 2);
	
	g.setColor((Color)((TableCouleurs.getTableCouleurs()).get(this.getEtat().substring(0,1))));
	
	g.fillRect(x - cote/2, y + cote/2 + 2 , cote, cote/3);         	    
	//g.setColor(Color.blue);
	//g.drawString(etat, x - cote/2 + 20, y + cote + 8);
    }
    
    // Teste si le point donne en parametre appartient au sommet
    public boolean appartient(int x, int y) {
	return ((Math.abs(this.posx - x) <= (monCote / 2)) &&
		(Math.abs(this.posy - y) <= (monCote / 2)));
    }
    
  // Teste si le sommet est contenu en entier dans une zone rectangulaire.
  // (x1, y1) coordonnees en haut a gauche et (x2, y2) coordonnees en bas a droite.
  public boolean estDansRegion(int x1, int y1, int x2, int y2) {
    return ((x1 <= (this.posx - (monCote / 2))) && (y1 <= (this.posy - (monCote / 2))) &&
	   (x2 >= (this.posx + (monCote / 2))) && (y2 >= (this.posy + (monCote / 2))));
  }

	
    // returns the distance from the center to determines the positions of edges
  public float rayon(float angle){
      float tempo = Math.abs(angle);

      tempo = Math.min(tempo, (float)Math.PI - tempo);
      return monCote / (float)(2*Math.cos(Math.min(tempo, (float)Math.PI / 2 - tempo)));
  }

   /**
   * Renvoie sous la forme d'une string les caracteristiques
   * graphiques du sommet.  coordonnees, forme, cote, couleur du
   * trait, couleur d'enlumination.
   **/
  public String graphicProperties() {
      return new String("(" + Integer.toString(this.centreX()) + ","
			+ Integer.toString(this.centreY()) + ")\t" 
			+ this.forme() + "\t"
			+ Integer.toString(this.getCote()) + "\t" 
			+ this.couleurTrait().getRed() + "," + this.couleurTrait().getGreen() + "," + this.couleurTrait().getBlue() + "\t" +
			+ this.couleurFond().getRed() + "," + this.couleurFond().getGreen() + "," + this.couleurFond().getBlue());
  }

    /* methode qui cree et renvoie une copie du sommet 
     * cette copie est placee dans la liste d'affichage
     */
    
    public Object cloner(VueGraphe vueGraphe){
	Sommet le_clone = (Sommet)this.getSommet().cloner(vueGraphe.getGraphe());
	SommetCarre sommetRetour = new SommetCarre(vueGraphe,this.posx,this.posy,new String(this.monEtiquette),le_clone);
	sommetRetour.copyAllVariable(this);
	return sommetRetour;
    }

    // method which copy all the variable from the SommetCarre given in parameters
    public void copyAllVariable(SommetCarre s){
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
	return new BoiteSommetCarre(parent, this);
    }

}
