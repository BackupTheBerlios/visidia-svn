package visidia.gui.presentation.factory;

import visidia.gui.metier.Arete;
import visidia.gui.presentation.AreteDessin;
import visidia.gui.presentation.SommetDessin;

public interface FabriqueArete {

	public AreteDessin creerArete(SommetDessin origine,
			SommetDessin destination, Arete a);

	public AreteDessin creerArete(SommetDessin origine, SommetDessin destination);

	public String description();

}
