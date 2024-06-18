package de.kobich.tictactoe.control;

import de.kobich.tictactoe.FieldState;
import de.kobich.tictactoe.Player;

/**
 * One field on the pitch for calculation. Changes in the class does not effect UI. 
 */
public class Field {
	private final int m_index;
	private FieldState m_status;

	public Field(int index) {
		m_index = index;
		newGame();
	}

	public FieldState getStatus() {
		return m_status;
	}

	/**
	 * Sets new field state. Has no effect on UI.
	 */
	public void setStatus(FieldState state) {
		m_status = state;
	}
	
	public void setStatusByPlayer(Player player) {
		switch (player) {
			case COMPUTER:
				setStatus(FieldState.COMPUTER);
				break;
			case USER:
				setStatus(FieldState.USER);
				break;
		}
	}

	public int getIndex() {
		return m_index;
	 }

	public void newGame() {
		setStatus(FieldState.FREE);
	}

}