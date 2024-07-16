package model;

public class Queen extends Piece
{	
	public Queen(GamePanel gamePanel, int color, int col, int row) throws Exception
	{
		super(gamePanel, color, col, row);
		
		name = "Queen";
		
		// Choose an appropriate image
		if (color == 1)
		{
			image = getImage(1);
		}
		else
		{
			image = getImage(7);
		}
	}
	
	public boolean isLegalMoveTo(int newCol, int newRow)
	{
		return col == newCol || row == newRow || Math.abs(newCol - col) == Math.abs(newRow - row);
	}
	
	public boolean isCollidedWithPieceAt(int col, int row)
	{
		if (this.col == col || this.row == row)
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
		}
		else
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
		}
		
		return false;
	}
}