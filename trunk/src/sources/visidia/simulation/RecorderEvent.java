package visidia.simulation;

import visidia.tools.VQueue;

public class RecorderEvent implements Runnable, Cloneable {

	private VQueue evtIn, evtOut;

	private ObjectWriter writer;

	public RecorderEvent(VQueue evtIn_, VQueue evtOut_, ObjectWriter writer_) {
		this.evtIn = evtIn_;
		this.evtOut = evtOut_;
		this.writer = writer_;
	}

	public void run() {
		while (true) {
			SimulEvent simEvt = null;

			try {
				simEvt = (SimulEvent) this.evtIn.get();
			} catch (ClassCastException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				break;
			}

			this.writer.writeObject(simEvt);

			try {
				this.evtOut.put(simEvt);
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}

		}
	}
}
