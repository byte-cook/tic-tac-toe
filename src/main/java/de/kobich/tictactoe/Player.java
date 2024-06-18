package de.kobich.tictactoe;

public enum Player {
	USER, COMPUTER;
	
	public boolean isComputer() {
		return COMPUTER.equals(this);
	}
	public boolean isUser() {
		return USER.equals(this);
	}
	
	public Player getOther() {
		switch (this) {
			case USER:
				return COMPUTER;
			case COMPUTER:
			default:
				return USER;
		}
	}
}
