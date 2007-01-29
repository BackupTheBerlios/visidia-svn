package visidia.tools;

import java.util.Hashtable;
import java.util.Set;

/**
 * I   implement  a   collection  which   stores  objects   and  their
 * occurences. Objects are tested with their equals method.
 *
 * @see visidia.tests.tools.Bag
 */
public class Bag {

    private Hashtable<Object, Long> table;

    /**
     * Construct a new empty Bag.
     */
    public Bag() {
	table = new Hashtable();
    }

    /**
     * Returns    the     occurence    of    object     o    in    the
     * collection.  Differentes occurences  are  compared through  the
     * equals method.
     */
    public long getOccurrencesOf(Object o) {
	Long occurrences = table.get(o);

	if (occurrences == null)
	    return 0;
	else
	    return occurrences.intValue();
    }

    /**
     * Adds a new object to the collection <code>occurrences</code> times.
     */
    public void add (Object o, long occurrences) {
	long newOccurrences = getOccurrencesOf(o) + occurrences;

	table.put(o, new Long(newOccurrences));
    }

    /**
     * Adds a new object to the collection.
     */
    public void add (Object o) {
	add(o, 1);
    }

    /**
     * Returns a hashtable representing  the bag. Keys are the objects
     * you added. Values represents occurrences.
     */
    public Hashtable<Object, Long> asHashTable () {
	return table;
    }

    /**
     * Returns a Set containing all the objects you added to the bag.
     */
    public Set<Object> keySet() {
	return table.keySet();
    }
}

