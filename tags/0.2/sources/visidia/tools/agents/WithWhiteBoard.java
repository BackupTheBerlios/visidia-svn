/**
 * Implement this interface  if you want to provide  a WhiteBoard with
 * you instances.
 */

package visidia.tools.agents;

public interface WithWhiteBoard {

    /**
     * Return the value  associated with the key. If  the key can't be
     * found, throw NoSuchElementException.
     */
    public Object getProperty(Object key);

    /**
     * Insert a value and the corresponding key.
     */
    public void setProperty(Object key, Object value);

}
