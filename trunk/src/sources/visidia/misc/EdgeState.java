package visidia.misc;

import java.io.Serializable;
/**
 * cette classe est une représentattion générique de l'état d'un arête.
 * Toute classe définissant l'état d'une arête devrait dériver de cette
 * classe.
 */
public abstract class EdgeState implements Cloneable, Serializable {
    
    /**
     * cree une copy de cet objet.
     */
    public abstract Object clone();
}
