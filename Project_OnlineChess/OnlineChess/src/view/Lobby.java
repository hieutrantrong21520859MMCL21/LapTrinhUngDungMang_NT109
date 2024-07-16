package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.RoomPanel;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
public class Lobby extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private Login login;
	
	private ArrayList<RoomPanel> rooms = new ArrayList<RoomPanel>();
	
	boolean isRunning = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Lobby frame = new Lobby();
					//frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Lobby(Login login)
	{
		this.login = login;
		
		// Initialize components
		initialize();
		
		// Set icon
		setIconImage(Toolkit.getDefaultToolkit().getImage(Lobby.class.getResource("/images/login_icon.png")));
		
		// Set title
		setTitle("Online Chess - Lobby");
		
		// Set fixed frame's size
		setResizable(false);
		
		// Set position at center
		setLocationRelativeTo(null);
		
		// Timer for checking the room's status
		new Thread(() -> updateRoomState(this)).start();
	}
	
	public synchronized Login getLoginObj()
	{
		return login;
	}
	
	private void initialize()
	{
		try
		{
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 800, 700);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

			setContentPane(contentPane);
			contentPane.setLayout(new BorderLayout());
			
			/* Set up label greeting */
			JLabel lblGreeting = new JLabel("Online Chess");
			
			// Add a new font
			File fontStyle = new File("fonts/DecembergiftspersonaluseBdit-eZRXl.otf");
			Font font = Font.createFont(Font.TRUETYPE_FONT, fontStyle).deriveFont(80f);
			lblGreeting.setFont(font);
			lblGreeting.setForeground(Color.RED);
			lblGreeting.setHorizontalAlignment(SwingConstants.CENTER);
			
			/* Panel for greeting */
			JPanel pnGreeting = new JPanel(new BorderLayout());
			pnGreeting.add(lblGreeting, BorderLayout.CENTER);
			contentPane.add(pnGreeting, BorderLayout.NORTH);
			
			JPanel pnRooms = new JPanel(new GridLayout(3, 4, 10, 10));
			
			for (int i = 0; i < 12; i++)
			{
				RoomPanel roomPanel = new RoomPanel(this, i + 1); // test: RoomPanel(Lobby, id)
				
				// Add rooms to the list
				rooms.add(roomPanel);
				
				pnRooms.add(roomPanel);
			}
			
			contentPane.add(pnRooms, BorderLayout.CENTER);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private synchronized RoomPanel getRoomById(int roomId)
	{
		for (RoomPanel room : rooms)
		{
			if (room.getId() == roomId)
			{
				return room;
			}
		}
		
		return null;
	}
	
	private void updateRoomState(Lobby lobby)
	{
		/*
		Timer timer = new Timer();
		
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				String received = login.getReceived();
				
				if (received.startsWith("/accepted"))
				{
					System.out.println("lobby closed");
					timer.cancel();
					dispose();
				}
				
				else if (received.startsWith("/1-ready"))
				{
					int roomId = Integer.parseInt(received.split(" ")[1]);
					
					RoomPanel room = getRoomById(roomId);
					room.updateImage("/images/1_player.png");
					room.updateInfo("1/2");
					
					login.setReceived("");
				}
				
				else if (received.startsWith("/2-ready"))
				{
					int roomId = Integer.parseInt(received.split(" ")[1]);
					
					RoomPanel room = getRoomById(roomId);
					room.updateImage("/images/2_players.png");
					room.updateInfo("IN GAME");
					
					login.setReceived("");
				}
			}
		};
		
		timer.schedule(task, 0, 3000);*/
		
		Login login = lobby.getLoginObj();
		
		while (lobby.isRunning)
		{
			//String received = login.getReceived();
			String received = login.getReceivedForLobbyThread();
			
			//System.out.println("lobby: " + received);
			
			if (received.startsWith("/accepted"))
			{
				System.out.println("lobby closed");
				lobby.isRunning = false;
				lobby.dispose();
			}
			
			else if (received.startsWith("/1-ready"))
			{
				int roomId = Integer.parseInt(received.split(" ")[1]);
				
				RoomPanel room = lobby.getRoomById(roomId);
				room.setImage("/images/1_player.png");
				room.setInfo("1/2");
				
				login.setReceived("");
			}
			
			else if (received.startsWith("/2-ready"))
			{
				int roomId = Integer.parseInt(received.split(" ")[1]);
				
				RoomPanel room = lobby.getRoomById(roomId);
				room.setImage("/images/2_players.png");
				room.setInfo("IN GAME");
				
				login.setReceived("");
			}
		}
	}
}
