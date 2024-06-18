package de.kobich.tictactoe;

public enum PlayingStrength {
	HIGH, LOW;

	public boolean isHigh() {
		return HIGH.equals(this);
	}
	public boolean isLow() {
		return LOW.equals(this);
	}
}
