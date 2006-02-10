package visidia.misc;

import visidia.tools.*;

/**
 * classe representant un critere de selection portant sur un objet
 * de type <code>Message</code>.
 */
public class  MessageCriterion implements Criterion {
    /**
     * methode de test de l'objet.
     */
    public boolean isMatchedBy(Object mesg){
	return false;
    }
}


