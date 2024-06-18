package de.kobich.tictactoe.control;

import de.kobich.tictactoe.GameState;

/**
 * Calculation result.
 */
public class CalculationResult {
	private static final int NO_INDEX = -1;

	private final GameState m_whoWins;
	private final int m_index;
	private final Line m_winningLine;

	public static CalculationResult createDraw() {
		return new CalculationResult(GameState.DRAW, NO_INDEX, null);
	}
	public static CalculationResult createDraw(int index) {
		return new CalculationResult(GameState.DRAW, index, null);
	}
	
	public static CalculationResult createIndefinite(int index) {
		return new CalculationResult(GameState.OPEN, index, null);
	}
	
	public static CalculationResult createUserWins(Line winningLine) {
		return new CalculationResult(GameState.USER_WINS, NO_INDEX, winningLine);
	}
	
	public static CalculationResult createComputerWins(int index, Line winningLine) {
		return new CalculationResult(GameState.COMPUTER_WINS, index, winningLine);
	}
	
	private CalculationResult(GameState whoWins, int index, Line winningLine) {
		this.m_whoWins = whoWins;
		this.m_index = index;
		this.m_winningLine = winningLine;
	}

	public GameState getWhoWins() {
		return m_whoWins;
	}

	public int getIndex() {
		return m_index;
	}
	
	public boolean hasIndex() {
		return NO_INDEX != m_index;
	}

	public Line getWinningLine() {
		return m_winningLine;
	}
}