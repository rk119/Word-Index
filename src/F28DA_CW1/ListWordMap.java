package F28DA_CW1;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Riffat Khan
 * 
 *	This class performs the implementation of the map in a linked list format.
 *	Each node in the linked list stores the word and its position as an entry
 *	with the help of the MapEntry class provided. The positions of each word is stored
 *	in a linked list format as well. The following methods perform the basic functionality
 *	of a linked list based map.
 */

public class ListWordMap implements IWordMap {
	
	// declaring the map
	private LinkedList<MapEntry> wordPossMap;
	
	// constructor initializing the map
	public ListWordMap() {
		wordPossMap = new LinkedList<>();
	}
	
	/**
	 * This method adds a new word or a new position if the word already exists in the map. 
	 */
	@Override
	public void addPos(String word, IPosition pos) {
		// condition to check if the word exists in the map
		if (wordPossMap.contains(new MapEntry(word))) {
			// gets the index if it exists
			int index = wordPossMap.indexOf(new MapEntry(word));
			// adds the new position for that word
			wordPossMap.get(index).addPosition(pos);
		}
		else
			// adds a new word if it does not exist in the map 
			wordPossMap.add(new MapEntry(word, pos));
	}
	
	/**
	 * This method removes a word and its positions from the map. 
	 */
	@Override
	public void removeWord(String word) throws WordException {
		// condition to check if the word exists in the map
		if (wordPossMap.contains(new MapEntry(word))) {
			// gets the index if it exists
			int index = wordPossMap.indexOf(new MapEntry(word));
			// removes the word entirely including its positions
			wordPossMap.remove(index);
		}
		else
			throw new WordException("The word \""+ word + "\" is not present in the map");
	}
	
	/**
	 * This method removes a specified position of a word from the map.
	 */
	@Override
	public void removePos(String word, IPosition pos) throws WordException {
		// condition to check if the word exists in the map
		if (wordPossMap.contains(new MapEntry(word))) {
			// gets the index if it exists
			int index = wordPossMap.indexOf(new MapEntry(word));
			// removes specified position from the list of positions using the method from 
			// the MapEntry class
			wordPossMap.get(index).removePosition(pos);
			// if the word no longer has any positions left
			if (wordPossMap.get(index).getPositions().size() == 0) 
				// remove word entirely
				wordPossMap.remove(index);
		}
		else
			throw new WordException("The word \""+ word + "\" is not present in the map");
	}
	
	/**
	 * This following method returns an iterator to iterate through the words present in the map.
	 */
	@Override
	public Iterator<String> words() {
		return new Iterator<String>() {
			int index = 0;
			
			@Override
			public boolean hasNext() {
				// next word exists if the current index is not out of bounds
				return index < wordPossMap.size();
			}

			@Override
			public String next() {
				// gets next word using its index
				String word = wordPossMap.get(index).getMapWord();
				// increment index for next word
				index++;
				return word;
			}
		};
		
	}
	
	/**
	 * This following method returns an iterator to iterate through the positions of a word in the map.
	 */
	@Override
	public Iterator<IPosition> positions(String word) throws WordException {
		// condition to check if the word exists in the map
		if (wordPossMap.contains(new MapEntry(word))) {
			// gets index of the word
			int index = wordPossMap.indexOf(new MapEntry(word));
			// gets the positions of the word with that index
			LinkedList<IPosition> wordPositions = wordPossMap.get(index).getPositions();
			return wordPositions.iterator();
		}
		else
			throw new WordException("The word \""+ word + "\" is not present in the map");
		
	}
	
	/**
	 * This method returns the number of words present in the map.
	 */
	@Override
	public int numberOfEntries() {
		return wordPossMap.size();
	}
}
