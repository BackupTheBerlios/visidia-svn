package visidia.gui.presentation.userInterfaceEdition.undo;

import java.util.*;

/** Cette classe contient les informations permettant d'annuler ou de
    restaurer une opération.  C'est une sous-classe de Vector, car une
    opération de l'utilisateur peut être composée de plusieurs
    opérations simples. */

public class UndoInfoElement extends Vector{
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 4338558692800561187L;

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
      return ((UndoObject)this.elementAt(index));
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }
    
    /** Appelle "undo" pour chacun des éléments, dans l'ordre inverse
     * de leur introduction dans le vecteur. */
    public void undo() {
      for (int i = (this.size() - 1); i >= 0; i--) {
	this.getInfo(i).undo();
	
    }
  }


  /** Appelle "redo" pour chacun des éléments, dans l'ordre de leur
   * introduction dans le vecteur. */
  public void redo() {
    for (int i=0; i< this.size(); i++) {
      this.getInfo(i).redo();
    }
  }
  
  /** Retourne la description de l'annulation de l'opération.*/
  public String undoDescription() {
    return this.undoDescription;
  }

  /** Retourne la description de la restauration de l'opération.*/
  public String redoDescription() {
    return this.redoDescription;
  }
}
  
    
    
  
  
