package visidia.tools.agents;

import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A white board stores properties under names like a Hashtable. The difference
 * with the hashtable is given by default values which are not stored to save
 * space.
 */
public class WhiteBoard {

	private Hashtable<Object, Object> values;

	private final Hashtable<Object, Object> defaults;

	/**
	 * Using this constructor, there will be no default values. The WhiteBoard
	 * will be used just like a Hashtable.
	 */
	public WhiteBoard() {
		this(new Hashtable<Object, Object>());
	}

	/**
	 * Constructs a new WhiteBoard with default values.
	 * 
	 * @param defaults
	 *            Default values that will be used when nobody has modified
	 *            them.
	 */
	public WhiteBoard(Hashtable<Object, Object> defaults) {
		this(defaults, new Hashtable<Object, Object>());
	}

	/**
	 * Constructs a new WhiteBoard with default and specifics values.
	 * 
	 * @param def
	 *            Default values that will be used when nobody has modified
	 *            them.
	 * @param properties
	 *            Specifics values that will be used for this Whiteboard.
	 */
	public WhiteBoard(Hashtable<Object, Object> def,
			Hashtable<Object, Object> properties) {
		this.defaults = def;
		this.values = properties;
	}

	/**
	 * Returns the value associated with the key. If the key can't be found,
	 * throws NoSuchElementException.
	 * 
	 * @throws NoSuchElementException
	 *             Thrown when <code>key</code> doesn't exist in the white
	 *             board.
	 */
	public Object getValue(Object key) {
		if (this.values.containsKey(key)) {
			return this.values.get(key);
		} else if (this.defaults.containsKey(key)) {
			return this.defaults.get(key);
		} else {
			throw new NoSuchElementException();
		}
	}

	/**
	 * Inserts a value and the corresponding key.
	 */
	public void setValue(Object key, Object value) {
		this.values.put(key, value);
	}

	public void removeValue(Object key) {
		this.values.remove(key);
	}

	public Set keys() {
		Hashtable<Object, Object> common = (Hashtable<Object, Object>) this.defaults
				.clone();
		common.putAll(this.values);

		return common.keySet();
	}

	
	
	public boolean containsElement(Object key){
		if (this.values.containsKey(key)) {
			return true;
		} else if (this.defaults.containsKey(key)) {
			return true;
		} else  return false;	
	}
	
}
