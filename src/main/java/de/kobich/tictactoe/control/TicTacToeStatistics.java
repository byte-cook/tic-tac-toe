package de.kobich.tictactoe.control;

import de.kobich.tictactoe.GameState;

/**
 * Global statistics for all games.
 */
public class TicTacToeStatistics {
	private int m_nGames;
	private int m_nComputerWins;
	private int m_nUserWins;
	private int m_nDraws;

	public TicTacToeStatistics() {
		m_nGames = 0;
		m_nComputerWins = 0;
		m_nUserWins = 0;
		m_nDraws = 0;
	}

	public void setWhoWins(GameState whoWins) {
		m_nGames ++;

		switch (whoWins) {
			case COMPUTER_WINS:
				m_nComputerWins ++;
				break;
			case DRAW:
				m_nDraws ++;
				break;
			case USER_WINS:
				m_nUserWins ++;
				break;
			case OPEN:
				break;
		}
	}

	public int getNumberGames() {
		return m_nGames;
	}

	public int getNumberUserWins() {
		return m_nUserWins;
	}

	public int getNumberComputerWins() {
		return m_nComputerWins;
	}

	public int getNumberDraws() {
		return m_nDraws;
	}

}