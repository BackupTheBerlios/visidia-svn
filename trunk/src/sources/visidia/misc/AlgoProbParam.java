package visidia.misc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import visidia.agents.agentchooser.RandomAgentChooser;
import visidia.simulation.agents.AgentChooser;

/**
 * Use me for creating agents on nodes with either a bernoulli or a Poisson law.
 * Used By RandomAgentChooser.
 * 
 * @see RandomAgentChooser
 */

public class AlgoProbParam extends JFrame implements ActionListener,
		ListSelectionListener, ChangeListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6852598137642532543L;

	private final Color colorGreen = new Color(39, 117, 93);

	private JSpinner probValue;

	private JButton probB;

	private boolean probState;

	private JTextField algoName;

	private JButton algoB;

	private JPanel northPanel;

	private JCheckBox multipleChoice;

	private boolean choiceState;

	private DefaultListModel listModel;

	private JList list;

	private JPanel centerPanel;

	private JButton okB;

	private JButton cancelB;

	private JTextPane info;

	private StyledDocument doc;

	private MutableAttributeSet keyWord;

	private JPanel southPanel;

	private AgentChooser agentChooser;

	public AlgoProbParam(String algo, String prob, AgentChooser ac) {
		super("Choose your parameters");

		this.agentChooser = ac;

		this.setResizable(false);

		/**
		 * Initialization of the north panel
		 */

		this.probState = true; // Bernoulli Law
		this.probB = new JButton("Bernouilli Parameter");
		this.probB.addActionListener(this);

		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(new Float(
				0.500), new Float(0), new Float(1), new Float(0.001));
		this.probValue = new JSpinner(spinnerModel);
		this.probValue.addChangeListener(this);
		this.probValue.addKeyListener(this);
		this.probValue.setEnabled(true);

		this.algoName = new JTextField(algo);
		this.algoName.setHorizontalAlignment(JTextField.CENTER);
		this.algoName.addActionListener(this);
		this.algoName.addKeyListener(this);

		this.algoB = new JButton("Algorithm Name");
		this.algoB.addActionListener(this);

		this.northPanel = new JPanel(new GridLayout(2, 2));
		this.northPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Parameters"));
		this.northPanel.add(this.probB);
		this.northPanel.add(this.probValue);
		this.northPanel.add(this.algoB);
		this.northPanel.add(this.algoName);

		/**
		 * Initialization of the center frame
		 */

		this.listModel = new DefaultListModel();
		this.listModel.addElement("BasicAgent");
		this.listModel.addElement("BasicSynchronizedAgent1");
		this.listModel.addElement("BasicSynchronizedAgent2");
		this.listModel.addElement("BasicSynchronizedAgent3");
		this.listModel.addElement("Handshake");
		this.listModel.addElement("Akka");

		this.list = new JList(this.listModel);
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.setLayoutOrientation(JList.VERTICAL);

		this.list.addListSelectionListener(this);

		JScrollPane listScroller = new JScrollPane(this.list);
		listScroller
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listScroller.setPreferredSize(new Dimension(370, 110));
		listScroller.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), ""));

		this.multipleChoice = new JCheckBox("Multiple selection");
		this.multipleChoice.addActionListener(this);
		this.choiceState = true; // Single selection

		this.centerPanel = new JPanel(new BorderLayout());
		this.centerPanel.add(listScroller, BorderLayout.CENTER);
		this.centerPanel.add(this.multipleChoice, BorderLayout.NORTH);
		/**
		 * Initialization of the south panel
		 * 
		 */

		JPanel okCancelPanel = new JPanel();
		this.okB = new JButton("Ok");
		this.cancelB = new JButton("Cancel");

		this.okB.addActionListener(this);
		this.cancelB.addActionListener(this);

		okCancelPanel.add(this.okB);
		okCancelPanel.add(this.cancelB);

		/**
		 * Jus for fun
		 */
		this.info = new JTextPane();
		this.info.setPreferredSize(new Dimension(360, 70));
		this.doc = this.info.getStyledDocument();
		this.keyWord = new SimpleAttributeSet();
		this.doc.setParagraphAttributes(0, 0, this.keyWord, true);
		this.info.setEditable(false);
		this.info.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "Info"));

		this.southPanel = new JPanel(new BorderLayout());

		this.southPanel.add(this.info, BorderLayout.CENTER);
		this.southPanel.add(okCancelPanel, BorderLayout.SOUTH);

		/**
		 * Initialization of the main frame
		 */
		this.setLayout(new BorderLayout());

		this.getContentPane().add(this.northPanel, BorderLayout.NORTH);
		this.getContentPane().add(this.southPanel, BorderLayout.SOUTH);
		this.getContentPane().add(this.centerPanel, BorderLayout.CENTER);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				AlgoProbParam.this.close();
			}
		});
	}

	public void close() {
		this.setVisible(false);
		this.dispose();
		Runtime.getRuntime().gc();
	}

	/**
	 * write some comments in the info panel c'est un peu moche quand même.
	 */
	private void write() {
		try {
			this.doc.remove(0, this.doc.getLength());
		} catch (Exception e) {
		}

		if (this.choiceState) {

			if (this.probState) {
				this.write("On each Vertex, one agent ", this.colorGreen);
				this.write("[" + this.algoName.getText() + "]", Color.red);
				this.write(" will be created according to a ", Color.blue);
				this.write("Bernoulli law ", this.colorGreen);
				this.write("with parameter: ", Color.blue);
				this.write(((Float) ((SpinnerNumberModel) this.probValue
						.getModel()).getValue()).toString(), Color.red);
			} else {
				this.write("On each Vertex, a random number of agents ",
						this.colorGreen);
				this.write("[" + this.algoName.getText() + "]", Color.red);
				this.write(" will be created according to a ", Color.blue);
				this.write("Poisson law ", this.colorGreen);
				this.write("with parameter: ", Color.blue);
				this.write(((Float) ((SpinnerNumberModel) this.probValue
						.getModel()).getValue()).toString(), Color.red);
			}
		} else {
			if (this.probState) {
				this.write("On each Vertex, one agent ", this.colorGreen);
				this.write("[ choosen randomly from the selection ]",
						Color.blue);
				this.write(" will be created according to a ", Color.blue);
				this.write("Bernoulli law ", this.colorGreen);
				this.write("with parameter: ", Color.blue);
				this.write(((Float) ((SpinnerNumberModel) this.probValue
						.getModel()).getValue()).toString(), Color.red);
			} else {
				this.write("On each Vertex, a random number of agents ",
						this.colorGreen);
				this.write("[ choosen randomly form the selection ]",
						Color.blue);
				this.write(" will be created according to a ", Color.blue);
				this.write("Poisson law ", this.colorGreen);
				this.write("with parameter: ", Color.blue);
				this.write(((Float) ((SpinnerNumberModel) this.probValue
						.getModel()).getValue()).toString(), Color.red);
			}
		}

	}

	private void write(String s, Color c) {
		try {
			StyleConstants.setForeground(this.keyWord, c);
			this.doc.insertString(this.doc.getLength(), s, this.keyWord);
		} catch (Exception e) {
		}
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() instanceof JButton) {
			if (evt.getSource() == this.okB) {
				if (!this.list.isSelectionEmpty()) {
					Vector<String> selection = new Vector<String>(this.list
							.getSelectedValues().length);
					for (int i = 0; i < this.list.getSelectedValues().length; i++) {
						selection
								.add((String) ((this.list.getSelectedValues())[i]));
					}

					((RandomAgentChooser) this.agentChooser).setParam(
							((Float) this.probValue.getValue()).floatValue(),
							selection);

				} else {
					((RandomAgentChooser) this.agentChooser).setParam(
							this.algoName.getText(), ((Float) this.probValue
									.getValue()).floatValue());
				}
				this.close();
			} else if (evt.getSource() == this.cancelB) {
				this.close();
				/**
				 * Just for fun
				 */
			} else if (evt.getSource() == this.algoB) {
				this.list.clearSelection();
				Random rand = new Random();
				this.algoName.setText((String) this.listModel.elementAt(rand
						.nextInt(this.listModel.size())));
				this.write();
			} else if (evt.getSource() == this.probB) {
				if (this.probState) {
					this.probB.setText("Poisson Parameter (to do)");
				} else {
					this.probB.setText("Bernoulli parameter");
				}
				this.probState = !this.probState;
				this.write();
			}
		} else if (evt.getSource() instanceof JCheckBox) {
			if (this.choiceState) {
				this.list
						.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				this.algoName.setEnabled(false);
				this.algoB.setEnabled(false);
			} else {
				this.list.clearSelection();
				this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				this.algoName.setEnabled(true);
				this.algoB.setEnabled(true);
			}
			this.choiceState = !this.choiceState;
			this.write();
		} else {
			this.write();
		}
	}

	public void stateChanged(ChangeEvent e) {
		this.write();
	}

	public void valueChanged(ListSelectionEvent e) {
		int i = this.list.getSelectedIndex();
		if (i != -1) {
			String s = (String) this.listModel.elementAt(i);
			this.algoName.setText(s);
			this.write();
		}
	}

	public void keyPressed(KeyEvent e) {
		this.write();
	}

	public void keyReleased(KeyEvent e) {
		this.write();
	}

	public void keyTyped(KeyEvent e) {
		this.write();
	}

	public void start() {
		this.setPreferredSize(new Dimension(420, 300));

		this.pack();

		this.write();

		this.setVisible(true);

	}

}
