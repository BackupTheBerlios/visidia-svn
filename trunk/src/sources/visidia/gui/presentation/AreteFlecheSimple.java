package visidia.gui.presentation;

import java.awt.*;
import visidia.gui.metier.*;

/**
 * Représente le dessin d'une arête représentée par une flèche simple.
 **/
public class AreteFlecheSimple extends AreteDessin{
    
    // Variables de classe.
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 6687178169548010643L;
	static protected double angle_branches = 3e-1;
    static protected double longueur_branches = 20;

    // Variables d'instance.
    protected int branche1_x, branche1_y, branche2_x, branche2_y;
    //protected int mi_bran1_x, mi_bran1_y, mi_bran2_x, mi_bran2_y;
    protected boolean recalculer_branche = true;
    
    //Constructeurs.

    public AreteFlecheSimple(SommetDessin origine, SommetDessin destination, Arete arete) {
	super(origine, destination, arete);
    }
    
    public AreteFlecheSimple(SommetDessin origine, SommetDessin destination) {
	super(origine, destination);
    }
    
    
    //  Dessiner une fleche avec sa pointe sur un Graphics passe en argument.
    public void dessiner(Component c , Graphics g) {
	super.dessiner(c, g);
	if(this.recalculer_branche)
	    this.recalculer_branche();
	double theta =
	    Math.atan2((this.destx - this.origx), (this.desty - this.origy));

	double angle1 = theta - angle_branches, angle2 = theta + angle_branches;
	//	g.drawLine(destx, desty, branche1_x, branche1_y);
	g.drawLine((this.origx+this.destx)/2, (this.origy+this.desty)/2, ((this.origx+this.destx)/2)- ((int)Math.round(longueur_branches * Math.sin(angle1)))/2, ((this.origy+this.desty)/2)- ((int)Math.round(longueur_branches * Math.cos(angle1)))/2 );
	g.drawLine((this.origx+this.destx)/2, (this.origy+this.desty)/2, ((this.origx+this.destx)/2)- ((int)Math.round(longueur_branches * Math.sin(angle2)))/2, ((this.origy+this.desty)/2)-  ((int)Math.round(longueur_branches * Math.cos(angle2)))/2 );
	//g.drawLine(destx, desty, branche2_x, branche2_y);
    }

    public void deplacer(int dx, int dy) {
	super.deplacer(dx, dy);
	this.branche1_x += dx;
	this.branche1_y += dy;
	this.branche2_x += dx;
	this.branche2_y += dy;
	//mi_bran1_x += (dx/2);
	//mi_bran1_y += (dy/2);
	//mi_bran2_x += (dx/2);
	//mi_bran2_y += (dy/2);
    }
  

    public String forme() {
	return new String("FlecheSimple");
    }

    public void placerOrigine(int origine_x, int origine_y) {
	super.placerOrigine(origine_x, origine_y);
	this.recalculer_branche = true;
    }
    
    public void placerDestination(int destination_x, int destination_y) {
	super.placerDestination(destination_x, destination_y);
	this.recalculer_branche = true;
    }
    
    
    protected void recalculer_branche() {
	double theta =
	    Math.atan2((this.destx - this.origx), (this.desty - this.origy));
	double angle1 = theta - angle_branches, angle2 = theta + angle_branches;
	
	this.branche1_x = this.destx -
	    (int)Math.round(longueur_branches * Math.sin(angle1));
	this.branche1_y = this.desty -
	    (int)Math.round(longueur_branches * Math.cos(angle1));
	this.branche2_x = this.destx -
	    (int)Math.round(longueur_branches * Math.sin(angle2));
	this.branche2_y = this.desty -
	    (int)Math.round(longueur_branches * Math.cos(angle2));
	//mi_bran1_x = branche1_x-((branche1_x+origx)/2);
	//mi_bran1_y = branche1_y-((branche1_y+origy)/2);
	//mi_bran2_x = branche2_x-((branche2_x+origx)/2);
	//mi_bran2_y = branche2_y-((branche2_y+origy)/2);


	this.recalculer_branche = false;
  }
    
    // Duplique l'arete courante a partir des sommets origine et destination 
    // passes en parametres

    public Object cloner(SommetDessin origine, SommetDessin destination) {
	Arete a = (Arete)this.getArete().cloner(origine.getSommet(),destination.getSommet());
	AreteFlecheSimple le_clone = new AreteFlecheSimple(origine, destination, a);
	le_clone.copyAllVariable(this);
	return le_clone;
    }

    // method which copy all the variable from the AreteFlecheSimple
    // given in parameters
    public void copyAllVariable(AreteFlecheSimple a){
	super.copyAllVariable(a);}

}



