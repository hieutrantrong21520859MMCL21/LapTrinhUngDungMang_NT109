package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.RoomController;
import view.Lobby;

public class RoomPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	/* Components */
	private JLabel lblInfo;
	private JLabel lblRoomID;
	private JLabel lblImage;
	
	private Lobby lobby;
	
	private Font font;
	private int id;
	
	private RoomController controller;
	
	/**
	 * Create the panel.
	 */
	public RoomPanel(Lobby lobby, int id) // test: RoomPanel(Lobby, id)
	{
		this.id = id;
		this.lobby = lobby;
		//this.login = login;
		
		// Create a font
		font = new Font("VNI-Bazooka", Font.BOLD, 15);
		
		// Initialize components
		initialize();
		
		// Add events controller
		controller = new RoomController(this);
		addMouseListener(controller);
	}
	
	public synchronized int getId()
	{
		return id;
	}
	
	public synchronized Lobby getLobbyObj()
	{
		return lobby;
	}
	
	public synchronized void setInfo(String info)
	{
		lblInfo.setText(info);
	}
	
	public synchronized String getInfo()
	{
		return lblInfo.getText();
	}
	
	public synchronized void setImage(String path)
	{
		// Scale an image
		ImageIcon icon = new ImageIcon(RoomPanel.class.getResource(path));
		Image img = icon.getImage();
		Image scaledImage = img.getScaledInstance(120, 70, Image.SCALE_SMOOTH);
		
		// Add the image
		lblImage.setIcon(new ImageIcon(scaledImage));
	}
	
	private void initialize()
	{
		setLayout(null);
		setBackground(Color.BLACK);
		
		lblRoomID = new JLabel("ROOM " + id, JLabel.CENTER);
		lblRoomID.setBounds(20, 15, 150, 20);
		lblRoomID.setForeground(Color.PINK);
		lblRoomID.setFont(font);
		add(lblRoomID);
		
		/* Scale an image */
		ImageIcon icon = new ImageIcon(RoomPanel.class.getResource("/images/empty_chairs.png"));
		Image img = icon.getImage();
		Image scaledImage = img.getScaledInstance(120, 70, Image.SCALE_SMOOTH);
		
		/* Add images */
		lblImage = new JLabel("");
		lblImage.setBounds(35, 50, 120, 70);
		lblImage.setIcon(new ImageIcon(scaledImage));
		add(lblImage);
		
		/* Set a label */
		lblInfo = new JLabel("EMPTY ROOM", JLabel.CENTER);
		lblInfo.setBounds(20, 135, 150, 20);
		lblInfo.setFont(font);
		lblInfo.setForeground(Color.PINK);
		add(lblInfo);
	}
}
