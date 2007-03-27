package visidia.tools;

import java.util.Hashtable;
import java.util.Set;

public class Bag {

	private Hashtable<Object, Long> table;

	public Bag() {
		this.table = new Hashtable<Object, Long>();
	}

	public long getOccurrencesOf(Object o) {
		Long occurrences = this.table.get(o);

		if (occurrences == null) {
			return 0;
		} else {
			return occurrences.intValue();
		}
	}

	public void add(Object o, long occurrences) {
		long newOccurrences = this.getOccurrencesOf(o) + occurrences;

		this.table.put(o, new Long(newOccurrences));
	}

	public void add(Object o) {
		this.add(o, 1);
	}

	/*
	 * Compare la valeur courante du Bag correspondant a l'objet o avec la valeur occurences,
	 * et remplace cette valeur du Bag par le minimum des deux
	 */
	public void min(Object o, long occurences){
		if (occurences < this.getOccurrencesOf(o)){
			this.table.put(o, new Long(occurences));
		}	
	}
	
	/*
	 * Compare la valeur courante du Bag correspondant a l'objet o avec la valeur occurences,
	 * et remplace cette valeur du Bag par le maximum des deux
	 */
	public void max(Object o, long occurences){
		if (occurences > this.getOccurrencesOf(o)){
			this.table.put(o, new Long(occurences));
		}	
	}
	
	
	public Hashtable asHashTable() {
		return this.table;
	}

	public Set<Object> keySet() {
		return this.table.keySet();
	}

}
