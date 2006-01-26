package fr.enserb.das.misc;


/**
 *permet de choisir un objet selon sa classe.
 */
public class StringMessageCriterion extends MessageCriterion {
    public boolean isMatchedBy(Object o){
	return o instanceof StringMessage;	
    }
}
