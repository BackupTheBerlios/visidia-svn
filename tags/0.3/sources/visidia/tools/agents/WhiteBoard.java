/**
 * A white board  stores properties under names like  a Hashtable. The
 * difference with the hashtable is  given by default values which are
 * not stored to save space. 
 */

package visidia.tools.agents;

import java.util.Hashtable;
import java.util.Set;
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
        this(defaults,new Hashtable());
    }


    /**
     * Construct a new WhiteBoard with default and specifics values. 
     *
     * @param def  Default values  that will be  used when  nobody has
     * modified them.
     * @param properties Specifics values that will be used for this
     * Whiteboard.
     */
    public WhiteBoard(Hashtable def, Hashtable properties ) {
        this.defaults = def;
        this.values = properties;
    }

    /**
     * Return the value  associated with the key. If  the key can't be
     * found, throw NoSuchElementException.
     */
    public Object getValue(Object key) {
        if (values.containsKey(key))
            return values.get(key);
        else if (defaults.containsKey(key))
            return defaults.get(key);
        else
            throw new NoSuchElementException();
    }

    /**
     * Insert a value and the corresponding key.
     */
    public void setValue(Object key, Object value) {
        values.put(key, value);
        System.out.println("Values " + values);
        System.out.println("Defaults : " + defaults);
    }
    
    public Set keys() {
        Hashtable common = (Hashtable)defaults.clone();
        common.putAll(values);

        return common.keySet();
    }

}
