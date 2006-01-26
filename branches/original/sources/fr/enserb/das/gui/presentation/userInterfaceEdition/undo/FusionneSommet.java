package fr.enserb.das.gui.presentation.userInterfaceEdition.undo;


import fr.enserb.das.gui.presentation.*;
import fr.enserb.das.gui.metier.*;
import java.util.*;

/** Cette classe contient les informations pour annuler les fusions de sommets
intervenant lors du d�placement d'un sommet existant : il faut pouvoir recr�er 
le sommet qui est d�truit lors la fusion, lui redonner sa position initiale, 
et r�affecter les extr�mit�s des ar�tes.*/
public class FusionneSommet implements UndoObject {
  
  protected SommetDessin sommetDetruit;//le sommet qui sera d�truit par la fusion(le sommet qu'on d�place) 
  protected SommetDessin sommetGarde; //le sommet sur lequel est r�alis� la fusion.
  private Vector aretesEntrantes;
  private Vector aretesSortantes;  
  protected int original_X; //l'abscisse initiale du sommet d�truit.
  protected int original_Y; //l'ordonn�e initiale du sommet d�truit.
  
  public FusionneSommet(SommetDessin sommetDetruit,
		       SommetDessin sommetGarde,
		       int original_X,
		       int original_Y) {
    this.sommetDetruit = sommetDetruit;
    this.sommetGarde = sommetGarde;
    aretesEntrantes = new Vector();
    aretesSortantes = new Vector();
    Enumeration e = sommetDetruit.getSommet().aretesEntrantes();
    while (e.hasMoreElements()) {
	this.aretesEntrantes.addElement(((Arete)e.nextElement()).getAreteDessin());
    }
    e = (sommetDetruit.getSommet()).aretesSortantes();
    while (e.hasMoreElements()) {
	this.aretesSortantes.addElement(((Arete)e.nextElement()).getAreteDessin());
    }
    this.original_X = original_X;
    this.original_Y = original_Y;
  }
  
  public void undo() {
      sommetDetruit.getVueGraphe().putObject(sommetDetruit);
      int i;
      for (i=0; i<aretesSortantes.size(); i++) {
	  ((AreteDessin)aretesSortantes.elementAt(i)).changerOrigine(sommetDetruit);
      }
      for (i=0; i<aretesEntrantes.size(); i++) {
	  ((AreteDessin)aretesEntrantes.elementAt(i)).changerDestination(sommetDetruit);
      }
      sommetDetruit.placer(original_X, original_Y);
  }


  public void redo() {
      int i;
      for (i=0; i<aretesSortantes.size(); i++) {
	  ((AreteDessin)aretesSortantes.elementAt(i)).changerOrigine(sommetGarde);
      }
      for (i=0; i<aretesEntrantes.size(); i++) {
	  ((AreteDessin)aretesEntrantes.elementAt(i)).changerDestination(sommetGarde);
      }
      sommetDetruit.getVueGraphe().delObject(sommetDetruit);
  }

  public FormeDessin content() {
    return sommetDetruit;
  }
}
