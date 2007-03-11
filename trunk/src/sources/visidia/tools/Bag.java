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

		if (occurrences == null)
			return 0;
		else
			return occurrences.intValue();
	}

	public void add(Object o, long occurrences) {
		long newOccurrences = this.getOccurrencesOf(o) + occurrences;

		this.table.put(o, new Long(newOccurrences));
	}

	public void add(Object o) {
		this.add(o, 1);
	}

	public Hashtable asHashTable() {
		return this.table;
	}

	public Set<Object> keySet() {
		return this.table.keySet();
	}

}
