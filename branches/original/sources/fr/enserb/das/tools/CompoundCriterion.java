package fr.enserb.das.tools;

/**
 * cette classe definit un critere qui est la conjonctions d'une liste de criteres.
 * Lorsque la liste  est vide la methode <code>isMatchedBy()</code> retourne <code>false</code>.
 */
import java.util.LinkedList;
import java.util.Iterator;

public class CompoundCriterion implements Criterion{
    private LinkedList criterionList = null;

    /**
     * Contruit une nouvelle classe de composition de criteres. A la creation
     * elle ne contient aucun critere.
     */
    public CompoundCriterion(){
	criterionList = new LinkedList();
    }

    /**
     * ajoute un critere a la liste de criteres.
     */
    public void add(Criterion c){
	criterionList.add(c);
    }

    /**
     * supprime un critere de la liste des criteres.
     */
    public boolean remove(Criterion c){
	return criterionList.remove(c);
    }

    /**
     * supprime tous les criteres.
     */
    public void removeAllCriterion(){
	criterionList =  new LinkedList();
    }

    public boolean isMatchedBy(Object o){
	if( criterionList.isEmpty() )
	    return false;

	Iterator iterator = criterionList.iterator();
	while(iterator.hasNext()){
	    Criterion c = (Criterion) iterator.next();
	    if(!c.isMatchedBy(o))
		return false;
	}

	return true;
    }

}
	    
