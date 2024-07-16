package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.LoginController;
import model.Player;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.SwingConstants;

public class Login extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	/* Components */
	private JPanel contentPane;
	private JLabel lblServerIP;
	private JLabel lblServerPort;
	private JLabel lblPlayerName;
	private JTextField tfName;
	private JTextField tfIP;
	private JTextField tfPort;
	private JButton btnRandom;
	private JButton btnChoose;
	private JLabel lblImage;

	private Player player;
	
	private LoginController controller;
	
	private String received;
	private String receivedForLobbyThread;
	private String receivedForDialogThread;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login()
	{
		try
		{
			// Initialize components
			initialize();
			
			// Initialize a new player
			player = new Player(null, "", "");
			
			// Set the listener
			controller = new LoginController(this);
			
			// Set icon
			setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/images/login_icon.png")));
			
			// Set title
			setTitle("Online Chess - Login");
			
			// Set fixed frame's size
			setResizable(false);
			
			// Set position at center
			setLocationRelativeTo(null);
			
			// Add buttons' events
			btnChoose.addActionListener(controller);
		}
		catch (Exception e)
		{
			
		}
	}
	
	private void initialize()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		/* Label Server IP */
		lblServerIP = new JLabel("Server's IP");
		lblServerIP.setHorizontalAlignment(SwingConstants.RIGHT);
		lblServerIP.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblServerIP.setBounds(41, 35, 90, 13);
		contentPane.add(lblServerIP);
		
		/* Label Server port */
		lblServerPort = new JLabel("Server's Port");
		lblServerPort.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblServerPort.setBounds(41, 91, 90, 13);
		contentPane.add(lblServerPort);
		
		/* Button "Random a new game"
		btnRandom = new JButton("Random a new game");
		btnRandom.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnRandom.setBounds(41, 181, 161, 32);
		contentPane.add(btnRandom);*/
		
		/* Button "Choose a room" */
		btnChoose = new JButton("Choose a room");
		btnChoose.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnChoose.setBounds(139, 183, 175, 50);
		contentPane.add(btnChoose);
		
		/* TextField IP */
		tfIP = new JTextField();
		tfIP.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tfIP.setHorizontalAlignment(SwingConstants.CENTER);
		tfIP.setBounds(153, 30, 245, 25);
		contentPane.add(tfIP);
		tfIP.setColumns(10);
		
		/* TextField Port */
		tfPort = new JTextField();
		tfPort.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tfPort.setHorizontalAlignment(SwingConstants.CENTER);
		tfPort.setBounds(155, 85, 245, 25);
		contentPane.add(tfPort);
		tfPort.setColumns(10);
		
		/* Label Name */
		lblPlayerName = new JLabel("Name");
		lblPlayerName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPlayerName.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblPlayerName.setBounds(41, 143, 90, 13);
		contentPane.add(lblPlayerName);
		
		/* TextField Name */
		tfName = new JTextField();
		tfName.setHorizontalAlignment(SwingConstants.CENTER);
		tfName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tfName.setBounds(153, 137, 245, 25);
		contentPane.add(tfName);
		tfName.setColumns(10);
		
		/* Background image */
		lblImage = new JLabel("");
		lblImage.setBounds(0, 0, 436, 263);
		
		// Scale image
		ImageIcon icon = new ImageIcon(Login.class.getResource("/images/login_background.jpg"));
		Image img = icon.getImage();
		Image scaled = img.getScaledInstance(lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_SMOOTH);
		lblImage.setIcon(new ImageIcon(scaled));
		contentPane.add(lblImage);
	}
	
	public synchronized Player getPlayer()
	{
		return player;
	}
	
	public synchronized void setReceived(String received)
	{
		this.received = received;
	}
	
	public synchronized String getReceived()
	{
		return received;
	}
	
	public String getReceivedForLobbyThread() {
		return receivedForLobbyThread;
	}

	public void setReceivedForLobbyThread(String receivedForLobbyThread) {
		this.receivedForLobbyThread = receivedForLobbyThread;
	}

	public String getReceivedForDialogThread() {
		return receivedForDialogThread;
	}

	public void setReceivedForDialogThread(String receivedForDialogThread) {
		this.receivedForDialogThread = receivedForDialogThread;
	}

	public void createPlayer()
	{
		String serverIP = tfIP.getText().toString();
		String serverPort = tfPort.getText().toString();
		String playerName = tfName.getText().toString();
		
		try
		{
			player.setSocket(new Socket(serverIP, Integer.parseInt(serverPort)));
			player.setName(playerName);
			
			// Send player's information
			String msg = "/login" + " " + player.getName() + " " + player.getSocket().getLocalPort();
			player.sendMessage(msg);
			
			// Start listening thread
			new Thread(() -> listenThread(this)).start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void listenThread(Login login)
	{
		Player player = login.getPlayer();
		
		try
		{
			while (true)
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(player.getSocket().getInputStream()));
				String received = br.readLine();
				System.out.println("from server: " + received);
				login.setReceived(received);
				login.setReceivedForLobbyThread(received);
				login.setReceivedForDialogThread(received);
				
				if (received.startsWith("/login-ok"))
				{
					new Lobby(login).setVisible(true);
					login.dispose();
				}
			}
		}
		
		catch (Exception e)
		{
			e.printStackTrace();	
		}
	}
}
