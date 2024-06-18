package de.kobich.tictactoe.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Statistics dialog
 */
public class StatisticDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 360;
	private static final int HEIGHT = 260;

	private Container m_contentPane;
	private JLabel m_nUserWins;
	private JLabel m_nComWins;
	private JLabel m_nDraws;
	private JPanel center;
	private JLabel m_nGames;

	public StatisticDialog(JFrame owner) {
		super (owner, "Statistics", true);

		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(owner);
		m_contentPane = getContentPane();
		m_contentPane.setLayout(new BorderLayout(5, 5));
		addKeyListener(new StatisticKeyListener());

		makeInfo();
		makeButton();
	}

	/**
	 * Sets the current statistics
	 */
	public void setStatistic(int nGames, int nUserWins, int nComWins, int nDraws) {
		m_nGames.setText(java.lang.String.valueOf(nGames));
		m_nUserWins.setText(java.lang.String.valueOf(nUserWins));
		m_nComWins.setText(java.lang.String.valueOf(nComWins));
		m_nDraws.setText(java.lang.String.valueOf(nDraws));
	}

	private void makeInfo() {
		center = new JPanel(new GridLayout(5, 1, 0, 10));
		m_nUserWins = new JLabel();
		m_nComWins = new JLabel();
		m_nDraws = new JLabel();
		m_nGames = new JLabel();

		center.add(makeBox(new JLabel("Total number of games:"), m_nGames));
		center.add(makeBox(new JLabel("Number of user wins:"), m_nUserWins));
		center.add(makeBox(new JLabel("Number of draws:"), m_nDraws));
		center.add(makeBox(new JLabel("Number of computer wins:"), m_nComWins));

		m_contentPane.add(center, BorderLayout.CENTER);
		m_contentPane.add(new JPanel(), BorderLayout.NORTH);
		m_contentPane.add(new JPanel(), BorderLayout.WEST);
		m_contentPane.add(new JPanel(), BorderLayout.EAST);
	}

	private void makeButton() {
		JPanel south = new JPanel();
		JButton ok = new JButton("Close");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		south.add(ok);
		m_contentPane.add(south, BorderLayout.SOUTH);
	}

	/**
	 * Returns Box with two labels
	 * @param left 
	 * @param right
	 */
	private Box makeBox(JLabel left, JLabel right) {
		Box b = Box.createHorizontalBox();

		b.add(Box.createHorizontalStrut(10));
		b.add(left);
		b.add(Box.createHorizontalGlue());
		b.add(right);
		b.add(Box.createHorizontalStrut(10));

		return b;
	}

	private class StatisticKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyChar();

			if (keyCode == KeyEvent.VK_ESCAPE) {
				StatisticDialog.this.setVisible(false);
			}
		}
	}
}