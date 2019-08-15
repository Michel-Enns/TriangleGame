package application;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class GameRoot extends BorderPane {

	boolean drawingActive = false;
	GameDot startDot;
	Line playerLine = new Line(0, 0, 0, 0);
	int playerCount;
	int currentPlayer = 1;
	Color [] playerColour = {Color.RED, Color.BLUE, Color.GREEN, Color.DEEPPINK, Color.DARKVIOLET, Color.SANDYBROWN};
	Polygon triangle;
	
	public GameRoot(int playerCount) {
		this.playerCount = playerCount;
		getChildren().add(playerLine);
		GameLogic gameLogic = new GameLogic(playerCount);
		GameDot.setHiglightColour(playerColour[1]);
		
		setOnMouseDragged(event->{
			if (drawingActive) {
				playerLine.setEndX(event.getSceneX());
				playerLine.setEndY(event.getSceneY());
			}
		});
		setOnMouseDragReleased(event->{				// Note that it has to be MouseDragRelease not MouseReleased to work with startFullDrag()
			 if(event.getTarget() instanceof GameDot) {
				GameDot endDot = (GameDot) event.getTarget();
				playerLine.setEndX(endDot.x);
				playerLine.setEndY(endDot.y);
				drawingActive = false;
				startDot.unhighlight();
				if (gameLogic.lineAvailable(playerLine)) {
					triangle = gameLogic.addPlayerLine(currentPlayer, playerLine);
					getChildren().add(copyLine(playerLine));

					if (triangle != null) {
						// We've got a winner!
						// triangle.setFill(playerColour[currentPlayer]);
						fillTriangle(triangle);
						getChildren().add(triangle);
					} 
					else if(playerLine.getEndX() != playerLine.getStartX() || playerLine.getEndY() != playerLine.getStartY()){
						nextPlayer();
					}
				}
			}
			else {
				resetPlayerLine();
				drawingActive = false;
				startDot.unhighlight();
			}
		});
	}

	public GameRoot(Node center) {
		super(center);
	}

	public GameRoot(Node center, Node top, Node right, Node bottom, Node left) {
		super(center, top, right, bottom, left);
	}

	void startDrawing(GameDot startDot) {
		drawingActive = true;
		this.startDot = startDot;
		playerLine.setStartX(startDot.x);
		playerLine.setStartY(startDot.y);
		playerLine.setEndX(startDot.x);
		playerLine.setEndY(startDot.y);
		playerLine.setStroke(playerColour[currentPlayer]);
	}
	
	void resetPlayerLine() {
		playerLine.setStartX(0);
		playerLine.setStartY(0);
		playerLine.setEndX(0);
		playerLine.setEndY(0);
	}
	
	void nextPlayer() {
		currentPlayer = ++currentPlayer % playerCount;
		GameDot.setHiglightColour(playerColour[currentPlayer]);
	}
	
	Line copyLine (Line ogLine) {
		Line copyLine = new Line(ogLine.getStartX(), ogLine.getStartY(), ogLine.getEndX(), ogLine.getEndY());
		copyLine.setStroke(ogLine.getStroke());
		return copyLine;
	}
	
	void fillTriangle(Polygon triangle) {
		double x = 0, y = 0;
		boolean xTurn = true;
		for(double d: triangle.getPoints()) {
			if (xTurn) x += d;
			else y += d;
			xTurn = !xTurn;
		}
		x = x/3;
		y = y/3;
		RadialGradient triangleFill = new RadialGradient(0, 0.1, x, y, 100, false, CycleMethod.NO_CYCLE, new Stop(0, Color.BLACK), 
				new Stop(1, playerColour[currentPlayer]));
		triangle.setFill(triangleFill);
	}

}
