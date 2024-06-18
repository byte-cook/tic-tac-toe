package de.kobich.tictactoe.ui;

import javax.swing.JOptionPane;

public class UIUtils {
	/**
	 * Shows error dialog
	 * @param title 
	 * @param message
	 */
	public static void showError(String title, String message) {
		JOptionPane.showMessageDialog(null, " " + message, title, JOptionPane.ERROR_MESSAGE);
	}
}