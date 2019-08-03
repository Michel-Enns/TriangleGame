package application;

public class Point {
	double THRESHOLD = 0.00001;
	double x, y;

	public Point(double x, double y) { 
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object p){
		if (!(p instanceof Point)){
			return false;
		}
		return Math.abs(((Point)p).x - x) < THRESHOLD && Math.abs(((Point)p).y - y) < THRESHOLD;
	}

	@Override
	public int hashCode() {
		return (int)(x + y);
	}

	@Override
	public String toString(){
		return String.format("(%.2f, %.2f)", x, y);
	}
}
