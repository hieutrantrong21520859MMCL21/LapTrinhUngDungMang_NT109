package model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Piece
{
	int x, y;
	int col, row, preCol, preRow;
	int color;
	boolean isFirstMove = true;
	String name;
	Image image;
	GamePanel gamePanel;
	
	public Piece(GamePanel gamePanel, int color, int col, int row)
	{
		this.gamePanel = gamePanel;
		this.color = color;
		this.col = col;
		this.row = row;
		preCol = col;
		preRow = row;
		x = getX();
		y = getY();
	}
	
	public int getX()
	{
		return col * Board.SQUARE_SIZE;
	}
	
	public int getY()
	{
		return row * Board.SQUARE_SIZE;
	}
	
	public int getRow()
	{
		return row;
	}
	
	public int getColumn()
	{
		return col;
	}
	
	public int getPreviousColumn()
	{
		return preCol;
	}
	
	public int getPreviousRow()
	{
		return preRow;
	}
	
	public int getColor()
	{
		return color;
	}
	
	public Image getImage(int index) throws Exception
	{
		Image imgs[] = new Image[12];
		BufferedImage all = ImageIO.read(new File("src/images/chess.png"));
		int i = 0;
		for (int y = 0; y < 400; y+=200)
		{
			for (int x = 0; x < 1200; x+=200)
			{
				imgs[i++] = all.getSubimage(x, y, 200, 200).getScaledInstance(Board.SQUARE_SIZE, Board.SQUARE_SIZE, BufferedImage.SCALE_SMOOTH);
			}
		}
		
		return imgs[index];
	}
	
	public void draw(Graphics2D g2)
	{
		g2.drawImage(image, x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
	}
	
	public boolean isLegalMoveTo(int newCol, int newRow)
	{
		return true;
	}
	
	public boolean isCollidedWithPieceAt(int col, int row)
	{
		return false;
	}
}