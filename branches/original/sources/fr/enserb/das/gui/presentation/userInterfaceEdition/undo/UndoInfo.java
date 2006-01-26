package fr.enserb.das.gui.presentation.userInterfaceEdition.undo;

import fr.enserb.das.gui.presentation.*;
import java.util.*;
import java.io.*;

/** Cette classe impl�mente la structure de donn�es utilis�e pour g�rer les op�rations de undo/redo. **/

public class UndoInfo extends Vector {
  
  /** L'index de l'op�ration courante. */
  protected int curseur;
  
  /** Instancie un nouvel objet UndoInfo vide.*/
  public UndoInfo() {
    super();
    curseur = -1;
  }
  
  /* ************************************************************* */
  /* M�thodes priv�es "accessoires", pour faciliter l'impl�mentation
    des autres m�thodes.*/
  /* ************************************************************* */



  /** Accesseur au curseur. */
  private int curseur() {
    return curseur;
  }

  /** Retourne le groupe d'op�rations courant.*/
  private UndoInfoElement currentGroup() {
    return ((UndoInfoElement)elementAt(curseur));
  }

  /** Retourne le groupe d'op�rations � l'index "i" */
  private UndoInfoElement groupAt(int i) {
    return ((UndoInfoElement)elementAt(i));
  }

  /* ************************************************************* */
  /* M�thodes 'fondamentales' : le strict minimum.                 */
  /* ************************************************************* */
  
  /** Cr�e un nouveau groupe d'op�rations
   * @param undo la description de l'annulation de l'op�ration
   * @param redo la description de la restauration l'op�ration
   */
  public void newGroup(String undo, String redo) {
    if (redoMore()) {
      trimGroups();
    }
    addElement(new UndoInfoElement(undo, redo));
    curseur++;
  }

  /** Ajoute une op�ration dans la liste, et incr�mente le nombre d'op�rations simples dans l'op�ration complexe courante. */
  public void addInfo(UndoObject objet) {
    currentGroup().add(objet);
  }

  /** Detruit les informations concernant les groupes cr��s post�rieurement au groupe courant. */  
  private void trimGroups() {
    removeRange(curseur() + 1, size());
  }    
  
  /** Cette m�thode permet d'annuler l'op�ration complexe courante, en annulant chacune des op�rations simples qui la composent. */
  public void undo() {
    currentGroup().undo();
    curseur--;
  }
  
  /** Cette m�thode permet de restaurer l'op�ration complexe courante, en restaurant chacune des op�rations simples qui la composent. */
  public void redo() {
    curseur++;
    currentGroup().redo();
  }
    
  /** Retourne VRAI si il reste au moins une op�ration complexe susceptible d'�tre annul�e. */
  public boolean undoMore() {
    return (curseur >= 0);
  }

  /** Retourne VRAI si il reste au moins une op�ration complexe susceptible d'�tre restaur�e. */
  public boolean redoMore() {
    return (curseur < (size() - 1));
  }

  /* ************************************************************* */
  /* m�thodes rendues n�cessaires par certaines fonctionnalit�s    */
  /* d'autographe (ex : glissement d'un sommet...)                 */
  /* ************************************************************* */

  /** Supprime le groupe d'op�rations courant. */
  public void removeEmptyGroup() {
    if (currentGroup().isEmpty()) {
      remove(curseur);
      curseur = curseur - 1;
    }
  }

  /** Annule la derni�re op�ration et supprime le groupe correspondant. */
  public void undoAndRemove() {
    undo();
    remove(curseur + 1);
  }

  /**  Retire l'UndoObject qui contient la FormeDessin pass�e en argument,  dans le groupe courant */
  public void removeObject(FormeDessin objet) {
    int i=0;
    try {
	  while (!objet.equals(currentGroup().getInfo(i).content())) {
	i++;
	}
     	  currentGroup().remove(i);
    } catch (ArrayIndexOutOfBoundsException e) {
    }
  }
  

  /* ************************************************************* */
  /* m�thodes 'd'agr�ment' : elles raffinent le syst�me           */
  /* (undo/redo par lots, affichage des descriptions...)           */
  /* ************************************************************* */

  /** Cette m�thode permet d'annuler les "i" derni�res op�rations (ou toutes les op�rations si il y en a moins de "i") */
  public void undo(int i) {
    int compteur = 0;
    while (undoMore() && compteur < i) {
      currentGroup().undo();
      curseur--;
      compteur++;
    }
  }
  
  /** Cette m�thode permet de restaurer les "i" derni�res op�rations (ou toutes les op�rations si il y en a moins de "i")*/
  public void redo(int i) {
    int compteur = 0;
    while (redoMore() && compteur < i) {
      curseur++;
      currentGroup().redo();
      compteur++;
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



