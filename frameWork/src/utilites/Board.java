package utilites;

import android.graphics.Point;

/**
 * the board for the game is a 9x9 grid of squares
 * 
 * @author shaun
 *
 */
public class Board {

	
	Square [] [] board;
	
	public Board()
	{
		for(int i = 0; i<9; i++)
		{
			for(int j = 0; j<9; j++)
			{
				Point newCo = new Point(i,j);
				if(i==4 && j==4)
				{
					Square square = new Square(newCo, 3);
				}
				Square square = new Square(newCo);
			}
		}
	}
	
	
	
	
}
