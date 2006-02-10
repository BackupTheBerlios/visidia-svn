package visidia.gui.presentation.userInterfaceEdition.undo;

import visidia.gui.presentation.*;
import java.util.*;
import java.io.*;

/** Cette classe implemente la structure de donnees utilisee pour gerer les operations de undo/redo. **/

public class UndoInfo extends Vector {
  
  /** L'index de l'operation courante. */
  protected int curseur;
  
  /** Instancie un nouvel objet UndoInfo vide.*/
  public UndoInfo() {
    super();
    curseur = -1;
  }
  
  /* ************************************************************* */
  /* Methodes privees "accessoires", pour faciliter l'implementation
    des autres methodes.*/
  /* ************************************************************* */



  /** Accesseur au curseur. */
  private int curseur() {
    return curseur;
  }

  /** Retourne le groupe d'operations courant.*/
  private UndoInfoElement currentGroup() {
      try { 
	  return ((UndoInfoElement)elementAt(curseur));
      } catch (ArrayIndexOutOfBoundsException e) {
	  return null;
      }
  }


  /** Retourne le groupe d'operations a l'index "i" */
  private UndoInfoElement groupAt(int i) {
      try{
	  return ((UndoInfoElement)elementAt(i));
      } catch (ArrayIndexOutOfBoundsException e) {
	  return null;
      }
  }
  /* ************************************************************* */
  /* Methodes 'fondamentales' : le strict minimum.                 */
  /* ************************************************************* */
  
  /** Cree un nouveau groupe d'operations
   * @param undo la description de l'annulation de l'operation
   * @param redo la description de la restauration l'operation
   */
  public void newGroup(String undo, String redo) {
    if (redoMore()) {
      trimGroups();
    }
    addElement(new UndoInfoElement(undo, redo));
    curseur++;
  }

  /** Ajoute une operation dans la liste, et incremente le nombre d'operations simples dans l'operation complexe courante. */
  public void addInfo(UndoObject objet) {
      if(currentGroup() != null)
	  currentGroup().add(objet);
  }

  /** Detruit les informations concernant les groupes crees posterieurement au groupe courant. */  
  private void trimGroups() {
    removeRange(curseur() + 1, size());
  }    
  
  /** Cette methode permet d'annuler l'operation complexe courante, en annulant chacune des operations simples qui la composent. */
  public void undo() {
      if(currentGroup() != null) {
	  currentGroup().undo();
	  curseur--;
      }
  }
  
  /** Cette methode permet de restaurer l'operation complexe courante, en restaurant chacune des operations simples qui la composent. */
  public void redo() {
      //if(currentGroup() != null) {
      try{
	  curseur++;
	  currentGroup().redo();
	  //}
      } catch (Exception e) {
      }
  }
    
  /** Retourne VRAI si il reste au moins une operation complexe susceptible d'etre annulee. */
  public boolean undoMore() {
    return (curseur >= 0);
  }

  /** Retourne VRAI si il reste au moins une operation complexe susceptible d'etre restauree. */
  public boolean redoMore() {
    return (curseur < (size() - 1));
  }

  /* ************************************************************* */
  /* methodes rendues necessaires par certaines fonctionnalites    */
  /* d'autographe (ex : glissement d'un sommet...)                 */
  /* ************************************************************* */

  /** Supprime le groupe d'operations courant. */
  public void removeEmptyGroup() {
      if(currentGroup() != null) {
	  if (currentGroup().isEmpty()) {
	      remove(curseur);
	      curseur = curseur - 1;
	  }
      }
  }

  /** Annule la derniere operation et supprime le groupe correspondant. */
  public void undoAndRemove() {
    undo();
    remove(curseur + 1);
  }

  /**  Retire l'UndoObject qui contient la FormeDessin passee en argument,  dans le groupe courant */
  public void removeObject(FormeDessin objet) {
    int i=0;
    try {
	while (!objet.equals(currentGroup().getInfo(i).content())) {
	    i++;
	}
	currentGroup().remove(i);
    } catch (ArrayIndexOutOfBoundsException e) {
	return;
    }
  }
  

  /* ************************************************************* */
  /* methodes 'd'agrement' : elles raffinent le systeme           */
  /* (undo/redo par lots, affichage des descriptions...)           */
  /* ************************************************************* */

  /** Cette methode permet d'annuler les "i" dernieres operations (ou toutes les operations si il y en a moins de "i") */
  public void undo(int i) {
    int compteur = 0;
    try{
	while (undoMore() && compteur < i) {
	    currentGroup().undo();
	    curseur--;
	    compteur++;
	}
    } catch (Exception e) {
	return;
    }
  }
  
  /** Cette methode permet de restaurer les "i" dernieres operations (ou toutes les operations si il y en a moins de "i")*/
  public void redo(int i) {
    int compteur = 0;
    try{
	while (redoMore() && compteur < i) {
	    curseur++;
	    currentGroup().redo();
	    compteur++;
	}
    } catch(Exception e) {
	return;
    }
  }

  /** Retourne la description de ce qui sera fait lors du prochain undo*/
  public String undoDescription() {
    if (undoMore()) {
      return (currentGroup().undoDescription());
    } else { 
      return "Undo";
    }
  }
  
  /** Retourne la description de ce qui sera fait lors du prochain redo*/
  public String redoDescription() {
    if (redoMore()) {
      return (groupAt(curseur() + 1).redoDescription());
    } else { 
      return "Redo";
    }
  }
  
  
}



