package view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import model.GamePanel;
import model.Player;
import javax.swing.border.TitledBorder;

import controller.PlayerUIController;

import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class PlayerUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	/* Components */
	private JPanel contentPane;
	private JPanel pnChatbox;
	private JPanel pnInfo;
	private JTextArea taChatbox;
	private JTextArea taMessage;
	private JButton btnSend;
	private JLabel lblInfo;
	private JScrollPane spChatbox;
	private JScrollPane spMessage;
	
	private GamePanel gamePanel;
	
	private Login login;
	
	private String type;
	
	private PlayerUIController controller;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{	
					//PlayerUI frame = new PlayerUI();
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
	public PlayerUI(Login login) // test constructor PlayerUI()
	{
		this.login = login;
		type = login.getPlayer().getType();
		
		// Initialize components
		initialize();
		
		// Set icon
		setIconImage(Toolkit.getDefaultToolkit().getImage(PlayerUI.class.getResource("/images/login_icon.png")));
		
		// Set title
		setTitle(login.getPlayer().getName());
		
		// Set fixed size
		setResizable(false);
		
		// Set the frame's position at center
		setLocationRelativeTo(null);
		
		// Set a timer updating the player's UI
		updateUI();
		
		// Add events controller
		controller = new PlayerUIController(this);
		taMessage.addFocusListener(controller);
		btnSend.addActionListener(controller);
	}
	
	public String getMessageText()
	{
		return taMessage.getText();
	}
	
	public void clearMessageText()
	{
		taMessage.setText("");
	}
	
	public Login getLoginObj()
	{
		return login;
	}
	
	public void customMessageBox(String text, Color color)
	{
		taMessage.setText(text);
		taMessage.setForeground(color);
	}
	
	public void displayMessage(String msg)
	{
		taChatbox.append(msg + "\n");
	}
	
	public void updateTurnInfo(String msg)
	{
		lblInfo.setText(msg);
	}
	
	private void initialize()
	{
		try
		{
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(10, 10, 950, 620);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

			setContentPane(contentPane);
			contentPane.setLayout(null);
			
			/* Set a chatbox panel */
			pnChatbox = new JPanel(null);
			pnChatbox.setBounds(532, 55, 394, 518);
			
			// Custom a border of the panel
			TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "Chatbox");
			border.setTitleFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
			border.setTitleColor(Color.BLACK);
			border.setTitleJustification(TitledBorder.LEADING);
			border.setTitlePosition(TitledBorder.TOP);
			pnChatbox.setBorder(border);
			
			/* Custom a new TextArea for chatting */
			taChatbox = new JTextArea();
			taChatbox.setEditable(false);
			taChatbox.setBackground(Color.WHITE);
			taChatbox.setFont(new Font("Tahoma", Font.PLAIN, 13));
			
			// Set padding
			taChatbox.setBorder(BorderFactory.createCompoundBorder(taChatbox.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			
			// Set a new scroll pane that contains taChatbox
			spChatbox = new JScrollPane(taChatbox);
			spChatbox.setBounds(10, 20, 374, 436);
			spChatbox.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			spChatbox.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			pnChatbox.add(spChatbox);
			
			/* Custom a new TextArea for editing messages */
			taMessage = new JTextArea("Enter your message ...");
			taMessage.setFont(new Font("Tahoma", Font.PLAIN, 13));
			taMessage.setForeground(Color.GRAY);
			taMessage.setColumns(10);
			
			// Set padding
			taMessage.setBorder(BorderFactory.createCompoundBorder(taMessage.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			
			// Set a new scroll pane that contains taMessage
			spMessage = new JScrollPane(taMessage);
			spMessage.setBounds(10, 466, 291, 42);
			spMessage.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			spMessage.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			pnChatbox.add(spMessage);
			
			/* Add a new button to the panel */
			btnSend = new JButton("Send");
			btnSend.setBounds(311, 466, 73, 42);
			btnSend.setFont(new Font("Tahoma", Font.PLAIN, 13));
			pnChatbox.add(btnSend);
			
			// Add the panel to the parent panel
			contentPane.add(pnChatbox);
			
			/* Panel informs that this is the players' turn */
			pnInfo = new JPanel(null);
			pnInfo.setBounds(10, 10, 512, 41);
			pnInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			pnInfo.setBackground(Color.WHITE);
			
			// Add a label
			lblInfo = new JLabel("Player's turn");
			lblInfo.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblInfo.setForeground(Color.BLUE);
			lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
			lblInfo.setBounds(10, 10, 492, 21);
			pnInfo.add(lblInfo);
			
			// Add the panel to the parent panel
			contentPane.add(pnInfo);
			
			/* Set a chess panel */
			gamePanel = new GamePanel(this);
			gamePanel.setBounds(10, 61, 512, 512);
			
			
			if (type.equals("white"))
			{
				gamePanel.setCurrentColor(1);
				gamePanel.setTurn(true);
				lblInfo.setText("You go first!");
			}
			else
			{
				gamePanel.setCurrentColor(0);
				gamePanel.setTurn(false);
				lblInfo.setText("The Whites go first!");
			}
			
			// Setup pieces
			gamePanel.setPieces();
			
			// Add the chess board to the parent panel
			contentPane.add(gamePanel);
		}
		catch (Exception e)
		{
			
		}
	}
	
	private void updateUI()
	{
		TimerTask task = new TimerTask() {
			
			@Override
			public void run()
			{
				String received = login.getReceived();
				
				if (received.startsWith("/chat"))
				{
					String opponentName = received.split("-")[1];
					String msg = received.split("-")[2];
					
					// Display opponent's message
					displayMessage(opponentName + ": " + msg);
					
					//login.setReceived("");
				}
				
				else if (received.startsWith("/movement"))
				{
					// Update turn info
					updateTurnInfo("It's your turn!");
					
					String movementInfo[] = received.split(" ");
					
					int selectedPieceCol = Integer.parseInt(movementInfo[1]);
					int selectedPieceRow = Integer.parseInt(movementInfo[2]);
					int newCol = Integer.parseInt(movementInfo[3]);
					int newRow = Integer.parseInt(movementInfo[4]);
					
					/* Check if the captured piece's info is sent */
					if (movementInfo.length > 6)
					{
						int capturedPieceCol = Integer.parseInt(movementInfo[5]);
						int capturedPieceRow = Integer.parseInt(movementInfo[6]);
						
						// Set the captured piece
						gamePanel.setCapturedPiece(gamePanel.getPieceAt(capturedPieceCol, capturedPieceRow));
						
						// Set the player's turn
						gamePanel.setTurn(Boolean.parseBoolean(movementInfo[7]));
					}
					
					else
					{
						// Set the player's turn
						gamePanel.setTurn(Boolean.parseBoolean(movementInfo[5]));
					}
					
					gamePanel.setSelectedPiece(gamePanel.getPieceAt(selectedPieceCol, selectedPieceRow));
					gamePanel.movePiece(gamePanel.getSelectedPiece(), newCol, newRow);
					
					// Set the selected piece to null because highlighting squares is not necessary
					gamePanel.setSelectedPiece(null);
					
					// Reload the chess board
					gamePanel.repaint();
					
					//login.setReceived("");
				}
				
				login.setReceived("");
			}
		};
		
		new Timer().schedule(task, 0, 500);
	}
}
