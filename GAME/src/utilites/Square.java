package utilites;

import android.graphics.Point;

/**
 * this class represent each of the squares on the board 
 * needs to track the locations current state - home(3) built(2), scavenged(1) or raw (0)
 * 
 * @author shaun
 */
public class Square {

	private int state = 0;
	private Point coords = new Point();
	
	public Square()
	{
		
	}
	
	public Square(Point co)
	{
		this.coords = co;
	}
	
	public Square(Point co, int state)
	{
		this.state = state;
		this.coords = co;
	}
	
}
