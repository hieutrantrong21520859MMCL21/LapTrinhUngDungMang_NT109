package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import controller.ChessController;
import view.Login;
import view.PlayerUI;

public class GamePanel extends JPanel
{
	//private static final long serialVersionUID = 1L;
	
	Board board = new Board();
	
	ArrayList<Piece> pieces = new ArrayList<Piece>();
	
	// Color
	int currentColor = 1; // test only
	
	int enPassantSquareNum = -1;
	
	Piece capturedPiece;
	Piece selectedPiece;
	
	private Login login;
	private PlayerUI playerUI;
	
	boolean turn;
	
	boolean isGameOver = false;

	/**
	 * Create the panel
	 */
	public GamePanel(PlayerUI playerUI)
	{
		this.playerUI = playerUI;
		login = playerUI.getLoginObj();
		addMouseListener(new ChessController(this));
	}
	
	public int getCurrentColor()
	{
		return currentColor;
	}
	
	public void setCurrentColor(int currentColor)
	{
		this.currentColor = currentColor;
	}
	
	public boolean isTurn()
	{
		return turn;
	}
	
	public void setTurn(boolean turn)
	{
		this.turn = turn;
	}
	
	public void setSelectedPiece(Piece selectedPiece)
	{
		this.selectedPiece = selectedPiece;
	}
	
	public Piece getSelectedPiece()
	{
		return selectedPiece;
	}
	
	public void setCapturedPiece(Piece capturedPiece)
	{
		this.capturedPiece = capturedPiece;
	}
	
	public Piece getCapturedPiece()
	{
		return capturedPiece;
	}
	
	public int getEnPassantSquareNum()
	{
		return enPassantSquareNum;
	}
	
	public Login getLoginObj()
	{
		return login;
	}
	
	public PlayerUI getPlayerUI()
	{
		return playerUI;
	}
	
	/* Set a chess board */
	@Override
	public void paintComponent(Graphics g)
	{
		// Draw board
		Graphics2D g2 = (Graphics2D) g;
		board.draw(g2);
		
		// Highlight squares when the piece is selected
		highlightValidSquares(g2);
		
		// Draw pieces
		for (Piece p : pieces)
		{
			p.draw(g2);
		}
	}
	
	/* Set pieces */
	public void setPieces() throws Exception
	{
		// Set pieces on the board (left -> right, top -> bottom) following the current type first
		pieces.add(new Pawn(this, currentColor, 0, 6));
		pieces.add(new Pawn(this, currentColor, 1, 6));
		pieces.add(new Pawn(this, currentColor, 2, 6));
		pieces.add(new Pawn(this, currentColor, 3, 6));
		pieces.add(new Pawn(this, currentColor, 4, 6));
		pieces.add(new Pawn(this, currentColor, 5, 6));
		pieces.add(new Pawn(this, currentColor, 6, 6));
		pieces.add(new Pawn(this, currentColor, 7, 6));
		pieces.add(new Rook(this, currentColor, 0, 7));
		pieces.add(new Knight(this, currentColor, 1, 7));
		pieces.add(new Bishop(this, currentColor, 2, 7));
		pieces.add(new Bishop(this, currentColor, 5, 7));
		pieces.add(new Knight(this, currentColor, 6, 7));
		pieces.add(new Rook(this, currentColor, 7, 7));
		
		// Set pieces following the opposite type
		pieces.add(new Rook(this, 1 - currentColor, 0, 0));
		pieces.add(new Knight(this, 1 - currentColor, 1, 0));
		pieces.add(new Bishop(this, 1 - currentColor, 2, 0));
		pieces.add(new Bishop(this, 1 - currentColor, 5, 0));
		pieces.add(new Knight(this, 1 - currentColor, 6, 0));
		pieces.add(new Rook(this, 1 - currentColor, 7, 0));
		pieces.add(new Pawn(this, 1 - currentColor, 0, 1));
		pieces.add(new Pawn(this, 1 - currentColor, 1, 1));
		pieces.add(new Pawn(this, 1 - currentColor, 2, 1));
		pieces.add(new Pawn(this, 1 - currentColor, 3, 1));
		pieces.add(new Pawn(this, 1 - currentColor, 4, 1));
		pieces.add(new Pawn(this, 1 - currentColor, 5, 1));
		pieces.add(new Pawn(this, 1 - currentColor, 6, 1));
		pieces.add(new Pawn(this, 1 - currentColor, 7, 1));
		
		// Set positions of the King and the Queen
		if (currentColor == 1)
		{
			// Player
			pieces.add(new Queen(this, currentColor, 3, 7));
			pieces.add(new King(this, currentColor, 4, 7));
			
			// Opponent
			pieces.add(new Queen(this, 1 - currentColor, 3, 0));
			pieces.add(new King(this, 1 - currentColor, 4, 0));
		}
		else
		{
			// Player
			pieces.add(new King(this, currentColor, 3, 7));
			pieces.add(new Queen(this, currentColor, 4, 7));
						
			// Opponent
			pieces.add(new King(this, 1 - currentColor, 3, 0));
			pieces.add(new Queen(this, 1 - currentColor, 4, 0));
		}
	}
	
	/* Move the selected piece to a new position */
	public void movePiece(Piece selectedPiece, int newCol, int newRow)
	{
		/* Movement for Pawns only */
		if (selectedPiece.name.equals("Pawn"))
		{
			// En Passant technique
			int rowOffset = (currentColor == 1 ? (selectedPiece.color == 1 ? 1 : -1) : (selectedPiece.color == 1 ? -1 : 1));
			
			if (getSquareNum(newCol, newRow) == enPassantSquareNum)
			{
				capturedPiece = getPieceAt(newCol, newRow + rowOffset);
			}
			
			if (Math.abs(selectedPiece.row - newRow) == 2)
			{
				enPassantSquareNum = getSquareNum(newCol, newRow + rowOffset);
			}
			else
			{
				enPassantSquareNum = -1;
			}
			
			// Promotion technique
			int row = (currentColor == 1 ? (selectedPiece.color == 1 ? 0 : 7) : (selectedPiece.color == 1 ? 7 : 0));
			
			if (row == newRow)
			{
				try
				{
					pieces.add(new Queen(this, selectedPiece.color, newCol, newRow));
					pieces.remove(selectedPiece);
				}
				catch (Exception e)
				{
					
				}
			}
		}
		
		/* Castling technique */
		if (selectedPiece.name.equals("King"))
		{
			if (Math.abs(selectedPiece.col - newCol) == 2)
			{
				if (currentColor == 1)
				{
					Piece rook;
					
					if (selectedPiece.col < newCol)
					{
						rook = getPieceAt(7, selectedPiece.row);
						rook.col = 5;
					}
					
					else
					{
						rook = getPieceAt(0, selectedPiece.row);
						rook.col = 3;
					}
					
					rook.x = rook.getX();
				}
				
				else
				{
					Piece rook;
					
					if (selectedPiece.col < newCol)
					{
						rook = getPieceAt(7, selectedPiece.row);
						rook.col = 4;
					}
					
					else
					{
						rook = getPieceAt(0, selectedPiece.row);
						rook.col = 2;
					}
					
					rook.x = rook.getX();
				}
			}
		}
		
		this.selectedPiece.preCol = selectedPiece.col;
		this.selectedPiece.preRow = selectedPiece.row;
		this.selectedPiece.col = newCol;
		this.selectedPiece.row = newRow;
		this.selectedPiece.x = selectedPiece.getX();
		this.selectedPiece.y = selectedPiece.getY();
		
		this.selectedPiece.isFirstMove = false;
		
		// Remove the captured piece
		pieces.remove(capturedPiece);
		
		updateGameState();
	}
	
	/* Check if the piece can be moved followed by some rules */
	public boolean isValidMove(Piece selectedPiece, int newCol, int newRow)
	{
		capturedPiece = getPieceAt(newCol, newRow);
		
		if (isGameOver)
		{
			return false;
		}
		
		/*if (!turn)
		{
			return false;
		}*/
		
		if (isSameTeam(selectedPiece, capturedPiece))
		{
			return false;
		}
		
		if (!selectedPiece.isLegalMoveTo(newCol, newRow))
		{
			return false;
		}
		
		if (selectedPiece.isCollidedWithPieceAt(newCol, newRow))
		{
			return false;
		}
		
		// Check
		if (isKingChecked(selectedPiece, newCol, newRow))
		{
			return false;
		}
		
		return true;
	}
	
	public boolean isKingChecked(Piece selectedPiece, int newCol, int newRow)
	{
		Piece king = getKingBy(selectedPiece.color);
		assert king != null;
		
		int kingCol = king.col;
		int kingRow = king.row;
		
		if (this.selectedPiece != null && this.selectedPiece.name.equals("King"))
		{
			kingCol = newCol;
			kingRow = newRow;
		}
		
		return isHitByRook(newCol, newRow, king, kingCol, kingRow, 0, 1) ||
				isHitByRook(newCol, newRow, king, kingCol, kingRow, 1, 0) ||
				isHitByRook(newCol, newRow, king, kingCol, kingRow, 0, -1) ||
				isHitByRook(newCol, newRow, king, kingCol, kingRow, -1, 0) ||
				
				isHitByBishop(newCol, newRow, king, kingCol, kingRow, -1, -1) ||
				isHitByBishop(newCol, newRow, king, kingCol, kingRow, 1, -1) ||
				isHitByBishop(newCol, newRow, king, kingCol, kingRow, -1, 1) ||
				isHitByBishop(newCol, newRow, king, kingCol, kingRow, 1, 1) ||
				
				isHitByKnight(newCol, newRow, king, kingCol, kingRow) ||
				isHitByPawn(newCol, newRow, king, kingCol, kingRow) ||
				isHitByKing(king, kingCol, kingRow);
	}
	
	public void updateGameState()
	{
		int color = turn ? 1 : 0;
		Piece king = getKingBy(color);
		
		if (isGameOver(king))
		{
			if (isKingChecked(king, king.col, king.row)) // It's checkmate
			{
				
			}
			
			else // This is stalemate
			{
				
			}
			
			isGameOver = true;
		}
		
		else if (arePiecesInsufficient(currentColor) && arePiecesInsufficient(1 - currentColor))
		{
			isGameOver = true;
		}
	}
	
	/* Select the piece at the known position */
	public Piece getPieceAt(int col, int row)
	{
		for (Piece p : pieces)
		{
			if (p.getRow() == row && p.getColumn() == col)
			{
				return p;
			}
		}
		
		return null;
	}
	
	public int getSquareNum(int col, int row)
	{
		return 8 * col + row;
	}
	
	/* Check if the pieces have the same color */
	private boolean isSameTeam(Piece p1, Piece p2)
	{
		if (p1 == null || p2 == null)
		{
			return false;
		}
		
		return p1.color == p2.color;
	}
	
	/* Suggest valid directions of the selected piece */
	private void highlightValidSquares(Graphics2D g2)
	{
		if (selectedPiece != null)
		{
			for (int row = 0; row < Board.MAX_ROW; row++)
			{
				for (int col = 0; col < Board.MAX_COL; col++)
				{
					if (isValidMove(selectedPiece, col, row))
					{
						g2.setColor(new Color(68, 180, 57, 190));
						g2.fillRect(col * Board.SQUARE_SIZE, row * Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
					}
				}
			}
		}
	}
	
	/* Check technique */
	private Piece getKingBy(int color)
	{
		for (Piece p : pieces)
		{
			if (p.color == color && p.name.equals("King"))
			{
				return p;
			}
		}
		
		return null;
	}
	
	// Check if the King is checked by a Rook
	private boolean isHitByRook(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal)
	{
		for (int i = 1; i < 8; i++)
		{
			if (kingCol + (i * colVal) == col && kingRow + (i * rowVal) == row)
			{
				break;
			}
			
			Piece p = getPieceAt(kingCol + (i * colVal), kingRow + (i * rowVal));
			if (p != null && p != selectedPiece)
			{
				if (!isSameTeam(p, king) && (p.name.equals("Rook") || p.name.equals("Queen")))
				{
					return true;
				}
				
				break;
			}
		}
		
		return false;
	}
	
	// Check if the King is checked by a Bishop
	private boolean isHitByBishop(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal)
	{
		for (int i = 1; i < 8; i++)
		{
			if (kingCol - (i * colVal) == col && kingRow - (i * rowVal) == row)
			{
				break;
			}
			
			Piece p = getPieceAt(kingCol - (i * colVal), kingRow - (i * rowVal));
			if (p != null && p != selectedPiece)
			{
				if (!isSameTeam(p, king) && (p.name.equals("Bishop") || p.name.equals("Queen")))
				{
					return true;
				}
				
				break;
			}
		}
		
		return false;
	}
	
	// Check if the King is checked by a Knight
	private boolean checkKnight(Piece p, Piece k, int col, int row)
	{
		return p != null && !isSameTeam(p, k) && p.name.equals("Knight") && !(p.col == col && p.row == row);
	}
	
	private boolean isHitByKnight(int col, int row, Piece king, int kingCol, int kingRow)
	{
		return checkKnight(getPieceAt(kingCol - 1, kingRow - 2), king, col, row) ||
				checkKnight(getPieceAt(kingCol + 1, kingRow - 2), king, col, row) ||
				checkKnight(getPieceAt(kingCol + 2, kingRow - 1), king, col, row) ||
				checkKnight(getPieceAt(kingCol + 2, kingRow + 1), king, col, row) ||
				checkKnight(getPieceAt(kingCol + 1, kingRow + 2), king, col, row) ||
				checkKnight(getPieceAt(kingCol - 1, kingRow + 2), king, col, row) ||
				checkKnight(getPieceAt(kingCol - 2, kingRow + 1), king, col, row) ||
				checkKnight(getPieceAt(kingCol - 2, kingRow - 1), king, col, row);
	}
	
	// Check if the King is checked by the opposite King
	private boolean checkKing(Piece p, Piece k)
	{
		return p != null && !isSameTeam(p, k) && p.name.equals("King");
	}
	
	private boolean isHitByKing(Piece king, int kingCol, int kingRow)
	{
		return checkKing(getPieceAt(kingCol - 1, kingRow - 1), king) ||
				checkKing(getPieceAt(kingCol + 1, kingRow - 1), king) ||
				checkKing(getPieceAt(kingCol, kingRow - 1), king) ||
				checkKing(getPieceAt(kingCol - 1, kingRow), king) ||
				checkKing(getPieceAt(kingCol + 1, kingRow), king) ||
				checkKing(getPieceAt(kingCol - 1, kingRow + 1), king) ||
				checkKing(getPieceAt(kingCol + 1, kingRow + 1), king) ||
				checkKing(getPieceAt(kingCol, kingRow + 1), king);
	}
	
	// Check if the King is checked by a Pawn
	private boolean checkPawn(Piece p, Piece k, int col, int row)
	{
		return p != null && !isSameTeam(p, k) && p.name.equals("Pawn") && !(p.col == col && p.row == row);
	}
	
	private boolean isHitByPawn(int col, int row, Piece king, int kingCol, int kingRow)
	{
		int rowOffset = (currentColor == 1 ? (king.color == 1 ? -1 : 1) : (king.color == 1 ? 1 : -1));
		
		return checkPawn(getPieceAt(kingCol + 1, kingRow + rowOffset), king, col, row) ||
				checkPawn(getPieceAt(kingCol - 1, kingRow + rowOffset), king, col, row);
	}
	
	/* Check if the game is over */
	private boolean isGameOver(Piece king)
	{
		for (Piece p : pieces)
		{
			if (isSameTeam(p, king))
			{
				selectedPiece = p == king ? king : null;
				
				for (int row = 0; row < Board.MAX_ROW; row++)
				{
					for (int col = 0; col < Board.MAX_COL; col++)
					{
						if (isValidMove(p, col, row))
						{
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}
	
	/* Check if there are insufficient pieces on the board */
	private boolean arePiecesInsufficient(int color)
	{
		ArrayList<String> names = pieces.stream()
				.filter(p -> p.color == color)
				.map(p -> p.name)
				.collect(Collectors.toCollection(ArrayList::new));
		
		if (names.contains("Queen") || names.contains("Rook") || names.contains("Pawn"))
		{
			return false;
		}
		
		return names.size() < 3;
	}
}