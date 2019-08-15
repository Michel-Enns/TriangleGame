package application;
	
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;

public class Main extends Application {
	int windowWidth = 400;
	int playerCount = 2;
	int gameDotCount = 6;
	@Override
	public void start(Stage primaryStage) {
		try {
			List<Integer> choices = new ArrayList<Integer>();
			for(int i=1; i<7;i++) {
				choices.add(i);
			}
			ChoiceDialog<Integer> dialog = new ChoiceDialog<>(2, choices);
			dialog.setTitle("Triangle Game");
			dialog.setHeaderText("Welcome to the Triangle Game");
			dialog.setGraphic(null);
			dialog.setContentText("How many players?");
			Optional<Integer> result = dialog.showAndWait();
			if(result.isPresent()) {
				playerCount = result.get();
			}
			// The minDots array stores the minimum number of dots needed so that
			// each player can draw at least three lines.  minDots[2] is for 2 players etc.
			int[] minDots = {0, 3, 4, 5, 6, 6, 7};
			// The maxDots array stores the minimum number of dots needed so that
			// each player can draw six lines.  maxDots[2] is for 2 players etc.
			int[] maxDots = {0, 4, 6, 7, 8, 9, 9};
			choices.clear();
			for(int i = minDots[playerCount]; i <= maxDots[playerCount]; i++) {
				choices.add(i);
			}
			dialog = new ChoiceDialog<Integer>(choices.get(0), choices);
			dialog.setTitle("Triangle Game");
			dialog.setHeaderText(null);
			dialog.setGraphic(null);
			dialog.setContentText("How many dots?");
			result = dialog.showAndWait();
			if(result.isPresent()) {
				gameDotCount = result.get();
			}
			
			GameRoot root = new GameRoot(playerCount);
			GameDot[] gameDots = GameDotGenerator.makeGameDots(gameDotCount, windowWidth);
			for (GameDot dot: gameDots){
				root.getChildren().add(dot);
				dot.gameRoot = root;
			}

			Scene scene = new Scene(root,windowWidth,windowWidth);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
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
