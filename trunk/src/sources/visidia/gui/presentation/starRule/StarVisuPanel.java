package visidia.gui.presentation.starRule;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import visidia.gui.donnees.TableCouleurs;
import visidia.gui.presentation.AreteDessin;
import visidia.gui.presentation.FormeDessin;
import visidia.gui.presentation.SommetDessin;
import visidia.gui.presentation.VueGraphe;
import visidia.gui.presentation.boite.EtatPanel;
import visidia.gui.presentation.boite.VueEtatPanel;

/**
 * Visualizes and permits to compose a star as a VueGraph. The parent must add
 * the instance to its MouseListener and MouseMotionListener
 */
public class StarVisuPanel implements MouseListener, MouseMotionListener {

	// Used for drag'n drop a vertex
	SommetDessin drag_n_drop_sommet = null;

	Point ancien_pos = null;

	// Used to change vertex label
	JPopupMenu vertexPopup;

	SommetDessin sommetC;

	VueGraphe vg;

	boolean isSimpleRule;

	int vertexNumber;

	int ray;

	Point center;

	JPanel parent;

	/**
	 * The center of the star is located at "center" ; if the star is from a
	 * simple rule, just two vertexes can compose the VueGraphe.
	 */
	public StarVisuPanel(VueGraphe vg, int ray, Point center, JPanel parent,
			boolean isSimpleRule) {
		this.isSimpleRule = isSimpleRule;
		this.vg = vg;
		this.ray = ray;
		this.center = center;
		this.parent = parent;
		this.sommetC = (SommetDessin) vg.en_dessous(center.x, center.y);
		this.vertexPopup = new JPopupMenu();
		this.vertexPopup.setBorder(BorderFactory.createRaisedBevelBorder());
		this.vertexNumber = vg.nbObjets();
		if (this.vertexNumber > 1) {
			// Edges must be deduted
			this.vertexNumber = this.vertexNumber - (this.vertexNumber / 2);
		}
	}

	// Returns SommetDessin s as s.getEtiquette().equals(id)
	private SommetDessin getVertex(String id) {
		for (Enumeration e = this.vg.listeAffichage(); e.hasMoreElements();) {
			FormeDessin f = (FormeDessin) e.nextElement();
			if (f instanceof SommetDessin) {
				if (((SommetDessin) f).getEtiquette().equals(id)) {
					return (SommetDessin) f;
				}
			}
		}
		return null;
	}

	/**
	 * Places the vertexes (not the center one) in a regular way
	 */
	public void reorganizeVertex() {
		if (this.isSimpleRule) {
			SommetDessin s = this.getVertex("1");
			this.sommetC.placer(this.center.x, this.center.y);
			if (s != null) {
				s.placer(this.center.x + this.ray, this.center.y);
			}
		} else {
			double alpha = 0;
			double step = 2 * Math.PI / (this.vertexNumber - 1);
			for (int i = 1; i < this.vertexNumber; i++) {
				SommetDessin s = this.getVertex("" + i);
				int x = this.center.x + (int) (Math.sin(alpha) * this.ray);
				int y = this.center.y - (int) (Math.cos(alpha) * this.ray);
				s.placer(x, y);
				alpha += step;
			}
		}
		this.parent.repaint();
	}

	// Clockwise renumbering
	private void renumeberVertex() {
		double alpha = 0;
		SommetDessin s = null;
		int number = 1;
		this.sommetC.setEtiquette("0");
		for (alpha = 0.0; alpha < 2 * Math.PI - 0.2; alpha += 0.2) {
			int x = this.center.x + (int) (Math.sin(alpha) * this.ray);
			int y = this.center.y - (int) (Math.cos(alpha) * this.ray);
			try {
				SommetDessin s2 = (SommetDessin) this.vg.en_dessous(x, y, s);
				// System.out.println ("Sommet " + s2
				// + " trouve a alpha " + alpha + " " + x + " " + y);
				s2.setEtiquette(Integer.toString(number++));
				s = s2;
			} catch (NoSuchElementException e) {
			}
		}
	}

	// Returns null if (x, y) is not near the circle or
	// returns the nearest point on the circle
	// If isSimpleRule == true, returns the point of the second vertex or null
	private Point getRoundedPosition(int x, int y) {
		if (this.isSimpleRule) {
			if ((x > this.center.x + this.ray - 25)
					&& (x < this.center.x + this.ray + 25)
					&& (y > this.center.y - 50) && (y < this.center.y + 50)) {
				return new Point(this.center.x + this.ray, this.center.y);
			} else {
				return null;
			}
		} else {
			double r2 = Math.sqrt(Math.pow(x - this.center.x, 2)
					+ Math.pow(y - this.center.y, 2));
			if (Math.abs(r2 - this.ray) > 10) {
				return null;
			}
			return new Point(
					(int) (this.center.x + (this.ray * (x - this.center.x))
							/ r2),
					(int) (this.center.y + (this.ray * (y - this.center.y))
							/ r2));
		}
	}

	/**
	 * Left and center buttons change the structure Right button change the
	 * associated data
	 */
	public void mousePressed(MouseEvent evt) {
		int x = evt.getX();
		int y = evt.getY();
		int modifiers = evt.getModifiers();

		// System.out.println (x + " " + y);

		// Left click
		if ((modifiers == InputEvent.BUTTON1_MASK)
				&& (evt.getClickCount() == 2)) {
			Point p = this.getRoundedPosition(x, y);
			// System.out.println (p);

			if (p != null) {
				try { // Delete the vertex
					SommetDessin s = (SommetDessin) this.vg.en_dessous(x, y);
					AreteDessin a = this.vg.rechercherArete(s.getEtiquette(),
							this.sommetC.getEtiquette());
					this.vg.delObject(s);
					this.vg.delObject(a);
					this.vertexNumber--;
				} catch (NoSuchElementException e) { // Add a new vertex
					SommetDessin s = this.vg.creerSommet(p.x, p.y);
					this.vg.creerArete((SommetDessin) this.vg.en_dessous(
							this.center.x, this.center.y), s);
					this.vertexNumber++;
				}
				this.renumeberVertex();
				this.parent.repaint();
			}
		} else if ((modifiers == InputEvent.BUTTON2_MASK)
				|| ((modifiers == (InputEvent.BUTTON1_MASK | InputEvent.ALT_MASK)) && !this.isSimpleRule)) {
			// Center click or left + shift
			try {
				if (this.getRoundedPosition(x, y) != null) {
					this.drag_n_drop_sommet = (SommetDessin) this.vg
							.en_dessous(x, y);
				}
			} catch (NoSuchElementException e) {
				this.drag_n_drop_sommet = null;
				this.ancien_pos = new Point(x, y);
			}
		} else if (modifiers == InputEvent.BUTTON3_MASK) {
			// Right click
			try {
				FormeDessin f = this.vg.en_dessous(x, y);
				if ((f instanceof AreteDessin) || (f instanceof SommetDessin)) {
					this.maybeShowPopup(evt, f);
				}
			} catch (NoSuchElementException e) {

			}
		}
	}

	public void mouseClicked(MouseEvent evt) {
	}

	public void mouseEntered(MouseEvent evt) {
	}

	public void mouseExited(MouseEvent evt) {
	}

	public void mouseMoved(MouseEvent evt) {
	}

	private void maybeShowPopup(MouseEvent e, final FormeDessin f) {
		if (e.isPopupTrigger()) {
			final boolean estSommet = (f instanceof SommetDessin);
			VueEtatPanel vueEtatPanel = new VueEtatPanel() {
				public void elementModified(String str) {
					if (estSommet) {
						((SommetDessin) f).setEtat(str);
					} else {
						((AreteDessin) f).setEtat(str);
					}
					StarVisuPanel.this.vertexPopup.setVisible(false);
					StarVisuPanel.this.parent.repaint();
				}
			};
			this.vertexPopup.removeAll();
			String etat = (estSommet ? ((SommetDessin) f).getEtat()
					: ((AreteDessin) f).getEtatStr());

			if (!estSommet) {
				final boolean isMarked = ((AreteDessin) f).getEtat();
				JMenuItem edgePopUpItem = new JMenuItem(isMarked ? "Dismark"
						: "Mark");
				edgePopUpItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						((AreteDessin) f).setEtat(!isMarked);
						StarVisuPanel.this.vertexPopup.setVisible(false);
						StarVisuPanel.this.parent.repaint();
					}
				});
				this.vertexPopup.add(edgePopUpItem);
				if (etat != null) {
					edgePopUpItem = new JMenuItem("No label");
					edgePopUpItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							((AreteDessin) f).setEtat(null);
							StarVisuPanel.this.vertexPopup.setVisible(false);
							StarVisuPanel.this.parent.repaint();
						}
					});
					this.vertexPopup.add(edgePopUpItem);
				}
				this.vertexPopup.addSeparator();
			}
			if (etat == null) {
				etat = "N";
			}
			EtatPanel etatPanel = new EtatPanel(TableCouleurs
					.getTableCouleurs(), vueEtatPanel, etat, true);
			this.vertexPopup.add(etatPanel);
			this.vertexPopup.show(e.getComponent(), e.getX(), e.getY());
			etatPanel.requestFocus();
		}
	}

	/**
	 * Left and center buttons just modify the structure
	 */
	public void mouseReleased(MouseEvent evt) {
		int x = evt.getX();
		int y = evt.getY();
		int modifiers = evt.getModifiers();

		if (modifiers == InputEvent.BUTTON3_MASK) {
			try {
				FormeDessin f = this.vg.en_dessous(x, y);
				this.maybeShowPopup(evt, f);
			} catch (NoSuchElementException e) {
			}
		} else if (this.drag_n_drop_sommet != null) {
			// On verifie s'il ne faut pas fusionner deux sommets
			try {
				SommetDessin s = this.vg.sommet_en_dessous(x, y,
						this.drag_n_drop_sommet);
				AreteDessin a = this.vg.rechercherArete(s.getEtiquette(),
						this.sommetC.getEtiquette());
				this.vg.delObject(a);
				this.vg.delObject(s);
				this.renumeberVertex();
				this.vertexNumber--;
				this.parent.repaint();
			} catch (NoSuchElementException e) {
				this.drag_n_drop_sommet = null;
			}
		}
	}

	/**
	 * Modify the structure
	 */
	public void mouseDragged(MouseEvent evt) {
		int x = evt.getX();
		int y = evt.getY();
		int modifiers = evt.getModifiers();

		if ((modifiers == InputEvent.BUTTON2_MASK)
				|| (modifiers == (InputEvent.BUTTON1_MASK | InputEvent.ALT_MASK))) {
			if (this.drag_n_drop_sommet != null) {
				Point p = this.getRoundedPosition(x, y);
				if (p != null) {
					try {
						// S'il y a un sommet dessous, il est aspire
						SommetDessin s = this.vg.sommet_en_dessous(x, y,
								this.drag_n_drop_sommet);
						this.ancien_pos = new Point(s.centreX(), s.centreY());
						this.drag_n_drop_sommet.placer(this.ancien_pos.x,
								this.ancien_pos.y);
					} catch (NoSuchElementException e) {
						// Sinon il est simplement deplace
						this.drag_n_drop_sommet.placer(p.x, p.y);
						this.ancien_pos = new Point(x, y);
					}
					this.renumeberVertex();
					this.parent.repaint();
				}
			}
		}
	}
}
