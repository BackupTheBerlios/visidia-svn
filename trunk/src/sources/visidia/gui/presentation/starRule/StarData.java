package visidia.gui.presentation.starRule;

import java.awt.Color;

/**
 * Stores margins and color data for drawing star rules
 */
public class StarData {

	public static Color ruleColor = new Color(225, 225, 185);

	public static Color contextColor = new Color(187, 225, 185);

	public static int ray = 100;

	public static int rule_top = 60;

	public static int rule_left = 40;

	public static int rule_right = 40;

	public static int rule_bottom = 35;

	public static int rule_center = 110;

	public static int arrow_x1 = StarData.ray * 2 + StarData.rule_left
			+ StarData.rule_center / 4;

	public static int arrow_x2 = StarData.ray * 2 + StarData.rule_left + 3
			* StarData.rule_center / 4;

	public static int arrow_length = StarData.rule_center / 2;

	public static int ctxt_top = 35;

	public static int ctxt_left = 45;

	public static int ctxt_right = 45;

	public static int ctxt_bottom = 35;
}
