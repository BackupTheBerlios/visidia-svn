package visidia.simulation;

import java.io.Serializable;

/**
 * cette classe définit une interface reprénsentant une requête
 * envoyée, par le système de simulation, à l'interface graphique pour
 * la visualisation.
 */
public interface SimulAck extends Serializable {
    /**
     * numero de la requete.
     */
    public Long number();

    /**
     * type de la requete.
     */
    public int type();

    
    
}
