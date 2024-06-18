package de.kobich.tictactoe.control;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import de.kobich.tictactoe.FieldState;
import de.kobich.tictactoe.Player;
import de.kobich.tictactoe.control.Calculator.ActionHint;

/**
 * Calculates the field for computer's next move.
 */
public class TicTacToeController {
	private static final Logger LOGGER = Logger.getLogger(TicTacToeController.class.getName());
	private final Pitch m_pitch;
	private final OpeningDatabase m_openingDB;
	private final Calculator m_calculator;

	public TicTacToeController() {
		m_pitch = new Pitch();
		m_openingDB = new OpeningDatabase(m_pitch);
		m_calculator = new Calculator(m_pitch);
	}

	public Field[] getFields() {
		return (m_pitch != null) ? m_pitch.getFields() : null;
	}

	/**
	 * Returns the field on which the computer sets
	 */
	public CalculationResult getComputerField(GameProperties properties) {
		// use opening DB:
		// if strength is high AND number moves < 3
		if (properties.getStrength().isHigh() && properties.getNumberMoves() < 3) {
			int index = m_openingDB.getComputerField(properties);
			return CalculationResult.createIndefinite(index);
		}
		// or if strength is low AND number moves == 0
		else if (properties.getStrength().isLow() && properties.getNumberMoves() == 0) {
			int index = m_openingDB.getComputerField(properties);
			return CalculationResult.createIndefinite(index);
		}

		// user has already won
		Line winningLine = m_pitch.getWinningLine(Player.USER).orElse(null);
		if (winningLine != null) {
			return CalculationResult.createUserWins(winningLine);
		}

		// computer wins
		winningLine = m_pitch.getThreatenedLine(Player.COMPUTER).orElse(null);
		if (winningLine != null) {
			return CalculationResult.createComputerWins(winningLine.getFreeIndex().orElseThrow(), winningLine);
		}

		// check for draw
		if (properties.getNumberMoves() == 9) {
			return CalculationResult.createDraw();
		}
		// check for last field
		else if (properties.getNumberMoves() == 8) {
			return CalculationResult.createDraw(m_pitch.getFirstFreeField().orElseThrow());
		}

		// computer must set
		Line threatenedLine = m_pitch.getThreatenedLine(Player.USER).orElse(null);
		if (threatenedLine != null) {
			return CalculationResult.createIndefinite(threatenedLine.getFreeIndex().orElseThrow());
		}

		// calculation
		List<Integer> possibleFields = new ArrayList<>();

		for (int i = 1; i < 10; i ++) {
			Field[] fields = m_pitch.getFields();
			if (fields[i].getStatus().isFree()) {
				try {
					// set on each free field
					fields[i].setStatus(FieldState.COMPUTER);
					PitchState pitchState = PitchState.of(m_pitch);
					LOGGER.info(String.format("=== Test field: %d \n%s", i, pitchState.toStringBuilder()));
	
					ActionHint calc = m_calculator.calculate(pitchState);
					switch (calc) {
						case DO:
							return CalculationResult.createIndefinite(i);
						case WAIT:
							possibleFields.add(i);
							break;
						case DO_NOT:
							break;
					}
					LOGGER.info(String.format("=== Test field: %d -> %s", i, calc));
				}
				finally {
					fields[i].setStatus(FieldState.FREE);
				}
			}
		}
		LOGGER.info("Possible fields: " + possibleFields);
		
		if (possibleFields.size() > 0) {
			int index = ((Integer) possibleFields.get((int) (Math.random() * possibleFields.size()))).intValue();
			return CalculationResult.createIndefinite(index);
		}
		// should not happen
		throw new IllegalStateException("No possible field found");
	}

}