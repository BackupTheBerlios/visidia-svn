package visidia.gui.metier.simulation;

import java.util.*;
import visidia.gui.metier.*;
import visidia.graph.*;
import java.io.*;


/** cette classe contient une methode statique de conversion d'un
 * grapheVisu cree par l'interface graphique en un graphe utilise par
 * les algorithmes de simulation
 */
public class Convertisseur {
	
	public static SimpleGraph convertir(Graphe  ancienGraphe){
		
		SimpleGraph nouveauGraph = new SimpleGraph();
		Enumeration enumerationSommets = ancienGraphe.sommets();
	       
		Enumeration enumerationAretes = ancienGraphe.aretes();
		int taille = ancienGraphe.ordre(); 
		Sommet unSommet;
		Arete uneArete;
		
		while(enumerationSommets.hasMoreElements()){ 
		    unSommet = (Sommet)enumerationSommets.nextElement();
		    nouveauGraph.put(new Integer(unSommet.getSommetDessin()
                                                 .getEtiquette()));
		    nouveauGraph.vertex(new Integer(unSommet
                                                    .getSommetDessin()
                                                    .getEtiquette()))
                        .setData(unSommet.getSommetDessin().getStateTable()
                                 .clone());
		}
		while(enumerationAretes.hasMoreElements()){
		    uneArete = (Arete)enumerationAretes.nextElement();
		    if (uneArete.getAreteDessin().forme()
                        .equals("FlecheSimple")) {
			Integer origine = new Integer(uneArete.origine()
                                                      .getSommetDessin()
                                                      .getEtiquette());
			Integer dest = new Integer(uneArete.destination()
                                                   .getSommetDessin()
                                                   .getEtiquette());
			nouveauGraph.orientedLink(origine, dest);
		    }
		    
		    else 
			     
			nouveauGraph.link(new Integer(uneArete.origine().
                                                      getSommetDessin().
                                                      getEtiquette()),
			new Integer(uneArete.destination().getSommetDessin().
                                    getEtiquette()));
		}
		
		return nouveauGraph;
	}

    public static SimpleGraph convert(Graphe oldGraph,
                                      Hashtable agentsPosition) {
        SimpleGraph graph = convertir(oldGraph);
        int i;
        Enumeration e;

        e = agentsPosition.keys();

        while(e.hasMoreElements()) {

            Integer key = (Integer)e.nextElement();
            Collection agentsNames = (Collection)agentsPosition.get(key);

            graph.vertex(key).setAgentsNames(new Vector(agentsNames));
        }
        
        return graph;
    }
}
