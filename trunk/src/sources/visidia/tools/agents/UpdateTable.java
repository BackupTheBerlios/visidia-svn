package visidia.tools.agents;

import javax.swing.table.AbstractTableModel;

public abstract class UpdateTable implements Runnable {

    AbstractTableModel table;
    long sleepTime;
    boolean stop;

    public UpdateTable(AbstractTableModel table, long sleepTime) {
        this.table = table;
        this.sleepTime = sleepTime;
        this.stop = false;
    }

    public UpdateTable(AbstractTableModel table) {
        this(table, 1000);
    }

    public void stop() {
        this.stop = true;
    }

    abstract public void run();

}
