package model;

public class Pawn extends Piece
{	
	public Pawn(GamePanel gamePanel, int color, int col, int row) throws Exception
	{
		super(gamePanel, color, col, row);
		
		name = "Pawn";
		
		// Choose an appropriate image
		if (color == 1)
		{
			image = getImage(5);
		}
		else
		{
			image = getImage(11);
		}
	}
	
	public boolean isLegalMoveTo(int newCol, int newRow)
	{
		int rowOffset = (gamePanel.getCurrentColor() == 1 ? (color == 1 ? 1 : -1) : (color == 1 ? -1 : 1));
		
		// Check if the pawn is moved forward 1 square
		if (newCol == col && newRow == row - rowOffset && gamePanel.getPieceAt(newCol, newRow) == null)
		{
			return true;
		}
		
		// Check if the pawn is moved forward 2 squares
		if (isFirstMove && newCol == col && newRow == this.row - rowOffset * 2 && gamePanel.getPieceAt(newCol, newRow) == null && gamePanel.getPieceAt(newCol, newRow + rowOffset) == null)
		{
			return true;
		}
		
		// Check if the pawn can capture the opponent's piece on the left side
		if (newCol == col - 1 && newRow == row - rowOffset && gamePanel.getPieceAt(newCol, newRow) != null)
		{
			return true;
		}
		
		// Check if the pawn can capture the opponent's piece on the right side
		if (newCol == col + 1 && newRow == row - rowOffset && gamePanel.getPieceAt(newCol, newRow) != null)
		{
			return true;
		}
		
		/* En Passant technique */
		if (gamePanel.getSquareNum(newCol, newRow) == gamePanel.getEnPassantSquareNum())
		{
			// Left En Passant
			if (newCol == col - 1 && newRow == row - rowOffset && gamePanel.getPieceAt(newCol, newRow + rowOffset) != null)
			{
				return true;
			}
			
			if (newCol == col + 1 && newRow == row - rowOffset && gamePanel.getPieceAt(newCol, newRow + rowOffset) != null)
			{
				return true;
			}
		}
		
		return false;
	}
}