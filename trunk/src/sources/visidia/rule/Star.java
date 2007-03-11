package visidia.rule;

import java.io.Serializable;
import java.util.Iterator;

/**
 * 
 */
public class Star implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7349762522537418113L;

	protected String centerState;

	protected MyVector neighbourhood;

	static String unknown = "UNKNOWN";

	/**
	 * default constructor. default centerState is "UNKNOWN"
	 */
	public Star() {
		this.centerState = Star.unknown;
		this.neighbourhood = new MyVector();

	}

	/**
	 * constructor of a star clone of an other.
	 * 
	 * @param s
	 *            a Star.
	 */
	public Star(Star s) {
		this();
		int i;
		int arity = s.arity();
		this.centerState = new String(s.centerState);
		this.neighbourhood = new MyVector(arity);
		for (i = 0; i < arity; i++) {
			this.neighbourhood.add((s.neighbour(i)).clone());
		}

	}

	/**
	 * 
	 * @param centerState
	 *            the label of the center.
	 */
	public Star(String centerState) {
		this.centerState = centerState;
		this.neighbourhood = new MyVector();
	}

	/**
	 * create a Star. wich Neighbours doors are numerated from 0 to arity -1.
	 * 
	 * @param centerState
	 *            the label of the center.
	 * @param arity
	 *            the arity of the star.
	 */
	public Star(String centerState, int arity) {
		int i;
		this.centerState = centerState;
		this.neighbourhood = new MyVector(arity);
		for (i = 0; i < arity; i++) {

			this.neighbourhood.add(new Neighbour(i));
		}
	}

	/**
	 * create a Star. wich Neighbours doors are numerated from 0 to arity -1.
	 * center state is "UNKNOWN"
	 * 
	 * @param arity
	 *            the arity of the star.
	 */
	public Star(int arity) {
		int i;
		this.centerState = Star.unknown;
		this.neighbourhood = new MyVector(arity);
		for (i = 0; i < arity; i++) {

			this.neighbourhood.add(new Neighbour(i));
		}
	}

	public String toString() {
		return "\n<Star>" + this.centerState + "," + this.arity()
				+ " Neighbours:" + this.neighbourhood.toString()
				+ "\n<End Star>";
	}

	/* accessors */

	public void setCenterState(String state) {
		this.centerState = new String(state);
	}

	/**
	 * 
	 * @return the state of the center.
	 */
	public String centerState() {
		return new String(this.centerState);
	}

	/**
	 * 
	 * @param i
	 *            the position
	 * @return the Neighbour on the position i.
	 */
	public Neighbour neighbour(int i) {
		return (Neighbour) this.neighbourhood.get(i);
	}

	/**
	 * 
	 * @param i
	 *            a position.
	 * @return the number of the door of the neighbour on the position i.
	 */
	public int neighbourDoor(int i) {
		return (((Neighbour) this.neighbourhood.get(i))).doorNum();
	}

	/**
	 * add a the Neighbour v to the neighbourhood. the Neighbour is added at the
	 * end of the vector.
	 * 
	 * @param v
	 *            a new Neighbour
	 */
	public void addNeighbour(Neighbour v) {
		this.neighbourhood.add(v);
	}

	/**
	 * remove from neighbourhood the neighbour at position i.
	 * 
	 * @param i
	 *            a position.
	 */
	public void removeNeighbour(int i) {
		this.neighbourhood.remove(i);
	}

	/**
	 * remove all elements from neighbourhood.
	 */
	public void removeAll() {
		this.neighbourhood.clear();
	}

	/**
	 * sets the Neighbour n at the position i in the neighbourhood.
	 * 
	 * @param position
	 *            a position in neighbourhood.
	 * @param n
	 *            a Neighbour.
	 */
	public void setState(int position, Neighbour n) {
		this.neighbourhood.setElementAt(n, position);
	}

	/**
	 * 
	 * @param s2
	 *            star
	 * @return true if centers are equals, false otherwoise.
	 */
	public boolean sameCentState(Star s2) {
		return this.centerState.equals(s2.centerState());
	}

	/**
	 * 
	 * @return the arity of the star.
	 */
	public int arity() {
		return (this.neighbourhood.count());
	}

	/**
	 * 
	 * @return the neighbourhood.
	 */
	public MyVector neighbourhood() {
		return this.neighbourhood;
	}

	/**
	 * sets the door numbers of the star, with the value of door numbers of
	 * those at the same position in the star b.
	 * 
	 * @param b
	 *            a star with the same arity.
	 */
	public void setDoors(Star b) {
		int i;
		for (i = 0; i < b.arity(); i++) {
			this.neighbour(i).setDoorNum(b.neighbour(i).doorNum());
		}
	}

	/**
	 * sets states of the star elements (center and neighbours), with the value
	 * of states of those at the same position in the star b.
	 * 
	 * @param b
	 *            a star with the same arity.
	 */
	public void setStates(Star b) {
		int i;
		this.setCenterState(b.centerState());
		for (i = 0; i < b.arity(); i++) {
			for (int k = 0; k < this.arity(); k++) {
				if (b.neighbour(i).doorNum() == this.neighbour(k).doorNum()) {
					this.setState(k, b.neighbour(i));
				}
			}
		}
	}

	/* comparators, they use random access */
	/**
	 * looks in the star for a Neighbour equals to the Neighbour nei. ATT!!! it
	 * also sets the door number of nei with the door number of the element if
	 * found! * the operation looking for is Randomized
	 * 
	 * @param nei
	 * @return the index of the element if found. -1 otherwoise.
	 */
	public int contains(Neighbour nei) {
		int i = this.neighbourhood.indexOf(nei);
		if (i > -1) {
			nei.setDoorNum(((Neighbour) this.neighbourhood.get(i)).doorNum());
		}

		return i;
	}

	/**
	 * looks in the star for a Neighbour with the same label of the Neighbour
	 * nei. the operation "looking for" is Randomized
	 * 
	 * @param nei
	 * @return the index of the element if found. -1 otherwoise.
	 */
	public int containsLabel(Neighbour nei) {
		int i = this.neighbourhood.indexOfLabel(nei);
		if (i > -1) {
		}
		return i;
	}

	/* we don't begin with the first neighbour, but we choose anyone */
	/**
	 * warning: this method sets doors of context by those of corresponding
	 * elements in the star. so always use a copy of the context while using
	 * this methode. the sens of equality is defined in the class Neighbour
	 * 
	 * @param context
	 *            a context
	 * @return true it's to identify the context with a part of teh Star.
	 */
	public boolean contains(Star context) {
		int k = 0;
		int i = -1;
		if (this.sameCentState(context) == false) {
			return false;
		} else {
			Iterator it = context.neighbourhood.randIterator();
			while (it.hasNext()) {
				Neighbour n = (Neighbour) it.next();
				i = this.contains(n);
				if (i < 0) {
					return false;
				} else {
					int door = ((Neighbour) this.neighbourhood.get(i))
							.doorNum();
					n.setDoorNum(door);
					this.removeNeighbour(i);

				}
				k++;
			}

			return true;
		}
	}

	/**
	 * 
	 * @param context
	 *            a context
	 * @return true it's to identify the context with a part of teh Star. the
	 *         identification concerne only labels.
	 */
	public boolean containsLabels(Star star) {
		int k = 0;
		int i = -1;
		while (k < star.neighbourhood.count()) {
			i = this.containsLabel(star.neighbour(k));
			if (i < 0) {
				// System.out.println("<0 ? ="+i+" (k="+k+")");
				return false;
			} else {
				this.removeNeighbour(i);
				// System.out.println("i="+i+"k"+k+"door
				// "+door+"=?"+star.neighbour(k));
			}
			k++;
		}
		return true;
	}

	public Object clone() {
		Star s = new Star(this);
		return s;
	}

}
