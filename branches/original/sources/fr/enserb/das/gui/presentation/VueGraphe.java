package fr.enserb.das.gui.presentation;

import java.util.*;
import java.awt.*;
import java.io.*;
import fr.enserb.das.gui.donnees.*;
import fr.enserb.das.gui.metier.*;
import fr.enserb.das.gui.presentation.userInterfaceEdition.undo.*;
import fr.enserb.das.gui.donnees.conteneurs.*;
import fr.enserb.das.gui.presentation.factory.*; 

/** Cette classe impl�mente la partie graphique d'un graphe. **/
public class VueGraphe implements Serializable{

  // Variable d'instance.
  protected boolean afficherEtiquettes;
  protected int numero_sommet;
  protected Font fontNormal;
  protected Font fontNormalEtiquette;
  protected Font fontGras;
  protected Font fontGrasEtiquette;
  protected Dimension size; 

  protected Ensemble liste_affichage = new Ensemble(); // liste de SommetDessin et AreteDessin

   // this class knows what sort of creation to do
    protected static FabriqueSommet fabriqueSommet = new FabriqueSommetCarre(); 
    protected static FabriqueArete fabriqueArete = new FabriqueAreteSegment();
    protected static FabriqueAreteFleche fabriqueAreteFleche = new FabriqueAreteFleche();

  
  // acces au graphe associe
    protected Graphe graphe;


  // Constructeur.
  

  /**
   * Instancier un nouveau VueGraphe
   **/

  public VueGraphe(Graphe gr) {
      graphe = gr ;

      setFontSize(9);
      numero_sommet = 0;
   
      afficherEtiquettes = true;
      size = new Dimension(0,0);
    
      // on remplit la table des couleurs
      TableCouleurs.setTableCouleurs();

  }


    /** Clones the current graph */
    public VueGraphe cloner(){
	Graphe graphe = new Graphe();
	VueGraphe vue = graphe.getVueGraphe();
	Enumeration e = liste_affichage.elements();
	Hashtable sommets_clones = new Hashtable();
	while (e.hasMoreElements()){
	    FormeDessin forme = (FormeDessin) e.nextElement();

	    if (forme.type().equals("vertex")){
		SommetDessin sommet_courant, sommet_clone;
		sommet_courant = (SommetDessin)forme;
		if(!sommets_clones.containsKey(sommet_courant)){
		  sommets_clones.put(sommet_courant,
				     sommet_clone = (SommetDessin)sommet_courant.cloner(vue));
		}
	    }


	    if (forme.type().equals("edge")){
		SommetDessin origine_clone, destination_clone;
		AreteDessin arete_courante = (AreteDessin)forme;
		SommetDessin origine = arete_courante.getArete().origine().getSommetDessin();
		SommetDessin destination = arete_courante.getArete().destination().getSommetDessin();

		if(sommets_clones.containsKey(origine))
		    origine_clone = (SommetDessin)sommets_clones.get(origine);
		else {
		    sommets_clones.put(origine,
				       origine_clone = (SommetDessin)origine.cloner(vue));
		}
		if(sommets_clones.containsKey(destination))
		    destination_clone = (SommetDessin)sommets_clones.get(destination);
		else {
		    sommets_clones.put(destination,
				       destination_clone = (SommetDessin)destination.cloner(vue));
		}
		arete_courante.cloner(origine_clone,destination_clone);
	    }
	}
	return vue;
    }
	
    

    

 /**
   * Positionne tous les objets du grapheVMA dans les couleurs standards.
   **/
  public void couleursStandards() {
    Enumeration e = listeAffichage();
    while(e.hasMoreElements()){
	FormeDessin f = (FormeDessin)e.nextElement();
	if (f.type().equals("vertex"))
	    ((SommetDessin)f).changerCouleurFond(Color.white);
	
	if (f.type().equals("edge"))
	    ((AreteDessin)e.nextElement()).changerCouleurTrait(Color.black);
    }
  }

    /**
   * Retourne une �numeration qui contient les sommets dessines du graphe.
   **/
    public Enumeration listeAffichage() {
	return liste_affichage.elements();
    }

    public void insererListeAffichage(FormeDessin f){
	liste_affichage.inserer(f);}

    public void supprimerListeAffichage(FormeDessin f){
	liste_affichage.supprimer(f);}
    
 
 /**
   * Accesseur � afficherEtiquettes.
   **/
  public boolean afficherEtiquettes() {
    return afficherEtiquettes;
  }

  /**
   * Positionne si oui ou non on affiche les �tiquettes.
   **/
  public void afficherEtiquettes(boolean booleen) {
    afficherEtiquettes = booleen;
  }



  // Methodes de Graphe.

  /**
   * Cr�er un nouveau sommet dans le graphe avec une position donn�e.
   **/
    public SommetDessin creerSommet(int posx, int posy) {
	return fabriqueSommet.creerSommet(this,posx, posy,Integer.toString(numero_sommet++));
    }
  
   /**
    * Cr�er une nouvelle ar�te dans le graphe en pr�cisant ses sommets d'origine
    * et de destination
    **/
    public AreteDessin creerArete(SommetDessin origine, SommetDessin destination) {
	return fabriqueArete.creerArete(origine,destination);
    }
  
    public AreteDessin creerAreteFleche(SommetDessin origine, SommetDessin destination) {
	return fabriqueAreteFleche.creerArete(origine,destination);
    }
    // pour les aretes et les sommets, il est possible de changer le type d'affichage
    
    // pour les aretes : pour les differents types d'affichage.
     public void changerFormeArete(FabriqueArete fabrique, UndoInfo undoInfo){
     
	 fabriqueArete = fabrique;
	 undoInfo.newGroup("Replace edges styles", "Remake edge styles");
	 /*
	   Enumeration e = listeAffichage();
	   FormeDessin f;
	   AreteDessin nouvelle_arete;
	   Stack objets_a_etudier = new Stack();
	   while(e.hasMoreElements())
	   objets_a_etudier.push(e.nextElement());
	 
	   undoInfo.newGroup("Replace edges styles", "Remake edge styles");
	   while(!objets_a_etudier.empty()){
	   f =((FormeDessin)objets_a_etudier.pop());
	   
	   if (f.type().equals("edge")){
	   liste_affichage.supprimer((AreteDessin)f);
	   
	   nouvelle_arete = fabriqueAreteFleche.creerArete(((AreteDessin)f).getArete().origine().getSommetDessin(),((AreteDessin)f).getArete().destination().getSommetDessin(),((AreteDessin)f).getArete());
	   nouvelle_arete.copyAllVariable((AreteDessin)f);
	   undoInfo.addInfo(new ChangeFormeDessin(f,nouvelle_arete));
	   }
	   }
*/
     }
		

// pour les sommets : pour les differents types d'affichage
    
    public void changerFormeSommet(FabriqueSommet fabrique, UndoInfo undoInfo){
	fabriqueSommet = fabrique;
	Enumeration e = listeAffichage();
	FormeDessin f;
	SommetDessin nouveau_sommet;
	
	Stack objets_a_etudier = new Stack();
	while(e.hasMoreElements())
	    objets_a_etudier.push(e.nextElement());

	undoInfo.newGroup("Replace vertices styles", "Remake vertices styles");
	while(!objets_a_etudier.empty()){
	    f =((FormeDessin)objets_a_etudier.pop());
    
	    if (f.type().equals("vertex")){
		liste_affichage.supprimer((SommetDessin)f);
		nouveau_sommet = fabriqueSommet.creerSommet(this,((SommetDessin)f).centreX(),((SommetDessin)f).centreY(),((SommetDessin)f).getEtiquette(),((SommetDessin)f).getSommet());
		nouveau_sommet.copyAllVariable((SommetDessin)f);
		undoInfo.addInfo(new ChangeFormeDessin(f,nouveau_sommet));
	    }
	}
    }

  
   /**
   * Modificateur de l'attribut numero_sommet.
   **/
  public void numero_sommet(int valeur) {
    numero_sommet = valeur;
  }

  /**
   * Renum�rote tous les sommets en partant de 0 et positionne la variable
   * numero_sommet au prochain numero.
   **/
  public void renumeroterSommets() {
    Enumeration e = listeAffichage();
    numero_sommet = 0;
    while(e.hasMoreElements()){
	FormeDessin f = (FormeDessin)e.nextElement();
	if (f.type().equals("vertex"))
	    ((SommetDessin)f).setEtiquette(Integer.toString(numero_sommet++));
    }
  }



  /**
   * Accesseur fontNormal.
   **/
  public Font fontNormal() {
    return fontNormal;
  }

  /**
   * Accesseur fontNormalEtiquette.
   **/
  public Font fontNormalEtiquette() {
    return fontNormalEtiquette;
  }
  /**
   * Accesseur fontGras.
   **/
  public Font fontGras() {
    return fontGras;
  }

  /**
   * Accesseur fontGrasEtiquette.
   **/
  public Font fontGrasEtiquette() {
    return fontGrasEtiquette;
  }
  
    /**
     *  Modifie la taille des fontes
     **/

    public void setFontSize(float newsize){
	int size = (int)newsize+1;
	fontNormal = new Font("default", Font.PLAIN, size);
	fontNormalEtiquette = new Font("default", Font.PLAIN, size+2);
	fontGras = new Font("default", Font.BOLD, size);
	fontGrasEtiquette = new Font("default", Font.BOLD, size+2);
    }


  /**
   * Retourne une cha�ne de caract�res identifiant le type du graphe courant.
   * Classiquement, chaque classe retourne un type diff�rent.
   **/
  public String type() {
    return "standard graph";
  }


  /**
   * Delete a FormeDessin from the drawing list and the graph lists
   * Remarque : this method doesn't delete incident edges of vertex
   **/
  public void delObject(FormeDessin element) {
      liste_affichage.supprimer(element);
      element.getObjetGraphe().supprimer();
  }


  /**
   * Put a FormeDessin in the drawing list and in the liste of
   * edges and vertices of the du graph.
   **/
  public void putObject(FormeDessin element) {
      liste_affichage.inserer(element);
      element.getObjetGraphe().ajouter();
  }

      
 
  /**
   * Dessiner le graphe sur le Graphics g.
   **/
  public void dessiner(Component c ,Graphics g) {
      Enumeration e = listeAffichage();
      while (e.hasMoreElements())
	  ((FormeDessin)e.nextElement()).dessiner(c,g);
  }

  /**
   * Retourne un objet du graphe se trouvant � la position donn�e.
   **/
  public FormeDessin en_dessous(int x, int y) {
      return en_dessous(x,y,null);
  }

  /**
   * Retourne une arete du graphe se trouvant a la position donnee, mais pas
   * celle passee en argument.
   **/
  public FormeDessin en_dessous(int x, int y, FormeDessin sauf_celui_ci) {
      Enumeration ensemble = listeAffichage();

      // si l'enumeration n'a plus d'element -> NoSuchElementException
      // ok car c'est aussi l'exception "pas d'objet dessous"
      FormeDessin elt = (FormeDessin)ensemble.nextElement();

      while((!elt.appartient(x, y)) || (elt == sauf_celui_ci))
	  elt = (FormeDessin)ensemble.nextElement();
      
      return elt;
  }

  /**
   * Renvoie le nombre d'objets contenus dans le graphe.
   **/
  public int nbObjets() {
    return liste_affichage.taille();
  }

  /**
   * Renvoie l'ensemble des objets contenus dans le graphe sous la forme d'une �num�ration.
   **
   public Enumeration objets() {
   return planche.objets();
   }
  */

  /**
   * Renvoie l'ensemble des objets contenus dans le graphe dans la zone d�termin�e par les
   * coordonn�es donn�es en param�tre.
   **/
  public Enumeration objetsDansRegion(int x1, int y1, int x2, int y2) {
    Vector trouves = new Vector();
    Enumeration e = listeAffichage();

    while(e.hasMoreElements()) {
      FormeDessin elt = (FormeDessin)e.nextElement();
      if(elt.estDansRegion(x1, y1, x2, y2))
	  trouves.addElement(elt);
    }
    return trouves.elements();
  }


  // Methodes personnelles.

  /**
   * Renvoie sous la forme d'une string une description des caract�ristiques graphiques
   * des sommets stock�es lors d'une sauvegarde texte .
   **/
  public String verticesGraphicsPropertiesColumns() {
    return new String("Vertices (index, position, shape, size, color1, color2)");
  }

  /**
   * Renvoie sous la forme d'une string une description des caract�ristiques abstraites
   * des sommets stock�es lors d'une sauvegarde texte .
   **/
  public String edgesAbstractDescriptionColumns() { 
    return new String("List of edges (index, origin, destination)");
  }

  /**
   * Renvoie sous la forme d'une string une description des caract�ristiques graphiques
   * des sommets stock�es lors d'une sauvegarde texte .
   **/
  public String edgesGraphicPropertiesColumns() {
    return new String("Edges (index, shape, color1, color2)");
  }

  /**
   * Retourne un sommet du graphe se trouvant � la position donn�e.<BR>
   * Une exception NoSuchElementException est lev�e si aucun sommet n'est trouv�.
   **/
  public SommetDessin sommet_en_dessous(int x, int y) {
    return (SommetDessin)objet_visu_en_dessous(x, y, null,new String("vertex"));
  }

  /**
   * Retourne un sommet du graphe se trouvant � la position donn�e, mais pas
   * celui pass� en argument.<BR>
   * Une exception NoSuchElementException est lev�e si aucun sommet n'est trouv�.
   **/
  public SommetDessin sommet_en_dessous(int x, int y,
                                      FormeDessin pas_celui_ci) {
    return (SommetDessin)objet_visu_en_dessous(x, y, pas_celui_ci,new String("vertex"));
  }

  /**
   * Retourne une ar�te du graphe se trouvant � la position donn�e.<BR>
   * Une exception NoSuchElementException est lev�e si aucune ar�te n'est trouv�e.
   **/
  public AreteDessin arete_en_dessous(int x, int y) {
    return (AreteDessin)objet_visu_en_dessous(x, y, null, new String("edge"));
  }

  /**
   * Retourne une ar�te du graphe se trouvant � la position donn�e, mais pas
   * celle pass�e en argument.<BR>
   * Une exception NoSuchElementException est lev�e si aucune ar�te n'est trouv�e.
   **/
  public AreteDessin arete_en_dessous(int x, int y, FormeDessin pas_celui_ci) {
    return (AreteDessin)objet_visu_en_dessous(x, y, pas_celui_ci,new String("edge"));
  }

  // Methodes privees.

  /**
   * Retourne un FormeDessin du graphe se trouvant � la position donn�e, mais pas
   * celui pass� en argument.<BR>
   * Une exception NoSuchElementException est lev�e si aucune forme n'est trouv�e.
   **/
  private FormeDessin objet_visu_en_dessous(int x, int y,
      FormeDessin pas_celui_ci, String type_element) {
    // si l'enumeration n'a plus d'element -> NoSuchElementException
    // ok car c'est aussi l'exception "pas d'objet dessous" (malin ...)

    Enumeration enum_objets_visu = listeAffichage();  

    FormeDessin objet_visu_courant =
      (FormeDessin)enum_objets_visu.nextElement();

    while ( (!objet_visu_courant.appartient(x, y)) ||
	    (objet_visu_courant == pas_celui_ci) ||
	    (!type_element.equals(objet_visu_courant.type())))
      objet_visu_courant = (FormeDessin)enum_objets_visu.nextElement();

    return objet_visu_courant;
  }

    // dimensions graphiques du graphe

  public Dimension donnerDimension(){
  	return size;	
  }

  public void changerDimension(Dimension unSize){
  	this.size = unSize;
  } 

    // rechercher un sommet avec l'identifiant specifie

  public SommetDessin  rechercherSommet(String id){
      Enumeration e = listeAffichage();
      FormeDessin f;
      while(e.hasMoreElements())
	  {
	      f = (FormeDessin)e.nextElement();
	      if (f.type().equals("vertex"))
		  if (((SommetDessin)f).getEtiquette().equals(id))
		  return ((SommetDessin)f);	
	  }
      return null;
  }

    
    public AreteDessin rechercherArete(String id1,String id2){
	Enumeration e = listeAffichage();
	FormeDessin f; 
	AreteDessin a;
	while(e.hasMoreElements()){
	    f = (FormeDessin)e.nextElement();
	    if (f.type().equals("edge")){
		a = (AreteDessin)f;
		if (((AreteDessin)a).getArete().origine().getSommetDessin().getEtiquette().equals(id1))
		    if(((AreteDessin)a).getArete().destination().getSommetDessin().getEtiquette().equals(id2))
			return a;
		if (((AreteDessin)a).getArete().origine().getSommetDessin().getEtiquette().equals(id2))
		    if(((AreteDessin)a).getArete().destination().getSommetDessin().getEtiquette().equals(id1))
			return a;
	    }
	}
	return null;
    }
    

    public Graphe getGraphe(){
	return graphe;}


    /**
     * Permet de d�placer l'ensemble des �l�ments contenus dans la s�lection.
     **/
    public static void deplacerFormeDessin(Enumeration e, int dx, int dy) {
	while(e.hasMoreElements())
	    ((FormeDessin)e.nextElement()).deplacer(dx, dy);
    }

    // return the factory used
    public static FabriqueArete getFabriqueArete(){
    	return fabriqueArete;}
    public static FabriqueSommet getFabriqueSommet(){
    	return fabriqueSommet;}	
    public static FabriqueArete getFabriqueAreteFleche() {
	return fabriqueAreteFleche;}

}
