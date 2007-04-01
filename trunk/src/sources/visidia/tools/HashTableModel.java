package visidia.tools;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class HashTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9162551228717994603L;

	protected Map<Object,Object> table = null;

	protected List<Object> keys = null;

	public HashTableModel(Map<Object,Object> table) {
		this.setProperties(table);
	}

	public void setProperties(Map<Object,Object>  table) {

		if (table == null) {
			table = new Hashtable<Object,Object>();
		}

		this.table = table;
		this.keys = new Vector<Object>(table.keySet());
		this.fireTableDataChanged();
	}

	public int getRowCount() {
		return this.keys.size();
	}

	public int getColumnCount() {
		return 2;
	}

	public Object getValueAt(int row, int col) {
		switch (col) {

		case 0:
			return this.keys.get(row);
		case 1:
			return this.table.get(this.keys.get(row));

		}
		throw new IllegalArgumentException();
	}

	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "Keys";
		case 1:
			return "Values";
		}
		throw new IllegalArgumentException();
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

}
