package visidia.tools;

import javax.swing.*;
import visidia.gui.donnees.*;



/**
 * lancer cette classe directement avec java et elle vous donnera des
 * informations sur votre système
 *
 */
public class PropertyEditor extends JTable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -8269361080781297274L;

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
