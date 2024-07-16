package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import model.Player;
import view.Server;

public class ServerController implements ActionListener
{
	private final int MAX_PLAYERS = 24; // real is 24
	
	private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
	private ArrayList<Player> players = new ArrayList<Player>();
	
	private Server server;
	private ServerSocket serverSocket;
	
	boolean isRunning = true;
	
	public ServerController(Server server)
	{
		this.server = server;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		//String port = server.getServerPort();
		String comm = e.getActionCommand();
		
		if (comm.equals("Start"))
		{
			// Disable button "Start"
			server.setButtonStartEnabled(false);
			
			// Enable button "Stop"
			server.setButtonStopEnabled(true);
			
			startServer();
		}
		else if (comm.equals("Stop"))
		{
			// Enable button "Start"
			server.setButtonStartEnabled(true);
						
			// Disable button "Stop"
			server.setButtonStopEnabled(false);
			
			// Close the server
			try
			{
				serverSocket.close();
				System.out.println("Server closed!!");
				server.writeLog("Server closed!!");
				isRunning = false;
			}
			catch (Exception ex)
			{
				
			}
		}	
	}
	
	private void startServer()
	{
		String port = server.getServerPort();
		
		try
		{
			// Create a socket at specified port
			serverSocket = new ServerSocket(Integer.parseInt(port));
			System.out.println("Server is listening at port " + serverSocket.getLocalPort());
			server.writeLog("Server is listening at port " + serverSocket.getLocalPort());
			
			// Start listening thread
			new Thread(() -> listenThread()).start();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public synchronized ArrayList<Player> getPlayers()
	{
		return players;
	}
	
	public Server getServerObj()
	{
		return server;
	}
	
	private void listenThread()
	{
		try
		{
			while (!serverSocket.isClosed())
			{
				Socket socket = serverSocket.accept();
				
				// Receive and analyze player's information
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				
				String received = br.readLine();
				
				String name = received.split(" ")[1];
				int id = Integer.parseInt(received.split(" ")[2]);
				
				System.out.println("Player (id: " + id + ", name: " + name + ") has joined!");
				server.writeLog("Player (id: " + id + ", name: " + name + ") has joined!");
				
				// Add a new player to the list
				Player player = new Player(socket, name, "");
				player.setId(id);
				players.add(player);
				
				if (players.size() > MAX_PLAYERS)
				{
					int lastPos = players.size() - 1;
					
					// Reject the new added client
					//player.sendMessage("/login-error");
					//msg = "/error" + " " + "Sorry, the lobby is full! Please try again later";
					
					bw.write("/login-error");
					bw.newLine();
					bw.flush();
					
					players.remove(lastPos);
				}
				else
				{
					//msg = "ok" + clients.size();
					//player.sendMessage("/login-ok " + clients.size());
					
					bw.write("/login-ok" + " " + players.size());
					bw.newLine();
					bw.flush();
					
					Thread playerThread = new Thread(new ClientHandler(this, player));
					playerThread.start();
				}
			}
		}
		catch (Exception e)
		{
			
		}
	}
}