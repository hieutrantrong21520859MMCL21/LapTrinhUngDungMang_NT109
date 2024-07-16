package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

import view.PlayerUI;

public class PlayerUIController implements FocusListener, ActionListener
{
	private PlayerUI playerUI;
	
	public PlayerUIController(PlayerUI playerUI)
	{
		this.playerUI = playerUI;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Socket socket = playerUI.getLoginObj().getPlayer().getSocket();
		String playerName = playerUI.getLoginObj().getPlayer().getName();
		String msg = playerUI.getMessageText();
		String send = "/chat" + "-" + playerName + "-" + msg;
		
		try
		{
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			bw.write(send);
			bw.newLine();
			bw.flush();
			
			// Display message
			playerUI.displayMessage(msg);
			
			// Clear message box
			playerUI.clearMessageText();
		}
		catch (Exception ex)
		{
			
		}
	}

	/* Set a placeholder on message box */
	@Override
	public void focusGained(FocusEvent e)
	{
		if (playerUI.getMessageText().equals("Enter your message ..."))
		{
			playerUI.customMessageBox("", Color.BLACK);
		}
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		if (playerUI.getMessageText().equals(""))
		{
			playerUI.customMessageBox("Enter your message ...", Color.GRAY);
		}
	}
}