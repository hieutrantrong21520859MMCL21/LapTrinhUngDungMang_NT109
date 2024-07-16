package view;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import controller.ServerController;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.SwingConstants;
import javax.swing.JTextArea;

public class Server extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	/* Components */
	private JPanel contentPane;
	private JPanel pnLog;
	private JLabel lblServerPort;
	private JTextField tfPort;
	private JTextArea taLog;
	private JButton btnStart;
	private JButton btnStop;
	private JScrollPane spLog;
	
	private ServerController controller;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server frame = new Server();
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
	public Server()
	{
		// Initialize components
		initialize();
		
		// Initialize the listener
		controller = new ServerController(this);
		
		// Set title
		setTitle("Server");
		
		// Set fixed size
		setResizable(false);
		
		// Set the frame's position at center
		setLocationRelativeTo(null);
		
		// Add buttons' events
		btnStart.addActionListener(controller);
		btnStop.addActionListener(controller);
	}
	
	public String getServerPort()
	{
		return tfPort.getText().toString();
	}
	
	public void setButtonStartEnabled(boolean isEnabled)
	{
		btnStart.setEnabled(isEnabled);
	}
	
	public void setButtonStopEnabled(boolean isEnabled)
	{
		btnStop.setEnabled(isEnabled);
	}
	
	public synchronized void writeLog(String msg)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		// Write datetime and message to the log
		taLog.append(sdf.format(new Date()) + " - " + msg + "\n");
	}
	
	private void initialize()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 540, 430);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		/* Label "Port" */
		lblServerPort = new JLabel("Port");
		lblServerPort.setHorizontalAlignment(SwingConstants.CENTER);
		lblServerPort.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblServerPort.setBounds(26, 34, 45, 13);
		contentPane.add(lblServerPort);
		
		/* TextField Port */
		tfPort = new JTextField();
		tfPort.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tfPort.setHorizontalAlignment(SwingConstants.CENTER);
		tfPort.setBounds(95, 28, 166, 25);
		contentPane.add(tfPort);
		tfPort.setColumns(10);
		
		/* Set a loggin panel */
		pnLog = new JPanel(null);
		pnLog.setBounds(26, 167, 490, 220);
		
		// Custom a border of the panel
		TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "Logging");
		border.setTitleFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		border.setTitleColor(Color.BLACK);
		border.setTitleJustification(TitledBorder.LEADING);
		border.setTitlePosition(TitledBorder.TOP);
		pnLog.setBorder(border);
		
		
		/* TextArea */
		taLog = new JTextArea();
		taLog.setFocusable(false);
		taLog.setBounds(10, 20, 470, 190);
		taLog.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		// Set padding
		taLog.setBorder(BorderFactory.createCompoundBorder(taLog.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		
		// Set a new scroll pane that contains taLog
		spLog = new JScrollPane(taLog);
		spLog.setBounds(10, 20, 470, 190);
		spLog.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		spLog.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		pnLog.add(spLog);
		
		// Add the panel to the parent panel
		contentPane.add(pnLog);
		
		/* Button "Start" */
		btnStart = new JButton("Start");
		btnStart.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnStart.setBounds(355, 28, 99, 32);
		contentPane.add(btnStart);
		
		/* Button "Stop" */
		btnStop = new JButton("Stop");
		btnStop.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnStop.setBounds(355, 98, 99, 32);
		btnStop.setEnabled(false);
		contentPane.add(btnStop);
	}
}
