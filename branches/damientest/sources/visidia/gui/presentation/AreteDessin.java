package visidia.gui.presentation;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import visidia.gui.presentation.boite.*;
import visidia.gui.metier.*;
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
	id1=Integer.valueOf(origine.getEtiquette()).intValue();
	id2=Integer.valueOf(destination.getEtiquette()).intValue();
	vueGraphe = origine.getVueGraphe();
	repositionner(origine, destination);
	graphObject = arete;
	arete.setAreteDessin(this);
	vueGraphe.insererListeAffichage(this);
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
	if (etatArete) {
	    if (enlumineBis) {
		g.setColor (COULEUR_ENLUMINER_BIS);
		dessinerLigne (g, 7.0f, origx, origy, destx, desty);		
	    }
	    g.setColor (enlumine ? couleur_fond : couleur_trait);
	    dessinerLigne (g, 5.0f, origx, origy, destx, desty);
	} else {
	    if (enlumineBis) {
		g.setColor (COULEUR_ENLUMINER_BIS);
		dessinerLigne (g, 3.0f, origx, origy, destx, desty);
	    }
	    g.setColor (enlumine ? couleur_fond : couleur_trait);
	    dessinerLigne (g, 1.0f, origx, origy, destx, desty);
	}
	//	    if(isOriented){
	if(false){
	    Shape arrowHead = 
		ArrowHeadFactory.createSegmentArrowHead(new Point(origx, origy), 
							new Point(destx, desty),
							6, 10);
	    if(g instanceof Graphics2D){
		((Graphics2D)g).fill(arrowHead);
	    }
	}
	if (etiquetteEtatArete != null) {
	    g.setColor(Color.blue);
	    if(est_enlumine())
		g.setFont((getVueGraphe()).fontGras());
	    else
		g.setFont((getVueGraphe()).fontNormal());
	    g.drawString(etiquetteEtatArete, 
			 (origx + destx) / 2 + 5, (origy + desty) / 2);
	}
    }
   
    
 	
    
    // Recalculates the position of the edge when one of its verticies is moved
    public void repositionner(SommetDessin origine, SommetDessin destination) {
	int x1 = origine.centreX(),
	    x2 = destination.centreX();
	int y1 = origine.centreY(),
	    y2 = destination.centreY();
	int dx = x2 - x1, dy = y2 - y1;
	float dist = (float)Math.sqrt((double)(dx * dx + dy * dy));
	
	if(dist > 0) {
	    float theta = (float)Math.atan2((double)dx, (double)dy);
	    float r1 = origine.rayon(theta),
		r2 = destination.rayon(theta + ((theta > 0) ? (-(float)Math.PI) : (float)Math.PI));
	    
	    if((r1 + r2) < dist) {
		float cos_theta = dx / dist, sin_theta = dy / dist;
		
		placerOrigine(x1 + (int)(r1 * cos_theta),
			      y1 + (int)(r1 * sin_theta));
		placerDestination(x2 - (int)(r2 * cos_theta),
				  y2 - (int)(r2 * sin_theta));
		return;
	    }
	}
	
	// Big tip not that pretty letting the edge not to be displayed
	// (waiting for a better solution...)
	placerOrigine(Integer.MAX_VALUE, Integer.MAX_VALUE);
	placerDestination(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    

    private void repositionnerOrigine(SommetDessin origine) {
	int x1 = origine.centreX();
	int y1 = origine.centreY();
	int x2 = destx;
	int y2 = desty;
	
	int dx = x2 - x1, dy = y2 - y1;
	float dist = (float)Math.sqrt((double)(dx * dx + dy * dy));
	
	if(dist > 0) {
	    float theta = (float)Math.atan2((double)dx, (double)dy);
	    float r1 = origine.rayon(theta);
	    
	    if(r1  < dist) {
		float cos_theta = dx / dist, sin_theta = dy / dist;
		
		placerOrigine(x1 + (int)(r1 * cos_theta),
			      y1 + (int)(r1 * sin_theta));
		return;
	    }
	}
	// Big tip not that pretty letting the edge not to be displayed
	// (waiting for a better solution...)
	placerOrigine(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    
    private void repositionnerDestination(SommetDessin destination) {
	int x1 = origx;
	int x2 = destination.centreX();
	int y1 = origy;
	int y2 = destination.centreY();
	int dx = x2 - x1, dy = y2 - y1;
	float dist = (float)Math.sqrt((double)(dx * dx + dy * dy));
	
	if(dist > 0) {
	    float theta = (float)Math.atan2((double)dx, (double)dy);
	    float r2 = destination.rayon(theta + ((theta > 0) ? (-(float)Math.PI) : (float)Math.PI));
	    
	    if(r2 < dist) {
		float cos_theta = dx / dist, sin_theta = dy / dist;
		
		placerDestination(x2 - (int)(r2 * cos_theta),
				  y2 - (int)(r2 * sin_theta));
		return;
	    }
	}

	// Big tip not that pretty letting the edge not to be displayed
	// (waiting for a better solution...)
	placerDestination(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
     
 
    // Moves the edge of (dx, dy)
    public void deplacer(int dx, int dy) {
	((Arete)graphObject).origine().getSommetDessin().deplacer(dx,dy);
	if (((Arete)graphObject).origine() != ((Arete)graphObject).destination())
	    ((Arete)graphObject).destination().getSommetDessin().deplacer(dx,dy);
    }
   
    // Tests whether the point parameter belongs to the edge 
    public boolean appartient(int x, int y){
	double scalaire =
	    (x - origx) * (destx - origx) + (y - origy) * (desty - origy);
	double long_carre =
	    Math.pow(destx - origx, 2) + Math.pow(desty - origy, 2);

	if(scalaire > 0) {
	    double distance_carre =
		((Math.pow(x - origx , 2) + Math.pow(y - origy, 2)) *
		 long_carre - Math.pow(scalaire, 2)) / long_carre;
	    return ((Math.pow(scalaire, 2) < Math.pow(long_carre, 2)) &&
		    (distance_carre < Math.pow(precision, 2)));
	} else
	    return false;
    }

    // Tests whether the edge is entirely inside a rectangular zone
    // (x1, y1) are the top left side coordinates and (x2, y2) are the bottom right side coordinates
    public boolean estDansRegion(int x1, int y1, int x2, int y2) {
	return ((x1 <= origx) && (y1 <= origy) && (x2 >= destx) &&
		(y2 >= desty) && (x1 <= destx) && (y1 <= desty) &&
		(x2 >= origx) && (y2 >= origy));
    }

    

    // Accessors
    public String forme() {
	return new String("edge");
    }

    public String type() {
	return new String("edge");
    }
   
    public boolean getEtat(){
	return etatArete;
    }

    public Arete getArete(){
	return ((Arete)graphObject);
    }

    public String getEtatStr() {
	return etiquetteEtatArete;
    }

    public int origineX(){
	return origx;
    }

    public int origineY(){
	return origy;
    }
    
    public int destinationX(){
	return destx;
    }

    public int destinationY(){
	return desty;
    }

    // stop

    public void setEtat(boolean etat){
	etatArete = etat;
    }

    public void setEtat(String strEtat){
	etiquetteEtatArete = strEtat;
    }

    public void setOriented(boolean b){
	isOriented = b;
    }

    public boolean getOriented(){
	return isOriented;
    }

    public void changerOrigine(SommetDessin nouvelleOrigine) {
	((Arete)graphObject).changerOrigine(nouvelleOrigine.getSommet());
	repositionnerOrigine(nouvelleOrigine);
    }
    
    public void changerDestination(SommetDessin nouvelleDestination) {
	((Arete)graphObject).changerDestination(nouvelleDestination.getSommet());
	repositionnerDestination(nouvelleDestination);
    }
    
    // these methods permit to move origin or destination
    public void placerOrigine(int x, int y){
	origx = x;
	origy = y;
    }
   
    public void placerDestination(int x, int y){
	destx = x;
	desty = y;
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
	return new String(forme() + "\t" 
			  + couleurTrait().getRed() + "," 
			  + couleurTrait().getGreen() + "," 
			  + couleurTrait().getBlue() + "\t"
			  + couleurFond().getRed() + "," 
			  + couleurFond().getGreen() + "," 
			  + couleurFond().getBlue());
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
	super.copyAllVariable((FormeDessin)s);
    }

    public int getId1() {
	return id1;
    }
    
    public int getId2() {
	return id2;
    }
}


