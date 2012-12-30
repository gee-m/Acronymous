package acronymous;

import java.io.*;
import java.util.Scanner;
import javax.swing.*;

//TODO Make code more self documenting

public class DictionaryReader{
	
	private final String dictionary;
	private final String dictionaryURL;
	private final String keyLetters;
	private final Object callingObject;
	//private File dictionaryFile;
	private int minJ = 2999;
	Scanner fileIn;
	
	public DictionaryReader(String dictionary, String keyLetters, Object o) {
		this.dictionary = dictionary;
		this.keyLetters = keyLetters.toLowerCase();
		callingObject = o;
		dictionaryURL = "D:/eclipse/workspace/Acronymous/src/acronymous/" +  "dictionaries/" + dictionary;
		//dictionaryURL = "dictionaries/" + dictionary;
		
		//dictionaryFile = new File(getClass().getResource(dictionary).getPath());
	}
	
	
	public String generateStringOfPolyWords() {
		String polyWords = "";
		String[][] rawWords = new String[keyLetters.length()][100000]; //3000
		String temp;
		
		for(int i = 0; i<keyLetters.length(); i++) {
			try {
				fileIn = new Scanner(new FileReader(dictionaryURL));
			}
			catch (FileNotFoundException e) {
				((AcronymousGUI) callingObject).setGeneratedWordsArea("Sorry, File not found");
				JOptionPane.showMessageDialog(null, "In file not found catch, filname : ");
			}
			int j = 0;
			while(fileIn.hasNextLine()) {
				temp = fileIn.nextLine().toLowerCase();
				if(((String) (temp.charAt(0) + "")).equalsIgnoreCase((keyLetters.charAt(i) + ""))) {
					rawWords[i][j] = temp;
					j++;
				}
			}
			minJ = (j>5 && j<minJ) ? j : minJ;
			fileIn.reset();
			if (i == keyLetters.length() - 1) {
				fileIn.close();
			}
		}
		//to print it to polyWords
		
		for(int j = 0; j < minJ; j++) {
			int i = (0 + j)%rawWords.length;
			polyWords += "   " + rawWords[i][j] + "\t";
			if (j%rawWords.length == rawWords.length-1) {
				polyWords += "\n";
			}
		}
		return polyWords;
		
		
	}
	
	public String generateStringOfMonoWords() {
		String monoWords = "   ";
		
		try {
			fileIn = new Scanner(new FileReader(dictionaryURL));
		}
		catch (FileNotFoundException e) {
			((AcronymousGUI) callingObject).setGeneratedWordsArea("Sorry, File not found: ");
			JOptionPane.showMessageDialog(null, "In file not found catch, filname : " + this.dictionary);
		}

		String temp = "";
		int i = 0;
		
		while (fileIn.hasNextLine()) {
			boolean stop = false; //this gets set to true if the word temp doesn't match the conditions
			temp = fileIn.nextLine().toLowerCase();
			int indexOfLast = 0;
			i = 0;
			
			if(temp.contains(keyLetters)) {
				monoWords += temp + "\n" + "   ";
			}
			else {
				while((stop == false) && (i != -1) && (temp.indexOf(keyLetters.charAt(i)) != -1)) {
					indexOfLast = (i == 0? 0 : temp.indexOf(keyLetters.charAt(i-1)));
					if (indexOfLast > temp.indexOf(keyLetters.charAt(i))) {
						stop = true;
					}
					else if(i == keyLetters.length()-1) {

						monoWords += temp + "\n" + "   ";
						
					}
					i = ((i>=keyLetters.length()-1) ? -1 : i+1);
				}
			}
			
		}
		
		if (monoWords.equals("   ")) {
		}

		return monoWords;
	}
	
	
}
