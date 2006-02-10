package visidia.gui.presentation.userInterfaceEdition.undo;

import java.io.*;
import visidia.gui.presentation.*;

/** Cette interface est implementee par les objets representant des operations susceptibles d'etre annulees ou restaurees (creation, suppression, selection, deselection ou deplacements de sommets ou d'aretes). */
public interface UndoObject extends Serializable {
  
  /** Cette methode annule l'operation. */
  public void undo();
  
  /** Cette methode retablit l'operation. */
  public void redo();

  /** Cette methode retourne l'objet concerne par l'operation. */
  public FormeDessin content();
}
