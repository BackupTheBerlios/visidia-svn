package fr.enserb.das.misc;


/**
 *permet de choisir un objet selon sa classe.
 */
public class ArrowMessageCriterion extends MessageCriterion {
    public boolean isMatchedBy(Object o){
	return o instanceof ArrowMessage;	
    }
}
