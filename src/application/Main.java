//⚡ NTUA ECE, MediaLab 2022
// Kitsos Orfanopoulos el17025

// ❕ This class follows javadoc documentation protocol

package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("HangmanGUI.fxml")); 

			Scene scene = new Scene(root,Color.DODGERBLUE);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setTitle("The Ultimate Hangman-kitsorfan");
			Image icon = new Image("C:\\Users\\kitso\\eclipse-workspace\\Hangman\\src\\application\\images\\hangman.png");
			primaryStage.getIcons().add(icon);
			primaryStage.setFullScreen(true);
			primaryStage.setFullScreenExitHint("The Ultimate Hangman! If you wanna exit full-screen press <esc>");
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
