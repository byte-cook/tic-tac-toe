package de.kobich.tictactoe;

public enum FieldState {
	FREE, USER, COMPUTER, USER_WINS, COMPUTER_WINS;
	
	public boolean isFree() {
		return FREE.equals(this);
	}
	public boolean isUser() {
		return USER.equals(this);
	}
	public boolean isComputer() {
		return COMPUTER.equals(this);
	}
	
	public boolean belongsTo(Player player) {
		switch (this) {
			case USER:
			case USER_WINS:
				return Player.USER.equals(player);
			case COMPUTER:
			case COMPUTER_WINS:
				return Player.COMPUTER.equals(player);
			case FREE:
			default:
				return false;
		}
	}
	
	public char toChar() {
		switch (this) {
			case USER:
			case USER_WINS:
				return 'X';
			case COMPUTER:
			case COMPUTER_WINS:
				return 'O';
			case FREE:
			default:
				return '-';
		}
	}
	
}
