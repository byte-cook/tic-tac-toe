package de.kobich.tictactoe;

public enum GameState {
	OPEN, DRAW, USER_WINS, COMPUTER_WINS;
	
	public boolean isComputerWins() {
		return COMPUTER_WINS.equals(this);
	}
	public boolean isUserWins() {
		return USER_WINS.equals(this);
	}
	public boolean isDraw() {
		return DRAW.equals(this);
	}
	public boolean isOpen() {
		return OPEN.equals(this);
	}
	
}
