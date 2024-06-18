package de.kobich.tictactoe.control;

import java.util.Optional;

import de.kobich.tictactoe.Player;

/**
 * A line on the pitch.
 */
public class Line {
	private final Field[] m_fields;

	public Line(Field field0, Field field1, Field field2) {
		m_fields = new Field[3];
		m_fields[0] = field0;
		m_fields[1] = field1;
		m_fields[2] = field2;
	}

	/**
	 * Returns first free field index
	 * @return Index
	 */
	public Optional<Integer> getFreeIndex() {
		for (int i = 0; i < 3; i ++) {
			if (m_fields[i].getStatus().isFree()) {
				return Optional.of(m_fields[i].getIndex());
			}
		}
		return Optional.empty();
	}

	/**
	 * Returns true if this line is complete (win/loss)
	 */
	public boolean isComplete(Player player) {
		for (int i = 0; i < 3; i ++) {
			if (!m_fields[i].getStatus().belongsTo(player)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if in this line must be set. 
	 * @param player USER, COMPUTER
	 */
	public boolean isThreaten(Player player) {
		int number = 0;

		for (int i = 0; i < 3; i ++) {
			if (m_fields[i].getStatus().belongsTo(player)) {
				number ++;
			}
			else if (!m_fields[i].getStatus().isFree()) {
				// line is already occupied by other player
				return false;
			}
		}
		return number == 2;
	}

	public int[] getIndizes() {
		int[] indizes = new int[3];
		for (int i = 0; i < 3; i ++) {
			indizes[i] = m_fields[i].getIndex();
		}
		return indizes;
	}

}