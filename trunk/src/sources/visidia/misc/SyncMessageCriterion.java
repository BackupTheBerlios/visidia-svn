package visidia.misc;

/**
 *permet de choisir un objet selon sa classe.
 */
public class SyncMessageCriterion extends MessageCriterion {
    public boolean isMatchedBy(Object o){
	return o instanceof SyncMessage;	
    }
}
