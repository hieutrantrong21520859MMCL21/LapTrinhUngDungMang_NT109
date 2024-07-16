package model;

public class Rook extends Piece
{	
	public Rook(GamePanel gamePanel, int color, int col, int row) throws Exception
	{
		super(gamePanel, color, col, row);
		
		name = "Rook";
		
		// Check an appropriate image
		if (color == 1)
		{
			image = getImage(4);
		}
		else
		{
			image = getImage(10);
		}
	}
	
	public boolean isLegalMoveTo(int newCol, int newRow)
	{
		return col == newCol || row == newRow;
	}
	
	public boolean isCollidedWithPieceAt(int col, int row)
	{
		// Check if there is any pieces on the up side
		if (this.row > row)
		{
			for (int i = this.row - 1; i > row; i--)
			{
				if (gamePanel.getPieceAt(this.col, i) != null)
				{
					return true;
				}
			}
		}
		
		// Check if there is any pieces on the down side
		if (this.row < row)
		{
			for (int i = this.row + 1; i < row; i++)
			{
				if (gamePanel.getPieceAt(this.col, i) != null)
				{
					return true;
				}
			}
		}
		
		// Check if there is any pieces on the left side
		if (this.col > col)
		{
			for (int i = this.col - 1; i > col; i--)
			{
				if (gamePanel.getPieceAt(i, this.row) != null)
				{
					return true;
				}
			}
		}
		
		// Check if there is any pieces on the right side
		if (this.col < col)
		{
			for (int i = this.col + 1; i < col; i++)
			{
				if (gamePanel.getPieceAt(i, this.row) != null)
				{
					return true;
				}
			}
		}
		
		return false;
	}
}