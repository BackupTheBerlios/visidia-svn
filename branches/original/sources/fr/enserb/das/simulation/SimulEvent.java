package fr.enserb.das.simulation;

import java.io.Serializable;

/**
 * definit un interface reprensentants une requete envoyee, par le systeme 
 * de simulation, a l'interface graphique pour la visualisation.
 */
public interface SimulEvent extends Serializable {
    /**
     * numero de la requete.
     */
    public Long eventNumber();


    /**
     * type de la requete.
     */
    public int type();
}
