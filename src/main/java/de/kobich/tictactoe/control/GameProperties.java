package de.kobich.tictactoe.control;

import de.kobich.tictactoe.GameState;
import de.kobich.tictactoe.Player;
import de.kobich.tictactoe.PlayingStrength;

/**
 * Properties of a game.
 */
public class GameProperties {
	private final Player m_whoStarts;
	private final PlayingStrength m_strength;
	private GameState m_gameState;
	private int m_nMoves;
	private Line m_winningLine;

	public GameProperties(Player whoStarts, PlayingStrength strength) {
		m_whoStarts = whoStarts;
		m_strength = strength;
		m_gameState = GameState.OPEN;
		m_nMoves = 0;
		m_winningLine = null;
	}

	public void incrementMove() {
		m_nMoves ++;
	}

	public void setGameState(GameState state) {
		m_gameState = state;
	}

	public void setWinningLine(Line winningLine) {
		m_winningLine = winningLine;
	}

	/**
	 * Returns if the game is still running
	 */
	public boolean isOpen() {
		return m_gameState.isOpen();
	}

	public int getNumberMoves() {
		return m_nMoves;
	}

	public Player getWhoStarts() {
		return m_whoStarts;
	}

	public PlayingStrength getStrength() {
		return m_strength;
	}

	public Line getWinningLine() {
		return m_winningLine;
	}

	public GameState getGameState() {
		return m_gameState;
	}

}