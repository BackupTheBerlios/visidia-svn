package visidia.tools.agents;

import visidia.gui.donnees.AgentPropertyTableModel;

public class UpdateTableAgent extends UpdateTable {

    public UpdateTableAgent(AgentPropertyTableModel table, long sleepTime) {
        super(table,sleepTime);
    }

    public UpdateTableAgent(AgentPropertyTableModel table) {
        this(table, 1000);
    }

    public void run() {
        while (! stop) {
            try {
                synchronized (this) {
                    wait(1000);
                }
            } catch (InterruptedException e) {
                stop();
            }
            table.fireTableDataChanged();
        }
    }

}
