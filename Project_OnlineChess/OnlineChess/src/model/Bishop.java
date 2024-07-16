package model;

public class Bishop extends Piece
{
	public Bishop(GamePanel gamePanel, int color, int col, int row) throws Exception
	{
		super(gamePanel, color, col, row);
		
		name = "Bishop";
		
		// Choose an appropriate image
		if (color == 1)
		{
			image = getImage(2);
		}
		else
		{
			image = getImage(8);
		}
	}
	
	public boolean isLegalMoveTo(int newCol, int newRow)
	{
		return Math.abs(newCol - col) == Math.abs(newRow - row);
	}
	
	public boolean isCollidedWithPieceAt(int col, int row)
	{
		// Check if there is any piece on up-left side
		if (this.col > col && this.row > row)
		{
			for (int i = 1; i < this.col - col; i++)
			{
				if (gamePanel.getPieceAt(this.col - i, this.row - i) != null)
				{
					return true;
				}
			}
		}
		
		// Check if there is any pieces on up-right side
		if (this.col < col && this.row > row)
		{
			for (int i = 1; i < col - this.col; i++)
			{
				if (gamePanel.getPieceAt(this.col + i, this.row - i) != null)
				{
					return true;
				}
			}
		}
		
		// Check if there is any pieces on down-left side
		if (this.col > col && this.row < row)
		{
			for (int i = 1; i < this.col - col; i++)
			{
				if (gamePanel.getPieceAt(this.col - i, this.row + i) != null)
				{
					return true;
				}
			}
		}
		
		// Check if there is any pieces on down-right side
		if (this.col < col && this.row < row)
		{
			for (int i = 1; i < col - this.col; i++)
			{
				if (gamePanel.getPieceAt(this.col + i, this.row + i) != null)
				{
					return true;
				}
			}
		}
		
		return false;
	}
}