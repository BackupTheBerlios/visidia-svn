package visidia.gui.donnees;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

/**
 * Class that maps properties (key, value) entries into
 * a double column table model.
 */ 
public class AgentPropertyTableModel extends PropertyTableModel {
    protected Hashtable properties = null;
    protected Hashtable hashKeys = null;
    
    /**
     * Constructs new empty property table model.
     */
    public AgentPropertyTableModel(){
        this(null,null);
    }

    /**
     * Constructs new property table model from <code>props</code>.
     */
    public AgentPropertyTableModel(Hashtable props, Hashtable def){
        if(props == null){
            props = new Hashtable();
        }

        properties = props;
        defProps = def;

        hashKeys = (Hashtable) def.clone();
        hashKeys.putAll(props);

        keys = new Vector(hashKeys.keySet());        

    }

    public void setProperties(Hashtable props){
	properties = props;
	keys = new Vector(props.keySet());
	fireTableDataChanged();
    }
    
    public void putProperty(String key, Object value){
	if (!keys.contains(key)) keys.add(key);
	properties.put(key,value);
	fireTableDataChanged();
    }

    public void removeProperty(int row) {
        Object key = getValueAt(row,0);

        properties.remove(key);
        keys.remove(row);
        hashKeys.remove(key);

        fireTableDataChanged();
    }

    public Hashtable getProperties(){
        return (Hashtable) hashKeys.clone();
    }
    
    public Object getValueAt(int row, int col){
        Object key;

        switch(col){
        case 0: return keys.elementAt(row);
        case 1: key = keys.elementAt(row);
            
            if (properties.containsKey(key))
                return properties.get(key);
            else if (defProps.containsKey(key))
                return defProps.get(key);
            
        }
        throw new IllegalArgumentException();	
    }
    
     /**
     * Sets row value to <code>aValue</code>.
     */ 
    public void setValueAt(Object aValue, int row, int col){

        String value=(String) aValue;

        if(!( row < properties.size() + defProps.size() ) && ( col == 1)){
            throw new IllegalArgumentException();
        }
	
        Object obj = getValueAt(row,col);


        try{
            
            if (obj instanceof String)
                properties.put(keys.elementAt(row),value);
            else if(obj instanceof Integer) {
                properties.put(keys.elementAt(row),Integer.decode(value));
            }
            else if(obj instanceof  Byte)
                properties.put(keys.elementAt(row),Byte.decode(value));
            else if(obj instanceof  Character)
                properties.put(keys.elementAt(row), value.charAt(0));
            else if(obj instanceof  Double)
                properties.put(keys.elementAt(row), Double.parseDouble(value));
            else if(obj instanceof  Float)
                properties.put(keys.elementAt(row), Float.parseFloat(value));
            else if(obj instanceof Long)
                properties.put(keys.elementAt(row), Long.parseLong(value));
            else if(obj instanceof  Short)
                properties.put(keys.elementAt(row), Short.parseShort(value));
            else if(obj instanceof Boolean)
                properties.put(keys.elementAt(row), Boolean.parseBoolean(value));
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,
                                          e.getMessage(), 
                                          "Warning",
                                          JOptionPane.WARNING_MESSAGE); 
        }
        
        fireTableCellUpdated(row,col);
    }


    public void updateKeys() {
        hashKeys.putAll(properties);
        
        keys = new Vector(hashKeys.keySet());        
    }

}
