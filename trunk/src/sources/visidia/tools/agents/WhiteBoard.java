/**
 * A white board  stores properties under names like  a Hashtable. The
 * difference with the hashtable is  given by default values which are
 * not stored to save space. 
 */

package visidia.tools.agents;

import java.util.Hashtable;
import java.util.NoSuchElementException;

public class WhiteBoard {

    private Hashtable values;
    private final Hashtable defaults;

    /**
     * Using this  constructor, there will  be no default  values. The
     * WhiteBoard will be used just like a Hashtable.
     */
    public WhiteBoard() {
        this(new Hashtable());
    }

    /**
     * Construct a new WhiteBoard with default values. 
     *
     * @param defaults  Default values that  will be used  when nobody
     * has modified them. 
     */
    public WhiteBoard(Hashtable defaults) {
        this.defaults = defaults;
        this.values = new Hashtable();
    }

    public Object getValue(Object key) {
        if (values.containsKey(key))
            return values.get(key);
        else if (defaults.containsKey(key))
            return defaults.get(key);
        else
            throw new NoSuchElementException();
    }

    public void setValue(Object key, Object value) {
        values.put(key, value);
    }
    
}
