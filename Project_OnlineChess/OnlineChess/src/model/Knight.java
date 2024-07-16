package model;

public class Knight extends Piece
{
	public Knight(GamePanel gamePanel, int color, int col, int row) throws Exception
	{
		super(gamePanel, color, col, row);
		
		name = "Knight";
		
		// Choose an appropriate image
		if (color == 1)
		{
			image = getImage(3);
		}
		else
		{
			image = getImage(9);
		}
	}
	
	public boolean isLegalMoveTo(int newCol, int newRow)
	{
		return Math.abs(newCol - col) * Math.abs(newRow - row) == 2;
	}
}