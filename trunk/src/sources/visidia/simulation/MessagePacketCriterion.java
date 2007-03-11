package visidia.simulation;

import visidia.misc.MessageCriterion;
import visidia.tools.Criterion;

/**
 * MessagePacketCriterion is a massage criterion wrapper that handle a message
 * packet, extract its message before give it to the wrapped message criterion
 * for test. This because each node message queue contains objects of type
 * MessagePacket and not Message.
 */
class MessagePacketCriterion implements Criterion {
	private MessageCriterion mc;

	MessagePacketCriterion(MessageCriterion mc) {
		this.mc = mc;
	}

	MessagePacketCriterion() {
		this(null);
	}

	public boolean isMatchedBy(Object o) {
		if (this.mc == null) {
			return false;
		}

		if (!(o instanceof MessagePacket)) {
			return false;
		}

		MessagePacket mp = (MessagePacket) o;

		return this.mc.isMatchedBy(mp.message());
	}
}
