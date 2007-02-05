package visidia.gui.presentation.userInterfaceEdition.undo;

import java.util.*;

/** Cette classe contient les informations permettant d'annuler ou de
    restaurer une opération.  C'est une sous-classe de Vector, car une
    opération de l'utilisateur peut être composée de plusieurs
    opérations simples. */

public class UndoInfoElement extends Vector{
  
  /** Une description de l'annulation de l'opération.*/
  protected String undoDescription;

  /** Une description de la restauration de l'opération.*/
  protected String redoDescription;

  /** Instancie un nouvel UndoInfoElement. */
  public UndoInfoElement(String undoDescription, String redoDescription) {
    super();
    this.undoDescription = undoDescription;
    this.redoDescription = redoDescription;
  }
  
  /** Retourne l'élément situé à l'index i. */
  public UndoObject getInfo(int index) {
    try {
      return ((UndoObject)elementAt(index));
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }
    
    /** Appelle "undo" pour chacun des éléments, dans l'ordre inverse
     * de leur introduction dans le vecteur. */
    public void undo() {
      for (int i = (size() - 1); i >= 0; i--) {
	getInfo(i).undo();
	
    }
  }


  /** Appelle "redo" pour chacun des éléments, dans l'ordre de leur
   * introduction dans le vecteur. */
  public void redo() {
    for (int i=0; i< size(); i++) {
      getInfo(i).redo();
    }
  }
  
  /** Retourne la description de l'annulation de l'opération.*/
  public String undoDescription() {
    return undoDescription;
  }

  /** Retourne la description de la restauration de l'opération.*/
  public String redoDescription() {
    return redoDescription;
  }
}
  
    
    
  
  
