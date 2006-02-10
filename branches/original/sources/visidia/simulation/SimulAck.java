package visidia.simulation;

import java.io.Serializable;

/**
 * definit un interface reprensentants une requete envoyee, par le systeme 
 * de simulation, a l'interface graphique pour la visualisation.
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
