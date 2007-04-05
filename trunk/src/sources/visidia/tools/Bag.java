package visidia.tools;

import java.util.Hashtable;
import java.util.Set;
import visidia.simulation.agents.Agent;
import visidia.simulation.agents.stats.AgentCreationStat;
import visidia.simulation.agents.stats.MaxNbAgent;
import visidia.simulation.agents.stats.MemoryAverageSize;
import visidia.simulation.agents.stats.MemorySizeMax;
import visidia.simulation.agents.stats.MemorySizeMin;
import visidia.simulation.agents.stats.MemorySizeSum;
import visidia.simulation.agents.stats.MoveMaxStat;
import visidia.simulation.agents.stats.MoveStat;
import visidia.simulation.agents.stats.TerminatedStat;

public class Bag {

	private Hashtable<Object, Long> table;

	public Bag() {
		this.table = new Hashtable<Object, Long>();
	}

	public long getOccurrencesOf(Object o) {
		Long occurrences = this.table.get(o);

		if (occurrences == null) {
			return 0;
		} else {
			return occurrences.intValue();
		}
	}

	/**
	 * Add the current value of the Bag corresponding to the objet 'o'
	 * with the value 'occurences'
	 */
	public void add(Object o, long occurrences) {
		long newOccurrences = this.getOccurrencesOf(o) + occurrences;

		this.table.put(o, new Long(newOccurrences));
	}

	/**
	 * Increments the current value of the Bag corresponding to the objet 'o'
	 */
	public void add(Object o) {
		this.add(o, 1);
	}

	/**
	 * Compare the current value of the Bag corresponding to the objet 'o'
	 * with the value 'occurences' and replace the value of the Bag with
	 * the minimum of both values
	 */
	public void min(Object o, long occurences){
		if (occurences < this.getOccurrencesOf(o)){
			this.table.put(o, new Long(occurences));
		}	
	}

	/**
	 * Compare the current value of the Bag corresponding to the objet 'o'
	 * with the value 'occurences' and replace the value of the Bag with
	 * the maximum of both values
	 */
	public void max(Object o, long occurences){
		if (occurences > this.getOccurrencesOf(o)){
			this.table.put(o, new Long(occurences));
		}	
	}

	/**
	 * Replace the current value of the Bag corresponding to the objet 'o'
	 * with the value 'occurences'
	 */
	public void replace (Object o, long occurences){
		this.table.put(o, new Long(occurences));
	}


	public Hashtable asHashTable() {
		return this.table;
	}

	public Set<Object> keySet() {
		return this.table.keySet();
	}

}
