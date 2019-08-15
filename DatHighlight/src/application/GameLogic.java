package application;

import java.util.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;


public class GameLogic {
	int playerCount;
	Set<Segment> allPlayedLines = new HashSet<Segment>();					// Keeps track of all lines played by all players together
	ArrayList<Set<Segment>> playerLines = new ArrayList<Set<Segment>>();	// Keeps track of each lines played by each player

	
	public GameLogic (int playerCount) {
		this.playerCount = playerCount;
		for(int i=0; i<playerCount; i++) {
			playerLines.add(new HashSet<Segment>());
		}
	}

	// Returns the Polygon that checkForTriangel returns
	 Polygon makeLine (Segment newLine, int currentPlayer){
		Set<Segment> newSegments = new HashSet<Segment>();
		newSegments.add(newLine);
		ArrayList<Point> newIntersectionPoints = new ArrayList<Point>();
		newIntersectionPoints.add(newLine.p1);
		// Now we check if the new line intersects any existing lines
		for (Segment testSegment: playerLines.get(currentPlayer)){
			if(testSegment.equals(newLine)) {
				continue;  // Shoudn't be there, but just in case.
			}
			if(testSegment.intersectsWith(newLine)){
				// If it does intersect, the existing line spawns exactly two new line segments.  
				// Each of which shouldn't intersect with anything new.
				Point p3 = testSegment.intersectionPoint(newLine);
				newSegments.add(new Segment(testSegment.p1, p3));
				newSegments.add(new Segment(testSegment.p2, p3));
				newIntersectionPoints.add(p3);
			}
		}
		newIntersectionPoints.add(newLine.p2);
		// We've gone through all the existing lines looking for intersection points,
		// now it's time to split up the new line into line segments based on these points and add them.
		int pnum = newIntersectionPoints.size();
		if(pnum == 2) {								// If we only have the two end points then we're done.
			for(Segment segmentToAdd: newSegments) {
				Polygon retval = checkForTriangle(currentPlayer, segmentToAdd);
				if(retval != null) {
					return retval;
				}
				playerLines.get(currentPlayer).add(segmentToAdd);
			}
		}
		for (int i = 1; i <= pnum; i++) {			// i is the length of the line segment in terms of shortest sub-segments
			for(int j = 0; j < pnum - i; j++) {     // j is the starting point of the segment
				Segment newSegment = new Segment(newIntersectionPoints.get(j), newIntersectionPoints.get(j+i));
				newSegments.add(newSegment);    
			}
		}
		for(Segment segmentToAdd: newSegments) {
			Polygon retval = checkForTriangle(currentPlayer, segmentToAdd);
			if(retval != null) {
				return retval;
			}
			playerLines.get(currentPlayer).add(segmentToAdd);
		}
		return null;
	}

	// The checkForTriangle method takes the player number and a given segment and checks to see if that
	// segment forms a triangle with any two other existing segments (not just played lines) from that 
	// player.  If it does, the Point that completes the triangle is returned.  If no triangle is formed
	// null is returned.
	public Polygon checkForTriangle(int playerNumber, Segment givenLine){
		Set<Point> set1 = new HashSet<Point>();
		Set<Point> set2 = new HashSet<Point>();
		for(Segment testLine: playerLines.get(playerNumber)){
			if(testLine.equals(givenLine)) {
				continue;
			}
			if(testLine.hasEndPoint(givenLine.p1)){
				Point p = testLine.otherPoint(givenLine.p1);
				if(set2.contains(p)) {
					if(!givenLine.hasPoint(p)) {
						
						return new Polygon(givenLine.p1.x, givenLine.p1.y, givenLine.p2.x, givenLine.p2.y ,p.x, p.y);
					}
				}
				else {
					set1.add(p);
				}
			}
			if(testLine.hasEndPoint(givenLine.p2)){
				Point p = testLine.otherPoint(givenLine.p2);
				if(set1.contains(p)) {
					if(!givenLine.hasPoint(p)) {
						return new Polygon(givenLine.p1.x, givenLine.p1.y, givenLine.p2.x, givenLine.p2.y ,p.x, p.y);
					}
				}
				else {
					set2.add(p);
				}
			}
		}
		return null;
	}
	
	boolean lineAvailable (Line proposedLine) {
		return !allPlayedLines.contains(new Segment (proposedLine));
	}
	
	Polygon addPlayerLine(int playerNumber, Line newLine) {
		Segment newSegment = new Segment(newLine);
		allPlayedLines.add(newSegment);
		return makeLine(newSegment, playerNumber);
	}
}
