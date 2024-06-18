package de.kobich.tictactoe.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.kobich.tictactoe.TicTacToeImage;

/**
 * About dialog.
 */
public class AboutDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 530;
	private static final int HEIGHT = 280;

	private Container m_contentPane;

	public AboutDialog(JFrame owner) {
		super(owner, "About", true);

		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(owner);
		m_contentPane = getContentPane();
		m_contentPane.setLayout(new BorderLayout());
		addKeyListener(new AboutDialogKeyListener());

		makeImage();
		makeInfo();
	}

	private void makeImage() {
		JLabel imageLabel = new JLabel(TicTacToeImage.ICON_LARGE.getImageIcon());
		imageLabel.setBackground(getBackground());
		imageLabel.setOpaque(true);
		Box b = Box.createVerticalBox();
		Box.createVerticalStrut(10);
		b.add(imageLabel);
		JPanel west = new JPanel();
		west.add(b);
		m_contentPane.add(west, BorderLayout.WEST);
	}

	private void makeInfo() {
		JPanel info = new JPanel(new BorderLayout(5, 5));
		JLabel title = new JLabel("<html><H1>Tic Tac Toe</H1><HR></html>");

		JPanel center = new JPanel(new GridLayout(6, 2, 0, 5));
		center.add(new JLabel("Version:"));
		center.add(new JLabel(makeBold("1.1")));
		center.add(new JLabel("Autor:"));
		center.add(new JLabel(makeBold("Christoph Korn")));
		center.add(new JLabel("Java:"));
		center.add(new JLabel(makeBold(System.getProperty("java.version"))));
		center.add(new JLabel("Virtual Machine:"));
		center.add(new JLabel(makeBold(System.getProperty("java.vm.name"))));
		info.add(title, BorderLayout.NORTH);
		info.add(center, BorderLayout.CENTER);
		info.add(new JPanel(), BorderLayout.WEST);
		m_contentPane.add(info, BorderLayout.CENTER);
	}

	/**
	 * Returns string in bold
	 */
	private String makeBold(String s) {
		return "<html><b>" + s + "</b></html>";
	}

	private class AboutDialogKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyChar();

			if (keyCode == KeyEvent.VK_ESCAPE) {
				AboutDialog.this.setVisible(false);
			}
		}
	}
}