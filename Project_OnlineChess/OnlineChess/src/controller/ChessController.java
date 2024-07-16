package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import model.Board;
import model.GamePanel;
import model.Piece;
import view.Login;
import view.PlayerUI;



public class ChessController extends MouseAdapter
{
	private GamePanel gamePanel;
	private int clickCount;
	private Piece selectedPiece;
	private Login login;
	private PlayerUI playerUI;
	
	public ChessController(GamePanel gamePanel)
	{
		this.gamePanel = gamePanel;
		playerUI = gamePanel.getPlayerUI();
		login = playerUI.getLoginObj();
		clickCount = 0;
	}
	
	//main method
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (gamePanel.isTurn())
		{
			// Get the position of mouse
			int row = e.getY() / Board.SQUARE_SIZE;
			int col = e.getX() / Board.SQUARE_SIZE;
			
			clickCount++;
			
			Piece piece = gamePanel.getPieceAt(col , row);
			
			if (clickCount == 1)
			{
				if (piece != null && piece.getColor() == gamePanel.getCurrentColor())
				{
					selectedPiece = piece;
					gamePanel.setSelectedPiece(piece);
				}
			}
			
			if (clickCount == 2)
			{
				if (selectedPiece != null)
				{
					if (gamePanel.isValidMove(selectedPiece, col, row))
					{
						gamePanel.movePiece(selectedPiece, col, row);
						
						try
						{
							BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(login.getPlayer().getSocket().getOutputStream()));
							
							// Set the selected piece info
							String send = "/movement" + " " + (7 - selectedPiece.getPreviousColumn()) + " " + (7 - selectedPiece.getPreviousRow()) + " " + (7 - col) + " " + (7 - row);
							
							// Add the captured piece info if not null
							if (gamePanel.getCapturedPiece() != null)
							{
								send += " " + (7 - gamePanel.getCapturedPiece().getColumn()) + " " + (7 - gamePanel.getCapturedPiece().getRow());
							}
							
							// Allow opponent's turn
							send += " " + "true";
							
							bw.write(send);
							bw.newLine();
							bw.flush();
							
							// Change the turn
							gamePanel.setTurn(false);
							
							// Update turn info
							playerUI.updateTurnInfo("Your opponent's turn!");
						}
						catch (Exception ex)
						{
							
						}
					}
				}
				
				selectedPiece = null;
				gamePanel.setSelectedPiece(null);
				clickCount = 0;
			}
			
			gamePanel.repaint();
		}
	}
	
	/*@Override
	public void mouseClicked(MouseEvent e) {
		// Get the position of mouse
		int row = e.getY() / Board.SQUARE_SIZE;
		int col = e.getX() / Board.SQUARE_SIZE;
					
		clickCount++;
					
		Piece piece = gamePanel.getPieceAt(col , row);
					
		if (clickCount == 1)
		{
			if (piece != null)
			{
				selectedPiece = piece;
				gamePanel.setSelectedPiece(piece);
			}
		}
					
		if (clickCount == 2)
		{
			if (selectedPiece != null)
			{
				if (gamePanel.isValidMove(selectedPiece, col, row))
				{
					gamePanel.movePiece(selectedPiece, col, row);
								

				}
			}
						
			selectedPiece = null;
			gamePanel.setSelectedPiece(null);
			clickCount = 0;
		}
					
		gamePanel.repaint();
	}*/
}