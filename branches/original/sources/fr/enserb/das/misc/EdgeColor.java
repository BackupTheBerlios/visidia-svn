package fr.enserb.das.misc;

import java.io.*;

/**
 * This class is a generic representation of the color oh an edge.
 * All classes which defined the color of an edge should derivate from this class.
 * @version 1.0
 */
public abstract class EdgeColor implements Cloneable, Serializable {
    
    /**
     * Create a copy of this object.
     */
    public abstract Object clone();
}
