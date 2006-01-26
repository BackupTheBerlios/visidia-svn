package fr.enserb.das.gui.presentation.userInterfaceEdition.undo;

import java.util.*;
import java.io.*;

/** Cette classe contient les informations permettant d'annuler ou de restaurer une op�ration. C'est une sous-classe de Vector, car une op�ration de l'utilisateur peut �tre compos�e de plusieurs op�rations simples. */

public class UndoInfoElement extends Vector{
  
  /** Une description de l'annulation de l'op�ration.*/
  protected String undoDescription;

  /** Une description de la restauration de l'op�ration.*/
  protected String redoDescription;

  /** Instancie un nouvel UndoInfoElement. */
  public UndoInfoElement(String undoDescription, String redoDescription) {
    super();
    this.undoDescription = undoDescription;
    this.redoDescription = redoDescription;
  }
  
  /** Retourne l'�l�ment situ� � l'index i. */
  public UndoObject getInfo(int index) {
    try {
      return ((UndoObject)elementAt(index));
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }
  
  /** Appelle "undo" pour chacun des �l�ments, dans l'ordre inverse de leur introduction dans le vecteur. */
  public void undo() {
    for (int i = (size() - 1); i >= 0; i--) {
      getInfo(i).undo();

    }
  }


  /** Appelle "redo" pour chacun des �l�ments, dans l'ordre de leur introduction dans le vecteur. */
  public void redo() {
    for (int i=0; i< size(); i++) {
      getInfo(i).redo();
    }
  }
  
  /** Retourne la description de l'annulation de l'op�ration.*/
  public String undoDescription() {
    return undoDescription;
  }

  /** Retourne la description de la restauration de l'op�ration.*/
  public String redoDescription() {
    return redoDescription;
  }
}
  
    
    
  
  
