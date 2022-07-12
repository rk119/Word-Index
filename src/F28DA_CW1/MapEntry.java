package F28DA_CW1;

import java.util.LinkedList;

/**
 * @author Riffat Khan
 *
 * This class is used to create an entry for storing each word with its filename and 
 * positions (lines) in the file in the form a linked list.
 */

public class MapEntry {
	
	// declaring variables
	private String word;
	private LinkedList<IPosition> positions;
	
	// this variable is used by the HashWordMap to represent a deleted entry
	private boolean DEFUNCT = false;
	
	// constructor for making a new entry in the map for the word
	public MapEntry(String word) {
		this.word = word;
		positions = new LinkedList<>();
	}
	
	// constructor for making a new entry in the map for the word and its given position
	public MapEntry(String word,IPosition position) {
		this.word = word;
		positions = new LinkedList<>();
		positions.add(position);
	}
	
	// getter method to return the private variable word
	public String getMapWord() {
		return word;
	}
	
	// getter method to return the private variable positions
	public LinkedList<IPosition> getPositions() {
		return positions;
	}
	
	// adds a new position in the linked list of positions
	public void addPosition(IPosition position) {
		positions.add(position);
	}
	
	// removes a position from the linked list of positions
	public void removePosition(IPosition position) {
		positions.remove(position);
	}
	
	// getter method to return the private variable defunct
	public boolean getDEFUNCT() {
		return DEFUNCT;
	}
	
	// setter method to set the private variable defunct
	public void setDEFUNCT(boolean setBool) {
		DEFUNCT = setBool;
	}
	
	// Reference -> https://stackoverflow.com/questions/17533876/in-java-how-can-i-check-if-an-object-is-in-a-linked-list
	public boolean equals(Object that) {
		if (that instanceof MapEntry) { 
			MapEntry thatWord = (MapEntry) that;
			return word.equalsIgnoreCase(thatWord.word);
		}
		else
			return false;
	}
}
