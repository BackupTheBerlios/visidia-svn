package visidia.gui.presentation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;

import javax.swing.JFrame;

import visidia.gui.metier.Arete;
import visidia.gui.presentation.boite.BoiteAreteDessin;
import visidia.gui.presentation.boite.BoiteFormeDessin;
import visidia.tools.ArrowHeadFactory;

/** This class draws an edge  */
public abstract class AreteDessin extends FormeDessin{
    
    // Static variable for the click precision
    static protected double precision = 3;
    static protected Color COULEUR_ENLUMINER_BIS = Color.red;

    // Identities of edge's vertex
    protected int id1,id2;

    // The origin vertex coordinates
    protected int origx;
    protected int origy;
    // The destination vertex coordinates
    protected int destx;
    protected int desty;
  
    protected boolean etatArete = false;
    protected String etiquetteEtatArete = null;
    protected boolean isOriented = false;

    

    // Constructors
    
    // Create a new edge knowing its origin and destination verticies
    public AreteDessin(SommetDessin origine, SommetDessin destination, Arete arete){
	this.id1=Integer.valueOf(origine.getEtiquette()).intValue();
	this.id2=Integer.valueOf(destination.getEtiquette()).intValue();
	this.vueGraphe = origine.getVueGraphe();
	this.repositionner(origine, destination);
	this.graphObject = arete;
	arete.setAreteDessin(this);
	this.vueGraphe.insererListeAffichage(this);
    }

    // if the edge is not specified, we create a new one
    public AreteDessin(SommetDessin origine, SommetDessin destination){
	this(origine,destination,new Arete(origine.getVueGraphe().getGraphe(),origine.getSommet(),destination.getSommet()));
    }
    
    //PFA2003
    protected void dessinerLigne (Graphics g, float largeur,
				  int x1, int y1, int x2, int y2) {
	Stroke tmp = ((Graphics2D) g).getStroke ();
	
	//if (isConnected) {
	    ((Graphics2D) g).setStroke (new BasicStroke (largeur));
	    /*} else {
	    float[] dash = {6.0f, 4.0f, 2.0f, 4.0f, 2.0f, 4.0f};
	    BasicStroke bs = new BasicStroke(largeur, BasicStroke.CAP_BUTT, 
					     BasicStroke.JOIN_MITER, 10.0f, 
					     dash, 0.0f);
	    ((Graphics2D) g).setStroke(bs);
	}
	    */
	g.drawLine(x1, y1, x2, y2);
	
	((Graphics2D) g).setStroke (tmp);
    }
    

    // Draws an edge on the Graphics parameter
    //PFA2003
    public void dessiner(Component c , Graphics g) {
	if (this.etatArete) {
	    if (this.enlumineBis) {
		g.setColor (COULEUR_ENLUMINER_BIS);
		this.dessinerLigne (g, 7.0f, this.origx, this.origy, this.destx, this.desty);		
	    }
	    g.setColor (this.enlumine ? this.couleur_fond : this.couleur_trait);
	    this.dessinerLigne (g, 5.0f, this.origx, this.origy, this.destx, this.desty);
	} else {
	    if (this.enlumineBis) {
		g.setColor (COULEUR_ENLUMINER_BIS);
		this.dessinerLigne (g, 3.0f, this.origx, this.origy, this.destx, this.desty);
	    }
	    g.setColor (this.enlumine ? this.couleur_fond : this.couleur_trait);
	    this.dessinerLigne (g, 1.0f, this.origx, this.origy, this.destx, this.desty);
	}
	
	// if(isOriented){ à revoir
	if(false){
	    Shape arrowHead = 
		ArrowHeadFactory.createSegmentArrowHead(new Point(this.origx, this.origy), 
							new Point(this.destx, this.desty),
							6, 10);
	    if(g instanceof Graphics2D){
		((Graphics2D)g).fill(arrowHead);
	    }
	}

	
	if (this.etiquetteEtatArete != null) {
	    g.setColor(Color.blue);
	    if(this.est_enlumine())
		g.setFont((this.getVueGraphe()).fontGras());
	    else
		g.setFont((this.getVueGraphe()).fontNormal());
	    g.drawString(this.etiquetteEtatArete, 
			 (this.origx + this.destx) / 2 + 5, (this.origy + this.desty) / 2);
	}
    }
   
    
 	
    
    // Recalculates the position of the edge when one of its verticies is moved
    public void repositionner(SommetDessin origine, SommetDessin destination) {
	int x1 = origine.centreX(),
	    x2 = destination.centreX();
	int y1 = origine.centreY(),
	    y2 = destination.centreY();
	int dx = x2 - x1, dy = y2 - y1;
	float dist = (float)Math.sqrt((dx * dx + dy * dy));
	
	if(dist > 0) {
	    float theta = (float)Math.atan2(dx, dy);
	    float r1 = origine.rayon(theta),
		r2 = destination.rayon(theta + ((theta > 0) ? (-(float)Math.PI) : (float)Math.PI));
	    
	    if((r1 + r2) < dist) {
		float cos_theta = dx / dist, sin_theta = dy / dist;
		
		this.placerOrigine(x1 + (int)(r1 * cos_theta),
			      y1 + (int)(r1 * sin_theta));
		this.placerDestination(x2 - (int)(r2 * cos_theta),
				  y2 - (int)(r2 * sin_theta));
		return;
	    }
	}
	
	// Big tip not that pretty letting the edge not to be displayed
	// (waiting for a better solution...)
	this.placerOrigine(Integer.MAX_VALUE, Integer.MAX_VALUE);
	this.placerDestination(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    

    private void repositionnerOrigine(SommetDessin origine) {
	int x1 = origine.centreX();
	int y1 = origine.centreY();
	int x2 = this.destx;
	int y2 = this.desty;
	
	int dx = x2 - x1, dy = y2 - y1;
	float dist = (float)Math.sqrt((dx * dx + dy * dy));
	
	if(dist > 0) {
	    float theta = (float)Math.atan2(dx, dy);
	    float r1 = origine.rayon(theta);
	    
	    if(r1  < dist) {
		float cos_theta = dx / dist, sin_theta = dy / dist;
		
		this.placerOrigine(x1 + (int)(r1 * cos_theta),
			      y1 + (int)(r1 * sin_theta));
		return;
	    }
	}
	// Big tip not that pretty letting the edge not to be displayed
	// (waiting for a better solution...)
	this.placerOrigine(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    
    private void repositionnerDestination(SommetDessin destination) {
	int x1 = this.origx;
	int x2 = destination.centreX();
	int y1 = this.origy;
	int y2 = destination.centreY();
	int dx = x2 - x1, dy = y2 - y1;
	float dist = (float)Math.sqrt((dx * dx + dy * dy));
	
	if(dist > 0) {
	    float theta = (float)Math.atan2(dx, dy);
	    float r2 = destination.rayon(theta + ((theta > 0) ? (-(float)Math.PI) : (float)Math.PI));
	    
	    if(r2 < dist) {
		float cos_theta = dx / dist, sin_theta = dy / dist;
		
		this.placerDestination(x2 - (int)(r2 * cos_theta),
				  y2 - (int)(r2 * sin_theta));
		return;
	    }
	}

	// Big tip not that pretty letting the edge not to be displayed
	// (waiting for a better solution...)
	this.placerDestination(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
     
 
    // Moves the edge of (dx, dy)
    public void deplacer(int dx, int dy) {
	((Arete)this.graphObject).origine().getSommetDessin().deplacer(dx,dy);
	if (((Arete)this.graphObject).origine() != ((Arete)this.graphObject).destination())
	    ((Arete)this.graphObject).destination().getSommetDessin().deplacer(dx,dy);
    }
   
    // Tests whether the point parameter belongs to the edge 
    public boolean appartient(int x, int y){
	double scalaire =
	    (x - this.origx) * (this.destx - this.origx) + (y - this.origy) * (this.desty - this.origy);
	double long_carre =
	    Math.pow(this.destx - this.origx, 2) + Math.pow(this.desty - this.origy, 2);

	if(scalaire > 0) {
	    double distance_carre =
		((Math.pow(x - this.origx , 2) + Math.pow(y - this.origy, 2)) *
		 long_carre - Math.pow(scalaire, 2)) / long_carre;
	    return ((Math.pow(scalaire, 2) < Math.pow(long_carre, 2)) &&
		    (distance_carre < Math.pow(precision, 2)));
	} else
	    return false;
    }

    // Tests whether the edge is entirely inside a rectangular zone
    // (x1, y1) are the top left side coordinates and (x2, y2) are the bottom right side coordinates
    public boolean estDansRegion(int x1, int y1, int x2, int y2) {
	return ((x1 <= this.origx) && (y1 <= this.origy) && (x2 >= this.destx) &&
		(y2 >= this.desty) && (x1 <= this.destx) && (y1 <= this.desty) &&
		(x2 >= this.origx) && (y2 >= this.origy));
    }

    

    // Accessors
    public String forme() {
	return new String("edge");
    }

    public String type() {
	return new String("edge");
    }
   
    public boolean getEtat(){
	return this.etatArete;
    }

    public Arete getArete(){
	return ((Arete)this.graphObject);
    }

    public String getEtatStr() {
	return this.etiquetteEtatArete;
    }

    public int origineX(){
	return this.origx;
    }

    public int origineY(){
	return this.origy;
    }
    
    public int destinationX(){
	return this.destx;
    }

    public int destinationY(){
	return this.desty;
    }

    // stop

    public void setEtat(boolean etat){
	this.etatArete = etat;
    }

    public void setEtat(String strEtat){
	this.etiquetteEtatArete = strEtat;
    }

    public void setOriented(boolean b){
	this.isOriented = b;
    }

    public boolean getOriented(){
	return this.isOriented;
    }

    public void changerOrigine(SommetDessin nouvelleOrigine) {
	((Arete)this.graphObject).changerOrigine(nouvelleOrigine.getSommet());
	this.repositionnerOrigine(nouvelleOrigine);
    }
    
    public void changerDestination(SommetDessin nouvelleDestination) {
	((Arete)this.graphObject).changerDestination(nouvelleDestination.getSommet());
	this.repositionnerDestination(nouvelleDestination);
    }
    
    // these methods permit to move origin or destination
    public void placerOrigine(int x, int y){
	this.origx = x;
	this.origy = y;
    }
   
    public void placerDestination(int x, int y){
	this.destx = x;
	this.desty = y;
    }

     
    // returns the descritpion of the edge as a string
    public String abstractDescription() {
	return new String("");
    } 

    
   

    /**
     * returns the graphic properties of the edge
     * shape
     * foreground color
     * enluminated color
     **/
    public String graphicProperties() {
	return new String(this.forme() + "\t" 
			  + this.couleurTrait().getRed() + "," 
			  + this.couleurTrait().getGreen() + "," 
			  + this.couleurTrait().getBlue() + "\t"
			  + this.couleurFond().getRed() + "," 
			  + this.couleurFond().getGreen() + "," 
			  + this.couleurFond().getBlue());
    }

    /**
   * Returns a window 'BoiteFormeDessin' wich shows theproperties of 
   * the edge. Call the pach() and show() methods to display
   *
   * some of the properties could be changeable
   **/
    public BoiteFormeDessin proprietes(JFrame parent) {
	return new BoiteAreteDessin(parent, this);
    }
    

    // Definition of the method 'cloner' which must be implemented in the under-classes
    public abstract Object cloner(SommetDessin origine, SommetDessin destination);

    // method which copy all the variable from the AreteDessin given in parameters
    public void copyAllVariable(AreteDessin s){
	this.etatArete=s.getEtat();
	super.copyAllVariable(s);
    }

    public int getId1() {
	return this.id1;
    }
    
    public int getId2() {
	return this.id2;
    }
}


