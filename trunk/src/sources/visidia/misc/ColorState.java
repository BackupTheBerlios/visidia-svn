package visidia.misc;

import java.awt.Color;

/**
 * This class set the new color
 * 
 * @version 1.0
 */
public class ColorState extends EdgeColor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5260797950185458080L;

	Color isColor;

	public ColorState(Color c) {
		this.isColor = c;
	}

	public Color isColored() {
		return this.isColor;
	}

	public Object clone() {
		return new ColorState(this.isColor);
	}
}
