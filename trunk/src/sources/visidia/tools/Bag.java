package visidia.tools;

import java.util.Hashtable;
import java.util.Set;

public class Bag {

    private Hashtable<Object, Integer> table;

    public Bag() {
	table = new Hashtable();
    }

    public int getOccurrencesOf(Object o) {
	Integer occurrences = table.get(o);

	if (occurrences == null)
	    return 0;
	else
	    return occurrences.intValue();
    }

    public void add (Object o, int occurrences) {
	int newOccurrences = getOccurrencesOf(o) + occurrences;

	table.put(o, new Integer(newOccurrences));
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