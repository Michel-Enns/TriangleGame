package application;
/**
 * 
 * The makePoints method returns an array of GameDot objects
 * equal spaced around a circle.
 *
 */

public class GameDotGenerator {
	static GameDot[] makeGameDots (int numPoints, int canvasWidth) {
		GameDot[] gameDots= new GameDot[numPoints];
		double centreX = canvasWidth/2;
		double centreY = canvasWidth/2;
		double radius = canvasWidth/2.5;
		
		for(int i=0; i < numPoints; i++) {
			double x, y;
			x = centreX + radius * Math.sin(i * 2 * Math.PI / numPoints);
			y = centreY + radius * Math.cos(i * 2 * Math.PI / numPoints);
			gameDots[i] = new GameDot (x, y);
		}
		return gameDots;
	}
	 
}
