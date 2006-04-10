package visidia.tools;

import java.util.Hashtable;
import java.util.Set;

public class Bag {

    private Hashtable<Object, Long> table;

    public Bag() {
	table = new Hashtable();
    }

    public long getOccurrencesOf(Object o) {
	Long occurrences = table.get(o);

	if (occurrences == null)
	    return 0;
	else
	    return occurrences.intValue();
    }

    public void add (Object o, long occurrences) {
	long newOccurrences = getOccurrencesOf(o) + occurrences;

	table.put(o, new Long(newOccurrences));
    }

    public void add (Object o) {
	add(o, 1);
    }

    public Hashtable asHashTable () {
	return table;
    }

    public Set<Object> keySet() {
	return table.keySet();
    }

}
