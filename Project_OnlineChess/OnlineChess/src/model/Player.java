package model;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Player
{
	private Socket socket;
	private int id;
	private String name;
	private String type;
	private int roomId;
	private String status;
	
	public Player(Socket socket, String name, String type) throws Exception
	{
		this.socket = socket;
		this.name = name;
		this.type = type;
	}
	
	public int getId()
	{
		return id;
	}
	
	public synchronized void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public Socket getSocket()
	{
		return socket;
	}

	public void setSocket(Socket socket)
	{
		this.socket = socket;
	}
	
	public int getRoomId()
	{
		return roomId;
	}

	public synchronized void setRoomId(int roomId)
	{
		this.roomId = roomId;
	}

	public String getStatus()
	{
		return status;
	}

	public synchronized void setStatus(String status)
	{
		this.status = status;
	}

	public void sendMessage(String msg)
	{
		try
		{
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			bw.write(msg);
			bw.newLine();
			bw.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean isReadyAtRoom(int roomId)
	{
		return this.roomId == roomId && status.equals("ready");
	}
}