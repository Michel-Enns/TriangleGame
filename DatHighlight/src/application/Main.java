package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {
	int windowWidth = 400;
	int playerCount = 2;
	int gameDotCount = 6;
	@Override
	public void start(Stage primaryStage) {
		try {
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
