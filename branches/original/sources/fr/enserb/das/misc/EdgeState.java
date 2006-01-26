package fr.enserb.das.misc;

import java.io.*;
/**
 * cette classe est une representattion generique de l'etat d'un arete.
 * Toute classe definissant l'etat d'une arete devrait deriver de cette
 * classe.
 */
public abstract class EdgeState implements Cloneable, Serializable {
    
    /**
     * cree une copy de cet objet.
     */
    public abstract Object clone();
}
