package visidia.gui.presentation.factory;

import java.io.Serializable;

import visidia.gui.metier.Arete;
import visidia.gui.presentation.AreteDessin;
import visidia.gui.presentation.AreteSegment;
import visidia.gui.presentation.SommetDessin;

public class FabriqueAreteSegment implements FabriqueArete,Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 7755370699313986823L;

	public AreteDessin creerArete(SommetDessin origine, SommetDessin destination, Arete a){
	return new AreteSegment(origine, destination,a);
    }
 
    public AreteDessin creerArete(SommetDessin origine, SommetDessin destination){
	return new AreteSegment(origine, destination);
    }
    
    public String description(){
    	return "Non oriented edge";}
}

