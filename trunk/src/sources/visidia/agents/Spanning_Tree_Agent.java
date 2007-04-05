package visidia.agents;

import java.util.NoSuchElementException;

import visidia.simulation.agents.Agent;
import java.util.LinkedList;
import java.util.Collection;

/**
 * Implements  a spanning  tree  algorithm with  an  agent. This  agent
 * doesn't use unique identifier of vertices.
 *
 * @see Spanning_Tree_Agent_WithId
 */
public abstract class Spanning_Tree_Agent extends Agent {

	abstract public void init();

	protected void setVertexIdTree(Integer idTree) {
		this.setVertexProperty("IdTree", idTree);

		String s;

		switch (idTree.intValue()) {
		case 0:
			s = "A";
			break;
		case 1:
			s = "B";
			break;
		case 2:
			s = "C";
			break;
		case 3:
			s = "D";
			break;
		case 4:
			s = "E";
			break;
		case 5:
			s = "F";
			break;
		case 6:
			s = "G";
			break;
		case 7:
			s = "H";
			break;
		case 8:
			s = "I";
			break;
		case 9:
			s = "J";
			break;
		case 10:
			s = "K";
			break;
		default:
			s = null;
		}

		if (s != null)
			this.setVertexProperty("label", s);

	}

	/**
	 * Return the IdTree of the Vertex (its color)
	 * @return idTree of the Vertex
	 */
	protected Integer getVertexIdTree() {
		Integer idOfTheTree;

		try {
			idOfTheTree = (Integer) this.getVertexProperty("IdTree");
		} catch (NoSuchElementException e) {
			idOfTheTree = null;
		}

		return idOfTheTree;
	}

	protected boolean isVertexBelongToTheTree(Integer idTree) {
		return idTree.equals(this.getVertexIdTree());
	}

	protected boolean isRootOfTheTree(Integer idTree) {
		return this.getVertexParent(idTree) == null;
	}

	protected void setVertexPortToParent(Integer idTree, Integer p) {
		this.setVertexProperty("Tree" + idTree.toString() + "Port"
				+ p.toString(), "Parent");
	}

	protected void setVertexPortToChild(Integer idTree, Integer p) {
		this.setVertexProperty("Tree" + idTree.toString() + "Port"
				+ p.toString(), "Child");
	}

	protected void delVertexPort(Integer idTree, Integer p) {
		this.setVertexProperty("Tree" + idTree.toString() + "Port"
				+ p.toString(), "");
	}

	/**
	 * Return an Iterator of the childs of the tree specified
	 * @param idTree Identification of the tree
	 * @return vertex childs
	 */
	protected Collection<Integer> getVertexChilds(Integer idTree) {

		Collection<Integer> children = new LinkedList<Integer>();

		for (Integer i = 0; i < this.getArity(); i++) {

			try {
				if (((String) this.getVertexProperty("Tree" + idTree.toString()
						+ "Port" + i.toString())).equals("Child")) {
					children.add(new Integer(i));
				}
			} catch (NoSuchElementException e) {
			}
		}

		return children;

	}

	

	protected boolean isPortIsChildInTheTree(Integer idTree, Integer port) {

		try {
			if (((String) this.getVertexProperty("Tree" + idTree.toString()
						+ "Port" + port.toString())).equals("Child")) {
					return true;
				}
		} 
		catch (NoSuchElementException e) {
		}

		return false;

	}

	
	protected boolean isPortIsParentInTheTree(Integer idTree, Integer port) {

		try {
			if (((String) this.getVertexProperty("Tree" + idTree.toString()
						+ "Port" + port.toString())).equals("Parent")) {
					return true;
				}
		} 
		catch (NoSuchElementException e) {
		}

		return false;

	}
	
	
	/**
	 * Return an Iterator of the childs of the tree specified
	 * @param idTree Identification of the tree
	 * @return vertex childs
	 */
	protected boolean isPortMarkedInTheTree(Integer idTree, Integer port) {

		if(getVertexParent(idTree)!=null && getVertexParent(idTree).equals(port)) return true;
		
		for (Integer i = 0; i < this.getArity(); i++) {

			try {
				if (((String) this.getVertexProperty("Tree" + idTree.toString()
						+ "Port" + i.toString())).equals("Child")) {
					if(i.equals(port)) return true;
				}
			} catch (NoSuchElementException e) {
			}
		}

		return false;

	}
	
	
	/**
	 * Returns the parent vertex or null if the vertex haven't a parent (he's root)
	 * @param idTree Identification of the tree
	 * @return vertex parent
	 */
	protected Integer getVertexParent(Integer idTree) {

		for (Integer i = 0; i < this.getArity(); i++) {

			try {
				if (((String) this.getVertexProperty("Tree" + idTree.toString()
						+ "Port" + i.toString())).equals("Parent")) {
					return new Integer(i);
				}
			} catch (NoSuchElementException e) {
			}
		}

		return null;

	}

	protected String getVertexPort(Integer idTree, Integer port) {
		try {
			return (String) this.getVertexProperty("Tree" + idTree.toString()
					+ "Port" + port.toString());
		} catch (NoSuchElementException e) {
			return null;
		}
	}

}
