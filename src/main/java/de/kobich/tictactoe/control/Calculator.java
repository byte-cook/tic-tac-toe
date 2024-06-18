package de.kobich.tictactoe.control;

import java.util.List;
import java.util.logging.Logger;

import de.kobich.tictactoe.FieldState;
import de.kobich.tictactoe.Player;

/**
 * Calculates the field on which the computer sets
 */
public class Calculator {
	private static final Logger LOGGER = Logger.getLogger(Calculator.class.getName());
	private final Pitch m_pitch;
	private final Field[] m_fields;
	
	enum ActionHint {
		DO, WAIT, DO_NOT;
	}
	
	public Calculator(Pitch pitch) {
		this.m_pitch = pitch;
		this.m_fields = m_pitch.getFields();
	}

	/**
	 * Evaluates the current game situation
	 */
	public ActionHint calculate(PitchState pitchState) {
		return calculate(Player.COMPUTER, pitchState);
	}

	/**
	 * Evaluates the current game situation
	 * @param lastPlayer the player who made the last move
	 * @return DO, WAIT, NOT
	 */
	private ActionHint calculate(final Player lastPlayer, PitchState pitchState) {
		final Player nextPlayer = lastPlayer.getOther();
		ActionHint returnValue = ActionHint.WAIT;

		// nextPlayer will win
		if (m_pitch.getNumberThreats(nextPlayer).size() >= 1) {
			LOGGER.info("Next player wins: \n" + pitchState.toStringBuilder().toString());
			return getActionHint(nextPlayer);
		}

		List<Integer> threats = m_pitch.getNumberThreats(lastPlayer);
		int threatSize = threats.size();
		// lastPlayer will win
		if (threatSize > 1) {
			LOGGER.info("Last player wins: \n" + pitchState.toStringBuilder().toString());
			return getActionHint(lastPlayer);
		}
		// nextPlayer must set
		else if (threatSize == 1) {
			int index = threats.get(0);
			try {
				m_fields[index].setStatusByPlayer(nextPlayer);
				returnValue = calculate(nextPlayer, pitchState.append(m_pitch));
			}
			finally {
				m_fields[index].setStatus(FieldState.FREE);
			}
		}
		// calculation
		else {
			// number of possible fields
			int nPossibleFields = 0;	
			// number of fields on which the computer should not set
			int nNegativeFields = 0;	

			for (int i = 1; i < 10; i ++) {
				if (m_fields[i].getStatus().isFree()) {
					try {
						m_fields[i].setStatusByPlayer(nextPlayer);
	
						ActionHint nextCalculated = calculate(nextPlayer, pitchState.append(m_pitch));
	
						// user sets next
						if (Player.USER.equals(nextPlayer)) {
							// user can win
							if (nextCalculated.equals(ActionHint.DO_NOT)) {
								LOGGER.info(ActionHint.DO_NOT + ": \n" + pitchState.toStringBuilder().toString());
								return ActionHint.DO_NOT;
							}
						}
						// computer sets next
						else if (Player.COMPUTER.equals(nextPlayer)) {
							// computer can win
							if (m_pitch.getNumberThreats(nextPlayer).size() > 1) {
								LOGGER.info(ActionHint.DO + ": \n" + pitchState.toStringBuilder().toString());
								return ActionHint.DO;
							}
	
							nPossibleFields ++;
	
							// computer can lose
							if (nextCalculated.equals(ActionHint.DO_NOT)) {
								nNegativeFields ++;
							}
						}
					}
					finally {
						m_fields[i].setStatus(FieldState.FREE);
					}
				}
			}

			// check if possible fields are available AND if there are more possible fields than negative ones
			if (nPossibleFields > 0 && nPossibleFields == nNegativeFields) {
				returnValue = ActionHint.DO_NOT;
			}
		}

//		LOGGER.info("\n" + pitchState.toStringBuilder().toString());
		return returnValue;
	}

	/**
	 * Returns DO or NOT depending on if computer wins or not
	 * @param player USER, COMPUTER
	 */
	private ActionHint getActionHint(Player player) {
		return Player.COMPUTER.equals(player) ? ActionHint.DO : ActionHint.DO_NOT;
	}

}