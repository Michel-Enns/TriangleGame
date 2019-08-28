package application;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

class GameDot extends Circle{
	static double radius = 15;
	static Color baseColour = Color.DARKGRAY;
	static Color highlightColour = baseColour;
	double x, y;
	GameRoot gameRoot;
	
	GameDot (double x, double y){
		super (x, y, radius);
		this.x = x;
		this.y = y;
		this.setFill(Color.DARKGRAY);
		setOnMouseEntered(event ->{
			highlight();
		});
		setOnMouseDragEntered(event->{
			highlight();
		});
		setOnMouseExited(event ->{
			if (!gameRoot.drawingActive) {
				unhighlight();
			}
		});
		setOnMouseDragExited(event ->{
			if (gameRoot.drawingActive && !gameRoot.startDot.equals(this)) {
				unhighlight();
			}
		});
		setOnDragDetected(event->{
			gameRoot.startFullDrag();
			GameRoot gr = (GameRoot) getParent();
			gr.startDrawing(this);
		});
	}
	
	Point getPoint() {
		return new Point (x,y);
	}
	
	static void setHiglightColour(Color c) {
		highlightColour = c;
	}
	
	void unhighlight() {
		setFill(baseColour);
	}
	
	void highlight() {
		RadialGradient hilightGradiant = new RadialGradient(0, 0.1, x, y, radius, false, CycleMethod.NO_CYCLE, new Stop(0, highlightColour),
				new Stop(1, Color.DARKGRAY));
		setFill(hilightGradiant);
	}
}