package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * The driver class for the whole program.
 * 
 * @author Mason Makarwich
 *
 */

public class Main {

	/**
	 * Driver method. Asks the user for a set of letters, and then displays the list of possible words that can be made
	 * from those letters.
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		boolean loop1Break = true; // Used to break the following while loop when the user enters something.
		String input = ""; // The string to contain the list of letters.
		
		/*
		 * Asks the user for input. Loops until they enter something.
		 */
		
		//TODO Add more error checking for invalid input.
		
		while(loop1Break) {
		
			System.out.println("Enter letters, no spaces, all on one line.");
			Scanner scan = new Scanner(System.in);
			input = scan.nextLine();
			
			if(input.equals("")) {
				
				System.out.println("Nothing was entered.");
			} else {
				
				loop1Break = false;
			}
		}
		
		input = input.toLowerCase();
		
		/*
		 * Sets up the variables used in the recursive algorithm, wordFinder.
		 */
		
		Queue<Character> lList = new LinkedList<Character>(); // To be used as the list of letters after the word string
		char[] letterList = input.toCharArray(); // Splits the input string into a char array.
		
		/*
		 * Adds all of the letters in the char array to the LinkedList.
		 */
		
		for(int i = 0; i<letterList.length; i++) {
			
			lList.add(letterList[i]);
		}
		
		/*
		 * Generates all of the words with wordFinder.
		 */
		
		Set<String> wordList = wordFinder(lList, "");
		
		/*
		 * Prints out all of the generated words.
		 */
		
		System.out.println("\nThe possible words are:\n");
		
		for(String item : wordList) {
			
			System.out.println(item);
		}
	}
	
	/**
	 * A recursive algorithm that takes in a list of letters and a part of a word and generates all words using a
	 * combination of the partial word and each of the letters in the list.
	 * 
	 * @param letterList List of the letters currently in rotation to be added on to the end of <code>word</code>.
	 * @param word Partial word that is modified by <code>letterList</code>.
	 * @return List of acceptable words.
	 */
	
	public static Set<String> wordFinder(Queue<Character> letterList, String word) {
		
		Set<String> wordList = new HashSet<String>(); // The list of words to be returned.
		
		/*
		 * Recursive case: If there are letters in the letter list.
		 * Base case: If the list is empty.
		 */
		
		if(letterList.size() > 0) {
			
			int size = letterList.size(); // Grab the initial size of the LinkedList so as to keep the size static as 
			//									the List changes.
			
			/*
			 * Iterate through all the letters, check to see if it's a word; if so, add it to the list; if not, recurse
			 * with the current word (including the added letter) and the new letter list.
			 */
			
			for(int i = 0; i<size; i++) {
				
				char temp = letterList.poll(); // A temporary variable for the head of the queue.
				String wordTemp = word; // A snapshot of the previous word before the letter is added.
				Queue<Character> newList = new LinkedList<Character>(); // To make sure the previous queue isn't messed
				//															with.
				newList.addAll(letterList); // Put all of the letter in the previous queue in the new one.
				letterList.add(temp); // Shuffle the polled letter to the end of the queue.
				word += temp; // Add the letter to the partial word.
				
				/*
				 * Checks to see if the current word is a word, and if so, adds it to the word list.
				 */
				
				if(isWord(word) && !wordList.contains(word)) {
					
					wordList.add(word);
				}
				
				wordList.addAll(wordFinder(newList, word)); // Recurse with the new word and letter list.
				word = wordTemp; // Change the word back to the original for the next iteration.
			}
		}
		
		return wordList;	// Regardless, return the word list at the end.
	}
	
	/**
	 * Checks to see if the inputed word is an English word. First checks a local file to see if the word is on either
	 * the accepted or unaccepted list. If it is, it returns true or false, respectively, if not, it asks the user.
	 * 
	 * @param word Word being checked
	 * @return The validity of the word.
	 */
	
	public static boolean isWord(String word) {
		
		File file = new File("accepted_and_unaccepted_words.txt"); // Original file.
		boolean fileExists = file.exists(); // Checks to see if the file exists.
		
		/*
		 * If the original file doesn't exist, then create one and set the file existence to true.
		 */
		
		if(!fileExists) {
			
			file = new File("accepted_and_unaccepted_words.txt");
			fileExists = true;
		}
		
		ArrayList<String> tempFileLines = new ArrayList<String>(); // Stores the lines of the file to be rewritten in
		//																a temp file.
		BufferedReader br; // Buffered reader for the file.
		
		/*
		 * Attempts to open the file and read and write from it.
		 */
		
		try {
			
			/*
			 * Sets up the BufferedReader.
			 */
			
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			/*
			 * Reads the first line and adds it to the temp file ArrayList.
			 */
			
			String line = br.readLine();
			tempFileLines.add(line);
			
			/*
			 * Goes through each word in the white list and checks to see if the word given is any of them. It stops
			 * if the line is null (EOF) or if the line reads "UNACCEPTED", because that would be the end of the 
			 * white list.
			 */
			
			while(line != null && !(line.equals("UNACCEPTED"))) {
				
				/*
				 * If the current line equals the given word, close the input stream and return true.
				 */
				
				if(line.equals(word)) {
					
					br.close();
					return true;
					
				/*
				 * Otherwise, read in the next line and add it to the ArrayList buffer.
				 */
					
				} else {
				
					line = br.readLine();
					tempFileLines.add(line);
				}
			}
			
			/*
			 * After reading through the white list, start reading through the blacklist to see if the given word
			 * matches any of them.
			 */
			
			line = br.readLine();
			tempFileLines.add(line);
			
			/*
			 * Read through each line of the blacklist and end if you get to a null line (EOF).
			 */
			
			while(line != null) {
				
				/*
				 * If the current line equals the given word, close the input stream and return false.
				 */
				
				if(line.equals(word)) {
					
					br.close();
					return false;
					
				/*
				 * Otherwise, read in the next line and add it to the ArrayList buffer.
				 */
					
				} else {
					
					line = br.readLine();
					tempFileLines.add(line);
				}
			}
			
			/*
			 * If the word does not appear on the entire list, ask the user if it is a word.
			 */
			
			System.out.println("Is \"" + word + "\" a word?(y/n)");
			Scanner scan2 = new Scanner(System.in);
			String input = scan2.nextLine();
			File tempFile;
			
			/*
			 * Create a temp file to write to.
			 */
			
			if(fileExists) {
				
				tempFile = new File("accepted_and_unaccepted_words.txt");
			} else {
				
				tempFile = new File("accepted_and_unaccepted_words2.txt");
			}
			
			PrintStream ps = new PrintStream(new FileOutputStream(tempFile)); // Set up the file output
			
			/*
			 * If the response is yes, add the word to the end of the white list.
			 */
			
			if(input.equals("y")) {
				
				/*
				 * Go through the entire ArrayList of lines from the original file.
				 */
				
				for(int i = 0; i<tempFileLines.size()-1; i++) {
					
					/*
					 * If it's at the end of the white list... (had to add the second clause to the boolean statement
					 * because it kept trying to access a value after the end of the list).
					 */
					
					if(tempFileLines.get(i).equals("") && i != tempFileLines.size()-2) {
						
						/*
						 * Print the new word on the file (Added this to make sure it wasn't at the end of the black 
						 * list).
						 */
						
						if(tempFileLines.get(i+1).equals("UNACCEPTED")) {
							
							ps.println(word);
							ps.println();
						}
						
					/*
					 * Otherwise, print the next line in the ArrayList.
					 */
						
					} else {
						
						ps.println(tempFileLines.get(i));
					}
				}
				
				/*
				 * Afterwards, close the print stream and return true.
				 */
				
				ps.close();
				return true;
				
			/*
			 * If the response is no, add the word to the end of the black list.
			 */
				
			} else if(input.equals("n")) {
				
				/*
				 * Go through the ArrayList with the lines from the original file, and print them all on the file 
				 * until it gets to the end.
				 */
				
				for(int i = 0; i<tempFileLines.size()-1; i++) {
						
					ps.println(tempFileLines.get(i));
				}
				
				/*
				 * After printing the rest of the file, print the blacklisted word.
				 */
				
				ps.println(word);
				
				/*
				 * Then close the print stream and return false.
				 */
				
				ps.close();
				return false;
			}
			
			/*
			 * Close the reader and the print stream (for continuity's sake).
			 */
			
			br.close();
			ps.close();
			
		/*
		 * Print the stack trace if any errors occur.
		 */
			
		} catch(IOException e) {
			
			e.printStackTrace();
			return false;
		} 
		
		/*
		 * Try to close the reader and return false (also for continuity).
		 */
		
		try {
			br.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return false;
	}

}
