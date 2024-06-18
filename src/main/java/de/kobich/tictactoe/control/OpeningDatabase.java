package de.kobich.tictactoe.control;

import java.util.List;

import de.kobich.tictactoe.Player;

/**
 * Opening database of the first 3 moves.
 */
public class OpeningDatabase {
	// 7 8 9
	// 4 5 6
	// 1 2 3
	private static final Integer CENTER = 5;
	private static final List<Integer> CORNERS = List.of(1, 3, 7, 9);
	private static final List<Integer> EDGES = List.of(2, 4, 6, 8);
	
	private final Field[] m_fields;

	public OpeningDatabase(Pitch pitch) {
		m_fields = pitch.getFields();
	}

	/**
	 * Returns the field on which the computer sets. 
	 * NOTE: only supports the first three moves.
	 * @return number between 1 and 9
	 */
	public int getComputerField(GameProperties properties) {
		// 1. move
		if (properties.getNumberMoves() == 0) {
			return (int) (Math.random() * 9 + 1);
		}
		// 2. move
		else if (properties.getNumberMoves() == 1) {
			// center
			if (isSelected(5, Player.USER)) {
				int[] posFields = {1, 3, 7, 9};
				return posFields[(int) (Math.random() * 4)];
			}
			// edge
			else if (isSelected(2, Player.USER)) {
				int[] posFields = {1, 3, 5, 8};
				return posFields[(int) (Math.random() * 4)];
			}
			else if (isSelected(4, Player.USER)) {
				int[] posFields = {1, 5, 6, 7};
				return posFields[(int) (Math.random() * 4)];
			}
			else if (isSelected(6, Player.USER)) {
				int[] posFields = {3, 4, 5, 9};
				return posFields[(int) (Math.random() * 4)];
			}
			else if (isSelected(8, Player.USER)) {
				int[] posFields = {2, 5, 7, 9};
				return posFields[(int) (Math.random() * 4)];
			}
			// corner
			else {
				return 5;
			}
		}
		// 3. move
		else if (properties.getNumberMoves() == 2) {
			// 1. move: computer
			int computerIndex = getIndex(Player.COMPUTER);
			// 2. move: user
			int userIndex = getIndex(Player.USER);
			
			// 1. move center
			if (computerIndex == CENTER) {
				// 2. move corner
				if (CORNERS.contains(userIndex)) {
					return 10 - userIndex;
				}
				// 2. move edge
				else if (EDGES.contains(userIndex)) {
					return (userIndex != 4) ? userIndex + 1 : 8;
				}
			}
			// 1. move corner
			else if (CORNERS.contains(computerIndex)) {
				// 2. move center
				if (userIndex == CENTER) {
					if (computerIndex == 3 || computerIndex == 9) {
						int[] posFields = {11 - computerIndex, 4, 10 - computerIndex};
						return posFields[(int) (Math.random() * 3)];
					}
					else {
						int[] posFields = {9 - computerIndex, 6, 10 - computerIndex};
						return posFields[(int) (Math.random() * 3)];
					}
				}
				// 2. move corner
				else if (CORNERS.contains(userIndex)) {
					// opposite
					if (computerIndex + userIndex == 10) {
						if (computerIndex == 1 || computerIndex == 9) {
							int[] posFields = {3, 7};
							return posFields[(int) (Math.random() * 2)];
						}
						else {
							int[] posFields = {1, 9};
							return posFields[(int) (Math.random() * 2)];
						}
					}
					else {
						return 10 - computerIndex;
					}
				}
				// 2. move edge
				else if (EDGES.contains(userIndex)) {
					return 5;
				}
			}
			// 1. move edge
			else if (EDGES.contains(computerIndex)) {
				// 2. move center
				if (userIndex == CENTER) {
					if (computerIndex == 2) {
						int[] posFields = {4, 6, 7, 9};
						return posFields[(int) (Math.random() * 4)];
					}
					else if (computerIndex == 4) {
						int[] posFields = {2, 3, 8, 9};
						return posFields[(int) (Math.random() * 4)];
					}
					else if (computerIndex == 6) {
						int[] posFields = {1, 2, 7, 8};
						return posFields[(int) (Math.random() * 4)];
					}
					else if (computerIndex == 8) {
						int[] posFields = {1, 3, 4, 6};
						return posFields[(int) (Math.random() * 4)];
					}
				}
				// 2. move corner
				else if (CORNERS.contains(userIndex)) {
					if (computerIndex == 2) {
						if (userIndex > 5) {
							return userIndex - 6;
						}
						else {
							int[] posFields = {7, 9};
							return posFields[(int) (Math.random() * 2)];
						}
					}
					else if (computerIndex == 4) {
						if (userIndex % 3 == 0) {
							return userIndex - 2;
						}
						else {
							int[] posFields = {3, 9};
							return posFields[(int) (Math.random() * 2)];
						}
					}
					else if (computerIndex == 6) {
						if (userIndex % 3 == 0) {
							int[] posFields = {1, 7};
							return posFields[(int) (Math.random() * 2)];
						}
						else {
							return userIndex + 2;
						}
					}
					else if (computerIndex == 8) {
						if (userIndex > 5) {
							int[] posFields = {1, 3};
							return posFields[(int) (Math.random() * 2)];
						}
						else {
							return userIndex + 6;
						}
					}
				}
				// 2. move edge
				else if (EDGES.contains(userIndex)) {
					// opposite
					if (computerIndex + userIndex == 10) {
						if (userIndex == 2) {
							int[] posFields = {1, 3, 4, 6};
							return posFields[(int) (Math.random() * 4)];
						}
						else if (userIndex == 8) {
							int[] posFields = {4, 6, 7, 9};
							return posFields[(int) (Math.random() * 4)];
						}
						else if (userIndex == 4) {
							int[] posFields = {1, 3, 7, 8};
							return posFields[(int) (Math.random() * 4)];
						}
						else if (userIndex == 6) {
							int[] posFields = {2, 3, 8, 9};
							return posFields[(int) (Math.random() * 4)];
						}
					}
					else {
						return 5;
					}
				}
			}
		}
		throw new IllegalStateException("Opening DB only support three moves");
	}

	/**
	 * Returns index of the field that is occupied by who
	 * @param who USER, COMPUTER
	 */
	private int getIndex(Player who) {
		for (int i = 1; i < 10; i ++) {
			if (m_fields[i].getStatus().belongsTo(who))
				return i;
		}
		throw new IllegalStateException("No field found");
	}

	/**
	 * Returns if field with index i is occupied by who
	 * @param who USER, COMPUTER
	 */
	private boolean isSelected(int i, Player who) {
		return m_fields[i].getStatus().belongsTo(who);
	}

}