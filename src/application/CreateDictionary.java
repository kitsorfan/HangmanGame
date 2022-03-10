//⚡ NTUA ECE, MediaLab 2022
// Kitsos Orfanopoulos el17025

// ✔ This class follows javadoc documentation protocol

package application;


import java.io.InputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import java.net.URL;
import java.net.HttpURLConnection;

import org.json.JSONObject; 
import org.json.JSONException;


/**
* This is a program for creating a dictionary, containing words for the Hangman game.
* Words are extracted from a json file obtained via openlibrary API.
* @author kitsorfan
*/
public final class CreateDictionary {

  //⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜
  //#############--  Private Attributes  --########

  private String description;       // description as shown in JSON file
  private List<String> listOfWords; // list of words extracted from description
  private String errors;            // errors if list of words is not eligible for the dictionary
   


  //🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦
  //#############--  Constructor  --################
	/**
   * The constructor initializes description, listOfWords and errors
   * @param OLID is the open-library-id of the book, @see https://openlibrary.org/dev/docs/api/books
   */
  public CreateDictionary(String OLID){
    
    this.description = getJSONDescription("https://openlibrary.org/works/"+OLID+".json");  // get description value from JSON file
    this.listOfWords = extractListOfWords(this.description);  // create list of words from the above desciption string
    this.errors = checkConstraints(this.listOfWords);  // check if the above list is eligible for creating a dictionary

  }

  //#############--  Methods  --################


  //🟧🟧
  /**
   * This is a function that takes a url string, creates an http request, which obtains a JSON
   * extracts the value of "description" tag of that JSON, and returns a string
   *  
   * @param urlString the url of http request (Open-library API)
   * @return the description string
   */
  private String getJSONDescription(String urlString) {
	String result = null;
	String json = null;
	try {
		URL url = new URL(urlString);     // create url object 
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    InputStream inStream = connection.getInputStream();
	    Scanner scanner = new Scanner(inStream, "UTF-8");
	  	json = scanner.useDelimiter("\\Z").next();
	  	scanner.close();
	}
    catch (IOException ex) { 
    	ex.printStackTrace();
     }
    try {

    	
    	result = new JSONObject(json).getJSONObject("description").getString("value");   
    } 
    catch (JSONException e){;
    }
    return result;
  }
  

  //🟥🟥🟥🟥
  /**
   * This is a function that takes a description string and returns a list of words.
   * It also removes any punctuation and converts to capital letters
   *  
   * @param initial the initial description text (punctuation included)
   * @return eligibleWordList the list of capital words without punctuation  
   */   
  private List<String> extractListOfWords(String initial) {
	 if (initial==null) return null;
	 String cleanText = initial.replaceAll("\\p{Punct}", "");   // remove punctuation 
	 String cleanTextCapital = cleanText.toUpperCase();         // convert to capital
	 String[] wordList= cleanTextCapital.split("\\s");          // split text to words
	 List<String> eligibleWordList = new ArrayList<String>();   
	  for(String word:wordList){  
		  if(word.length() >5  && !(eligibleWordList.contains(word))){ // we only add 5+ letter unique words
			  eligibleWordList.add(word);
		  }
    }
	 return eligibleWordList;
  }


  //🟧🟧🟧🟧🟧🟧
  /**
   * This is a function that checks the extra constraints for the Dictionary.
   * 20% of the words must have 9 letters or more.
   * We need 20 or more words in our list.
   *  
   * @param wordList the candidate list of words
   * @return result a message string 
   */ 
  private String checkConstraints( List<String> wordList) {
	String result = null;
    if (wordList==null) return "Error: Description with value not found";
	if( wordList.size() < 20 ) {
      result = "Error: Eligible words are less than 20.";
    }	  
	  else { 
      int count = 0;
      for(String word:wordList){  
        if(word.length() >= 9){ 
          count++;
        }
      }  
	   if((float)count >= 0.2f*((float)wordList.size())) {
       result = "Success: List is ok - Have fun!";
      } 
	   else { 
       result =  "Error: Too few words with 9+ letters.";
      }
	  } 
    return result;
  } 
  

  // 🟥🟥🟥🟥🟥🟥🟥🟥
  /**
   * This is a void function that stores our wordList to a file inside a specific medialab folder.
   * It creates a file with given filename.txt
   * @param wordList list of eligible words
   * @param filename name for the new file
   */
  public void saveFile(List<String> wordList,String filename) throws IOException{
    // create new file
    File targetFile = new File("C:\\Users\\kitso\\medialab\\"+filename+".txt");
    File parent = targetFile.getParentFile();
    if (parent != null && !parent.exists() && !parent.mkdirs()) {
        throw new IllegalStateException("Couldn't create dir: " + parent);
    }
    // write the data
    FileWriter fw;
    try {
      fw = new FileWriter(targetFile.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      for(String word: wordList ) { 
        bw.write(word); 
        bw.write('\n');
      }
      bw.close();
    }
    catch (IOException e) { e.printStackTrace();}
  }  
  

  //🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨🟨
  //#############--  Getters  --###################
  
  public String       getDescription()  { return this.description; }
  public List<String> getListOfWords()  { return this.listOfWords; }
  public String       getErrors()       { return this.errors; }

  //🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩🟩
  //#############--  Main  --###################
  /**
   * main does nothing
   */
  public static void main(String[] args) {
     // example:
//     CreateDictionary p = new CreateDictionary("OL278022W");
//     System.out.println(p.getErrors());
//     List<String> dictionary = (p.getListOfWords());
//     try {
//     p.saveFile(dictionary, "dictionary");
//     }
//     catch (IOException ex) { 
//     	ex.printStackTrace();
//      }
    	 
  }
}
  
// other examples JSON of OPEN LIBRARY API:
//https://openlibrary.org/works/OL118135W.json    -> less than 20 eligible words
//https://openlibrary.org/works/OL33891821M.json  -> too small words
//https://openlibrary.org/books/OL278022W.json    -> is ok!