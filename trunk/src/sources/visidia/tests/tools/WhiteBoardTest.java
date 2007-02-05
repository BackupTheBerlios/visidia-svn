/**
 * Test WhiteBoards found in visidia.tools
 */

package visidia.tests.tools;

import visidia.tools.agents.WhiteBoard;

import java.util.Hashtable;
import java.util.NoSuchElementException;

public class WhiteBoardTest extends junit.framework.TestCase {
    
    WhiteBoard empty, wb1;
    String key1, value2;
    Integer key2, value1;
    
    /**
     * Create  two whiteboards, one  empty and  one with  two defaults
     * values.
     */
    protected void setUp() {
        Hashtable hash = new Hashtable();

        empty = new WhiteBoard();

        key1 = "key1";
        key2 = new Integer(2);

        value1 = new Integer(1);
        value2 = "2";

        hash.put(key1, value1);
        hash.put(key2, value2);

        wb1 = new WhiteBoard(hash);
    }

    /**
     * Nothing modified, WhiteBoards must returns default values.
     */
    public void testGetDefaults() {
        assertEquals(new Integer(1), wb1.getValue("key1"));
        assertEquals("2", wb1.getValue(new Integer(2)));
    }

    /**
     * Invalid keys must throw NoSuchElementException.
     */
    public void testGetInvalidKey() {
        boolean ok = false;

        try {
            empty.getValue("my key");
        } catch (NoSuchElementException e) {
            ok = true;
        }

        assertTrue("Previous getValue() should have raised an Exception", ok);
        
        ok = false;

        try {
            wb1.getValue("unknown key");
        } catch (NoSuchElementException e) {
            ok = true;
        }

        assertTrue("Previous getValue() should have raised an Exception", ok);
    }

    /**
     * Try to  override a default value and  verify WhiteBoard returns
     * the new value.
     */
    public void testOverrideDefault() {
        assertEquals(new Integer(1), wb1.getValue("key1"));
        wb1.setValue("key1", new Integer(3));
        assertEquals(new Integer(3), wb1.getValue("key1"));
    }

    /**
     * Write two values under the same key. A getValue() should return
     * the last one.
     */
    public void testOverride() {
        wb1.setValue("key1", new Integer(3));
        assertEquals(new Integer(3), wb1.getValue("key1"));
        wb1.setValue("key1", new Integer(6));
        assertEquals(new Integer(6), wb1.getValue("key1"));
    }

    /**
     * Default values  are used to  keep space in  memory. WhiteBoards
     * MUST NOT copy this default values ; instead, they have to store
     * the provided Hashtable (not copying it).
     */
    public void testDoNotCopyDefaults() {
        assertTrue("Should not copy values (must keep memory space)",
                   wb1.getValue(key1) == value1);
        assertTrue("Should not copy values (must keep memory space)",
                   wb1.getValue(key2) == value2);
    }
}
