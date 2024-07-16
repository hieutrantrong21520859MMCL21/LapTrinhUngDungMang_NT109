package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JOptionPane;

import model.Player;
import view.Lobby;
import view.Login;
import view.PlayerUI;

public class LoginController implements ActionListener
{
	private Login login;
	
	public LoginController(Login login)
	{
		this.login = login;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String comm = e.getActionCommand();
		
		login.createPlayer();
		
		if (comm.equals("Choose a room"))
		{
			System.out.println("this is " + login.getPlayer().getName());
			login.getPlayer().sendMessage("/lobby");
		}
	}
}