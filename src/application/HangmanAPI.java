//⚡ NTUA ECE, MediaLab 2022
// Kitsos Orfanopoulos el17025

// ✔ This class follows javadoc documentation protocol

package application;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

import java.io.IOException;


public final class HangmanAPI {
	
  	//⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜
 	//#############--  Private Attributes  --########

  	private String[] wordList;	// dictionary from file
	private String secretWord;	// a random selected secret Word for hangman game
	
	private ArrayList<Character> correctList;   // list of correct-unknown letters 	(example: _ _ _ _, K _ _ G , K I N G)
	private ArrayList<Boolean> boolList;        // list of true-false per letter 	(example: F F F F, T F F T , T T T T)
	
	static int correctMoves; 	// number of correct moves	
	static int wrongMoves;		// number of wrong moves
	static int totalPoints;		// total points
	
	// Getters and Setters (fix static access way)
	private void 	setCorrectMoves(int a)	{ correctMoves = a;}
	private void	setWrongMoves(int a)	{ wrongMoves = a;}
	private void	setTotalPoints(int a)	{ totalPoints = a;}
	public int 		getCorrectMoves()	{return correctMoves;}
	public int 		getWrongMoves()		{return wrongMoves;}
	public int 		getTotalPoints()	{return totalPoints;}
	



  	//🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
  	//#############--  Constructor  --################

	/**
	* The constuctor HangmanAPI creates an object that accepts request for the hangman game.
	* @param filename name of the file of the dictionary that shall be imported
	*/
	public HangmanAPI(Path filename)  {                          

		// initialize
		wrongMoves = 0;
		totalPoints = 0;
		correctMoves = 0;
		
		// read words from stored dictionary and create wordList
		List<String> lines;
		try {
			lines = Files.readAllLines(filename, StandardCharsets.UTF_8);
			this.wordList = lines.toArray(new String[lines.size()]);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Generate a random index and choose a random word for wordList
		int index = (int)Math.floor(Math.random()*(wordList.length));
		this.secretWord = wordList[index];
		
		// Remove secretWord from wordList
		// Note: it is easier to erase an element from a list than from an array, thus
		// we create a duplicate list first, we erase the word, and then we convert it back to array.
		List<String> list = new ArrayList<String>(Arrays.asList(wordList)); 
		list.remove(index); // remove
		this.wordList = list.toArray(new String[0]);
		
		// initialize correctList. All letters ar unknown (_ _ _ _)
		this.correctList = new ArrayList<Character>();
		for (int i = 0; i < this.secretWord.length(); i++)
			this.correctList.add('_');  

		// initialize boolList. All flags are false (F F F F)
		this.boolList = new ArrayList<Boolean>();
		for(int i=0; i<this.secretWord.length(); i++) { 
			this.boolList.add(i, false);
		}
	}
	

  	//🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩
  	//#############--  Public Getters  --################
	
	//🟨 
	/**
	 * This getter method returns secretWord
	 * @return secretWord as a string
	 */
	public String getSecretWord() { 	
		return this.secretWord;	
	}

	//🟨🟨
	/**
	 * This getter method returns size of secretWord
	 * @return secretWord size as int
	 */
	public int getSecretWordSize() {
		return this.secretWord.length();
	}

	//🟨🟨🟨
	/**
	 * This getter method returns boolList
	 * @return boolList
	 */
	public ArrayList<Boolean> getBoolList() { 
		return this.boolList; 
	}
	
	//🟨🟨🟨🟨
	/**
	* This getter method returns the percentage of correct choices per word
	* @return percentage of correct choice per word
	*/
	public String correctPercentage() {
		float per = 100*(float) getCorrectMoves()/(float) this.secretWord.length();
		return String.valueOf(per)+"%";
	}

	//🟨🟨🟨🟨🟨
	/**
	* This getter method returns TotalPoints as string
	* @return TotalPoints as string
	*/
	public String getTotalPointsString() { 
		return String.valueOf(getTotalPoints()); 
	}

	//🟨🟨🟨🟨🟨🟨
	/**
	* This getter method returns wordList length as string
	* @return wordList length as string
	*/
	public String wordListLengthString() { 
		return String.valueOf(this.wordList.length); 
	}

	//🟨🟨🟨🟨🟨🟨🟨
	/** 
	 * This getter method returns wordList length as int
	* @return wordList length as int
	*/
	public int wordListLength() {
		return this.wordList.length;
	}



	//🟥🟥🟥🟥🟥🟥🟥🟥🟥🟥🟥🟥🟥🟥🟥🟥🟥🟥🟥🟥🟥🟥
  	//#############--  Public Clauses  --################
	//🟥🟥
	/**
	 * Method that answers if the game is won
	 * @return true if the game is won, false otherwise
	 */
	public boolean gameWon() {
		return (correctMoves == secretWord.length());
	}
	
	//🟥🟥🟥🟥
	/**
	 * Method that answers if the game is lost
	 * @return true if the game is lost, false otherwise
	 */
	public boolean gameLost() { 
		return (getWrongMoves() >= 6); 
	}
	
	//🟥🟥🟥🟥🟥🟥
	/**
	 * Method that answers if the game is over
     * @return true if the game is won or lost, false otherwise
     */
    public boolean gameOver() { 
		return (gameLost() || gameWon()); 
	}
	
	

  	//🟪🟪🟪🟪🟪🟪🟪🟪🟪🟪🟪🟪🟪🟪🟪🟪🟪🟪🟪🟪🟪🟪
  	//#############--  Public Methods  --################
	
	//🟪🟪
	/**
	* This method returns the correctList as string
	* @return correctList as string
	*/
	public String correctListString() {
		StringBuilder sb = new StringBuilder();

		for (Character ch : this.correctList) { // if unknown then "_"
			sb.append(ch);
			sb.append(" ");
		}
		String string = sb.toString();	
		return string;
	}

  	//🟪🟪🟪🟪
	/**
	 * This method is used everytime the player makes a new move.
	 * The user gives a character and its position. We check if the letter is correct or not.
	 * @param stringLetter the guessed letter as string (user inputs strings)
	 * @param stringIndex the position of the guessed letter as string (users inputs strings)
	 */
	public void nextMove(String stringLetter , String stringIndex) {
		//convert strings
		char letter = stringLetter.charAt(0);
		int index = Integer.parseInt(stringIndex);
		
		// if move is correct then update correctList/boolList/totalPoints and remove not compatible words from wordList
		if((this.secretWord.charAt(index) == letter) && (this.boolList.get(index) == false)){ // guess a correct character for the first time
            setCorrectMoves(getCorrectMoves()+1); 	// correctMoves++
			this.correctList.set(index, letter); 	// correctList[index] = letter (example: with (K,0) [K _ _ _])    
			this.boolList.set(index , true);		// same with boolList [T F F F]
			removeWords(letter, index, true);
			setTotalPoints(getTotalPoints()+newPoints(letter,index)); //totalPoints += newPoints(letter,index)
		}

		// if move is wrong then update wrongMoves/totalPoints and remove not compatible words from wordList 
		else {   	
			setWrongMoves(getWrongMoves()+1); //wrongMoves++             	                     
			removeWords(letter, index, false);
			if (getTotalPoints()>=15) setTotalPoints(getTotalPoints()-15); //totalPoints -= 15
			else setTotalPoints(0);
		}
	}

  	//🟪🟪🟪🟪🟪🟪
	/**
	 * This method returns the statistics for the current dictionary;
	 * how many words with 6, 6-9 or 10+ letters exist.
	 * @return an array with 3 values, the percentage for every interval
	 */ 
	public ArrayList<Float> dictionaryStats(){
		
		int letters_6 = 0;
		int letters_7_to_9 = 0;
		int letters_10_plus = 0;
		
		for(String word: this.wordList) {
			if(word.length() > 9 ) {
				letters_10_plus++;
			}
			else if( word.length() >6) {
				letters_7_to_9++;
			}
			else {
				letters_6 ++ ;
			}
		}
		ArrayList<Float> stats = new ArrayList<Float>();
		stats.add((float)100*(float) letters_6 / (float) this.wordList.length );
		stats.add((float)100*(float) letters_7_to_9 / (float) this.wordList.length);
		stats.add((float)100*(float) letters_10_plus / (float) this.wordList.length);
		
		return stats;
	}
	
  	//🟪🟪🟪🟪🟪🟪🟪🟪
	/**
	 * This method creates a suggestion message string containing 
	 * the SortedChoiceList for every unknown letter
	 * @return a message string with one suggestion per line for each unknown letter 
	 */
	public String letterSuggestions() {
		
		//add suggestion string per letter
		ArrayList<String> suggestionPerLetter = new ArrayList<String>();
	  	for(int i=0; i< this.secretWord.length(); i++) {
	  		if( this.boolList.get(i) == false){ //unknown letter
	  			StringBuilder sb = new StringBuilder();
		        for(Character ch : createSortedChoiseList(i)) {
		            sb.append(ch);
		            sb.append(" ");
		        }
		        String string = sb.toString();  		 
	  			suggestionPerLetter.add(string);
	  		}  		
	  	}
		// add index of unknown letters
		ArrayList<Integer> indexArray = new ArrayList<Integer>();
		for(int i=0; i<boolList.size(); i++){ 
			if(boolList.get(i)== false){ 
				indexArray.add(i+1); // i+1, user-friendly enumeration (starts from 1)
			}
		}
		// combine indexes with suggestions
		StringBuffer st = new StringBuffer();
	  	st.append("Possible choices for every unknown letter (higher possibility to the right):\n----------------------------------------------\n");
	  	int index=0;
	  	for(String s: suggestionPerLetter) {
			  st.append(Integer.toString(indexArray.get(index)) + ".\t" + s+"\n");
			  index++;
	    }
	  	String suggestionString = st.toString();
	 	return suggestionString;      
	}
	



  	//🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧🟧
  	//#############--  Private Methods  --################

	//🟧🟧
	/**
	 * This method is used to create an list with every letter to choose for Hangman game.
	 * The list of letters is sorted by the probability of this specific letter to be a correct choice (descending order).
	 * @param index the position of a letter in the secretWord
	 * @return ArrayList of all letters of the alphabet, sorted in descending order by the probability of each specific letter to be correct. 
	 */
	private ArrayList<Character> createSortedChoiseList(int index) {     // for the index letter, not the main index 
	      
		int sameLetters; 		// count how many known letters each word have in common with the secretWord
		int compatibleWords;	// count compatible words (compatible according to Hangman game)
		Character[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
		List<Tuple<Character,Float>> letterTuples = new ArrayList<Tuple<Character,Float>>();   
		
		// comparator is java interface. We implement it especialy for our Tuples
		Comparator<Tuple<Character, Float>> tupleCompare = new Comparator<Tuple<Character,Float>>(){
			public int compare(Tuple<Character,Float> tupleA, Tuple<Character,Float> tupleB){
				if (tupleA.getIndex()<tupleB.getIndex()) return -1;
				else if (tupleA.getIndex()==tupleB.getIndex()) return 0;
				else return 1;
			}
		};
		
		// for every letter of the alphabet:
		for(Character letter:alphabet ) {
			compatibleWords = 0;
			// we check if a word in the wordList has the same boolList (ex. T T F T)
			for(String word: this.wordList) {
				sameLetters = 0;
				if( (word.length() == this.secretWord.length()) && ( word.charAt(index) == letter ) ) {
					for(int i=0; i< this.secretWord.length(); i++) {
						if( (this.boolList.get(i) == true) && (word.charAt(i) == this.secretWord.charAt(i) )) { 
							sameLetters++;
						}    
					}
					if(sameLetters == getCorrectMoves()) { 
						compatibleWords++;
					}
				}
			}
			// calculate probability and add to tuple list.
			float letterProbability = ((float) compatibleWords) / ((float) this.wordList.length);
			letterTuples.add(new Tuple<Character,Float>(letter , letterProbability));
		}	
		//sort
		Collections.sort(letterTuples, tupleCompare);
		
		// convert tuple list to single arrayList of data
		ArrayList<Character> sortedlist = new ArrayList<Character>();
		for (Tuple<Character, Float> t : letterTuples){  
			sortedlist.add(t.getData());
		}
		return sortedlist;                
	} 

	//🟧🟧🟧🟧
	/**
	 * This method removes not compatible words whenever a new move is made.
	 * If the choice was correct then we update the wordList by removing words with not common letters in that index,
	 * else if the choice was wrong we update by removing words with common letters in tha index.
	 * then remove the wordList from lexicon that don't have the same letter in same position
	 * @param letter 
	 * @param index of that letter
	 * @param flag true, if the choice was correct, false otherwise
	 */
	private void removeWords(char letter, int index, boolean flag) {
		
		List<String> wordslist = new ArrayList<String>(Arrays.asList(this.wordList));
		for(String word: this.wordList) {
			if(word.length() == this.secretWord.length()){
				if (flag == true){ // correct choice
					if (word.charAt(index) !=  letter ) { 
						wordslist.remove(word);
					}
				}
				else{ // wrong choice
					if (word.charAt(index) ==  letter ) { //  same length && different index letter
						wordslist.remove(word);
					}
				}
			}  
		} 	  	    			
		this.wordList = wordslist.toArray(new String[0]);		
	}	
	


	//🟧🟧🟧🟧🟧🟧
	/**
	 * This method computes the points gained every time a new letter is found.
	 * @param letter letter found successfully
	 * @param index position of letter successfully found
	 * @return result int of new points
	 */
	private int newPoints(char letter, int index) {
		int sameLetters;
		int compatibleWords=0;
		
		// FIXME: duplicate check (see createSortedChoiseList)
		// we check if a word in the wordList has the same boolList (ex. T T F T)
		for(String word: this.wordList) {
			sameLetters = 0;
			if( (word.length() == this.secretWord.length()) && ( word.charAt(index) == letter ) ) {
				for(int i=0; i< this.secretWord.length(); i++) {
					if( (this.boolList.get(i) == true) && (word.charAt(i) == this.secretWord.charAt(i))) { 
						sameLetters++;
					}    
				}
				if(sameLetters == getCorrectMoves()) { 
					compatibleWords++;
				}
			}
		}
		float probability = ((float) compatibleWords) / ((float) this.wordList.length);
		int result=0;
		if		(probability >=0.6f) 	{result= 0;}
		else if (probability >= 0.4f) 	{result=10;}
		else if (probability >=0.25f) 	{result=15;}
		else 							{result=30;}
		
		return result;
	}
	


	//🟪🟦🟪🟦🟪🟦🟪🟦🟪🟦🟪🟦🟪🟦🟪🟦
  	//#############--  Main  --###############
	public static void main(String [] args) {} // nothing
	
}