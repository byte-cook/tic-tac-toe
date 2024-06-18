package de.kobich.tictactoe.ui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import de.kobich.tictactoe.FieldState;
import de.kobich.tictactoe.TicTacToeImage;

/**
 * UI field.
 */
public class FieldLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	private ImageIcon m_userImage;
	private ImageIcon m_computerImage;
	private ImageIcon m_userWinsImage;
	private ImageIcon m_computerWinsImage;

	public FieldLabel() {
		super();
		setHorizontalAlignment(JLabel.CENTER);

		m_userImage = TicTacToeImage.USER.getImageIcon();
		m_computerImage = TicTacToeImage.COMPUTER.getImageIcon();
		m_userWinsImage = TicTacToeImage.USER_WINS.getImageIcon();
		m_computerWinsImage = TicTacToeImage.COMPUTER_WINS.getImageIcon();
	}

	/**
	 * Returns whether this field is already occupied (actual state)
	 */
	public boolean isSelected() {
		return getIcon() != null;
	}

	/**
	 * Sets this fields as occupied (actual state)
	 * @param id USER, COMPUTER, USER_WINS, COMPUTER_WINS
	 */
	public void setSelected(FieldState id) {
		if (!isSelected()) {
			if (id == FieldState.USER) {
				setIcon(m_userImage);
			}
			else if (id == FieldState.COMPUTER) {
				setIcon(m_computerImage);
			}
		}
		// else {
		if (id == FieldState.USER_WINS) {
			setIcon(m_userWinsImage);
		}
		else if (id == FieldState.COMPUTER_WINS) {
			setIcon(m_computerWinsImage);
		}
		// }
	}

	/**
	 * Initializes this field for a new game
	 */
	public void newGame() {
		setIcon(null);
	}

}