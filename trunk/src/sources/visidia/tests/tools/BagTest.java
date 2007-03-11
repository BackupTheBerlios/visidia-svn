package visidia.tests.tools;

import junit.framework.Assert;
import visidia.tools.Bag;

public class BagTest extends junit.framework.TestCase {

	private Bag bag;

	protected void setUp() {
		this.bag = new Bag();
	}

	public void testAdd() {
		Assert.assertEquals(0, this.bag.getOccurrencesOf(new String("key1")));

		this.bag.add(new String("key1"));
		Assert.assertEquals(1, this.bag.getOccurrencesOf(new String("key1")));

		this.bag.add(new String("key2"));
		Assert.assertEquals(1, this.bag.getOccurrencesOf(new String("key1")));
		Assert.assertEquals(1, this.bag.getOccurrencesOf(new String("key2")));

		this.bag.add(new String("key1"));
		Assert.assertEquals(2, this.bag.getOccurrencesOf(new String("key1")));
		Assert.assertEquals(1, this.bag.getOccurrencesOf(new String("key2")));
	}

	public void testAddWithOccurrences() {
		Assert.assertEquals(0, this.bag.getOccurrencesOf(new Integer(1)));

		this.bag.add(new Integer(1), 101);
		Assert.assertEquals(101, this.bag.getOccurrencesOf(new Integer(1)));

		this.bag.add(new Integer(2), 1672);
		Assert.assertEquals(101, this.bag.getOccurrencesOf(new Integer(1)));
		Assert.assertEquals(1672, this.bag.getOccurrencesOf(new Integer(2)));

		this.bag.add(new Integer(1), 89);
		Assert
				.assertEquals(101 + 89, this.bag
						.getOccurrencesOf(new Integer(1)));
		Assert.assertEquals(1672, this.bag.getOccurrencesOf(new Integer(2)));

	}

	public void testEqual() {
		Assert.assertEquals(new String("1"), new String("1"));
		Assert.assertFalse(new String("1") == new String("1"));
	}
}
