package model;

public class King extends Piece
{
	
	public King(GamePanel gamePanel, int color, int col, int row) throws Exception
	{
		super(gamePanel, color, col, row);
		
		name = "King";
		
		// Choose an appropriate image
		if (color == 1)
		{
			image = getImage(0);
		}
		else
		{
			image = getImage(6);
		}
	}
	
	public boolean isLegalMoveTo(int newCol, int newRow)
	{
		return Math.abs((newCol - col) * (newRow - row)) == 1 ||
				Math.abs(newCol - col) + Math.abs(newRow - row) == 1 ||
				canCastle(newCol, newRow);
	}
	
	private boolean canCastle(int newCol, int newRow)
	{
		/* The player's type is WHITE */
		if (gamePanel.currentColor == 1)
		{
			if (row == newRow)
			{
				if (newCol == 6)
				{
					Piece rook = gamePanel.getPieceAt(7, newRow);
					
					if (rook != null && rook.isFirstMove && isFirstMove)
					{
						return gamePanel.getPieceAt(6, newRow) == null &&
								gamePanel.getPieceAt(5, newRow) == null &&
								!gamePanel.isKingChecked(this, 5, newRow) &&
								!gamePanel.isKingChecked(this, col, row);
					}
				}
				
				else if (newCol == 2)
				{
					Piece rook = gamePanel.getPieceAt(0, newRow);
					
					if (rook != null && rook.isFirstMove && isFirstMove)
					{
						return gamePanel.getPieceAt(3, newRow) == null &&
								gamePanel.getPieceAt(2, newRow) == null &&
								gamePanel.getPieceAt(1, newRow) == null &&
								!gamePanel.isKingChecked(this, 3, newRow) &&
								!gamePanel.isKingChecked(this, col, row);
					}
				}
			}
		}
		
		else /* The player's type is BLACK */
		{
			if (row == newRow)
			{
				if (newCol == 1)
				{
					Piece rook = gamePanel.getPieceAt(0, newRow);
					
					if (rook != null && rook.isFirstMove && isFirstMove)
					{
						return gamePanel.getPieceAt(2, newRow) == null &&
								gamePanel.getPieceAt(1, newRow) == null &&
								!gamePanel.isKingChecked(this, 2, newRow) &&
								!gamePanel.isKingChecked(this, col, row);
					}
				}
				
				else if (newCol == 5)
				{
					Piece rook = gamePanel.getPieceAt(7, newRow);
					
					if (rook != null && rook.isFirstMove && isFirstMove)
					{
						return gamePanel.getPieceAt(6, newRow) == null &&
								gamePanel.getPieceAt(5, newRow) == null &&
								gamePanel.getPieceAt(4, newRow) == null &&
								!gamePanel.isKingChecked(this, 3, newRow) &&
								!gamePanel.isKingChecked(this, col, row);
					}
				}
			}
		}
		
		return false;
	}
}