package visidia.tools;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import visidia.gui.donnees.*;

/**
 *
 */
public class PropertyEditor extends JTable {
	
    public static void main(String[] args){
    	PropertyTableModel model = new PropertyTableModel(System.getProperties());
    	JTable table = new JTable();
    	table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	JScrollPane spane = new JScrollPane(table);
    	JFrame frame = new JFrame("System Properties");
    	frame.getContentPane().add(spane);
    	frame.pack();
    	frame.setVisible(true);
	frame.addWindowListener(new java.awt.event.WindowAdapter() {
		public void windowClosing(java.awt.event.WindowEvent e) {
			System.exit(0);
		};
	});
    }
}
