package visidia.gui.presentation.userInterfaceEdition;

import java.util.*;
import visidia.gui.presentation.*;
import visidia.gui.metier.*;
import visidia.gui.donnees.conteneurs.*;

/**
 * La classe Traitement ne peut etre instanciée, elle contient
 * simplement quelques fonctions de traitement des graphes de
 * l'éditeur Editeur.  C'est une "boite a outils".
 **/
public class Traitements {

    private Traitements() {
    }

    
    /**
     * On passe a cette méthode une enumeration de sommets et d'arêtes.
     *
     * @return un ensemble contenant tous les sommets de l'enumeration
     * enumeration et les sommets incidents des arêtes de cette meme
     * enumeration.
     **/
    public static Ensemble sommetsTotaux(Enumeration v_enum){
	Ensemble ensemble = new Ensemble();
	while(v_enum.hasMoreElements()) {
	    FormeDessin f = (FormeDessin)v_enum.nextElement();
	    if (f.type().equals("vertex"))ensemble.inserer(f);
	    if (f.type().equals("edge")){
		ensemble.inserer(((AreteDessin)f).getArete().origine().getSommetDessin());
		ensemble.inserer(((AreteDessin)f).getArete().destination().getSommetDessin());
	    }
	}
	return ensemble;
    }

    /* On passe a cette methode une enumeration de sommets et d'arêtes. 
     * 
     * @return un ensemble contenant tous les sommets et arêtes de l'
     * enumeration et les arêtes incidentes aux sommets de cette même
     * enumeration.
     */
    public static Ensemble elementsTotaux(Enumeration v_enum){
	Ensemble ensemble = new Ensemble();
	while(v_enum.hasMoreElements()) {
	    FormeDessin f = (FormeDessin)v_enum.nextElement();
	    if (f.type().equals("edge"))ensemble.inserer(f);
	    if (f.type().equals("vertex")){
		Enumeration e = (((SommetDessin)f).getSommet().aretesIncidentes());
		while (e.hasMoreElements())
		    ensemble.inserer(((Arete)e.nextElement()).getAreteDessin());
		ensemble.inserer(f);
	    }
	}
	return ensemble;
    }
    
  
    // renvoie les sommetDessin de cette enumeration
    public static Enumeration sommetDessin(Enumeration e){
	Ensemble ens_Temp=new Ensemble();
	FormeDessin elt;
	while (e.hasMoreElements()) {
	    elt = (FormeDessin)e.nextElement();
	    if (elt.type().equals("vertex"))
		ens_Temp.inserer(elt);
	}
	return ens_Temp.elements();
    }

    // renvoie les areteDessin de cette enumeration
    public static Enumeration areteDessin(Enumeration e){
	Ensemble ens_Temp=new Ensemble();
	FormeDessin elt;
	while (e.hasMoreElements()) {
	    elt = (FormeDessin)e.nextElement();
	    if (elt.type().equals("edge"))
		ens_Temp.inserer(elt);
	}
	return ens_Temp.elements();
    }



    /**
     * Cette méthode complete le sous-graphe formé par ces sommets.
     *
     * @param e enumeration de SommetDessin
     *
     * @return an enumeration of created edges
     **/
    public static Enumeration completersousGraphe(Enumeration v_enum) {
	Ensemble ensembleArete = new Ensemble();
	Enumeration e = sommetDessin(v_enum);
	if(e.hasMoreElements()) {
	    SommetDessin sommet_courant = (SommetDessin)e.nextElement();
	    VueGraphe un_graphe = (VueGraphe)sommet_courant.getVueGraphe();
	  
	    Vector sommets_deja_traites = new Vector();
	    sommets_deja_traites.addElement(sommet_courant);
	  
	    while(e.hasMoreElements()) {
		sommet_courant = (SommetDessin)e.nextElement();
		Enumeration une_enumeration = sommets_deja_traites.elements();
	      
		while(une_enumeration.hasMoreElements()) {
		    SommetDessin un_sommet = (SommetDessin)une_enumeration.nextElement();
		    if(sommet_courant.getSommet().aPourVoisin(un_sommet.getSommet()) == 0) 
			ensembleArete.inserer(un_graphe.creerArete(un_sommet, sommet_courant));
		}
	      
		sommets_deja_traites.addElement(sommet_courant);
	    }
	    sommets_deja_traites.removeAllElements();
	}
	return ensembleArete.elements();
    }

    /**
     * Duplique tous les éléments contenus dans une sélection.
     *
     * @return une sélection contenant tous les clones.
     **/
    public static SelectionDessin dupliquerSelectionDessin(
							   SelectionDessin une_selection) {
	Ensemble selection_clones = new Ensemble();
	//  (SelectionDessin)une_selection.getVueGraphe().creerSelection();
	Hashtable sommets_clones = new Hashtable();
	Enumeration e;
	 
	int i = 0; //a enlever
      
	e = une_selection.elements();
	while(e.hasMoreElements()) {
	    FormeDessin forme_courante = (FormeDessin)e.nextElement();

	    // D'abord on clone les sommets
	    if (forme_courante.type().equals("vertex")){
		SommetDessin sommet_courant, sommet_clone;
		sommet_courant = (SommetDessin)forme_courante;
		if(!sommets_clones.containsKey(sommet_courant)){
		    sommets_clones.put(sommet_courant,
				       sommet_clone = (SommetDessin)sommet_courant.cloner());
		    selection_clones.inserer(sommet_clone);
		}
	    }
      
     
	    // On passe aux aretes
	    if (forme_courante.type().equals("edge")){
		AreteDessin arete_courante = (AreteDessin)forme_courante;
		SommetDessin origine, origine_clone, destination, destination_clone;
		origine = arete_courante.getArete().origine().getSommetDessin();
		destination = arete_courante.getArete().destination().getSommetDessin();
	      
		if(sommets_clones.containsKey(origine))
		    origine_clone = (SommetDessin)sommets_clones.get(origine);
		else {
		    sommets_clones.put(origine,
				       origine_clone = (SommetDessin)origine.cloner());
		    selection_clones.inserer(origine_clone);
		}
	 
		if(sommets_clones.containsKey(destination))
		    destination_clone = (SommetDessin)sommets_clones.get(destination);
		else {
		    sommets_clones.put(destination,
				       destination_clone = (SommetDessin)destination.cloner());
		    selection_clones.inserer(destination_clone);
		}
	 
		selection_clones.inserer((AreteDessin)arete_courante.cloner(origine_clone,
									    destination_clone));
	    }
	}
	une_selection.deSelect();
	e = selection_clones.elements();
	while(e.hasMoreElements()) {
	    une_selection.insererElement((FormeDessin)e.nextElement());
	}
	//une_selection.select();
	return une_selection;
    }

    /**
     * Cette méthode sélectionne le sous_graphe connexe auquel
     * appartient le sommet passe en argument.  Le calcul tient compte
     * de l'orientation des arêtes.
     *
     * @return une selection contenant les sommets et les arêtes du
     * sous-graphe connexe contenant le sommet.
     **/
    public static SelectionDessin sousGrapheConnexeOriente(
							   Enumeration sommets_initiaux) {
	if(!sommets_initiaux.hasMoreElements())
	    return null;
      
	// initialisation des structures de donnees
	SommetDessin sommet_courant = (SommetDessin)sommets_initiaux.nextElement();
	// la selection contient tous les sommets et arêtes déjà rencontrés
	SelectionDessin selection_connexe = new SelectionDessin();
	// la pile contient tous les sommets et aretes deja rencontres pour lesquels
	// on n'a pas encore fait un tour de boucle
	/* Stack sommets_restants = new Stack();

	selection_connexe.insererElement(sommet_courant);
	sommets_restants.push(sommet_courant);
	while(sommets_initiaux.hasMoreElements()) {
	sommet_courant = (Sommet)sommets_initiaux.nextElement();
	selection_connexe.insererElement(sommet_courant);
	sommets_restants.push(sommet_courant);
	}

	// boucle principale: un tour de boucle pour chacun des sommets rencontres
	do {
	sommet_courant = (Sommet)sommets_restants.pop();
	// on s'occupe des sommets successeurs
	Enumeration successeurs = sommet_courant.successeurs();
	while(successeurs.hasMoreElements()) {
	Sommet un_successeur = (Sommet)successeurs.nextElement();
	// Si c'est la premiere fois qu'on recontre le sommets, on le met
	// dans la selection
	if(!selection_connexe.contient(un_successeur)) {
	selection_connexe.insererElement(un_successeur);
	sommets_restants.push(un_successeur);
	}
	}

	// on s'occupe des aretes sortantes
	Enumeration aretes_sortantes = sommet_courant.aretesSortantes();
	while(aretes_sortantes.hasMoreElements())
	selection_connexe.insererElement(
	(Arete)aretes_sortantes.nextElement());
	}
	while(!sommets_restants.empty())
	;
	*/

	return selection_connexe;
    }

    /**
     * Cette méthode sélectionne le sous_graphe connexe auquel
     * appartient le sommet passé en argument.  Le calcul ne tient pas
     * compte de l'orientation des arêtes.
     *
     * @return une sélection contenant les sommets et les arêtes du
     * sous-graphe connexe contenant le sommet.
     **/
    public static SelectionDessin sousGrapheConnexe(Enumeration v_enum) {
	Enumeration sommets_initiaux = sommetDessin(v_enum);
	if(!sommets_initiaux.hasMoreElements())
	    return null;

	// initialisation des structures de donnees
	SommetDessin sommet_courant = (SommetDessin)sommets_initiaux.nextElement();
	// la selection contient tous les sommets et aretes deja rencontres
	SelectionDessin selection_connexe = new SelectionDessin();
	// la pile contient tous les sommets et aretes deja rencontres pour lesquels
	// on n'a pas encore fait un tour de boucle
	/*
	  Stack sommets_restants = new Stack();

	  selection_connexe.insererElement(sommet_courant);
	  sommets_restants.push(sommet_courant);
	  while(sommets_initiaux.hasMoreElements()) {
	  sommet_courant = (SommetDessin)sommets_initiaux.nextElement();
	  selection_connexe.insererElement(sommet_courant);
	  sommets_restants.push(sommet_courant);
	  }

	  // boucle principale: un tour de boucle pour chacun des sommets rencontres
	  do {
	  sommet_courant = (SommetDessin)sommets_restants.pop();
	  // on s'occupe des sommets voisins
	  Enumeration voisins = sommet_courant.sommetsVoisins();
	  while(voisins.hasMoreElements()) {
	  SommetDessin un_voisin = (SommetDessin)voisins.nextElement();
	  // Si c'est la premiere fois qu'on recontre le sommets, on le met
	  // dans la selection
	  if(!selection_connexe.contient(un_voisin)) {
	  selection_connexe.insererElement(un_voisin);
	  sommets_restants.push(un_voisin);
	  }
	  }

	  // on s'occupe des aretes incidentes
	  Enumeration aretes_incidentes = sommet_courant.aretesIncidentes();
	  while(aretes_incidentes.hasMoreElements())
	  selection_connexe.insererElement(
	  (Arete)aretes_incidentes.nextElement());
	  }
	  while(!sommets_restants.empty()) ;
	*/

	return selection_connexe;
    }

}
