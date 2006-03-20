package visidia.gui.donnees;

/**
 * Class that maps properties (key, value) entries into
 * a double column table model.
 */ 
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

public class PropertyTableModel extends AbstractTableModel {
    protected Hashtable properties = null;
    protected Vector keys = null;
    
    
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
        properties = props;
        keys = new Vector(props.keySet());
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

    public Hashtable getProperties(){
        return (Hashtable) properties.clone();
    }

    public Class getColumnClass(int col){
        return String.class;
    }
    
    public int getColumnCount(){
        return 2;
    }
    
    public int getRowCount(){
        return keys.size();
    }
    
    public Object getValueAt(int row, int col){
        switch(col){
        case 0: return keys.elementAt(row);
        case 1: return properties.get(keys.elementAt(row));
        }
        throw new IllegalArgumentException();	
    }
    
    /**
     * Only value column cell are editable.
     */
    public boolean isCellEditable(int row, int col){
        //xav	
        // B  	 byte
        // C 	char
        // D 	double
        // F 	float
        // I 	int
        // J 	long
        // Lclassname; 	classe ou interface
        // S 	short
        // Z 	boolean

        Class cellClass = getValueAt(row,col).getClass();

        System.out.println("isCellEditable :" + cellClass.getName());

        //xav	return ((col == 1) && (!keys.elementAt(row).equals("label")));
	return ((col == 1) && (!keys.elementAt(row).equals("label"))
                               && (( cellClass == java.lang.String.class)
                                   || ( cellClass == java.lang.Integer.class)
                                   || ( cellClass.getName() == "B")
                                   || ( cellClass.getName() == "C")
                                   || ( cellClass.getName() == "D")
                                   || ( cellClass.getName() == "F")
                                   || ( cellClass.getName() == "I")
                                   || ( cellClass.getName() == "J")
                                   || ( cellClass.getName() == "S")
                                   || ( cellClass.getName() == "Z")));

    }

    public String getColumnName(int col){
        switch(col){
        case 0: return "name";
        case 1: return "value";
        }
        throw new IllegalArgumentException();	
    }
    
    /**
     * Sets row value to <code>aValue</code>.
     */ 
    public void setValueAt(Object aValue, int row, int col){
        if(!( row < properties.size() ) && ( col == 1)){
            throw new IllegalArgumentException();
        }
	
        properties.put(keys.elementAt(row), aValue);
        fireTableCellUpdated(row,col);
    }
}
