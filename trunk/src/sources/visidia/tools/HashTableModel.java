package visidia.tools;

import java.util.Map;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class HashTableModel extends AbstractTableModel {

    protected Map table = null;
    protected List keys = null;

    public HashTableModel(Map table){
        setProperties(table);
    }

    public void setProperties(Map table){

        if(table == null)
            table = new Hashtable();

        this.table = table;
        this.keys = new Vector(table.keySet());
	fireTableDataChanged();
    }

    public int getRowCount(){
        return keys.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int row, int col){
        switch(col){

        case 0: return keys.get(row);
        case 1: return table.get(keys.get(row));
        
        }
        throw new IllegalArgumentException();	
    }

    public String getColumnName(int col) {
	switch (col){
	case 0: return "Keys";
	case 1: return "Values";
	}
	throw new IllegalArgumentException();
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

}
