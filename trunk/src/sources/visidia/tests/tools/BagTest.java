package visidia.tests.tools;

import visidia.tools.Bag;

//import junit.framework.TestCase;

public class BagTest extends junit.framework.TestCase {
    
    private Bag bag;
    
    protected void setUp() {
	bag = new Bag();
    }

    public void testAdd() {
	assertEquals(0, bag.getOccurrencesOf(new String("key1")));

	bag.add(new String("key1"));
	assertEquals(1, bag.getOccurrencesOf(new String("key1")));

	bag.add(new String("key2"));
	assertEquals(1, bag.getOccurrencesOf(new String("key1")));
	assertEquals(1, bag.getOccurrencesOf(new String("key2")));

	bag.add(new String("key1"));
	assertEquals(2, bag.getOccurrencesOf(new String("key1")));
	assertEquals(1, bag.getOccurrencesOf(new String("key2")));
    }

    public void testAddWithOccurrences() {
	assertEquals(0, bag.getOccurrencesOf(new Integer(1)));

	bag.add(new Integer(1), 101);
	assertEquals(101, bag.getOccurrencesOf(new Integer(1)));

	bag.add(new Integer(2), 1672);
	assertEquals(101, bag.getOccurrencesOf(new Integer(1)));
	assertEquals(1672, bag.getOccurrencesOf(new Integer(2)));

	bag.add(new Integer(1), 89);
	assertEquals(101 + 89, bag.getOccurrencesOf(new Integer(1)));
	assertEquals(1672, bag.getOccurrencesOf(new Integer(2)));

    }

    public void testEqual() {
	assertEquals(new String("1"), new String ("1"));
	assertFalse(new String("1") == new String("1"));
    }
}
