package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;

import model.Player;
import model.RoomPanel;
import view.WaitingDialog;

public class WaitingController extends WindowAdapter implements ActionListener
{
	private WaitingDialog dialog;

	private RoomPanel roomPanel;
	
	private Player player;
	
	private String status;
	
	public WaitingController(WaitingDialog dialog)
	{
		this.dialog = dialog;
		roomPanel = dialog.getRoomPanel();
		player = roomPanel.getLobbyObj().getLoginObj().getPlayer();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String comm = e.getActionCommand();
		
		if (comm.equals("Ready"))
		{
			status = "ready";
			dialog.changeStatus("Not ready", "YOU ARE READY", Color.BLUE);
			roomPanel.setImage("/images/1_player.png");
			roomPanel.setInfo("1/2");
		}
		
		else if (comm.equals("Not ready"))
		{
			status = "unready";
			dialog.changeStatus("Ready", "YOU ARE NOT READY", Color.RED);
		}
		
		String send = "/enter" + " " + dialog.getRoomId() + " " + status;
		player.sendMessage(send);
	}
	
	@Override
	public void windowClosing(WindowEvent e)
	{/*
		String send = "/enter" + " " + frame.getRoomId() + " " + "unready";
		player.sendMessage(send);
		System.out.println("frame: " + send);*/
		System.out.println("closed");
		//dialog.setRunning(false);
		//dialog.dispose();
	}
}