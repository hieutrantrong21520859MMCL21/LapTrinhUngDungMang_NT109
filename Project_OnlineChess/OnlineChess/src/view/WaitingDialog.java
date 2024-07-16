package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.WaitingController;
import model.RoomPanel;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingConstants;

public class WaitingDialog extends JDialog // test: change from jframe to jdialog
{
	private static final long serialVersionUID = 1L;
	
	/* Components */
	private JPanel contentPane;
	private JLabel lblPlayerName;
	private JLabel lblOpponentName;
	private JLabel lblPlayerStatus;
	private JLabel lblOpponentStatus;
	private JLabel lblOpponentConfirm;
	private JButton btnConfirm;
	
	/* Add a new font */
	private File fontStyle;
	private Font font;
	
	private RoomPanel roomPanel;
	
	private WaitingController controller;
	
	boolean isRunning = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//WaitingFrame frame = new WaitingFrame();
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
	public WaitingDialog(RoomPanel roomPanel)
	{
		this.roomPanel = roomPanel;
		//isRunning = true;
		
		try
		{
			fontStyle = new File("fonts/Aloevera-OVoWO.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, fontStyle).deriveFont(13f);
			
			// Initialize components
			initialize();
			
			// Set icon
			setIconImage(Toolkit.getDefaultToolkit().getImage(WaitingDialog.class.getResource("/images/login_icon.png")));
						
			// Set a title
			setTitle(roomPanel.getLobbyObj().getLoginObj().getPlayer().getName() + " - Room " + roomPanel.getId());
						
			// Set fixed size
			setResizable(false);
			
			setModal(true);
						
			// Set position at center
			setLocationRelativeTo(null);
			
			// Add events controller
			controller = new WaitingController(this);
			btnConfirm.addActionListener(controller);
			addWindowListener(controller);
			
			// Set a timer updating status
			new Thread(() -> updateState(this)).start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public int getRoomId()
	{
		return roomPanel.getId();
	}
	
	public synchronized RoomPanel getRoomPanel()
	{
		return roomPanel;
	}
	
	public void changeStatus(String buttonStatus, String labelStatus, Color statusColor)
	{
		btnConfirm.setText(buttonStatus);
		lblPlayerStatus.setText(labelStatus);
		lblPlayerStatus.setForeground(statusColor);
	}
	
	public void setRunning(boolean isRunning)
	{
		this.isRunning = isRunning;
	}
	
	private void initialize()
	{
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 180);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		/* Label for player's name */
		lblPlayerName = new JLabel(roomPanel.getLobbyObj().getLoginObj().getPlayer().getName());
		lblPlayerName.setFont(new Font("SansSerif", Font.BOLD, 13));
		lblPlayerName.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerName.setBounds(36, 24, 151, 25);
		contentPane.add(lblPlayerName);
		
		/* Label for player's status */
		lblPlayerStatus = new JLabel("YOU ARE NOT READY");
		lblPlayerStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerStatus.setBounds(36, 58, 151, 25);
		lblPlayerStatus.setFont(font);
		lblPlayerStatus.setForeground(Color.RED);
		contentPane.add(lblPlayerStatus);
		
		/* Button "Ready" for the player */
		btnConfirm = new JButton("Ready");
		btnConfirm.setBounds(52, 104, 118, 25);
		btnConfirm.setFont(font);
		contentPane.add(btnConfirm);
		
		/* Label for opponent's name */
		lblOpponentName = new JLabel("WAITING PLAYER ...");
		lblOpponentName.setFont(new Font("SansSerif", Font.BOLD, 13));
		lblOpponentName.setHorizontalAlignment(SwingConstants.CENTER);
		lblOpponentName.setBounds(273, 24, 151, 25);
		contentPane.add(lblOpponentName);
		
		/* Label for opponent's status */
		lblOpponentStatus = new JLabel("WAITING YOUR OPPONENT ...");
		lblOpponentStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblOpponentStatus.setBounds(232, 58, 244, 25);
		lblOpponentStatus.setFont(font);
		lblOpponentStatus.setForeground(Color.RED);
		contentPane.add(lblOpponentStatus);
		
		/* Label "Not ready" for the opponent */
		lblOpponentConfirm = new JLabel("Not ready", JLabel.CENTER);
		lblOpponentConfirm.setBounds(289, 104, 118, 25);
		lblOpponentConfirm.setFont(font);
		contentPane.add(lblOpponentConfirm);
	}
	
	private void updateState(WaitingDialog dialog)
	{
		/*
		Timer timer = new Timer();
		
		TimerTask task = new TimerTask()
		{	
			@Override
			public void run()
			{
				String received = roomPanel.getLobbyObj().getLoginObj().getReceived();
				
				if(received.startsWith("/accepted"))
				{
					int type = Integer.parseInt(received.split(" ")[2]);
					
					if (type == 1)
					{
						roomPanel.getLobbyObj().getLoginObj().getPlayer().setType("white");
					}
					
					else
					{
						roomPanel.getLobbyObj().getLoginObj().getPlayer().setType("black");
					}
					
					new PlayerUI(roomPanel.getLobbyObj().getLoginObj()).setVisible(true);
					
					System.out.println("dialog closed");
					timer.cancel();
					dispose();
				}
				
				else if (received.startsWith("/checked"))
				{
					// Update opponent's status
					lblOpponentName.setText(received.split(" ")[1]);
					
					lblOpponentStatus.setText("YOUR OPPONENT IS READY");
					lblOpponentStatus.setForeground(Color.BLUE);
					
					lblOpponentConfirm.setText("Ready");
				}
			}
		};
		
		timer.schedule(task, 0, 3000);*/
		
		RoomPanel roomPanel = dialog.getRoomPanel();
		//dialog.isRunning = true;
		
		while (dialog.isRunning)
		{
			//String received = roomPanel.getLobbyObj().getLoginObj().getReceived();
			String received = roomPanel.getLobbyObj().getLoginObj().getReceivedForDialogThread();
			
			//System.out.println("dialog: " + received);
			
			if(received.startsWith("/accepted"))
			{
				int type = Integer.parseInt(received.split(" ")[2]);
				
				if (type == 1)
				{
					roomPanel.getLobbyObj().getLoginObj().getPlayer().setType("white");
				}
				
				else
				{
					roomPanel.getLobbyObj().getLoginObj().getPlayer().setType("black");
				}
				
				new PlayerUI(roomPanel.getLobbyObj().getLoginObj()).setVisible(true);
				
				System.out.println("dialog closed");
				dialog.isRunning = false;
				dialog.dispose();
			}
			
			else if (received.startsWith("/checked"))
			{
				// Update opponent's status
				dialog.lblOpponentName.setText(received.split(" ")[1]);
				
				dialog.lblOpponentStatus.setText("YOUR OPPONENT IS READY");
				dialog.lblOpponentStatus.setForeground(Color.BLUE);
				
				dialog.lblOpponentConfirm.setText("Ready");
			}
		}
	}
}
