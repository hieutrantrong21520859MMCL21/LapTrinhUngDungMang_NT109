package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import model.Player;

public class ClientHandler implements Runnable
{
	private Player player;
	private ServerController server;
	private Socket socket;
	
	private BufferedReader br;
	private BufferedWriter bw;
	
	public ClientHandler(ServerController server, Player player) throws Exception
	{
		this.server = server;
		this.player = player;
		socket = player.getSocket();
		
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}
	
	@Override
	public void run() {
		try
		{
			while (socket.isConnected() && server.isRunning)
			{
				String received = br.readLine();
				String send = "";
				
				// Cases
				if (received.startsWith("/lobby"))
				{
					send = player.getName() + " entered lobby";
				}
				
				else if (received.startsWith("/enter"))
				{
					int roomId = Integer.parseInt(received.split(" ")[1]);
					String status = received.split(" ")[2];
					
					player.setRoomId(roomId);
					player.setStatus(status);
					
					if (!isMatchCreatedAtRoom(roomId))
					{
						// Announcing if there is any player readying
						if (status.equals("ready"))
						{
							broadcastMessage("/1-ready" + " " + roomId);
							
							// Write log
							server.getServerObj().writeLog("Player (id: " + player.getId() + ", name: " + player.getName() + ") is ready at room " + roomId + "!");
						}
					}
				}
				
				else if (received.startsWith("/chat") || received.startsWith("/movement"))
				{
					forwardMessage(received);
				}
				
				else if (received.startsWith("/check"))
				{
					int roomId = Integer.parseInt(received.split(" ")[1]);
					
					if (isAnyPlayerReadyAtRoom(roomId))
					{
						// Write log
					}
				}
			}
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private synchronized boolean isMatchCreatedAtRoom(int roomId)
	{
		ArrayList<Player> others = new ArrayList<Player>(server.getPlayers());
		others.remove(player);
		
		for (Player p : others)
		{
			if (p.isReadyAtRoom(roomId) && player.isReadyAtRoom(roomId))
			{
				// Announce that this room is full
				broadcastMessage("/2-ready" + " " + roomId);
				
				// Write log
				server.getServerObj().writeLog("Player (id: " + player.getId() + ", name: " + player.getName() + ") is ready at room " + roomId + "!");
				server.getServerObj().writeLog("The match at room " + roomId + " has started!");
				
				// Randomize type for players
				int type = new Random().nextInt(0, 2);
				
				p.sendMessage("/accepted" + " " + roomId + " " + type);
				player.sendMessage("/accepted" + " " + roomId + " " + (1 - type));
				
				return true;
			}
		}
		
		return false;
	}
	
	private synchronized void forwardMessage(String msg)
	{
		ArrayList<Player> others = new ArrayList<Player>(server.getPlayers());
		others.remove(player);
		
		for (Player p : others)
		{
			if (p.getRoomId() == player.getRoomId() && p.getStatus().equals("ready") && player.getStatus().equals("ready"))
			{
				p.sendMessage(msg);
				return;
			}
		}
	}
	
	private synchronized void broadcastMessage(String msg)
	{
		ArrayList<Player> others = new ArrayList<Player>(server.getPlayers());
		others.remove(player);
		
		for (Player p : others)
		{
			p.sendMessage(msg);
		}
	}
	
	private synchronized boolean isAnyPlayerReadyAtRoom(int roomId)
	{
		ArrayList<Player> others = new ArrayList<Player>(server.getPlayers());
		others.remove(player);
		
		for (Player p : others)
		{
			if (p.isReadyAtRoom(roomId))
			{
				//broadcastMessage("/checked" + " " + p.getName());
				player.sendMessage("/checked" + " " + p.getName());
				return true;
			}
		}
		
		return false;
	}
}