package fr.enserb.das.gui.metier;
import fr.enserb.das.gui.presentation.FormeDessin;
import java.io.Serializable;

/**
 * The ObjetGraphe interface has to be implemented
 * by objects which are susceptible to belongs to a graph. 
 * The methods of this
 * interface permit to add or delete
 * a ObjetGraphe in a Graph
 *
 **/
public abstract class ObjetGraphe implements Serializable  {

    Graphe graph;
    FormeDessin formeDessin;


  /**
   * Ajoute l'objet dans son graphe.
   * Cette méthode sera utilisée pour les undo/redo.
   **/
  public abstract void ajouter();

  /**
   * Supprime l'objet de son graphe.
   * Cette méthode sera utilisée pour les undo/redo.
   **/
  public abstract void supprimer();

    // accessor on the graph 
    public Graphe graphe(){
	return graph;}

    // modificator of "graph" variable
    public void setGraph(Graphe newGraph){
	graph = newGraph;
    }

}







