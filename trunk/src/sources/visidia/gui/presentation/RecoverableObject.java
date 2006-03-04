package visidia.gui.presentation;

import visidia.gui.donnees.*;
import visidia.gui.donnees.conteneurs.*;
import java.util.Enumeration;



public interface RecoverableObject {

    public FormeDessin en_dessous(int x, int y);

    public Enumeration objetsDansRegion(int x1, int y1, int x2, int y2);
    
}
