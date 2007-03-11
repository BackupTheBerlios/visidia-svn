package visidia.tools.agents;

import java.util.Set;

/**
 * Implement this interface if you want to provide a WhiteBoard with your
 * instances.
 */
public interface WithWhiteBoard {

	/**
	 * Returns the value associated with the key. If the key can't be found,
	 * throws NoSuchElementException.
	 */
	public Object getProperty(Object key);

	/**
	 * Insert a value and the corresponding key.
	 */
	public void setProperty(Object key, Object value);

	/**
	 * Gets all the keys of a Hashtable.
	 */
	public Set getPropertyKeys();
}
