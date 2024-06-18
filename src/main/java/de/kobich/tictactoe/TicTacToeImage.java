package de.kobich.tictactoe;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

public enum TicTacToeImage {
	ICON("images/tictactoe-icon.gif"),
	ICON_LARGE("images/tictactoe-icon-large.gif"),
	USER("images/user.gif"),
	COMPUTER("images/computer.gif"),
	USER_WINS("images/user-wins.gif"),
	COMPUTER_WINS("images/computer-wins.gif");

	private final String path;
	
	private TicTacToeImage(String path) {
		this.path = path;
	}
	
	public ImageIcon getImageIcon() {
		java.net.URL imageURL = ClassLoader.getSystemResource(this.path);
		Image image = Toolkit.getDefaultToolkit().getImage(imageURL);
		return new ImageIcon(image);
	}
	
	public Image getImage() {
		java.net.URL imageURL = ClassLoader.getSystemResource(this.path);
		return Toolkit.getDefaultToolkit().getImage(imageURL);
	}
	
}
