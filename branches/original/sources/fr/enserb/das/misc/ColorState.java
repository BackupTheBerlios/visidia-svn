package fr.enserb.das.misc;

import java.awt.Color;

/**
 * This class set the new color
 * @version 1.0
 */
public class ColorState extends EdgeColor {

    Color isColor;
    
    public ColorState(Color c){
	isColor = c;
    }

    public Color isColored(){
	return isColor;
    }

    public Object clone(){
	return new ColorState(isColor);
    }
}
