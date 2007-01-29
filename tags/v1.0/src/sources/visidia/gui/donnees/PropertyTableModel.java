package visidia.gui.donnees;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

/**
 * Class that maps properties (key, value) entries into
 * a double column table model.
 */ 
public class PropertyTableModel extends AbstractPropertyTableModel {
    protected Hashtable defProps = null;
    
    
    /**
     * Constructs new empty property table model.
     */
    public PropertyTableModel(){
        this(null);
    }

    /**
     * Constructs new property table model from <code>props</code>.
     */
    public PropertyTableModel(Hashtable props){
        if(props == null){
            props = new Hashtable();
        }
        defProps = props;
        keys = new Vector(props.keySet());
    }

    public void setProperties(Hashtable props){
	defProps = props;
	keys = new Vector(props.keySet());
	fireTableDataChanged();
    }
    
    public void putProperty(String key, Object value){
	if (!keys.contains(key)) keys.add(key);
	defProps.put(key,value);
	fireTableDataChanged();
    }

    public void removeProperty(int row) {
        Object key = getValueAt(row,0);

        defProps.remove(key);
        keys.remove(row);

        fireTableDataChanged();
    }


    public Hashtable getProperties(){
        return (Hashtable) defProps.clone();
    }
    
    public Object getValueAt(int row, int col){
        switch(col){
        case 0: return keys.elementAt(row);
        case 1: return getTypeName(defProps.get(keys.elementAt(row)));
        case 2: return defProps.get(keys.elementAt(row));
        }
        throw new IllegalArgumentException();	
    }
    
    /**
     * Only value column cell are editable.
     */
    public boolean isCellEditable(int row, int col){
        
        Object obj = getValueAt(row,col);

        return ((col == 2) && (!keys.elementAt(row).equals("label"))
                               && (( obj instanceof String)
                                   || ( obj instanceof Integer)
                                   || ( obj instanceof Double)
                                   || ( obj instanceof Float)
                                   || ( obj instanceof Boolean)
                                   || ( obj instanceof Long)
                                   || ( obj instanceof Short)
                                   || ( obj instanceof Byte) 
                                   || ( obj instanceof Character)));
    }
    
    /**
     * Sets row value to <code>aValue</code>.
     */ 
    public void setValueAt(Object aValue, int row, int col){
        if(!( row < defProps.size() ) && ( col == 2)){
            throw new IllegalArgumentException();
        }

        String value = (String) aValue;
	
        Object obj = getValueAt(row,col);
        
        try{
            
            if (obj instanceof String)
                defProps.put(keys.elementAt(row),value);
            else if(obj instanceof Integer) {
                defProps.put(keys.elementAt(row),Integer.decode(value));
            }
            else if(obj instanceof  Byte)
                defProps.put(keys.elementAt(row),Byte.decode(value));
            else if(obj instanceof  Character)
                defProps.put(keys.elementAt(row), value.charAt(0));
            else if(obj instanceof  Double)
                defProps.put(keys.elementAt(row), Double.parseDouble(value));
            else if(obj instanceof  Float)
                defProps.put(keys.elementAt(row), Float.parseFloat(value));
            else if(obj instanceof Long)
                defProps.put(keys.elementAt(row), Long.parseLong(value));
            else if(obj instanceof  Short)
                defProps.put(keys.elementAt(row), Short.parseShort(value));
            else if(obj instanceof Boolean)
                defProps.put(keys.elementAt(row), Boolean.parseBoolean(value));
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,
                                          e.getMessage(), 
                                          "Warning",
                                          JOptionPane.WARNING_MESSAGE); 
        }
        
        fireTableCellUpdated(row,col);

    }
}
