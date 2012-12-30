package acronymous;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.print.*;

//TODO make code more self documenting

public class AcronymousGUI extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 500;
	private Object o = this;
	private String dictionary = "liste_francais.txt";
	private File historyFile = new File("D:/eclipse/workspace/Acronymous/src/acronymous/" +  "history/" + "history.txt");
	
	private JTextField acronymInputField; 		// user's acronym input
	private JTextField decronymInputField;		//user's decronym input
	private JTextArea generatedWordsArea = new JTextArea(" ");	// the generated words from the input
	private JCheckBox setEditableCheckBox;
	private JScrollPane scrollPane;
	private JRadioButtonMenuItem frenchOption, englishOption;
	private JMenuItem print;
	private ButtonGroup dictionaryGroup;
	private JLabel languageLabel;
	private JLabel savedLabel;
	
	//***********************************************************************
	
	public AcronymousGUI() {
		setTitle("Acronymous acronnymization");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		createContents();
		setVisible(true);
		historyFile.setReadOnly();
	} // end AcronymousGUI constructor
	
	//***********************************************************************
	
	//Create the components and add to the window
	
	private void createContents() {
		JPanel inputPanel;
		JPanel bottomPanel;
		JLabel acronymLabel;
		JLabel decronymLabel;
		JButton decronymizeButton;
		JButton copyButton;
		JButton saveDecronymButton;
		Listener listener;
		
		setLayout(new BorderLayout());
		
		//create the user input panel
		acronymLabel = new JLabel("Acronym: ");
		languageLabel = new JLabel("   language: Francais");
		decronymLabel = new JLabel("                                                   Decronym: ");
		savedLabel = new JLabel("");
		acronymInputField = new JTextField(10);
		decronymInputField = new JTextField(10);
		decronymizeButton = new JButton("Decronymize!");
		saveDecronymButton = new JButton("Save Decronym!");
		inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		inputPanel.add(acronymLabel);
		inputPanel.add(acronymInputField);
		inputPanel.add(decronymizeButton);
		inputPanel.add(languageLabel);
		inputPanel.add(decronymLabel);
		inputPanel.add(decronymInputField);
		inputPanel.add(saveDecronymButton);
		inputPanel.add(savedLabel);
		
		//no need to create the center panel, it is only composed of a TextArea
		scrollPane = new JScrollPane(generatedWordsArea);

		//create the bottom panel
		copyButton = new JButton("copy text");
		setEditableCheckBox = new JCheckBox("set the text editable?", false);
		bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		bottomPanel.add(copyButton);
		bottomPanel.add(setEditableCheckBox);
		
		generatedWordsArea.setEditable(false);
		//add all of them to the window
		add(inputPanel, BorderLayout.NORTH);
		add(new JPanel(), BorderLayout.WEST); //in order to have decent borders around the text area
		add(new JPanel(), BorderLayout.EAST); //in order to have decent borders around the text area
		add(scrollPane, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		
		//add the menu bar
		JMenuBar mBar;
		JMenu menu1, menu2;
		menu1 = new JMenu("Language");
		menu2 = new JMenu("History");
		frenchOption = new JRadioButtonMenuItem("Francais");
		englishOption = new JRadioButtonMenuItem("English");
		ButtonGroup dictionaryGroup = new ButtonGroup();
		frenchOption.setSelected(true);
		frenchOption.setMnemonic(KeyEvent.VK_F);
		frenchOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		englishOption.setSelected(false);
		englishOption.setMnemonic(KeyEvent.VK_E);
		englishOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		dictionaryGroup.add(frenchOption);
		dictionaryGroup.add(englishOption);
		JMenu recentHistory = new JMenu("Recent history");
		recentHistory.setMnemonic(KeyEvent.VK_R);
		JMenuItem history = new JMenuItem("history.txt");
		JMenuItem clearHistory = new JMenuItem("Clear history");
		clearHistory.setMnemonic(KeyEvent.VK_C);
		history.setMnemonic(KeyEvent.VK_H);
		history.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
		recentHistory.add(history);
		print = new JMenuItem("print!");
		print.setMnemonic(KeyEvent.VK_P);
		print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		
		
		menu1.add(frenchOption);
		menu1.add(englishOption);
		menu2.add(recentHistory);
		menu2.add(clearHistory);
		mBar = new JMenuBar();
		mBar.add(menu1);
		mBar.add(menu2);
		mBar.add(print);
		setJMenuBar(mBar);
		
		
		
		//set up the listeners
		listener = new Listener();
		DictionaryListener dictionaryListener = new DictionaryListener();
		HistoryListener historyListener = new HistoryListener();
		
		decronymizeButton.addActionListener(listener);
		copyButton.addActionListener(listener);
		setEditableCheckBox.addActionListener(listener);
		frenchOption.addActionListener(dictionaryListener);
		englishOption.addActionListener(dictionaryListener);
		history.addActionListener(historyListener);
		clearHistory.addActionListener(historyListener);
		copyButton.addActionListener(listener);
		saveDecronymButton.addActionListener(listener);
		print.addActionListener(listener);
		
	}
	
	public void setGeneratedWordsArea(String t) {
		generatedWordsArea.setText(t);
	}
	
	private class Listener implements ActionListener, Printable {
		public void actionPerformed(ActionEvent e) {
			
			PrintWriter fileOut;
			
			//from the setEditable CheckBox:
			if (e.getActionCommand().equals("set the text editable?")) {
				if (setEditableCheckBox.isSelected()) {
					generatedWordsArea.setEditable(true);
				}
				else {
					generatedWordsArea.setEditable(false);
				}
			}
			
			//from the copy button
			if (e.getActionCommand().equals("copy text")) {
				generatedWordsArea.selectAll();
				generatedWordsArea.copy();
				generatedWordsArea.select(0, 0);
				JOptionPane.showMessageDialog(null, "in copy button listener");
			}
			
			if (e.getActionCommand().equals("Save Decronym!")) {
				try {
					historyFile.setWritable(true);
					fileOut = new PrintWriter(new FileWriter(historyFile, true));
					fileOut.print("\t Chosen Decronym: " + decronymInputField.getText());
					fileOut.close();
					historyFile.setReadOnly();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				// for visuals, simulated saving
				try {
					Thread.sleep(500);
					savedLabel.setText("... saved!");
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			
			//from the decronymize button:
			if (e.getActionCommand().equals("Decronymize!")) {
				if(acronymInputField.getText().equalsIgnoreCase("") || acronymInputField.getText().contains("1") 
						|| acronymInputField.getText().contains("2") || acronymInputField.getText().contains("3") 
						|| acronymInputField.getText().contains("4") || acronymInputField.getText().contains("5") 
						|| acronymInputField.getText().contains("6") || acronymInputField.getText().contains("7") 
						|| acronymInputField.getText().contains("8") || acronymInputField.getText().contains("9") ) {
					generatedWordsArea.setText("Sorry, you must only enter letters");
				}
				else {
					try {
						historyFile.setWritable(true);
						fileOut = new PrintWriter(new FileWriter(historyFile, true));
						String language = (frenchOption.isSelected() ? "francais:\t" : "english:\t");
						fileOut.println();
						fileOut.print(language + acronymInputField.getText());
						fileOut.close();
						historyFile.setReadOnly();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					DictionaryReader dictionaryReader = new DictionaryReader(dictionary, acronymInputField.getText(), o);
					
					String monoWords = dictionaryReader.generateStringOfMonoWords();
					String polyWords = dictionaryReader.generateStringOfPolyWords();
					generatedWordsArea.setText(("********************* Here is the list of the words that have \"" + acronymInputField.getText() + "\" in themselves, in order: \n\n"
							+ monoWords + "\n " +
							"************************************************************************************\n" +
							"************************************************************************************\n" +
							"************************************************************************************\n" +
							"************************************************************************************\n" +
							"************************************************************************************\n" +
							"************************************************************************************\n" +
							"************************************************************************************\n" +
							"************************************************************************************\n" +
							"\n <or>, words with which the beginning start by the letters: " + acronymInputField.getText() + "\n\n" + polyWords));
					generatedWordsArea.setCaretPosition(0);
					
				}
			}
			
			if (e.getActionCommand().equals("print!")) {
				try {
					generatedWordsArea.print();
				} catch (PrinterException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		//this is a mandatory method in the abstract for in implements Printable
		public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
			/*if (page > 0 ) {
				return NO_SUCH_PAGE;
			}
			
			Graphics2D g2d = (Graphics2D)g;
			g2d.translate(pf.getImageableX(), pf.getImageableY());
			
			generatedWordsArea.printAll(g); */
			return PAGE_EXISTS;
		}

	}
	
	private class DictionaryListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equalsIgnoreCase("Francais")) {
				dictionary = "liste_francais.txt";
				frenchOption.setSelected(true);
				languageLabel.setText("   language: Francais");
			}
			else if (e.getActionCommand().equalsIgnoreCase("English")) {
				dictionary = "list_english.txt";
				englishOption.setSelected(true);
				languageLabel.setText("   language: English ");
			}
		}
	}
	
	private class HistoryListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("history.txt")) {
				try {
					java.awt.Desktop.getDesktop().open(historyFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else if (e.getActionCommand().equals("Clear history")) {
				historyFile.setWritable(true);
				PrintWriter fileOut;
				try {
					historyFile.setWritable(true);
					fileOut = new PrintWriter(new FileWriter(historyFile, false));
					fileOut.println("************************************ YOU CANNOT EDIT THIS!, TO EDIT IT SAVE IT AS A NEW FILE, TO CLEAR, USE \"CLEAR HISTORY\""); 
					fileOut.println("*** From oldest to most recent:  ***");
					fileOut.println("************************************");
					fileOut.close();
					historyFile.setReadOnly();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
}
