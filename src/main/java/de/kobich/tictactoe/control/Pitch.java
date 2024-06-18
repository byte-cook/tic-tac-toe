package de.kobich.tictactoe.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.kobich.tictactoe.Player;

/**
 * The pitch of the game.
 */
public class Pitch {
	private final Field[] m_fields;
	private final Line[] m_lines;

	public Pitch() {
		// init fields
		m_fields = new Field[10];
		for (int i = 1; i < 10; i ++) {
			m_fields[i] = new Field(i);
		}

		// init lines
		m_lines = new Line[8];
		m_lines[0] = new Line(m_fields[1], m_fields[2], m_fields[3]);
		m_lines[1] = new Line(m_fields[4], m_fields[5], m_fields[6]);
		m_lines[2] = new Line(m_fields[7], m_fields[8], m_fields[9]);
		
		m_lines[3] = new Line(m_fields[1], m_fields[4], m_fields[7]);
		m_lines[4] = new Line(m_fields[2], m_fields[5], m_fields[8]);
		m_lines[5] = new Line(m_fields[3], m_fields[6], m_fields[9]);

		m_lines[6] = new Line(m_fields[1], m_fields[5], m_fields[9]);
		m_lines[7] = new Line(m_fields[3], m_fields[5], m_fields[7]);
	}

	/**
	 * Returns the winning line for the given player 
	 */
	public Optional<Line> getWinningLine(Player player) {
		for (int i = 0; i < 8; i ++) {
			if (m_lines[i].isComplete(player)) {
				return Optional.of(m_lines[i]);
			}
		}
		return Optional.empty();
	}

	/**
	 * Returns a threatened line for the given player
	 */
	public Optional<Line> getThreatenedLine(Player player) {
		for (int i = 0; i < 8; i ++) {
			if (m_lines[i].isThreaten(player)) {
				return Optional.of(m_lines[i]);
			}
		}
		return Optional.empty();
	}

	/**
	 * Returns field indexes of threatened lines for the given player
	 */
	public List<Integer> getNumberThreats(Player player) {
		List<Integer> nThreats = new ArrayList<>();
		for (int i = 0; i < 8; i ++) {
			if (m_lines[i].isThreaten(player)) {
				m_lines[i].getFreeIndex().ifPresent(nThreats::add);
			}
		}
		return nThreats;
	}

	public Field[] getFields() {
		return m_fields;
	}
	
	public Optional<Integer> getFirstFreeField() {
		for (int i = 1; i < m_fields.length; i++) {
			if (m_fields[i].getStatus().isFree()) {
				return Optional.of(i);
			}
		}
		return Optional.empty();
	}

}