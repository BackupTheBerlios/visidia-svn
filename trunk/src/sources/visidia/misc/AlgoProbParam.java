package visidia.misc;

import visidia.simulation.agents.AgentChooser;
import visidia.agents.agentchooser.RandomAgentChooser;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.Random;
import java.util.Vector;

/**
 * Use me for creating agents on nodes with either a bernoulli or a
 * Poisson law. Used By RandomAgentChooser.
 *
 * @see RandomAgentChooser
 **/

public class AlgoProbParam extends JFrame implements ActionListener, ListSelectionListener, ChangeListener, KeyListener {


    /**
	 * 
	 */
	private static final long serialVersionUID = -6852598137642532543L;

	private final Color colorGreen = new Color(39,117,93);

    private JSpinner probValue;
    private JButton probB;
    private boolean probState;
    
    
    private JTextField algoName;
    private JButton algoB;

    private JPanel northPanel;


    private JCheckBox multipleChoice;
    private boolean choiceState;
    private JSpinner probMultiple;
    
    private DefaultListModel listModel;
    private JList list;
    private JPanel centerPanel;

    
    private JButton okB;
    private JButton cancelB;

    private JTextPane info ;
    private StyledDocument doc;
    private MutableAttributeSet keyWord;
    
    private JPanel southPanel;
    

    private AgentChooser agentChooser;
    
    public AlgoProbParam (String algo, String prob, AgentChooser ac) {
	super("Choose your parameters");
	
	agentChooser = ac;

	setResizable(false);

	/**
	 * Initialization of the north panel
	 **/
	
	probState = true; // Bernoulli Law
	probB = new JButton("Bernouilli Parameter");
	probB.addActionListener(this);


	SpinnerNumberModel spinnerModel = new SpinnerNumberModel(new Float(0.500),new Float(0),new Float(1),new Float(0.001));
	probValue = new JSpinner(spinnerModel);
	probValue.addChangeListener(this);
	probValue.addKeyListener(this);
	probValue.setEnabled(true);


	algoName = new JTextField(algo);
	algoName.setHorizontalAlignment(JTextField.CENTER);
	algoName.addActionListener(this);
	algoName.addKeyListener(this);
	
	algoB = new JButton("Algorithm Name");
	algoB.addActionListener(this);
	
	northPanel = new JPanel(new GridLayout(2,2));
	northPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Parameters"));
	northPanel.add(probB);
	northPanel.add(probValue);
	northPanel.add(algoB);
	northPanel.add(algoName);


	
	/**
	 * Initialization of the center frame
	 **/
	
	listModel = new DefaultListModel();
	listModel.addElement("BasicAgent");
	listModel.addElement("BasicSynchronizedAgent1");
	listModel.addElement("BasicSynchronizedAgent2");
	listModel.addElement("BasicSynchronizedAgent3");
	listModel.addElement("Handshake");
	listModel.addElement("Akka");
	
	list = new JList(listModel); 
	list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	list.setLayoutOrientation(JList.VERTICAL);
	

	list.addListSelectionListener(this);

	JScrollPane listScroller = new JScrollPane(list);
	listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	listScroller.setPreferredSize(new Dimension(370,110));
	listScroller.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),""));

	
	multipleChoice = new JCheckBox("Multiple selection");
	multipleChoice.addActionListener(this);
	choiceState = true; // Single selection 


	centerPanel = new JPanel(new BorderLayout());
	centerPanel.add(listScroller,BorderLayout.CENTER);
	centerPanel.add(multipleChoice,BorderLayout.NORTH);
	/**
	 * Initialization of the south panel
	 *
	 **/
	
	JPanel okCancelPanel = new JPanel();
	okB = new JButton("Ok");
	cancelB = new JButton("Cancel");
	

	okB.addActionListener(this);
	cancelB.addActionListener(this);
	
	okCancelPanel.add(okB);
	okCancelPanel.add(cancelB);

	/**
	 * Jus for fun
	 **/
	info = new JTextPane();
	info.setPreferredSize(new Dimension(360,70));
	doc = info.getStyledDocument();
	keyWord = new SimpleAttributeSet();
	doc.setParagraphAttributes(0, 0, keyWord, true);
	info.setEditable(false);
	info.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Info"));

	southPanel = new JPanel(new BorderLayout());
	
	southPanel.add(info,BorderLayout.CENTER);
	southPanel.add(okCancelPanel,BorderLayout.SOUTH);
	

	
	
	/**
	 * Initialization of the main frame
	 **/
	this.setLayout(new BorderLayout());

	this.getContentPane().add(northPanel, BorderLayout.NORTH);
	this.getContentPane().add(southPanel, BorderLayout.SOUTH);
	this.getContentPane().add(centerPanel, BorderLayout.CENTER);
	
	this.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    close();
		}
	    });
    }

    public void close() {
	setVisible(false);
	dispose();
	Runtime.getRuntime().gc();
    }
    

    /**
     * write some comments in the info panel c'est un peu moche quand
     * même.
     **/
    private void write() {
	try {
	    doc.remove(0,doc.getLength());
	} catch (Exception e) {
	}

	if(choiceState) {
	    
	    if(probState) {
		write("On each Vertex, one agent ", colorGreen);
		write("["+algoName.getText()+"]",Color.red);
		write(" will be created according to a ", Color.blue);
		write("Bernoulli law ", colorGreen);
		write("with parameter: ", Color.blue);
		write(((Float)((SpinnerNumberModel)probValue.getModel()).getValue()).toString(),Color.red);
	    }  else {
		write("On each Vertex, a random number of agents ", colorGreen);
		write("["+algoName.getText()+"]",Color.red);
		write(" will be created according to a ", Color.blue);
		write("Poisson law ", colorGreen);
		write("with parameter: ", Color.blue);
		write(((Float)((SpinnerNumberModel)probValue.getModel()).getValue()).toString(),Color.red);
	    }
	} else {
	    if(probState) {
		write("On each Vertex, one agent ", colorGreen);
		write("[ choosen randomly from the selection ]",Color.blue);
		write(" will be created according to a ", Color.blue);
		write("Bernoulli law ", colorGreen);
		write("with parameter: ", Color.blue);
		write(((Float)((SpinnerNumberModel)probValue.getModel()).getValue()).toString(),Color.red);
	    }  else {
		write("On each Vertex, a random number of agents ", colorGreen);
		write("[ choosen randomly form the selection ]",Color.blue);
		write(" will be created according to a ", Color.blue);
		write("Poisson law ", colorGreen);
		write("with parameter: ", Color.blue);
		write(((Float)((SpinnerNumberModel)probValue.getModel()).getValue()).toString(),Color.red);
	    }
	}
	
    }
	
    private void write(String s,Color c) {
	try {
	    StyleConstants.setForeground(keyWord, c);
	    doc.insertString(doc.getLength(), s, keyWord);
	}catch (Exception e) {}
    }

    
    public void actionPerformed(ActionEvent evt) {
	if(evt.getSource() instanceof JButton) {
	    if(evt.getSource() == okB) {
		if(! list.isSelectionEmpty()) {
		    Vector<String> selection = new Vector<String>(list.getSelectedValues().length);
		    for (int i = 0; i < list.getSelectedValues().length ; i++) {
			selection.add((String)((list.getSelectedValues())[i]));
		    }
		    
		    ((RandomAgentChooser)agentChooser).setParam(((Float)probValue.getValue()).floatValue(),selection);

		} else 
		    ((RandomAgentChooser)agentChooser).setParam(algoName.getText(),((Float)probValue.getValue()).floatValue());
		close();
	    } else if (evt.getSource() ==  cancelB){
		close();
		/**
		 * Just for fun
		 **/
	    } else if (evt.getSource() == algoB) {
		list.clearSelection();
		Random rand = new Random();
		algoName.setText((String)listModel.elementAt(rand.nextInt(listModel.size())));
		write();
	    } else if (evt.getSource() == probB) {
		if(probState) {
		    probB.setText("Poisson Parameter (to do)");
		} else {
		    probB.setText("Bernoulli parameter");
		}
		probState = !probState;
		write();
	    }
	} else if (evt.getSource() instanceof JCheckBox) {
	    if(choiceState) {
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		algoName.setEnabled(false);
		algoB.setEnabled(false);
	    } else {
		list.clearSelection();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		algoName.setEnabled(true);
		algoB.setEnabled(true);
	    }
	    choiceState = !choiceState;
	    write();
	} else {
	    write();
	}
    }
    
    public void stateChanged(ChangeEvent e) {
	write();
    }

    
    public void valueChanged(ListSelectionEvent e) {
	int i = list.getSelectedIndex();
	if(i != -1){
	    String s = (String)listModel.elementAt(i);
	    algoName.setText(s);
	    write();
	}
    }

    public void keyPressed(KeyEvent e) {write();}
    public void keyReleased(KeyEvent e) {write();}
    public void keyTyped(KeyEvent e) {write();}
    
    public void start() {
	setPreferredSize(new Dimension(420,300));
	
	pack();

	write();
	
	setVisible(true);
	
    }
    
}
