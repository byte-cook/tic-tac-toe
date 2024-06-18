package de.kobich.tictactoe.control;

/**
 * Represents a pitch as string.
 */
public class PitchState {
	private final String line1;
	private final String line2;
	private final String line3;
	
	public static PitchState of(Pitch pitch) {
		Field[] fields = pitch.getFields();
		String line1 = getLine(fields[7], fields[8], fields[9]);
		String line2 = getLine(fields[4], fields[5], fields[6]);
		String line3 = getLine(fields[1], fields[2], fields[3]);
		return new PitchState(line1, line2, line3);
	}
	
	private static String getLine(Field f1, Field f2, Field f3) {
		return String.format("%c %c %c", f1.getStatus().toChar(), f2.getStatus().toChar(), f3.getStatus().toChar());
	}
	
	private PitchState(String line1, String line2, String line3) {
		this.line1 = line1;
		this.line2 = line2;
		this.line3 = line3;
	}
	
	public PitchState append(Pitch pitch) {
		Field[] fields = pitch.getFields();
		String line1 = String.format("%s    %s", this.line1, getLine(fields[7], fields[8], fields[9]));
		String line2 = String.format("%s    %s", this.line2, getLine(fields[4], fields[5], fields[6]));
		String line3 = String.format("%s    %s", this.line3, getLine(fields[1], fields[2], fields[3]));
		return new PitchState(line1, line2, line3);
	}
	
	public StringBuilder toStringBuilder() {
		StringBuilder sb = new StringBuilder();
		sb.append(line1);
		sb.append("\n");
		sb.append(line2);
		sb.append("\n");
		sb.append(line3);
		sb.append("\n");
		return sb;
	}
	
}
