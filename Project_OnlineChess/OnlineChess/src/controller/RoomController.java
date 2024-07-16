package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import javax.swing.JOptionPane;

import model.Player;
import model.RoomPanel;
import view.WaitingDialog;

public class RoomController extends MouseAdapter
{
	private RoomPanel roomPanel;
	
	public RoomController(RoomPanel roomPanel)
	{
		this.roomPanel = roomPanel;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (!roomPanel.getInfo().equals("IN GAME"))
		{
			/*
			int id = roomPanel.getId();
			
			new WaitingDialog(roomPanel.getLobbyObj(), id).setVisible(true); // test: WaitingFrame(Lobby,id)
			
			// Send check request
			Player player = roomPanel.getLobbyObj().getLoginObj().getPlayer();
			
			try
			{
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(player.getSocket().getOutputStream()));
				bw.write("/check" + " " + id);
				bw.newLine();
				bw.flush();
				
				System.out.println("msg sent");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}*/
			
			int id = roomPanel.getId();
			Player player = roomPanel.getLobbyObj().getLoginObj().getPlayer();
			
			try
			{
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(player.getSocket().getOutputStream()));
				bw.write("/check" + " " + id);
				bw.newLine();
				bw.flush();
				
				System.out.println("msg sent");
				
				new WaitingDialog(roomPanel).setVisible(true);
			}
			
			catch (Exception ex)
			{
				
			}
		}
		
		else
		{
			JOptionPane.showConfirmDialog(null, "This room is full!", "Announcement!!", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}