package visidia.tools.agents;

import java.util.Hashtable;
import java.util.Set;
import java.util.NoSuchElementException;

/**
 * A white board  stores properties under names like  a Hashtable. The
 * difference with the hashtable is  given by default values which are
 * not stored to save space. 
 */
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
     * Constructs a new WhiteBoard with default values. 
     *
     * @param defaults  Default values that  will be used  when nobody
     * has modified them. 
     */
    public WhiteBoard(Hashtable defaults) {
        this(defaults,new Hashtable());
    }


    /**
     * Constructs a new WhiteBoard with default and specifics values. 
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
     * Returns the value associated with  the key. If the key can't be
     * found, throws NoSuchElementException.
     *
     * @throws  NoSuchElementException  Thrown  when  <code>key</code>
     * doesn't exist in the white board.
     */
    public Object getValue(Object key) {
        if (this.values.containsKey(key))
            return this.values.get(key);
        else if (this.defaults.containsKey(key))
            return this.defaults.get(key);
        else
            throw new NoSuchElementException();
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
        Hashtable common = (Hashtable)this.defaults.clone();
        common.putAll(this.values);

        return common.keySet();
    }

}
