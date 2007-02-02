package visidia.gui.donnees;

//import java.awt.*;
import javax.swing.*;
//import javax.swing.table.*;
import java.util.*;

import visidia.tools.agents.WhiteBoard;


/**
 * Class that maps properties (key, value) entries into
 * a double column table model.
 */ 
public class AgentPropertyTableModel extends AbstractPropertyTableModel {
    protected WhiteBoard wb = null;
    
    /**
     * Constructs new empty property table model.
     */
    public AgentPropertyTableModel(){
        this(null);
    }

    /**
     * Constructs new property table model from <code>props</code>.
     */
    public AgentPropertyTableModel(WhiteBoard whiteboard){
        
        wb = whiteboard;
        
        keys = new Vector(wb.keys());
    }

    public void putProperty(String key, Object value){
	if (!keys.contains(key)) keys.add(key);
	wb.setValue(key,value);
	fireTableDataChanged();
    }

    public void removeProperty(int row) {
        Object key = keys.elementAt(row);

        keys.remove(row);
        wb.removeValue(key);

        fireTableDataChanged();
    }


    public Object getValueAt(int row, int col){
        switch(col){
        case 0: return keys.elementAt(row);
        case 1: return getTypeName(wb.getValue(keys.elementAt(row)));
        case 2: return wb.getValue(keys.elementAt(row));
        }
        throw new IllegalArgumentException();	
    }
    

    /**
     * Sets row value to <code>aValue</code>.
     */ 
    public void setValueAt(Object aValue, int row, int col){
        if(!( row < keys.size() ) && ( col == valueColumn)){
            throw new IllegalArgumentException();
        }

        String value = (String) aValue;
	
        Object obj = getValueAt(row,col);
        
        try{
            
            if (obj instanceof String)
                wb.setValue(keys.elementAt(row),value);
            else if(obj instanceof Integer) {
                wb.setValue(keys.elementAt(row),Integer.decode(value));
            }
            else if(obj instanceof  Byte)
                wb.setValue(keys.elementAt(row),Byte.decode(value));
            else if(obj instanceof  Character)
                wb.setValue(keys.elementAt(row), value.charAt(0));
            else if(obj instanceof  Double)
                wb.setValue(keys.elementAt(row), Double.parseDouble(value));
            else if(obj instanceof  Float)
                wb.setValue(keys.elementAt(row), Float.parseFloat(value));
            else if(obj instanceof Long)
                wb.setValue(keys.elementAt(row), Long.parseLong(value));
            else if(obj instanceof  Short)
                wb.setValue(keys.elementAt(row), Short.parseShort(value));
            else if(obj instanceof Boolean)
                wb.setValue(keys.elementAt(row), Boolean.parseBoolean(value));
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
