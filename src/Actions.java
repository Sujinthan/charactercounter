import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JTable;

import java.sql.Connection;

public class Actions {
	static int occur = 0;
	static int order = 0;
static String CipLetter;
	public static void ChartoDB(char letter, int total) throws SQLException,
			ClassNotFoundException, IOException {
		/*This function adds the Character to the database*/
		
		//System.out.println("inside  OrderFereq");
		//this variable gets the number, from the database,  
		//of time the letter has appeared in the text
		int occurcounts = getOccurence(letter);
		//System.out.print(letter);
		String sAdd = "Update Alphabet SET " + "Occurence = ?, Frequencey = ?"
				+ " WHERE Letter =?";
		//connects to database
		Connection conn = DB.getConnection();
		PreparedStatement addLetterPS = conn.prepareStatement(sAdd);
		addLetterPS.setInt(1, occurcounts);
		addLetterPS.setFloat(2, (float) occurcounts / total);
		addLetterPS.setString(3, String.valueOf(letter));
		addLetterPS.executeUpdate();
		addLetterPS.close();
		conn.close();
	}

	public static int getOccurence(char letter) throws SQLException,
			ClassNotFoundException {
		/*
		 * This function returns a number from the Occurence column in the database table.
		 */
		
		
		String sAdd = "SELECT Occurence FROM Alphabet WHERE Letter =?;";
		Connection conn = DB.getConnection();
		PreparedStatement getLetter = conn.prepareStatement(sAdd);
		getLetter.setString(1, String.valueOf(letter));
		ResultSet rs = getLetter.executeQuery();
		while (rs.next()) {
			 //this gets the value from the column "Occurence"
			occur = rs.getInt("Occurence");
			if (occur == 0) {
				/*
				 * occur is null the it equals 1
				 */
				occur += 1;
			}

			else
				/*
				 * if occur has a value then increase it by one
				 */
				occur += 1;
		}
		// System.out.println(occur);
		getLetter.close();
		conn.close();
		return occur;
	}

	public static void clear() throws ClassNotFoundException, SQLException {
		/*
		 * This function cleans everything in the database. 
		 */
		Connection conn = DB.getConnection();
		
		PreparedStatement getLetter = conn
				.prepareStatement("Update Alphabet SET "
						+ "Occurence = null , Frequencey = null, Orders = null, Huffman_code = null ");
		getLetter.executeUpdate();
		getLetter.close();
		conn.close();
	}

	public static void OrderFereq() throws ClassNotFoundException, SQLException {
		/*
		 * this gives value to the column Orders in the database
		 * it does this by getting the Occurence column in order from highest to lowest
		 * then it gives a value to the Orders column
		 */
		//System.out.println("inside  OrderFereq");
		Connection conn = DB.getConnection();
		PreparedStatement getLetter = conn
				.prepareStatement("SELECT Letter, Occurence  FROM Alphabet Order By Occurence desc");
		ResultSet rs = getLetter.executeQuery();

		while (rs.next()) {
			String Letter = rs.getString(1);
			int Occurence = rs.getInt(2);
			PreparedStatement getLetters = conn
					.prepareStatement("Update Alphabet SET "
							+ "Orders = ? Where Letter =?");
			getLetters.setInt(1, order += 1);
			getLetters.setString(2, Letter);
			getLetters.executeUpdate();
		}

		getLetter.close();
		conn.close();
	}

	public static void HuffmanCode() throws ClassNotFoundException,
			SQLException {
		/*
		 * This adds the values to the HuffmanCode column
		 */
		order = -1;
		System.out.println("inside HuffmanCode");
		Connection conn = DB.getConnection();
		PreparedStatement getLetter = conn
				.prepareStatement("SELECT Letter, Occurence  FROM Alphabet Order By Occurence desc");
		ResultSet rs = getLetter.executeQuery();
		//adds the HuffmanCode to an array
		String num[] = { "100", "0010", "0011", "1111", "1110", "1100", "1011",
				"1010", "0110", "0101", "11011", "01111", "01001", "01000",
				"00011", "00010", "00001", "00000", "110101", "011101",
				"011100", "1101001", "110100011", "110100001", "110100000",
				"1101000101", "1101000100" };
		while (rs.next()) {
			String Letter = rs.getString(1);
			int Occurence = rs.getInt(2);
			PreparedStatement getLetters = conn
					.prepareStatement("Update Alphabet SET "
							+ "Huffman_code = ? Where Letter =?");
			order += 1;
			//adds the hubbmanCode to by getting it from the array num
			getLetters.setInt(1, Integer.parseInt(num[order]));
			getLetters.setString(2, Letter);
			getLetters.executeUpdate();
		}

		getLetter.close();
		conn.close();
	}

	public static void InsertGuiTable() throws ClassNotFoundException,
			SQLException {
		/*
		 * Gets the table from database, and adds the correct values to the GUI table
		 */
		System.out.println("inside InsertGuiTable");
		JTable data = Frame.getTable();
		int count = 0;
		int num =0;
		Connection conn = DB.getConnection();
		PreparedStatement getLetter = conn
				.prepareStatement("SELECT * FROM Alphabet Order By Letter");
		ResultSet rs = getLetter.executeQuery();
		do {
			while (rs.next()) {
				String Letter = rs.getString("Letter");
				data.getModel().setValueAt(Letter, count, 0);
				String ASCII = rs.getString("ASCII_values");
				num += Integer.parseInt(ASCII,2);
				int Occur = rs.getInt("Occurence");
				data.getModel().setValueAt(Occur, count, 1);
				double frequency = rs.getDouble("Frequencey");
				data.getModel().setValueAt(frequency, count, 2);
				int Huffman_code = rs.getInt("Huffman_code");
				data.getModel().setValueAt(Huffman_code, count, 3);
				count += 1;
			}
		} while (count == 26);
		Frame.getsizeOrg().setText(String.valueOf(num));
		getLetter.close();
		conn.close();
	}
	public static void InsertCiphered() throws IOException, ClassNotFoundException, SQLException{
		/*
		 * this just adds the huffman code to the GUI cipheretext
		 */
		//FileDialog fd = Frame.GetFileDialog();
		//System.out.println("inside InsertCiphered");
		InputStreamReader  isr = new InputStreamReader (new FileInputStream(Frame.GetFileDialog().getDirectory() + Frame.GetFileDialog().getFile()));
		BufferedReader text = new BufferedReader(isr);
		
		Connection conn = DB.getConnection();
		PreparedStatement getLetter = conn
				.prepareStatement("SELECT Huffman_code, Letter FROM Alphabet Where Letter=?");
		int word = 0 ;
		String charac = "";
		String tempnum ="";
		int textnum = 0;
		do{
			textnum = text.read();
			//System.out.println(textnum);
			getLetter.setString(1, String.valueOf((char)textnum));
			ResultSet rs = getLetter.executeQuery();
			while(rs.next()){
				int Huffman_code = rs.getInt("Huffman_code");
				Frame.getsizecipher().setText(Frame.getsizecipher().getText() + String.valueOf(Huffman_code));
				tempnum=Integer.toBinaryString(Huffman_code);
				word += Integer.valueOf(tempnum, 2);
				//System.out.println("word" + tempnum);
				charac += String.valueOf(Huffman_code);
				String letter = rs.getString("Letter"); 
				CipLetter+=letter;
			}
		}while(textnum!=-1);
		
		/*
		String huffcodtext = Frame.getOrgingText().getText();
		int word = 0 ;
		String charac = "";
		for (int x = 0; x<huffcodtext.length();x++){
			getLetter.setString(1, String.valueOf(huffcodtext.charAt(x)));
			ResultSet rs = getLetter.executeQuery();
			while (rs.next()) {
				int Huffman_code = rs.getInt("Huffman_code");
				word+=Huffman_code;
				charac += String.valueOf(Huffman_code);
				String letter = rs.getString("Letter"); 
				CipLetter+=letter;
				
			}
		}*/
		getLetter.close();
		conn.close();
		Frame.getCipher().setText(charac);
		Frame.getsizecipher().setText(String.valueOf((word)));
	}
	
	public static void Decrypt(){
		/*
		 * this decrypts the text
		 */
		Frame.getCipher().setText(CipLetter);
	}
}
