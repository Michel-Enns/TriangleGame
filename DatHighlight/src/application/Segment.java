package application;

import javafx.scene.shape.Line;

public class Segment {

	enum Orientation {
		CW,
		CCW,
		COLINEAR
	}

	Point p1, p2;
	public Segment(Point p1, Point p2) { 
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public Segment(Line line) {
		this.p1 = new Point (line.getStartX(), line.getStartY());
		this.p2 = new Point (line.getEndX(), line.getEndY());
	}

	Line toLine(Segment s) {
		return new Line (s.p1.x, s.p1.y, s.p2.x, s.p2.y);
	}
	
	boolean hasEndPoint (Point p) {
		return p.equals(p1)||p.equals(p2);
	}
	
	boolean hasPoint (Point p){
		double THRESHOLD = 0.00001;	// Tolerance of how close the slopes have to be to be considered equal
		// First the easy case, is the test point one of the end points.
		if (p.equals(p1)||p.equals(p2)) {
			return true;
		}
		// Now check to see that the test point's x and y values are between
		// the x and y values of the line segment.
//		if (((p.x > p1.x && p.x < p2.x)||(p.x < p1.x && p.x > p2.x))&&
//				((p.y > p1.y && p.y < p2.y)||(p.y < p1.y && p.y > p2.y))) 
		
		// First check the special case where the segment is vertical.
		if(Math.abs(p1.x - p2.x) < THRESHOLD && Math.abs(p1.x -  p.x) < THRESHOLD){
			return true;
		}
		
		// Then check the general case.
		{
			double slope1 = (p2.y - p1.y) / (p2.x - p1.x);
			double slope2 = (p.y - p1.y) / (p.x - p1.x);
			return Math.abs(slope1 - slope2) < THRESHOLD;
		}
//		return false;
	}

	Point otherPoint (Point p){
		if (p.x == p1.x  && p.y == p1.y)
			return p2;
		else
			return p1;
	}

    @Override
    public int hashCode() {
        return (int) (p1.x + p2.x + p1.y + p2.y);
    }
	
	@Override
	public boolean equals (Object otherLine){
		if(!(otherLine instanceof Segment)){
			return false;
		}
		return (p1.equals(((Segment)otherLine).p1) && p2.equals(((Segment)otherLine).p2)) || 
				(p1.equals(((Segment)otherLine).p2) && p2.equals(((Segment)otherLine).p1));
	}

	@Override
	public String toString(){
		return p1 +  " to " + p2;
	}

	boolean intersectsWith(Segment otherLine){
		if(otherLine.hasEndPoint(p1)  || otherLine.hasEndPoint(p2)) {     // We don't count sharing an end point as intersecting.
			return false;
		}
		Orientation orient1 = findOrientation(p1, p2, otherLine.p1);
		Orientation orient2 = findOrientation(p1, p2, otherLine.p2);
		Orientation orient3 = findOrientation(otherLine.p1, otherLine.p2, p1);
		Orientation orient4 = findOrientation(otherLine.p1, otherLine.p2, p2);

		return(orient1 != orient2 && orient3 != orient4);
		
	}

	Orientation findOrientation(Point p1, Point p2, Point p3){
		double val = (p2.y - p1.y) * (p3.x - p2.x) - (p2.x - p1.x) * (p3.y - p2.y);
		if (val == 0) return Orientation.COLINEAR;
		return (val > 0)? Orientation.CW: Orientation.CCW;
	}

	Point intersectionPoint(Segment otherLine) { 
		// this line represented as a1x + b1y = c1 
		double a1 = p2.y - p1.y; 
		double b1 = p1.x - p2.x; 
		double c1 = a1*(p1.x) + b1*(p1.y); 

		// otherLine represented as a2x + b2y = c2 
		double a2 = otherLine.p2.y - otherLine.p1.y; 
		double b2 = otherLine.p1.x - otherLine.p2.x; 
		double c2 = a2*(otherLine.p1.x)+ b2*(otherLine.p1.y); 

		double determinant = a1*b2 - a2*b1; 

		if (determinant == 0) 
		{ 
			// The lines are parallel. This is simplified 
			// by returning a pair of FLT_MAX 
			return new Point(Double.MAX_VALUE, Double.MAX_VALUE); 
		} 
		else
		{ 
			double x = (b2*c1 - b1*c2)/determinant; 
			double y = (a1*c2 - a2*c1)/determinant; 
			return new Point(x, y); 
		} 
	} 
}
