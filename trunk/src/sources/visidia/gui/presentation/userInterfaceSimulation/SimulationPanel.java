package visidia.gui.presentation.userInterfaceSimulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JPanel;

import visidia.gui.metier.simulation.SentMessage;
import visidia.gui.presentation.FormeDessin;
import visidia.gui.presentation.RecoverableObject;
import visidia.gui.presentation.SelectionDessin;
import visidia.gui.presentation.SelectionGetData;
import visidia.gui.presentation.SelectionUnit;
import visidia.gui.presentation.userInterfaceEdition.undo.UndoInfo;
import visidia.simulation.MessageSendingAck;
import visidia.simulation.MessageSendingEvent;

public class SimulationPanel extends JPanel implements ActionListener,
		MouseListener, MouseMotionListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4148068412265994572L;

	/** Couleur de fond par defaut du grapheVisuPanel * */
	// private static final Color RECT_SELECTION_COULEUR = Color.gray;
	private FenetreDeSimulationDist fenetreDeSimulationDist;

	private FenetreDeSimulation fenetreDeSimulation;

	protected FormeDessin objet_sous_souris;

	protected int PAS_PAR_DEFAUT = 10;

	protected int lePas;

	protected javax.swing.Timer timer;

	protected int x_ancien;

	protected int y_ancien;

	int current_x = 0;

	int current_y = 0;

	/** Dimension du graphique */
	protected Dimension size;

	protected SelectionUnit selectionUnit;

	protected Vector sentMessageVector = new Vector(10, 10);

	private final Object concurrentObject = new Object();

	/**
	 * cet objet sert a bloquer le SimulEventHandler en mode synchrone en
	 * attendant la fin de la visualization des messages
	 */

	private final Object waitObject = new Object();

	/**
	 * Instancie un SimulationPanel associe a la fenetre de simulation passee en
	 * argument.
	 */
	public SimulationPanel(FenetreDeSimulation simulation) {
		this.fenetreDeSimulation = simulation;
		this.objet_sous_souris = null;
		this.lePas = this.PAS_PAR_DEFAUT;

		if (simulation.getVueGraphe().getGraphe().ordre() != 0) {
			this.size = simulation.getVueGraphe().donnerDimension();
			this.setPreferredSize(this.size);
			this.revalidate();
		} else {
			this.size = new Dimension(0, 0);
		}

		this.selectionUnit = new SelectionUnit(new SelectionGetData() {
			public SelectionDessin getSelectionDessin() {
				return SimulationPanel.this.fenetreDeSimulation.selection;
			}

			public UndoInfo getUndoInfo() throws NoSuchMethodException {
				throw new NoSuchMethodException("undo processing not used");
			}

			public RecoverableObject getRecoverableObject() {
				return SimulationPanel.this.fenetreDeSimulation.getVueGraphe();
			}
		}, this);

		this.addMouseListener(this.selectionUnit);
		this.addMouseListener(this);
		this.addMouseMotionListener(this.selectionUnit);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);

		this.timer = new javax.swing.Timer(30, this);
		this.setBackground(new Color(0xe6e6fa));
	}

	public SimulationPanel(FenetreDeSimulationDist simulation) {
		this.fenetreDeSimulationDist = simulation;
		this.objet_sous_souris = null;
		this.lePas = this.PAS_PAR_DEFAUT;

		if (simulation.getVueGraphe().getGraphe().ordre() != 0) {
			this.size = simulation.getVueGraphe().donnerDimension();
			this.setPreferredSize(this.size);
			this.revalidate();
		} else {
			this.size = new Dimension(0, 0);
		}

		this.selectionUnit = new SelectionUnit(new SelectionGetData() {
			public SelectionDessin getSelectionDessin() {
				return SimulationPanel.this.fenetreDeSimulationDist.selection;
			}

			public UndoInfo getUndoInfo() throws NoSuchMethodException {
				throw new NoSuchMethodException("undo processing not used");
			}

			public RecoverableObject getRecoverableObject() {
				return SimulationPanel.this.fenetreDeSimulationDist
						.getVueGraphe();
			}
		}, this);

		this.addMouseListener(this.selectionUnit);
		this.addMouseListener(this);
		this.addMouseMotionListener(this.selectionUnit);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);

		this.timer = new javax.swing.Timer(30, this);
		this.setBackground(new Color(0xe6e6fa));
	}

	public int pas() {
		return this.lePas;
	}

	public void updatePas(int p) {
		this.lePas = p;
		// synchronized(sentMessageVector){
		synchronized (this.concurrentObject) {
			// SentMessage sentMessage;
			int size = this.sentMessageVector.size();
			for (int i = 0; i < size; i++) {
				((SentMessage) this.sentMessageVector.elementAt(i)).setStep(p);
			}
		}
	}

	/** * PROBLEME SEND TO *** */
	/**
	 * Redessine les elements du graphique.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (this.fenetreDeSimulationDist == null) {
			this.fenetreDeSimulation.getVueGraphe().dessiner(this, g);
		} else {
			this.fenetreDeSimulationDist.getVueGraphe().dessiner(this, g);
		}

		g.setColor(Color.red);

		// synchronized(sentMessageVector) {
		synchronized (this.concurrentObject) {
			SentMessage sentMessage;
			int size = this.sentMessageVector.size();
			for (int i = 0; i < size; i++) {
				sentMessage = (SentMessage) this.sentMessageVector.elementAt(i);
				sentMessage.paint(g);
			}
		}

		this.selectionUnit.drawSelection(g);
	}

	public void actionPerformed(ActionEvent e) {

		// synchronized(sentMessageVector){
		synchronized (this.concurrentObject) {
			SentMessage sentMessage;
			int size = this.sentMessageVector.size();
			Vector tmpVect = new Vector(size);
			// int i=size-1;
			for (int i = 0; i < size; i++) {
				// for(int i= size-1; i >=0; i--){
				sentMessage = (SentMessage) this.sentMessageVector.elementAt(i);
				if (sentMessage.isIntoBounds()) {
					sentMessage.moveForward();
					tmpVect.add(sentMessage);
				} else {
					MessageSendingAck msa = new MessageSendingAck(sentMessage
							.getEvent().eventNumber());

					// envoyer un message d'acquitement de fin d'animation
					try {
						if (this.fenetreDeSimulationDist == null) {
							this.fenetreDeSimulation.getAckPipe().put(msa);
							// System.out.println("UN MESSAGE TRANSMIS");
						} else {
							this.fenetreDeSimulationDist.getAckPipe().put(msa);
						}

					} catch (InterruptedException exp) {
						// this interruption should have been caused
						// by the simulation stop.
						return;
					}
					// supprimer le message de la liste des message a etre
					// afficher
					// sentMessageVector.removeElementAt(i);
				}
			}

			this.sentMessageVector = tmpVect;

			if (tmpVect.size() == 0) {
				try {
					synchronized (this.waitObject) {
						this.waitObject.notify();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out
							.println("Bug ref 1 : SimulationPanel, a reporter en precisant\n les conditions");
				}
			}
		}
		this.repaint();
	}

	public boolean isRunning() {
		if (this.timer.isRunning()) {
			return true;
		} else {
			return false;
		}
	}

	public void start() {
		this.timer.start();
	}

	public void pause() {
		this.timer.stop();
	}

	public void stop() {
		this.timer.stop();
		// synchronized(sentMessageVector){
		synchronized (this.concurrentObject) {
			this.sentMessageVector = new Vector(10, 10);
		}

		synchronized (this.waitObject) {
			try {
				this.waitObject.notifyAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void mousePressed(MouseEvent evt) {
	}

	public void mouseClicked(MouseEvent evt) {
	}

	public void mouseMoved(MouseEvent evt) {
	}

	public void mouseReleased(MouseEvent evt) {
	}

	public void mouseDragged(MouseEvent evt) {
	}

	public void mouseEntered(MouseEvent evt) {
	}

	public void mouseExited(MouseEvent evt) {
	}

	/**
	 * Implementation de KeyListener.
	 */
	public void keyPressed(KeyEvent evt) {
		switch (evt.getKeyCode()) {
		// Delete
		case KeyEvent.VK_DELETE:
		case KeyEvent.VK_BACK_SPACE:
			if (this.fenetreDeSimulationDist == null) {
				this.fenetreDeSimulation.commandeSupprimer();
			} else {
				this.fenetreDeSimulationDist.commandeSupprimer();
			}
			this.repaint();
			break;
		default:
		}
	}

	public void keyReleased(KeyEvent evt) {
	}

	public void keyTyped(KeyEvent evt) {
	}

	/* Fin d'implementation de KeyListener */

	public Dimension getMinimumSize() {
		return new Dimension(20, 20);
	}

	// à finir
	public void terminatedAlgorithm() {
		synchronized (this.concurrentObject) {
			this.repaint();
		}
	}

	public void nextPulseReady() {
		// attendre la fin de la visualization des messages du pulse en cours
		// et envoyer un acquitement
		// synchronized(sentMessageVector){
		try {
			synchronized (this.waitObject) {
				if (this.sentMessageVector.size() != 0) {
					// System.out.println("Bloque");
					this.waitObject.wait();
					// System.out.println("Debloque");
				}
				// sentMessageVector.wait();
			}
		} catch (Exception e) {
			System.out
					.println("bug ref 0 : Simulation Panel, a reporter en precisant les conditions");
			e.printStackTrace();
		}
	}

	public void animate(MessageSendingEvent mse) {
		if (this.fenetreDeSimulationDist == null) {
			// synchronized(sentMessageVector){
			synchronized (this.concurrentObject) {
				SentMessage sentMessage = new SentMessage(mse,
						this.fenetreDeSimulation.getVueGraphe()
								.rechercherSommet(mse.sender().toString())
								.centre(), this.fenetreDeSimulation
								.getVueGraphe().rechercherSommet(
										mse.receiver().toString()).centre(),
						this.lePas);
				this.sentMessageVector.add(sentMessage);
			}
		} else {
			// synchronized(sentMessageVector){
			synchronized (this.concurrentObject) {
				SentMessage sentMessage = new SentMessage(mse,
						this.fenetreDeSimulationDist.getVueGraphe()
								.rechercherSommet(mse.sender().toString())
								.centre(), this.fenetreDeSimulationDist
								.getVueGraphe().rechercherSommet(
										mse.receiver().toString()).centre(),
						this.lePas);
				this.sentMessageVector.add(sentMessage);
			}
		}

	}

}
