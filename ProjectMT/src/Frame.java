package ProjectMT.src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class Frame extends JFrame {
	
	// This text field will display the file directory
	// Initially it will appear blank.
	JTextField userInput = new JTextField(" 		");
	
	//Buttons
	JButton Browse = new JButton("Browse");
	JButton Encrypt = new JButton("Encrypt");
	JButton Decrypt = new JButton("Decrypt");
	JButton Exit = new JButton("Exit");
	
	//The is the text field for the size, the (8) 
	//is the length of the text that can fit in this textfeild
	static JTextField sizecipherText = new JTextField(8);
	static JTextField sizeclearText = new JTextField(8);
	JTextField CompressionText = new JTextField(8);
	
	//This is the Text area, 10 is the row(height), and 31 is the column(width).
	static JTextArea CipherText = new JTextArea(10, 31);
	static JTextArea OriginalText = new JTextArea(10, 31);
	static FileDialog fd = null;
	
	//This array is used to create table row. There are 27 rows,
	//each 27 rows will have 4 informations.
	static Object[][] data = new Object[27][4];
	
	//this array is used to create the column name. there are four column.
	String[] columnName = { "Letter", "Occurence", "Frequence", "Code" };
	static int total = 0;
	//this creates a table
	static JTable table;

	public Frame() {
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
		
		//Here we are assigning the table with the row and columns
		table = new JTable(data, columnName);
		
		//setting the table size
		table.setPreferredScrollableViewportSize(new Dimension(300, 433));
		table.setFillsViewportHeight(true);
		
		//adding the table to a scrollpane, so we can scroll if we need to
		JScrollPane scrollPane = new JScrollPane(table);
		
		//creating a panel and adding the scrollpane into the panel.
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT, 50, 0));
		p1.add(scrollPane);
		
		//Creating a another panel for the size calculation
		JPanel p2 = new JPanel(new GridLayout(3, 0, 0, 0));
		
		//adding label to panle, label that describes the text area
		p2.add(new JLabel("Size ciphered Text: "));
		//adding the the sizecipher textfeild to p2 panel.
		p2.add(sizecipherText);
		p2.add(new JLabel("Size clear Text: "));
		p2.add(sizeclearText);
		
		//giving a label to the panel
		p2.add(new JLabel("Compression Ratio: "));
		p2.add(CompressionText);
		
		//adding border to the panel and title
		p2.setBorder(new TitledBorder("Compression Ratio"));
		
		//crateing a panel for ciptextarea
		JPanel CipText = new JPanel();
		//adding border and title
		CipText.setBorder(new TitledBorder("Ciphered Text"));
		//adding ciptextarea to the panel
		CipText.add(CipherText);
		//allowing ciptextpanel to be scroll
		JScrollPane CipScroll = new JScrollPane(CipText);
		//creates a panel for orignaltextarea
		JPanel OrgText = new JPanel();
		//adding border and title to the textfield
		OrgText.setBorder(new TitledBorder("Original Text"));
		//adding OriginalText area to the panel
		OrgText.add(OriginalText);
		//adding scroll area to originaltext
		JScrollPane OrgScroll = new JScrollPane(OrgText);
		
		//creating a panle to add orginaltext and ciptext
		JPanel BigPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		BigPanel.add(CipScroll);
		BigPanel.add(OrgScroll);
		
		//these are action listeners for the button. 
		//these tell the buttons what to do once clicked.
		BrowseHandler selecFile = new BrowseHandler();
		Browse.addActionListener(selecFile);
		EncryptHandler Encyption = new EncryptHandler();
		Encrypt.addActionListener(Encyption);
		decryptHandler uncrypt = new decryptHandler();
		Decrypt.addActionListener(uncrypt);
		ExitHandler Exits = new ExitHandler();
		Exit.addActionListener(Exits);
		
		//this is a label for the user input text area. They are added to 
		//the frame that the user will see
		add(new JLabel("Select the file you would like to run: "));
		add(userInput);
		add(Browse);
		add(Encrypt);
		add(Decrypt);
		add(Exit);
		//borderlayout will place the panel in the frame based on the locaton
		//east means the panel will appear on the right, west is left, south is botter.
		add(p1, BorderLayout.EAST);
		add(p2, BorderLayout.WEST);
		add(BigPanel, BorderLayout.SOUTH);
	}

	public static JTable getTable() {
		/*returns the table, this can be used in other class to get this table*/
		return table;
	}

	public static JTextArea getCipher() {
		return CipherText;
	}

	public static JTextArea getOrgingText(){
		return OriginalText;
	}
	public static FileDialog GetFileDialog() {
		return fd;
	}

	public static JTextField getsizecipher() {
		return sizecipherText;
	}

	private class decryptHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//calls the Decrypt function from Actons class
			Actions.Decrypt();
		}
	}

	private class BrowseHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//opens up a file dialog so the user can select a file
			JFrame b = new JFrame();
			fd = new FileDialog(b, "Pick a file: ", FileDialog.LOAD);
			fd.setVisible(true);
			userInput.setText(fd.getDirectory() + fd.getFile());
		}
	}

	private class ExitHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//exits the program
			System.exit(0);
		}
	}

	@SuppressWarnings("resource")
	public static int totalchar() throws IOException {
		//gets the total character of the program
		InputStreamReader isr;
		int character;
		try {
			isr = new InputStreamReader(new FileInputStream(fd.getDirectory()
					+ fd.getFile()));
			BufferedReader text = new BufferedReader(isr);
			do {
				//reads the text one character at a time
				character = text.read();
				if (character == 32 || character == 44 || character == 58
						|| character == 59) {
					character = 32;
					total += 1;
				}
				if ((character > 96 && character < 123)
						|| (character > 64 && character < 91)) {
					total += 1;
				}

			} while (character != -1);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//the sizecleartext now has the value of the total text
		sizeclearText.setText(String.valueOf(total));
		//System.out.println(total);
		return total;
	}

	private class EncryptHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int character;

			String word = "";
			try {
				InputStreamReader isr = new InputStreamReader(
						new FileInputStream(fd.getDirectory() + fd.getFile()));
				BufferedReader text = new BufferedReader(isr);
				//this makes the table in the database empty
				Actions.clear();
				//this calls the totalchar function to get the total characters in the text
				totalchar();
				do {

					character = text.read();
					if (character == 32 || character == 44 || character == 58
							|| character == 59) {
						character = 32;
					}
					//this adds the character to word
					word += String.valueOf((char) character);
					//this calls the ChartoDB method in Actions class
					Actions.ChartoDB((char) character, total);
					//this adds the text to the OriginalText TextFeild
					OriginalText.setText(word);

				} while (character != -1);
				//calls the OrderFreq function in Actions class 
				Actions.OrderFereq();
				Actions.HuffmanCode();
				Actions.InsertGuiTable();
				Actions.InsertCiphered();
				//gets the text from sizeciphertext textfeild
				String ciptextsize = sizecipherText.getText();
				String orgtextsize = sizeclearText.getText();
				
				//asigning the text to compressTextFeild
				CompressionText
						.setText(String.valueOf(100 - ((Integer
								.valueOf(ciptextsize) / Integer
								.valueOf(orgtextsize)) * 100)));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
