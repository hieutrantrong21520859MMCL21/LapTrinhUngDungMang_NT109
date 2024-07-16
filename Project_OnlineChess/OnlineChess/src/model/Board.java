package model;

import java.awt.Color;
import java.awt.Graphics2D;

public class Board
{
	static final int MAX_COL = 8;
	static final int MAX_ROW = 8;
	
	public static final int SQUARE_SIZE = 64;
	public static final int HALF_SQUARE_SIZE = SQUARE_SIZE / 2;
	
	public void draw(Graphics2D g2)
	{
		boolean isWhite = true;
		for (int row = 0; row < MAX_ROW; row++)
		{
			for (int col = 0; col < MAX_COL; col++)
			{
				if (isWhite)
				{
					g2.setColor(new Color(227, 198, 181));
				}
				else
				{
					g2.setColor(new Color(107, 105, 53));
				}
				
				g2.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
				isWhite = !isWhite;
			}
			
			isWhite = !isWhite;
		}
	}
}