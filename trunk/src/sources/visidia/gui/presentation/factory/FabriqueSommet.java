package visidia.gui.presentation.factory;

import visidia.gui.metier.Sommet;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.VueGraphe;

public interface FabriqueSommet {

	public SommetDessin creerSommet(VueGraphe vg, int x, int y, String label,
			Sommet s);

	public SommetDessin creerSommet(VueGraphe vg, int x, int y, String label);

	public String description();

}
