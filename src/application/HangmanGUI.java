//⚡ NTUA ECE, MediaLab 2022
// Kitsos Orfanopoulos el17025

// ❕ This class follows javadoc documentation protocol

// This class works as the frondend, where HangmanAPI is the backend

package application;

import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.geometry.Insets;
import javafx.fxml.FXML;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.*;
import java.util.concurrent.TimeUnit; // for sleep

public class HangmanGUI {
	
	// ########### --- Variables --- ###########

	 HangmanAPI hangman; // hangman object (see HangmanAPI)
	 Path dictionaryPath = Paths.get("C:\\Users\\kitso\\medialab\\dictionary.txt"); // path for default dictionary

	 static int rounds =0; // rounds of game
	 
	 static ArrayList<String> 	secretWords = new ArrayList<String>();  // list of past secretWords
	 static ArrayList<Integer> 	totalMoves = new ArrayList<Integer>();  // total moves (correct + wrong)
	 static ArrayList<String> 	gamesWon = new ArrayList<String>();		// number of victorious games
	 
	 
	 // Menu Bar
	 // Application
	 @FXML private MenuItem menuA1; // start new game
	 @FXML private MenuItem menuA2; // load dictionary	
	 @FXML private MenuItem menuA3;	// create new dictionary
	 @FXML private MenuItem menuA4;	// exit hangman
	 
	 //Details
	 @FXML private MenuItem menuB1;	// dictionary statistics
	 @FXML private MenuItem menuB2;	// game statistics
	 @FXML private MenuItem menuB3;	// spoil the solution
    
	 //Dropdown
	 @FXML
     public  ComboBox<String> dropdown_letter ;	 	// dropdown with alphabet
     public  ComboBox<String> dropdown_position ;	// dropdown for position 

     @FXML private Label textBox1;	// Dictionary cardinality
     @FXML private Label textBox2;	// Total points
     @FXML private Label textBox3;	// Correct choices percentage
     @FXML private Label textBox4;	// correctList (see HangmanAPI)
     @FXML private Label textBox5;	// letter suggestions
     
     @FXML private Button button;	// main button for next move
     @FXML private AnchorPane anchor;			// general anchor handler
     @FXML private AnchorPane rightAnchorPane;  // right anchor
     
     @FXML private ImageView hangman_image;		// main image of hangman progress
	 
	 
	 // ########### --- Handle Dropdowns --- ########### 
	 @FXML //letter dropdown
	 public void clicked_dropdown_letter(MouseEvent event) {
		 
		 String alphabet[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
		 ObservableList<String> letterList = FXCollections.observableArrayList(alphabet);
		 dropdown_letter.setItems(letterList);
		 
	 }
    @FXML //position dropdown
    public void clicked_dropdown_position(MouseEvent event) {

		// take boolList from hangman and check if we have found some
    	ArrayList<Boolean> positions = hangman.getBoolList();

    	String[] arr= new String[positions.size()];
    	Arrays.fill(arr, "OK");

    	for(int i=0; i<positions.size(); i++) {
    		if(positions.get(i) == false) {
    			arr[i] = String.valueOf(i+1); // user friendly enumeration starts from 1
    		}
    	}
    	
     	ObservableList<String> positionList = FXCollections.observableArrayList(arr);
     	dropdown_position.setItems(positionList);	
    }
     
	 // ########### --- Handle Main Button --- ########### 
    @FXML
    public void button_pressed(ActionEvent event) {

		// get values from dropdowns
    	String	index = dropdown_position.getSelectionModel().getSelectedItem().toString();
		int temp = Integer.parseInt(index); // fix enumeration problem, computer array starts from 0
		index = Integer.toString(temp-1);
    	String letter = dropdown_letter.getSelectionModel().getSelectedItem().toString();

		// make next move
    	hangman.nextMove(letter, index);

		// update image of progress
		File file = null;
		switch(hangman.getWrongMoves()) {
		case 0:	
			file = new File("C:\\Users\\kitso\\eclipse-workspace\\Hangman\\src\\application\\images\\BadChoice0.png");
			break;	    	
		case 1:
			file = new File("C:\\Users\\kitso\\eclipse-workspace\\Hangman\\src\\application\\images\\BadChoice1.png");
			break;
		case 2:
			file = new File("C:\\Users\\kitso\\eclipse-workspace\\Hangman\\src\\application\\images\\BadChoice2.png");				
			break;
		case 3:
			file = new File("C:\\Users\\kitso\\eclipse-workspace\\Hangman\\src\\application\\images\\BadChoice3.png");
			break;
		case 4:
			file = new File("C:\\Users\\kitso\\eclipse-workspace\\Hangman\\src\\application\\images\\BadChoice4.png");
			break;
		case 5:
			file = new File("C:\\Users\\kitso\\eclipse-workspace\\Hangman\\src\\application\\images\\BadChoice5.png");
			break;	    	
		case 6:
			file = new File("C:\\Users\\kitso\\eclipse-workspace\\Hangman\\src\\application\\images\\BadChoice6.png");
			break;	    	
	    }
		hangman_image.setImage(new Image(file.toURI().toString()));
    	

		if(!hangman.gameOver()) { // if round is not over update texts
			textBox1.setText("Total words in Dictionary: \t"+ hangman.wordListLengthString());
			textBox2.setText("Total Points: \t" + hangman.getTotalPointsString());
			textBox3.setText("Correct guesses percentage:   "+ hangman.correctPercentage());
			textBox4.setText(hangman.correctListString());
			textBox5.setText(hangman.letterSuggestions());
    	}
    
    	// round is over
    	else {
			rounds++;
			secretWords.add(hangman.getSecretWord());
			totalMoves.add(hangman.getWrongMoves() +hangman.getCorrectMoves());
    		if(hangman.getCorrectMoves() < hangman.getSecretWordSize()) {
    			
				// update progress
    			gamesWon.add("Failure");
    			
    			
    			//Failure
				// Group root = new Group();
    			Stage failStage = new Stage();
				failStage.setTitle("FAILURE :(");
    	    	Label t = new Label("You got hanged!" + "\n" + "You couldn't find the word:   " + hangman.getSecretWord()+"\n\n Press start new game for a next round!");
    	        Image icon = new Image("C:\\Users\\kitso\\eclipse-workspace\\Hangman\\src\\application\\images\\hangman.png");
				failStage.getIcons().add(icon);
    	        HBox box = new HBox(5.0D);
    	        box.setPadding(new Insets(25.0D, 5.0D, 5.0D, 50.0D));
    	        box.getChildren().addAll(t);
    	        Scene scene = new Scene(box, 595.0D, 150.0D, Color.BEIGE);
    	        failStage.setScene(scene);
    	        failStage.show();
    	      
    			
    		}
    		else {
    			// update progress
    			gamesWon.add("Victory");
    			
    			//Victory
    			Stage victoryStage = new Stage();
				victoryStage.setTitle("VICTORY :)");
				Image icon = new Image("C:\\Users\\kitso\\eclipse-workspace\\Hangman\\src\\application\\images\\hangman.png");
				victoryStage.getIcons().add(icon);
    	    	Label t = new Label("Victory!" + "\n Ready for next round? Press start new game! ");
    	        HBox box = new HBox(5.0D);
    	        box.setPadding(new Insets(30.0D, 8.0D, 8.0D, 60.0D));
    	        box.getChildren().addAll(t);
    	        Scene scene = new Scene(box, 600.0D, 150.0D, Color.GREEN);
    	        victoryStage.setScene(scene);
    	        victoryStage.show();
    		}
    	}
    	
    }
    
	// ########### --- Handle Menu Buttons --- ########### 
    @FXML // start new game
    void menuA1_action(ActionEvent event) { 
		File file = new File("C:\\Users\\kitso\\eclipse-workspace\\Hangman\\src\\application\\images\\BadChoice0.png");
        hangman_image.setImage(new Image(file.toURI().toString()));
		hangman = new HangmanAPI(dictionaryPath); // restart

    	textBox1.setText("Total words in Dictionary:   "+ hangman.wordListLengthString());
    	textBox2.setText("Total Points:   " + hangman.getTotalPointsString());
    	textBox3.setText("Correct guesses percentage:   "+ hangman.correctPercentage());
    	textBox4.setText(hangman.correctListString());
    	textBox5.setText(hangman.letterSuggestions());
    
    }   
    
    
    @FXML // load a dictionary
    void menuA2_action(ActionEvent event) {  
			
    		Stage stage = new Stage();
	        TextField tf = new TextField();
	    	Label t = new Label();
	        Button button = new Button("OK");
	        Label id = new Label("Type dictionary filename: ");
			Image icon = new Image("C:\\Users\\kitso\\eclipse-workspace\\Hangman\\src\\application\\images\\hangman.png");
			stage.getIcons().add(icon);
	        HBox box = new HBox(5.0D);
	        box.setPadding(new Insets(30.0D, 8.0D, 8.0D, 60.0D));
	        box.getChildren().addAll(id, tf, button, t);
	        Scene scene = new Scene(box, 800.0D, 200.0D, Color.WHITE);
	        stage.setTitle("Load dictionary");
	        stage.setScene(scene);
	        stage.show();
		
	        button.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					String filename = tf.getText();
					dictionaryPath = Paths.get("C:\\Users\\kitso\\medialab"+filename+".txt");
					
					if (Files.exists(dictionaryPath)) {   // dictionary exists
						t.setText("Dictionary loaded correctly.\nClose this window and press start a new game!");
					}
					else {
						t.setText("Error: dictionary doesn't exist");
					}
				}
	    	});
    }   
    
    @FXML // create new dictionary
    void menuA3_action(ActionEvent event) {
    	Stage stage = new Stage();
	        TextField inputOLID = new TextField();
	        TextField inputFilename = new TextField();

	        Label t = new Label();
	        
	        Button button = new Button("OK");
	        Label lib_id = new Label("Open Library ID of book: ");
	        Label dic_id = new Label("FIlename of new dictionary : ");
        
	        HBox box = new HBox(5.0D);
	        box.setPadding(new Insets(25, 0, 0, 5));      //top, bottom, right, left
	        box.getChildren().addAll(lib_id, inputOLID, dic_id , inputFilename, button , t);
	        
	        Scene scene = new Scene(box, 800.0D, 180.0D, Color.CYAN);
			Image icon = new Image("C:\\Users\\kitso\\eclipse-workspace\\Hangman\\src\\application\\images\\hangman.png");
			stage.getIcons().add(icon);
	        stage.setTitle("Create new dictionary");
	        stage.setScene(scene);
	        stage.show();
	        
	        button.setOnAction(new EventHandler<ActionEvent>() {
	    	    public void handle(ActionEvent e) {
		    	    String OLID = inputOLID.getText();
		    	    String filename = inputFilename.getText();		    	    
		    	    CreateDictionary p = new CreateDictionary(OLID);
		    		
		    	    if(p.getErrors() == "Success: List is ok - Have fun!") { 
		    			try {
							p.saveFile(p.getListOfWords(), filename); 
							t.setText( "Dictionary created succesfully. \n Please close this window.");
		    			}
		    			catch (IOException e1) {e1.printStackTrace();}
		    		}
		    		else {t.setText("Try Again"+p.getErrors());}
	    	    }	
	        });
    }

    @FXML // exit
    void menuA4_action(ActionEvent event) { System.exit(0); }   

    @FXML // Dictionary Statistics
    void menuB1_action(ActionEvent event) {
    	Stage stage = new Stage();
		Image icon = new Image("C:\\Users\\kitso\\eclipse-workspace\\Hangman\\src\\application\\images\\hangman.png");
		stage.getIcons().add(icon);
        Label id = new Label("Percentage of words with:\n----------------------\n6 letters:\t\t\t" +hangman.dictionaryStats().get(0) +" %"+"\n" + "7 to 9 letters:\t\t" +hangman.dictionaryStats().get(1) +" %"+ "\n" + "10+ letters:\t\t" + hangman.dictionaryStats().get(2) +" %");
        HBox box = new HBox(5.0D);
        box.setPadding(new Insets(40.0D, 10.0D, 10.0D, 80.0D));
        box.getChildren().addAll(id);
        Scene scene = new Scene(box, 600.0D, 200.0D, Color.WHITE);
        stage.setTitle("Dictionary Statistics");
        stage.setScene(scene);
        stage.show();
       
    }   

    @FXML // Game statistics
    void menuB2_action(ActionEvent event) { 
    	int rounds= secretWords.size()-5;
    	Stage stage = new Stage();
		Image icon = new Image("C:\\Users\\kitso\\eclipse-workspace\\Hangman\\src\\application\\images\\hangman.png");
		stage.getIcons().add(icon);
		stage.setTitle("Last 5 Rounds Statistics");
		HBox box = new HBox(5.0D);
		box.setPadding(new Insets(25.0D, 5.0D, 5.0D, 50.0D));

    	try {
			Label id1 = new Label("Secret Word:\t\nTotal Moves:\t\nResult:\t\n");
			Label id2 = new Label(secretWords.get(rounds+0) + "\n" + String.valueOf(totalMoves.get(rounds+0)) + "\n" + gamesWon.get(rounds+0)+"\t");
			Label id3 = new Label(secretWords.get(rounds+1) + "\n" + String.valueOf(totalMoves.get(rounds+1)) + "\n" + gamesWon.get(rounds+1)+"\t");
			Label id4 = new Label(secretWords.get(rounds+2) + "\n" + String.valueOf(totalMoves.get(rounds+2)) + "\n" + gamesWon.get(rounds+2)+"\t");
			Label id5 = new Label(secretWords.get(rounds+3) + "\n" + String.valueOf(totalMoves.get(rounds+3)) + "\n" + gamesWon.get(rounds+3)+"\t");
			Label id6 = new Label(secretWords.get(rounds+4) + "\n" + String.valueOf(totalMoves.get(rounds+4)) + "\n" + gamesWon.get(rounds+4)+"\t");

			box.getChildren().addAll( id1, id2, id3, id4, id5, id6);
			Scene scene = new Scene(box, 595.0D, 150.0D, Color.WHITE);
			stage.setScene(scene);
			stage.show();
    	}
    	
        catch(Exception e){
	        if(rounds <0) {
	    		Label exc = new Label("Statics will appear when you complete 5+ rounds.");
	            box.getChildren().addAll( exc);
	            Scene scene = new Scene(box, 595.0D, 150.0D, Color.RED);
	            stage.setScene(scene);
	            stage.show();
	    	}
        }
    }   

    @FXML // Spoil the solution
    void menuB3_action(ActionEvent event) {     //Solution
    	
    	// According the rules this round is considered as failure, hidden word appears
    	
    	secretWords.add(hangman.getSecretWord());
		totalMoves.add(hangman.getWrongMoves() +hangman.getCorrectMoves());
		gamesWon.add("Failure");
    	
    	Stage stage = new Stage();
		Image icon = new Image("C:\\Users\\kitso\\eclipse-workspace\\Hangman\\src\\application\\images\\hangman.png");
		stage.getIcons().add(icon);
    	Label id = new Label("Secret word was:\t" + hangman.getSecretWord()+"\nThe round is considered as failure.\nYou must start a new round");

        HBox box = new HBox(5.0D);
        box.setPadding(new Insets(25.0D, 5.0D, 5.0D, 50.0D));
        box.getChildren().addAll( id);
        Scene scene = new Scene(box, 600.0D, 150.0D, Color.BEIGE);
        stage.setTitle("Cheater, cheater, pumpkin eater!");
        stage.setScene(scene);
        stage.show();
		hangman = new HangmanAPI(dictionaryPath); // restart
    }   
}

